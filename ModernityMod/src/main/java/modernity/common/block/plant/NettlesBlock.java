/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.block.plant;

import modernity.common.block.plant.growing.FertilityGrowLogic;
import modernity.common.entity.MDEntityTags;
import modernity.common.util.MDDamageSource;
import modernity.generic.util.EntityUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;

public class NettlesBlock extends SimplePlantBlock implements IDangerousPlant {
    public static final VoxelShape NETTLES_SHAPE = makePlantShape( 14, 14 );

    public NettlesBlock( Properties properties ) {
        super( properties, NETTLES_SHAPE );
        setGrowLogic( new FertilityGrowLogic( this ) );
    }

    @Override
    public boolean dealsDamage( World world, BlockPos pos, BlockState state, Entity entity ) {
        boolean speed = false;
        if( entity.lastTickPosX != entity.getPosX() || entity.lastTickPosZ != entity.getPosZ() ) {
            double dx = Math.abs( entity.getPosX() - entity.lastTickPosX );
            double dz = Math.abs( entity.getPosZ() - entity.lastTickPosZ );
            speed = dx > 0.003 || dz > 0.003;
        }
        return speed && entity instanceof LivingEntity && ! entity.getType().isContained( MDEntityTags.NETTLES_IMMUNE );
    }

    @Override
    public DamageSource getDamageSource( World world, BlockPos pos, BlockState state, Entity entity ) {
        return MDDamageSource.NETTLES;
    }

    @Override
    public float getDamage( World world, BlockPos pos, BlockState state, Entity entity ) {
        return 1F;
    }

    @Override
    public void onEntityCollision( BlockState state, World world, BlockPos pos, Entity entity ) {
        super.onEntityCollision( state, world, pos, entity );
        if( ! entity.getType().isContained( MDEntityTags.NETTLES_IMMUNE ) ) {
            EntityUtil.setSmallerMotionMutliplier( state, entity, new Vec3d( 0.75, 1, 0.75 ) );
        }
    }
}
