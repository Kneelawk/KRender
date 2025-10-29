package com.kneelawk.krender.engine.backend.neoforge.impl.model;

import net.neoforged.neoforge.client.model.data.ModelProperty;

import org.jetbrains.annotations.UnknownNullability;

public record ModelKeyHolder(@UnknownNullability Object modelKey) {
    public static final ModelProperty<ModelKeyHolder> PROPERTY = new ModelProperty<>();
}
