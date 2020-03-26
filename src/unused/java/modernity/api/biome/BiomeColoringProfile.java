/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 28 - 2020
 * Author: rgsw
 */

package modernity.api.biome;

import com.google.gson.*;
import modernity.api.util.ColorUtil;
import modernity.client.ModernityClient;
import modernity.client.colormap.ColorMap;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import net.rgsw.noise.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Java representation of a biome color profile. Biome color profiles are json files defined in the
 * <code>assets/.../data/biomecolors/</code> folder. They specify the color of foliage, water, and other things in
 * specific biomes.
 *
 * @author RGSW
 */
// TODO:
// Move all provider implementations to separate classes and add deserializers, so that new implementations can be
// added easily
@OnlyIn( Dist.CLIENT )
public class BiomeColoringProfile {
    private static final Logger LOGGER = LogManager.getLogger();

    // Thread-local because chunk rendering is multithreaded...
    private final ThreadLocal<BlockPos> cachedPos = new ThreadLocal<>();
    private final ThreadLocal<Long> cachedSeed = new ThreadLocal<>();
    private final ThreadLocal<Integer> cachedColor = new ThreadLocal<>();

    private final HashMap<Biome, IColorProvider> biomeColors = new HashMap<>();
    private IColorProvider itemColor;
    private IColorProvider defaultColor;

    /**
     * Obtains the color provider for a specific biome.
     */
    public IColorProvider getColorProviderFor( Biome biome ) {
        return biomeColors.computeIfAbsent( biome, b -> getDefaultColor() );
    }

    /**
     * Returns the color at a specific position in the specified world, blending colors at biome edges when necessary.
     * The last seed, position and returned color are cached and loaded from cache when this method is called again.
     *
     * @return The 256-bits RGB color in an integer.
     */
    public int getColor( IEnviromentBlockReader world, BlockPos pos ) {
        long seed = ModernityClient.get().getLastWorldSeed();

        int color;
        if( pos == null && world == null ) {
            color = getDefaultColor().getColor( BlockPos.ZERO, seed );
        } else if( pos == null ) {
            color = getColor( world, BlockPos.ZERO );
        } else if( world == null ) {
            color = getDefaultColor().getColor( pos, seed );
        } else {
            if( cachedSeed.get() != null && seed == cachedSeed.get() && pos.equals( cachedPos.get() ) ) {
                return cachedColor.get();
            } else {
                color = getColor( world, pos, seed );
            }
        }

        if( pos != null ) {
            cachedColor.set( color );
            cachedPos.set( pos.toImmutable() );
            cachedSeed.set( seed );
        }

        return color;
    }

    /**
     * Returns the item color.
     *
     * @return The 256-bits RGB color in an integer.
     */
    public int getItemColor() {
        long seed = 0;
        if( Minecraft.getInstance().world != null ) {
            seed = Minecraft.getInstance().world.getSeed();
        }

        return getItemColorProvider().getColor( BlockPos.ZERO, seed );
    }

