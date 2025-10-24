plugins {
    id("java-common-conventions")
    `maven-publish`
}

publishing {
    publications {
        create<MavenPublication>("lightweightQueryApi") {
            from(components["java"])
        }
    }
}