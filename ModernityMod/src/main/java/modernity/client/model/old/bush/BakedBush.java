/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 31 - 2020
 * Author: rgsw
 */

package modernity.client.model.old.bush;


// TODO Re-evaluate
//@SuppressWarnings( "deprecation" )
//public class BakedBush implements IBakedModel {
//
//    private static final float eps = 1e-3f;
//
//    private static final SideSelector UP = new SideSelector( BushModelProperties.UP );
//    private static final SideSelector DOWN = new SideSelector( BushModelProperties.DOWN );
//    private static final SideSelector EAST = new SideSelector( BushModelProperties.EAST );
//    private static final SideSelector WEST = new SideSelector( BushModelProperties.WEST );
//    private static final SideSelector NORTH = new SideSelector( BushModelProperties.NORTH );
//    private static final SideSelector SOUTH = new SideSelector( BushModelProperties.SOUTH );
//    private static final SideSelector UP_NORTH = new SideSelector( BushModelProperties.UP_NORTH );
//    private static final SideSelector UP_SOUTH = new SideSelector( BushModelProperties.UP_SOUTH );
//    private static final SideSelector DOWN_NORTH = new SideSelector( BushModelProperties.DOWN_NORTH );
//    private static final SideSelector DOWN_SOUTH = new SideSelector( BushModelProperties.DOWN_SOUTH );
//    private static final SideSelector EAST_UP = new SideSelector( BushModelProperties.UP_EAST );
//    private static final SideSelector EAST_DOWN = new SideSelector( BushModelProperties.DOWN_EAST );
//    private static final SideSelector WEST_UP = new SideSelector( BushModelProperties.UP_WEST );
//    private static final SideSelector WEST_DOWN = new SideSelector( BushModelProperties.DOWN_WEST );
//    private static final SideSelector NORTH_EAST = new SideSelector( BushModelProperties.NORTH_EAST );
//    private static final SideSelector NORTH_WEST = new SideSelector( BushModelProperties.NORTH_WEST );
//    private static final SideSelector SOUTH_EAST = new SideSelector( BushModelProperties.SOUTH_EAST );
//    private static final SideSelector SOUTH_WEST = new SideSelector( BushModelProperties.SOUTH_WEST );
//
//    private static final Predicate<IModelData> ALL = state -> true;
//
//    private final VertexFormat format;
//    private final int color;
//    private final TextureAtlasSprite texture;
//    private Optional<TRSRTransformation> transformation;
//    private ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transforms;
//
//    private final ImmutableList<QuadDef> quadDefs;
//
//    public BakedBush( Optional<TRSRTransformation> transformation, ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transforms, VertexFormat format, TextureAtlasSprite texture ) {
//        this.transformation = transformation;
//        this.transforms = transforms;
//        this.format = format;
//        this.color = 0;
//        this.texture = texture;
//        this.quadDefs = build( texture );
//    }
//
//
//    private static boolean getValue( IModelData state, ModelProperty<Boolean> bool ) {
//        return state != null && nonnull( state.getData( bool ) );
//    }
//
//    private static boolean nonnull( Boolean value ) {
//        return value == null ? false : value;
//    }
//
//    @Nonnull
//    @Override
//    public List<BakedQuad> getQuads( @Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData ) {
//        if( state != null && ! ( state.getBlock() instanceof BushBlock ) ) return ImmutableList.of();
//        List<BakedQuad> quads = new ArrayList<>();
//
//        for( QuadDef quadDef : quadDefs ) { // Loop over quads and add the quads that we want
//            boolean ok = quadDef.predicate.test( extraData );
//            if( ok ) {
//                quads.add( quadDef.quad );
//            }
//        }
//
//        return quads;
//    }
//
//    @Override
//    public List<BakedQuad> getQuads( @Nullable BlockState state, @Nullable Direction side, Random rand ) {
//        return Collections.emptyList();
//    }
//
//    private BakedQuad quad( TextureAtlasSprite tex, Direction side, float... p ) {
//        int verts = p.length / 5;
//        UnpackedBakedQuad.Builder builder1 = new UnpackedBakedQuad.Builder( this.format );
//
//        builder1.setTexture( tex );
//        builder1.setQuadTint( 0 );
//
//        for( int i = 0; i < verts; i++ ) {
//            int ix = i * 5;
//            int iy = ix + 1;
//            int iz = ix + 2;
//            int iu = ix + 3;
//            int iv = ix + 4;
//
//            this.putVertex( builder1, side, p[ ix ], p[ iy ], p[ iz ], tex.getInterpolatedU( fixTexBleeding( p[ iu ] ) ), tex.getInterpolatedV( fixTexBleeding( p[ iv ] ) ) );
//        }
//
//        return builder1.build();
//    }
//
//    private BakedQuad quad( float nx, float ny, float nz, TextureAtlasSprite tex, float... p ) {
//        int verts = p.length / 5;
//        UnpackedBakedQuad.Builder builder1 = new UnpackedBakedQuad.Builder( this.format );
//
//        builder1.setTexture( tex );
//        builder1.setQuadTint( 0 );
//
//        Vector3f vec3f = new Vector3f( nx, ny, nz );
//        vec3f.normalize();
//
//        for( int i = 0; i < verts; i++ ) {
//            int ix = i * 5;
//            int iy = ix + 1;
//            int iz = ix + 2;
//            int iu = ix + 3;
//            int iv = ix + 4;
//
//            this.putVertex( builder1, vec3f.getX(), vec3f.getY(), vec3f.getZ(), p[ ix ], p[ iy ], p[ iz ], tex.getInterpolatedU( fixTexBleeding( p[ iu ] ) ), tex.getInterpolatedV( fixTexBleeding( p[ iv ] ) ) );
//        }
//
//        return builder1.build();
//    }
//
//    private List<BakedQuad> quadDouble( float nx, float ny, float nz, TextureAtlasSprite tex, float... p ) {
//        int verts = p.length / 5;
//        UnpackedBakedQuad.Builder builder1 = new UnpackedBakedQuad.Builder( this.format );
//        UnpackedBakedQuad.Builder builder2 = new UnpackedBakedQuad.Builder( this.format );
//
//        builder1.setTexture( tex );
//        builder1.setQuadTint( 0 );
//        builder2.setTexture( tex );
//        builder2.setQuadTint( 0 );
//
//        Vector3f vec3f = new Vector3f( nx, ny, nz );
//        vec3f.normalize();
//
//        for( int i = 0; i < verts; i++ ) {
//            int ix = i * 5;
//            int iy = ix + 1;
//            int iz = ix + 2;
//            int iu = ix + 3;
//            int iv = ix + 4;
//
//            this.putVertex( builder1, vec3f.getX(), vec3f.getY(), vec3f.getZ(), p[ ix ], p[ iy ], p[ iz ], tex.getInterpolatedU( fixTexBleeding( p[ iu ] ) ), tex.getInterpolatedV( fixTexBleeding( p[ iv ] ) ) );
//        }
//
//        for( int i = verts - 1; i >= 0; i-- ) {
//            int ix = i * 5;
//            int iy = ix + 1;
//            int iz = ix + 2;
//            int iu = ix + 3;
//            int iv = ix + 4;
//
//            this.putVertex( builder2, - vec3f.getX(), - vec3f.getY(), - vec3f.getZ(), p[ ix ], p[ iy ], p[ iz ], tex.getInterpolatedU( fixTexBleeding( p[ iu ] ) ), tex.getInterpolatedV( fixTexBleeding( p[ iv ] ) ) );
//        }
//
//        return ImmutableList.of( builder1.build(), builder2.build() );
//    }
//
//    private static float fixTexBleeding( float v ) {
//        return v / 16 * 15.998F + 0.001F;
//    }
//
//    /**
//     * Pre-bakes all possible quads: it would be a performance leak if we rebuild them every time we render a bush
//     * block. All quads are stored with a predicate which checks if they must render on a specific state.
//     *
//     * @param texture The bush texture
//     * @return A list of pre-built quads with a predicate that checks whether a quad should be rendered on a specific
//     *     state or not
//     */
//    private ImmutableList<QuadDef> build( TextureAtlasSprite texture ) {
//        ImmutableList.Builder<QuadDef> builder = new ImmutableList.Builder<>();
//        buildUp( builder, texture );
//        buildDown( builder, texture );
//        buildNorth( builder, texture );
//        buildSouth( builder, texture );
//        buildEast( builder, texture );
//        buildWest( builder, texture );
//        buildEdges( builder, texture );
//        buildCorners( builder, texture );
//        buildInner( builder, texture );
//        return builder.build();
//    }
//
//    private static final float TEX0 = 0;
//    private static final float TEX1 = 3;
//    private static final float TEX2 = 13;
//    private static final float TEX3 = 16;
//
//    private static final float TEXP = 4;//(float) Math.sqrt( TEX1 * TEX1 * 2 );
//    private static final float TEXQ = 16 - TEXP;
//
//    private static final float POS0 = 0 / 16F;
//    private static final float POS1 = 3 / 16F;
//    private static final float POS2 = 13 / 16F;
//    private static final float POS3 = 16 / 16F;
//
//    private void buildUp( ImmutableList.Builder<QuadDef> builder, TextureAtlasSprite texture ) {
//        builder.add( new QuadDef( this.quad(
//            texture, Direction.UP,
//            POS1, POS3, POS2, TEX1, TEX2,
//            POS2, POS3, POS2, TEX2, TEX2,
//            POS2, POS3, POS1, TEX2, TEX1,
//            POS1, POS3, POS1, TEX1, TEX1
//        ), ALL ) );
//
//        builder.add( new QuadDef( this.quad(
//            texture, Direction.UP,
//            POS1, POS3, POS1, TEX1, TEX1,
//            POS2, POS3, POS1, TEX2, TEX1,
//            POS2, POS3, POS0, TEX2, TEX0,
//            POS1, POS3, POS0, TEX1, TEX0
//        ), NORTH ) );
//
//        builder.add( new QuadDef( this.quad(
//            texture, Direction.UP,
//            POS1, POS3, POS3, TEX1, TEX3,
//            POS2, POS3, POS3, TEX2, TEX3,
//            POS2, POS3, POS2, TEX2, TEX2,
//            POS1, POS3, POS2, TEX1, TEX2
//        ), SOUTH ) );
//
//        builder.add( new QuadDef( this.quad(
//            texture, Direction.UP,
//            POS2, POS3, POS2, TEX2, TEX2,
//            POS3, POS3, POS2, TEX3, TEX2,
//            POS3, POS3, POS1, TEX3, TEX1,
//            POS2, POS3, POS1, TEX2, TEX1
//        ), EAST ) );
//
//        builder.add( new QuadDef( this.quad(
//            texture, Direction.UP,
//            POS0, POS3, POS2, TEX0, TEX2,
//            POS1, POS3, POS2, TEX1, TEX2,
//            POS1, POS3, POS1, TEX1, TEX1,
//            POS0, POS3, POS1, TEX0, TEX1
//        ), WEST ) );
//
//        builder.add( new QuadDef( this.quad(
//            texture, Direction.UP,
//            POS2, POS3, POS1, TEX2, TEX1,
//            POS3, POS3, POS1, TEX3, TEX1,
//            POS3, POS3, POS0, TEX3, TEX0,
//            POS2, POS3, POS0, TEX2, TEX0
//        ), NORTH_EAST ) );
//
//        builder.add( new QuadDef( this.quad(
//            texture, Direction.UP,
//            POS0, POS3, POS1, TEX0, TEX1,
//            POS1, POS3, POS1, TEX1, TEX1,
//            POS1, POS3, POS0, TEX1, TEX0,
//            POS0, POS3, POS0, TEX0, TEX0
//        ), NORTH_WEST ) );
//
//        builder.add( new QuadDef( this.quad(
//            texture, Direction.UP,
//            POS2, POS3, POS3, TEX2, TEX3,
//            POS3, POS3, POS3, TEX3, TEX3,
//            POS3, POS3, POS2, TEX3, TEX2,
//            POS2, POS3, POS2, TEX2, TEX2
//        ), SOUTH_EAST ) );
//
//        builder.add( new QuadDef( this.quad(
//            texture, Direction.UP,
//            POS0, POS3, POS3, TEX0, TEX3,
//            POS1, POS3, POS3, TEX1, TEX3,
//            POS1, POS3, POS2, TEX1, TEX2,
//            POS0, POS3, POS2, TEX0, TEX2
//        ), SOUTH_WEST ) );
//    }
//
//    private void buildDown( ImmutableList.Builder<QuadDef> builder, TextureAtlasSprite texture ) {
//        builder.add( new QuadDef( this.quad(
//            texture, Direction.DOWN,
//            POS1, POS0, POS1, TEX1, TEX1,
//            POS2, POS0, POS1, TEX2, TEX1,
//            POS2, POS0, POS2, TEX2, TEX2,
//            POS1, POS0, POS2, TEX1, TEX2
//        ), ALL ) );
//
//        builder.add( new QuadDef( this.quad(
//            texture, Direction.DOWN,
//            POS1, POS0, POS0, TEX1, TEX0,
//            POS2, POS0, POS0, TEX2, TEX0,
//            POS2, POS0, POS1, TEX2, TEX1,
//            POS1, POS0, POS1, TEX1, TEX1
//        ), NORTH ) );
//
//        builder.add( new QuadDef( this.quad(
//            texture, Direction.DOWN,
//            POS1, POS0, POS2, TEX1, TEX2,
//            POS2, POS0, POS2, TEX2, TEX2,
//            POS2, POS0, POS3, TEX2, TEX3,
//            POS1, POS0, POS3, TEX1, TEX3
//        ), SOUTH ) );
//
//        builder.add( new QuadDef( this.quad(
//            texture, Direction.DOWN,
//            POS2, POS0, POS1, TEX2, TEX1,
//            POS3, POS0, POS1, TEX3, TEX1,
//            POS3, POS0, POS2, TEX3, TEX2,
//            POS2, POS0, POS2, TEX2, TEX2
//        ), EAST ) );
//
//        builder.add( new QuadDef( this.quad(
//            texture, Direction.DOWN,
//            POS0, POS0, POS1, TEX0, TEX1,
//            POS1, POS0, POS1, TEX1, TEX1,
//            POS1, POS0, POS2, TEX1, TEX2,
//            POS0, POS0, POS2, TEX0, TEX2
//        ), WEST ) );
//
//        builder.add( new QuadDef( this.quad(
//            texture, Direction.DOWN,
//            POS2, POS0, POS0, TEX2, TEX0,
//            POS3, POS0, POS0, TEX3, TEX0,
//            POS3, POS0, POS1, TEX3, TEX1,
//            POS2, POS0, POS1, TEX2, TEX1
//        ), NORTH_EAST ) );
//
//        builder.add( new QuadDef( this.quad(
//            texture, Direction.DOWN,
//            POS0, POS0, POS0, TEX0, TEX0,
//            POS1, POS0, POS0, TEX1, TEX0,
//            POS1, POS0, POS1, TEX1, TEX1,
//            POS0, POS0, POS1, TEX0, TEX1
//        ), NORTH_WEST ) );
//
//        builder.add( new QuadDef( this.quad(
//            texture, Direction.DOWN,
//            POS2, POS0, POS2, TEX2, TEX2,
//            POS3, POS0, POS2, TEX3, TEX2,
//            POS3, POS0, POS3, TEX3, TEX3,
//            POS2, POS0, POS3, TEX2, TEX3
//        ), SOUTH_EAST ) );
//
//        builder.add( new QuadDef( this.quad(
//            texture, Direction.DOWN,
//            POS0, POS0, POS2, TEX0, TEX2,
//            POS1, POS0, POS2, TEX1, TEX2,
//            POS1, POS0, POS3, TEX1, TEX3,
//            POS0, POS0, POS3, TEX0, TEX3
//        ), SOUTH_WEST ) );
//    }
//
//    private void buildNorth( ImmutableList.Builder<QuadDef> builder, TextureAtlasSprite texture ) {
//        builder.add( new QuadDef( this.quad(
//            texture, Direction.NORTH,
//            POS1, POS2, POS0, TEX1, TEX2,
//            POS2, POS2, POS0, TEX2, TEX2,
//            POS2, POS1, POS0, TEX2, TEX1,
//            POS1, POS1, POS0, TEX1, TEX1
//        ), ALL ) );
//
//        builder.add( new QuadDef( this.quad(
//            texture, Direction.NORTH,
//            POS1, POS1, POS0, TEX1, TEX1,
//            POS2, POS1, POS0, TEX2, TEX1,
//            POS2, POS0, POS0, TEX2, TEX0,
//            POS1, POS0, POS0, TEX1, TEX0
//        ), DOWN ) );
//
//        builder.add( new QuadDef( this.quad(
//            texture, Direction.NORTH,
//            POS1, POS3, POS0, TEX1, TEX3,
//            POS2, POS3, POS0, TEX2, TEX3,
//            POS2, POS2, POS0, TEX2, TEX2,
//            POS1, POS2, POS0, TEX1, TEX2
//        ), UP ) );
//
//        builder.add( new QuadDef( this.quad(
//            texture, Direction.NORTH,
//            POS2, POS2, POS0, TEX2, TEX2,
//            POS3, POS2, POS0, TEX3, TEX2,
//            POS3, POS1, POS0, TEX3, TEX1,
//            POS2, POS1, POS0, TEX2, TEX1
//        ), EAST ) );
//
//        builder.add( new QuadDef( this.quad(
//            texture, Direction.NORTH,
//            POS0, POS2, POS0, TEX0, TEX2,
//            POS1, POS2, POS0, TEX1, TEX2,
//            POS1, POS1, POS0, TEX1, TEX1,
//            POS0, POS1, POS0, TEX0, TEX1
//        ), WEST ) );
//
//        builder.add( new QuadDef( this.quad(
//            texture, Direction.NORTH,
//            POS2, POS1, POS0, TEX2, TEX1,
//            POS3, POS1, POS0, TEX3, TEX1,
//            POS3, POS0, POS0, TEX3, TEX0,
//            POS2, POS0, POS0, TEX2, TEX0
//        ), EAST_DOWN ) );
//
//        builder.add( new QuadDef( this.quad(
//            texture, Direction.NORTH,
//            POS0, POS1, POS0, TEX0, TEX1,
//            POS1, POS1, POS0, TEX1, TEX1,
//            POS1, POS0, POS0, TEX1, TEX0,
//            POS0, POS0, POS0, TEX0, TEX0
//        ), WEST_DOWN ) );
//
//        builder.add( new QuadDef( this.quad(
//            texture, Direction.NORTH,
//            POS2, POS3, POS0, TEX2, TEX3,
//            POS3, POS3, POS0, TEX3, TEX3,
//            POS3, POS2, POS0, TEX3, TEX2,
//            POS2, POS2, POS0, TEX2, TEX2
//        ), EAST_UP ) );
//
//        builder.add( new QuadDef( this.quad(
//            texture, Direction.NORTH,
//            POS0, POS3, POS0, TEX0, TEX3,
//            POS1, POS3, POS0, TEX1, TEX3,
//            POS1, POS2, POS0, TEX1, TEX2,
//            POS0, POS2, POS0, TEX0, TEX2
//        ), WEST_UP ) );
//    }
//
//    private void buildSouth( ImmutableList.Builder<QuadDef> builder, TextureAtlasSprite texture ) {
//
//        builder.add( new QuadDef( this.quad(
//            texture, Direction.SOUTH,
//            POS1, POS1, POS3, TEX1, TEX1,
//            POS2, POS1, POS3, TEX2, TEX1,
//            POS2, POS2, POS3, TEX2, TEX2,
//            POS1, POS2, POS3, TEX1, TEX2
//        ), ALL ) );
//        builder.add( new QuadDef( this.quad(
//            texture, Direction.SOUTH,
//            POS1, POS0, POS3, TEX1, TEX0,
//            POS2, POS0, POS3, TEX2, TEX0,
//            POS2, POS1, POS3, TEX2, TEX1,
//            POS1, POS1, POS3, TEX1, TEX1
//        ), DOWN ) );
//
//        builder.add( new QuadDef( this.quad(
//            texture, Direction.SOUTH,
//            POS1, POS2, POS3, TEX1, TEX2,
//            POS2, POS2, POS3, TEX2, TEX2,
//            POS2, POS3, POS3, TEX2, TEX3,
//            POS1, POS3, POS3, TEX1, TEX3
//        ), UP ) );
//
//        builder.add( new QuadDef( this.quad(
//            texture, Direction.SOUTH,
//            POS2, POS1, POS3, TEX2, TEX1,
//            POS3, POS1, POS3, TEX3, TEX1,
//            POS3, POS2, POS3, TEX3, TEX2,
//            POS2, POS2, POS3, TEX2, TEX2
//        ), EAST ) );
//
//        builder.add( new QuadDef( this.quad(
//            texture, Direction.SOUTH,
//            POS0, POS1, POS3, TEX0, TEX1,
//            POS1, POS1, POS3, TEX1, TEX1,
//            POS1, POS2, POS3, TEX1, TEX2,
//            POS0, POS2, POS3, TEX0, TEX2
//        ), WEST ) );
//
//        builder.add( new QuadDef( this.quad(
//            texture, Direction.SOUTH,
//            POS2, POS0, POS3, TEX2, TEX0,
//            POS3, POS0, POS3, TEX3, TEX0,
//            POS3, POS1, POS3, TEX3, TEX1,
//            POS2, POS1, POS3, TEX2, TEX1
//        ), EAST_DOWN ) );
//
//        builder.add( new QuadDef( this.quad(
//            texture, Direction.SOUTH,
//            POS0, POS0, POS3, TEX0, TEX0,
//            POS1, POS0, POS3, TEX1, TEX0,
//            POS1, POS1, POS3, TEX1, TEX1,
//            POS0, POS1, POS3, TEX0, TEX1
//        ), WEST_DOWN ) );
//
//        builder.add( new QuadDef( this.quad(
//            texture, Direction.SOUTH,
//            POS2, POS2, POS3, TEX2, TEX2,
//            POS3, POS2, POS3, TEX3, TEX2,
//            POS3, POS3, POS3, TEX3, TEX3,
//            POS2, POS3, POS3, TEX2, TEX3
//        ), EAST_UP ) );
//
//        builder.add( new QuadDef( this.quad(
//            texture, Direction.SOUTH,
//            POS0, POS2, POS3, TEX0, TEX2,
//            POS1, POS2, POS3, TEX1, TEX2,
//            POS1, POS3, POS3, TEX1, TEX3,
//            POS0, POS3, POS3, TEX0, TEX3
//        ), WEST_UP ) );
//
//    }
//
//    private void buildEast( ImmutableList.Builder<QuadDef> builder, TextureAtlasSprite texture ) {
//        builder.add( new QuadDef( this.quad(
//            texture, Direction.EAST,
//            POS0, POS1, POS2, TEX1, TEX2,
//            POS0, POS2, POS2, TEX2, TEX2,
//            POS0, POS2, POS1, TEX2, TEX1,
//            POS0, POS1, POS1, TEX1, TEX1
//        ), ALL ) );
//
//        builder.add( new QuadDef( this.quad(
//            texture, Direction.EAST,
//            POS0, POS0, POS1, TEX1, TEX0,
//            POS0, POS0, POS2, TEX2, TEX0,
//            POS0, POS1, POS2, TEX2, TEX1,
//            POS0, POS1, POS1, TEX1, TEX1
//        ), DOWN ) );
//
//        builder.add( new QuadDef( this.quad(
//            texture, Direction.EAST,
//            POS0, POS2, POS1, TEX1, TEX2,
//            POS0, POS2, POS2, TEX2, TEX2,
//            POS0, POS3, POS2, TEX2, TEX3,
//            POS0, POS3, POS1, TEX1, TEX3
//        ), UP ) );
//
//        builder.add( new QuadDef( this.quad(
//            texture, Direction.EAST,
//            POS0, POS1, POS2, TEX2, TEX1,
//            POS0, POS1, POS3, TEX3, TEX1,
//            POS0, POS2, POS3, TEX3, TEX2,
//            POS0, POS2, POS2, TEX2, TEX2
//        ), SOUTH ) );
//
//        builder.add( new QuadDef( this.quad(
//            texture, Direction.EAST,
//            POS0, POS1, POS0, TEX0, TEX1,
//            POS0, POS1, POS1, TEX1, TEX1,
//            POS0, POS2, POS1, TEX1, TEX2,
//            POS0, POS2, POS0, TEX0, TEX2
//        ), NORTH ) );
//
//        builder.add( new QuadDef( this.quad(
//            texture, Direction.EAST,
//            POS0, POS0, POS2, TEX2, TEX0,
//            POS0, POS0, POS3, TEX3, TEX0,
//            POS0, POS1, POS3, TEX3, TEX1,
//            POS0, POS1, POS2, TEX2, TEX1
//        ), DOWN_SOUTH ) );
//
//        builder.add( new QuadDef( this.quad(
//            texture, Direction.EAST,
//            POS0, POS0, POS0, TEX0, TEX0,
//            POS0, POS0, POS1, TEX1, TEX0,
//            POS0, POS1, POS1, TEX1, TEX1,
//            POS0, POS1, POS0, TEX0, TEX1
//        ), DOWN_NORTH ) );
//
//        builder.add( new QuadDef( this.quad(
//            texture, Direction.EAST,
//            POS0, POS2, POS2, TEX2, TEX2,
//            POS0, POS2, POS3, TEX3, TEX2,
//            POS0, POS3, POS3, TEX3, TEX3,
//            POS0, POS3, POS2, TEX2, TEX3
//        ), UP_SOUTH ) );
//
//        builder.add( new QuadDef( this.quad(
//            texture, Direction.EAST,
//            POS0, POS2, POS0, TEX0, TEX2,
//            POS0, POS2, POS1, TEX1, TEX2,
//            POS0, POS3, POS1, TEX1, TEX3,
//            POS0, POS3, POS0, TEX0, TEX3
//        ), UP_NORTH ) );
//    }
//
//    private void buildWest( ImmutableList.Builder<QuadDef> builder, TextureAtlasSprite texture ) {
//        builder.add( new QuadDef( this.quad(
//            texture, Direction.WEST,
//            POS3, POS1, POS1, TEX1, TEX1,
//            POS3, POS2, POS1, TEX2, TEX1,
//            POS3, POS2, POS2, TEX2, TEX2,
//            POS3, POS1, POS2, TEX1, TEX2
//        ), ALL ) );
//
//        builder.add( new QuadDef( this.quad(
//            texture, Direction.WEST,
//            POS3, POS1, POS1, TEX1, TEX1,
//            POS3, POS1, POS2, TEX2, TEX1,
//            POS3, POS0, POS2, TEX2, TEX0,
//            POS3, POS0, POS1, TEX1, TEX0
//        ), DOWN ) );
//
//        builder.add( new QuadDef( this.quad(
//            texture, Direction.WEST,
//            POS3, POS2, POS1, TEX1, TEX2,
//            POS3, POS3, POS1, TEX1, TEX3,
//            POS3, POS3, POS2, TEX2, TEX3,
//            POS3, POS2, POS2, TEX2, TEX2
//        ), UP ) );
//
//        builder.add( new QuadDef( this.quad(
//            texture, Direction.WEST,
//            POS3, POS2, POS2, TEX2, TEX2,
//            POS3, POS2, POS3, TEX3, TEX2,
//            POS3, POS1, POS3, TEX3, TEX1,
//            POS3, POS1, POS2, TEX2, TEX1
//        ), SOUTH ) );
//
//        builder.add( new QuadDef( this.quad(
//            texture, Direction.WEST,
//            POS3, POS2, POS0, TEX0, TEX2,
//            POS3, POS2, POS1, TEX1, TEX2,
//            POS3, POS1, POS1, TEX1, TEX1,
//            POS3, POS1, POS0, TEX0, TEX1
//        ), NORTH ) );
//
//        builder.add( new QuadDef( this.quad(
//            texture, Direction.WEST,
//            POS3, POS1, POS2, TEX2, TEX1,
//            POS3, POS1, POS3, TEX3, TEX1,
//            POS3, POS0, POS3, TEX3, TEX0,
//            POS3, POS0, POS2, TEX2, TEX0
//        ), DOWN_SOUTH ) );
//
//        builder.add( new QuadDef( this.quad(
//            texture, Direction.WEST,
//            POS3, POS1, POS0, TEX0, TEX1,
//            POS3, POS1, POS1, TEX1, TEX1,
//            POS3, POS0, POS1, TEX1, TEX0,
//            POS3, POS0, POS0, TEX0, TEX0
//        ), DOWN_NORTH ) );
//
//        builder.add( new QuadDef( this.quad(
//            texture, Direction.WEST,
//            POS3, POS3, POS2, TEX2, TEX3,
//            POS3, POS3, POS3, TEX3, TEX3,
//            POS3, POS2, POS3, TEX3, TEX2,
//            POS3, POS2, POS2, TEX2, TEX2
//        ), UP_SOUTH ) );
//
//        builder.add( new QuadDef( this.quad(
//            texture, Direction.WEST,
//            POS3, POS3, POS0, TEX0, TEX3,
//            POS3, POS3, POS1, TEX1, TEX3,
//            POS3, POS2, POS1, TEX1, TEX2,
//            POS3, POS2, POS0, TEX0, TEX2
//        ), UP_NORTH ) );
//    }
//
//    private static <T> Predicate<T> merge( Predicate<T> a, Predicate<T> b, boolean invA, boolean invB ) {
//        if( invA ) a = a.negate();
//        if( invB ) b = b.negate();
//        return a.and( b );
//    }
//
//    private void buildEdges( ImmutableList.Builder<QuadDef> builder, TextureAtlasSprite texture ) {
//
//        for( int i = 0; i < 4; i++ ) {
//            boolean up = ( i & 2 ) == 0;
//            boolean down = ( i & 1 ) == 0;
//            float y0 = down ? 0 : POS1;
//            float y1 = up ? 1 : POS2;
//            builder.add( new QuadDef( this.quad(
//                1, 0, - 1,
//                texture,
//                POS3, y0, POS1, TEXP, y0 * TEX3,
//                POS2, y0, POS0, TEX0, y0 * TEX3,
//                POS2, y1, POS0, TEX0, y1 * TEX3,
//                POS3, y1, POS1, TEXP, y1 * TEX3
//            ), merge( DOWN, UP, ! down, ! up ) ) );
//            builder.add( new QuadDef( this.quad(
//                1, 0, 1,
//                texture,
//                POS3, y1, POS2, TEXP, y1 * TEX3,
//                POS2, y1, POS3, TEX0, y1 * TEX3,
//                POS2, y0, POS3, TEX0, y0 * TEX3,
//                POS3, y0, POS2, TEXP, y0 * TEX3
//            ), merge( DOWN, UP, ! down, ! up ) ) );
//            builder.add( new QuadDef( this.quad(
//                - 1, 0, 1,
//                texture,
//                POS0, y0, POS2, TEXP, y0 * TEX3,
//                POS1, y0, POS3, TEX0, y0 * TEX3,
//                POS1, y1, POS3, TEX0, y1 * TEX3,
//                POS0, y1, POS2, TEXP, y1 * TEX3
//            ), merge( DOWN, UP, ! down, ! up ) ) );
//            builder.add( new QuadDef( this.quad(
//                - 1, 0, - 1,
//                texture,
//                POS0, y1, POS1, TEXP, y1 * TEX3,
//                POS1, y1, POS0, TEX0, y1 * TEX3,
//                POS1, y0, POS0, TEX0, y0 * TEX3,
//                POS0, y0, POS1, TEXP, y0 * TEX3
//            ), merge( DOWN, UP, ! down, ! up ) ) );
//        }
//
//        for( int i = 0; i < 4; i++ ) {
//            boolean north = ( i & 2 ) == 0;
//            boolean south = ( i & 1 ) == 0;
//            float z0 = north ? 0 : POS1;
//            float z1 = south ? 1 : POS2;
//            builder.add( new QuadDef( this.quad(
//                - 1, 1, 0,
//                texture,
//                POS1, POS3, z1, z1 * TEX3, TEXP,
//                POS1, POS3, z0, z0 * TEX3, TEXP,
//                POS0, POS2, z0, z0 * TEX3, TEX0,
//                POS0, POS2, z1, z1 * TEX3, TEX0
//            ), merge( NORTH, SOUTH, ! north, ! south ) ) );
//            builder.add( new QuadDef( this.quad(
//                - 1, - 1, 0,
//                texture,
//                POS0, POS1, z1, z1 * TEX3, TEX0,
//                POS0, POS1, z0, z0 * TEX3, TEX0,
//                POS1, POS0, z0, z0 * TEX3, TEXP,
//                POS1, POS0, z1, z1 * TEX3, TEXP
//            ), merge( NORTH, SOUTH, ! north, ! south ) ) );
//            builder.add( new QuadDef( this.quad(
//                1, - 1, 0,
//                texture,
//                POS2, POS0, z1, z1 * TEX3, TEXP,
//                POS2, POS0, z0, z0 * TEX3, TEXP,
//                POS3, POS1, z0, z0 * TEX3, TEX0,
//                POS3, POS1, z1, z1 * TEX3, TEX0
//            ), merge( NORTH, SOUTH, ! north, ! south ) ) );
//            builder.add( new QuadDef( this.quad(
//                1, 1, 0,
//                texture,
//                POS3, POS2, z1, z1 * TEX3, TEX0,
//                POS3, POS2, z0, z0 * TEX3, TEX0,
//                POS2, POS3, z0, z0 * TEX3, TEXP,
//                POS2, POS3, z1, z1 * TEX3, TEXP
//            ), merge( NORTH, SOUTH, ! north, ! south ) ) );
//        }
//
//        for( int i = 0; i < 4; i++ ) {
//            boolean west = ( i & 2 ) == 0;
//            boolean east = ( i & 1 ) == 0;
//            float x0 = west ? 0 : POS1;
//            float x1 = east ? 1 : POS2;
//            builder.add( new QuadDef( this.quad(
//                0, 1, - 1,
//                texture,
//                x1, POS2, POS0, x1 * TEX3, TEX0,
//                x0, POS2, POS0, x0 * TEX3, TEX0,
//                x0, POS3, POS1, x0 * TEX3, TEXP,
//                x1, POS3, POS1, x1 * TEX3, TEXP
//            ), merge( WEST, EAST, ! west, ! east ) ) );
//            builder.add( new QuadDef( this.quad(
//                0, 1, 1,
//                texture,
//                x1, POS3, POS2, x1 * TEX3, TEXP,
//                x0, POS3, POS2, x0 * TEX3, TEXP,
//                x0, POS2, POS3, x0 * TEX3, TEX0,
//                x1, POS2, POS3, x1 * TEX3, TEX0
//            ), merge( WEST, EAST, ! west, ! east ) ) );
//            builder.add( new QuadDef( this.quad(
//                0, - 1, 1,
//                texture,
//                x1, POS1, POS3, x1 * TEX3, TEX0,
//                x0, POS1, POS3, x0 * TEX3, TEX0,
//                x0, POS0, POS2, x0 * TEX3, TEXP,
//                x1, POS0, POS2, x1 * TEX3, TEXP
//            ), merge( WEST, EAST, ! west, ! east ) ) );
//            builder.add( new QuadDef( this.quad(
//                0, - 1, - 1,
//                texture,
//                x1, POS0, POS1, x1 * TEX3, TEXP,
//                x0, POS0, POS1, x0 * TEX3, TEXP,
//                x0, POS1, POS0, x0 * TEX3, TEX0,
//                x1, POS1, POS0, x1 * TEX3, TEX0
//            ), merge( WEST, EAST, ! west, ! east ) ) );
//        }
//    }
//
//    private void buildCorners( ImmutableList.Builder<QuadDef> builder, TextureAtlasSprite texture ) {
//        builder.add( new QuadDef( this.quad(
//            1, 1, 1,
//            texture,
//            POS3, POS2, POS2, TEX3, TEXQ,
//            POS2, POS3, POS2, TEXQ, TEX3,
//            POS2, POS2, POS3, TEXQ, TEXQ,
//            POS2, POS2, POS3, TEXQ, TEXQ
//        ), ALL ) );
//
//        builder.add( new QuadDef( this.quad(
//            1, 1, - 1,
//            texture,
//            POS2, POS2, POS0, TEXQ, TEXQ,
//            POS2, POS2, POS0, TEXQ, TEXQ,
//            POS2, POS3, POS1, TEXQ, TEX3,
//            POS3, POS2, POS1, TEX3, TEXQ
//        ), ALL ) );
//
//        builder.add( new QuadDef( this.quad(
//            1, - 1, - 1,
//            texture,
//            POS3, POS1, POS1, TEX3, TEXQ,
//            POS2, POS0, POS1, TEXQ, TEX3,
//            POS2, POS1, POS0, TEXQ, TEXQ,
//            POS2, POS1, POS0, TEXQ, TEXQ
//        ), ALL ) );
//
//        builder.add( new QuadDef( this.quad(
//            1, - 1, 1,
//            texture,
//            POS2, POS1, POS3, TEXQ, TEXQ,
//            POS2, POS1, POS3, TEXQ, TEXQ,
//            POS2, POS0, POS2, TEXQ, TEX3,
//            POS3, POS1, POS2, TEX3, TEXQ
//        ), ALL ) );
//
//        builder.add( new QuadDef( this.quad(
//            - 1, 1, 1,
//            texture,
//            POS1, POS2, POS3, TEXQ, TEXQ,
//            POS1, POS2, POS3, TEXQ, TEXQ,
//            POS1, POS3, POS2, TEXQ, TEX3,
//            POS0, POS2, POS2, TEX3, TEXQ
//        ), ALL ) );
//
//        builder.add( new QuadDef( this.quad(
//            - 1, 1, - 1,
//            texture,
//            POS0, POS2, POS1, TEX3, TEXQ,
//            POS1, POS3, POS1, TEXQ, TEX3,
//            POS1, POS2, POS0, TEXQ, TEXQ,
//            POS1, POS2, POS0, TEXQ, TEXQ
//        ), ALL ) );
//
//        builder.add( new QuadDef( this.quad(
//            - 1, - 1, - 1,
//            texture,
//            POS1, POS1, POS0, TEXQ, TEXQ,
//            POS1, POS1, POS0, TEXQ, TEXQ,
//            POS1, POS0, POS1, TEXQ, TEX3,
//            POS0, POS1, POS1, TEX3, TEXQ
//        ), ALL ) );
//
//        builder.add( new QuadDef( this.quad(
//            - 1, - 1, 1,
//            texture,
//            POS0, POS1, POS2, TEX3, TEXQ,
//            POS1, POS0, POS2, TEXQ, TEX3,
//            POS1, POS1, POS3, TEXQ, TEXQ,
//            POS1, POS1, POS3, TEXQ, TEXQ
//        ), ALL ) );
//    }
//
//    private static void addAll( ImmutableList.Builder<QuadDef> builder, List<BakedQuad> quads, Predicate<IModelData> pr ) {
//        for( BakedQuad quad : quads ) {
//            builder.add( new QuadDef( quad, pr ) );
//        }
//    }
//
//    private void buildInner( ImmutableList.Builder<QuadDef> builder, TextureAtlasSprite texture ) {
//
//        addAll( builder, this.quadDouble(
//            1, 0, 1,
//            texture,
//            POS1, POS3, POS1, TEX0, TEX3,
//            POS2, POS3, POS2, TEX2, TEX3,
//            POS2, POS0, POS2, TEX2, TEX0,
//            POS1, POS0, POS1, TEX0, TEX0
//        ), ALL );
//
//        addAll( builder, this.quadDouble(
//            1, 0, 1,
//            texture,
//            POS1, POS3, POS2, TEX0, TEX3,
//            POS2, POS3, POS1, TEX2, TEX3,
//            POS2, POS0, POS1, TEX2, TEX0,
//            POS1, POS0, POS2, TEX0, TEX0
//        ), ALL );
//
//        addAll( builder, this.quadDouble(
//            1, 1, 0,
//            texture,
//            POS1, POS1, POS3, TEX3, TEX0,
//            POS2, POS2, POS3, TEX3, TEX2,
//            POS2, POS2, POS0, TEX0, TEX2,
//            POS1, POS1, POS0, TEX0, TEX0
//        ), ALL );
//
//        addAll( builder, this.quadDouble(
//            1, 1, 0,
//            texture,
//            POS1, POS2, POS3, TEX3, TEX0,
//            POS2, POS1, POS3, TEX3, TEX2,
//            POS2, POS1, POS0, TEX0, TEX2,
//            POS1, POS2, POS0, TEX0, TEX0
//        ), ALL );
//
//        addAll( builder, this.quadDouble(
//            0, 1, 1,
//            texture,
//            POS3, POS1, POS1, TEX3, TEX0,
//            POS3, POS2, POS2, TEX3, TEX2,
//            POS0, POS2, POS2, TEX0, TEX2,
//            POS0, POS1, POS1, TEX0, TEX0
//        ), ALL );
//
//        addAll( builder, this.quadDouble(
//            0, 1, 1,
//            texture,
//            POS3, POS2, POS1, TEX3, TEX0,
//            POS3, POS1, POS2, TEX3, TEX2,
//            POS0, POS1, POS2, TEX0, TEX2,
//            POS0, POS2, POS1, TEX0, TEX0
//        ), ALL );
//    }
//
//    private void putVertex( UnpackedBakedQuad.Builder builder, Direction side, float x, float y, float z, float u, float v ) {
//        for( int e = 0; e < this.format.getElementCount(); e++ ) {
//            switch( this.format.getElement( e ).getUsage() ) {
//                case POSITION:
//                    float[] data = {
//                        x - side.getDirectionVec().getX() * eps, y, z - side.getDirectionVec().getZ() * eps, 1
//                    };
//                    if( this.transformation.isPresent() && ! this.transformation.get().isIdentity() ) {
//                        Vector3f vec = new Vector3f( data[ 0 ], data[ 1 ], data[ 2 ] );
//                        this.transformation.get().getMatrixVec().transform( vec );
//                        vec.get( data );
//                    }
//                    builder.put( e, data );
//                    break;
//                case COLOR:
//                    builder.put(
//                        e, 1, 1, 1, 1
//                    );
//                    break;
//                case UV:
//                    if( this.format.getElement( e ).getIndex() == 0 ) {
//                        builder.put( e, u, v, 0f, 1f );
//                        break;
//                    }
//                case NORMAL:
//                    builder.put( e, (float) side.getXOffset(), (float) side.getYOffset(), (float) side.getZOffset(), 0f );
//                    break;
//                default:
//                    builder.put( e );
//                    break;
//            }
//        }
//    }
//
//    private void putVertex( UnpackedBakedQuad.Builder builder, float nx, float ny, float nz, float x, float y, float z, float u, float v ) {
//        for( int e = 0; e < this.format.getElementCount(); e++ ) {
//            switch( this.format.getElement( e ).getUsage() ) {
//                case POSITION:
//                    float[] data = { x, y, z, 1 };
//                    if( this.transformation.isPresent() && ! this.transformation.get().isIdentity() ) {
//                        Vector3f vec = new Vector3f( data );
//                        this.transformation.get().getMatrixVec().transform( vec );
//                        vec.get( data );
//                    }
//                    builder.put( e, data );
//                    break;
//                case COLOR:
//                    builder.put(
//                        e, 1, 1, 1, 1
//                    );
//                    break;
//                case UV:
//                    if( this.format.getElement( e ).getIndex() == 0 ) {
//                        builder.put( e, u, v, 0f, 1f );
//                        break;
//                    }
//                case NORMAL:
//                    builder.put( e, nx, ny, nz, 0f );
//                    break;
//                default:
//                    builder.put( e );
//                    break;
//            }
//        }
//    }
//
//    @Override
//    public boolean isAmbientOcclusion() {
//        return true;
//    }
//
//    @Override
//    public boolean isGui3d() {
//        return true;
//    }
//
//    @Override
//    public boolean isBuiltInRenderer() {
//        return false;
//    }
//
//    @Override
//    public TextureAtlasSprite getParticleTexture() {
//        return this.texture;
//    }
//
//    @Override
//    public ItemOverrideList getOverrides() {
//        return ItemOverrideList.EMPTY;
//    }
//
//    @Override
//    @SuppressWarnings( "deprecation" )
//    public Pair<? extends IBakedModel, Matrix4f> handlePerspective( ItemCameraTransforms.TransformType type ) {
//        return PerspectiveMapWrapper.handlePerspective( this, this.transforms, type );
//    }
//
//    @Nonnull
//    @Override
//    public IModelData getModelData( @Nonnull IEnviromentBlockReader world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull IModelData tileData ) {
//        ModelDataMap data = BushModelProperties.createData();
//
//        if( state.getBlock() instanceof BushBlock ) {
//            ( (BushBlock) state.getBlock() ).fillModelData( world, pos, state, data );
//        }
//
//        return data;
//    }
//
//    private static class QuadDef {
//        private final BakedQuad quad;
//        private final Predicate<IModelData> predicate;
//
//        private QuadDef( BakedQuad quad, Predicate<IModelData> predicate ) {
//            this.quad = quad;
//            this.predicate = predicate;
//        }
//    }
//
//    private static class SideSelector implements Predicate<IModelData> {
//
//        private final ModelProperty<Boolean> property;
//
//        private SideSelector( ModelProperty<Boolean> property ) {
//            this.property = property;
//        }
//
//        @Override
//        public boolean test( IModelData state ) {
//            return getValue( state, property );
//        }
//    }
//}