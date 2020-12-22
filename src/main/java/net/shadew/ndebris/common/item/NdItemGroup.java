package net.shadew.ndebris.common.item;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import net.shadew.ndebris.common.NaturesDebris;
import net.shadew.ndebris.common.block.NdBlocks;

public abstract class NdItemGroup {
    public static final ItemGroup BUILDING = FabricItemGroupBuilder.create(NaturesDebris.id("building"))
                                                                   .icon(() -> new ItemStack(NdBlocks.ROCK))
                                                                   .build();
}
