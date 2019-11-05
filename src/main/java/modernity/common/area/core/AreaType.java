package modernity.common.area.core;

import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.apache.commons.lang3.Validate;

import java.util.function.BiFunction;

public class AreaType extends ForgeRegistryEntry<AreaType> {
    private final BiFunction<World, AreaBox, Area> factory;

    public final int updateInterval;

    public AreaType( BiFunction<World, AreaBox, Area> factory, int updateInterval ) {
        Validate.isTrue( updateInterval >= 0, "updateInterval < 0" );
        this.factory = factory;
        this.updateInterval = updateInterval;
    }

    public Area create( World world, AreaBox box ) {
        return factory.apply( world, box );
    }
}
