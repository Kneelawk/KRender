plugins {
    id("com.kneelawk.versioning")
    id("com.kneelawk.submodule")
}

submodule {
    applyMixinExpansions()
    xplatProjectDependency(":model-gltf")
    xplatProjectDependency(":model-obj")
    xplatProjectDependency(":engine-api")
    val kregistry_version: String by project
    xplatExternalDependency { "com.kneelawk.kregistry:kregistry-core-$it:$kregistry_version" }
}
