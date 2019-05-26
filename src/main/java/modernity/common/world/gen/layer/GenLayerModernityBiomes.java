package modernity.common.world.gen.layer;

import net.minecraft.util.registry.IRegistry;
import net.minecraft.world.gen.IContext;
import net.minecraft.world.gen.area.AreaDimension;
import net.minecraft.world.gen.layer.traits.IAreaTransformer0;

import modernity.common.biome.MDBiomes;

public enum GenLayerModernityBiomes implements IAreaTransformer0 {
    INSTANCE;

    private static final int MEADOW = IRegistry.BIOME.getId( MDBiomes.MEADOW );
    private static final int FOREST = IRegistry.BIOME.getId( MDBiomes.FOREST );
    private static final int WATERLANDS = IRegistry.BIOME.getId( MDBiomes.WATERLANDS );

    private static final int[] biomes = {
            MEADOW,
            FOREST,
            WATERLANDS
    };

    @Override
    public int apply( IContext context, AreaDimension dimen, int x, int z ) {
        return biomes[ context.random( 3 ) ];
    }
}
