plugins {
    id("java-common-conventions")
    `maven-publish`
}

dependencies {
    api(project(":save-utils"))
    implementation(libs.spring.web)
    implementation(libs.jackson.databind)
    implementation(libs.jackson.datatype.jsr310)
}

publishing {
    publications {
        create<MavenPublication>("serverApiHttps") {
            from(components["java"])
        }
    }
}