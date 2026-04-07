plugins {
    id("java-common-conventions")
}

dependencies {
    api(project(":server-https"))
    api(project(":server-query"))
}

publishing {
    publications {
        create<MavenPublication>("server") {
            artifactId = "jicsit-server"
            from(components["java"])
        }
    }
}