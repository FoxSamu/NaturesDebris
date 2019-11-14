/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 11 - 2019
 */

package modernity.common.world.dimen;

import com.google.common.collect.ImmutableSet;
import modernity.common.Modernity;
import modernity.common.registry.RegistryEventHandler;
import modernity.common.registry.RegistryHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;

/**
 * Holder class for Modernity dimensions.
 */
@ObjectHolder( "modernity" )
public final class MDDimensions {
    private static final RegistryHandler<ModDimension> ENTRIES = new RegistryHandler<>( "modernity" );
    public static final ModernityDim MODERNITY = register( "modernity", new ModernityDim( MDSurfaceDimension::new ) );

    private static ImmutableSet<ResourceLocation> dimensionResLocs;

    public static ImmutableSet<ResourceLocation> getDimensionResLocs() {
        return dimensionResLocs;
    }

    static {
        Modernity.MOD_EVENT_BUS.register( new MDDimensions() );
    }

    private static <T extends ModDimension> T register( String id, T dim ) {
        ENTRIES.register( id, dim );
        return dim;
    }

    private MDDimensions() {
    }

    public static void setup( RegistryEventHandler handler ) {
        handler.addHandler( ModDimension.class, ENTRIES );
    }

    public static void restore( Set<ResourceLocation> missingNames ) {
        for( ResourceLocation loc : missingNames ) {
            if( loc.equals( MODERNITY.getRegistryName() ) ) {
                register( new ResourceLocation( "modernity:modernity" ), MODERNITY, true );
            }
        }
    }

    private static void register( ResourceLocation location, ModDimension dimen, boolean skylight ) {
        if( ! Registry.DIMENSION_TYPE.containsKey( location ) ) {
            DimensionManager.registerDimension( location, dimen, null, skylight );
        }
    }

    /**
     * Registers the dimensions to the {@link DimensionManager}.
     */
    public static void init() {
        register( new ResourceLocation( "modernity:modernity" ), MODERNITY, true );
    }

    public static class ModernityDim extends ModDimension {
        private final BiFunction<World, DimensionType, ? extends Dimension> factory;

        public DimensionType getType() {
            return DimensionType.byName( getRegistryName() );
        }

        private ModernityDim( BiFunction<World, DimensionType, ? extends Dimension> factory ) {
            this.factory = factory;
        }

        @Override
        public BiFunction<World, DimensionType, ? extends Dimension> getFactory() {
            return this.factory;
        }
    }

    @SubscribeEvent
    public void load( FMLLoadCompleteEvent event ) {
        Set<ResourceLocation> locs = new HashSet<>();
        locs.addAll( Registry.DIMENSION_TYPE.keySet() );
        locs.addAll( ForgeRegistries.MOD_DIMENSIONS.getKeys() );
        dimensionResLocs = ImmutableSet.copyOf( locs );
    }
}
