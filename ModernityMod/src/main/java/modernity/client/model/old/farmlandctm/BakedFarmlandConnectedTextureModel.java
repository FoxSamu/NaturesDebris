/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 25 - 2020
 * Author: rgsw
 */

package modernity.client.model.old.farmlandctm;


// TODO Re-evaluate
//public class BakedFarmlandConnectedTextureModel implements IBakedModel {
//    private static final int CENTER = 0;
//    private static final int ALL = 1;
//    private static final int CROSS = 2;
//    private static final int HORIZONTAL = 3;
//    private static final int VERTICAL = 4;
//
//    // NE SE SW NW
//    private static final String[] CONNECTION_CODES = {
//        "cccc", "hhcc", "hhhh", "cchh", "hxvc", "cvxh", "xxvv", "hxxh", "axxx", "xaxx", "xxaa", "axxa",
//        "cvvc", "havc", "haah", "cvah", "xvch", "vchx", "xhhx", "vvxx", "xxxa", "xxax", "xaax", "aaxx",
//        "vvvv", "aavv", "aaaa", "vvaa", "xavv", "hxah", "axvv", "haxh", "axaa", "aaxa", "axax", "xaxa",
//        "vccv", "ahcv", "avva", "vcha", "avvx", "vvxa", "xhha", "vvax", "xaaa", "aaax", "xxxx"
//    };
//
//    private static final int[][] CONNECTIONS = new int[ 47 ][ 4 ];
//
//    static {
//        for( int i = 0; i < 47; i++ ) {
//            int[] conn = CONNECTIONS[ i ];
//            String code = CONNECTION_CODES[ i ];
//            for( int j = 0; j < 4; j++ ) {
//                char c = code.charAt( j );
//                if( c == 'c' || c == 'C' ) conn[ j ] = CENTER;
//                if( c == 'x' || c == 'X' ) conn[ j ] = CROSS;
//                if( c == 'v' || c == 'V' ) conn[ j ] = VERTICAL;
//                if( c == 'h' || c == 'H' ) conn[ j ] = HORIZONTAL;
//                if( c == 'a' || c == 'A' ) conn[ j ] = ALL;
//            }
//        }
//    }
//
//    private final TextureAtlasSprite all;
//    private final TextureAtlasSprite particle;
//    private final TextureAtlasSprite center;
//    private final TextureAtlasSprite cross;
//    private final TextureAtlasSprite horizontal;
//    private final TextureAtlasSprite vertical;
//
//    private final BakedQuad[] nw = new BakedQuad[ 5 ];
//    private final BakedQuad[] ne = new BakedQuad[ 5 ];
//    private final BakedQuad[] sw = new BakedQuad[ 5 ];
//    private final BakedQuad[] se = new BakedQuad[ 5 ];
//
//    private final ConnectionState[] cache = new ConnectionState[ 256 ];
//
//    public BakedFarmlandConnectedTextureModel( int tint, float y, VertexFormat format, Optional<TRSRTransformation> transform, TextureAtlasSprite all, TextureAtlasSprite center, TextureAtlasSprite cross, TextureAtlasSprite horizontal, TextureAtlasSprite vertical, TextureAtlasSprite particle ) {
//        this.all = all;
//        this.center = center;
//        this.cross = cross;
//        this.horizontal = horizontal;
//        this.vertical = vertical;
//        this.particle = particle;
//
//        TextureAtlasSprite[] sprites = {
//            center, all, cross, horizontal, vertical
//        };
//
//        QuadMaker maker = new QuadMaker( format );
//
//        make( maker, nw, false, false, y, tint, sprites, transform );
//        make( maker, ne, true, false, y, tint, sprites, transform );
//        make( maker, sw, false, true, y, tint, sprites, transform );
//        make( maker, se, true, true, y, tint, sprites, transform );
//
//        maker.start( transform );
//        maker.tint( tint );
//    }
//
//    private void make( QuadMaker maker, BakedQuad[] quads, boolean offX, boolean offZ, float y, int tint, TextureAtlasSprite[] sprites, Optional<TRSRTransformation> transform ) {
//        int nx = offX ? 8 : 0;
//        int nz = offZ ? 8 : 0;
//        int px = offX ? 16 : 8;
//        int pz = offZ ? 16 : 8;
//        float nu = offX ? 8 : 0.003F;
//        float nv = offZ ? 8 : 0.003F;
//        float pu = offX ? 15.997F : 8;
//        float pv = offZ ? 15.997F : 8;
//
//        for( int i = 0; i < 5; i++ ) {
//            maker.start( transform );
//            maker.tint( tint );
//            maker.sprite( sprites[ i ] );
//            maker.diffuseLighting( true );
//            maker.orientation( Direction.UP );
//            maker.pos( nx, y, nz, 16 ).tex( nu, nv, 16 ).end();
//            maker.pos( nx, y, pz, 16 ).tex( nu, pv, 16 ).end();
//            maker.pos( px, y, pz, 16 ).tex( pu, pv, 16 ).end();
//            maker.pos( px, y, nz, 16 ).tex( pu, nv, 16 ).end();
//
//            quads[ i ] = maker.make();
//        }
//    }
//
//    @Override
//    public List<BakedQuad> getQuads( @Nullable BlockState state, @Nullable Direction side, Random rand ) {
//        return Collections.emptyList();
//    }
//
//    @Nonnull
//    @Override
//    public List<BakedQuad> getQuads( @Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nullable IModelData extraData ) {
//        if( side == Direction.UP ) {
//            Integer ctmObj = extraData != null ? extraData.getData( ITopTextureConnectionBlock.CONNECTIONS ) : 0;
//            int ctm = ctmObj == null ? 0 : ctmObj;
//
//            if( cache[ ctm ] != null ) return cache[ ctm ].quads;
//
//            boolean up = ( ctm & CTMUtil.UP ) != 0;
//            boolean right = ( ctm & CTMUtil.RIGHT ) != 0;
//            boolean down = ( ctm & CTMUtil.DOWN ) != 0;
//            boolean left = ( ctm & CTMUtil.LEFT ) != 0;
//            boolean upright = ( ctm & CTMUtil.UPRIGHT ) != 0;
//            boolean downright = ( ctm & CTMUtil.DOWNRIGHT ) != 0;
//            boolean downleft = ( ctm & CTMUtil.DOWNLEFT ) != 0;
//            boolean upleft = ( ctm & CTMUtil.UPLEFT ) != 0;
//
//            BakedQuad[] quads = {
//                getQuad( ne, ! right, ! up, ! upright ),
//                getQuad( nw, ! left, ! up, ! upleft ),
//                getQuad( se, ! right, ! down, ! downright ),
//                getQuad( sw, ! left, ! down, ! downleft ),
//            };
//
//            ConnectionState cs = new ConnectionState( Arrays.asList( quads ) );
//            cache[ ctm ] = cs;
//
//            return cs.quads;
//        }
//        return Collections.emptyList();
//    }
//
//    private BakedQuad getQuad( BakedQuad[] list, boolean horSide, boolean vertSide, boolean corner ) {
//        if( horSide && vertSide ) return list[ CENTER ];
//        else if( horSide ) return list[ VERTICAL ];
//        else if( vertSide ) return list[ HORIZONTAL ];
//        else if( corner ) return list[ CROSS ];
//        else return list[ ALL ];
//    }
//
//    @Nonnull
//    @Override
//    public IModelData getModelData( @Nonnull IEnviromentBlockReader world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull IModelData tileData ) {
//        if( state.getBlock() instanceof ITopTextureConnectionBlock ) {
//            IModelData data = new ModelDataMap.Builder().withProperty( ITopTextureConnectionBlock.CONNECTIONS ).build();
//            ( (ITopTextureConnectionBlock) state.getBlock() ).fillModelData(
//                world, pos, state, data
//            );
//            return data;
//        }
//        return tileData;
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
//        return true;
//    }
//
//    @Override
//    public TextureAtlasSprite getParticleTexture() {
//        return particle;
//    }
//
//    @Override
//    public ItemOverrideList getOverrides() {
//        return ItemOverrideList.EMPTY;
//    }
//
//    private static class ConnectionState {
//        private final List<BakedQuad> quads;
//
//        private ConnectionState( List<BakedQuad> quads ) {
//            this.quads = quads;
//        }
//    }
//}
