/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 19 - 2020
 * Author: rgsw
 */

package modernity.common.block.dirt.logic;

import modernity.common.block.farmland.FarmlandBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.redgalaxy.util.Lazy;

import java.util.HashMap;
import java.util.function.Supplier;

public class FarmlandDirtLogic extends DirtLogic {
    private static final HashMap<IDirtLogicType, FarmlandDirtLogic> TYPE_MAP = new HashMap<>();

    private final Lazy<NormalDirtLogic> noFarmland;

    public FarmlandDirtLogic( Supplier<? extends FarmlandBlock> block, IDirtLogicType type, Supplier<? extends NormalDirtLogic> noFarmland ) {
        super( block, type );
        if( ! type.allowOnFarmland() ) {
            throw new IllegalArgumentException( "Type not allowed on farmland dirt logics" );
        }
        if( TYPE_MAP.containsKey( type ) ) {
            throw new IllegalStateException( "Farmland dirt logic type already registered" );
        }
        TYPE_MAP.put( type, this );
        this.noFarmland = Lazy.of( noFarmland );
    }

    @Override
    public DirtLogic switchTo( IDirtLogicType type ) {
        return TYPE_MAP.get( type );
    }

    @Override
    public boolean canSwitchTo( IDirtLogicType type ) {
        return TYPE_MAP.containsKey( type );
    }

    public BlockState makeNormal( IWorld world, BlockPos pos, BlockState state ) {
        return noFarmland.get().switchState( world, pos, state );
    }
}
