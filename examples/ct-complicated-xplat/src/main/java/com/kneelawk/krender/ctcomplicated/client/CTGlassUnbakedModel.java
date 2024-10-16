package com.kneelawk.krender.ctcomplicated.client;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;

import com.kneelawk.krender.engine.api.KRenderer;
import com.kneelawk.krender.engine.api.material.BlendMode;

import static com.kneelawk.krender.ctcomplicated.CTConstants.rl;

public class CTGlassUnbakedModel implements UnbakedModel {
    @Override
    public Collection<ResourceLocation> getDependencies() {
        return List.of();
    }

    @Override
    public void resolveParents(Function<ResourceLocation, UnbakedModel> resolver) {

    }

    @Override
    public @Nullable BakedModel bake(ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter,
                                     ModelState state) {
        Function<String, TextureAtlasSprite> material =
            (String str) -> spriteGetter.apply(new Material(TextureAtlas.LOCATION_BLOCKS, rl(str)));
        return KRenderer.getDefault().bakedModelFactory()
            .wrap(new CTGlassBakedModel(true, true, material.apply("item/ct_glass"), new TextureAtlasSprite[]{
                material.apply("block/ct_glass_convex"),
                material.apply("block/ct_glass_horizontal"),
                material.apply("block/ct_glass_vertical"),
                material.apply("block/ct_glass_concave"),
                material.apply("block/ct_glass_center")
            }, KRenderer.getDefault().materialManager().materialFinder().setBlendMode(BlendMode.CUTOUT).find()));
    }
}
