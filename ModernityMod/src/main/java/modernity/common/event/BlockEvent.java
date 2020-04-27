/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.event;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

/**
 * A block event is an event that happens in a world at a specific block position. This event is sent to observing
 * clients where it is played.
 */
public abstract class BlockEvent<T> extends ForgeRegistryEntry<BlockEvent<?>> {
    private final int range;

    /**
     * Creates a block event with specific notify range.
     */
    protected BlockEvent( int range ) {
        this.range = Math.max( range, 1 );
    }

    /**
     * Creates a block event with default notify range of 64 blocks.
     */
    protected BlockEvent() {
        this( 64 );
    }

    /**
     * Plays the event.
     *
     * @param world The world to play in.
     * @param pos   The pos to play at.
     * @param data  The data sent with the event.
     */
    @OnlyIn( Dist.CLIENT )
    public abstract void playEvent( World world, BlockPos pos, T data );

    /**
     * Writes the data to the specified packet buffer.
     *
     * @param data   The data to write
     * @param buffer The buffer to write to
     */
    public abstract void writeData( T data, PacketBuffer buffer );

    /**
     * Reads the data from the specified packet buffer.
     *
     * @param buffer The buffer to read from
     * @return The read data
     */
    @OnlyIn( Dist.CLIENT )
    public abstract T readData( PacketBuffer buffer );

    /**
     * Plays this event
     *
     * @param player The player not to sent the event to. When null, every near player receives the event.
     * @param world  The world to play the event in.
     * @param pos    The location to play the event at.
     * @param data   Additional data to send with the event.
     */
    public final void play( @Nullable ServerPlayerEntity player, World world, BlockPos pos, T data ) {
        if( ! world.isRemote ) { // TODO Send these important packets!
//            Modernity.network().sendToRange(
//                new SEventPacket( pos, this, data ),
//                new Vec3d( pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5 ),
//                range,
//                world.dimension.getType(),
//                player
//            );
        }
    }

    /**
     * Plays this event
     *
     * @param world The world to play the event in.
     * @param pos   The location to play the event at.
     * @param data  Additional data to send with the event.
     */
    public final void play( World world, BlockPos pos, T data ) {
        play( null, world, pos, data );
    }

    /**
     * Plays this event
     *
     * @param player The player not to sent the event to. When null, every near player receives the event.
     * @param world  The world to play the event in.
     * @param pos    The location to play the event at.
     */
    public final void play( @Nullable ServerPlayerEntity player, World world, BlockPos pos ) {
        play( player, world, pos, null );
    }

    /**
     * Plays this event
     *
     * @param world The world to play the event in.
     * @param pos   The location to play the event at.
     */
    public final void play( World world, BlockPos pos ) {
        play( null, world, pos, null );
    }
}
