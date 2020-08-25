/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.blockold.plant;

import natures.debris.client.model.bush.BushModelProperties;
import natures.debris.generic.util.MovingBlockPos;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILightReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelDataMap;

public abstract class BushBlock extends PlantBlock {
    public BushBlock(Properties properties) {
        super(properties);
    }

    protected boolean canConnect(ILightReader world, MovingBlockPos pos, Direction dir) {
        pos.move(dir, 1);
        BlockState state = world.getBlockState(pos);
        return state.getBlock() == this || connectToSolidBlocks() && isBlockSideSustainable(state, world, pos, dir.getOpposite());
    }

    protected boolean canConnectDiagonally(ILightReader world, MovingBlockPos pos, Direction dir1, Direction dir2) {
        pos.move(dir1, 1);
        BlockState state1 = world.getBlockState(pos);
        boolean solidSide1 = isBlockSideSustainable(state1, world, pos, dir1.getOpposite());
        pos.move(dir1, -1).move(dir2, 1);
        BlockState state2 = world.getBlockState(pos);
        boolean solidSide2 = isBlockSideSustainable(state2, world, pos, dir2.getOpposite());

        if (connectToSolidBlocks()) {
            if (solidSide1 && solidSide2) return true;
        }

        pos.move(dir1, 1);
        BlockState state3 = world.getBlockState(pos);

        return state1.getBlock() == this && state2.getBlock() == this && state3.getBlock() == this;
    }

    protected boolean connectToSolidBlocks() {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public void fillModelData(ILightReader world, BlockPos pos, BlockState state, ModelDataMap data) {
        MovingBlockPos mpos = new MovingBlockPos();

        if (canConnect(world, mpos.setPos(pos), Direction.UP)) data.setData(BushModelProperties.UP, true);
        if (canConnect(world, mpos.setPos(pos), Direction.DOWN)) data.setData(BushModelProperties.DOWN, true);
        if (canConnect(world, mpos.setPos(pos), Direction.NORTH)) data.setData(BushModelProperties.NORTH, true);
        if (canConnect(world, mpos.setPos(pos), Direction.EAST)) data.setData(BushModelProperties.EAST, true);
        if (canConnect(world, mpos.setPos(pos), Direction.SOUTH)) data.setData(BushModelProperties.SOUTH, true);
        if (canConnect(world, mpos.setPos(pos), Direction.WEST)) data.setData(BushModelProperties.WEST, true);

        if (canConnectDiagonally(world, mpos.setPos(pos), Direction.UP, Direction.NORTH))
            data.setData(BushModelProperties.UP_NORTH, true);
        if (canConnectDiagonally(world, mpos.setPos(pos), Direction.UP, Direction.EAST))
            data.setData(BushModelProperties.UP_EAST, true);
        if (canConnectDiagonally(world, mpos.setPos(pos), Direction.UP, Direction.SOUTH))
            data.setData(BushModelProperties.UP_SOUTH, true);
        if (canConnectDiagonally(world, mpos.setPos(pos), Direction.UP, Direction.WEST))
            data.setData(BushModelProperties.UP_WEST, true);

        if (canConnectDiagonally(world, mpos.setPos(pos), Direction.DOWN, Direction.NORTH))
            data.setData(BushModelProperties.DOWN_NORTH, true);
        if (canConnectDiagonally(world, mpos.setPos(pos), Direction.DOWN, Direction.EAST))
            data.setData(BushModelProperties.DOWN_EAST, true);
        if (canConnectDiagonally(world, mpos.setPos(pos), Direction.DOWN, Direction.SOUTH))
            data.setData(BushModelProperties.DOWN_SOUTH, true);
        if (canConnectDiagonally(world, mpos.setPos(pos), Direction.DOWN, Direction.WEST))
            data.setData(BushModelProperties.DOWN_WEST, true);

        if (canConnectDiagonally(world, mpos.setPos(pos), Direction.NORTH, Direction.EAST))
            data.setData(BushModelProperties.NORTH_EAST, true);
        if (canConnectDiagonally(world, mpos.setPos(pos), Direction.NORTH, Direction.WEST))
            data.setData(BushModelProperties.NORTH_WEST, true);
        if (canConnectDiagonally(world, mpos.setPos(pos), Direction.SOUTH, Direction.EAST))
            data.setData(BushModelProperties.SOUTH_EAST, true);
        if (canConnectDiagonally(world, mpos.setPos(pos), Direction.SOUTH, Direction.WEST))
            data.setData(BushModelProperties.SOUTH_WEST, true);
    }
}
