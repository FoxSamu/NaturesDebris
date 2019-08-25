/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 8 - 26 - 2019
 */

package modernity.api.biome;

import com.google.gson.*;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReaderBase;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import net.rgsw.noise.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import modernity.api.util.ColorUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@OnlyIn( Dist.CLIENT )
public class BiomeColoringProfile {
    private static final Logger LOGGER = LogManager.getLogger();

    // Thread-local because chunk rendering is multithreaded...
    private final ThreadLocal<BlockPos> cachedPos = new ThreadLocal<>();
    private final ThreadLocal<Long> cachedSeed = new ThreadLocal<>();
    private final ThreadLocal<Integer> cachedColor = new ThreadLocal<>();

    private final HashMap<Biome, IColorProvider> biomeColors = new HashMap<>();

    public IColorProvider getColorProviderFor( Biome biome ) {
        return biomeColors.computeIfAbsent( biome, b -> createErrorProvider() );
    }

    public int getColor( IWorldReaderBase world, BlockPos pos ) {
        long seed = 0;
        if( Minecraft.getInstance().world != null ) {
            seed = Minecraft.getInstance().world.getSeed();
        }

        if( cachedSeed.get() != null && seed == cachedSeed.get() && pos.equals( cachedPos.get() ) ) {
            return cachedColor.get();
        }

        int color = getColor( world, pos, seed );
        cachedColor.set( color );
        cachedPos.set( pos.toImmutable() );
        cachedSeed.set( seed );

        return color;
    }

    private int getColor( IWorldReaderBase world, BlockPos pos, long seed ) {
        BlockPos.MutableBlockPos rpos = new BlockPos.MutableBlockPos();
        int radius = Minecraft.getInstance().gameSettings.biomeBlendRadius;
        int tot = 0;

        int r = 0;
        int g = 0;
        int b = 0;

        for( int x = - radius; x <= radius; x++ ) {
            for( int z = - radius; z <= radius; z++ ) {
                rpos.setPos( pos.getX() + x, pos.getY(), pos.getZ() + z );

                Biome biome = world.getBiome( rpos );

                int col = getColorFor( biome, pos, seed );

                r += col >>> 16 & 0xff;
                g += col >>> 8 & 0xff;
                b += col & 0xff;

                tot++;
            }
        }

        if( tot == 0 ) tot = 1;
        r /= tot;
        g /= tot;
        b /= tot;

        return ColorUtil.rgb( r, g, b );
    }

    public int getColorFor( Biome biome, BlockPos pos, long seed ) {
        return getColorProviderFor( biome ).getColor( pos, seed );
    }

    protected void applyLayer( Map<Biome, IColorProvider> layer ) {
        biomeColors.putAll( layer );
    }

    public static BiomeColoringProfile create( IResourceManager resManager, ResourceLocation loc, String profileName ) throws IOException {
        List<IResource> list = resManager.getAllResources( loc );
        BiomeColoringProfile profile = new BiomeColoringProfile();
        for( IResource res : list ) {
            InputStream stream = res.getInputStream();
            JsonParser parser = new JsonParser(); try {
                JsonElement element = parser.parse( new InputStreamReader( stream ) );
                String packName = res.getPackName();
                if( ! element.isJsonObject() ) {
                    LOGGER.warn( "Biome color profile must have an object as root" );
                    continue;
                }
                Map<Biome, IColorProvider> layer = parseProfileLayer( element.getAsJsonObject(), profileName, packName );
                profile.applyLayer( layer );
            } catch( JsonParseException exc ) {
                LOGGER.warn( "Biome color profile of pack '{}' has invalid syntax: {}", res.getPackName(), exc.getMessage() );
            }
        }
        return profile;
    }

    public static Map<Biome, IColorProvider> parseProfileLayer( JsonObject object, String profileName, String packName ) {
        HashMap<Biome, IColorProvider> biomeColors = new HashMap<>();
        ArrayList<ColorFormatException> errors = new ArrayList<>();
        for( Map.Entry<String, JsonElement> entry : object.entrySet() ) {
            Biome biome = getBiomeForName( entry.getKey() );
            if( biome == null ) {
                errors.add( new ColorFormatException( "No such biome with id '" + entry.getKey() + "'" ).addParent( entry.getKey() ) );
                continue;
            }
            IColorProvider provider;
            try {
                provider = parseColorProvider( entry.getValue(), entry.getKey() );
            } catch( ColorFormatException exc ) {
                provider = createErrorProvider();
                errors.add( exc );
            }
            biomeColors.put( biome, provider );
        }
        if( ! errors.isEmpty() ) {
            LOGGER.warn( "Biome profile '{}' in pack '{}' has {} problems", profileName, packName, errors.size() );
            for( ColorFormatException exc : errors ) {
                LOGGER.warn( " - " + exc.getMessage() );
            }
        }
        return biomeColors;
    }

    private static Biome getBiomeForName( String name ) {
        try {
            ResourceLocation loc = new ResourceLocation( name );
            return ForgeRegistries.BIOMES.getValue( loc );
        } catch( Throwable e ) {
            return null;
        }
    }

    public static IColorProvider createErrorProvider() {
        return new CheckerboardColor( new SolidColor( 0 ), new SolidColor( 0xff00ff ), 1 );
    }

    public static IColorProvider parseColorProvider( JsonElement element ) {
        try {
            return parseColorProvider( element, "format" );
        } catch( ColorFormatException exc ) {
            LOGGER.warn( "Error parsing coloring format:" );
            LOGGER.warn( exc.getMessage() );
            return createErrorProvider();
        }
    }

