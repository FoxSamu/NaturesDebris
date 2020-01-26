/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 19 - 2020
 * Author: rgsw
 */

package modernity.common.block.dirt.logic;

import modernity.common.block.dirt.DirtlikeBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Field;
import java.util.function.Supplier;

public final class MDDirtLogics {
    public static final BasicLogicType DIRT_TYPE = new BasicLogicType();
    public static final GrassLogicType GRASS_TYPE = new GrassLogicType();
    public static final PodzolLogicType PODZOL_TYPE = new PodzolLogicType();
    public static final BasicLogicType HUMUS_TYPE = new BasicLogicType();

    public static final FarmlandableDirtLogic DIRT_LOGIC = new FarmlandableDirtLogic( supplyBlock( "murky_dirt" ), DIRT_TYPE, supply( "DIRT_LOGIC_FL" ) );
    public static final FarmlandDirtLogic DIRT_LOGIC_FL = new FarmlandDirtLogic( supplyBlock( "murky_dirt_farmland" ), DIRT_TYPE, supply( "DIRT_LOGIC" ) );

    public static final FarmlandableDirtLogic GRASS_LOGIC = new FarmlandableDirtLogic( supplyBlock( "murky_grass_block" ), GRASS_TYPE, supply( "GRASS_LOGIC_FL" ) );
    public static final FarmlandDirtLogic GRASS_LOGIC_FL = new FarmlandDirtLogic( supplyBlock( "murky_grass_block_farmland" ), GRASS_TYPE, supply( "GRASS_LOGIC" ) );

    public static final FarmlandableDirtLogic HUMUS_LOGIC = new FarmlandableDirtLogic( supplyBlock( "humus" ), HUMUS_TYPE, supply( "HUMUS_LOGIC_FL" ) );
    public static final FarmlandDirtLogic HUMUS_LOGIC_FL = new FarmlandDirtLogic( supplyBlock( "murky_humus_farmland" ), HUMUS_TYPE, supply( "HUMUS_LOGIC" ) );

    public static final FarmlandableDirtLogic PODZOL_LOGIC = new FarmlandableDirtLogic( supplyBlock( "murky_podzol" ), PODZOL_TYPE, supply( "PODZOL_LOGIC_FL" ) );
    public static final FarmlandDirtLogic PODZOL_LOGIC_FL = new FarmlandDirtLogic( supplyBlock( "murky_podzol_farmland" ), PODZOL_TYPE, supply( "PODZOL_LOGIC" ) );


    @SuppressWarnings( "unchecked" )
    public static <T extends DirtLogic> Supplier<T> supply( String fieldName ) {
        return () -> {
            try {
                Field field = MDDirtLogics.class.getField( fieldName );
                return (T) field.get( null );
            } catch( Exception e ) {
                throw new RuntimeException( e );
            }
        };
    }

    @SuppressWarnings( "unchecked" )
    public static <T extends DirtlikeBlock> Supplier<T> supplyBlock( String id ) {
        ResourceLocation loc = new ResourceLocation( "modernity", id );
        return () -> (T) ForgeRegistries.BLOCKS.getValue( loc );
    }

    private MDDirtLogics() {
    }
}