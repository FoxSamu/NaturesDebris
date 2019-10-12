/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 12 - 2019
 */

package modernity.client.render.entity;

import com.mojang.blaze3d.platform.GlStateManager;
import modernity.common.entity.EntityFallBlock;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.fml.client.registry.IRenderFactory;

import java.util.Random;

@OnlyIn( Dist.CLIENT )
public class RenderFallBlock extends EntityRenderer<EntityFallBlock> {
    public RenderFallBlock( EntityRendererManager manager ) {
        super( manager );
        this.shadowSize = 0.5F;
    }

    @Override
    public void doRender( EntityFallBlock entity, double x, double y, double z, float entityYaw, float partialTicks ) {
        BlockState state = entity.getFallingBlock();
        if( state.getRenderType() == BlockRenderType.MODEL ) {

            World world = entity.getWorldObj();
            if( state != world.getBlockState( new BlockPos( entity ) ) ) {
                bindTexture( AtlasTexture.LOCATION_BLOCKS_TEXTURE );

                GlStateManager.pushMatrix();
                GlStateManager.disableLighting();

                Tessellator tess = Tessellator.getInstance();
                BufferBuilder buff = tess.getBuffer();

                if( renderOutlines ) {
                    GlStateManager.enableColorMaterial();
                    GlStateManager.setupSolidRenderingTextureCombine( getTeamColor( entity ) );
                }

                buff.begin( 7, DefaultVertexFormats.BLOCK );

                BlockPos pos = new BlockPos( entity.posX, entity.getBoundingBox().maxY, entity.posZ );
                GlStateManager.translated( x - pos.getX() - 0.5, y - pos.getY(), z - pos.getZ() - 0.5 );

                BlockRendererDispatcher renderer = Minecraft.getInstance().getBlockRendererDispatcher();
                renderer.getBlockModelRenderer().renderModel(
                    world,
                    renderer.getModelForState( state ),
                    state, pos, buff,
                    false, new Random(),
                    MathHelper.getPositionRandom( pos ),
                    EmptyModelData.INSTANCE
                );

                tess.draw();

                if( renderOutlines ) {
                    GlStateManager.tearDownSolidRenderingTextureCombine();
                    GlStateManager.disableColorMaterial();
                }

                GlStateManager.enableLighting();
                GlStateManager.popMatrix();
                super.doRender( entity, x, y, z, entityYaw, partialTicks );
            }
        }
    }

    @Override
    protected ResourceLocation getEntityTexture( EntityFallBlock entity ) {
        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
    }

    public static class Factory implements IRenderFactory<EntityFallBlock> {
        @Override
        public EntityRenderer<? super EntityFallBlock> createRenderFor( EntityRendererManager manager ) {
            return new RenderFallBlock( manager );
        }
    }
}