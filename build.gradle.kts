plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "dev.orewaee"
version = "0.3.1"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public")
}

dependencies {
    compileOnly("com.velocitypowered:velocity-api:3.2.0-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:3.2.0-SNAPSHOT")
    implementation("net.dv8tion:JDA:5.0.0-beta.13")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.tomlj:tomlj:1.1.0")
    implementation("net.kyori:adventure-text-minimessage:4.14.0")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    shadowJar {
        archiveFileName.set("DiscordAuth-$version.jar")
    }
}
