/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 24 - 2020
 * Author: rgsw
 */

package modernity.client.sound.system;

import net.minecraft.client.audio.IAudioStream;

import javax.sound.sampled.AudioFormat;
import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;

public class AudioStream implements Closeable {
    private IAudioStream wrapped;

    public AudioStream( IAudioStream wrapped ) {
        this.wrapped = wrapped;
    }

    public AudioStream() {
    }

    public void setWrapped( IAudioStream wrapped ) {
        this.wrapped = wrapped;
    }

    public AudioFormat getFormat() {
        return wrapped.func_216454_a();
    }

    public ByteBuffer getFullBuffer() throws IOException {
        return wrapped.func_216453_b();
    }

    public ByteBuffer stream( int size ) throws IOException {
        return wrapped.func_216455_a( size );
    }

    @Override
    public void close() throws IOException {
        wrapped.close();
    }
}
