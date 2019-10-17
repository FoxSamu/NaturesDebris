package modernity.common.world.gen.feature;

import com.google.common.reflect.TypeToken;
import modernity.common.block.MDBlocks;
import modernity.common.registry.RegistryEventHandler;
import modernity.common.registry.RegistryHandler;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.registries.ObjectHolder;

/**
 * Holder class for all Modernity features.
 */
@ObjectHolder( "modernity" )
public final class MDFeatures {
    private static final RegistryHandler<Feature<?>> ENTRIES = new RegistryHandler<>( "modernity" );

    public static final GroupedBushFeature GROUPED_BUSH = register( "grouped_bush", new GroupedBushFeature() );
    public static final ClusterBushFeature CLUSTER_BUSH = register( "cluster_bush", new ClusterBushFeature() );
    public static final DepositFeature DEPOSIT = register( "deposit", new DepositFeature() );
    public static final LakeFeature LAKE = register( "lake", new LakeFeature() );
    public static final FluidFallFeature FLUID_FALL = register( "fluid_fall", new FluidFallFeature() );
    public static final MinableFeature MINABLE = register( "minable", new MinableFeature() );

    public static final HangTreeFeature BLACKWOOD_TREE = register( "blackwood_tree", new HangTreeFeature(
        MDBlocks.BLACKWOOD_LEAVES.getDefaultState(),
        MDBlocks.BLACKWOOD_LOG.getDefaultState(),
        MDBlocks.BLACKWOOD.getDefaultState()
    ) );
    public static final SphericalTreeFeature INVER_TREE = register( "inver_tree", new SphericalTreeFeature(
        MDBlocks.INVER_LEAVES.getDefaultState(),
        MDBlocks.INVER_LOG.getDefaultState(),
        MDBlocks.INVER.getDefaultState()
    ) );

    private static <T extends Feature<?>> T register( String id, T feature ) {
        return ENTRIES.register( id, feature );
    }

    @SuppressWarnings( "unchecked" )
    public static void setup( RegistryEventHandler handler ) {
        TypeToken<Feature<?>> token = new TypeToken<Feature<?>>() {
        };
        handler.addHandler( (Class<Feature<?>>) token.getRawType(), ENTRIES );
    }

    private MDFeatures() {
    }
}