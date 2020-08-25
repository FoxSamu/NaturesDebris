/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.client.model;


import net.minecraft.client.renderer.TransformationMatrix;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.model.pipeline.BakedQuadBuilder;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;
import net.minecraftforge.client.model.pipeline.TRSRTransformer;
import modernity.api.util.math.ColorUtil;

public class QuadMaker {
    private static final VertexFormat BLOCK_FORMAT = DefaultVertexFormats.BLOCK;
    private final float[] data = new float[11];
    private IVertexConsumer consumer;
    private BakedQuadBuilder builder;
    private TextureAtlasSprite sprite;
    private boolean fixBleeding;

    public QuadMaker() {
    }

    public void start(TransformationMatrix transform, TextureAtlasSprite tas, boolean contractUVs) {
        sprite = tas;
        BakedQuadBuilder bqb = new BakedQuadBuilder(tas);
        bqb.setContractUVs(contractUVs);
        consumer = builder = bqb;
        if(!transform.isIdentity()) {
            consumer = new TRSRTransformer(consumer, transform);
        }

        // Reset
        pos(0, 0, 0);
        col(1, 1, 1);
        tex0(0, 0);
        nor(0, 1, 0);
    }


    public QuadMaker pos(float x, float y, float z) {
        data[0] = x;
        data[1] = y;
        data[2] = z;
        return this;
    }

    public QuadMaker pos(float x, float y, float z, float div) {
        data[0] = x / 16;
        data[1] = y / 16;
        data[2] = z / 16;
        return this;
    }

    public QuadMaker col(float r, float g, float b) {
        data[3] = r;
        data[4] = g;
        data[5] = b;
        return this;
    }

    public QuadMaker col(float r, float g, float b, float div) {
        data[3] = r / div;
        data[4] = g / div;
        data[5] = b / div;
        return this;
    }

    public QuadMaker col(int col) {
        data[3] = ColorUtil.redf(col);
        data[4] = ColorUtil.greenf(col);
        data[5] = ColorUtil.bluef(col);
        return this;
    }

    public QuadMaker tex(float u, float v) {
        data[6] = u(u);
        data[7] = v(v);
        return this;
    }

    private void tex0(float u, float v) {
        data[6] = u;
        data[7] = v;
    }

    public QuadMaker tex(float u, float v, float div) {
        data[6] = u(u / div);
        data[7] = v(v / div);
        return this;
    }

    private float fixBleeding(float uv) {
        if(fixBleeding) {
            return uv / 16 * 15.996F + 0.002F;
        }
        return uv;
    }

    private float u(float u) {
        return sprite == null ? u : sprite.getInterpolatedU(fixBleeding(u * 16));
    }

    private float v(float v) {
        return sprite == null ? v : sprite.getInterpolatedV(fixBleeding(v * 16));
    }

    public QuadMaker nor(float x, float y, float z) {
        float ln = MathHelper.sqrt(x * x + y * y + z * z);
        data[8] = ln == 0 ? 0 : x / ln;
        data[9] = ln == 0 ? 1 : y / ln;
        data[10] = ln == 0 ? 0 : z / ln;
        return this;
    }

    public QuadMaker nor(Direction dir) {
        return nor(dir.getXOffset(), dir.getYOffset(), dir.getZOffset());
    }

    public void end() {
        for(int e = 0; e < BLOCK_FORMAT.getElements().size(); e++) {
            switch(BLOCK_FORMAT.getElements().get(e).getUsage()) {
                case POSITION:
                    consumer.put(e, data[0], data[1], data[2], 1);
                    break;
                case COLOR:
                    consumer.put(e, data[3], data[4], data[5], 1);
                    break;
                case NORMAL:
                    consumer.put(e, data[8], data[9], data[10], 0);
                    break;
                case UV:
                    if(BLOCK_FORMAT.getElements().get(e).getIndex() == 0) {
                        consumer.put(e, data[6], data[7], 0, 1);
                        break;
                    }
                default:
                    consumer.put(e, 0, 0, 0, 0);
            }
        }

        // Reset
        pos(0, 0, 0);
        col(1, 1, 1);
        tex0(0, 0);
        nor(0, 1, 0);
    }

    public QuadMaker tint(int tint) {
        consumer.setQuadTint(tint);
        return this;
    }

    public QuadMaker orientation(Direction orientation) {
        consumer.setQuadOrientation(orientation);
        return this;
    }

    public QuadMaker diffuseLighting(boolean diffuse) {
        consumer.setApplyDiffuseLighting(diffuse);
        return this;
    }

    public QuadMaker fixBleeding(boolean fixBleeding) {
        this.fixBleeding = fixBleeding;
        return this;
    }

    public BakedQuad make() {
        return builder.build();
    }
}
