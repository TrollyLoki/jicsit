plugins {
    id("java-common-conventions")
    `java-test-fixtures`
    `maven-publish`
}

dependencies {
    implementation(libs.jackson.databind)
}

publishing {
    publications {
        create<MavenPublication>("save") {
            artifactId = "jicsit-save"
            from(components["java"])
        }
    }
}