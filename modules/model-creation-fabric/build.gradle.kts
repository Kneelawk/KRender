plugins {
    id("com.kneelawk.versioning")
    id("com.kneelawk.submodule")
    id("com.kneelawk.kpublish")
}

submodule {
    setLibsDirectory()
    applyXplatConnection(":model-creation-xplat")
    setupJavadoc()
}

kpublish {
    createPublication()
}
