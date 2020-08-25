/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.client.render.entity;

// TODO Re-evaluate
///**
// * Renders a falling block entity (the modernity one)
// */
//@OnlyIn( Dist.CLIENT )
//public class FallBlockRender extends EntityRenderer<FallBlockEntity> {
//    public FallBlockRender( EntityRendererManager manager ) {
//        super( manager );
//        this.shadowSize = 0.5F;
//    }
//
//    @Override
//    public void doRender( FallBlockEntity entity, double x, double y, double z, float entityYaw, float partialTicks ) {
//        BlockState state = entity.getFallingBlock();
//        if( state.getRenderType() == BlockRenderType.MODEL ) {
//
//            World world = entity.getWorldObj();
//            if( state != world.getBlockState( new BlockPos( entity ) ) ) {
//                bindTexture( AtlasTexture.LOCATION_BLOCKS_TEXTURE );
//
//                GlStateManager.pushMatrix();
//                GlStateManager.disableLighting();
//
//                Tessellator tess = Tessellator.getInstance();
//                BufferBuilder buff = tess.getBuffer();
//
//                if( renderOutlines ) {
//                    GlStateManager.enableColorMaterial();
//                    GlStateManager.setupSolidRenderingTextureCombine( getTeamColor( entity ) );
//                }
//
//                buff.begin( 7, DefaultVertexFormats.BLOCK );
//
//                BlockPos pos = new BlockPos( entity.posX, entity.getBoundingBox().maxY, entity.posZ );
//                GlStateManager.translated( x - pos.getX() - 0.5, y - pos.getY(), z - pos.getZ() - 0.5 );
//
//                BlockRendererDispatcher renderer = Minecraft.getInstance().getBlockRendererDispatcher();
//                renderer.getBlockModelRenderer().renderModel(
//                    world,
//                    renderer.getModelForState( state ),
//                    state, pos, buff,
//                    false, new Random(),
//                    MathHelper.getPositionRandom( pos ),
//                    EmptyModelData.INSTANCE
//                );
//
//                tess.draw();
//
//                if( renderOutlines ) {
//                    GlStateManager.tearDownSolidRenderingTextureCombine();
//                    GlStateManager.disableColorMaterial();
//                }
//
//                GlStateManager.enableLighting();
//                GlStateManager.popMatrix();
//                super.doRender( entity, x, y, z, entityYaw, partialTicks );
//            }
//        }
//    }
//
//    @Override
//    protected ResourceLocation getEntityTexture( FallBlockEntity entity ) {
//        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
//    }
//
//    public static class Factory implements IRenderFactory<FallBlockEntity> {
//        @Override
//        public EntityRenderer<? super FallBlockEntity> createRenderFor( EntityRendererManager manager ) {
//            return new FallBlockRender( manager );
//        }
//    }
//}