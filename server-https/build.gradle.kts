plugins {
    id("java-common-conventions")
    `maven-publish`
}

dependencies {
    api(project(":save"))
    implementation(libs.spring.web)
    implementation(libs.jackson.databind)
}

publishing {
    publications {
        create<MavenPublication>("serverHttps") {
            artifactId = "jicsit-server-https"
            from(components["java"])
        }
    }
}