/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 14 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant.old;

import modernity.api.util.MDVoxelShapes;
import modernity.common.block.MDBlockTags;
import modernity.common.entity.MDEntityTags;
import modernity.common.util.MDDamageSource;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.redgalaxy.util.Lazy;

import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Describes single-block plants that deal damage when entities stand inside this block.
 */
@SuppressWarnings( "deprecation" )
public class DangerousPlantBlock extends SinglePlantBlock {

    private final float damage;
    private final Lazy<DamageSource> damageLazy;
    private final Predicate<Entity> entityFilter;

    public DangerousPlantBlock( double damage, Supplier<DamageSource> damageSupplier, Predicate<Entity> entityFilter, Block.Properties properties ) {
        super( properties );
        this.damage = (float) damage;
        this.damageLazy = Lazy.of( damageSupplier );
        this.entityFilter = entityFilter;
    }

    @Override
    public void onEntityCollision( BlockState state, World world, BlockPos pos, Entity entity ) {
        if( entityFilter.test( entity ) ) {
            entity.attackEntityFrom( damageLazy.get(), damage );
        }
    }

    /**
     * The nettles block.
     */
    public static class Nettles extends DangerousPlantBlock {
        public static final VoxelShape NETTLES_SHAPE = MDVoxelShapes.create16( 1, 0, 1, 15, 14, 15 );

        public Nettles( Block.Properties properties ) {
            super(
                1.5, () -> MDDamageSource.NETTLES,
                e -> e instanceof LivingEntity && ! e.getType().isContained( MDEntityTags.NETTLES_IMMUNE ),
                properties
            );
        }

        @Override
        public boolean canBlockSustain( BlockState state ) {
            return state.isIn( MDBlockTags.DIRTLIKE );
        }

        @Override
        public OffsetType getOffsetType() {
            return OffsetType.XZ;
        }

        @Override
        public VoxelShape getShape( BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx ) {
            Vec3d off = state.getOffset( world, pos );
            return NETTLES_SHAPE.withOffset( off.x, off.y, off.z );
        }
    }
}
