/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 9 - 2 - 2019
 */

package modernity.common.block.base;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

public class BlockSticky extends BlockBase {
    public static final VoxelShape COLLISION_SHAPE = makeCuboidShape( 0, 0, 0, 16, 15, 16 );

    public BlockSticky( String id, Properties properties, Item.Properties itemProps ) {
        super( id, properties, itemProps );
    }

    public BlockSticky( String id, Properties properties ) {
        super( id, properties );
    }

    @Override
    public void onEntityCollision( IBlockState state, World world, BlockPos pos, Entity entity ) {
        entity.setInWeb();
    }

    @Override
    public VoxelShape getCollisionShape( IBlockState state, IBlockReader world, BlockPos pos ) {
        return COLLISION_SHAPE;
    }

    @Override
    public boolean isFullCube( IBlockState state ) {
        return false;
    }

    @Override
    public boolean causesSuffocation( IBlockState state ) {
        return true;
    }

    public static class Digable extends BlockSticky {

        public Digable( String id, Properties properties, Item.Properties itemProps ) {
            super( id, properties, itemProps );
        }

        public Digable( String id, Properties properties ) {
            super( id, properties );
        }

        @Override
        public boolean isToolEffective( IBlockState state, ToolType tool ) {
            return tool == ToolType.SHOVEL;
        }
    }
}
