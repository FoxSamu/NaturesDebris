package modernity.common.area;

import modernity.common.area.core.Area;
import modernity.common.area.core.AreaBox;
import modernity.common.area.core.AreaType;
import modernity.common.registry.RegistryEventHandler;
import modernity.common.registry.RegistryHandler;
import net.minecraft.world.World;
import net.minecraftforge.registries.ObjectHolder;

import java.util.function.BiFunction;

@ObjectHolder( "modernity" )
public class MDAreas {
    private static final RegistryHandler<AreaType> ENTRIES = new RegistryHandler<>( "modernity" );

    public static final AreaType TEST = register( "test", TestArea::new, 20 );

    private static AreaType register( String id, BiFunction<World, AreaBox, Area> factory, int updateInterval, String... aliases ) {
        return ENTRIES.register( id, new AreaType( factory, updateInterval ), aliases );
    }

    public static void setup( RegistryEventHandler handler ) {
        handler.addHandler( AreaType.class, ENTRIES );
    }
}