    private static IColorProvider parseColorProvider( JsonElement element, String name ) throws ColorFormatException {
        try {
            if( element.isJsonNull() ) {
                throw new ColorFormatException( "Null is not a color" );
            } else if( element.isJsonPrimitive() ) {
                if( element.getAsJsonPrimitive().isString() ) {
                    Integer col = ColorUtil.parseHexString( element.getAsJsonPrimitive().getAsString() );
                    if( col == null ) {
                        throw new ColorFormatException( "Invalid hexadecimal string ('" + element.getAsJsonPrimitive().getAsString() + "')" );
                    }
                    return new SolidColor( col );
                } else if( element.getAsJsonPrimitive().isNumber() ) {
                    int color = element.getAsJsonPrimitive().getAsInt();
                    return new SolidColor( color );
                } else if( element.getAsJsonPrimitive().isBoolean() ) {
                    if( element.getAsJsonPrimitive().getAsBoolean() ) {
                        return new SolidColor( 0xffffff );
                    } else {
                        return new SolidColor( 0 );
                    }
                } else {
                    throw new ColorFormatException( "String, Number or Boolean expected as primitive color..." );
                }
            } else if( element.isJsonArray() ) {
                JsonArray array = element.getAsJsonArray();
                return parseRGBArray( array );
            } else {
                JsonObject object = element.getAsJsonObject();
                if( object.has( "rgb" ) ) {
                    JsonElement rgb = object.get( "rgb" );
                    if( ! rgb.isJsonArray() ) {
                        throw new ColorFormatException( "'rgb' requires an array" );
                    } else {
                        return parseRGBArray( rgb.getAsJsonArray() );
                    }
                } else if( object.has( "greyscale" ) ) {
                    JsonElement grayscale = object.get( "greyscale" );
                    if( ! grayscale.isJsonPrimitive() || ! grayscale.getAsJsonPrimitive().isNumber() ) {
                        throw new ColorFormatException( "'greyscale' requires a number" );
                    } else {
                        return new SolidColor( ColorUtil.fromGrayscale( grayscale.getAsDouble() ) );
                    }
                } else if( object.has( "grayscale" ) ) {
                    JsonElement grayscale = object.get( "grayscale" );
                    if( ! grayscale.isJsonPrimitive() || ! grayscale.getAsJsonPrimitive().isNumber() ) {
                        throw new ColorFormatException( "'grayscale' requires a number" );
                    } else {
                        return new SolidColor( ColorUtil.fromGrayscale( grayscale.getAsDouble() ) );
                    }
                } else if( object.has( "r" ) && object.has( "g" ) && object.has( "b" ) ) {
                    JsonElement r = object.get( "r" );
                    JsonElement g = object.get( "g" );
                    JsonElement b = object.get( "b" );
                    if( ! r.isJsonPrimitive() || ! r.getAsJsonPrimitive().isNumber() ) {
                        throw new ColorFormatException( "'r' must be a number" );
                    } else if( ! g.isJsonPrimitive() || ! g.getAsJsonPrimitive().isNumber() ) {
                        throw new ColorFormatException( "'g' must be a number" );
                    } else if( ! g.isJsonPrimitive() || ! g.getAsJsonPrimitive().isNumber() ) {
                        throw new ColorFormatException( "'b' must be a number" );
                    } else {
                        return new SolidColor( ColorUtil.rgb( r.getAsDouble(), g.getAsDouble(), b.getAsDouble() ) );
                    }
                } else if( object.has( "checkerboard" ) ) {
                    JsonElement checkerboard = object.get( "checkerboard" );
                    if( ! checkerboard.isJsonObject() ) {
                        throw new ColorFormatException( "'checkerboard' requires an object" );
                    } else {
                        return parseCheckerboard( checkerboard.getAsJsonObject() );
                    }
                } else if( object.has( "pickrandom2d" ) ) {
                    JsonElement pickrandom = object.get( "pickrandom2d" );
                    if( ! pickrandom.isJsonObject() ) {
                        throw new ColorFormatException( "'pickrandom2d' requires an object" );
                    } else {
                        return parsePickRandom2D( pickrandom.getAsJsonObject() );
                    }
                } else if( object.has( "pickrandom3d" ) ) {
                    JsonElement pickrandom = object.get( "pickrandom3d" );
                    if( ! pickrandom.isJsonObject() ) {
                        throw new ColorFormatException( "'pickrandom3d' requires an object" );
                    } else {
                        return parsePickRandom3D( pickrandom.getAsJsonObject() );
                    }
                } else if( object.has( "random2d" ) ) {
                    JsonElement random = object.get( "random2d" );
                    if( ! random.isJsonObject() ) {
                        throw new ColorFormatException( "'random2d' requires an object" );
                    } else {
                        return parseRandom2D( random.getAsJsonObject() );
                    }
                } else if( object.has( "random3d" ) ) {
                    JsonElement random = object.get( "random3d" );
                    if( ! random.isJsonObject() ) {
                        throw new ColorFormatException( "'random3d' requires an object" );
                    } else {
                        return parseRandom3D( random.getAsJsonObject() );
                    }
                } else if( object.has( "perlin2d" ) ) {
                    JsonElement random = object.get( "perlin2d" );
                    if( ! random.isJsonObject() ) {
                        throw new ColorFormatException( "'perlin2d' requires an object" );
                    } else {
                        return parsePerlin2D( random.getAsJsonObject() );
                    }
                } else if( object.has( "perlin3d" ) ) {
                    JsonElement random = object.get( "perlin3d" );
                    if( ! random.isJsonObject() ) {
                        throw new ColorFormatException( "'perlin3d' requires an object" );
                    } else {
                        return parsePerlin3D( random.getAsJsonObject() );
                    }
                } else if( object.has( "opensimplex2d" ) ) {
                    JsonElement random = object.get( "opensimplex2d" );
                    if( ! random.isJsonObject() ) {
                        throw new ColorFormatException( "'opensimplex2d' requires an object" );
                    } else {
                        return parseOpenSimplex2D( random.getAsJsonObject() );
                    }
                } else if( object.has( "opensimplex3d" ) ) {
                    JsonElement random = object.get( "opensimplex3d" );
                    if( ! random.isJsonObject() ) {
                        throw new ColorFormatException( "'opensimplex3d' requires an object" );
                    } else {
                        return parseOpenSimplex3D( random.getAsJsonObject() );
                    }
                } else if( object.has( "fracperlin2d" ) ) {
                    JsonElement random = object.get( "fracperlin2d" );
                    if( ! random.isJsonObject() ) {
                        throw new ColorFormatException( "'fracperlin2d' requires an object" );
                    } else {
                        return parseFractalPerlin2D( random.getAsJsonObject() );
                    }
                } else if( object.has( "fracperlin3d" ) ) {
                    JsonElement random = object.get( "fracperlin3d" );
                    if( ! random.isJsonObject() ) {
                        throw new ColorFormatException( "'fracperlin3d' requires an object" );
                    } else {
                        return parseFractalPerlin3D( random.getAsJsonObject() );
                    }
                } else if( object.has( "fracopensimplex2d" ) ) {
                    JsonElement random = object.get( "fracopensimplex2d" );
                    if( ! random.isJsonObject() ) {
                        throw new ColorFormatException( "'fracopensimplex2d' requires an object" );
                    } else {
                        return parseFractalOpenSimplex2D( random.getAsJsonObject() );
                    }
                } else if( object.has( "fracopensimplex3d" ) ) {
                    JsonElement random = object.get( "fracopensimplex3d" );
                    if( ! random.isJsonObject() ) {
                        throw new ColorFormatException( "'fracopensimplex3d' requires an object" );
                    } else {
                        return parseFractalOpenSimplex3D( random.getAsJsonObject() );
                    }
                }
                if( object.size() == 0 ) {
                    throw new ColorFormatException( "Format object must have at least one member" );
                } else {
                    String key = object.entrySet().iterator().next().getKey();
                    throw new ColorFormatException( "'" + key + "' is not a valid format type" );
                }
            }
        } catch( ColorFormatException exc ) {
            throw exc.addParent( name );
        }
    }

