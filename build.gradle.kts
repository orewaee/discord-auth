plugins {
    id("java-library")
}

subprojects {
    version = "0.4.0"

    apply(plugin = "java-library")

    repositories {
        mavenCentral()
    }
}