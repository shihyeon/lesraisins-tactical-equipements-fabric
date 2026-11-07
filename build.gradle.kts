import java.util.zip.ZipFile

object BuildConfig {
    val JAVA_VERSION: Int = 21

    val MOD_ID: String = "lrtactical"
    val MOD_VERSION: String = "0.4.4"

    val MINECRAFT_VERSION: String = "1.21.1"
    val PARCHMENT_VERSION: String = "2024.11.17"
    val FABRIC_LOADER_VERSION: String = "0.19.3"
    val FABRIC_API_VERSION: String = "0.116.15+1.21.1"

    val TACZ_VERSION: String = "1.0.0-neo1.2.0+mc1.21.1"
    val FORGE_CONFIG_API_PORT_VERSION: String = "v21.1.6-1.21.1-Fabric"

    val CLOTH_CONFIG_VERSION: String = "15.0.140"
    val MODMENU_VERSION: String = "11.0.3"
    val PLAYER_ANIMATOR_VERSION: String = "2.0.1+1.21.1"
    val JEI_VERSION: String = "19.21.0.247"
}

plugins {
    id("java-library")
    id("net.fabricmc.fabric-loom-remap") version ("1.17.+")
}

base {
    archivesName = "${BuildConfig.MOD_ID}-fabric-${BuildConfig.MINECRAFT_VERSION}"
}

group = "me.xjqsh"
version = BuildConfig.MOD_VERSION

val gameTest: SourceSet by sourceSets.creating {
    compileClasspath += sourceSets.main.get().compileClasspath + sourceSets.main.get().output
    runtimeClasspath += sourceSets.main.get().runtimeClasspath + sourceSets.main.get().output
}

loom {
    accessWidenerPath.set(file("src/main/resources/lrtactical.accesswidener"))

    mods {
        create(BuildConfig.MOD_ID) {
            sourceSet(sourceSets.main.get())
        }
        create("${BuildConfig.MOD_ID}-gametest") {
            sourceSet(gameTest)
        }
    }

    runs {
        named("client") {
            runDir = "run/fabric-client"
        }
        named("server") {
            runDir = "run/fabric-server"
        }
        create("gameTestServer") {
            server()
            configName = "GameTest Server"
            source(gameTest)
            runDir = "run/gametest"
            vmArg("-Dfabric-api.gametest")
            vmArg("-Dfabric-api.gametest.report-file=${layout.buildDirectory.file("gametest/report.xml").get().asFile}")
        }
    }
}

tasks.named<ProcessResources>("processGameTestResources") {
    // Upstream uses unprefixed templates ("empty") from data/lrtactical/structure. Fabric reads the template
    // name as written, which resolves to the minecraft namespace, so the structure is copied there too.
    from("src/gameTest/resources/data/lrtactical/structure") {
        into("data/minecraft/structure")
    }
}

repositories {
    mavenCentral()
    maven("https://maven.parchmentmc.org")
    exclusiveContent {
        forRepository {
            maven("https://api.modrinth.com/maven")
        }
        filter {
            includeGroup("maven.modrinth")
        }
    }
    maven("https://maven.shedaniel.me/")
    maven("https://maven.terraformersmc.com/")
    maven("https://maven.kosmx.dev/")
    maven("https://maven.blamejared.com/")
    maven("https://jitpack.io") {
        content {
            includeGroup("com.github.FiguraMC.luaj")
        }
    }
}

val taczDependency = "maven.modrinth:tacz-refabricated:${BuildConfig.TACZ_VERSION}"

/**
 * TACZ-Refabricated nests its libraries (SimpleBedrockModel, Cardinal Components, LuaJ, ...). SimpleBedrockModel
 * has no published Fabric artifact, so it is compiled against from here. Loom does not load nested jars in the
 * dev runtime, so all of them are put on the dev runtime classpath as well.
 */