    /**
     * Returns the color at a specific position in the specified world, blending colors at biome edges when necessary.
     * This is an internal method used in {@link #getColor(IEnviromentBlockReader, BlockPos)}.
     *
     * @return The 256-bits RGB color in an integer.
     */
    private int getColor( IEnviromentBlockReader world, BlockPos pos, long seed ) {
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

    /**
     * Returns the default color provider, which is used for biomes that were not defined in the JSON files.
     */
    private IColorProvider getDefaultColor() {
        if( defaultColor == null ) defaultColor = createErrorProvider();
        return defaultColor;
    }

    /**
     * Returns the item color provider, or the {@linkplain #getDefaultColor() default color provider} when the item
     * color provider was not defined in the JSON files.
     */
    private IColorProvider getItemColorProvider() {
        if( itemColor == null ) itemColor = getDefaultColor();
        return itemColor;
    }

    /**
     * Returns the color for a specific biome at a specific position, using the specific world seed. This method is used
     * in {@link #getColor(IEnviromentBlockReader, BlockPos, long)}.
     */
    private int getColorFor( Biome biome, BlockPos pos, long seed ) {
        return getColorProviderFor( biome ).getColor( pos, seed );
    }

    /**
     * Applies a specific map of biome-color relations, which is loaded from the JSON file.
     */
    private void applyLayer( Map<String, IColorProvider> layer ) {
        layer.forEach( this::applyLayerItem );
    }

    /**
     * Applies a specific layer item, or one entry in one JSON file.
     *
     * @param key   The entry key
     * @param color The entry value
     */
    private void applyLayerItem( String key, IColorProvider color ) {
        Biome biome = getBiomeForName( key );
        if( biome != null ) {
            biomeColors.put( biome, color );
        } else if( key.equals( "item" ) ) {
            itemColor = color;
        } else if( key.equals( "default" ) ) {
            defaultColor = color;
        }
    }

    /**
     * Loads a coloring profile from the JSON files in the resource packs. The JSON files are layered in the order of
     * resource packs.
     *
     * @param resManager  The resource manager to load from.
     * @param loc         The path to the JSON file
     * @param profileName The profile name, only used for error reporting
     * @return The loaded coloring profile
     *
     * @throws IOException When any IOException occurs during loading...
     */
    public static BiomeColoringProfile create( IResourceManager resManager, ResourceLocation loc, String profileName ) throws IOException {
        List<IResource> list = resManager.getAllResources( loc );
        BiomeColoringProfile profile = new BiomeColoringProfile();
        for( IResource res : list ) {
            InputStream stream = res.getInputStream();
            JsonParser parser = new JsonParser();
            try {
                JsonElement element = parser.parse( new InputStreamReader( stream ) );
                String packName = res.getPackName();
                if( ! element.isJsonObject() ) {
                    LOGGER.warn( "Biome color profile must have an object as root" );
                    continue;
                }
                Map<String, IColorProvider> layer = parseProfileLayer( element.getAsJsonObject(), profileName, packName );
                profile.applyLayer( layer );
            } catch( JsonParseException exc ) {
                LOGGER.warn( "Biome color profile of pack '{}' has invalid syntax: {}", res.getPackName(), exc.getMessage() );
            }
        }
        LOGGER.info( "Biome color profile loaded: " + loc );
        return profile;
    }

    /**
     * Parses the JSON file from one resource pack, resulting in a map of biome-color relations.
     *
     * @param object      The JSON object loaded
     * @param profileName The profile name, only used for error reporting
     * @param packName    The pack name, only used for error reporting
     * @return A map containing the entries defined in the JSON file
     */
    private static Map<String, IColorProvider> parseProfileLayer( JsonObject object, String profileName, String packName ) {
        HashMap<String, IColorProvider> biomeColors = new HashMap<>();
        ArrayList<ColorFormatException> errors = new ArrayList<>();
        for( Map.Entry<String, JsonElement> entry : object.entrySet() ) {
            String biome = entry.getKey();
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

    /**
     * Loads a biome for the specific name, suppressing any errors thrown when parsing the {@link ResourceLocation}.
     */
    private static Biome getBiomeForName( String name ) {
        try {
            ResourceLocation loc = new ResourceLocation( name );
            return ForgeRegistries.BIOMES.getValue( loc );
        } catch( Throwable e ) {
            return null;
        }
    }

    /**
     * Creates a magenta-black checkerboard pattern, indicating an error...
     */
    public static IColorProvider createErrorProvider() {
        return new CheckerboardColor( new SolidColor( 0 ), new SolidColor( 0xff00ff ), 1 );
    }

    /**
     * Parses a color provider from a JSON element. If any error occurs, it is reported in the log and the error pattern
     * is returned.
     */
    public static IColorProvider parseColorProvider( JsonElement element ) {
        try {
            return parseColorProvider( element, "format" );
        } catch( ColorFormatException exc ) {
            LOGGER.warn( "Error parsing coloring format:" );
            LOGGER.warn( exc.getMessage() );
            return createErrorProvider();
        }
    }

    /**
     * Internally parses a color provider, throwing an exception when not a valid format...
     *
     * @param element The JSON to parse
     * @param name    The name of the provider we're parsing, used only for error reporting
     * @return The parsed color provider.
     *
     * @throws ColorFormatException Thrown when not a valid format...
     */
    private static IColorProvider parseColorProvider( JsonElement element, String name ) throws ColorFormatException {
        try {
            // Null is not valid
            if( element.isJsonNull() ) {
                throw new ColorFormatException( "Null is not a color" );
            }
            // Primitive values
            else if( element.isJsonPrimitive() ) {
                // String, loads '#xxxxxx' or '#xxx' as a color...
                if( element.getAsJsonPrimitive().isString() ) {
                    Integer col = ColorUtil.parseHexString( element.getAsJsonPrimitive().getAsString() );
                    if( col == null ) {
                        throw new ColorFormatException( "Invalid hexadecimal string ('" + element.getAsJsonPrimitive().getAsString() + "')" );
                    }
                    return new SolidColor( col );
                }
                // Number, loads grayscale value
                else if( element.getAsJsonPrimitive().isNumber() ) {
                    int color = element.getAsJsonPrimitive().getAsInt();
                    return new SolidColor( color );
                }
                // Boolean, true=white, false=black
                else if( element.getAsJsonPrimitive().isBoolean() ) {
                    if( element.getAsJsonPrimitive().getAsBoolean() ) {
                        return new SolidColor( 0xffffff );
                    } else {
                        return new SolidColor( 0 );
                    }
                }
                // Not valid!
                else {
                    throw new ColorFormatException( "String, Number or Boolean expected as primitive color..." );
                }
            }
            // Array, loads rgb from first three elements
            else if( element.isJsonArray() ) {
                JsonArray array = element.getAsJsonArray();
                return parseRGBArray( array );
            }
            // Object, loads any other pattern
            else {
                JsonObject object = element.getAsJsonObject();
                // RGB
                if( object.has( "rgb" ) ) {
                    JsonElement rgb = object.get( "rgb" );
                    if( ! rgb.isJsonArray() ) {
                        throw new ColorFormatException( "'rgb' requires an array" );
                    } else {
                        return parseRGBArray( rgb.getAsJsonArray() );
                    }
                }
                // Greyscale
                else if( object.has( "greyscale" ) ) {
                    JsonElement grayscale = object.get( "greyscale" );
                    if( ! grayscale.isJsonPrimitive() || ! grayscale.getAsJsonPrimitive().isNumber() ) {
                        throw new ColorFormatException( "'greyscale' requires a number" );
                    } else {
                        return new SolidColor( ColorUtil.fromGrayscale( grayscale.getAsDouble() ) );
                    }
                }
                // Grayscale, alias to greyscale
                else if( object.has( "grayscale" ) ) {
                    JsonElement grayscale = object.get( "grayscale" );
                    if( ! grayscale.isJsonPrimitive() || ! grayscale.getAsJsonPrimitive().isNumber() ) {
                        throw new ColorFormatException( "'grayscale' requires a number" );
                    } else {
                        return new SolidColor( ColorUtil.fromGrayscale( grayscale.getAsDouble() ) );
                    }
                }
                // RGB values
                else if( object.has( "r" ) && object.has( "g" ) && object.has( "b" ) ) {
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
                }
                // Checkerboard
                else if( object.has( "checkerboard" ) ) {
                    JsonElement checkerboard = object.get( "checkerboard" );
                    if( ! checkerboard.isJsonObject() ) {
                        throw new ColorFormatException( "'checkerboard' requires an object" );
                    } else {
                        return parseCheckerboard( checkerboard.getAsJsonObject() );
                    }
                }
                // Pick random 2D
                else if( object.has( "pickrandom2d" ) ) {
                    JsonElement pickrandom = object.get( "pickrandom2d" );
                    if( ! pickrandom.isJsonObject() ) {
                        throw new ColorFormatException( "'pickrandom2d' requires an object" );
                    } else {
                        return parsePickRandom2D( pickrandom.getAsJsonObject() );
                    }
                }
                // Pick random 3D
                else if( object.has( "pickrandom3d" ) ) {
                    JsonElement pickrandom = object.get( "pickrandom3d" );
                    if( ! pickrandom.isJsonObject() ) {
                        throw new ColorFormatException( "'pickrandom3d' requires an object" );
                    } else {
                        return parsePickRandom3D( pickrandom.getAsJsonObject() );
                    }
                }
                // Interpolate random 2D
                else if( object.has( "random2d" ) ) {
                    JsonElement random = object.get( "random2d" );
                    if( ! random.isJsonObject() ) {
                        throw new ColorFormatException( "'random2d' requires an object" );
                    } else {
                        return parseRandom2D( random.getAsJsonObject() );
                    }
                }
                // Interpolate random 3D
                else if( object.has( "random3d" ) ) {
                    JsonElement random = object.get( "random3d" );
                    if( ! random.isJsonObject() ) {
                        throw new ColorFormatException( "'random3d' requires an object" );
                    } else {
                        return parseRandom3D( random.getAsJsonObject() );
                    }
                }
                // Interpolate perlin 2D
                else if( object.has( "perlin2d" ) ) {
                    JsonElement random = object.get( "perlin2d" );
                    if( ! random.isJsonObject() ) {
                        throw new ColorFormatException( "'perlin2d' requires an object" );
                    } else {
                        return parsePerlin2D( random.getAsJsonObject() );
                    }
                }
                // Interpolate perlin 3D
                else if( object.has( "perlin3d" ) ) {
                    JsonElement random = object.get( "perlin3d" );
                    if( ! random.isJsonObject() ) {
                        throw new ColorFormatException( "'perlin3d' requires an object" );
                    } else {
                        return parsePerlin3D( random.getAsJsonObject() );
                    }
                }
                // Interpolate open simplex 2D
                else if( object.has( "opensimplex2d" ) ) {
                    JsonElement random = object.get( "opensimplex2d" );
                    if( ! random.isJsonObject() ) {
                        throw new ColorFormatException( "'opensimplex2d' requires an object" );
                    } else {
                        return parseOpenSimplex2D( random.getAsJsonObject() );
                    }
                }
                // Interpolate open simplex 3D
                else if( object.has( "opensimplex3d" ) ) {
                    JsonElement random = object.get( "opensimplex3d" );
                    if( ! random.isJsonObject() ) {
                        throw new ColorFormatException( "'opensimplex3d' requires an object" );
                    } else {
                        return parseOpenSimplex3D( random.getAsJsonObject() );
                    }
                }
                // Interpolate fractal perlin 2D
                else if( object.has( "fracperlin2d" ) ) {
                    JsonElement random = object.get( "fracperlin2d" );
                    if( ! random.isJsonObject() ) {
                        throw new ColorFormatException( "'fracperlin2d' requires an object" );
                    } else {
                        return parseFractalPerlin2D( random.getAsJsonObject() );
                    }
                }
                // Interpolate fractal perlin 3D
                else if( object.has( "fracperlin3d" ) ) {
                    JsonElement random = object.get( "fracperlin3d" );
                    if( ! random.isJsonObject() ) {
                        throw new ColorFormatException( "'fracperlin3d' requires an object" );
                    } else {
                        return parseFractalPerlin3D( random.getAsJsonObject() );
                    }
                }
                // Interpolate fractal open simplex 2D
                else if( object.has( "fracopensimplex2d" ) ) {
                    JsonElement random = object.get( "fracopensimplex2d" );
                    if( ! random.isJsonObject() ) {
                        throw new ColorFormatException( "'fracopensimplex2d' requires an object" );
                    } else {
                        return parseFractalOpenSimplex2D( random.getAsJsonObject() );
                    }
                }
                // Interpolate fractal open simplex 3D
                else if( object.has( "fracopensimplex3d" ) ) {
                    JsonElement random = object.get( "fracopensimplex3d" );
                    if( ! random.isJsonObject() ) {
                        throw new ColorFormatException( "'fracopensimplex3d' requires an object" );
                    } else {
                        return parseFractalOpenSimplex3D( random.getAsJsonObject() );
                    }
                }
                // From color map
                else if( object.has( "colormap" ) ) {
                    JsonElement random = object.get( "colormap" );
                    if( ! random.isJsonObject() ) {
                        throw new ColorFormatException( "'colormap' requires an object" );
                    } else {
                        return parseColorMap( random.getAsJsonObject() );
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

    /**
     * Parses fractal open simplex 2D provider from JSON
     */
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

    /**
     * Parses fractal open simplex 3D provider from JSON
     */
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

    /**
     * Parses fractal perlin 2D provider from JSON
     */
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

    /**
     * Parses fractal perlin 3D provider from JSON
     */
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

    /**
     * Parses open simplex 2D provider from JSON
     */
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

    /**
     * Parses open simplex 3D provider from JSON
     */
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

    /**
     * Parses perlin 2D provider from JSON
     */
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

    /**
     * Parses perlin 3D provider from JSON
     */
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

    /**
     * Parses random 3D provider from JSON
     */
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

    /**
     * Parses random 2D provider from JSON
     */
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

    /**
     * Parses pick random 3D provider from JSON
     */
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

    /**
     * Parses pick random 2D provider from JSON
     */
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

    /**
     * Parses checkerboard provider from JSON
     */
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

    private static IColorProvider parseColorMap( JsonObject object ) throws ColorFormatException {
        try {
            if( ! object.has( "texture" ) )
                throw new ColorFormatException( "Missing required 'texture'" );
            if( ! object.has( "x" ) )
                throw new ColorFormatException( "Missing required 'x'" );
            if( ! object.has( "y" ) )
                throw new ColorFormatException( "Missing required 'y'" );

            if( ! object.get( "texture" ).isJsonPrimitive() || ! object.getAsJsonPrimitive( "texture" ).isString() )
                throw new ColorFormatException( "'texture' must be a string" );

            if( ! object.get( "x" ).isJsonPrimitive() || ! object.getAsJsonPrimitive( "x" ).isNumber() )
                throw new ColorFormatException( "'x' must be a number" );

            if( ! object.get( "y" ).isJsonPrimitive() || ! object.getAsJsonPrimitive( "y" ).isNumber() )
                throw new ColorFormatException( "'y' must be a number" );

            String texLoc = object.getAsJsonPrimitive( "texture" ).getAsString();
            ResourceLocation texture = ResourceLocation.tryCreate( texLoc );

            if( texture == null ) {
                throw new ColorFormatException( "'" + texLoc + "' is not a valid texture name" );
            }

            float x = object.getAsJsonPrimitive( "x" ).getAsFloat();
            float y = object.getAsJsonPrimitive( "y" ).getAsFloat();

            IColorProvider fallback = createErrorProvider();
            if( object.has( "fallback" ) ) {
                JsonElement fallbackEl = object.get( "fallback" );
                fallback = parseColorProvider( fallbackEl );
            }
            return new FromColorMap( texture, fallback, x, y );
        } catch( ColorFormatException exc ) {
            throw exc.addParent( "colormap" );
        }
    }

    /**
     * Parses a solid color from rgb array using the first three elements, ignoring any additional elements
     */
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

    /**
     * Main color provider interface...
     */
    @FunctionalInterface
    public interface IColorProvider {
        default int getColor( BlockPos pos, long seed ) {
            return getColor( pos );
        }
        int getColor( BlockPos pos );
    }

    /**
     * Color provider using seed
     */
    public static abstract class SeededProvider implements IColorProvider {
        private long seed;
        private final boolean hasCustomSeed;

        public SeededProvider( boolean customSeed, long seed ) {
            this.hasCustomSeed = customSeed;
            if( hasCustomSeed ) {
                this.seed = seed;
            }
        }

        /**
         * Has to be called after initialization in constructor
         */
        public void init() {
            initForSeed( seed );
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

    /**
     * Provides a solid color
     */
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

    /**
     * Provides a checkerboard pattern between two color poviders
     */
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

    /**
     * Interpolates between two colors using noise
     */
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
            double noise = this.noise.generate( pos.getX(), pos.getY(), pos.getZ() ) * 0.5 + 0.5;
            return ColorUtil.interpolate( colorA, colorB, noise );
        }

        @Override
        public final void initForSeed( long seed ) {
            this.noise = createNoise( seed );
        }

        protected abstract INoise3D createNoise( long seed );
    }

    /**
     * Fractal perlin 3D color provider
     */
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
            init();
        }

        @Override
        protected INoise3D createNoise( long seed ) {
            return new FractalPerlin3D( (int) seed, scaleX, scaleY, scaleZ, octaves );
        }
    }

    /**
     * Perlin 3D color provider
     */
    public static class Perlin3DColor extends NoiseColor {
        private final double scaleX;
        private final double scaleY;
        private final double scaleZ;

        public Perlin3DColor( IColorProvider a, IColorProvider b, boolean customSeed, long seed, double scaleX, double scaleY, double scaleZ ) {
            super( a, b, customSeed, seed );
            this.scaleX = scaleX;
            this.scaleY = scaleY;
            this.scaleZ = scaleZ;
            init();
        }

        @Override
        protected INoise3D createNoise( long seed ) {
            return new Perlin3D( (int) seed, scaleX, scaleY, scaleZ );
        }
    }

    /**
     * Fractal open simplex 3D color provider
     */
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
            init();
        }

        @Override
        protected INoise3D createNoise( long seed ) {
            return new FractalOpenSimplex3D( (int) seed, scaleX, scaleY, scaleZ, octaves );
        }
    }

    /**
     * Open simplex 3D color provider
     */
    public static class OpenSimplex3DColor extends NoiseColor {
        private final double scaleX;
        private final double scaleY;
        private final double scaleZ;

        public OpenSimplex3DColor( IColorProvider a, IColorProvider b, boolean customSeed, long seed, double scaleX, double scaleY, double scaleZ ) {
            super( a, b, customSeed, seed );
            this.scaleX = scaleX;
            this.scaleY = scaleY;
            this.scaleZ = scaleZ;
            init();
        }

        @Override
        protected INoise3D createNoise( long seed ) {
            return new OpenSimplex3D( (int) seed, scaleX, scaleY, scaleZ );
        }
    }

    /**
     * Fractal perlin 2D color provider
     */
    public static class FractalPerlin2DColor extends NoiseColor {
        private final int octaves;
        private final double scaleX;
        private final double scaleZ;

        public FractalPerlin2DColor( IColorProvider a, IColorProvider b, boolean customSeed, long seed, int octaves, double scaleX, double scaleZ ) {
            super( a, b, customSeed, seed );
            this.octaves = octaves;
            this.scaleX = scaleX;
            this.scaleZ = scaleZ;
            init();
        }

        @Override
        protected INoise3D createNoise( long seed ) {
            return INoise3D.from2DY( new FractalPerlin2D( (int) seed, scaleX, scaleZ, octaves ) );
        }
    }

    /**
     * Perlin 2D color provider
     */
    public static class Perlin2DColor extends NoiseColor {
        private final double scaleX;
        private final double scaleZ;

        public Perlin2DColor( IColorProvider a, IColorProvider b, boolean customSeed, long seed, double scaleX, double scaleZ ) {
            super( a, b, customSeed, seed );
            this.scaleX = scaleX;
            this.scaleZ = scaleZ;
            init();
        }

        @Override
        protected INoise3D createNoise( long seed ) {
            return INoise3D.from2DY( new Perlin2D( (int) seed, scaleX, scaleZ ) );
        }
    }

    /**
     * Fractal open simplex 2D color provider
     */
    public static class FractalOpenSimplex2DColor extends NoiseColor {
        private final int octaves;
        private final double scaleX;
        private final double scaleZ;

        public FractalOpenSimplex2DColor( IColorProvider a, IColorProvider b, boolean customSeed, long seed, int octaves, double scaleX, double scaleZ ) {
            super( a, b, customSeed, seed );
            this.octaves = octaves;
            this.scaleX = scaleX;
            this.scaleZ = scaleZ;
            init();
        }

        @Override
        protected INoise3D createNoise( long seed ) {
            return INoise3D.from2DY( new FractalOpenSimplex2D( (int) seed, scaleX, scaleZ, octaves ) );
        }
    }

    /**
     * Open simplex 2D color provider
     */
    public static class OpenSimplex2DColor extends NoiseColor {
        private final double scaleX;
        private final double scaleZ;

        public OpenSimplex2DColor( IColorProvider a, IColorProvider b, boolean customSeed, long seed, double scaleX, double scaleZ ) {
            super( a, b, customSeed, seed );
            this.scaleX = scaleX;
            this.scaleZ = scaleZ;
            init();
        }

        @Override
        protected INoise3D createNoise( long seed ) {
            return INoise3D.from2DY( new OpenSimplex2D( (int) seed, scaleX, scaleZ ) );
        }
    }

    /**
     * Random 2D color provider
     */
    public static class Random2DColor extends NoiseColor {
        private final double scaleX;
        private final double scaleZ;

        public Random2DColor( IColorProvider a, IColorProvider b, boolean customSeed, long seed, double scaleX, double scaleZ ) {
            super( a, b, customSeed, seed );
            this.scaleX = scaleX;
            this.scaleZ = scaleZ;
            init();
        }

        @Override
        protected INoise3D createNoise( long seed ) {
            return INoise3D.from2DY( INoise2D.random( (int) seed ).scale( 1 / scaleX, 1 / scaleZ ) );
        }
    }

    /**
     * Random 3D color provider
     */
    public static class Random3DColor extends NoiseColor {
        private final double scaleX;
        private final double scaleY;
        private final double scaleZ;

        public Random3DColor( IColorProvider a, IColorProvider b, boolean customSeed, long seed, double scaleX, double scaleY, double scaleZ ) {
            super( a, b, customSeed, seed );
            this.scaleX = scaleX;
            this.scaleY = scaleY;
            this.scaleZ = scaleZ;
            init();
        }

        @Override
        protected INoise3D createNoise( long seed ) {
            return INoise3D.random( (int) seed ).scale( 1 / scaleX, 1 / scaleY, 1 / scaleZ );
        }
    }

    /**
     * Pick a random color provider using 2D noise
     */
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
            init();
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

    /**
     * Pick a random color provider using 3D noise
     */
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

    public static class FromColorMap implements IColorProvider {
        private final ColorMap colorMap;
        private final IColorProvider fallback;
        private final float x;
        private final float y;

        public FromColorMap( ResourceLocation loc, IColorProvider fallback, float x, float y ) {
            this.colorMap = new ColorMap( new ResourceLocation( loc.getNamespace(), "textures/" + loc.getPath() + ".png" ) );
            this.fallback = fallback;
            this.x = x;
            this.y = y;
        }

        @Override
        public int getColor( BlockPos pos ) {
            return colorMap.get( x, y );
        }

        @Override
        public int getColor( BlockPos pos, long seed ) {
            return colorMap.isLoaded() ? colorMap.get( x, y ) : fallback.getColor( pos, seed );
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
