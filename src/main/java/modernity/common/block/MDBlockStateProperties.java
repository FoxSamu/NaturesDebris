package modernity.common.block;

import modernity.api.util.EWaterlogType;
import net.minecraft.state.EnumProperty;

// TODO: Move all block state properties to here
public final class MDBlockStateProperties {
    public static final EnumProperty<EWaterlogType> WATERLOGGED = EnumProperty.create( "waterlogged", EWaterlogType.class );

    private MDBlockStateProperties() {
    }
}
