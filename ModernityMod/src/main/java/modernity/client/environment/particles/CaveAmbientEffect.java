/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 14 - 2020
 * Author: rgsw
 */

package modernity.client.environment.particles;

// TODO Re-evaluate
//public class CaveAmbientEffect implements IEnvironmentParticleEffect {
//    @Override
//    public void addParticleEffect( World world, BlockPos pos, Random rand ) {
//        if( rand.nextInt( 1000 ) == 0 ) {
//            int y = CaveUtil.caveHeight( pos.getX(), pos.getZ(), world );
//            if( pos.getY() < y - 3 ) {
//                world.addParticle(
//                    new RgbParticleData( MDParticleTypes.AMBIENT, randomColor( rand ) ),
//                    rand.nextDouble() + pos.getX(),
//                    rand.nextDouble() + pos.getY(),
//                    rand.nextDouble() + pos.getZ(),
//                    0, 0, 0
//                );
//            }
//        }
//    }
//
//    private static int randomColor( Random rand ) {
//        double ipl = rand.nextDouble();
//        return ColorUtil.interpolate( 0xa39f93, 0xdbd9cc, ipl );
//    }
//}
