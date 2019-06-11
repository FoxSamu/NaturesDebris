/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 11 - 2019
 */

package modernity.common.world.dim;

import com.google.common.collect.ImmutableSet;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public class MDDimensions {
    public static final int MODERNITY_ID = 5;
    public static final ModernityDim MODERNITY = new ModernityDim( "modernity:modernity", ModernityDimension::new );

    private static ImmutableSet<ResourceLocation> dimensionResLocs;

    public static ImmutableSet<ResourceLocation> getDimensionResLocs() {
        return dimensionResLocs;
    }

    static {
        FMLJavaModLoadingContext.get().getModEventBus().register( new MDDimensions() );
    }

    private MDDimensions() {}

    public static void register( IForgeRegistry<ModDimension> registry ) {
        registry.register( MODERNITY );
        DimensionManager.registerDimension( new ResourceLocation( "modernity:modernity" ), MODERNITY, null );
    }

    public static void init( Set<ResourceLocation> missingNames ) {
        for( ResourceLocation loc : missingNames ) {
            if( loc.toString().equals( MODERNITY.id ) ) {
                DimensionManager.registerDimension( loc, MODERNITY, null );
            }
        }
    }

    public static class ModernityDim extends ModDimension {

        private final String id;
        private Supplier<Dimension> supplier;

        private final Function<DimensionType, ? extends Dimension> factory = (Function<DimensionType, Dimension>) dimensionType -> this.supplier.get();

        public DimensionType getType() {
            return DimensionType.byName( new ResourceLocation( this.id ) );
        }

        private ModernityDim( String id, Supplier<Dimension> supplier ) {
            this.id = id;
            this.supplier = supplier;
            this.setRegistryName( id );
        }

        @Override
        public Function<DimensionType, ? extends Dimension> getFactory() {
            return this.factory;
        }
    }

    @SubscribeEvent
    public void load( final FMLLoadCompleteEvent event ) {
        Set<ResourceLocation> locs = new HashSet<>();
        locs.addAll( IRegistry.DIMENSION_TYPE.keySet() );
        locs.addAll( ForgeRegistries.MOD_DIMENSIONS.getKeys() );
        dimensionResLocs = ImmutableSet.copyOf( locs );
    }
}
