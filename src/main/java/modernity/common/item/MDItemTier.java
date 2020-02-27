/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 27 - 2020
 * Author: rgsw
 */

package modernity.common.item;

import modernity.common.block.MDBlocks;
import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyLoadBase;

import java.util.function.Supplier;

public enum MDItemTier implements IItemTier {
    BLACKWOOD( 0, 64, 2, 0, 2, () -> {
        return Ingredient.fromItems( MDBlocks.BLACKWOOD_PLANKS );
    } ),
    DARKROCK( 1, 125, 4, 1, 7, () -> {
        return Ingredient.fromItems( MDBlocks.DARKROCK );
    } ),
    ALUMINIUM( 2, 284, 6, 2, 13, () -> {
        return Ingredient.fromItems( MDItems.ALUMINIUM_INGOT );
    } );

    private final int harvestLevel;
    private final int maxUses;
    private final float efficiency;
    private final float attackDamage;
    private final int enchantability;
    private final LazyLoadBase<Ingredient> repairMaterial;

    MDItemTier( int level, int uses, float eff, float damage, int ench, Supplier<Ingredient> mat ) {
        harvestLevel = level;
        maxUses = uses;
        efficiency = eff;
        attackDamage = damage;
        enchantability = ench;
        repairMaterial = new LazyLoadBase<>( mat );
    }

    @Override
    public int getMaxUses() {
        return maxUses;
    }

    @Override
    public float getEfficiency() {
        return efficiency;
    }

    @Override
    public float getAttackDamage() {
        return attackDamage;
    }

    @Override
    public int getHarvestLevel() {
        return harvestLevel;
    }

    @Override
    public int getEnchantability() {
        return enchantability;
    }

    @Override
    public Ingredient getRepairMaterial() {
        return repairMaterial.getValue();
    }
}
