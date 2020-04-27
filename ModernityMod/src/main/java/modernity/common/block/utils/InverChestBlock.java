/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.block.utils;

import modernity.generic.block.ITexturedChest;
import net.minecraft.util.ResourceLocation;

public class InverChestBlock extends ExtChestBlock implements ITexturedChest {
    private static final ResourceLocation SINGLE = new ResourceLocation( "modernity:textures/entity/chest/inver.png" );
    private static final ResourceLocation DOUBLE = new ResourceLocation( "modernity:textures/entity/chest/inver_double.png" );


    public InverChestBlock( Properties properties ) {
        super( properties );
    }

    @Override
    public ResourceLocation getChestTexture( boolean doubleChest ) {
        return doubleChest ? DOUBLE : SINGLE;
    }
}
