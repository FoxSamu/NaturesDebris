package modernity.common.world.gen.layer;

import net.minecraft.world.gen.IContext;
import net.minecraft.world.gen.area.AreaDimension;
import net.minecraft.world.gen.layer.traits.IAreaTransformer0;

public enum GenLayerRiverInit implements IAreaTransformer0 {
    INSTANCE;

    @Override
    public int apply( IContext context, AreaDimension dimen, int x, int z ) {
        return context.random( 299999 ) + 2;
    }
}