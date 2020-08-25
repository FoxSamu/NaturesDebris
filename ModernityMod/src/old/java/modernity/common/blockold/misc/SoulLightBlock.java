/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.blockold.misc;

import modernity.common.tileentity.SoulLightTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class SoulLightBlock extends Block {
    private static final VoxelShape HITBOX = makeCuboidShape(4, 4, 4, 12, 12, 12);
    private static final VoxelShape COLLIDER = VoxelShapes.empty();

    public SoulLightBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new SoulLightTileEntity();
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        return HITBOX;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        return COLLIDER;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean addRunningEffects(BlockState state, World world, BlockPos pos, Entity entity) {
        return true;
    }

    @Override
    public boolean addLandingEffects(BlockState state1, ServerWorld worldserver, BlockPos pos, BlockState state2, LivingEntity entity, int numberOfParticles) {
        return true;
    }

    // TODO Re-evaluate destroy effects
//    private SoulLightColor getColor( World world, BlockPos pos ) {
//        TileEntity e = world.getTileEntity( pos );
//        if( e == null ) return SoulLightColor.DEFAULT;
//        if( ! ( e instanceof SoulLightTileEntity ) ) return SoulLightColor.DEFAULT;
//        return ( (SoulLightTileEntity) e ).getColor();
//    }
//
//    @Override
//    @OnlyIn( Dist.CLIENT )
//    public boolean addHitEffects( BlockState state, World world, RayTraceResult target, ParticleManager manager ) {
//        BlockRayTraceResult brtr = (BlockRayTraceResult) target;
//        SoulLightDiggingParticle.addBlockHitEffects( manager, world, brtr.getPos(), brtr.getFace(), getColor( world, brtr.getPos() ) );
//        return true;
//    }
//
//    @Override
//    @OnlyIn( Dist.CLIENT )
//    public boolean addDestroyEffects( BlockState state, World world, BlockPos pos, ParticleManager manager ) {
//        SoulLightDiggingParticle.addBlockDestroyEffects( manager, world, pos, state, getColor( world, pos ) );
//        return true;
//    }
}
