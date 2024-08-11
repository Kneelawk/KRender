plugins {
    id("com.kneelawk.versioning")
    id("com.kneelawk.submodule")
    id("com.kneelawk.kpublish")
}

submodule {
    setRefmaps("krender_engine_api")
    setupJavadoc()
}

kpublish {
    createPublication("intermediate")
}
