plugins {
    id("com.kneelawk.versioning")
    id("com.kneelawk.submodule")
    id("com.kneelawk.kpublish")
}

submodule {
    setRefmaps("krender_model_obj")
    xplatProjectDependency(":model-loading", include = false)
    xplatProjectDependency(":engine-api", include = false)
    setupJavadoc()
}

kpublish {
    createPublication("intermediate")
}
