/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 24 - 2019
 * Author: rgsw
 */

package modernity.common.generator.structure;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;

import java.util.HashMap;
import java.util.Map;

/**
 * Holder class for Modernity structure pieces.
 */
public final class MDStructurePieceTypes {
    private static final HashMap<String, IStructurePieceType> PIECES = new HashMap<>();

    public static final IStructurePieceType CAVE_DATA = register( "cave_data", CaveStructure.Piece::new );
    public static final IStructurePieceType FOREST_RUNES = register( "forest_runes", ForestRunesStructure.Piece::new );

    private static <T extends IStructurePieceType> T register( String id, T type ) {
        PIECES.put( "modernity:" + id, type );
        return type;
    }

    public static void registerPieces() {
        for( Map.Entry<String, IStructurePieceType> entry : PIECES.entrySet() ) {
            Registry.register( Registry.STRUCTURE_PIECE, entry.getKey(), entry.getValue() );
        }
    }

    private MDStructurePieceTypes() {
    }
}
