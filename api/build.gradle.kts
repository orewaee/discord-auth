repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    implementation(libs.annotations)
    compileOnly(libs.velocity)
    annotationProcessor(libs.velocity)
}
