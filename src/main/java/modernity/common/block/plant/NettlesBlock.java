/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 14 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant;

import modernity.common.entity.MDEntityTags;
import modernity.common.util.MDDamageSource;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;

public class NettlesBlock extends SimplePlantBlock implements IDangerousPlant {
    public static final VoxelShape NETTLES_SHAPE = makeCuboidShape( 1, 0, 1, 15, 14, 15 );

    public NettlesBlock( Properties properties ) {
        super( properties, NETTLES_SHAPE );
    }

    @Override
    public boolean dealsDamage( World world, BlockPos pos, BlockState state, Entity entity ) {
        boolean speed = false;
        if( entity.lastTickPosX != entity.posX || entity.lastTickPosZ != entity.posZ ) {
            double dx = Math.abs( entity.posX - entity.lastTickPosX );
            double dz = Math.abs( entity.posZ - entity.lastTickPosZ );
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
}
