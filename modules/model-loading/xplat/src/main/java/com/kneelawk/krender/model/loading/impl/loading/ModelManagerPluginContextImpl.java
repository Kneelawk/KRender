package com.kneelawk.krender.model.loading.impl.loading;

import java.util.Map;
import java.util.Set;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;

import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.block.model.SingleVariant;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;

import com.kneelawk.krender.model.loading.api.ModelManagerPlugin;

public class ModelManagerPluginContextImpl implements ModelManagerPlugin.Context {
    private final Map<Identifier, UnbakedModel> referenceableModels = new Object2ObjectLinkedOpenHashMap<>();
    private final Map<BlockState, BlockStateModel.UnbakedRoot> blockStateModels =
        new Object2ObjectLinkedOpenHashMap<>();
    private final Set<Identifier> extraModels = new ObjectLinkedOpenHashSet<>();

    public ModelManagerPluginManager createManager() {
        return new ModelManagerPluginManager(referenceableModels, blockStateModels, extraModels);
    }

    @Override
    public void linkBlockStateToModel(BlockState state, Identifier name) {
        blockStateModels.put(state, new SingleVariant.Unbaked(new Variant(name)).asRoot());
    }

    @Override
    public void linkBlockStatesToModels(Map<BlockState, Identifier> models) {
        for (var entry : models.entrySet()) {
            blockStateModels.put(entry.getKey(), new SingleVariant.Unbaked(new Variant(entry.getValue())).asRoot());
        }
    }

    @Override
    public void addBlockStateModel(BlockState state, BlockStateModel.UnbakedRoot model) {
        blockStateModels.put(state, model);
    }

    @Override
    public void addBlockStateModels(Map<BlockState, ? extends BlockStateModel.UnbakedRoot> models) {
        blockStateModels.putAll(models);
    }

    @Override
    public void addReferenceableModel(Identifier name, UnbakedModel model) {
        referenceableModels.put(name, model);
    }

    @Override
    public void addReferenceableModels(Map<Identifier, ? extends UnbakedModel> models) {
        referenceableModels.putAll(models);
    }

    @Override
    public void addExtraModel(Identifier name) {
        extraModels.add(name);
    }

    @Override
    public void addExtraModels(Set<Identifier> names) {
        extraModels.addAll(names);
    }
}
