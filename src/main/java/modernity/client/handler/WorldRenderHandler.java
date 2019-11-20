/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 20 - 2019
 * Author: rgsw
 */

package modernity.client.handler;

import modernity.api.dimension.IEnvironmentDimension;
import modernity.client.ModernityClient;
import modernity.client.environment.EnvironmentRenderingManager;
import modernity.common.biome.ModernityBiome;
import modernity.common.environment.precipitation.IPrecipitation;
import modernity.common.environment.precipitation.IPrecipitationFunction;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.settings.ParticleStatus;
import net.minecraft.fluid.IFluidState;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;

public enum WorldRenderHandler {
    INSTANCE;

    private final Random rand = new Random();
    private final Minecraft mc = Minecraft.getInstance();
    private int rainSoundCounter;
    private int renderUpdateCount;

    @SubscribeEvent
    public void onRenderWorldLast( RenderWorldLastEvent event ) {
        ModernityClient.get().getAreaRenderManager().renderAreas( event.getPartialTicks() );

        // TODO: Add render last particles and render them here if queued...
    }

    @SubscribeEvent
    public void onClientTick( TickEvent.ClientTickEvent event ) {
        renderUpdateCount++;

        if( mc.world != null && ! mc.isGamePaused() ) {
            if( mc.world.dimension instanceof IEnvironmentDimension ) {
                addRainParticles();
            }
        }
    }

    private void addRainParticles() {
        if( mc.world.dimension instanceof IEnvironmentDimension ) {
            ( (IEnvironmentDimension) mc.world.dimension )
                .updatePrecipitation( EnvironmentRenderingManager.PRECIPITATION );
        }

        ActiveRenderInfo activeRender = mc.gameRenderer.getActiveRenderInfo();
        float rainStrength = EnvironmentRenderingManager.PRECIPITATION.strength;
        int rainLevel = EnvironmentRenderingManager.PRECIPITATION.level;
        if( ! mc.gameSettings.fancyGraphics ) {
            rainStrength /= 2;
        }

        if( rainStrength != 0 && rainLevel != 0 ) {
            rand.setSeed( renderUpdateCount * 312987231L );
            IWorldReader world = mc.world;
            BlockPos viewPos = new BlockPos( activeRender.getProjectedView() );

            double soundX = 0;
            double soundY = 0;
            double soundZ = 0;
            int soundCounter = 0;


            int particleCount = (int) ( 100 * rainStrength * rainStrength );
            if( mc.gameSettings.particles == ParticleStatus.DECREASED ) {
                particleCount >>= 1;
            } else if( mc.gameSettings.particles == ParticleStatus.MINIMAL ) {
                particleCount = 0;
            }

            for( int i = 0; i < particleCount; ++ i ) {
                BlockPos pos = viewPos.add( rand.nextInt( 10 ) - rand.nextInt( 10 ), 0, rand.nextInt( 10 ) - rand.nextInt( 10 ) );
                Biome biome = world.getBiome( pos );

                IPrecipitationFunction precFn = ( (ModernityBiome) biome ).getPrecipitationFunction();
                IPrecipitation prec = precFn.computePrecipitation( rainLevel );

                boolean particles = prec.hasParticles( mc.world.rand );
                boolean sound = prec.hasSound( mc.world.rand );

                int height = prec.getHeight( mc.world, pos.getX(), pos.getZ() );
                pos = new BlockPos( pos.getX(), height, pos.getZ() );
                BlockPos down = pos.down();

                if( ( particles || sound ) && pos.getY() <= viewPos.getY() + 10 && pos.getY() >= viewPos.getY() - 10 ) {
                    double px = rand.nextDouble();
                    double pz = rand.nextDouble();
                    BlockState state = world.getBlockState( down );
                    IFluidState fluid = world.getFluidState( pos );
                    double rainY = 0;
                    double smokeY = 0;
                    if( particles ) {
                        VoxelShape shape = state.getCollisionShape( world, down );
                        double shapeMax = shape.max( Direction.Axis.Y, px, pz );
                        double fluidMax = fluid.func_215679_a( world, pos );
                        if( shapeMax >= fluidMax ) {
                            rainY = shapeMax;
                            smokeY = shape.min( Direction.Axis.Y, px, pz );
                        }
                    }

                    if( rainY > - Double.MAX_VALUE ) {
                        if( ! fluid.isTagged( FluidTags.LAVA ) && state.getBlock() != Blocks.MAGMA_BLOCK && ( state.getBlock() != Blocks.CAMPFIRE || ! state.get( CampfireBlock.LIT ) ) ) {
                            if( sound ) {
                                soundCounter++;
                                if( rand.nextInt( soundCounter ) == 0 ) {
                                    soundX = down.getX() + px;
                                    soundY = down.getY() + 0.1 + rainY - 1;
                                    soundZ = down.getZ() + pz;
                                }
                            }

                            if( particles ) {
                                IParticleData particle = prec.getParticleType( rand );

                                mc.world.addParticle( particle, down.getX() + px, down.getY() + 0.1 + rainY, down.getZ() + pz, 0, 0, 0 );
                            }
                        } else if( particles ) {
                            mc.world.addParticle( ParticleTypes.SMOKE, pos.getX() + px, pos.getY() + 0.1 - smokeY, pos.getZ() + pz, 0, 0, 0 );
                        }
                    }
                }
            }

            if( soundCounter > 0 && rand.nextInt( 3 ) < rainSoundCounter++ ) {
                rainSoundCounter = 0;
                BlockPos soundPos = new BlockPos( soundX, soundY, soundZ );
                ModernityBiome biome = (ModernityBiome) world.getBiome( soundPos );
                IPrecipitationFunction precFn = biome.getPrecipitationFunction();
                IPrecipitation prec = precFn.computePrecipitation( rainLevel );

                int height = prec.getHeight( mc.world, soundPos.getX(), soundPos.getZ() );
                boolean above = soundY > viewPos.getY() + 1 && height > viewPos.getY();

                prec.playSound( soundX, soundY, soundZ, above, mc.world, rainStrength );
            }

        }
    }
}
