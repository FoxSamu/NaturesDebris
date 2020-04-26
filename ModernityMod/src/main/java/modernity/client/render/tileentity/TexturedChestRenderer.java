/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 15 - 2020
 * Author: rgsw
 */

package modernity.client.render.tileentity;

// TODO Re-evaluate
//@OnlyIn( Dist.CLIENT )
//public class TexturedChestRenderer<T extends TileEntity & IChestLid & ITexturedChest> extends TileEntityRenderer<T> {
//    private final ChestModel simpleChest = new ChestModel();
//    private final ChestModel largeChest = new LargeChestModel();
//
//    public TexturedChestRenderer() {
//    }
//
//    @Override
//    public void render( T chest, double x, double y, double z, float partialTicks, int destroyStage ) {
//        GlStateManager.enableDepthTest();
//        GlStateManager.depthFunc( GL11.GL_LEQUAL );
//        GlStateManager.depthMask( true );
//        BlockState state = chest.hasWorld() ? chest.getBlockState() : MDBuildingBlocks.BLACKWOOD_CHEST.getDefaultState().with( ChestBlock.FACING, Direction.SOUTH );
//        ChestType type = state.has( ChestBlock.TYPE ) ? state.get( ChestBlock.TYPE ) : ChestType.SINGLE;
//
//        if( type != ChestType.LEFT ) {
//            boolean isDouble = type != ChestType.SINGLE;
//            ChestModel model = getChestModel( chest, destroyStage, isDouble );
//            if( destroyStage >= 0 ) {
//                GlStateManager.matrixMode( GL11.GL_TEXTURE );
//                GlStateManager.pushMatrix();
//                GlStateManager.scalef( isDouble ? 8 : 4, 4, 1 );
//                GlStateManager.translatef( 0.0625F, 0.0625F, 0.0625F );
//                GlStateManager.matrixMode( GL11.GL_MODELVIEW );
//            } else {
//                GlStateManager.color4f( 1, 1, 1, 1 );
//            }
//
//            GlStateManager.pushMatrix();
//            GlStateManager.enableRescaleNormal();
//            GlStateManager.translatef( (float) x, (float) y + 1, (float) z + 1 );
//            GlStateManager.scalef( 1, - 1, - 1 );
//
//            float angle = state.get( ChestBlock.FACING ).getHorizontalAngle();
//            if( (double) Math.abs( angle ) > 1.0E-5D ) {
//                GlStateManager.translatef( 0.5F, 0.5F, 0.5F );
//                GlStateManager.rotatef( angle, 0, 1, 0 );
//                GlStateManager.translatef( - 0.5F, - 0.5F, - 0.5F );
//            }
//
//            applyLidRotation( chest, partialTicks, model );
//            model.renderAll();
//            GlStateManager.disableRescaleNormal();
//            GlStateManager.popMatrix();
//            GlStateManager.color4f( 1, 1, 1, 1 );
//            if( destroyStage >= 0 ) {
//                GlStateManager.matrixMode( GL11.GL_TEXTURE );
//                GlStateManager.popMatrix();
//                GlStateManager.matrixMode( GL11.GL_MODELVIEW );
//            }
//
//        }
//    }
//
//    private ChestModel getChestModel( T chest, int destroyStage, boolean doubleChest ) {
//        ResourceLocation tex;
//        if( destroyStage >= 0 ) {
//            tex = DESTROY_STAGES[ destroyStage ];
//        } else {
//            tex = chest.getChestTexture( doubleChest );
//        }
//
//        bindTexture( tex );
//        return doubleChest ? largeChest : simpleChest;
//    }
//
//    private void applyLidRotation( T chest, float partialTicks, ChestModel model ) {
//        float angle = chest.getLidAngle( partialTicks );
//        angle = 1 - angle;
//        angle = 1 - angle * angle * angle;
//        model.getLid().rotateAngleX = - ( angle * ( (float) Math.PI / 2F ) );
//    }
//}