/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.client.model.old.bush;


// TODO Re-evaluate
//public class BakedBushModel implements IBakedModel {
//    private final TextureAtlasSprite sprite;
//
//    private final List<BakedQuad> mainQuads = new ArrayList<>();
//    private final HashMap<ModelProperty<Boolean>, List<BakedQuad>> quads = new HashMap<>();
//    private final Optional<TRSRTransformation> transform;
//
//    public BakedBushModel( TextureAtlasSprite sprite, VertexFormat format, Optional<TRSRTransformation> transform ) {
//        this.sprite = sprite;
//        this.transform = transform;
//
//        QuadMaker maker = new QuadMaker( format );
//
//        y( 3, 3, 13, 13, 0, Direction.DOWN, maker, mainQuads );
//        y( 3, 3, 13, 13, 16, Direction.UP, maker, mainQuads );
//        x( 3, 3, 13, 13, 0, Direction.WEST, maker, mainQuads );
//        x( 3, 3, 13, 13, 16, Direction.EAST, maker, mainQuads );
//        z( 3, 3, 13, 13, 0, Direction.NORTH, maker, mainQuads );
//        z( 3, 3, 13, 13, 16, Direction.SOUTH, maker, mainQuads );
//    }
//
//    private void initMaker( QuadMaker maker, Direction dir ) {
//        maker.tint( 0 );
//        maker.sprite( sprite );
//        maker.diffuseLighting( false );
//        maker.orientation( dir );
//    }
//
//    private void y( float minX, float minZ, float maxX, float maxZ, float y, Direction dir, QuadMaker maker, List<BakedQuad> list ) {
//        maker.start( transform );
//        initMaker( maker, dir );
//        maker.pos( minX, y, minZ, 16 ).tex( minX, minZ, 16 );
//        maker.pos( maxX, y, minZ, 16 ).tex( maxX, minZ, 16 );
//        maker.pos( maxX, y, maxZ, 16 ).tex( maxX, maxZ, 16 );
//        maker.pos( minX, y, maxZ, 16 ).tex( minX, maxZ, 16 );
//        list.add( maker.make() );
//
//        maker.start( transform );
//        initMaker( maker, dir );
//        maker.pos( minX, y, maxZ, 16 ).tex( minX, maxZ, 16 );
//        maker.pos( maxX, y, maxZ, 16 ).tex( maxX, maxZ, 16 );
//        maker.pos( maxX, y, minZ, 16 ).tex( maxX, minZ, 16 );
//        maker.pos( minX, y, minZ, 16 ).tex( minX, minZ, 16 );
//        list.add( maker.make() );
//    }
//
//    private void y( float minX, float minZ, float maxX, float maxZ, float y1, float y2, float y3, float y4, Direction dir, QuadMaker maker, List<BakedQuad> list ) {
//        maker.start( transform );
//        initMaker( maker, dir );
//        maker.pos( minX, y1, minZ, 16 ).tex( minX, minZ, 16 );
//        maker.pos( maxX, y2, minZ, 16 ).tex( maxX, minZ, 16 );
//        maker.pos( maxX, y3, maxZ, 16 ).tex( maxX, maxZ, 16 );
//        maker.pos( minX, y4, maxZ, 16 ).tex( minX, maxZ, 16 );
//        list.add( maker.make() );
//
//        maker.start( transform );
//        initMaker( maker, dir );
//        maker.pos( minX, y4, maxZ, 16 ).tex( minX, maxZ, 16 );
//        maker.pos( maxX, y3, maxZ, 16 ).tex( maxX, maxZ, 16 );
//        maker.pos( maxX, y2, minZ, 16 ).tex( maxX, minZ, 16 );
//        maker.pos( minX, y1, minZ, 16 ).tex( minX, minZ, 16 );
//        list.add( maker.make() );
//    }
//
//    private void z( float minX, float minY, float maxX, float maxY, float z, Direction dir, QuadMaker maker, List<BakedQuad> list ) {
//        maker.start( transform );
//        initMaker( maker, dir );
//        maker.pos( minX, minY, z, 16 ).tex( minX, minY, 16 );
//        maker.pos( maxX, minY, z, 16 ).tex( maxX, minY, 16 );
//        maker.pos( maxX, maxY, z, 16 ).tex( maxX, maxY, 16 );
//        maker.pos( minX, maxY, z, 16 ).tex( minX, maxY, 16 );
//        list.add( maker.make() );
//
//        maker.start( transform );
//        initMaker( maker, dir );
//        maker.pos( minX, maxY, z, 16 ).tex( minX, maxY, 16 );
//        maker.pos( maxX, maxY, z, 16 ).tex( maxX, maxY, 16 );
//        maker.pos( maxX, minY, z, 16 ).tex( maxX, minY, 16 );
//        maker.pos( minX, minY, z, 16 ).tex( minX, minY, 16 );
//        list.add( maker.make() );
//    }
//
//    private void x( float minZ, float minY, float maxZ, float maxY, float x1, float x2, float x3, float x4, Direction dir, QuadMaker maker, List<BakedQuad> list ) {
//        maker.start( transform );
//        initMaker( maker, dir );
//        maker.pos( x1, minY, minZ, 16 ).tex( minZ, minY, 16 );
//        maker.pos( x2, minY, maxZ, 16 ).tex( maxZ, minY, 16 );
//        maker.pos( x3, maxY, maxZ, 16 ).tex( maxZ, maxY, 16 );
//        maker.pos( x4, maxY, minZ, 16 ).tex( minZ, maxY, 16 );
//        list.add( maker.make() );
//
//        maker.start( transform );
//        initMaker( maker, dir );
//        maker.pos( x4, maxY, minZ, 16 ).tex( minZ, maxY, 16 );
//        maker.pos( x3, maxY, maxZ, 16 ).tex( maxZ, maxY, 16 );
//        maker.pos( x2, minY, maxZ, 16 ).tex( maxZ, minY, 16 );
//        maker.pos( x1, minY, minZ, 16 ).tex( minZ, minY, 16 );
//        list.add( maker.make() );
//    }
//
//    private void x( float minZ, float minY, float maxZ, float maxY, float x, Direction dir, QuadMaker maker, List<BakedQuad> list ) {
//        maker.start( transform );
//        initMaker( maker, dir );
//        maker.pos( x, minY, minZ, 16 ).tex( minZ, minY, 16 );
//        maker.pos( x, minY, maxZ, 16 ).tex( maxZ, minY, 16 );
//        maker.pos( x, maxY, maxZ, 16 ).tex( maxZ, maxY, 16 );
//        maker.pos( x, maxY, minZ, 16 ).tex( minZ, maxY, 16 );
//        list.add( maker.make() );
//
//        maker.start( transform );
//        initMaker( maker, dir );
//        maker.pos( x, maxY, minZ, 16 ).tex( minZ, maxY, 16 );
//        maker.pos( x, maxY, maxZ, 16 ).tex( maxZ, maxY, 16 );
//        maker.pos( x, minY, maxZ, 16 ).tex( maxZ, minY, 16 );
//        maker.pos( x, minY, minZ, 16 ).tex( minZ, minY, 16 );
//        list.add( maker.make() );
//    }
//
//    @Override
//    public List<BakedQuad> getQuads( @Nullable BlockState state, @Nullable Direction side, Random rand ) {
//        return Collections.emptyList();
//    }
//
//    @Nonnull
//    @Override
//    public List<BakedQuad> getQuads( @Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData ) {
//        return Collections.emptyList();
//    }
//
//    @Override
//    public boolean isAmbientOcclusion() {
//        return false;
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
//        return sprite;
//    }
//
//    @Override
//    public ItemOverrideList getOverrides() {
//        return ItemOverrideList.EMPTY;
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
//}
