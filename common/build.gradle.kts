dependencies {
    compileOnly(libs.annotations)
    implementation(libs.gson)
    implementation(libs.toml)

    api(project(":api"))
}
