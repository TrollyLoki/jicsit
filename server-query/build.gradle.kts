plugins {
    id("java-common-conventions")
    `maven-publish`
}

publishing {
    publications {
        create<MavenPublication>("serverQuery") {
            artifactId = "jicsit-server-query"
            from(components["java"])
        }
    }
}