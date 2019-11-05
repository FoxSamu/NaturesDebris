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
        System.out.println( "Server Area Tick" );
    }

    @Override
    public void tickClient() {
        System.out.println( "Client Area Tick" );
    }
}
