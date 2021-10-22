plugins {
    idea
    java

    id("com.github.johnrengelman.shadow") version "7.1.0" apply false
    id("io.papermc.paperweight.patcher") version "1.1.12"
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "maven-publish")

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(16))
        }
    }

    tasks.withType<JavaCompile> {
        options.isFork = true; options.isIncremental = true; options.encoding =
        Charsets.UTF_8.name(); options.release.set(16)
    }
}

subprojects {

    tasks.withType<Javadoc> {
        options.encoding = Charsets.UTF_8.name()
    }

    tasks.withType<ProcessResources> {
        filteringCharset = Charsets.UTF_8.name()
    }

    repositories {
        mavenCentral()
        maven("https://oss.sonatype.org/content/groups/public/")
        maven("https://papermc.io/repo/repository/maven-public/")
        maven("https://ci.emc.gs/nexus/content/groups/aikar/")
        maven("https://repo.aikar.co/content/groups/aikar")
        maven("https://repo.md-5.net/content/repositories/releases/")
        maven("https://hub.spigotmc.org/nexus/content/groups/public/")
        maven("https://nexus.velocitypowered.com/repository/velocity-artifacts-snapshots/")
        maven("https://jitpack.io")
        maven("https://libraries.minecraft.net")
    }
}

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/") {
        content { onlyForConfigurations("paperclip") }
    }
    maven("https://maven.quiltmc.org/repository/release/") {
        content { onlyForConfigurations("remapper") }
    }
}

dependencies {
    remapper("net.fabricmc:tiny-remapper:0.6.0:fat")
    paperclip("io.papermc:paperclip:2.0.1")
}

paperweight {
    serverProject.set(project(":BenPlane-Server"))

    useStandardUpstream("airplane") {
        url.set(github("TECHNOVE", "Airplane"))
        ref.set(providers.gradleProperty("airplaneRef"))

        withStandardPatcher {
            baseName("Airplane")

            remapRepo.set("https://maven.fabricmc.net/")
            decompileRepo.set("https://files.minecraftforge.net/maven/")

            apiOutputDir.set(layout.projectDirectory.dir("BenPlane-API"))
            serverOutputDir.set(layout.projectDirectory.dir("BenPlane-Server"))
        }
    }
}