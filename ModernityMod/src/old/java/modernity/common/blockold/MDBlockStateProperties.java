/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.blockold;

import modernity.common.blockold.base.SlabType;
import modernity.common.blockold.farmland.Fertility;
import modernity.common.blockold.fluid.WaterlogType;
import modernity.common.blockold.plant.DoubleDirectionalPlantBlock;
import modernity.common.blockold.portal.PortalCornerState;
import modernity.common.blockold.prop.IntEnumProperty;
import modernity.common.blockold.prop.SignedIntegerProperty;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.util.Direction;

public final class MDBlockStateProperties {
    public static final EnumProperty<WaterlogType> WATERLOGGED = EnumProperty.create("waterlogged", WaterlogType.class);

    public static final BooleanProperty CORNER_000 = BooleanProperty.create("c000");
    public static final BooleanProperty CORNER_001 = BooleanProperty.create("c001");
    public static final BooleanProperty CORNER_010 = BooleanProperty.create("c010");
    public static final BooleanProperty CORNER_011 = BooleanProperty.create("c011");
    public static final BooleanProperty CORNER_100 = BooleanProperty.create("c100");
    public static final BooleanProperty CORNER_101 = BooleanProperty.create("c101");
    public static final BooleanProperty CORNER_110 = BooleanProperty.create("c110");
    public static final BooleanProperty CORNER_111 = BooleanProperty.create("c111");

    public static final EnumProperty<SlabType> SLAB_TYPE = EnumProperty.create("type", SlabType.class);

    public static final EnumProperty<Fertility> FERTILITY = EnumProperty.create("fertility", Fertility.class);

    public static final SignedIntegerProperty DISTANCE_N1_3 = SignedIntegerProperty.create("distance", -1, 3);
    public static final IntegerProperty DISTANCE_0_10 = IntegerProperty.create("distance", 0, 10);
    public static final SignedIntegerProperty DISTANCE_N1_10 = SignedIntegerProperty.create("distance", -1, 10);

    public static final DirectionProperty FACING_NO_UP = DirectionProperty.create("facing", facing -> facing != Direction.UP);

    public static final IntEnumProperty ROOT_END_TYPE = IntEnumProperty.builder("type")
                                                                       .with(DoubleDirectionalPlantBlock.ROOT, "root")
                                                                       .with(DoubleDirectionalPlantBlock.END, "end")
                                                                       .create();

    public static final BooleanProperty ROOT = BooleanProperty.create("root");
    public static final BooleanProperty END = BooleanProperty.create("end");

    public static final IntegerProperty AGE_0_11 = IntegerProperty.create("age", 0, 11);
    public static final BooleanProperty NATURAL = BooleanProperty.create("natural");
    public static final BooleanProperty CAN_SPREAD = BooleanProperty.create("can_spread");

    public static final IntegerProperty LENGTH_1_4 = IntegerProperty.create("length", 1, 4);

    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public static final EnumProperty<Direction.Axis> HORIZONTAL_AXIS = EnumProperty.create("axis", Direction.Axis.class, Direction.Axis::isHorizontal);

    public static final EnumProperty<PortalCornerState> PORTAL_CORNER_STATE = EnumProperty.create("state", PortalCornerState.class);

    public static final IntegerProperty DENSITY_1_16 = IntegerProperty.create("density", 1, 16);
    public static final IntegerProperty LEVEL_0_5 = IntegerProperty.create("level", 0, 5);
    public static final IntegerProperty DECAY_0_8 = IntegerProperty.create("decay", 0, 8);

    public static final IntegerProperty AGE_1_8 = IntegerProperty.create("age", 1, 8);
    public static final IntegerProperty AGE_1_6 = IntegerProperty.create("age", 1, 6);

    private MDBlockStateProperties() {
    }
}
