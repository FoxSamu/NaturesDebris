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
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class BlockDamagingPlant extends BlockSinglePlant {

    private final float damage;
    private final Supplier<DamageSource> damageSupplier;
    private final Predicate<Entity> entityFilter;

    public BlockDamagingPlant( String id, float damage, Supplier<DamageSource> damageSupplier, Predicate<Entity> entityFilter, Properties properties, Item.Properties itemProps ) {
        super( id, properties, itemProps );
        this.damage = damage;
        this.damageSupplier = damageSupplier;
        this.entityFilter = entityFilter;
    }

    public BlockDamagingPlant( String id, float damage, Supplier<DamageSource> damageSupplier, Predicate<Entity> entityFilter, Properties properties ) {
        super( id, properties );
        this.damage = damage;
        this.damageSupplier = damageSupplier;
        this.entityFilter = entityFilter;
    }

    @Override
    public void onEntityCollision( IBlockState state, World world, BlockPos pos, Entity entity ) {
        if( entityFilter.test( entity ) ) {
            entity.attackEntityFrom( damageSupplier.get(), damage );
        }
    }

    public static class Nettles extends BlockDamagingPlant {
        public static final VoxelShape MINT_SHAPE = MDVoxelShapes.create16( 1, 0, 1, 15, 14, 15 );

        public Nettles( String id, Properties properties, Item.Properties itemProps ) {
            super( id, 1.5F, () -> MDDamageSource.NETTLES, e -> e instanceof EntityLivingBase, properties, itemProps );
        }

        public Nettles( String id, Properties properties ) {
            super( id, 1.5F, () -> MDDamageSource.NETTLES, e -> e instanceof EntityLivingBase, properties );
        }

        @Override
        public boolean canBlockSustain( IBlockState state ) {
            return state.getBlock() instanceof BlockDirt;
        }

        @Override
        public EnumOffsetType getOffsetType() {
            return EnumOffsetType.XZ;
        }

        @Override
        public VoxelShape getShape( IBlockState state, IBlockReader world, BlockPos pos ) {
            Vec3d off = state.getOffset( world, pos );
            return MINT_SHAPE.withOffset( off.x, off.y, off.z );
        }
    }
}
