import net.minecrell.pluginyml.bukkit.BukkitPluginDescription.Permission

plugins {
    id("java")
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.freefair.lombok") version "8.6"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
    id("com.diffplug.spotless") version "6.25.0"
}

spotless {
    java {
        endWithNewline()
        indentWithSpaces(4)
        removeUnusedImports()
        trimTrailingWhitespace()
        targetExclude("build/generated/**/*")
    }

    kotlinGradle {
        endWithNewline()
        indentWithSpaces(4)
        trimTrailingWhitespace()
    }
}

group = "ac.grim.grimac"
version = "2.3.68"
description = "Libre simulation anticheat designed for 1.21 with 1.8-1.21 support, powered by PacketEvents 2.0."

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(18))
}

// Set to false for debug builds
// You cannot live reload classes if the jar relocates dependencies
// Checks Project properties -> environment variable -> defaults true
val relocate: Boolean = project.findProperty("relocate")?.toString()?.toBoolean()
    ?: System.getenv("RELOCATE_JAR")?.toBoolean()
    ?: true

repositories {
    mavenLocal()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") // Spigot
    maven("https://jitpack.io/") { // Grim API
        content {
            includeGroup("com.github.grimanticheat")
        }
    }
    maven("https://repo.viaversion.com") // ViaVersion
    maven("https://repo.aikar.co/content/groups/aikar/") // ACF
    maven("https://nexus.scarsz.me/content/repositories/releases") // Configuralize
    maven("https://repo.opencollab.dev/maven-snapshots/") // Floodgate
    maven("https://repo.opencollab.dev/maven-releases/") // Cumulus (for Floodgate)
    maven("https://repo.codemc.io/repository/maven-releases/") // PacketEvents
    maven("https://repo.codemc.io/repository/maven-snapshots/")
    mavenCentral()
    // FastUtil, Discord-Webhooks
}

// Add JMH configuration
configurations {
    create("jmh")
    create("jmhAnnotationProcessor")
}


dependencies {
    implementation("com.github.retrooper:packetevents-spigot:2.6.1-SNAPSHOT")
    implementation("co.aikar:acf-paper:0.5.1-SNAPSHOT")
    implementation("club.minnced:discord-webhooks:0.8.0") // Newer versions include kotlin-stdlib, which leads to incompatibility with plugins that use Kotlin
    implementation("it.unimi.dsi:fastutil:8.5.15")
    implementation("github.scarsz:configuralize:1.4.0")

    //implementation("com.github.grimanticheat:grimapi:1193c4fa41")
    // Used for local testing: implementation("ac.grim.grimac:GRIMAPI:1.0")
    implementation("com.github.grimanticheat:grimapi:fc5634e444")

    implementation("org.jetbrains:annotations:24.1.0")
    compileOnly("org.geysermc.floodgate:api:2.0-SNAPSHOT")
    compileOnly("org.spigotmc:spigot-api:1.18.2-R0.1-SNAPSHOT")
    compileOnly("com.viaversion:viaversion-api:5.0.4-SNAPSHOT")
    //
    compileOnly("io.netty:netty-all:4.1.85.Final")

    // Replace jmhImplementation with the new configuration
    "jmh"("org.openjdk.jmh:jmh-core:1.37")
    "jmhAnnotationProcessor"("org.openjdk.jmh:jmh-generator-annprocess:1.37")

    implementation(files("./nalim.jar"))
}

bukkit {
    name = "GrimAC"
    author = "GrimAC"
    main = "ac.grim.grimac.GrimAC"
    website = "https://grim.ac/"
    apiVersion = "1.13"
    foliaSupported = true

    softDepend = listOf(
        "ProtocolLib",
        "ProtocolSupport",
        "Essentials",
        "ViaVersion",
        "ViaBackwards",
        "ViaRewind",
        "Geyser-Spigot",
        "floodgate",
        "FastLogin"
    )

    permissions {
        register("grim.alerts") {
            description = "Receive alerts for violations"
            default = Permission.Default.OP
        }

        register("grim.alerts.enable-on-join") {
            description = "Enable alerts on join"
            default = Permission.Default.OP
        }

        register("grim.performance") {
            description = "Check performance metrics"
            default = Permission.Default.OP
        }

        register("grim.profile") {
            description = "Check user profile"
            default = Permission.Default.OP
        }

        register("grim.brand") {
            description = "Show client brands on join"
            default = Permission.Default.OP
        }

        register("grim.sendalert") {
            description = "Send cheater alert"
            default = Permission.Default.OP
        }

        register("grim.nosetback") {
            description = "Disable setback"
            default = Permission.Default.FALSE
        }

        register("grim.nomodifypacket") {
            description = "Disable modifying packets"
            default = Permission.Default.FALSE
        }

        register("grim.exempt") {
            description = "Exempt from all checks"
            default = Permission.Default.FALSE
        }

        register("grim.verbose") {
            description = "Receive verbose alerts for violations. Requires grim.alerts"
            default = Permission.Default.OP
        }

        register("grim.verbose.enable-on-join") {
            description = "Enable verbose alerts on join. Requires grim.alerts and grim.alerts.enable-on-join"
            default = Permission.Default.FALSE
        }
    }
}

tasks.build {
    dependsOn(tasks.shadowJar)
    dependsOn(tasks.spotlessApply)
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

publishing.publications.create<MavenPublication>("maven") {
    artifact(tasks["shadowJar"])
}

java {
    sourceSets {
        create("java18") {
            java {
                srcDirs("src/main/java18")
            }
            compileClasspath += main.get().output
            runtimeClasspath += main.get().output
            dependencies {
                implementation(files("./nalim.jar")) // Add nalim.jar to java18 source set
            }
        }
        create("jmh") {
            java {
                srcDir("src/jmh/java")
            }
            compileClasspath += sourceSets.main.get().output +
                    sourceSets.getByName("java18").output +
                    configurations["jmh"]
            runtimeClasspath += sourceSets.main.get().output +
                    sourceSets.getByName("java18").output +
                    configurations["jmh"]
        }
    }
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(18))
    }
}

