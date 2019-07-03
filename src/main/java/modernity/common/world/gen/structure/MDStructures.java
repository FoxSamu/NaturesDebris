/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 3 - 2019
 */

package modernity.common.world.gen.structure;

import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureIO;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Method;
import java.util.Locale;

public class MDStructures {

    public static final CaveStructure CAVE_STRUCTURE = new CaveStructure();
    public static final NetherAltarStructure NETHER_ALTAR_STRUCTURE = new NetherAltarStructure();

    public static void register() {
        registerStructure( CaveStructure.Start.class, CaveStructure.NAME );
        registerStructure( NetherAltarStructure.Start.class, NetherAltarStructure.NAME );

        StructureIO.registerStructureComponent( NetherAltarStructure.Piece.class, NetherAltarStructure.NAME + "Piece" );

        registerLocatable( "Nether_Altar", NETHER_ALTAR_STRUCTURE );
    }

    private static final String SRG_StructureIO_registerStructure = "func_143034_b";
    private static Method registerStructureMethod;

    private static void init() {
        if( registerStructureMethod == null ) {
            registerStructureMethod = ObfuscationReflectionHelper.findMethod( StructureIO.class, SRG_StructureIO_registerStructure, Class.class, String.class );
        }
    }

    public static void registerStructure( Class<? extends StructureStart> startClass, String structureName ) {
        try {
            init();
            registerStructureMethod.invoke( null, startClass, structureName );
        } catch( Throwable problem ) {
            throw new RuntimeException( "Failed to register structure", problem );
        }
    }

    public static void registerLocatable( String name, Structure<?> structure ) {
        name = name.toLowerCase( Locale.ROOT );
        if( Feature.STRUCTURES.containsKey( name ) ) {
            throw new IllegalStateException( "Locatable already exists: " + name );
        }
        Feature.STRUCTURES.put( name, structure );
    }
}
