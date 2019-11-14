/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.common.block.base;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.pathfinding.PathType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

/**
 * Describes a sticky block, which is a block that heavily limits the movement of entities and prevents them from
 * jumping.
 */
public class StickyBlock extends Block {
    public static final VoxelShape COLLISION_SHAPE = makeCuboidShape( 0, 0, 0, 16, 15, 16 );

    public StickyBlock( Properties properties ) {
        super( properties );
    }

    @Override
    public void onEntityCollision( BlockState state, World world, BlockPos pos, Entity entity ) {
        if( ! entity.isInWater() ) { // Motion multiplier already limits jumping.
            entity.setMotionMultiplier( state, new Vec3d( 0.25, 0.05, 0.25 ) );
        }
    }

    @Override
    public VoxelShape getCollisionShape( BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context ) {
        return COLLISION_SHAPE;
    }


    @Override
    public boolean isNormalCube( BlockState state, IBlockReader worldIn, BlockPos pos ) {
        return true;
    }

    @Override
    public boolean allowsMovement( BlockState state, IBlockReader worldIn, BlockPos pos, PathType type ) {
        return false;
    }

    @Override
    public boolean canEntitySpawn( BlockState state, IBlockReader worldIn, BlockPos pos, EntityType<?> type ) {
        return true;
    }

    /**
     * A digable variant of {@link StickyBlock}.
     */
    public static class Digable extends StickyBlock {

        public Digable( Properties properties ) {
            super( properties );
        }

        @Override
        public boolean isToolEffective( BlockState state, ToolType tool ) {
            return tool == ToolType.SHOVEL;
        }
    }
}
