plugins {
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

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release = 17
    }

    shadowJar {
        archiveFileName.set("${rootProject.name}-$version.jar")
    }
}