    private static IColorProvider parseFractalOpenSimplex2D( JsonObject object ) throws ColorFormatException {
        try {
            if( ! object.has( "a" ) )
                throw new ColorFormatException( "Missing required 'a'" );
            if( ! object.has( "b" ) )
                throw new ColorFormatException( "Missing required 'b'" );
            if( ! object.has( "octaves" ) )
                throw new ColorFormatException( "Missing required 'octaves' number" );

            JsonElement octavesEl = object.get( "octaves" );
            if( ! octavesEl.isJsonPrimitive() || ! octavesEl.getAsJsonPrimitive().isNumber() )
                throw new ColorFormatException( "'octaves' must be a number" );

            int octaves = octavesEl.getAsInt();

            IColorProvider a = parseColorProvider( object.get( "a" ), "a" );
            IColorProvider b = parseColorProvider( object.get( "b" ), "b" );

            double sizeX = 1;
            double sizeZ = 1;

            if( object.has( "size" ) ) {
                JsonElement sizeEl = object.get( "size" );
                if( sizeEl.isJsonPrimitive() && sizeEl.getAsJsonPrimitive().isNumber() ) {
                    sizeX = sizeEl.getAsDouble();
                    sizeZ = sizeEl.getAsDouble();
                } else if( sizeEl.isJsonArray() && sizeEl.getAsJsonArray().size() >= 2 ) {
                    JsonElement sizeXEl = sizeEl.getAsJsonArray().get( 0 );
                    JsonElement sizeZEl = sizeEl.getAsJsonArray().get( 1 );
                    if( sizeXEl.isJsonPrimitive() && sizeXEl.getAsJsonPrimitive().isNumber() && sizeZEl.isJsonPrimitive() && sizeZEl.getAsJsonPrimitive().isNumber() ) {
                        sizeX = sizeXEl.getAsDouble();
                        sizeZ = sizeZEl.getAsDouble();
                    }
                }
            }

            boolean customseed = false;
            long seed = 0;

            if( object.has( "seed" ) ) {
                JsonElement seedEl = object.get( "seed" );
                if( seedEl.isJsonPrimitive() && seedEl.getAsJsonPrimitive().isNumber() ) {
                    customseed = true;
                    seed = seedEl.getAsLong();
                }
            }
            return new FractalOpenSimplex2DColor( a, b, customseed, seed, octaves, sizeX, sizeZ );
        } catch( ColorFormatException exc ) {
            throw exc.addParent( "fracopensimplex2d" );
        }
    }

    private static IColorProvider parseFractalOpenSimplex3D( JsonObject object ) throws ColorFormatException {
        try {
            if( ! object.has( "a" ) )
                throw new ColorFormatException( "Missing required 'a'" );
            if( ! object.has( "b" ) )
                throw new ColorFormatException( "Missing required 'b'" );
            if( ! object.has( "octaves" ) )
                throw new ColorFormatException( "Missing required 'octaves' number" );

            JsonElement octavesEl = object.get( "octaves" );
            if( ! octavesEl.isJsonPrimitive() || ! octavesEl.getAsJsonPrimitive().isNumber() )
                throw new ColorFormatException( "'octaves' must be a number" );

            int octaves = octavesEl.getAsInt();

            IColorProvider a = parseColorProvider( object.get( "a" ), "a" );
            IColorProvider b = parseColorProvider( object.get( "b" ), "b" );

            double sizeX = 1;
            double sizeY = 1;
            double sizeZ = 1;

            if( object.has( "size" ) ) {
                JsonElement sizeEl = object.get( "size" );
                if( sizeEl.isJsonPrimitive() && sizeEl.getAsJsonPrimitive().isNumber() ) {
                    sizeX = sizeEl.getAsDouble();
                    sizeY = sizeEl.getAsDouble();
                    sizeZ = sizeEl.getAsDouble();
                } else if( sizeEl.isJsonArray() && sizeEl.getAsJsonArray().size() >= 3 ) {
                    JsonElement sizeXEl = sizeEl.getAsJsonArray().get( 0 );
                    JsonElement sizeYEl = sizeEl.getAsJsonArray().get( 1 );
                    JsonElement sizeZEl = sizeEl.getAsJsonArray().get( 2 );
                    if( sizeXEl.isJsonPrimitive() && sizeXEl.getAsJsonPrimitive().isNumber() && sizeYEl.isJsonPrimitive() && sizeYEl.getAsJsonPrimitive().isNumber() && sizeZEl.isJsonPrimitive() && sizeZEl.getAsJsonPrimitive().isNumber() ) {
                        sizeX = sizeXEl.getAsDouble();
                        sizeY = sizeYEl.getAsDouble();
                        sizeZ = sizeZEl.getAsDouble();
                    }
                }
            }

            boolean customseed = false;
            long seed = 0;

            if( object.has( "seed" ) ) {
                JsonElement seedEl = object.get( "seed" );
                if( seedEl.isJsonPrimitive() && seedEl.getAsJsonPrimitive().isNumber() ) {
                    customseed = true;
                    seed = seedEl.getAsLong();
                }
            }
            return new FractalOpenSimplex3DColor( a, b, customseed, seed, octaves, sizeX, sizeY, sizeZ );
        } catch( ColorFormatException exc ) {
            throw exc.addParent( "fracopensimplex3d" );
        }
    }

    private static IColorProvider parseFractalPerlin2D( JsonObject object ) throws ColorFormatException {
        try {
            if( ! object.has( "a" ) )
                throw new ColorFormatException( "Missing required 'a'" );
            if( ! object.has( "b" ) )
                throw new ColorFormatException( "Missing required 'b'" );
            if( ! object.has( "octaves" ) )
                throw new ColorFormatException( "Missing required 'octaves' number" );

            JsonElement octavesEl = object.get( "octaves" );
            if( ! octavesEl.isJsonPrimitive() || ! octavesEl.getAsJsonPrimitive().isNumber() )
                throw new ColorFormatException( "'octaves' must be a number" );

            int octaves = octavesEl.getAsInt();

            IColorProvider a = parseColorProvider( object.get( "a" ), "a" );
            IColorProvider b = parseColorProvider( object.get( "b" ), "b" );

            double sizeX = 1;
            double sizeZ = 1;

            if( object.has( "size" ) ) {
                JsonElement sizeEl = object.get( "size" );
                if( sizeEl.isJsonPrimitive() && sizeEl.getAsJsonPrimitive().isNumber() ) {
                    sizeX = sizeEl.getAsDouble();
                    sizeZ = sizeEl.getAsDouble();
                } else if( sizeEl.isJsonArray() && sizeEl.getAsJsonArray().size() >= 2 ) {
                    JsonElement sizeXEl = sizeEl.getAsJsonArray().get( 0 );
                    JsonElement sizeZEl = sizeEl.getAsJsonArray().get( 1 );
                    if( sizeXEl.isJsonPrimitive() && sizeXEl.getAsJsonPrimitive().isNumber() && sizeZEl.isJsonPrimitive() && sizeZEl.getAsJsonPrimitive().isNumber() ) {
                        sizeX = sizeXEl.getAsDouble();
                        sizeZ = sizeZEl.getAsDouble();
                    }
                }
            }

            boolean customseed = false;
            long seed = 0;

            if( object.has( "seed" ) ) {
                JsonElement seedEl = object.get( "seed" );
                if( seedEl.isJsonPrimitive() && seedEl.getAsJsonPrimitive().isNumber() ) {
                    customseed = true;
                    seed = seedEl.getAsLong();
                }
            }
            return new FractalPerlin2DColor( a, b, customseed, seed, octaves, sizeX, sizeZ );
        } catch( ColorFormatException exc ) {
            throw exc.addParent( "fracperlin2d" );
        }
    }

