/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 19 - 2020
 * Author: rgsw
 */

package modernity.common.block.dirt.logic;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public interface IDirtLogicType {
    boolean allowOnFarmland();
    boolean allowOnNormal();
    boolean canSwitchTo( DirtLogic logic, IDirtLogicType switchTo );

    default boolean randomTicks() {
        return this instanceof IDecayingLogicType || this instanceof ISpreadingLogicType;
    }

    default void blockUpdate( World world, BlockPos pos, BlockState state, Random rand, DirtLogic logic ) {
        boolean spread = true;
        if( this instanceof IDecayingLogicType ) {
            if( ( (IDecayingLogicType) this ).decayTick( world, pos, state, rand ) ) {
                spread = false;
            }
        }
        if( spread && this instanceof ISpreadingLogicType ) {
            ( (ISpreadingLogicType) this ).spreadTick( world, pos, state, rand );
        }
    }
}
