/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.tileentity;

import modernity.generic.block.ITexturedChest;
import net.minecraft.block.Block;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.util.ResourceLocation;

public class TexturedChestTileEntity extends ChestTileEntity implements ITexturedChest {
    private static final ResourceLocation TEXTURE_NORMAL = new ResourceLocation( "textures/entity/chest/normal.png" );
    private static final ResourceLocation TEXTURE_NORMAL_DOUBLE = new ResourceLocation( "textures/entity/chest/normal_double.png" );

    public TexturedChestTileEntity() {
        super( MDTileEntitiyTypes.CHEST );
    }


    private Block block;

    public TexturedChestTileEntity( Block block ) {
        super( null );
        this.block = block;
    }

    @Override
    public ResourceLocation getChestTexture( boolean doubleChest ) {
        if( world != null ) {
            Block block = getBlockState().getBlock();
            if( block instanceof ITexturedChest ) {
                return ( (ITexturedChest) block ).getChestTexture( doubleChest );
            }
        } else if( block != null ) {
            if( block instanceof ITexturedChest ) {
                return ( (ITexturedChest) block ).getChestTexture( doubleChest );
            }
        }
        return doubleChest ? TEXTURE_NORMAL_DOUBLE : TEXTURE_NORMAL;
    }
}
