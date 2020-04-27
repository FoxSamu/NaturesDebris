/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.client.util;

// TODO Re-evaluate
///**
// * Handles any client-side hooks.
// */
//@OnlyIn( Dist.CLIENT )
//public final class ClientHooks {
//    public static void preRenderShaders( float partialTicks ) {
//        MinecraftForge.EVENT_BUS.post( new RenderShadersEvent( partialTicks ) );
//    }
//
//    /**
//     * Renders a block. Return null to pass this action to the {@link BlockRendererDispatcher}. Return true when
//     * something is rendered and false when nothing is rendered rendered at all.
//     */
//    public static Boolean onRenderBlock( BlockRendererDispatcher blockRenderer, BlockState state, BlockPos pos, IEnviromentBlockReader world, BufferBuilder buff, Random rand, IModelData modelData ) {
////        if( modelData instanceof EmptyModelData ) {
////            if( state.getBlock() instanceof IModelDataBlock ) {
////                modelData = ( (IModelDataBlock) state.getBlock() ).getModelData( world, pos, state );
////
////                if( modelData == null || modelData instanceof EmptyModelData ) {
////                    modelData = SpecialEmptyModelData.INSTANCE;
////                }
////
////                return blockRenderer.renderBlock( state, pos, world, buff, rand, modelData );
////            }
////        }
//        return null;
//    }
//
//    public static IUnbakedModel onGetUnbakedModel( ModelBakery bakery, ResourceLocation location ) {
//        return null;
//    }
//
//    /**
//     * Renders a fluid. Return null to pass this action to the {@link BlockRendererDispatcher}. Return true when
//     * something is rendered and false when nothing should be rendered rendered at all.
//     */
//    public static Boolean onRenderFluid( BlockRendererDispatcher blockRenderer, BlockPos pos, IEnviromentBlockReader reader, BufferBuilder buff, IFluidState state ) {
//        // Draw vanilla water and lava with our fluid renderer to enable custom rendering behaviour on our blocks...
//        if( state.getFluid() instanceof ICustomRenderFluid || state.getFluid() instanceof WaterFluid || state.getFluid() instanceof LavaFluid ) {
//            return ModernityClient.get().getFluidRenderer().render( reader, pos, buff, state );
//        }
//        // Return null to ignore render action
//        return null;
//    }
//
////    public static void onPlaySound( int source, Vec3d pos ) {
////        MinecraftForge.EVENT_BUS.post( new PlaySoundSourceEvent( source, pos ) );
////    }
//
//    private enum SpecialEmptyModelData implements IModelData {
//        INSTANCE;
//
//        @Override
//        public boolean hasProperty( ModelProperty<?> prop ) {
//            return false;
//        }
//
//        @Nullable
//        @Override
//        public <T> T getData( ModelProperty<T> prop ) {
//            return null;
//        }
//
//        @Nullable
//        @Override
//        public <T> T setData( ModelProperty<T> prop, T data ) {
//            return null;
//        }
//    }
//}
