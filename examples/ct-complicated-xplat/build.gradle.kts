plugins {
    id("com.kneelawk.versioning")
    id("com.kneelawk.submodule")
}

submodule {
    setRefmaps("ct_complicated")
    xplatProjectDependency(":model-loading")
    xplatProjectDependency(":model-creation")
}
