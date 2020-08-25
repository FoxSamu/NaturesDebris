/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.area;

import com.google.common.reflect.TypeToken;
import natures.debris.common.area.core.Area;
import natures.debris.common.area.core.AreaBox;
import natures.debris.common.area.core.AreaType;
import natures.debris.common.registryold.RegistryEventHandler;
import natures.debris.common.registryold.RegistryHandler;
import net.minecraft.world.World;

import java.util.function.BiFunction;

public final class MDAreas {
    private static final RegistryHandler<AreaType<?>> ENTRIES = new RegistryHandler<>("natures/debris");

    public static final AreaType<ForestRunesArea> FOREST_RUNES = register("forest_runes", ForestRunesArea::new, 20);
    public static final AreaType<HeightmapsArea> HEIGHTMAPS = register("heightmaps", HeightmapsArea::new, 20);

    private MDAreas() {
    }

    private static <T extends Area> AreaType<T> register(String id, BiFunction<World, AreaBox, T> factory, int updateInterval, String... aliases) {
        return ENTRIES.register(id, new AreaType<>(factory, updateInterval), aliases);
    }

//    @OnlyIn( Dist.CLIENT )
//    public static void setupClient( AreaRenderManager renderers ) {
//        renderers.register( FOREST_RUNES, new ForestRunesAreaRender() );
//    }

    @SuppressWarnings("unchecked")
    public static void setup(RegistryEventHandler handler) {
        TypeToken<AreaType<?>> token = new TypeToken<AreaType<?>>() {
        };
        handler.addHandler((Class<AreaType<?>>) token.getRawType(), ENTRIES);
    }
}
