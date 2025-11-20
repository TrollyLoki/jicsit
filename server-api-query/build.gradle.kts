plugins {
    id("java-common-conventions")
    `maven-publish`
}

publishing {
    publications {
        create<MavenPublication>("serverApiQuery") {
            from(components["java"])
        }
    }
}