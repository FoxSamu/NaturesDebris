/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 22 - 2019
 * Author: rgsw
 */

package modernity.common.generator.decorate.condition;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import java.util.Random;

public class Or implements IDecorCondition {
    private final IDecorCondition a;
    private final IDecorCondition b;

    public Or( IDecorCondition a, IDecorCondition b ) {
        this.a = a;
        this.b = b;
    }

    @Override
    public boolean canGenerate( IWorld world, BlockPos pos, Random rand ) {
        return a.canGenerate( world, pos, rand ) || b.canGenerate( world, pos, rand );
    }
}
