/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.item;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Supplier;

/**
 * Types of Modernity armor material
 */
public enum MDArmorMaterial implements IArmorMaterial {
    ALUMINIUM( "modernity:aluminium", 15, new int[] { 2, 5, 6, 2 }, 10, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0, () -> {
        return Ingredient.fromItems( MDItems.ALUMINIUM_INGOT );
    } );

    private static final int[] MAX_DAMAGE_ARRAY = { 13, 15, 16, 11 };
    private final String texName;
    private final int maxDamageFactor;
    private final int[] damageReductionAmountArray;
    private final int enchantability;
    private final SoundEvent soundEvent;
    private final float toughness;
    private final LazyValue<Ingredient> repairMaterial;

    MDArmorMaterial( String texName, int maxDamageFactor, int[] damageReductions, int enchantability, SoundEvent sound, float toughness, Supplier<Ingredient> p_i48533_9_ ) {
        this.texName = texName;
        this.maxDamageFactor = maxDamageFactor;
        this.damageReductionAmountArray = damageReductions;
        this.enchantability = enchantability;
        this.soundEvent = sound;
        this.toughness = toughness;
        this.repairMaterial = new LazyValue<>( p_i48533_9_ );
    }

    @Override
    public int getDurability( EquipmentSlotType slotIn ) {
        return MAX_DAMAGE_ARRAY[ slotIn.getIndex() ] * this.maxDamageFactor;
    }

    @Override
    public int getDamageReductionAmount( EquipmentSlotType slotIn ) {
        return this.damageReductionAmountArray[ slotIn.getIndex() ];
    }

    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    @Override
    public SoundEvent getSoundEvent() {
        return this.soundEvent;
    }

    @Override
    public Ingredient getRepairMaterial() {
        return this.repairMaterial.getValue();
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public String getName() {
        return this.texName;
    }

    @Override
    public float getToughness() {
        return this.toughness;
    }
}
