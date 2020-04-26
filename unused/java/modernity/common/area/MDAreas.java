/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 14 - 2020
 * Author: rgsw
 */

package modernity.common.area;

import com.google.common.reflect.TypeToken;
import modernity.client.render.area.AreaRenderManager;
import modernity.client.render.area.impl.ForestRunesAreaRender;
import modernity.common.area.core.Area;
import modernity.common.area.core.AreaBox;
import modernity.common.area.core.AreaType;
import modernity.common.registry.RegistryEventHandler;
import modernity.common.registry.RegistryHandler;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ObjectHolder;

import java.util.function.BiFunction;

@ObjectHolder( "modernity" )
public final class MDAreas {
    private static final RegistryHandler<AreaType<?>> ENTRIES = new RegistryHandler<>( "modernity" );

    public static final AreaType<ForestRunesArea> FOREST_RUNES = register( "forest_runes", ForestRunesArea::new, 20 );
    public static final AreaType<HeightmapsArea> HEIGHTMAPS = register( "heightmaps", HeightmapsArea::new, 20 );

    private static <T extends Area> AreaType<T> register( String id, BiFunction<World, AreaBox, T> factory, int updateInterval, String... aliases ) {
        return ENTRIES.register( id, new AreaType<>( factory, updateInterval ), aliases );
    }

    @SuppressWarnings( "unchecked" )
    public static void setup( RegistryEventHandler handler ) {
        TypeToken<AreaType<?>> token = new TypeToken<AreaType<?>>() {
        };
        handler.addHandler( (Class<AreaType<?>>) token.getRawType(), ENTRIES );
    }

    @OnlyIn( Dist.CLIENT )
    public static void setupClient( AreaRenderManager renderers ) {
        renderers.register( FOREST_RUNES, new ForestRunesAreaRender() );
    }

    private MDAreas() {
    }
}
