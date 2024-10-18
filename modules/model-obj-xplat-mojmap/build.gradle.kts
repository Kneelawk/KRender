plugins {
    id("com.kneelawk.versioning")
    id("com.kneelawk.submodule")
    id("com.kneelawk.kpublish")
}

submodule {
    applyXplatConnection(":model-obj-xplat")
    setupJavadoc()
}

kpublish {
    createPublication()
}
