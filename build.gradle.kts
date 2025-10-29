plugins {
    id("fabric-loom") apply false
    id("com.kneelawk.submodule") apply false
    id("agency.highlysuspect.minivan") apply false
    id("net.neoforged.moddev") apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}
