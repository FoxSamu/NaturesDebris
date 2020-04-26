/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.common.particle;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.registries.ForgeRegistries;
import net.redgalaxy.util.ColorUtil;

/**
 * Type of particle data that holds a color.
 */
public class RgbParticleData implements IParticleData {
    public static final IDeserializer<RgbParticleData> DESERIALIZER = new IDeserializer<RgbParticleData>() {

        private final SimpleCommandExceptionType error = new SimpleCommandExceptionType( new LiteralMessage( "Expected 6 hexadecimal characters" ) );

        @Override
        public RgbParticleData deserialize( ParticleType<RgbParticleData> type, StringReader reader ) throws CommandSyntaxException {
            reader.expect( '#' );
            if( ! reader.canRead( 6 ) ) {
                throw error.createWithContext( reader );
            }
            int c = reader.getCursor();
            String str = reader.getString().substring( c, c + 6 );
            try {
                int i = Integer.parseInt( str, 16 );
                reader.setCursor( c + 6 );
                return new RgbParticleData( type, i );
            } catch( NumberFormatException exc ) {
                throw error.createWithContext( reader );
            }
        }

        @Override
        public RgbParticleData read( ParticleType<RgbParticleData> type, PacketBuffer buffer ) {
            return new RgbParticleData( type, buffer.readInt() );
        }
    };

    private final ParticleType<RgbParticleData> type;
    private final int rgb;

    public RgbParticleData( ParticleType<RgbParticleData> type, int rgb ) {
        this.type = type;
        this.rgb = rgb;
    }

    public RgbParticleData( ParticleType<RgbParticleData> type, int r, int g, int b ) {
        this.type = type;
        this.rgb = ColorUtil.rgb( r, g, b );
    }

    public RgbParticleData( ParticleType<RgbParticleData> type, double r, double g, double b ) {
        this.type = type;
        this.rgb = ColorUtil.rgb( r, g, b );
    }

    public int rgb() {
        return rgb;
    }

    public double red() {
        return ColorUtil.red( rgb );
    }

    public double green() {
        return ColorUtil.green( rgb );
    }

    public double blue() {
        return ColorUtil.blue( rgb );
    }

    @Override
    public ParticleType<RgbParticleData> getType() {
        return type;
    }

    @Override
    public void write( PacketBuffer buffer ) {
        buffer.writeInt( rgb );
    }

    @Override
    public String getParameters() {
        return ForgeRegistries.PARTICLE_TYPES.getKey( type ) + " #" + Integer.toString( rgb, 16 );
    }
}
