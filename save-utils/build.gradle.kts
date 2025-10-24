plugins {
    id("java-common-conventions")
    `maven-publish`
}

dependencies {
    implementation(libs.jackson.annotations)
}

publishing {
    publications {
        create<MavenPublication>("saveUtils") {
            from(components["java"])
        }
    }
}