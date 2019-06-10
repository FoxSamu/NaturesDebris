package modernity.common.world.gen.layer;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.gen.IContextExtended;
import net.minecraft.world.gen.LazyAreaLayerContext;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.area.IAreaFactory;
import net.minecraft.world.gen.area.LazyArea;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerSmooth;
import net.minecraft.world.gen.layer.GenLayerVoronoiZoom;
import net.minecraft.world.gen.layer.GenLayerZoom;
import net.minecraft.world.gen.layer.traits.IAreaTransformer1;

import modernity.api.util.EMDDimension;

import java.util.function.LongFunction;

public class MDLayerUtil {
    public static <T extends IArea, C extends IContextExtended<T>> IAreaFactory<T> repeat( long seed, IAreaTransformer1 parent, IAreaFactory<T> factory, int count, LongFunction<C> contextFactory ) {
        for( int i = 0; i < count; ++ i ) {
            factory = parent.apply( contextFactory.apply( seed + (long) i ), factory );
        }

        return factory;
    }

    public static <T extends IArea, C extends IContextExtended<T>> ImmutableList<IAreaFactory<T>> buildModernityProcedure( LongFunction<C> contextFactory ) {
        IAreaFactory<T> factory = new ModernityBiomeFactory( EMDDimension.SURFACE, contextFactory.apply( 5291L ) );
        factory = GenLayerZoom.NORMAL.apply( contextFactory.apply( 3977L ), factory );
        factory = GenLayerZoom.NORMAL.apply( contextFactory.apply( 3978L ), factory );
        factory = GenLayerZoom.NORMAL.apply( contextFactory.apply( 3979L ), factory );
        factory = GenLayerZoom.NORMAL.apply( contextFactory.apply( 3980L ), factory );
        factory = GenLayerZoom.NORMAL.apply( contextFactory.apply( 3981L ), factory );
        factory = GenLayerZoom.NORMAL.apply( contextFactory.apply( 3982L ), factory );

        IAreaFactory<T> rivers = GenLayerRiverInit.INSTANCE.apply( contextFactory.apply( 5102L ) );
        rivers = repeat( 1000L, GenLayerZoom.NORMAL, rivers, 6, contextFactory );
        rivers = GenLayerRiver.INSTANCE.apply( contextFactory.apply( 9911L ), rivers );
        rivers = GenLayerSmooth.INSTANCE.apply( contextFactory.apply( 7716L ), rivers );

        factory = GenLayerRiverMix.INSTANCE.apply( contextFactory.apply( 1234L ), factory, rivers );

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