    private static IColorProvider parseFractalPerlin3D( JsonObject object ) throws ColorFormatException {
        try {
            if( ! object.has( "a" ) )
                throw new ColorFormatException( "Missing required 'a'" );
            if( ! object.has( "b" ) )
                throw new ColorFormatException( "Missing required 'b'" );
            if( ! object.has( "octaves" ) )
                throw new ColorFormatException( "Missing required 'octaves' number" );

            JsonElement octavesEl = object.get( "octaves" );
            if( ! octavesEl.isJsonPrimitive() || ! octavesEl.getAsJsonPrimitive().isNumber() )
                throw new ColorFormatException( "'octaves' must be a number" );

            int octaves = octavesEl.getAsInt();

            IColorProvider a = parseColorProvider( object.get( "a" ), "a" );
            IColorProvider b = parseColorProvider( object.get( "b" ), "b" );

            double sizeX = 1;
            double sizeY = 1;
            double sizeZ = 1;

            if( object.has( "size" ) ) {
                JsonElement sizeEl = object.get( "size" );
                if( sizeEl.isJsonPrimitive() && sizeEl.getAsJsonPrimitive().isNumber() ) {
                    sizeX = sizeEl.getAsDouble();
                    sizeY = sizeEl.getAsDouble();
                    sizeZ = sizeEl.getAsDouble();
                } else if( sizeEl.isJsonArray() && sizeEl.getAsJsonArray().size() >= 3 ) {
                    JsonElement sizeXEl = sizeEl.getAsJsonArray().get( 0 );
                    JsonElement sizeYEl = sizeEl.getAsJsonArray().get( 1 );
                    JsonElement sizeZEl = sizeEl.getAsJsonArray().get( 2 );
                    if( sizeXEl.isJsonPrimitive() && sizeXEl.getAsJsonPrimitive().isNumber() && sizeYEl.isJsonPrimitive() && sizeYEl.getAsJsonPrimitive().isNumber() && sizeZEl.isJsonPrimitive() && sizeZEl.getAsJsonPrimitive().isNumber() ) {
                        sizeX = sizeXEl.getAsDouble();
                        sizeY = sizeYEl.getAsDouble();
                        sizeZ = sizeZEl.getAsDouble();
                    }
                }
            }

            boolean customseed = false;
            long seed = 0;

            if( object.has( "seed" ) ) {
                JsonElement seedEl = object.get( "seed" );
                if( seedEl.isJsonPrimitive() && seedEl.getAsJsonPrimitive().isNumber() ) {
                    customseed = true;
                    seed = seedEl.getAsLong();
                }
            }
            return new FractalPerlin3DColor( a, b, customseed, seed, octaves, sizeX, sizeY, sizeZ );
        } catch( ColorFormatException exc ) {
            throw exc.addParent( "fracperlin3d" );
        }
    }

    private static IColorProvider parseOpenSimplex2D( JsonObject object ) throws ColorFormatException {
        try {
            if( ! object.has( "a" ) )
                throw new ColorFormatException( "Missing required 'a'" );
            if( ! object.has( "b" ) )
                throw new ColorFormatException( "Missing required 'b'" );
            IColorProvider a = parseColorProvider( object.get( "a" ), "a" );
            IColorProvider b = parseColorProvider( object.get( "b" ), "b" );

            double sizeX = 1;
            double sizeZ = 1;

            if( object.has( "size" ) ) {
                JsonElement sizeEl = object.get( "size" );
                if( sizeEl.isJsonPrimitive() && sizeEl.getAsJsonPrimitive().isNumber() ) {
                    sizeX = sizeEl.getAsDouble();
                    sizeZ = sizeEl.getAsDouble();
                } else if( sizeEl.isJsonArray() && sizeEl.getAsJsonArray().size() >= 2 ) {
                    JsonElement sizeXEl = sizeEl.getAsJsonArray().get( 0 );
                    JsonElement sizeZEl = sizeEl.getAsJsonArray().get( 1 );
                    if( sizeXEl.isJsonPrimitive() && sizeXEl.getAsJsonPrimitive().isNumber() && sizeZEl.isJsonPrimitive() && sizeZEl.getAsJsonPrimitive().isNumber() ) {
                        sizeX = sizeXEl.getAsDouble();
                        sizeZ = sizeZEl.getAsDouble();
                    }
                }
            }

            boolean customseed = false;
            long seed = 0;

            if( object.has( "seed" ) ) {
                JsonElement seedEl = object.get( "seed" );
                if( seedEl.isJsonPrimitive() && seedEl.getAsJsonPrimitive().isNumber() ) {
                    customseed = true;
                    seed = seedEl.getAsLong();
                }
            }
            return new OpenSimplex2DColor( a, b, customseed, seed, sizeX, sizeZ );
        } catch( ColorFormatException exc ) {
            throw exc.addParent( "opensimplex2d" );
        }
    }

    private static IColorProvider parseOpenSimplex3D( JsonObject object ) throws ColorFormatException {
        try {
            if( ! object.has( "a" ) )
                throw new ColorFormatException( "Missing required 'a'" );
            if( ! object.has( "b" ) )
                throw new ColorFormatException( "Missing required 'b'" );
            IColorProvider a = parseColorProvider( object.get( "a" ), "a" );
            IColorProvider b = parseColorProvider( object.get( "b" ), "b" );

            double sizeX = 1;
            double sizeY = 1;
            double sizeZ = 1;

            if( object.has( "size" ) ) {
                JsonElement sizeEl = object.get( "size" );
                if( sizeEl.isJsonPrimitive() && sizeEl.getAsJsonPrimitive().isNumber() ) {
                    sizeX = sizeEl.getAsDouble();
                    sizeY = sizeEl.getAsDouble();
                    sizeZ = sizeEl.getAsDouble();
                } else if( sizeEl.isJsonArray() && sizeEl.getAsJsonArray().size() >= 3 ) {
                    JsonElement sizeXEl = sizeEl.getAsJsonArray().get( 0 );
                    JsonElement sizeYEl = sizeEl.getAsJsonArray().get( 1 );
                    JsonElement sizeZEl = sizeEl.getAsJsonArray().get( 2 );
                    if( sizeXEl.isJsonPrimitive() && sizeXEl.getAsJsonPrimitive().isNumber() && sizeYEl.isJsonPrimitive() && sizeYEl.getAsJsonPrimitive().isNumber() && sizeZEl.isJsonPrimitive() && sizeZEl.getAsJsonPrimitive().isNumber() ) {
                        sizeX = sizeXEl.getAsDouble();
                        sizeY = sizeYEl.getAsDouble();
                        sizeZ = sizeZEl.getAsDouble();
                    }
                }
            }

            boolean customseed = false;
            long seed = 0;

            if( object.has( "seed" ) ) {
                JsonElement seedEl = object.get( "seed" );
                if( seedEl.isJsonPrimitive() && seedEl.getAsJsonPrimitive().isNumber() ) {
                    customseed = true;
                    seed = seedEl.getAsLong();
                }
            }
            return new OpenSimplex3DColor( a, b, customseed, seed, sizeX, sizeY, sizeZ );
        } catch( ColorFormatException exc ) {
            throw exc.addParent( "opensimplex3d" );
        }
    }

