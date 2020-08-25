/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.blockold.plant;

import modernity.common.blockold.plant.growing.BushGrowLogic;
import modernity.generic.util.EntityUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class MuxusBushBlock extends BushBlock {
    public MuxusBushBlock(Properties properties) {
        super(properties);
        setGrowLogic(new BushGrowLogic(this));
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if(entity instanceof PlayerEntity && ((PlayerEntity) entity).abilities.isFlying) return;
        entity.setMotion(entity.getMotion().mul(0.65, 0.65, 0.65));
        EntityUtil.suspendFallDistance(entity, 0.2);
    }

    @Override
    public boolean canRemain(IWorldReader world, BlockPos pos, BlockState state, Direction dir, BlockPos adj, BlockState adjState) {
        return dir != Direction.DOWN || canBlockSustain(world, adj, adjState);
    }

    public boolean canBlockSustain(IWorldReader reader, BlockPos pos, BlockState state) {
        return state.getBlock() == this || isBlockSideSustainable(state, reader, pos, Direction.UP);
    }
}
