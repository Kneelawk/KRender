# Modules

This contains the various different published modules that are part of KRender.

## engine-api-(fabric|neoforge|xplat|xplat-mojmap)

These modules contain the KRender Engine API. This is akin to the Fabric Render API, but is designed to be more of an
abstraction over rendering apis instead of its own dedicated rendering api.

## engine-backend-frapi

This module holds the Fabric Render API backend for the KRender Engine.

## engine-backend-neoforge

This module holds the NeoForge-native backend for the KRender engine.

## model-loading-(fabric|neoforge|xplat|xplat-mojmap)

These modules contain the model loading API. They provide several hooks into the model-loader mechanisms, allowing you
to register custom models, both top-level (like blockstates) and lower-level (like regular json block models). They also
provide ways for you to request various lower-level models be loaded as top-level models.
