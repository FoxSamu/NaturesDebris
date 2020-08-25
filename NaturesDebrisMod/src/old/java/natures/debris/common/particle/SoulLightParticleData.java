/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.particle;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import natures.debris.common.blockold.misc.SoulLightColor;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;

public class SoulLightParticleData implements IParticleData {
    public static final IDeserializer<SoulLightParticleData> DESERIALIZER = new IDeserializer<SoulLightParticleData>() {

        private final SimpleCommandExceptionType colorError = new SimpleCommandExceptionType(new LiteralMessage("Expected soul light color"));
        private final SimpleCommandExceptionType fadeError = new SimpleCommandExceptionType(new LiteralMessage("Expected 'false' or 'true'"));

        @Override
        public SoulLightParticleData deserialize(ParticleType<SoulLightParticleData> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');

            int start = reader.getCursor();
            SoulLightColor color = null;
            for (SoulLightColor c : SoulLightColor.values()) {
                String name = c.name().toLowerCase();
                int len = name.length();
                if (reader.canRead(len)) {
                    int end = start + len;
                    String read = reader.getString().substring(start, end);
                    if (read.equals(name)) {
                        reader.setCursor(end);
                        color = c;
                        break;
                    }
                }
            }

            if (color == null) {
                reader.setCursor(start);
                throw colorError.createWithContext(reader);
            }

            reader.expect(' ');

            Boolean fade = null;
            for (int i = 0; i < 2; i++) {
                boolean f = i == 1;
                String name = f + "";
                int len = name.length();
                if (reader.canRead(len)) {
                    int end = start + len;
                    String read = reader.getString().substring(start, end);
                    if (read.equals(name)) {
                        reader.setCursor(end);
                        fade = f;
                        break;
                    }
                }
            }

            if (fade == null) {
                reader.setCursor(start);
                throw fadeError.createWithContext(reader);
            }

            return new SoulLightParticleData(type, color, fade);
        }

        @Override
        public SoulLightParticleData read(ParticleType<SoulLightParticleData> type, PacketBuffer buffer) {
            return new SoulLightParticleData(
                type,
                SoulLightColor.fromOrdinal(buffer.readByte()),
                buffer.readBoolean()
            );
        }
    };

    private final ParticleType<?> type;
    private final SoulLightColor color;
    private final boolean fade;

    public SoulLightParticleData(ParticleType<?> type, SoulLightColor color, boolean fade) {
        this.type = type;
        this.color = color == null ? SoulLightColor.DEFAULT : color;
        this.fade = fade;
    }

    @Override
    public ParticleType<?> getType() {
        return type;
    }

    public SoulLightColor getColor() {
        return color;
    }

    public boolean fades() {
        return fade;
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeByte(color.ordinal());
        buffer.writeBoolean(fade);
    }

    @Override
    public String getParameters() {
        return " " + color.name().toLowerCase() + " " + fade;
    }
}
