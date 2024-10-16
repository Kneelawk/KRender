# KRender

Cross-platform Minecraft rendering abstraction

This library is an abstraction over the various loader-specific rendering mechanisms, especially focusing on block
rendering.

This is API takes significant inspiration from [FREX] and the [Fabric Render API]. However, one of the main differences
between this API and FREX and the Fabric Render API is that this API is designed to have multiple backends loaded and
running at once.

[FREX]: https://github.com/vram-guild/frex

[Fabric Render API]: https://github.com/FabricMC/fabric/tree/1.21.1/fabric-renderer-api-v1