val taczNestedJars: List<File> = run {
    val outputDir = layout.buildDirectory.dir("nested-jars/tacz").get().asFile
    if (!outputDir.isDirectory || outputDir.listFiles().isNullOrEmpty()) {
        val taczJar = configurations.detachedConfiguration(dependencies.create(taczDependency))
            .apply { isTransitive = false }
            .singleFile
        outputDir.mkdirs()
        ZipFile(taczJar).use { zip ->
            zip.entries().asSequence()
                .filter { it.name.startsWith("META-INF/jars/") && it.name.endsWith(".jar") }
                .forEach { entry ->
                    val target = File(outputDir, entry.name.substringAfterLast('/'))
                    zip.getInputStream(entry).use { input -> target.outputStream().use { input.copyTo(it) } }
                }
        }
    }
    outputDir.listFiles { file -> file.name.endsWith(".jar") }!!.sortedBy { it.name }
}

fun isFabricMod(jar: File): Boolean = ZipFile(jar).use { it.getEntry("fabric.mod.json") != null }

val simpleBedrockModelJar: File = taczNestedJars.firstOrNull { it.name.startsWith("simplebedrockmodel") }
    ?: throw GradleException("SimpleBedrockModel was not found inside TACZ-Refabricated")

dependencies {
    minecraft("com.mojang:minecraft:${BuildConfig.MINECRAFT_VERSION}")
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${BuildConfig.MINECRAFT_VERSION}:${BuildConfig.PARCHMENT_VERSION}@zip")
    })

    modImplementation("net.fabricmc:fabric-loader:${BuildConfig.FABRIC_LOADER_VERSION}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${BuildConfig.FABRIC_API_VERSION}")

    modImplementation(taczDependency)
    modImplementation("maven.modrinth:forge-config-api-port:${BuildConfig.FORGE_CONFIG_API_PORT_VERSION}")
    modCompileOnly(files(simpleBedrockModelJar))
    taczNestedJars.forEach { jar ->
        if (isFabricMod(jar)) modLocalRuntime(files(jar)) else localRuntime(files(jar))
    }
    // Nested one level deeper, inside SimpleBedrockModel
    localRuntime("com.maydaymemory:mae:1.1.4")
    // Forge Config API Port nests these, but Loom does not put nested jars on the dev runtime classpath
    localRuntime("com.electronwill.night-config:core:3.8.0")
    localRuntime("com.electronwill.night-config:toml:3.8.0")

    compileOnly("com.github.FiguraMC.luaj:luaj-core:3.0.8-figura")
    compileOnly("com.github.FiguraMC.luaj:luaj-jse:3.0.8-figura")
    compileOnly("com.google.code.findbugs:jsr305:3.0.2")

    modCompileOnly("me.shedaniel.cloth:cloth-config-fabric:${BuildConfig.CLOTH_CONFIG_VERSION}") {
        exclude(group = "net.fabricmc.fabric-api")
    }
    modCompileOnly("com.terraformersmc:modmenu:${BuildConfig.MODMENU_VERSION}")
    modCompileOnly("dev.kosmx.player-anim:player-animation-lib-fabric:${BuildConfig.PLAYER_ANIMATOR_VERSION}")
    modCompileOnly("mezz.jei:jei-${BuildConfig.MINECRAFT_VERSION}-common-api:${BuildConfig.JEI_VERSION}")
    modCompileOnly("mezz.jei:jei-${BuildConfig.MINECRAFT_VERSION}-fabric-api:${BuildConfig.JEI_VERSION}")

    // Run the dev game with the optional integrations: ./gradlew runClient -PwithOptionalMods
    if (providers.gradleProperty("withOptionalMods").isPresent) {
        modLocalRuntime("me.shedaniel.cloth:cloth-config-fabric:${BuildConfig.CLOTH_CONFIG_VERSION}") {
            exclude(group = "net.fabricmc.fabric-api")
        }
        modLocalRuntime("com.terraformersmc:modmenu:${BuildConfig.MODMENU_VERSION}")
        modLocalRuntime("dev.kosmx.player-anim:player-animation-lib-fabric:${BuildConfig.PLAYER_ANIMATOR_VERSION}")
        modLocalRuntime("mezz.jei:jei-${BuildConfig.MINECRAFT_VERSION}-fabric:${BuildConfig.JEI_VERSION}")
    }
}

