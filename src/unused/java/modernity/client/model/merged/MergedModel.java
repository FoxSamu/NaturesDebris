/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 19 - 2020
 * Author: rgsw
 */

package modernity.client.model.merged;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import modernity.client.model.empty.EmptyModel;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.texture.ISprite;
import net.minecraft.client.renderer.texture.MissingTextureSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Function;

public class MergedModel implements IUnbakedModel {
    private final ArrayList<Entry> entries = new ArrayList<>();
    private final HashMap<String, String> referenceableTextures = new HashMap<>();

    @Override
    public Collection<ResourceLocation> getDependencies() {
        return Collections.emptyList();
    }

    @Override
    public Collection<ResourceLocation> getTextures( Function<ResourceLocation, IUnbakedModel> modelGetter, Set<String> missingTextureErrors ) {
        Set<ResourceLocation> tex = new HashSet<>();
        for( Entry entry : entries ) {
            entry.model = ModelLoaderRegistry.getModelOrMissing( entry.loc );
            HashMap<String, String> remapKeys = new HashMap<>();
            for( Map.Entry<String, String> e : entry.textures.entrySet() ) {
                if( e.getValue().startsWith( "@" ) ) {
                    remapKeys.put( e.getKey(), referenceableTextures.get( e.getValue().substring( 1 ) ) );
                }
            }
            for( Map.Entry<String, String> e : remapKeys.entrySet() ) {
                entry.textures.put( e.getKey(), e.getValue() );
            }
            entry.process();
            Collection<ResourceLocation> textures = entry.model.getTextures( modelGetter, missingTextureErrors );
            tex.addAll( textures );
        }
        return tex;
    }

    @Override
    public IUnbakedModel process( ImmutableMap<String, String> customData ) {
        MergedModel out = new MergedModel();
        String models = customData.get( "models" );
        JsonElement element = new JsonParser().parse( models );

        if( element.isJsonArray() ) {
            for( JsonElement e : element.getAsJsonArray() ) {
                Entry entry = parseEntry( e );
                out.entries.add( entry );
            }
        }

        if( element.isJsonPrimitive() || element.isJsonObject() ) {
            Entry entry = parseEntry( element );
            out.entries.add( entry );
        }
        out.referenceableTextures.putAll( referenceableTextures );
        return out;
    }

    @Override
    public IUnbakedModel retexture( ImmutableMap<String, String> textures ) {
        MergedModel out = new MergedModel();
        out.referenceableTextures.putAll( textures );
        out.entries.addAll( entries );
        return out;
    }

    private Entry parseEntry( JsonElement json ) {
        if( json.isJsonPrimitive() ) {
            return new Entry( new ResourceLocation( json.getAsString() ) );
        } else if( json.isJsonObject() ) {
            JsonObject obj = json.getAsJsonObject();
            if( ! obj.has( "model" ) || ! obj.get( "model" ).isJsonPrimitive() ) {
                throw new JsonSyntaxException( "Entry object must contain field model!" );
            }

            ResourceLocation model = new ResourceLocation( obj.getAsJsonPrimitive( "model" ).getAsString() );
            Entry entry = new Entry( model );

            if( obj.has( "custom" ) && obj.get( "custom" ).isJsonObject() ) {
                JsonObject custom = obj.getAsJsonObject( "custom" );
                for( Map.Entry<String, JsonElement> e : custom.entrySet() ) {
                    entry.custom.put( e.getKey(), e.getValue().toString() );
                }
            }

            if( obj.has( "textures" ) && obj.get( "textures" ).isJsonObject() ) {
                JsonObject textures = obj.getAsJsonObject( "textures" );
                for( Map.Entry<String, JsonElement> e : textures.entrySet() ) {
                    if( e.getValue().isJsonPrimitive() && e.getValue().getAsJsonPrimitive().isString() ) {
                        entry.textures.put( e.getKey(), e.getValue().getAsString() );
                    }
                }
            }

            if( obj.has( "gui3d" ) && obj.get( "gui3d" ).isJsonPrimitive() ) {
                entry.gui3d = obj.getAsJsonPrimitive( "gui3d" ).isBoolean();
            }

            if( obj.has( "smooth_lighting" ) && obj.get( "smooth_lighting" ).isJsonPrimitive() ) {
                entry.smoothLighting = obj.getAsJsonPrimitive( "smooth_lighting" ).isBoolean();
            }

            return entry;
        } else {
            throw new JsonSyntaxException( "Entry must be object or string!" );
        }
    }

