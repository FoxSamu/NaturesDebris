package modernity.client.render.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.model.IBakedModel;

import java.util.HashMap;

public class BlockModelCache {
    private final HashMap<Integer, HashMap<Integer, IBakedModel>> cacheMap = new HashMap<>();
    private final BlockModelShapes shapes;
    private IBlockState lastRenderedState;
    private IBakedModel lastRenderedModel;

    public BlockModelCache( BlockModelShapes shapes ) {
        this.shapes = shapes;
    }

    public IBakedModel getModel( IBlockState state ) {
        if( lastRenderedState == state && lastRenderedModel != null ) return lastRenderedModel;

        Block block = state.getBlock();
        int blockHash = System.identityHashCode( block );
        int stateHash = System.identityHashCode( state );

        IBakedModel model = cacheMap.computeIfAbsent( blockHash, hash -> new HashMap<>() ).get( stateHash );

        if( model == null ) {
            model = shapes.getModel( state );
            cacheMap.get( blockHash ).put( stateHash, model );
        }

        lastRenderedState = state;
        lastRenderedModel = model;
        return model;
    }

    public void clear() {
        lastRenderedState = null;
        lastRenderedModel = null;
        cacheMap.clear();
    }
}