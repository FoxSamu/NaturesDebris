/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 02 - 2020
 * Author: rgsw
 */

package modernity.client.render.item;

import modernity.common.tileentity.TexturedChestTileEntity;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn( Dist.CLIENT )
public class TexturedChestItemRenderer extends ItemStackTileEntityRenderer {
    private final TexturedChestTileEntity chest;

    public TexturedChestItemRenderer( Block block ) {
        chest = new TexturedChestTileEntity( block );
    }

    @Override
    public void renderByItem( ItemStack stack ) {
        TileEntityRendererDispatcher.instance.renderAsItem( chest );
    }
}