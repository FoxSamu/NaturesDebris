package modernity.client.colors;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILightReader;

import javax.annotation.Nullable;

public interface IColorProfile {
    int getColor( @Nullable ILightReader world, @Nullable BlockPos pos );
    int getItemColor();
}
