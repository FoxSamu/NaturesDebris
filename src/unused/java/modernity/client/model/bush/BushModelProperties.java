/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 30 - 2020
 * Author: rgsw
 */

package modernity.client.model.bush;

import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;

public final class BushModelProperties {
    public static final ModelProperty<Boolean> UP = new ModelProperty<>();
    public static final ModelProperty<Boolean> DOWN = new ModelProperty<>();
    public static final ModelProperty<Boolean> NORTH = new ModelProperty<>();
    public static final ModelProperty<Boolean> EAST = new ModelProperty<>();
    public static final ModelProperty<Boolean> SOUTH = new ModelProperty<>();
    public static final ModelProperty<Boolean> WEST = new ModelProperty<>();

    public static final ModelProperty<Boolean> UP_NORTH = new ModelProperty<>();
    public static final ModelProperty<Boolean> UP_EAST = new ModelProperty<>();
    public static final ModelProperty<Boolean> UP_SOUTH = new ModelProperty<>();
    public static final ModelProperty<Boolean> UP_WEST = new ModelProperty<>();

    public static final ModelProperty<Boolean> DOWN_NORTH = new ModelProperty<>();
    public static final ModelProperty<Boolean> DOWN_EAST = new ModelProperty<>();
    public static final ModelProperty<Boolean> DOWN_SOUTH = new ModelProperty<>();
    public static final ModelProperty<Boolean> DOWN_WEST = new ModelProperty<>();

    public static final ModelProperty<Boolean> NORTH_EAST = new ModelProperty<>();
    public static final ModelProperty<Boolean> NORTH_WEST = new ModelProperty<>();
    public static final ModelProperty<Boolean> SOUTH_EAST = new ModelProperty<>();
    public static final ModelProperty<Boolean> SOUTH_WEST = new ModelProperty<>();

    private static final ModelDataMap.Builder BUILDER
        = new ModelDataMap.Builder()
              .withInitial( UP, false )
              .withInitial( DOWN, false )
              .withInitial( NORTH, false )
              .withInitial( EAST, false )
              .withInitial( SOUTH, false )
              .withInitial( WEST, false )
              .withInitial( UP_NORTH, false )
              .withInitial( UP_EAST, false )
              .withInitial( UP_SOUTH, false )
              .withInitial( UP_WEST, false )
              .withInitial( DOWN_NORTH, false )
              .withInitial( DOWN_EAST, false )
              .withInitial( DOWN_SOUTH, false )
              .withInitial( DOWN_WEST, false )
              .withInitial( NORTH_EAST, false )
              .withInitial( NORTH_WEST, false )
              .withInitial( SOUTH_EAST, false )
              .withInitial( SOUTH_WEST, false );

    public static ModelDataMap createData() {
        return BUILDER.build();
    }

    private BushModelProperties() {
    }
}
