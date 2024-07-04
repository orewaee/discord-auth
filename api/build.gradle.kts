repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly(libs.annotations)
    compileOnly(libs.velocity)
    annotationProcessor(libs.velocity)
}
