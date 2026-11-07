package kr.shihyeon.mods.lrtactical.event;

import com.mojang.logging.LogUtils;
import kr.shihyeon.mods.lrtactical.neoforged.api.distmarker.Dist;
import kr.shihyeon.mods.lrtactical.neoforged.bus.api.IEventBus;
import kr.shihyeon.mods.lrtactical.neoforged.fml.common.EventBusSubscriber;
import net.fabricmc.loader.api.ModContainer;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedHashSet;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Finds {@link EventBusSubscriber} classes in the mod jar, the way NeoForge does at load time.
 * Class files are read with ASM first, so classes for the other side are never loaded.
 */
public final class EventSubscriberScanner {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String ANNOTATION = Type.getDescriptor(EventBusSubscriber.class);
    private static final String DIST = Type.getDescriptor(Dist.class);
    private static final String BUS = Type.getDescriptor(EventBusSubscriber.Bus.class);

    public static void registerAll(ModContainer mod, String packagePrefix, IEventBus modBus, IEventBus gameBus) {
        Dist current = Dist.current();
        String pathPrefix = packagePrefix.replace('.', '/') + '/';
        List<Subscriber> subscribers = scan(mod, pathPrefix);
        int registered = 0;
        for (Subscriber subscriber : subscribers) {
            if (!subscriber.dists.contains(current)) {
                continue;
            }
            registered++;
            Class<?> type;
            try {
                type = Class.forName(subscriber.className, false, EventSubscriberScanner.class.getClassLoader());
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("Cannot load event subscriber " + subscriber.className, e);
            }
            (subscriber.bus == EventBusSubscriber.Bus.MOD ? modBus : gameBus).register(type);
        }
        LOGGER.info("Registered {} of {} event subscribers found in {} ({})", registered, subscribers.size(), packagePrefix, current);
        if (subscribers.isEmpty()) {
            LOGGER.warn("No event subscribers found; searched {}", roots(mod));
        }
    }

    /**
     * The mod roots, plus the directory the scanner's own classes come from. In a dev run Fabric only
     * reports the resources directory as the mod root, while classes live in a separate output directory.
     */
    private static Set<Path> roots(ModContainer mod) {
        Set<Path> roots = new LinkedHashSet<>(mod.getRootPaths());
        String resource = EventSubscriberScanner.class.getName().replace('.', '/') + ".class";
        URL url = EventSubscriberScanner.class.getClassLoader().getResource(resource);
        if (url != null && "file".equals(url.getProtocol())) {
            try {
                Path root = Path.of(url.toURI());
                for (int i = 0; i < resource.split("/").length; i++) {
                    root = root.getParent();
                }
                roots.add(root);
            } catch (URISyntaxException e) {
                LOGGER.warn("Cannot resolve class output directory from {}", url, e);
            }
        }
        return roots;
    }

    private static List<Subscriber> scan(ModContainer mod, String pathPrefix) {
        List<Subscriber> found = new ArrayList<>();
        for (Path root : roots(mod)) {
            Path base = root.resolve(pathPrefix);
            if (!Files.isDirectory(base)) {
                continue;
            }
            try (Stream<Path> files = Files.walk(base)) {
                files.filter(path -> path.toString().endsWith(".class")).sorted().forEach(path -> {
                    Subscriber subscriber = read(path);
                    if (subscriber != null) {
                        found.add(subscriber);
                    }
                });
            } catch (IOException e) {
                throw new UncheckedIOException("Cannot scan " + base, e);
            }
        }
        return found;
    }

    private static Subscriber read(Path classFile) {
        try (InputStream input = Files.newInputStream(classFile)) {
            SubscriberVisitor visitor = new SubscriberVisitor();
            new ClassReader(input).accept(visitor, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
            if (!visitor.annotated) {
                return null;
            }
            return new Subscriber(visitor.className.replace('/', '.'),
                    visitor.dists.isEmpty() ? EnumSet.allOf(Dist.class) : visitor.dists, visitor.bus);
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot read " + classFile, e);
        }
    }

    private record Subscriber(String className, Set<Dist> dists, EventBusSubscriber.Bus bus) {
    }

    private static final class SubscriberVisitor extends ClassVisitor {
        private String className;
        private boolean annotated;
        private final EnumSet<Dist> dists = EnumSet.noneOf(Dist.class);
        private EventBusSubscriber.Bus bus = EventBusSubscriber.Bus.GAME;

        SubscriberVisitor() {
            super(Opcodes.ASM9);
        }

        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            this.className = name;
        }

        @Override
        public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
            if (!ANNOTATION.equals(descriptor)) {
                return null;
            }
            annotated = true;
            return new AnnotationVisitor(Opcodes.ASM9) {
                @Override
                public void visitEnum(String name, String enumDescriptor, String value) {
                    if ("bus".equals(name) && BUS.equals(enumDescriptor)) {
                        bus = EventBusSubscriber.Bus.valueOf(value);
                    }
                }

                @Override
                public AnnotationVisitor visitArray(String name) {
                    if (!"value".equals(name)) {
                        return null;
                    }
                    return new AnnotationVisitor(Opcodes.ASM9) {
                        @Override
                        public void visitEnum(String ignored, String enumDescriptor, String value) {
                            if (DIST.equals(enumDescriptor)) {
                                dists.add(Dist.valueOf(value));
                            }
                        }
                    };
                }
            };
        }
    }

    private EventSubscriberScanner() {
    }
}