    @Nullable
    @Override
    public IBakedModel bake( ModelBakery bakery, Function<ResourceLocation, TextureAtlasSprite> spriteGetter, ISprite sprite, VertexFormat format ) {
        if( entries.isEmpty() ) {
            return new EmptyModel.Baked( spriteGetter.apply( MissingTextureSprite.getLocation() ) );
        }
        IBakedModel[] parts = new IBakedModel[ entries.size() ];
        int i = 0;
        for( Entry e : entries ) {
            parts[ i ] = e.model.bake( bakery, spriteGetter, sprite, format );
            i++;
        }
        return new Baked( parts );
    }

    static class Entry {
        final ResourceLocation loc;

        Boolean gui3d;
        Boolean smoothLighting;
        final Map<String, String> textures = new HashMap<>();
        final Map<String, String> custom = new HashMap<>();

        private IUnbakedModel model;

        Entry( ResourceLocation loc ) {
            this.loc = loc;
        }

        void process() {
            model = model.process( ImmutableMap.copyOf( custom ) );
            model = model.retexture( ImmutableMap.copyOf( textures ) );
            if( gui3d != null ) model.gui3d( gui3d );
            if( smoothLighting != null ) model.smoothLighting( smoothLighting );
        }
    }

    static class Baked implements IBakedModel {
        private final IBakedModel[] parts;
        private final ItemOverrideList overrides = new ItemOverrideList() {
            @Override
            public IBakedModel getModelWithOverrides( IBakedModel originalModel, ItemStack stack, @Nullable World world, @Nullable LivingEntity entity ) {
                if( originalModel != Baked.this ) {
                    return originalModel;
                }
                boolean dirty = false;
                ArrayList<IBakedModel> builder = new ArrayList<>();
                for( IBakedModel entry : parts ) {
                    IBakedModel newPart = entry.getOverrides().getModelWithOverrides( entry, stack, world, entity );
                    builder.add( newPart );
                    if( entry != newPart ) {
                        dirty = true;
                    }
                }
                if( dirty ) {
                    return new Baked( builder.toArray( new IBakedModel[ 0 ] ) );
                }
                return Baked.this;
            }
        };

        Baked( IBakedModel[] parts ) {
            this.parts = parts;
        }

        @Override
        public List<BakedQuad> getQuads( @Nullable BlockState state, @Nullable Direction side, Random rand ) {
            return getQuads( state, side, rand, EmptyModelData.INSTANCE );
        }

        @Nonnull
        @Override
        public List<BakedQuad> getQuads( @Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData ) {
            ArrayList<BakedQuad> quads = new ArrayList<>();
            for( IBakedModel model : parts ) {
                quads.addAll( model.getQuads( state, side, rand, extraData ) );
            }
            return quads;
        }

        @Override
        public boolean isAmbientOcclusion() {
            for( IBakedModel model : parts ) {
                if( model.isAmbientOcclusion() ) return true;
            }
            return false;
        }

        @Override
        public boolean isGui3d() {
            for( IBakedModel model : parts ) {
                if( model.isAmbientOcclusion() ) return true;
            }
            return false;
        }

        @Override
        public boolean isBuiltInRenderer() {
            return true;
        }

        @Override
        @SuppressWarnings( "deprecation" )
        public TextureAtlasSprite getParticleTexture() {
            return parts[ 0 ].getParticleTexture();
        }

        @Override
        public TextureAtlasSprite getParticleTexture( @Nonnull IModelData data ) {
            return parts[ 0 ].getParticleTexture( data );
        }

        @Nonnull
        @Override
        public IModelData getModelData( @Nonnull IEnviromentBlockReader world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull IModelData tileData ) {
            for( IBakedModel model : parts ) {
                tileData = model.getModelData( world, pos, state, tileData );
            }
            return tileData;
        }

        @Override
        public ItemOverrideList getOverrides() {
            return overrides;
        }
    }
}
