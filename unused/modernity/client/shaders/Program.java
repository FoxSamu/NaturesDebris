/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 25 - 2019
 * Author: rgsw
 */

package modernity.client.shaders;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

public class Program {
    private int programID;
    private int vertexID;
    private int fragmentID;

    private boolean loaded;

    private final HashMap<String, String> preCompileValues = new HashMap<>();

    private final ResourceLocation vertexLocation;
    private final ResourceLocation fragmentLocation;

    public Program( ResourceLocation vertexLocation, ResourceLocation fragmentLocation ) {
        this.vertexLocation = vertexLocation;
        this.fragmentLocation = fragmentLocation;
    }

    public Program( ResourceLocation location ) {
        this.vertexLocation = new ResourceLocation( location.getNamespace(), location.getPath() + ".vsh" );
        this.fragmentLocation = new ResourceLocation( location.getNamespace(), location.getPath() + ".fsh" );
    }

    public void addPreCompileValue( String key, Object value ) {
        preCompileValues.put( key, value + "" );
    }

    public int getProgramID() {
        return programID;
    }

    public int getVertexID() {
        return vertexID;
    }

    public int getFragmentID() {
        return fragmentID;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public ResourceLocation getVertexLocation() {
        return vertexLocation;
    }

    public ResourceLocation getFragmentLocation() {
        return fragmentLocation;
    }

    private String loadShaderSrc( ResourceLocation loc ) {
        IResourceManager manager = Minecraft.getInstance().getResourceManager();
        try {
            IResource resource = manager.getResource( loc );
            InputStream in = resource.getInputStream();
            InputStreamReader reader = new InputStreamReader( in );
            char[] cbuf = new char[ 512 ];
            StringBuilder builder = new StringBuilder();

            builder.append( "#version 120\n" );

            for( Map.Entry<String, String> entry : preCompileValues.entrySet() ) {
                builder.append( "#define " )
                       .append( entry.getKey() )
                       .append( " " )
                       .append( entry.getValue() )
                       .append( "\n" );
            }

            while( true ) {
                int len = reader.read( cbuf );
                if( len == - 1 ) break;
                builder.append( cbuf, 0, len );
            }
            return builder.toString();
        } catch( IOException e ) {
            throw new ShaderException( "Failed to load shader source (at '" + loc + "')", e );
        }
    }

    private int loadShader( int type ) {
        ResourceLocation res;
        String typeName;
        if( type == GL_VERTEX_SHADER ) {
            res = vertexLocation;
            typeName = "Vertex";
        } else {
            res = fragmentLocation;
            typeName = "Fragment";
        }
        String src = loadShaderSrc( res );
        int id = glCreateShader( type );
        glShaderSource( id, src );
        glCompileShader( id );
        int success = glGetShaderi( id, GL_COMPILE_STATUS );
        if( success != 1 ) {
            String log = glGetShaderInfoLog( id );
            throw new ShaderException( typeName + " shader compilation failed: " + log );
        }
        return id;
    }

    public void load() {
        if( loaded ) return;
        vertexID = loadShader( GL_VERTEX_SHADER );
        fragmentID = loadShader( GL_FRAGMENT_SHADER );

        programID = glCreateProgram();
        glAttachShader( programID, vertexID );
        glAttachShader( programID, fragmentID );
        glLinkProgram( programID );
        int success = glGetProgrami( programID, GL_LINK_STATUS );
        if( success != 1 ) {
            String log = glGetProgramInfoLog( programID );
            throw new ShaderException( "Program linkage failed: " + log );
        }

        loaded = true;
    }

    public void cleanup() {
        if( ! loaded ) return;
        glDeleteProgram( programID );
        glDeleteShader( vertexID );
        glDeleteShader( fragmentID );
        loaded = false;
    }

    public void use() {
        if( ! loaded ) {
            throw new ShaderException( "Cannot use unloaded program" );
        }
        glUseProgram( programID );
    }

    public Uniform uniform( String name ) {
        if( ! loaded ) throw new ShaderException( "Cannot get uniform of unloaded program" );
        int uniform = glGetUniformLocation( programID, name );
        return new Uniform( uniform, name, this );
    }

    public static void useNone() {
        glUseProgram( 0 );
    }

    public static class Uniform {
        private final int uniform;
        private final String name;
        private final Program program;

        private Uniform( int uniform, String name, Program program ) {
            this.uniform = uniform;
            this.name = name;
            this.program = program;
        }

        public String getName() {
            return name;
        }

        public int getUniform() {
            return uniform;
        }

        public Program getProgram() {
            return program;
        }

        public void set( int x ) {
            glUniform1i( uniform, x );
        }

        public void set( int x, int y ) {
            glUniform2i( uniform, x, y );
        }

        public void set( int x, int y, int z ) {
            glUniform3i( uniform, x, y, z );
        }

        public void set( int x, int y, int z, int w ) {
            glUniform4i( uniform, x, y, z, w );
        }

        public void set( float x ) {
            glUniform1f( uniform, x );
        }

        public void set( float x, float y ) {
            glUniform2f( uniform, x, y );
        }

        public void set( float x, float y, float z ) {
            glUniform3f( uniform, x, y, z );
        }

        public void set( float x, float y, float z, float w ) {
            glUniform4f( uniform, x, y, z, w );
        }

        public void set( double x ) {
            glUniform1f( uniform, (float) x );
        }

        public void set( double x, double y ) {
            glUniform2f( uniform, (float) x, (float) y );
        }

        public void set( double x, double y, double z ) {
            glUniform3f( uniform, (float) x, (float) y, (float) z );
        }

        public void set( double x, double y, double z, double w ) {
            glUniform4f( uniform, (float) x, (float) y, (float) z, (float) w );
        }

        public void set( boolean x ) {
            set( x ? 1 : 0 );
        }

        public void set( boolean x, boolean y ) {
            set( x ? 1 : 0, y ? 1 : 0 );
        }

        public void set( boolean x, boolean y, boolean z ) {
            set( x ? 1 : 0, y ? 1 : 0, z ? 1 : 0 );
        }

        public void set( boolean x, boolean y, boolean z, boolean w ) {
            set( x ? 1 : 0, y ? 1 : 0, z ? 1 : 0, w ? 1 : 0 );
        }

        public void set( int[] arr ) {
            switch( arr.length ) {
                case 1:
                    glUniform1iv( uniform, arr );
                    break;
                case 2:
                    glUniform2iv( uniform, arr );
                    break;
                case 3:
                    glUniform3iv( uniform, arr );
                    break;
                case 4:
                    glUniform4iv( uniform, arr );
                    break;
                default:
                    throw new IllegalArgumentException( "Invalid array length" );
            }
        }

        public void set( float[] arr ) {
            switch( arr.length ) {
                case 1:
                    glUniform1fv( uniform, arr );
                    break;
                case 2:
                    glUniform2fv( uniform, arr );
                    break;
                case 3:
                    glUniform3fv( uniform, arr );
                    break;
                case 4:
                    glUniform4fv( uniform, arr );
                    break;
                default:
                    throw new IllegalArgumentException( "Invalid array length" );
            }
        }

        public void setMatrix( float[] matrix, boolean transpose ) {
            switch( matrix.length ) {
                case 4:
                    glUniformMatrix2fv( uniform, transpose, matrix );
                    break;
                case 9:
                    glUniformMatrix3fv( uniform, transpose, matrix );
                    break;
                case 16:
                    glUniformMatrix4fv( uniform, transpose, matrix );
                    break;
                default:
                    throw new IllegalArgumentException( "Invalid marix array length (must be 4, 9 or 16)" );
            }
        }

        public void setMatrix( float[] matrix ) {
            setMatrix( matrix, true );
        }
    }
}
