/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.generator.decorate.decorator;

import natures.debris.common.generator.decorate.condition.Always;
import natures.debris.common.generator.decorate.condition.IDecorCondition;
import natures.debris.common.generator.decorate.count.IDecorCount;
import natures.debris.common.generator.decorate.count.One;
import natures.debris.common.generator.decorate.decoration.IDecoration;
import natures.debris.common.generator.decorate.position.IDecorPosition;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;

import java.util.Random;

public class DecorationDecorator implements IDecorator {

    private final IDecoration decoration;
    private final IDecorPosition position;
    private final IDecorCount count;
    private final IDecorCondition condition;

    public DecorationDecorator(IDecoration decoration, IDecorPosition position, IDecorCount count, IDecorCondition condition) {
        this.decoration = decoration;
        this.position = position;
        this.count = count;
        this.condition = condition;
    }

    public DecorationDecorator(IDecoration decoration, IDecorPosition position, IDecorCount count) {
        this(decoration, position, count, new Always());
    }

    public DecorationDecorator(IDecoration decoration, IDecorPosition position, IDecorCondition condition) {
        this(decoration, position, new One(), condition);
    }

    public DecorationDecorator(IDecoration decoration, IDecorPosition position) {
        this(decoration, position, new One(), new Always());
    }


    @Override
    public void decorate(IWorld world, int cx, int cz, Biome biome, Random rand, ChunkGenerator<?> chunkGenerator) {
        int cnt = count.count(world, cx, cz, rand);
        for (int i = 0; i < cnt; i++) {
            BlockPos pos = position.findPosition(world, cx, cz, rand);
            if (condition.canGenerate(world, pos, rand)) {
                decoration.generate(world, pos, rand, chunkGenerator);
            }
        }
    }
}
