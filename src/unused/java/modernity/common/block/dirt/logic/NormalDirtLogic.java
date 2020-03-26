/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 19 - 2020
 * Author: rgsw
 */

package modernity.common.block.dirt.logic;

import modernity.common.block.dirt.DirtlikeBlock;

import java.util.HashMap;
import java.util.function.Supplier;

public class NormalDirtLogic extends DirtLogic {
    private static final HashMap<IDirtLogicType, NormalDirtLogic> TYPE_MAP = new HashMap<>();

    public NormalDirtLogic( Supplier<? extends DirtlikeBlock> block, IDirtLogicType type ) {
        super( block, type );
        if( ! type.allowOnNormal() ) {
            throw new IllegalArgumentException( "Type not allowed on normal dirt logics" );
        }
        if( TYPE_MAP.containsKey( type ) ) {
            throw new IllegalStateException( "Normal dirt logic type already registered" );
        }
        TYPE_MAP.put( type, this );
    }

    @Override
    public DirtLogic switchTo( IDirtLogicType type ) {
        return TYPE_MAP.get( type );
    }

    @Override
    public boolean canSwitchTo( IDirtLogicType type ) {
        return TYPE_MAP.containsKey( type );
    }
}
