/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 29 - 2019
 * Author: rgsw
 */

package modernity.common.tileentity;

import modernity.common.container.WorkbenchContainer;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class WorkbenchTileEntity extends ContainerTileEntity {
    private static final ITextComponent TITLE = new TranslationTextComponent( "container.crafting" );

    public WorkbenchTileEntity() {
        super( MDTileEntitiyTypes.WORKBENCH );
    }

    @Override
    protected ITextComponent getDefaultName() {
        return TITLE;
    }

    @Override
    protected Container createMenu( int id, PlayerInventory player ) {
        assert world != null;
        return new WorkbenchContainer( id, player, this, IWorldPosCallable.of( world, pos ) );
    }

    @Override
    public int getSizeInventory() {
        return 9;
    }

    public void dropAll() {
        assert world != null;
        if( ! world.isRemote ) {
            for( ItemStack stack : stacks ) {
                world.addEntity( new ItemEntity( world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack ) );
            }
        }
    }
}
