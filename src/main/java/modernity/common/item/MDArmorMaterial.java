/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 9 - 2 - 2019
 */

package modernity.common.item;

import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyLoadBase;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Supplier;

public enum MDArmorMaterial implements IArmorMaterial {
    ALUMINIUM( "modernity:aluminium", 15, new int[] { 2, 5, 6, 2 }, 9, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F, () -> {
        return Ingredient.fromItems( MDItems.ALUMINIUM_INGOT );
    } );

    /** Holds the 'base' maxDamage that each armorType have. */
    private static final int[] MAX_DAMAGE_ARRAY = { 13, 15, 16, 11 };
    private final String texName;
    /**
     * Holds the maximum damage factor (each piece multiply this by it's own value) of the material, this is the item
     * damage (how much can absorb before breaks)
     */
    private final int maxDamageFactor;
    /**
     * Holds the damage reduction (each 1 points is half a shield on gui) of each piece of armor (helmet, plate, legs
     * and boots)
     */
    private final int[] damageReductionAmountArray;
    /** Return the enchantability factor of the material */
    private final int enchantability;
    private final SoundEvent soundEvent;
    private final float toughness;
    private final LazyLoadBase<Ingredient> repairMaterial;

    MDArmorMaterial( String texName, int maxDamageFactor, int[] damageReductions, int enchantability, SoundEvent sound, float toughness, Supplier<Ingredient> p_i48533_9_ ) {
        this.texName = texName;
        this.maxDamageFactor = maxDamageFactor;
        this.damageReductionAmountArray = damageReductions;
        this.enchantability = enchantability;
        this.soundEvent = sound;
        this.toughness = toughness;
        this.repairMaterial = new LazyLoadBase<>( p_i48533_9_ );
    }

    public int getDurability( EntityEquipmentSlot slotIn ) {
        return MAX_DAMAGE_ARRAY[ slotIn.getIndex() ] * this.maxDamageFactor;
    }

    public int getDamageReductionAmount( EntityEquipmentSlot slotIn ) {
        return this.damageReductionAmountArray[ slotIn.getIndex() ];
    }

    public int getEnchantability() {
        return this.enchantability;
    }

    public SoundEvent getSoundEvent() {
        return this.soundEvent;
    }

    public Ingredient getRepairMaterial() {
        return this.repairMaterial.getValue();
    }

    @OnlyIn( Dist.CLIENT )
    public String getName() {
        return this.texName;
    }

    public float getToughness() {
        return this.toughness;
    }
}
