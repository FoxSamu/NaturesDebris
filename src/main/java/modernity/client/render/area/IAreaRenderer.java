package modernity.client.render.area;

import modernity.common.area.core.Area;
import net.minecraft.world.World;

@FunctionalInterface
public interface IAreaRenderer<A extends Area> {
    void render( World world, A area, double x, double y, double z, float partialTicks );
}
