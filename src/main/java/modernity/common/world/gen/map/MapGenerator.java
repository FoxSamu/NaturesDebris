package modernity.common.world.gen.map;

import net.minecraft.world.IWorld;
import net.minecraft.world.gen.WorldGenRegion;

import java.util.Random;

public abstract class MapGenerator {
    public final IWorld world;
    public final Random rand;

    public MapGenerator( IWorld world ) {
        this.world = world;
        this.rand = new Random( world.getSeed() );
    }

    public abstract void generate( WorldGenRegion region );
}
