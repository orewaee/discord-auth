plugins {
    id("net.kyori.blossom") version "1.3.1"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly(libs.velocity)
    annotationProcessor(libs.velocity)
    implementation(libs.jda) {
        exclude(module = "opus-java")
    }

    implementation(project(":common"))
}

blossom {
    replaceTokenIn("dev/orewaee/discordauth/velocity/DiscordAuth.java")
    replaceToken("@version", version.toString())
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release = 17
    }

    shadowJar {
        archiveFileName.set("${rootProject.name}-$version.jar")
    }
}