    private static IColorProvider parsePerlin2D( JsonObject object ) throws ColorFormatException {
        try {
            if( ! object.has( "a" ) )
                throw new ColorFormatException( "Missing required 'a'" );
            if( ! object.has( "b" ) )
                throw new ColorFormatException( "Missing required 'b'" );
            IColorProvider a = parseColorProvider( object.get( "a" ), "a" );
            IColorProvider b = parseColorProvider( object.get( "b" ), "b" );

            double sizeX = 1;
            double sizeZ = 1;

            if( object.has( "size" ) ) {
                JsonElement sizeEl = object.get( "size" );
                if( sizeEl.isJsonPrimitive() && sizeEl.getAsJsonPrimitive().isNumber() ) {
                    sizeX = sizeEl.getAsDouble();
                    sizeZ = sizeEl.getAsDouble();
                } else if( sizeEl.isJsonArray() && sizeEl.getAsJsonArray().size() >= 2 ) {
                    JsonElement sizeXEl = sizeEl.getAsJsonArray().get( 0 );
                    JsonElement sizeZEl = sizeEl.getAsJsonArray().get( 1 );
                    if( sizeXEl.isJsonPrimitive() && sizeXEl.getAsJsonPrimitive().isNumber() && sizeZEl.isJsonPrimitive() && sizeZEl.getAsJsonPrimitive().isNumber() ) {
                        sizeX = sizeXEl.getAsDouble();
                        sizeZ = sizeZEl.getAsDouble();
                    }
                }
            }

            boolean customseed = false;
            long seed = 0;

            if( object.has( "seed" ) ) {
                JsonElement seedEl = object.get( "seed" );
                if( seedEl.isJsonPrimitive() && seedEl.getAsJsonPrimitive().isNumber() ) {
                    customseed = true;
                    seed = seedEl.getAsLong();
                }
            }
            return new Perlin2DColor( a, b, customseed, seed, sizeX, sizeZ );
        } catch( ColorFormatException exc ) {
            throw exc.addParent( "perlin2d" );
        }
    }

    private static IColorProvider parsePerlin3D( JsonObject object ) throws ColorFormatException {
        try {
            if( ! object.has( "a" ) )
                throw new ColorFormatException( "Missing required 'a'" );
            if( ! object.has( "b" ) )
                throw new ColorFormatException( "Missing required 'b'" );
            IColorProvider a = parseColorProvider( object.get( "a" ), "a" );
            IColorProvider b = parseColorProvider( object.get( "b" ), "b" );

            double sizeX = 1;
            double sizeY = 1;
            double sizeZ = 1;

            if( object.has( "size" ) ) {
                JsonElement sizeEl = object.get( "size" );
                if( sizeEl.isJsonPrimitive() && sizeEl.getAsJsonPrimitive().isNumber() ) {
                    sizeX = sizeEl.getAsDouble();
                    sizeY = sizeEl.getAsDouble();
                    sizeZ = sizeEl.getAsDouble();
                } else if( sizeEl.isJsonArray() && sizeEl.getAsJsonArray().size() >= 3 ) {
                    JsonElement sizeXEl = sizeEl.getAsJsonArray().get( 0 );
                    JsonElement sizeYEl = sizeEl.getAsJsonArray().get( 1 );
                    JsonElement sizeZEl = sizeEl.getAsJsonArray().get( 2 );
                    if( sizeXEl.isJsonPrimitive() && sizeXEl.getAsJsonPrimitive().isNumber() && sizeYEl.isJsonPrimitive() && sizeYEl.getAsJsonPrimitive().isNumber() && sizeZEl.isJsonPrimitive() && sizeZEl.getAsJsonPrimitive().isNumber() ) {
                        sizeX = sizeXEl.getAsDouble();
                        sizeY = sizeYEl.getAsDouble();
                        sizeZ = sizeZEl.getAsDouble();
                    }
                }
            }

            boolean customseed = false;
            long seed = 0;

            if( object.has( "seed" ) ) {
                JsonElement seedEl = object.get( "seed" );
                if( seedEl.isJsonPrimitive() && seedEl.getAsJsonPrimitive().isNumber() ) {
                    customseed = true;
                    seed = seedEl.getAsLong();
                }
            }
            return new Perlin3DColor( a, b, customseed, seed, sizeX, sizeY, sizeZ );
        } catch( ColorFormatException exc ) {
            throw exc.addParent( "perlin3d" );
        }
    }

    private static IColorProvider parseRandom3D( JsonObject object ) throws ColorFormatException {
        try {
            if( ! object.has( "a" ) )
                throw new ColorFormatException( "Missing required 'a'" );
            if( ! object.has( "b" ) )
                throw new ColorFormatException( "Missing required 'b'" );
            IColorProvider a = parseColorProvider( object.get( "a" ), "a" );
            IColorProvider b = parseColorProvider( object.get( "b" ), "b" );

            double sizeX = 1;
            double sizeY = 1;
            double sizeZ = 1;

            if( object.has( "size" ) ) {
                JsonElement sizeEl = object.get( "size" );
                if( sizeEl.isJsonPrimitive() && sizeEl.getAsJsonPrimitive().isNumber() ) {
                    sizeX = sizeEl.getAsDouble();
                    sizeY = sizeEl.getAsDouble();
                    sizeZ = sizeEl.getAsDouble();
                } else if( sizeEl.isJsonArray() && sizeEl.getAsJsonArray().size() >= 3 ) {
                    JsonElement sizeXEl = sizeEl.getAsJsonArray().get( 0 );
                    JsonElement sizeYEl = sizeEl.getAsJsonArray().get( 1 );
                    JsonElement sizeZEl = sizeEl.getAsJsonArray().get( 2 );
                    if( sizeXEl.isJsonPrimitive() && sizeXEl.getAsJsonPrimitive().isNumber() && sizeYEl.isJsonPrimitive() && sizeYEl.getAsJsonPrimitive().isNumber() && sizeZEl.isJsonPrimitive() && sizeZEl.getAsJsonPrimitive().isNumber() ) {
                        sizeX = sizeXEl.getAsDouble();
                        sizeY = sizeYEl.getAsDouble();
                        sizeZ = sizeZEl.getAsDouble();
                    }
                }
            }

            boolean customseed = false;
            long seed = 0;

            if( object.has( "seed" ) ) {
                JsonElement seedEl = object.get( "seed" );
                if( seedEl.isJsonPrimitive() && seedEl.getAsJsonPrimitive().isNumber() ) {
                    customseed = true;
                    seed = seedEl.getAsLong();
                }
            }
            return new Random3DColor( a, b, customseed, seed, sizeX, sizeY, sizeZ );
        } catch( ColorFormatException exc ) {
            throw exc.addParent( "random3d" );
        }
    }

