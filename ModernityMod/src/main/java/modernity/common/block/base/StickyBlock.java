/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.block.base;

import modernity.common.block.plant.IPlantSustainer;
import modernity.generic.block.ISolidBlock;
import modernity.generic.util.EntityUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.pathfinding.PathType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.ILightReader;
import net.minecraft.world.World;

/**
 * Describes a sticky block, which is a block that heavily limits the movement of entities and prevents them from
 * jumping.
 */
@SuppressWarnings( "deprecation" )
public class StickyBlock extends Block implements IPlantSustainer, ISolidBlock {
    public static final VoxelShape COLLISION_SHAPE = makeCuboidShape( 0, 0, 0, 16, 15, 16 );

    public StickyBlock( Properties properties ) {
        super( properties );
    }

    @Override
    public void onEntityCollision( BlockState state, World world, BlockPos pos, Entity entity ) {
        if( ! entity.isInWater() ) {
            EntityUtil.setSmallerMotionMutliplier( state, entity, new Vec3d( 0.25, 0.1, 0.25 ) );
        }
    }

    @Override
    public VoxelShape getCollisionShape( BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context ) {
        return COLLISION_SHAPE;
    }


    @Override
    public boolean isNormalCube( BlockState state, IBlockReader world, BlockPos pos ) {
        return true;
    }

    @Override
    public boolean allowsMovement( BlockState state, IBlockReader world, BlockPos pos, PathType type ) {
        return false;
    }

    @Override
    public boolean canEntitySpawn( BlockState state, IBlockReader world, BlockPos pos, EntityType<?> type ) {
        return true;
    }

    @Override
    public boolean canSustainPlant( ILightReader world, BlockPos pos, BlockState state, Block plant, Direction side ) {
        return true;
    }
}
