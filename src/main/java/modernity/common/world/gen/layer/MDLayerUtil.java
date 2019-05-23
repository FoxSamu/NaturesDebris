package modernity.common.world.gen.layer;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.gen.IContextExtended;
import net.minecraft.world.gen.LazyAreaLayerContext;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.area.IAreaFactory;
import net.minecraft.world.gen.area.LazyArea;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerVoronoiZoom;
import net.minecraft.world.gen.layer.GenLayerZoom;

import java.util.function.LongFunction;

public class MDLayerUtil {
    public static <T extends IArea, C extends IContextExtended<T>> ImmutableList<IAreaFactory<T>> buildModernityProcedure( LongFunction<C> contextFactory ) {
        IAreaFactory<T> factory = GenLayerModernityBiomes.INSTANCE.apply( contextFactory.apply( 5291L ) );
        factory = GenLayerZoom.NORMAL.apply( contextFactory.apply( 3977L ), factory );
        factory = GenLayerZoom.NORMAL.apply( contextFactory.apply( 3978L ), factory );
        factory = GenLayerZoom.NORMAL.apply( contextFactory.apply( 3979L ), factory );
        factory = GenLayerZoom.NORMAL.apply( contextFactory.apply( 3980L ), factory );
        factory = GenLayerZoom.NORMAL.apply( contextFactory.apply( 3981L ), factory );

        IAreaFactory<T> factory1 = GenLayerVoronoiZoom.INSTANCE.apply( contextFactory.apply( 9818L ), factory );
        return ImmutableList.of( factory, factory1, factory );
    }

    public static GenLayer[] buildModernityProcedure( long seed ) {
        int i = 1;
        int[] idCounter = new int[ 1 ];
        ImmutableList<IAreaFactory<LazyArea>> factoryList = buildModernityProcedure( localSeed -> {
            ++ idCounter[ 0 ];
            return new LazyAreaLayerContext( 1, idCounter[ 0 ], seed, localSeed );
        } );

        GenLayer layer1 = new GenLayer( factoryList.get( 0 ) );
        GenLayer layer2 = new GenLayer( factoryList.get( 1 ) );
        GenLayer layer3 = new GenLayer( factoryList.get( 2 ) );
        return new GenLayer[] { layer1, layer2, layer3 };
    }
}
