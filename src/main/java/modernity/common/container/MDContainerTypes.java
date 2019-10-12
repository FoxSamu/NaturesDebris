package modernity.common.container;

import com.google.common.reflect.TypeToken;
import modernity.client.gui.container.GuiNetherAltar;
import modernity.common.registry.RegistryEventHandler;
import modernity.common.registry.RegistryHandler;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder( "modernity" )
public final class MDContainerTypes {
    private static final RegistryHandler<ContainerType<?>> ENTRIES = new RegistryHandler<>( "modernity" );

    public static final ContainerType<NetherAltarContainer> NETHER_ALTAR = register(
        "modernity:nether_altar",
        new ContainerType<>(
            ( id, playerInventory ) ->
                new NetherAltarContainer( id, playerInventory, new Inventory( 5 ) )
        )
    );

    @SuppressWarnings( "unchecked" )
    private static <T extends Container> ContainerType<T> register( String id, ContainerType<?> type, String... aliases ) {
        ENTRIES.register( id, type, aliases );
        return (ContainerType<T>) type;
    }

    @SuppressWarnings( "unchecked" )
    public static void setup( RegistryEventHandler handler ) {
        TypeToken<ContainerType<?>> token = new TypeToken<ContainerType<?>>( ContainerType.class ) {
        };
        handler.addHandler( (Class<ContainerType<?>>) token.getRawType(), ENTRIES );
    }

    @OnlyIn( Dist.CLIENT )
    public static void registerScreens() {
        ScreenManager.registerFactory( NETHER_ALTAR, GuiNetherAltar::new );
    }

    private MDContainerTypes() {
    }
}
