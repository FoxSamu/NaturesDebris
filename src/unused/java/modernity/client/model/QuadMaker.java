/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 16 - 2020
 * Author: rgsw
 */

package modernity.client.model;

import modernity.api.util.ColorUtil;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;
import net.minecraftforge.client.model.pipeline.TRSRTransformer;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.common.model.TRSRTransformation;

import java.util.Optional;

public class QuadMaker {
    private final VertexFormat format;
    private final float[] data = new float[ 11 ];
    private IVertexConsumer consumer;
    private UnpackedBakedQuad.Builder builder;
    private TextureAtlasSprite sprite;

    public QuadMaker( VertexFormat format ) {
        this.format = format;
    }

    public void start( Optional<TRSRTransformation> transform ) {
        consumer = builder = new UnpackedBakedQuad.Builder( format );
        if( transform.isPresent() && ! transform.get().isIdentity() ) {
            this.consumer = new TRSRTransformer( consumer, transform.get() );
        }
        pos( 0, 0, 0 );
        col( 1, 1, 1 );
        tex0( 0, 0 );
        nor( 0, 1, 0 );
    }


    public QuadMaker pos( float x, float y, float z ) {
        data[ 0 ] = x;
        data[ 1 ] = y;
        data[ 2 ] = z;
        return this;
    }

    public QuadMaker pos( float x, float y, float z, float div ) {
        data[ 0 ] = x / 16;
        data[ 1 ] = y / 16;
        data[ 2 ] = z / 16;
        return this;
    }

    public QuadMaker col( float r, float g, float b ) {
        data[ 3 ] = r;
        data[ 4 ] = g;
        data[ 5 ] = b;
        return this;
    }

    public QuadMaker col( float r, float g, float b, float div ) {
        data[ 3 ] = r / div;
        data[ 4 ] = g / div;
        data[ 5 ] = b / div;
        return this;
    }

    public QuadMaker col( int col ) {
        data[ 3 ] = ColorUtil.redf( col );
        data[ 4 ] = ColorUtil.greenf( col );
        data[ 5 ] = ColorUtil.bluef( col );
        return this;
    }

    public QuadMaker tex( float u, float v ) {
        data[ 6 ] = u( u );
        data[ 7 ] = v( v );
        return this;
    }

    private void tex0( float u, float v ) {
        data[ 6 ] = u;
        data[ 7 ] = v;
    }

    public QuadMaker tex( float u, float v, float div ) {
        data[ 6 ] = u( u / div );
        data[ 7 ] = v( v / div );
        return this;
    }

    private float u( float u ) {
        return sprite == null ? u : sprite.getInterpolatedU( u * 16 );
    }

    private float v( float v ) {
        return sprite == null ? v : sprite.getInterpolatedV( v * 16 );
    }

    public QuadMaker nor( float x, float y, float z ) {
        float ln = MathHelper.sqrt( x * x + y * y + z * z );
        data[ 8 ] = ln == 0 ? 0 : x / ln;
        data[ 9 ] = ln == 0 ? 1 : y / ln;
        data[ 10 ] = ln == 0 ? 0 : z / ln;
        return this;
    }

    public QuadMaker nor( Direction dir ) {
        return nor( dir.getXOffset(), dir.getYOffset(), dir.getZOffset() );
    }

    public void end() {
        for( int e = 0; e < format.getElementCount(); e++ ) {
            switch( format.getElement( e ).getUsage() ) {
                case POSITION:
                    consumer.put( e, data[ 0 ], data[ 1 ], data[ 2 ], 1 );
                    break;
                case UV:
                    consumer.put( e, data[ 6 ], data[ 7 ], 0, 1 );
                    break;
                case COLOR:
                    consumer.put( e, data[ 3 ], data[ 4 ], data[ 5 ], 1 );
                    break;
                case NORMAL:
                    consumer.put( e, data[ 8 ], data[ 9 ], data[ 10 ], 0 );
                    break;
                default:
                    consumer.put( e, 0, 0, 0, 0 );
            }
        }

        // Reset
        pos( 0, 0, 0 );
        col( 1, 1, 1 );
        tex0( 0, 0 );
        nor( 0, 1, 0 );
    }

    public VertexFormat getVertexFormat() {
        return format;
    }

    public void tint( int tint ) {
        consumer.setQuadTint( tint );
    }

    public void orientation( Direction orientation ) {
        consumer.setQuadOrientation( orientation );
    }

    public void diffuseLighting( boolean diffuse ) {
        consumer.setApplyDiffuseLighting( diffuse );
    }

    public void sprite( TextureAtlasSprite texture ) {
        consumer.setTexture( texture );
        sprite = texture;
    }

    public BakedQuad make() {
        return builder.build();
    }
}