    private static IColorProvider parseRandom2D( JsonObject object ) throws ColorFormatException {
        try {
            if( ! object.has( "a" ) )
                throw new ColorFormatException( "Missing required 'a'" );
            if( ! object.has( "b" ) )
                throw new ColorFormatException( "Missing required 'b'" );
            IColorProvider a = parseColorProvider( object.get( "a" ), "a" );
            IColorProvider b = parseColorProvider( object.get( "b" ), "b" );

            double sizeX = 1;
            double sizeZ = 1;

            if( object.has( "size" ) ) {
                JsonElement sizeEl = object.get( "size" );
                if( sizeEl.isJsonPrimitive() && sizeEl.getAsJsonPrimitive().isNumber() ) {
                    sizeX = sizeEl.getAsDouble();
                    sizeZ = sizeEl.getAsDouble();
                } else if( sizeEl.isJsonArray() && sizeEl.getAsJsonArray().size() >= 2 ) {
                    JsonElement sizeXEl = sizeEl.getAsJsonArray().get( 0 );
                    JsonElement sizeZEl = sizeEl.getAsJsonArray().get( 1 );
                    if( sizeXEl.isJsonPrimitive() && sizeXEl.getAsJsonPrimitive().isNumber() && sizeZEl.isJsonPrimitive() && sizeZEl.getAsJsonPrimitive().isNumber() ) {
                        sizeX = sizeXEl.getAsDouble();
                        sizeZ = sizeZEl.getAsDouble();
                    }
                }
            }

            boolean customseed = false;
            long seed = 0;

            if( object.has( "seed" ) ) {
                JsonElement seedEl = object.get( "seed" );
                if( seedEl.isJsonPrimitive() && seedEl.getAsJsonPrimitive().isNumber() ) {
                    customseed = true;
                    seed = seedEl.getAsLong();
                }
            }
            return new Random2DColor( a, b, customseed, seed, sizeX, sizeZ );
        } catch( ColorFormatException exc ) {
            throw exc.addParent( "random2d" );
        }
    }

    private static IColorProvider parsePickRandom3D( JsonObject object ) throws ColorFormatException {
        try {
            if( ! object.has( "colors" ) )
                throw new ColorFormatException( "Missing required 'colors' array" );

            JsonElement colorsEl = object.get( "colors" );
            if( ! colorsEl.isJsonArray() )
                throw new ColorFormatException( "'colors' is not an array" );

            JsonArray colorsAr = colorsEl.getAsJsonArray();
            if( colorsAr.size() == 0 )
                throw new ColorFormatException( "'colors' must not be empty" );

            IColorProvider[] providers = new IColorProvider[ colorsAr.size() ];
            int i = 0;
            for( JsonElement el : colorsAr ) {
                providers[ i ] = parseColorProvider( el, "#" + ( i + 1 ) );
                i++;
            }

            double sizeX = 1;
            double sizeY = 1;
            double sizeZ = 1;

            if( object.has( "size" ) ) {
                JsonElement sizeEl = object.get( "size" );
                if( sizeEl.isJsonPrimitive() && sizeEl.getAsJsonPrimitive().isNumber() ) {
                    sizeX = sizeEl.getAsDouble();
                    sizeY = sizeEl.getAsDouble();
                    sizeZ = sizeEl.getAsDouble();
                } else if( sizeEl.isJsonArray() && sizeEl.getAsJsonArray().size() >= 3 ) {
                    JsonElement sizeXEl = sizeEl.getAsJsonArray().get( 0 );
                    JsonElement sizeYEl = sizeEl.getAsJsonArray().get( 1 );
                    JsonElement sizeZEl = sizeEl.getAsJsonArray().get( 2 );
                    if( sizeXEl.isJsonPrimitive() && sizeXEl.getAsJsonPrimitive().isNumber() && sizeYEl.isJsonPrimitive() && sizeYEl.getAsJsonPrimitive().isNumber() && sizeZEl.isJsonPrimitive() && sizeZEl.getAsJsonPrimitive().isNumber() ) {
                        sizeX = sizeXEl.getAsDouble();
                        sizeY = sizeYEl.getAsDouble();
                        sizeZ = sizeZEl.getAsDouble();
                    }
                }
            }

            boolean customseed = false;
            long seed = 0;

            if( object.has( "seed" ) ) {
                JsonElement seedEl = object.get( "seed" );
                if( seedEl.isJsonPrimitive() && seedEl.getAsJsonPrimitive().isNumber() ) {
                    customseed = true;
                    seed = seedEl.getAsLong();
                }
            }
            return new Random3DColorPicker( customseed, seed, providers, sizeX, sizeY, sizeZ );
        } catch( ColorFormatException exc ) {
            throw exc.addParent( "pickrandom3d" );
        }
    }

    private static IColorProvider parsePickRandom2D( JsonObject object ) throws ColorFormatException {
        try {
            if( ! object.has( "colors" ) )
                throw new ColorFormatException( "Missing required 'colors' array" );

            JsonElement colorsEl = object.get( "colors" );
            if( ! colorsEl.isJsonArray() )
                throw new ColorFormatException( "'colors' is not an array" );

            JsonArray colorsAr = colorsEl.getAsJsonArray();
            if( colorsAr.size() == 0 )
                throw new ColorFormatException( "'colors' must not be empty" );

            IColorProvider[] providers = new IColorProvider[ colorsAr.size() ];
            int i = 0;
            for( JsonElement el : colorsAr ) {
                providers[ i ] = parseColorProvider( el, "#" + ( i + 1 ) );
                i++;
            }

            double sizeX = 1;
            double sizeZ = 1;

            if( object.has( "size" ) ) {
                JsonElement sizeEl = object.get( "size" );
                if( sizeEl.isJsonPrimitive() && sizeEl.getAsJsonPrimitive().isNumber() ) {
                    sizeX = sizeEl.getAsDouble();
                    sizeZ = sizeEl.getAsDouble();
                } else if( sizeEl.isJsonArray() && sizeEl.getAsJsonArray().size() >= 2 ) {
                    JsonElement sizeXEl = sizeEl.getAsJsonArray().get( 0 );
                    JsonElement sizeZEl = sizeEl.getAsJsonArray().get( 1 );
                    if( sizeXEl.isJsonPrimitive() && sizeXEl.getAsJsonPrimitive().isNumber() && sizeZEl.isJsonPrimitive() && sizeZEl.getAsJsonPrimitive().isNumber() ) {
                        sizeX = sizeXEl.getAsDouble();
                        sizeZ = sizeZEl.getAsDouble();
                    }
                }
            }

            boolean customseed = false;
            long seed = 0;

            if( object.has( "seed" ) ) {
                JsonElement seedEl = object.get( "seed" );
                if( seedEl.isJsonPrimitive() && seedEl.getAsJsonPrimitive().isNumber() ) {
                    customseed = true;
                    seed = seedEl.getAsLong();
                }
            }
            return new Random2DColorPicker( customseed, seed, providers, sizeX, sizeZ );
        } catch( ColorFormatException exc ) {
            throw exc.addParent( "pickrandom2d" );
        }
    }

