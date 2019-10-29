/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 29 - 2019
 */

package modernity.common.block.base;

import modernity.api.util.MDVoxelShapes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Describes the Nether Altar block.
 */
public class NetherAltarBlock extends Block {
    private static final VoxelShape ALTAR_SHAPE;
    private static final VoxelShape SIMPLE_SHAPE = MDVoxelShapes.create16( 0, 0, 0, 16, 12, 16 );

    static {
        VoxelShape obsidian = MDVoxelShapes.create16( 1, 1, 1, 15, 11, 15 );
        VoxelShape corner1 = MDVoxelShapes.create16( 0, - 1, 0, 2, 12, 2 );
        VoxelShape corner2 = MDVoxelShapes.create16( 0, - 1, 14, 2, 12, 16 );
        VoxelShape corner3 = MDVoxelShapes.create16( 14, - 1, 14, 16, 12, 16 );
        VoxelShape corner4 = MDVoxelShapes.create16( 14, - 1, 0, 16, 12, 2 );
        VoxelShape edge1 = MDVoxelShapes.create16( 2, 10, 0, 14, 12, 2 );
        VoxelShape edge2 = MDVoxelShapes.create16( 2, 10, 14, 14, 12, 16 );
        VoxelShape edge3 = MDVoxelShapes.create16( 0, 10, 2, 2, 12, 14 );
        VoxelShape edge4 = MDVoxelShapes.create16( 14, 10, 2, 16, 12, 14 );
        VoxelShape bricks = MDVoxelShapes.create16( 3, 10.25, 3, 13, 11.25, 13 );
        VoxelShape netherrack1 = MDVoxelShapes.create16( 0, 0, 2, 1, 1, 14 );
        VoxelShape netherrack2 = MDVoxelShapes.create16( 15, 0, 2, 16, 1, 14 );
        VoxelShape netherrack3 = MDVoxelShapes.create16( 2, 0, 0, 14, 1, 1 );
        VoxelShape netherrack4 = MDVoxelShapes.create16( 2, 0, 15, 14, 1, 16 );
        VoxelShape sand1 = MDVoxelShapes.create16( - 1, - 1, - 1, 3, 2, 3 );
        VoxelShape sand2 = MDVoxelShapes.create16( - 1, - 1, 13, 3, 2, 17 );
        VoxelShape sand3 = MDVoxelShapes.create16( 13, - 1, 13, 17, 2, 17 );
        VoxelShape sand4 = MDVoxelShapes.create16( 13, - 1, - 1, 17, 2, 3 );
        VoxelShape glowstone = MDVoxelShapes.create16( 6, 11, 6, 10, 13, 10 );

        VoxelShape s = obsidian;
        s = VoxelShapes.combineAndSimplify( s, corner1, IBooleanFunction.OR );
        s = VoxelShapes.combineAndSimplify( s, corner2, IBooleanFunction.OR );
        s = VoxelShapes.combineAndSimplify( s, corner3, IBooleanFunction.OR );
        s = VoxelShapes.combineAndSimplify( s, corner4, IBooleanFunction.OR );
        s = VoxelShapes.combineAndSimplify( s, edge1, IBooleanFunction.OR );
        s = VoxelShapes.combineAndSimplify( s, edge2, IBooleanFunction.OR );
        s = VoxelShapes.combineAndSimplify( s, edge3, IBooleanFunction.OR );
        s = VoxelShapes.combineAndSimplify( s, edge4, IBooleanFunction.OR );
        s = VoxelShapes.combineAndSimplify( s, bricks, IBooleanFunction.OR );
        s = VoxelShapes.combineAndSimplify( s, netherrack1, IBooleanFunction.OR );
        s = VoxelShapes.combineAndSimplify( s, netherrack2, IBooleanFunction.OR );
        s = VoxelShapes.combineAndSimplify( s, netherrack3, IBooleanFunction.OR );
        s = VoxelShapes.combineAndSimplify( s, netherrack4, IBooleanFunction.OR );
        s = VoxelShapes.combineAndSimplify( s, sand1, IBooleanFunction.OR );
        s = VoxelShapes.combineAndSimplify( s, sand2, IBooleanFunction.OR );
        s = VoxelShapes.combineAndSimplify( s, sand3, IBooleanFunction.OR );
        s = VoxelShapes.combineAndSimplify( s, sand4, IBooleanFunction.OR );
        s = VoxelShapes.combineAndSimplify( s, glowstone, IBooleanFunction.OR );
        ALTAR_SHAPE = s;
    }

    public NetherAltarBlock( Properties properties ) {
        super( properties );
    }

    @Override
    public VoxelShape getShape( BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx ) {
        return ALTAR_SHAPE;
    }

    @Override
    public boolean onBlockActivated( BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rtr ) {
        if( ! world.isRemote ) {
            TileEntity te = world.getTileEntity( pos );
            if( te instanceof NetherAltarTileEntity ) {
                player.openContainer( (NetherAltarTileEntity) te );
            }
        }
        return true;
    }

    @Override
    public boolean hasTileEntity( BlockState state ) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity( BlockState state, IBlockReader world ) {
        return new NetherAltarTileEntity();
    }
}
