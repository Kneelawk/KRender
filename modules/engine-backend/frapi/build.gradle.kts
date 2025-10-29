plugins {
    id("com.kneelawk.versioning")
    id("com.kneelawk.submodule")
    id("com.kneelawk.kpublish")
}

submodule {
    setLibsDirectory()
    fabricProjectDependency(":engine-api", include = false)
    setupJavadoc()
}

kpublish {
    createPublication()
}
