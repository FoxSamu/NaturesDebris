/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.generic.block;

import net.minecraft.util.ResourceLocation;

@FunctionalInterface
public interface ITexturedChest {
    ResourceLocation getChestTexture( boolean doubleChest );
}
