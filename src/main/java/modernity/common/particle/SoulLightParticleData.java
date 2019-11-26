/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 26 - 2019
 * Author: rgsw
 */

package modernity.common.particle;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import modernity.api.util.ESoulLightColor;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;

public class SoulLightParticleData implements IParticleData {
    public static final IDeserializer<SoulLightParticleData> DESERIALIZER = new IDeserializer<SoulLightParticleData>() {

        private final SimpleCommandExceptionType colorError = new SimpleCommandExceptionType( new LiteralMessage( "Expected soul light color" ) );
        private final SimpleCommandExceptionType fadeError = new SimpleCommandExceptionType( new LiteralMessage( "Expected 'false' or 'true'" ) );

        @Override
        public SoulLightParticleData deserialize( ParticleType<SoulLightParticleData> type, StringReader reader ) throws CommandSyntaxException {
            reader.expect( ' ' );

            int start = reader.getCursor();
            ESoulLightColor color = null;
            for( ESoulLightColor c : ESoulLightColor.values() ) {
                String name = c.name().toLowerCase();
                int len = name.length();
                if( reader.canRead( len ) ) {
                    int end = start + len;
                    String read = reader.getString().substring( start, end );
                    if( read.equals( name ) ) {
                        reader.setCursor( end );
                        color = c;
                        break;
                    }
                }
            }

            if( color == null ) {
                reader.setCursor( start );
                throw colorError.createWithContext( reader );
            }

            reader.expect( ' ' );

            boolean f = false;
            boolean fade = false;
            do {
                String name = f + "";
                int len = name.length();
                if( reader.canRead( len ) ) {
                    int end = start + len;
                    String read = reader.getString().substring( start, end );
                    if( read.equals( name ) ) {
                        reader.setCursor( end );
                        fade = f;
                        break;
                    }
                }
            } while( ! f && ( f = true ) );

            if( fade != f ) {
                reader.setCursor( start );
                throw fadeError.createWithContext( reader );
            }
            return new SoulLightParticleData( type, color, fade );
        }

        @Override
        public SoulLightParticleData read( ParticleType<SoulLightParticleData> type, PacketBuffer buffer ) {
            return new SoulLightParticleData(
                type,
                ESoulLightColor.fromOrdinal( buffer.readByte() ),
                buffer.readBoolean()
            );
        }
    };

    private final ParticleType<?> type;
    private final ESoulLightColor color;
    private final boolean fade;

    public SoulLightParticleData( ParticleType<?> type, ESoulLightColor color, boolean fade ) {
        this.type = type;
        this.color = color == null ? ESoulLightColor.DEFAULT : color;
        this.fade = fade;
    }

    @Override
    public ParticleType<?> getType() {
        return type;
    }

    public ESoulLightColor getColor() {
        return color;
    }

    public boolean fades() {
        return fade;
    }

    @Override
    public void write( PacketBuffer buffer ) {
        buffer.writeByte( color.ordinal() );
        buffer.writeBoolean( fade );
    }

    @Override
    public String getParameters() {
        return " " + color.name().toLowerCase() + " " + fade;
    }
}
