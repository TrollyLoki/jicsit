plugins {
    id("java-common-conventions")
    `maven-publish`
}

publishing {
    publications {
        create<MavenPublication>("serverQuery") {
            from(components["java"])
        }
    }
}