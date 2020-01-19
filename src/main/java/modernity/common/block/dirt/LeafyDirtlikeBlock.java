/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 19 - 2020
 * Author: rgsw
 */

package modernity.common.block.dirt;

import modernity.api.reflect.FieldAccessor;
import modernity.common.block.dirt.logic.DirtLogic;
import modernity.common.particle.MDParticleTypes;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class LeafyDirtlikeBlock extends SnowyDirtlikeBlock {
    private static final FieldAccessor<Entity, Float> nextStepDistanceField = new FieldAccessor<>( Entity.class, "field_70150_b" );

    public LeafyDirtlikeBlock( DirtLogic logic, Properties properties ) {
        super( logic, properties );
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public void onFallenUpon( World world, BlockPos pos, Entity entity, float fallDistance ) {
        super.onFallenUpon( world, pos, entity, fallDistance );
        // Fallen leaf particles
        if( fallDistance > 0.1 ) {
            int amount = Math.min( (int) ( fallDistance * 10 ), 30 );
            if( amount > 0 ) {
                Random rand = world.rand;
                for( int i = 0; i < amount; i++ ) {
                    double mx = entity.getMotion().x * 0.2 + ( rand.nextDouble() * 0.3 - 0.15 );
                    double mz = entity.getMotion().z * 0.2 + ( rand.nextDouble() * 0.3 - 0.15 );
                    double my = rand.nextDouble() * 0.2;
                    double x = rand.nextDouble() * 0.6 - 0.3 + entity.posX;
                    double y = pos.getY() + 1.05;
                    double z = rand.nextDouble() * 0.6 - 0.3 + entity.posZ;

                    world.addParticle( MDParticleTypes.FALLEN_LEAF, x, y, z, mx, my, mz );
                }
            }
        }
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public void onEntityWalk( World world, BlockPos pos, Entity entity ) {
        // Fallen leaf particles
        if( ! entity.isInWater() ) {
            // Get entity.nextStepDistance via reflection
            double nsd = nextStepDistanceField.get( entity );
            double d = nsd - entity.distanceWalkedOnStepModified;
            double s = entity.getMotion().lengthSquared();

            if( d < 0.15 && s > 0.01 * 0.01 ) {
                Random rand = world.rand;
                for( int i = 0; i < 12; i++ ) {
                    double x = rand.nextDouble() * 0.6 - 0.3 + entity.posX;
                    double y = pos.getY() + 1.05;
                    double z = rand.nextDouble() * 0.6 - 0.3 + entity.posZ;
                    double mx = entity.getMotion().x * 1.05 + ( rand.nextDouble() * 0.15 - 0.075 );
                    double mz = entity.getMotion().z * 1.05 + ( rand.nextDouble() * 0.15 - 0.075 );
                    double my = rand.nextDouble() * 0.1;


                    world.addParticle( MDParticleTypes.FALLEN_LEAF, x, y, z, mx, my, mz );
                }
            }
        }
    }
}
