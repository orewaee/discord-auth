plugins {
    id("java-library")
}

subprojects {
    version = "0.4.1"

    apply(plugin = "java-library")

    repositories {
        mavenCentral()
    }
}