    private static IColorProvider parseCheckerboard( JsonObject object ) throws ColorFormatException {
        try {
            if( ! object.has( "a" ) )
                throw new ColorFormatException( "Missing required 'a'" );
            if( ! object.has( "b" ) )
                throw new ColorFormatException( "Missing required 'b'" );
            IColorProvider a = parseColorProvider( object.get( "a" ), "a" );
            IColorProvider b = parseColorProvider( object.get( "b" ), "b" );
            int size = 1;
            if( object.has( "size" ) ) {
                JsonElement sizeEl = object.get( "size" );
                if( sizeEl.isJsonPrimitive() && sizeEl.getAsJsonPrimitive().isNumber() ) {
                    size = sizeEl.getAsInt();
                }
            }
            return new CheckerboardColor( a, b, size );
        } catch( ColorFormatException exc ) {
            throw exc.addParent( "checkerboard" );
        }
    }

    private static IColorProvider parseRGBArray( JsonArray array ) throws ColorFormatException {
        try {
            if( array.size() < 3 ) {
                throw new ColorFormatException( "Array must have at least three elements" );
            }
            JsonElement rEl = array.get( 0 );
            JsonElement gEl = array.get( 1 );
            JsonElement bEl = array.get( 2 );

            if( ! rEl.isJsonPrimitive() || ! rEl.getAsJsonPrimitive().isNumber() || rEl.getAsInt() < 0 || rEl.getAsInt() > 255 ) {
                throw new ColorFormatException( "First element is not a number" );
            }
            if( ! gEl.isJsonPrimitive() || ! gEl.getAsJsonPrimitive().isNumber() || gEl.getAsInt() < 0 || gEl.getAsInt() > 255 ) {
                throw new ColorFormatException( "Second element is not a number" );
            }
            if( ! bEl.isJsonPrimitive() || ! bEl.getAsJsonPrimitive().isNumber() || bEl.getAsInt() < 0 || bEl.getAsInt() > 255 ) {
                throw new ColorFormatException( "Third element is not a number" );
            }

            return new SolidColor( ColorUtil.rgb( rEl.getAsInt(), gEl.getAsInt(), bEl.getAsInt() ) );
        } catch( ColorFormatException exc ) {
            throw exc.addParent( "rgb" );
        }
    }

    public interface IColorProvider {
        default int getColor( BlockPos pos, long seed ) { return getColor( pos ); }
        int getColor( BlockPos pos );
    }

    public static abstract class SeededProvider implements IColorProvider {
        private long seed;
        private final boolean hasCustomSeed;

        public SeededProvider( boolean customSeed, long seed ) {
            this.hasCustomSeed = customSeed;
            if( hasCustomSeed ) {
                this.seed = seed;
            }
            initForSeed( this.seed );
        }

        @Override
        public final int getColor( BlockPos pos, long seed ) {
            if( seed != this.seed ) {
                this.seed = seed;
                if( ! hasCustomSeed ) {
                    initForSeed( seed );
                }
            }
            return getColor( pos );
        }

        protected long getSeed() {
            return seed;
        }

        public abstract void initForSeed( long seed );
    }

    public static class SolidColor implements IColorProvider {
        private final int color;

        public SolidColor( int color ) {
            this.color = color;
        }

        @Override
        public int getColor( BlockPos pos ) {
            return this.color;
        }
    }

    public static class CheckerboardColor implements IColorProvider {
        private final IColorProvider a;
        private final IColorProvider b;
        private final int size;

        public CheckerboardColor( IColorProvider a, IColorProvider b, int size ) {
            this.a = a;
            this.b = b;
            this.size = size;
        }

        @Override
        public int getColor( BlockPos pos, long seed ) {
            int x = pos.getX() / size;
            int z = pos.getZ() / size;
            if( ( x + z & 1 ) == 0 ) {
                return a.getColor( pos, seed );
            } else {
                return b.getColor( pos, seed );
            }
        }

        @Override
        public int getColor( BlockPos pos ) {
            return 0;
        }
    }

    public static abstract class NoiseColor extends SeededProvider {
        private INoise3D noise;
        private final IColorProvider a;
        private final IColorProvider b;

        public NoiseColor( IColorProvider a, IColorProvider b, boolean customSeed, long seed ) {
            super( customSeed, seed );
            this.a = a;
            this.b = b;
        }

        @Override
        public int getColor( BlockPos pos ) {
            int colorA = a.getColor( pos, getSeed() );
            int colorB = b.getColor( pos, getSeed() );
            double noise = this.noise.generate( pos.getX(), pos.getY(), pos.getZ() );
            return ColorUtil.interpolate( colorA, colorB, noise );
        }

        @Override
        public final void initForSeed( long seed ) {
            this.noise = createNoise( seed );
        }

        protected abstract INoise3D createNoise( long seed );
    }

    public static class FractalPerlin3DColor extends NoiseColor {
        private final int octaves;
        private final double scaleX;
        private final double scaleY;
        private final double scaleZ;

        public FractalPerlin3DColor( IColorProvider a, IColorProvider b, boolean customSeed, long seed, int octaves, double scaleX, double scaleY, double scaleZ ) {
            super( a, b, customSeed, seed );
            this.octaves = octaves;
            this.scaleX = scaleX;
            this.scaleY = scaleY;
            this.scaleZ = scaleZ;
        }

        @Override
        protected INoise3D createNoise( long seed ) {
            return new FractalPerlin3D( (int) seed, scaleX, scaleY, scaleZ, octaves );
        }
    }

    public static class Perlin3DColor extends NoiseColor {
        private final double scaleX;
        private final double scaleY;
        private final double scaleZ;

        public Perlin3DColor( IColorProvider a, IColorProvider b, boolean customSeed, long seed, double scaleX, double scaleY, double scaleZ ) {
            super( a, b, customSeed, seed );
            this.scaleX = scaleX;
            this.scaleY = scaleY;
            this.scaleZ = scaleZ;
        }

        @Override
        protected INoise3D createNoise( long seed ) {
            return new Perlin3D( (int) seed, scaleX, scaleY, scaleZ );
        }
    }

    public static class FractalOpenSimplex3DColor extends NoiseColor {
        private final int octaves;
        private final double scaleX;
        private final double scaleY;
        private final double scaleZ;

        public FractalOpenSimplex3DColor( IColorProvider a, IColorProvider b, boolean customSeed, long seed, int octaves, double scaleX, double scaleY, double scaleZ ) {
            super( a, b, customSeed, seed );
            this.octaves = octaves;
            this.scaleX = scaleX;
            this.scaleY = scaleY;
            this.scaleZ = scaleZ;
        }