tasks.withType<JavaCompile> {
    if (name == "compileJava18Java") {
        options.compilerArgs.addAll(listOf("--add-modules", "jdk.incubator.vector", "-Xlint:unchecked"))
        sourceCompatibility = "18"
        targetCompatibility = "18"
    }
    if (name == "compileJava21Java") {
        options.compilerArgs.addAll(listOf("--enable-preview", "--add-modules", "jdk.incubator.vector", "-Xlint:unchecked"))
        sourceCompatibility = "21"
        targetCompatibility = "21"
    }
    if (name == "compileJmhJava") {
        options.compilerArgs.addAll(listOf("--add-modules", "jdk.incubator.vector"))
        sourceCompatibility = "18"
        targetCompatibility = "18"
    }
}

tasks.withType<Jar> {
    manifest {
        attributes("Multi-Release" to "true")
    }
    duplicatesStrategy = DuplicatesStrategy.INCLUDE // Important for multi-release JARs
    from(sourceSets.main.get().output)
    from(sourceSets.getByName("java18").output) {
        into("META-INF/versions/18")
    }
}

tasks.shadowJar {
    minimize()
    archiveFileName.set("${project.name}-${project.version}.jar")
    if (relocate) {
        relocate("io.github.retrooper.packetevents", "ac.grim.grimac.shaded.io.github.retrooper.packetevents")
        relocate("com.github.retrooper.packetevents", "ac.grim.grimac.shaded.com.github.retrooper.packetevents")
        relocate("co.aikar.commands", "ac.grim.grimac.shaded.acf")
        relocate("co.aikar.locale", "ac.grim.grimac.shaded.locale")
        relocate("club.minnced", "ac.grim.grimac.shaded.discord-webhooks")
        relocate("github.scarsz.configuralize", "ac.grim.grimac.shaded.configuralize")
        relocate("com.github.puregero", "ac.grim.grimac.shaded.com.github.puregero")
        relocate("com.google.code.gson", "ac.grim.grimac.shaded.gson")
        relocate("alexh", "ac.grim.grimac.shaded.maps")
        relocate("it.unimi.dsi.fastutil", "ac.grim.grimac.shaded.fastutil")
        relocate("net.kyori", "ac.grim.grimac.shaded.kyori")
        relocate("okhttp3", "ac.grim.grimac.shaded.okhttp3")
        relocate("okio", "ac.grim.grimac.shaded.okio")
        relocate("org.yaml.snakeyaml", "ac.grim.grimac.shaded.snakeyaml")
        relocate("org.json", "ac.grim.grimac.shaded.json")
        relocate("org.intellij", "ac.grim.grimac.shaded.intellij")
        relocate("org.jetbrains", "ac.grim.grimac.shaded.jetbrains")
    }
}

tasks.register<Jar>("jmhJar") {
    dependsOn("compileGeneratedJmh")

    from(sourceSets["main"].output)
    from(sourceSets["java18"].output)
    from(sourceSets["jmh"].output)
    from("${buildDir}/classes/java/jmh")
    from(configurations["jmh"].map { if (it.isDirectory) it else zipTree(it) })

    manifest {
        attributes(
            "Main-Class" to "org.openjdk.jmh.Main",
            "Add-Opens" to "java.base/java.lang java.base/java.io java.base/java.util java.base/java.util.concurrent java.base/java.net",
            "Add-Modules" to "jdk.incubator.vector"
        )
    }

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    archiveClassifier.set("benchmarks")
}


tasks.register<JavaCompile>("compileGeneratedJmh") {
    dependsOn("compileJmhJava")

    source = fileTree("${buildDir}/generated-sources/jmh")

    classpath = sourceSets["jmh"].compileClasspath +
            sourceSets["java18"].output +
            sourceSets["java18"].compileClasspath +
            sourceSets.main.get().output +
            sourceSets.main.get().compileClasspath +
            files("${buildDir}/classes/java/jmh")

    destinationDirectory.set(file("${buildDir}/classes/java/jmh"))

    sourceCompatibility = "18"
    targetCompatibility = "18"
    options.compilerArgs.addAll(listOf("--add-modules", "jdk.incubator.vector"))
}

tasks.register("jmh") {
    dependsOn("jmhJar")
    doLast {
        val includes = System.getProperty("includes", "")

        javaexec {
            classpath = files(tasks.named("jmhJar").get().outputs.files)
            mainClass.set("org.openjdk.jmh.Main")
            jvmArgs = listOf("--enable-preview", "--add-modules", "jdk.incubator.vector", "-XX:+UnlockExperimentalVMOptions", "-XX:+EnableJVMCI", "-javaagent:nalim.jar")

            // Initialize the args list with default settings
            args = mutableListOf<String>().apply {
                // Add any default arguments here if needed
                if (includes.isNotEmpty()) {
                    addAll(listOf(includes))
                }
            }
        }
    }
}

tasks.named<JavaCompile>("compileJmhJava") {
    source = fileTree("src/jmh/java")
    classpath = sourceSets["jmh"].compileClasspath +
            sourceSets.main.get().output +
            sourceSets["java18"].output
    destinationDirectory.set(file("${buildDir}/classes/java/jmh"))

    sourceCompatibility = "18"
    targetCompatibility = "18"
    options.compilerArgs.addAll(listOf("--add-modules", "jdk.incubator.vector"))
}

tasks.named("jmhJar") {
    dependsOn("compileJmhJava")
}