tasks {
    processResources {
        val properties = mapOf(
            "version" to project.version,
            "minecraft_version" to BuildConfig.MINECRAFT_VERSION,
            "fabric_loader_version" to BuildConfig.FABRIC_LOADER_VERSION,
        )
        inputs.properties(properties)
        // Plain token replacement: injected interface keys such as class_1792$class_1793 contain '$'
        filesMatching("fabric.mod.json") {
            filter { line ->
                properties.entries.fold(line) { text, (key, value) -> text.replace("\${$key}", value.toString()) }
            }
        }
    }

    remapJar {
        destinationDirectory.set(layout.buildDirectory.dir("mods"))
    }
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(BuildConfig.JAVA_VERSION)
    withSourcesJar()
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.release.set(BuildConfig.JAVA_VERSION)
}

/**
 * Points upstream (NeoForge) sources at the Fabric compatibility layer. Run it after merging upstream.
 *
 * Every `net.neoforged.X` reference becomes `kr.shihyeon.mods.lrtactical.neoforged.X`, which mirrors the NeoForge
 * API on top of Fabric. Classes that Forge Config API Port provides under their real names are left alone. A few
 * NeoForge-only annotations, constructors and static methods are rewritten to their Fabric counterparts.
 * It is idempotent and keeps each file's line endings.
 */
tasks.register("portUpstream") {
    group = "lrtactical"
    description = "Rewrites upstream NeoForge sources to use the Fabric compatibility layer."

    val sourceDirs = listOf(file("src/main/java/me"), file("src/gameTest/java"))

    doLast {
        val shimPrefix = "kr.shihyeon.mods.lrtactical.neoforged."
        // Provided by Forge Config API Port with the original package names
        val keep = listOf("net.neoforged.neoforge.common.ModConfigSpec", "net.neoforged.fml.config.ModConfig")
        val neoforgeName = Regex("""\bnet\.neoforged\.([A-Za-z0-9_.]*[A-Za-z0-9_*])""")
        val importLine = Regex("""^import [^;]+;\r?$""", RegexOption.MULTILINE)
        val environment = listOf("net.fabricmc.api.EnvType", "net.fabricmc.api.Environment")

        // pattern, replacement, imports the replacement needs
        val rewrites = listOf(
            // NeoForge strips @OnlyIn members on the other side; Fabric Loader does the same only for @Environment
            Triple(Regex("""@OnlyIn\(\s*(?:value\s*=\s*)?Dist\.CLIENT\s*\)"""), "@Environment(EnvType.CLIENT)", environment),
            Triple(Regex("""@OnlyIn\(\s*(?:value\s*=\s*)?Dist\.DEDICATED_SERVER\s*\)"""), "@Environment(EnvType.SERVER)", environment),
            // CreativeModeTab.builder() without arguments only exists on NeoForge
            Triple(Regex("""\bCreativeModeTab\.builder\(\)"""), "FabricItemGroup.builder()",
                listOf("net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup")),
            // KeyMapping constructors taking a conflict context and modifier only exist on NeoForge
            Triple(Regex("""(new KeyMapping\(\s*"[^"]*",)\s*KeyConflictContext\.\w+,\s*KeyModifier\.\w+,"""), "\$1", emptyList()),
        )

        fun addImport(text: String, name: String): String {
            if (Regex("""^import ${Regex.escape(name)};""", RegexOption.MULTILINE).containsMatchIn(text)) {
                return text
            }
            val first = importLine.find(text) ?: return text
            val newline = if ("\r\n" in text) "\r\n" else "\n"
            return text.substring(0, first.range.first) + "import $name;$newline" + text.substring(first.range.first)
        }

        var updated = 0
        sourceDirs.filter { it.isDirectory }.forEach { dir ->
            dir.walkTopDown().filter { it.isFile && it.extension == "java" }.forEach { source ->
                val original = source.readText(Charsets.UTF_8)
                var text = neoforgeName.replace(original) { match ->
                    if (keep.any { match.value.startsWith(it) }) match.value else shimPrefix + match.groupValues[1]
                }
                for ((pattern, replacement, imports) in rewrites) {
                    if (pattern.containsMatchIn(text)) {
                        text = pattern.replace(text, replacement)
                        imports.forEach { text = addImport(text, it) }
                    }
                }
                if (text != original) {
                    source.writeText(text, Charsets.UTF_8)
                    logger.lifecycle("updated {}", source.relativeTo(projectDir))
                    updated++
                }
            }
        }
        logger.lifecycle("{} file(s) updated", updated)
    }
}
