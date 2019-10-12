/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 8 - 28 - 2019
 */

package modernity.common.block.base;

import modernity.api.util.MDVoxelShapes;
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

import java.util.function.Predicate;
import java.util.function.Supplier;

@SuppressWarnings( "deprecation" )
public class DangerousPlantBlock extends SinglePlantBlock {

    private final float damage;
    private final Supplier<DamageSource> damageSupplier;
    private final Predicate<Entity> entityFilter;

    public DangerousPlantBlock( double damage, Supplier<DamageSource> damageSupplier, Predicate<Entity> entityFilter, Block.Properties properties ) {
        super( properties );
        this.damage = (float) damage;
        this.damageSupplier = damageSupplier;
        this.entityFilter = entityFilter;
    }

    @Override
    public void onEntityCollision( BlockState state, World world, BlockPos pos, Entity entity ) {
        if( entityFilter.test( entity ) ) {
            entity.attackEntityFrom( damageSupplier.get(), damage );
        }
    }

    public static class Nettles extends DangerousPlantBlock {
        public static final VoxelShape MINT_SHAPE = MDVoxelShapes.create16( 1, 0, 1, 15, 14, 15 );

        public Nettles( Block.Properties properties ) {
            super( 1.5, () -> MDDamageSource.NETTLES, e -> e instanceof LivingEntity, properties );
        }

        @Override
        public boolean canBlockSustain( BlockState state ) {
            return state.getBlock() instanceof DirtBlock;
        }

        @Override
        public OffsetType getOffsetType() {
            return OffsetType.XZ;
        }

        @Override
        public VoxelShape getShape( BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx ) {
            Vec3d off = state.getOffset( world, pos );
            return MINT_SHAPE.withOffset( off.x, off.y, off.z );
        }
    }
}
