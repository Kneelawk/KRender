plugins {
    id("com.kneelawk.versioning")
    id("com.kneelawk.submodule")
    id("com.kneelawk.kpublish")
}

submodule {
    applyMixinExpansions()
    xplatProjectDependency(":engine-api", include = false)
    setupJavadoc()
}

kpublish {
    createPublication("mojmap")
}
