/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 29 - 2019
 * Author: rgsw
 */

package modernity.common.tileentity;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.FurnaceContainer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class RockFurnaceTileEntity extends AbstractFurnaceTileEntity {
    public RockFurnaceTileEntity() {
        super( MDTileEntitiyTypes.ROCK_FURNACE, IRecipeType.SMELTING );
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent( "container.furnace" );
    }

    @Override
    protected Container createMenu( int id, PlayerInventory player ) {
        return new FurnaceContainer( id, player, this, furnaceData );
    }
}
