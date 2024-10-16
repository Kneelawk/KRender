package com.kneelawk.krender.ctcomplicated.block;

import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;

import com.kneelawk.krender.ctcomplicated.CTComplicated;
import com.kneelawk.krender.ctcomplicated.registry.Registrar;

public class CTBlocks {
    private static final Registrar<Block> BLOCKS = CTComplicated.REGISTRARS.get(Registries.BLOCK);
    private static final Registrar<Item> ITEMS = CTComplicated.REGISTRARS.get(Registries.ITEM);

    public static final Supplier<GlassBlock> CT_GLASS = BLOCKS.register("ct_glass", () -> new GlassBlock(
        BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.HAT).strength(0.3f).sound(SoundType.GLASS)
            .noOcclusion().isValidSpawn(CTBlocks::never).isRedstoneConductor(CTBlocks::never)
            .isSuffocating(CTBlocks::never).isViewBlocking(CTBlocks::never)));
    public static final Supplier<BlockItem> CT_GLASS_ITEM =
        ITEMS.register("ct_glass", () -> new BlockItem(CT_GLASS.get(), new Item.Properties()));

    public static void register() {
    }

    private static boolean never(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return false;
    }

    private static boolean never(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos,
                                 EntityType<?> entityType) {
        return false;
    }
}
