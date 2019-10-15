/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 11 - 2019
 */

package modernity.common.world.gen.layer;

import com.google.common.collect.ImmutableList;
import modernity.api.util.EMDDimension;
import net.minecraft.world.gen.IExtendedNoiseRandom;
import net.minecraft.world.gen.LazyAreaLayerContext;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.area.IAreaFactory;
import net.minecraft.world.gen.area.LazyArea;
import net.minecraft.world.gen.layer.Layer;
import net.minecraft.world.gen.layer.SmoothLayer;
import net.minecraft.world.gen.layer.VoroniZoomLayer;
import net.minecraft.world.gen.layer.ZoomLayer;
import net.minecraft.world.gen.layer.traits.IAreaTransformer1;

import java.util.function.LongFunction;

public final class MDLayerUtil {
    private MDLayerUtil() {
    }

    public static <T extends IArea, C extends IExtendedNoiseRandom<T>> IAreaFactory<T> repeat( long seed, IAreaTransformer1 parent, IAreaFactory<T> factory, int count, LongFunction<C> contextFactory ) {
        for( int i = 0; i < count; ++ i ) {
            factory = parent.apply( contextFactory.apply( seed + (long) i ), factory );
        }

        return factory;
    }

    public static <T extends IArea, C extends IExtendedNoiseRandom<T>> ImmutableList<IAreaFactory<T>> buildSurfaceProcedure( LongFunction<C> contextFactory ) {
        IAreaFactory<T> factory = new MDBiomeFactory<>( EMDDimension.SURFACE, contextFactory.apply( 5291L ) );
        factory = ZoomLayer.NORMAL.apply( contextFactory.apply( 3977L ), factory );
        factory = ZoomLayer.NORMAL.apply( contextFactory.apply( 3978L ), factory );
        factory = ZoomLayer.NORMAL.apply( contextFactory.apply( 3979L ), factory );
        factory = ZoomLayer.NORMAL.apply( contextFactory.apply( 3980L ), factory );
        factory = ZoomLayer.NORMAL.apply( contextFactory.apply( 3981L ), factory );
        factory = ZoomLayer.NORMAL.apply( contextFactory.apply( 3982L ), factory );

        IAreaFactory<T> rivers = MDRiverInitLayer.INSTANCE.apply( contextFactory.apply( 5102L ) );
        rivers = repeat( 1000L, ZoomLayer.NORMAL, rivers, 6, contextFactory );
        rivers = MDRiverLayer.INSTANCE.apply( contextFactory.apply( 9911L ), rivers );
        rivers = SmoothLayer.INSTANCE.apply( contextFactory.apply( 7716L ), rivers );

        factory = MDRiverMixLayer.INSTANCE.apply( contextFactory.apply( 1234L ), factory, rivers );

        IAreaFactory<T> factory1 = VoroniZoomLayer.INSTANCE.apply( contextFactory.apply( 9818L ), factory );
        return ImmutableList.of( factory, factory1, factory );
    }

    public static Layer[] buildSurfaceProcedure( long seed ) {
        ImmutableList<IAreaFactory<LazyArea>> factoryList = buildSurfaceProcedure(
            localSeed -> new LazyAreaLayerContext( 1, seed, localSeed )
        );

        Layer layer1 = new Layer( factoryList.get( 0 ) );
        Layer layer2 = new Layer( factoryList.get( 1 ) );
        Layer layer3 = new Layer( factoryList.get( 2 ) );
        return new Layer[] { layer1, layer2, layer3 };
    }
}
