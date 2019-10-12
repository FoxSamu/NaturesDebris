package modernity.common.event;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class StateBlockEvent extends BlockEvent<BlockState> {

    protected StateBlockEvent( int range ) {
        super( range );
    }

    protected StateBlockEvent() {
        super();
    }

    @Override
    public void writeData( BlockState data, PacketBuffer buffer ) {
        buffer.writeInt( Block.BLOCK_STATE_IDS.get( data ) );
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public BlockState readData( PacketBuffer buffer ) {
        return Block.BLOCK_STATE_IDS.getByValue( buffer.readInt() );
    }
}
