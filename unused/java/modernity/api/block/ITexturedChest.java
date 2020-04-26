/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 02 - 2020
 * Author: rgsw
 */

package modernity.generic.block;

import net.minecraft.util.ResourceLocation;

@FunctionalInterface
public interface ITexturedChest {
    ResourceLocation getChestTexture( boolean doubleChest );
}
