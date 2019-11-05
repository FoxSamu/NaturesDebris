package modernity.common.area;

import modernity.common.area.core.Area;
import modernity.common.area.core.AreaBox;
import modernity.common.area.core.ISidedTickableArea;
import net.minecraft.world.World;

public class TestArea extends Area implements ISidedTickableArea {
    public TestArea( World world, AreaBox box ) {
        super( MDAreas.TEST, world, box );
    }

    @Override
    public void tickServer() {
    }

    @Override
    public void tickClient() {
    }
}
