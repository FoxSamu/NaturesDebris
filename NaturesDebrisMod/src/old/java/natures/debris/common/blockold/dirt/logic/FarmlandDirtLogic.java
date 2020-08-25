/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.blockold.dirt.logic;

import modernity.util.Lazy;
import natures.debris.common.blockold.farmland.FarmlandBlock;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Random;
import java.util.function.Supplier;

public class FarmlandDirtLogic extends DirtLogic {
    private static final HashMap<IDirtLogicType, FarmlandDirtLogic> TYPE_MAP = new HashMap<>();

    private final Lazy<NormalDirtLogic> noFarmland;

    public FarmlandDirtLogic(Supplier<? extends FarmlandBlock> block, IDirtLogicType type, Supplier<? extends NormalDirtLogic> noFarmland) {
        super(block, type);
        if (!type.allowOnFarmland()) {
            throw new IllegalArgumentException("Type not allowed on farmland dirt logics");
        }
        if (TYPE_MAP.containsKey(type)) {
            throw new IllegalStateException("Farmland dirt logic type already registered");
        }
        TYPE_MAP.put(type, this);
        this.noFarmland = Lazy.of(noFarmland);
    }

    public NormalDirtLogic getDirtVariant() {
        return noFarmland.get();
    }

    @Override
    public DirtLogic switchTo(IDirtLogicType type) {
        return TYPE_MAP.get(type);
    }

    @Override
    public boolean canSwitchTo(IDirtLogicType type) {
        return TYPE_MAP.containsKey(type);
    }

    public BlockState makeNormal(IWorld world, BlockPos pos, BlockState state) {
        return noFarmland.get().switchState(world, pos, state);
    }

    @Override
    public void grow(World world, BlockPos pos, BlockState state, Random rand) {
    }

    @Override
    public boolean canGrow(ItemStack stack) {
        return false;
    }
}
