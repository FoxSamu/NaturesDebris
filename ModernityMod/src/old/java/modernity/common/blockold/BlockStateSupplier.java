/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.blockold;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.IProperty;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Supplier;

public class BlockStateSupplier implements Supplier<BlockState> {
    private final ResourceLocation id;
    private final ArrayList<Function<BlockState, BlockState>> modifications = new ArrayList<>();

    public BlockStateSupplier(ResourceLocation id) {
        this.id = id;
    }

    public <T extends Comparable<T>, V extends T> BlockStateSupplier with(IProperty<T> prop, V value) {
        modifications.add(state -> state.with(prop, value));
        return this;
    }

    @Override
    public BlockState get() {
        Block block = ForgeRegistries.BLOCKS.getValue(id);
        if(block == null) return null;
        BlockState state = block.getDefaultState();
        for(Function<BlockState, BlockState> modification : modifications) {
            state = modification.apply(state);
        }
        return state;
    }
}
