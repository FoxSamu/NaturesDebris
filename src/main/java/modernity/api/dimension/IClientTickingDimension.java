package modernity.api.dimension;

import modernity.client.ModernityClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Dimensions implementing this interface receive client side ticks from {@link ModernityClient}.
 */
@FunctionalInterface
public interface IClientTickingDimension {
    @OnlyIn( Dist.CLIENT )
    void tickClient();
}
