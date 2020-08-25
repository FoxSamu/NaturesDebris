/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.client.handler;

// TODO Re-evaluate
//public enum FogHandler {
//    INSTANCE;
//
//    private final Minecraft mc = Minecraft.getInstance();
//    private final GameRenderer gameRenderer = mc.gameRenderer;
//
//    private final FloatBuffer colorBuffer = GLAllocation.createDirectFloatBuffer( 16 );
//
//    private void setColor( float r, float g, float b ) {
//        colorBuffer.put( r );
//        colorBuffer.put( g );
//        colorBuffer.put( b );
//        colorBuffer.put( 1 );
//        colorBuffer.flip();
//        GlStateManager.fog( GL11.GL_FOG_COLOR, colorBuffer );
//    }
//
//    private void setColor( int rgb ) {
//        colorBuffer.put( ColorUtil.redf( rgb ) );
//        colorBuffer.put( ColorUtil.greenf( rgb ) );
//        colorBuffer.put( ColorUtil.bluef( rgb ) );
//        colorBuffer.put( 1 );
//        colorBuffer.flip();
//        GlStateManager.fog( GL11.GL_FOG_COLOR, colorBuffer );
//    }
//
//    @SubscribeEvent
//    public void onRenderFog( EntityViewRenderEvent.FogDensity event ) {
//        ActiveRenderInfo renderInfo = event.getInfo();
//        IFluidState fluid = renderInfo.getFluidState();
//        float partialTicks = (float) event.getRenderPartialTicks();
//        event.setCanceled( true );
//
//        boolean skipEnablingFog = false;
//        if( renderInfo.getRenderViewEntity() instanceof LivingEntity && ( (LivingEntity) renderInfo.getRenderViewEntity() ).isPotionActive( Effects.BLINDNESS ) ) {
//            float distance = 5;
//            int blindnessDuration = ( (LivingEntity) renderInfo.getRenderViewEntity() ).getActivePotionEffect( Effects.BLINDNESS ).getDuration();
//            if( blindnessDuration < 20 ) {
//                distance = MathHelper.lerp( 1 - blindnessDuration / 20F, 5, gameRenderer.getFarPlaneDistance() );
//            }
//
//            GlStateManager.fogMode( GlStateManager.FogMode.LINEAR );
//            GlStateManager.fogStart( distance * 0.25F );
//            GlStateManager.fogEnd( distance );
//            setColor( 0, 0, 0 );
//
//            event.setDensity( 1 );
//
//            GLX.setupNvFogDistance();
//        } else if( fluid.isTagged( MDFluidTags.MURKY_WATER ) ) {
//            GlStateManager.fogMode( GlStateManager.FogMode.EXP2 );
//            int waterColor = ModernityClient.get().getWaterColors().getColor( mc.world, renderInfo.getBlockPos() );
//            setColor( ColorUtil.darken( waterColor, 0.4 ) );
//            if( renderInfo.getRenderViewEntity() instanceof LivingEntity ) {
//                if( renderInfo.getRenderViewEntity() instanceof ClientPlayerEntity ) {
//                    ClientPlayerEntity player = (ClientPlayerEntity) renderInfo.getRenderViewEntity();
//
//                    float density = 0.05F - player.getWaterBrightness() * player.getWaterBrightness() * 0.03F;
//                    Biome biome = player.world.getBiome( new BlockPos( player ) );
//                    if( biome == Biomes.SWAMP || biome == Biomes.SWAMP_HILLS ) {
//                        density += 0.005F;
//                    }
//                    if( biome instanceof ModernityBiome ) {
//                        density += ( (ModernityBiome) biome ).getWaterFogDensity();
//                    }
//
//                    event.setDensity( density );
//                } else {
//                    event.setDensity( 0.05F );
//                }
//            } else {
//                event.setDensity( 0.1F );
//            }
//        } else if( fluid.isTagged( MDFluidTags.MOLTEN_ROCK ) ) {
//            GlStateManager.fogMode( GlStateManager.FogMode.EXP );
//            setColor( 0xfc9803 );
//            event.setDensity( 2 );
//        } else if( mc.world.dimension instanceof IEnvironmentDimension ) {
//            IEnvironmentDimension d = (IEnvironmentDimension) mc.world.dimension;
//            d.updateFog( EnvironmentRenderingManager.FOG );
//            Fog fog = EnvironmentRenderingManager.FOG;
//
//            if( fog.type == Fog.Type.VANILLA ) {
//                event.setCanceled( false );
//                return;
//            } else if( fog.type == Fog.Type.DISABLED ) {
//                GlStateManager.disableFog();
//                skipEnablingFog = true;
//            } else {
//                switch( fog.type ) {
//                    case EXP:
//                        GlStateManager.fogMode( GlStateManager.FogMode.EXP );
//                        break;
//                    case EXP2:
//                        GlStateManager.fogMode( GlStateManager.FogMode.EXP2 );
//                        break;
//                    case LINEAR:
//                        GlStateManager.fogMode( GlStateManager.FogMode.LINEAR );
//                        break;
//                }
//                GlStateManager.fogStart( fog.start );
//                GlStateManager.fogEnd( fog.end );
//                setColor(
//                    fog.color[ 0 ],
//                    fog.color[ 1 ],
//                    fog.color[ 2 ]
//                );
//                event.setDensity( fog.density );
//            }
//        } else {
//            event.setCanceled( false );
//            return;
//        }
//        GlStateManager.enableColorMaterial();
//        if( ! skipEnablingFog ) GlStateManager.enableFog();
//        GlStateManager.colorMaterial( 1028, 4608 );
//    }
//}
