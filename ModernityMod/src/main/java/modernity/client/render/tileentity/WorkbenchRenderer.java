/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.client.render.tileentity;

// TODO Re-evaluate
///**
// * The renderer of a {@link WorkbenchTileEntity}. This renderer renders items on top of a workbench block.
// */
//@SuppressWarnings( "deprecation" )
//public class WorkbenchRenderer extends TileEntityRenderer<WorkbenchTileEntity> {
//    private static final float ITEM_OFFSET = 3 / 16F;
//    private static final float ITEM_SCALE = 0.18F;
//    private static final float ITEM_THICKNESS = 1 * 0.2F / 16;
//    private static final float ANTI_Z_FIGHTING = 0.0001F;
//    private static final float Y_OFFSET = 1 + ITEM_THICKNESS + ANTI_Z_FIGHTING;
//
//    private final ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
//
//    @Override
//    public void render( WorkbenchTileEntity tile, double x, double y, double z, float partialTicks, int destroyStage ) {
//        if( ! tile.canRenderItemsOnTop() ) return;
//
//        BlockState state = tile.getBlockState();
//        Direction facing = state.get( WorkbenchBlock.FACING );
//
//        int rotation = ( 2 - facing.getHorizontalIndex() ) * 90;
//
//        GlStateManager.pushMatrix();
//        GlStateManager.translated( x + 0.5, y + Y_OFFSET, z + 0.5 );
//        GlStateManager.rotated( rotation, 0, 1, 0 );
//
//        // Workbench is a full block with zero light so we take the light from the block above (the items lay upon the
//        // workbench, they're not in it)
//        int combinedLight = tile.getWorld().getCombinedLight( tile.getPos().up(), 0 );
//        int blockLight = combinedLight % 65536;
//        int skyLight = combinedLight / 65536;
//
//        float lastBrightnessX = GLX.lastBrightnessX;
//        float lastBrightnessY = GLX.lastBrightnessY;
//        GLX.glMultiTexCoord2f( GLX.GL_TEXTURE1, blockLight, skyLight );
//
//        for( int i = 0; i < 9; i++ ) {
//            ItemStack stack = tile.getStackInSlot( i );
//
//            int slotX = 1 - i % 3;
//            int slotZ = 1 - i / 3;
//
//            float renderX = slotX * ITEM_OFFSET;
//            float renderZ = slotZ * ITEM_OFFSET;
//
//            GlStateManager.pushMatrix();
//            GlStateManager.translatef( renderX, 0, renderZ );
//            GlStateManager.scalef( ITEM_SCALE, ITEM_SCALE, ITEM_SCALE );
//            GlStateManager.rotatef( 90, 1, 0, 0 );
//
//            itemRenderer.renderItem( stack, ItemCameraTransforms.TransformType.FIXED );
//
//            GlStateManager.popMatrix();
//        }
//
//        // Reset lightmap coords
//        GLX.glMultiTexCoord2f( GLX.GL_TEXTURE1, lastBrightnessX, lastBrightnessY );
//
//        GlStateManager.popMatrix();
//    }
//}