        @Override
        protected INoise3D createNoise( long seed ) {
            return new FractalOpenSimplex3D( (int) seed, scaleX, scaleY, scaleZ, octaves );
        }
    }

    public static class OpenSimplex3DColor extends NoiseColor {
        private final double scaleX;
        private final double scaleY;
        private final double scaleZ;

        public OpenSimplex3DColor( IColorProvider a, IColorProvider b, boolean customSeed, long seed, double scaleX, double scaleY, double scaleZ ) {
            super( a, b, customSeed, seed );
            this.scaleX = scaleX;
            this.scaleY = scaleY;
            this.scaleZ = scaleZ;
        }

        @Override
        protected INoise3D createNoise( long seed ) {
            return new OpenSimplex3D( (int) seed, scaleX, scaleY, scaleZ );
        }
    }

    public static class FractalPerlin2DColor extends NoiseColor {
        private final int octaves;
        private final double scaleX;
        private final double scaleZ;

        public FractalPerlin2DColor( IColorProvider a, IColorProvider b, boolean customSeed, long seed, int octaves, double scaleX, double scaleZ ) {
            super( a, b, customSeed, seed );
            this.octaves = octaves;
            this.scaleX = scaleX;
            this.scaleZ = scaleZ;
        }

        @Override
        protected INoise3D createNoise( long seed ) {
            return INoise3D.from2DY( new FractalPerlin2D( (int) seed, scaleX, scaleZ, octaves ) );
        }
    }

    public static class Perlin2DColor extends NoiseColor {
        private final double scaleX;
        private final double scaleZ;

        public Perlin2DColor( IColorProvider a, IColorProvider b, boolean customSeed, long seed, double scaleX, double scaleZ ) {
            super( a, b, customSeed, seed );
            this.scaleX = scaleX;
            this.scaleZ = scaleZ;
        }

        @Override
        protected INoise3D createNoise( long seed ) {
            return INoise3D.from2DY( new Perlin2D( (int) seed, scaleX, scaleZ ) );
        }
    }

    public static class FractalOpenSimplex2DColor extends NoiseColor {
        private final int octaves;
        private final double scaleX;
        private final double scaleZ;

        public FractalOpenSimplex2DColor( IColorProvider a, IColorProvider b, boolean customSeed, long seed, int octaves, double scaleX, double scaleZ ) {
            super( a, b, customSeed, seed );
            this.octaves = octaves;
            this.scaleX = scaleX;
            this.scaleZ = scaleZ;
        }

        @Override
        protected INoise3D createNoise( long seed ) {
            return INoise3D.from2DY( new FractalOpenSimplex2D( (int) seed, scaleX, scaleZ, octaves ) );
        }
    }

    public static class OpenSimplex2DColor extends NoiseColor {
        private final double scaleX;
        private final double scaleZ;

        public OpenSimplex2DColor( IColorProvider a, IColorProvider b, boolean customSeed, long seed, double scaleX, double scaleZ ) {
            super( a, b, customSeed, seed );
            this.scaleX = scaleX;
            this.scaleZ = scaleZ;
        }

        @Override
        protected INoise3D createNoise( long seed ) {
            return INoise3D.from2DY( new OpenSimplex2D( (int) seed, scaleX, scaleZ ) );
        }
    }

    public static class Random2DColor extends NoiseColor {
        private final double scaleX;
        private final double scaleZ;

        public Random2DColor( IColorProvider a, IColorProvider b, boolean customSeed, long seed, double scaleX, double scaleZ ) {
            super( a, b, customSeed, seed );
            this.scaleX = scaleX;
            this.scaleZ = scaleZ;
        }

        @Override
        protected INoise3D createNoise( long seed ) {
            return INoise3D.from2DY( INoise2D.random( (int) seed ).scale( scaleX, scaleZ ) );
        }
    }

    public static class Random3DColor extends NoiseColor {
        private final double scaleX;
        private final double scaleY;
        private final double scaleZ;

        public Random3DColor( IColorProvider a, IColorProvider b, boolean customSeed, long seed, double scaleX, double scaleY, double scaleZ ) {
            super( a, b, customSeed, seed );
            this.scaleX = scaleX;
            this.scaleY = scaleY;
            this.scaleZ = scaleZ;
        }

        @Override
        protected INoise3D createNoise( long seed ) {
            return INoise3D.random( (int) seed ).scale( scaleX, scaleY, scaleZ );
        }
    }

    public static class Random2DColorPicker extends SeededProvider {
        private final IColorProvider[] colors;
        private final double scaleX;
        private final double scaleZ;
        private int seed;

        public Random2DColorPicker( boolean customSeed, long seed, IColorProvider[] colors, double scaleX, double scaleZ ) {
            super( customSeed, seed );
            this.colors = colors;
            this.scaleX = scaleX;
            this.scaleZ = scaleZ;
        }

        @Override
        public int getColor( BlockPos pos ) {
            int hash = ( Hash.hash2I( seed, (int) ( pos.getX() / scaleX ), (int) ( pos.getZ() / scaleZ ) ) & Integer.MAX_VALUE ) % colors.length;
            return colors[ hash ].getColor( pos, getSeed() );
        }

        @Override
        public void initForSeed( long seed ) {
            this.seed = (int) seed;
        }
    }

    public static class Random3DColorPicker extends SeededProvider {
        private final IColorProvider[] colors;
        private final double scaleX;
        private final double scaleY;
        private final double scaleZ;
        private int seed;

        public Random3DColorPicker( boolean customSeed, long seed, IColorProvider[] colors, double scaleX, double scaleY, double scaleZ ) {
            super( customSeed, seed );
            this.colors = colors;
            this.scaleX = scaleX;
            this.scaleY = scaleY;
            this.scaleZ = scaleZ;
        }

        @Override
        public int getColor( BlockPos pos ) {
            int hash = ( Hash.hash3I( seed, (int) ( pos.getX() / scaleX ), (int) ( pos.getY() / scaleY ), (int) ( pos.getZ() / scaleZ ) ) & Integer.MAX_VALUE ) % colors.length;
            return colors[ hash ].getColor( pos, getSeed() );
        }

        @Override
        public void initForSeed( long seed ) {
            this.seed = (int) seed;
        }
    }

    public static class ColorFormatException extends Exception {
        private final ArrayList<String> stack = new ArrayList<>();

        public ColorFormatException( String message ) {
            super( message );
        }

        public ColorFormatException addParent( String name ) {
            stack.add( 0, name );
            return this;
        }

        @Override
        public String getMessage() {
            StringBuilder stackBuilder = new StringBuilder( "[" );
            boolean arrow = false;
            for( String s : stack ) {
                if( arrow ) {
                    stackBuilder.append( " > " );
                } else {
                    arrow = true;
                }
                stackBuilder.append( s );
            }
            return stackBuilder.append( "] " ).append( super.getMessage() ).toString();
        }
    }
}
