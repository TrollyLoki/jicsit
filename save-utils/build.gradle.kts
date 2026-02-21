plugins {
    id("java-common-conventions")
    `maven-publish`
}

dependencies {
    implementation(libs.jackson.databind)
}

publishing {
    publications {
        create<MavenPublication>("saveUtils") {
            from(components["java"])
        }
    }
}