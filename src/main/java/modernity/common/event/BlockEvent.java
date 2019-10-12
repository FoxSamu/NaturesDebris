package modernity.common.event;

import modernity.common.Modernity;
import modernity.common.net.SEventPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public abstract class BlockEvent<T> extends ForgeRegistryEntry<BlockEvent<?>> {
    private final int range;

    protected BlockEvent( int range ) {
        this.range = Math.max( range, 1 );
    }

    protected BlockEvent() {
        this( 64 );
    }

    @OnlyIn( Dist.CLIENT )
    public abstract void playEvent( World world, BlockPos pos, T data );

    public abstract void writeData( T data, PacketBuffer buffer );

    @OnlyIn( Dist.CLIENT )
    public abstract T readData( PacketBuffer buffer );

    public final void play( @Nullable ServerPlayerEntity player, World world, BlockPos pos, T data ) {
        Modernity.network().sendToRange(
            new SEventPacket( pos, this, data ),
            new Vec3d( pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5 ),
            range,
            world.dimension.getType(),
            player
        );
    }

    public final void play( World world, BlockPos pos, T data ) {
        play( null, world, pos, data );
    }

    public final void play( @Nullable ServerPlayerEntity player, World world, BlockPos pos ) {
        play( player, world, pos, null );
    }

    public final void play( World world, BlockPos pos ) {
        play( null, world, pos, null );
    }
}
