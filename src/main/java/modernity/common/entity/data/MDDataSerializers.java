/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 12 - 2019
 */

package modernity.common.entity.data;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;

public class MDDataSerializers {
    public static final DataSerializer<IBlockState> BLOCK_STATE = new DataSerializer<IBlockState>() {
        public void write( PacketBuffer buf, IBlockState value ) {
            buf.writeVarInt( Block.getStateId( value ) );
        }

        public IBlockState read( PacketBuffer buf ) {
            int id = buf.readVarInt();
            return Block.getStateById( id );
        }

        public DataParameter<IBlockState> createKey( int id ) {
            return new DataParameter<>( id, this );
        }

        public IBlockState copyValue( IBlockState value ) {
            return value;
        }
    };

    static {
        DataSerializers.registerSerializer( BLOCK_STATE );
    }
}
