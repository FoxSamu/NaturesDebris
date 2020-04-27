/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.client.model.farmland;

import com.mojang.blaze3d.matrix.MatrixStack;
import modernity.client.model.QuadMaker;
import modernity.common.block.farmland.ITopTextureConnectionBlock;
import modernity.generic.util.CTMUtil;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.TransformationMatrix;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILightReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.PerspectiveMapWrapper;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import net.redgalaxy.exc.UnexpectedCaseException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class FarmlandBakedModel implements IDynamicBakedModel {
    @OnlyIn( Dist.CLIENT )
    public static final ModelProperty<Integer> CONNECTIONS = new ModelProperty<>();

    private static final int CENTER = 0;
    private static final int ALL = 1;
    private static final int CROSS = 2;
    private static final int HORIZONTAL = 3;
    private static final int VERTICAL = 4;

    private final boolean ao;
    private final boolean sideLit;
    private final boolean gui3d;
    private final ItemOverrideList overrides;
    private final TransformationMatrix transform;
    private final IModelTransform transforms;
    private final float y;
    private final int tint;
    private final int uvrot;
    private final TextureAtlasSprite particles;

    private final BakedQuad[] nw = new BakedQuad[ 5 ];
    private final BakedQuad[] ne = new BakedQuad[ 5 ];
    private final BakedQuad[] sw = new BakedQuad[ 5 ];
    private final BakedQuad[] se = new BakedQuad[ 5 ];

    private final ConnectionState[] cache = new ConnectionState[ 256 ];

    public FarmlandBakedModel( IModelConfiguration owner, ItemOverrideList overrides, float y, int tint, int uvrot, TextureAtlasSprite center, TextureAtlasSprite all, TextureAtlasSprite cross, TextureAtlasSprite horiz, TextureAtlasSprite vert, TextureAtlasSprite particles ) {
        this.y = y;
        this.tint = tint;
        this.uvrot = uvrot;
        this.ao = owner.useSmoothLighting();
        this.sideLit = owner.isSideLit();
        this.gui3d = owner.isShadedInGui();
        this.overrides = overrides;
        this.transforms = owner.getCombinedTransform();
        this.transform = transforms.getRotation();

        this.particles = particles;

        TextureAtlasSprite[] sprites = { center, all, cross, horiz, vert };

        QuadMaker maker = new QuadMaker();
        maker.fixBleeding( true );

        make( maker, nw, false, false, sprites );
        make( maker, ne, true, false, sprites );
        make( maker, sw, false, true, sprites );
        make( maker, se, true, true, sprites );
    }

    private void make( QuadMaker maker, BakedQuad[] quads, boolean offX, boolean offZ, TextureAtlasSprite[] sprites ) {
        int nx = offX ? 8 : 0;
        int nz = offZ ? 8 : 0;
        int px = offX ? 16 : 8;
        int pz = offZ ? 16 : 8;
        float nu = offX ? 8 : 0;
        float nv = offZ ? 8 : 0;
        float pu = offX ? 16 : 8;
        float pv = offZ ? 16 : 8;

        for( int i = 0; i < 5; i++ ) {
            maker.start( transform, sprites[ i ], false );
            maker.tint( tint );
            maker.diffuseLighting( true );
            maker.orientation( Direction.UP );
            maker.pos( nx, y, nz, 16 ).tex( rotateUV( nu, nu, pu, pu, 0 ), rotateUV( nv, pv, pv, nv, 0 ), 16 ).end();
            maker.pos( nx, y, pz, 16 ).tex( rotateUV( nu, nu, pu, pu, 1 ), rotateUV( nv, pv, pv, nv, 1 ), 16 ).end();
            maker.pos( px, y, pz, 16 ).tex( rotateUV( nu, nu, pu, pu, 2 ), rotateUV( nv, pv, pv, nv, 2 ), 16 ).end();
            maker.pos( px, y, nz, 16 ).tex( rotateUV( nu, nu, pu, pu, 3 ), rotateUV( nv, pv, pv, nv, 3 ), 16 ).end();

            quads[ i ] = maker.make();
        }
    }

    private float rotateUV( float uv1, float uv2, float uv3, float uv4, int index ) {
        index += uvrot;
        index %= 4;
        if( index < 0 ) index += 4;
        switch( index ) {
            case 0: return uv1;
            case 1: return uv2;
            case 2: return uv3;
            case 3: return uv4;
            default: throw new UnexpectedCaseException();
        }
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads( @Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData ) {
        if( side == Direction.UP ) {
            Integer ctmObj = extraData.getData( CONNECTIONS );
            int ctm = ctmObj == null ? 0 : ctmObj;

            if( cache[ ctm ] != null ) return cache[ ctm ].quads;

            boolean up = ( ctm & CTMUtil.UP ) != 0;
            boolean right = ( ctm & CTMUtil.RIGHT ) != 0;
            boolean down = ( ctm & CTMUtil.DOWN ) != 0;
            boolean left = ( ctm & CTMUtil.LEFT ) != 0;
            boolean upright = ( ctm & CTMUtil.UPRIGHT ) != 0;
            boolean downright = ( ctm & CTMUtil.DOWNRIGHT ) != 0;
            boolean downleft = ( ctm & CTMUtil.DOWNLEFT ) != 0;
            boolean upleft = ( ctm & CTMUtil.UPLEFT ) != 0;

            BakedQuad[] quads = {
                getQuad( ne, ! right, ! up, ! upright ),
                getQuad( nw, ! left, ! up, ! upleft ),
                getQuad( se, ! right, ! down, ! downright ),
                getQuad( sw, ! left, ! down, ! downleft ),
            };

            ConnectionState cs = new ConnectionState( Arrays.asList( quads ) );
            cache[ ctm ] = cs;

            return cs.quads;
        }
        return Collections.emptyList();
    }

    private BakedQuad getQuad( BakedQuad[] list, boolean horSide, boolean vertSide, boolean corner ) {
        if( horSide && vertSide ) return list[ CENTER ];
        else if( horSide ) return list[ VERTICAL ];
        else if( vertSide ) return list[ HORIZONTAL ];
        else if( corner ) return list[ CROSS ];
        else return list[ ALL ];
    }

    @Nonnull
    @Override
    public IModelData getModelData( @Nonnull ILightReader world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull IModelData tileData ) {
        if( state.getBlock() instanceof ITopTextureConnectionBlock ) {
            IModelData data = new ModelDataMap.Builder().withProperty( CONNECTIONS ).build();
            ( (ITopTextureConnectionBlock) state.getBlock() ).fillModelData( world, pos, state, data );
            return data;
        }
        return tileData;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return ao;
    }

    @Override
    public boolean isGui3d() {
        return gui3d;
    }

    @Override
    public boolean func_230044_c_() {
        return sideLit;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return particles;
    }

    @Override
    public ItemOverrideList getOverrides() {
        return overrides;
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public IBakedModel handlePerspective( ItemCameraTransforms.TransformType cameraTransformType, MatrixStack mat ) {
        return PerspectiveMapWrapper.handlePerspective( this, transforms, cameraTransformType, mat );
    }

    private static class ConnectionState {
        private final List<BakedQuad> quads;

        private ConnectionState( List<BakedQuad> quads ) {
            this.quads = quads;
        }
    }
}
