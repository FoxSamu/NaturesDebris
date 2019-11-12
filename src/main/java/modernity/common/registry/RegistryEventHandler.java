package modernity.common.registry;

import com.google.common.reflect.TypeToken;
import modernity.common.Modernity;
import modernity.common.area.MDAreas;
import modernity.common.area.core.AreaType;
import modernity.common.biome.MDBiomes;
import modernity.common.block.MDBlocks;
import modernity.common.container.MDContainerTypes;
import modernity.common.entity.MDEntityTypes;
import modernity.common.environment.event.EnvironmentEventType;
import modernity.common.environment.event.MDEnvEvents;
import modernity.common.event.BlockEvent;
import modernity.common.event.MDBlockEvents;
import modernity.common.fluid.MDFluids;
import modernity.common.item.MDItems;
import modernity.common.particle.MDParticleTypes;
import modernity.common.tileentity.MDTileEntitiyTypes;
import modernity.common.world.dimen.MDDimensions;
import modernity.common.world.gen.MDChunkGeneratorTypes;
import modernity.common.world.gen.biome.MDBiomeProviderTypes;
import modernity.common.world.gen.feature.MDFeatures;
import modernity.common.world.gen.placement.MDPlacements;
import modernity.common.world.gen.structure.MDStructures;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Handles the actual registry events.
 */
@SuppressWarnings( "unchecked" )
public enum RegistryEventHandler {
    INSTANCE;

    private final HashMap<Class<? extends IForgeRegistryEntry>, HashSet<RegistryHandler<?>>> registries = new HashMap<>();

    RegistryEventHandler() {
        MDItems.setup( this );
        MDBlocks.setup( this );
        MDFluids.setup( this );
        MDEntityTypes.setup( this );
        MDContainerTypes.setup( this );
        MDTileEntitiyTypes.setup( this );
        MDDimensions.setup( this );
        MDBiomes.setup( this );
        MDChunkGeneratorTypes.setup( this );
        MDBiomeProviderTypes.setup( this );
        MDStructures.setup( this );
        MDFeatures.setup( this );
        MDPlacements.setup( this );
        MDBlockEvents.setup( this );
        MDEnvEvents.setup( this );
        MDAreas.setup( this );
        MDParticleTypes.setup( this );
    }

    private <T extends IForgeRegistryEntry<T>> void register( Class<T> entryClass, IForgeRegistry<T> registry ) {
        HashSet<RegistryHandler<?>> list = registries.get( entryClass );
        if( list == null )
            return;
        for( RegistryHandler<?> handler : list ) {
            ( (RegistryHandler<T>) handler ).fillRegistry( registry );
        }
    }

    private <T extends IForgeRegistryEntry<T>> void remap( Class<T> entryClass, List<RegistryEvent.MissingMappings.Mapping<T>> registry ) {
        HashSet<RegistryHandler<?>> list = registries.get( entryClass );
        if( list == null )
            return;
        for( RegistryHandler<?> handler : list ) {
            ( (RegistryHandler<T>) handler ).remap( registry );
        }
    }

    /**
     * Adds a {@link RegistryHandler} for a specific registry type.
     */
    public <T extends IForgeRegistryEntry<T>> void addHandler( Class<T> entryClass, RegistryHandler<? extends T> handler ) {
        registries.computeIfAbsent( entryClass, ec -> new HashSet<>() ).add( handler );
    }

    // No subscription to generic event types: we want to listen to all registries and automatically detect a
    // registered RegistryHandler for that class... It saves us a lot of code here...
    @SubscribeEvent
    public void registerAll( RegistryEvent.Register event ) {
        register( event.getRegistry().getRegistrySuperType(), event.getRegistry() );
    }

    @SubscribeEvent
    public void registerRegistries( RegistryEvent.NewRegistry event ) {
        TypeToken<BlockEvent<?>> blockEventToken = new TypeToken<BlockEvent<?>>() {
        };
        TypeToken<AreaType<?>> areaTypeToken = new TypeToken<AreaType<?>>() {
        };
        new RegistryBuilder<BlockEvent<?>>()
            .setType( (Class<BlockEvent<?>>) blockEventToken.getRawType() )
            .setMaxID( Integer.MAX_VALUE - 1 )
            .disableSaving()
            .setName( Modernity.res( "block_events" ) )
            .create();

        new RegistryBuilder<EnvironmentEventType>()
            .setType( EnvironmentEventType.class )
            .setMaxID( Integer.MAX_VALUE - 1 )
            .setName( Modernity.res( "environment_events" ) )
            .create();

        new RegistryBuilder<AreaType<?>>()
            .setType( (Class<AreaType<?>>) areaTypeToken.getRawType() )
            .setMaxID( Integer.MAX_VALUE - 1 )
            .setName( Modernity.res( "area_types" ) )
            .create();
    }

    @SubscribeEvent
    public void remapMissing( RegistryEvent.MissingMappings event ) {
        remap( event.getRegistry().getRegistrySuperType(), event.getAllMappings() );
    }
}
