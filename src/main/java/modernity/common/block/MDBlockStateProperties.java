/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.common.block;

import modernity.api.util.EWaterlogType;
import net.minecraft.state.EnumProperty;

// TODO: Move all block state properties to here
public final class MDBlockStateProperties {
    public static final EnumProperty<EWaterlogType> WATERLOGGED = EnumProperty.create( "waterlogged", EWaterlogType.class );

    private MDBlockStateProperties() {
    }
}
