/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.blockold.dirt.logic;

import modernity.common.blockold.dirt.DirtlikeBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Field;
import java.util.function.Supplier;

public final class MDDirtLogics {
    public static final BasicLogicType DIRT_TYPE = new BasicLogicType();
    public static final GrassLogicType GRASS_TYPE = new GrassLogicType();
    public static final GrassLogicType HEATH_TYPE = new GrassLogicType();
    public static final LeafyHumusLogicType LEAFY_HUMUS_TYPE = new LeafyHumusLogicType();
    public static final BasicLogicType HUMUS_TYPE = new BasicLogicType();
    public static final BasicLogicType PODZOL_TYPE = new BasicLogicType();

    public static final FarmlandableDirtLogic DIRT_LOGIC = new FarmlandableDirtLogic(supplyBlock("murky_dirt"), DIRT_TYPE, supply("DIRT_LOGIC_FL"));
    public static final FarmlandDirtLogic DIRT_LOGIC_FL = new FarmlandDirtLogic(supplyBlock("murky_dirt_farmland"), DIRT_TYPE, supply("DIRT_LOGIC"));

    public static final FarmlandableDirtLogic GRASS_LOGIC = new FarmlandableDirtLogic(supplyBlock("murky_grass_block"), GRASS_TYPE, supply("GRASS_LOGIC_FL"));
    public static final FarmlandDirtLogic GRASS_LOGIC_FL = new FarmlandDirtLogic(supplyBlock("murky_grass_block_farmland"), GRASS_TYPE, supply("GRASS_LOGIC"));

    public static final FarmlandableDirtLogic HUMUS_LOGIC = new FarmlandableDirtLogic(supplyBlock("murky_humus"), HUMUS_TYPE, supply("HUMUS_LOGIC_FL"));
    public static final FarmlandDirtLogic HUMUS_LOGIC_FL = new FarmlandDirtLogic(supplyBlock("murky_humus_farmland"), HUMUS_TYPE, supply("HUMUS_LOGIC"));

    public static final FarmlandableDirtLogic LEAFY_HUMUS_LOGIC = new FarmlandableDirtLogic(supplyBlock("leafy_humus"), LEAFY_HUMUS_TYPE, supply("LEAFY_HUMUS_LOGIC_FL"));
    public static final FarmlandDirtLogic LEAFY_HUMUS_LOGIC_FL = new FarmlandDirtLogic(supplyBlock("leafy_humus_farmland"), LEAFY_HUMUS_TYPE, supply("LEAFY_HUMUS_LOGIC"));

    public static final FarmlandableDirtLogic HEATH_LOGIC = new FarmlandableDirtLogic(supplyBlock("heath_block"), HEATH_TYPE, supply("HEATH_LOGIC_FL"));
    public static final FarmlandDirtLogic HEATH_LOGIC_FL = new FarmlandDirtLogic(supplyBlock("heath_farmland"), HEATH_TYPE, supply("HEATH_LOGIC"));

    public static final FarmlandableDirtLogic PODZOL_LOGIC = new FarmlandableDirtLogic(supplyBlock("murky_podzol"), PODZOL_TYPE, supply("PODZOL_LOGIC_FL"));
    public static final FarmlandDirtLogic PODZOL_LOGIC_FL = new FarmlandDirtLogic(supplyBlock("murky_podzol_farmland"), PODZOL_TYPE, supply("PODZOL_LOGIC"));


    private MDDirtLogics() {
    }

    @SuppressWarnings("unchecked")
    public static <T extends DirtLogic> Supplier<T> supply(String fieldName) {
        return () -> {
            try {
                Field field = MDDirtLogics.class.getField(fieldName);
                return (T) field.get(null);
            } catch(Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static <T extends DirtlikeBlock> Supplier<T> supplyBlock(String id) {
        ResourceLocation loc = new ResourceLocation("modernity", id);
        return () -> (T) ForgeRegistries.BLOCKS.getValue(loc);
    }
}
