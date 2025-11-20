plugins {
    id("java-common-conventions")
}

dependencies {
    api(project(":server-api-https"))
    api(project(":server-api-query"))
}

publishing {
    publications {
        create<MavenPublication>("serverApi") {
            from(components["java"])
        }
    }
}