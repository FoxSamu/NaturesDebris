/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 25 - 2020
 * Author: rgsw
 */

package modernity.common.block.portal;

import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.shapes.VoxelShape;

public enum PortalCornerState implements IStringSerializable {
    INACTIVE( "inactive", PortalCornerBlock.SLAB_SHAPE, 0 ),
    EYE( "eye", PortalCornerBlock.COMBINED_SHAPE, 0 ),
    ACTIVE( "active", PortalCornerBlock.COMBINED_SHAPE, 8 ),
    EXHAUSTED( "exhausted", PortalCornerBlock.COMBINED_SHAPE, 0 );

    private final String name;
    private final VoxelShape shape;
    private final int lightValue;

    PortalCornerState( String name, VoxelShape shape, int lightValue ) {
        this.name = name;
        this.shape = shape;
        this.lightValue = lightValue;
    }

    @Override
    public String getName() {
        return name;
    }

    public VoxelShape getShape() {
        return shape;
    }

    public int getLightValue() {
        return lightValue;
    }
}
