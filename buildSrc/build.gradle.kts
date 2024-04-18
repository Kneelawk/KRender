plugins {
    id("java-gradle-plugin")
}

repositories {
    mavenCentral()
}

gradlePlugin {
    plugins {
        create("versioningPlugin") {
            id = "com.kneelawk.versioning"
            implementationClass = "com.kneelawk.versioning.VersioningPlugin"
        }
    }
}
