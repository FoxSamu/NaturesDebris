/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 7 - 2019
 */

package modernity.common.world.gen.structure;

import modernity.api.util.IPicker;
import modernity.api.util.WeightedCollection;
import modernity.common.item.MDItems;
import modernity.common.util.MDLootTables;
import net.minecraft.block.*;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.state.IProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.IChunkGenSettings;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.rgsw.MathUtil;
import net.rgsw.noise.FractalOpenSimplex3D;
import org.apache.commons.lang3.Validate;

import java.util.Collections;
import java.util.Random;

public class CurseRuinStructure extends Structure<CurseRuinStructure.Type> {
    public static final String NAME = "MDCurseRuin";

    @Override
    public boolean place( IWorld worldIn, IChunkGenerator<? extends IChunkGenSettings> generator, Random rand, BlockPos pos, Type config ) {
        return super.place( worldIn, generator, rand, pos, config );
    }

    @Override
    protected boolean hasStartAt( IChunkGenerator<?> chunkGen, Random rand, int chunkPosX, int chunkPosZ ) {
        int s = 3;
        int x = chunkPosX >> s;
        int z = chunkPosZ >> s;
        rand.setSeed( (long) ( x ^ z << s ) ^ chunkGen.getSeed() );
        rand.nextInt();
        if( rand.nextInt( 5 ) != 0 ) {
            return false;
        } else if( chunkPosX != ( x << s ) + rand.nextInt( (int) Math.pow( 2, s ) ) ) {
            return false;
        } else if( chunkPosZ != ( z << s ) + rand.nextInt( (int) Math.pow( 2, s ) ) ) {
            return false;
        } else {
            Biome biome = chunkGen.getBiomeProvider().getBiome( new BlockPos( ( chunkPosX << 4 ) + 9, 0, ( chunkPosZ << 4 ) + 9 ), Biomes.DEFAULT );
            if( biome == null ) {
                // Not the case, but just to suppress warning
                return false;
            }
            return chunkGen.hasStructure( biome, this );
        }
    }

    @Override
    protected boolean isEnabledIn( IWorld world ) {
        return world.getWorldInfo().isMapFeaturesEnabled();
    }

    @Override
    protected StructureStart makeStart( IWorld world, IChunkGenerator<?> generator, SharedSeedRandom random, int x, int z ) {
        Biome biome = generator.getBiomeProvider().getBiome( new BlockPos( ( x << 4 ) + 9, 0, ( z << 4 ) + 9 ), Biomes.DEFAULT );
        Validate.notNull( biome ); // Not the case
        Type type = (Type) generator.getStructureConfig( biome, this );
        if( type == null ) {
            type = Type.PLAINS;
        }
        return new Start( x, z, biome, random, world.getSeed(), world, type );
    }

    @Override
    protected String getStructureName() {
        return NAME;
    }

    @Override
    public int getSize() {
        return 1;
    }

    public static class Start extends StructureStart {

        public Start() {
        }

        public Start( int x, int z, Biome biome, SharedSeedRandom rand, long seed, IWorld world, Type type ) {
            super( x, z, biome, rand, seed );

            components.add( new Piece( rand, x * 16 + 8, z * 16 + 8, world, type ) );

            this.recalculateStructureSize( world );
        }
    }

    public static class Piece extends StructurePiece {

        private static final double N000 = - 0.2;
        private static final double N010 = - 0.5;
        private static final double N001 = - 0.1;
        private static final double N011 = - 0.3;
        private static final double N100 = 0.4;
        private static final double N110 = 0.2;
        private static final double N101 = 1;
        private static final double N111 = 0.8;

        private int height = - 1;
        private int x;
        private int z;
        private FractalOpenSimplex3D noise;
        private Type type;
        private Mirror mirror;
        private boolean frame;
        private boolean spawner;
        private boolean workbench;
        private boolean flowerPot;

        private double[] noiseBuffer;

        public Piece() {
            super( 0 );
        }

        public Piece( Random rand, int x, int z, IWorld world, Type type ) {
            super( 0 );

            this.x = x;
            this.z = z;

            this.type = type;

            mirror = Mirror.values()[ rand.nextInt( 3 ) ];

            createNoise( rand.nextInt() );



            EnumFacing coordBase = EnumFacing.Plane.HORIZONTAL.random( rand );
//            EnumFacing.Axis axis = coordBase.getAxis();

            setCoordBaseMode( coordBase );

            boundingBox = MutableBoundingBox.getComponentToAddBoundingBox( x, 0, z, - 2, - 1, - 4, 6, 4, 9, coordBase );
//            if( axis == EnumFacing.Axis.Z ) {
//                boundingBox = new MutableBoundingBox( - 2 + x, - 1, - 4 + z, 3 + x, 4, 4 + z );
//            } else {
//                boundingBox = new MutableBoundingBox( - 4 + x, - 1, - 2 + z, 4 + x, 4, 3 + z );
//            }
        }

        private void createNoise( int seed ) {
            noise = new FractalOpenSimplex3D( seed, 2.88156990284, 3 );
        }

        private double genNoise( int x, int y, int z ) {
            double dx = x / 5D;
            double dy = y / 3D;
            double dz = z / 8D;

            double noise = this.noise.generate( x, y, z );

            double n00 = MathUtil.lerp( N000, N001, dz );
            double n01 = MathUtil.lerp( N010, N011, dz );
            double n10 = MathUtil.lerp( N100, N101, dz );
            double n11 = MathUtil.lerp( N110, N111, dz );

            double n0 = MathUtil.lerp( n00, n01, dy );
            double n1 = MathUtil.lerp( n10, n11, dy );

            double n = MathUtil.lerp( n0, n1, dx );

            noise += n;
            return noise;
        }

        private void initNoiseBuffer() {
            if( noiseBuffer == null ) {
                noiseBuffer = new double[ 6 * 4 * 9 ];
                for( int x = 0; x <= 5; x++ ) {
                    for( int y = 0; y <= 3; y++ ) {
                        for( int z = 0; z <= 8; z++ ) {
                            noiseBuffer[ ( x * 4 + y ) * 9 + z ] = genNoise( x, y, z );
                        }
                    }
                }
            }
        }

        @Override
        protected void writeAdditional( NBTTagCompound nbt ) {
            nbt.putInt( "h", height );
            nbt.putInt( "x", x );
            nbt.putInt( "z", z );
            nbt.putInt( "s", noise.getSeed() );
            nbt.putInt( "m", mirror.ordinal() );
            nbt.putByte( "t", (byte) type.ordinal() );
            nbt.putBoolean( "f", frame );
            nbt.putBoolean( "p", spawner );
            nbt.putBoolean( "w", workbench );
            nbt.putBoolean( "q", flowerPot );
        }

        @Override
        protected void readAdditional( NBTTagCompound nbt, TemplateManager template ) {
            height = nbt.getInt( "h" );
            x = nbt.getInt( "x" );
            z = nbt.getInt( "z" );
            frame = nbt.getBoolean( "f" );
            spawner = nbt.getBoolean( "p" );
            workbench = nbt.getBoolean( "w" );
            flowerPot = nbt.getBoolean( "q" );
            createNoise( nbt.getInt( "s" ) );
            mirror = Mirror.values()[ nbt.getInt( "m" ) ];
            type = Type.values()[ nbt.getByte( "t" ) ];
        }

        @Override
        public boolean addComponentParts( IWorld world, Random rand, MutableBoundingBox box, ChunkPos cpos ) {
            if( height < 0 ) {
                height = world.getHeight( Heightmap.Type.WORLD_SURFACE_WG, x, z ) - 1;
                boundingBox.offset( 0, height, 0 );
            }

            Config cfg = type.config;

            double brokenNess = rand.nextDouble() * 0.3 - 0.15;

            for( int x = - 2; x <= 7; x++ ) {
                for( int z = - 2; z <= 10; z++ ) {
                    if( ( z == - 2 || z == 10 ) && ( x == - 2 || x == 7 ) ) continue;

                    if( getBlockStateFromPos( world, x, 1, z, box ).getMaterial().blocksMovement() && ( x < 0 || x > 5 ) && ( z < 0 || z > 8 ) ) {
                        setBlockState( world, cfg.getFillerBlock( rand ), x, 0, z, box );
                    } else {
                        setBlockState( world, cfg.getTopBlock( rand ), x, 0, z, box );
                    }
                    replaceAirAndLiquidDownwardsRandom( world, cfg::getFillerBlock, x, - 1, z, box, rand );
                }
            }

            fillWithRandomBlocksAndNoise( world, box, 0, 0, 0, 5, 0, 8, false, 0.1 + brokenNess, rand, Picker.selector( cfg::getFloor ) );

            fillWithRandomBlocksAndNoise( world, box, 0, 3, 1, 0, 3, 7, true, - 0.1 + brokenNess, rand, Picker.selector( cfg::getWallSlab ) );
            fillWithRandomBlocksAndNoise( world, box, 5, 3, 1, 5, 3, 7, true, - 0.1 + brokenNess, rand, Picker.selector( cfg::getWallSlab ) );
            fillWithRandomBlocksAndNoise( world, box, 1, 3, 0, 4, 3, 0, true, - 0.1 + brokenNess, rand, Picker.selector( cfg::getWallSlab ) );
            fillWithRandomBlocksAndNoise( world, box, 1, 3, 8, 4, 3, 8, true, - 0.1 + brokenNess, rand, Picker.selector( cfg::getWallSlab ) );

            fillWithRandomBlocksAndNoise( world, box, 1, 3, 1, 4, 3, 7, true, - 0.1 + brokenNess, rand, Picker.selector( cfg::getRoof ) );

            fillWithRandomBlocksAndNoise( world, box, 0, 1, 1, 0, 2, 7, false, 0 + brokenNess, rand, Picker.selector( cfg::getWall ) );
            fillWithRandomBlocksAndNoise( world, box, 5, 1, 1, 5, 2, 7, true, 0 + brokenNess, rand, Picker.selector( cfg::getWall ) );
            fillWithRandomBlocksAndNoise( world, box, 1, 1, 0, 4, 2, 0, false, 0 + brokenNess, rand, Picker.selector( cfg::getWall ) );
            fillWithRandomBlocksAndNoise( world, box, 1, 1, 8, 4, 2, 8, true, 0 + brokenNess, rand, Picker.selector( cfg::getWall ) );

            fillWithRandomBlocksAndNoise( world, box, 0, 0, 0, 0, 2, 0, false, 0 + brokenNess, rand, Picker.selector( cfg::getCorner ) );
            fillWithRandomBlocksAndNoise( world, box, 5, 0, 0, 5, 2, 0, false, 0 + brokenNess, rand, Picker.selector( cfg::getCorner ) );
            fillWithRandomBlocksAndNoise( world, box, 5, 0, 8, 5, 2, 8, true, 0 + brokenNess, rand, Picker.selector( cfg::getCorner ) );
            fillWithRandomBlocksAndNoise( world, box, 0, 0, 8, 0, 2, 8, false, 0 + brokenNess, rand, Picker.selector( cfg::getCorner ) );


            fillWithAir( world, box, 1, 1, 1, 4, 2, 7 );

            EnumFacing coordBase = mirror.mirror( getCoordBaseMode() );

            if( ! workbench ) {
                BlockPos workbenchPos = new BlockPos( getXWithOffset( 3, 7 ), getYWithOffset( 1 ), getZWithOffset( 3, 7 ) );
                if( box.isVecInside( workbenchPos ) ) {
                    cfg.placeWorkbench( rand, coordBase, world, workbenchPos );
                    workbench = true;
                }
            }

            if( ! flowerPot ) {
                BlockPos flowerPotPos = new BlockPos( getXWithOffset( 4, 6 ), getYWithOffset( 1 ), getZWithOffset( 4, 6 ) );
                if( box.isVecInside( flowerPotPos ) ) {
                    world.setBlockState( flowerPotPos, cfg.getFlowerPot( rand ), 2 );
                    flowerPot = true;
                }
            }

            BlockPos chestPos = new BlockPos( getXWithOffset( 4, 7 ), getYWithOffset( 1 ), getZWithOffset( 4, 7 ) );

            generateChest( world, box, rand, chestPos, cfg.getChestLootTable( rand ), Blocks.CHEST.getDefaultState().with( BlockChest.FACING, coordBase.getOpposite() ) );

            int shardType = getShardType( world );

            if( ! frame && shardType > 0 ) {
                BlockPos framePos = new BlockPos( getXWithOffset( 4, 7 ), getYWithOffset( 2 ), getZWithOffset( 4, 7 ) );
                if( box.isVecInside( framePos ) ) {
                    frame = true;

                    world.setBlockState( framePos.offset( coordBase ), cfg.getWall( rand ), 2 );
                    world.setBlockState( framePos, Blocks.AIR.getDefaultState(), 2 );

                    Item shard;
                    switch( shardType ) {
                        case 1:
                            shard = MDItems.CURSE_CRYSTAL_SHARD_1;
                            break;
                        case 2:
                            shard = MDItems.CURSE_CRYSTAL_SHARD_2;
                            break;
                        case 3:
                            shard = MDItems.CURSE_CRYSTAL_SHARD_3;
                            break;
                        default:
                            shard = MDItems.CURSE_CRYSTAL_SHARD_4;
                            break;
                    }
                    EntityItemFrame frame = new EntityItemFrame( world.getWorld(), framePos, coordBase.getOpposite() );
                    frame.setDisplayedItem( new ItemStack( shard ) );
                    frame.setItemRotation( rand.nextInt( 8 ) );
                    world.spawnEntity( frame );
                }
            }

            for( int i = 0; i < 30; i++ ) {
                int rx = rand.nextInt( 6 );
                int ry = rand.nextInt( 3 );
                int rz = rand.nextInt( 9 );

                // Item frame: don't place inside
                if( rx == 4 && ry == 2 && rz == 7 ) continue;

                if( ! getBlockStateFromPos( world, rx, ry, rz, box ).getMaterial().blocksMovement() ) {
                    if( getBlockStateFromPos( world, rx, ry - 1, rz, box ).isFullCube() ) {
                        for( EnumFacing facing : EnumFacing.Plane.HORIZONTAL ) {
                            int ox = rx + facing.getXOffset();
                            int oz = rz + facing.getZOffset();


                            if( getBlockStateFromPos( world, ox, ry, oz, box ).getMaterial().blocksMovement() ) {
                                setBlockState( world, cfg.getDebris( rand ), rx, ry, rz, box );
                                break;
                            }
                        }
                    }
                }
            }

            if( cfg.hasSpiderWebs( rand ) ) {
                for( int i = 0; i < 50; i++ ) {
                    int rx = rand.nextInt( 6 );
                    int ry = rand.nextInt( 4 );
                    int rz = rand.nextInt( 9 );

                    if( ! getBlockStateFromPos( world, rx, ry, rz, box ).getMaterial().blocksMovement() ) {
                        int amount = 0;
                        for( EnumFacing facing : EnumFacing.values() ) {
                            int ox = rx + facing.getXOffset();
                            int oy = ry + facing.getYOffset();
                            int oz = rz + facing.getZOffset();

                            if( getBlockStateFromPos( world, ox, oy, oz, box ).getMaterial().blocksMovement() ) {
                                amount++;
                                if( amount >= 2 ) {
                                    setBlockState( world, Blocks.COBWEB.getDefaultState(), rx, ry, rz, box );
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            if( cfg.hasVines( rand ) ) {
                for( int i = 0; i < 70; i++ ) {
                    int rx = rand.nextInt( 10 ) - 2;
                    int ry = rand.nextInt( 4 ) + 1;
                    int rz = rand.nextInt( 13 ) - 2;

                    if( ! getBlockStateFromPos( world, rx, ry, rz, box ).getMaterial().blocksMovement() ) {
                        int facings = 0;
                        for( EnumFacing facing : EnumFacing.Plane.HORIZONTAL ) {
                            int ox = rx + facing.getXOffset();
                            int oz = rz + facing.getZOffset();

                            if( getBlockStateFromPos( world, ox, ry, oz, box ).isFullCube() && rand.nextInt( 4 ) == 0 ) {
                                facings |= 1 << facing.getHorizontalIndex();
                            }
                        }

                        boolean up = getBlockStateFromPos( world, rx, ry + 1, rz, box ).getBlockFaceShape( world, pos( rx, ry + 1, rz ), EnumFacing.DOWN ) == BlockFaceShape.SOLID;
                        if( rand.nextBoolean() ) {
                            up = false;
                        }

                        if( facings > 0 ) {

                            int maxLen = 0;
                            int[] lengths = new int[ 4 ];

                            for( EnumFacing facing : EnumFacing.Plane.HORIZONTAL ) {
                                boolean has = ( facings & 1 << facing.getHorizontalIndex() ) > 0;
                                int len = has ? rand.nextInt( 3 ) + 1 : 0;
                                if( len > maxLen ) {
                                    maxLen = len;
                                }
                                lengths[ facing.getHorizontalIndex() ] = len;
                            }

                            for( int l = 0; l < maxLen; l++ ) {
                                int oy = ry - l;
                                if( getBlockStateFromPos( world, rx, oy, rz, box ).getMaterial().blocksMovement() ) {
                                    break;
                                }

                                IBlockState state = Blocks.VINE.getDefaultState();

                                state = state.with( BlockVine.UP, up && l == 0 );

                                for( EnumFacing facing : EnumFacing.Plane.HORIZONTAL ) {
                                    boolean face = l < lengths[ facing.getHorizontalIndex() ];
                                    state = state.with( BlockVine.FACING_TO_PROPERTY_MAP.get( facing.getOpposite() ), face );
                                }

                                state = state.mirror( mirror );

                                setBlockState( world, state, rx, oy, rz, box );
                            }

                        }
                    }
                }
            }

            int sx = rand.nextInt( 2 ) + 2;
            int sz = rand.nextInt( 3 ) + 2;

            if( ! spawner ) {
                BlockPos pos = new BlockPos( getXWithOffset( sx, sz ), getYWithOffset( 1 ), getZWithOffset( sx, sz ) );
                if( box.isVecInside( pos ) ) {
                    spawner = true;
                    world.setBlockState( pos, Blocks.SPAWNER.getDefaultState(), 2 );
                    TileEntity te = world.getTileEntity( pos );
                    if( te instanceof TileEntityMobSpawner ) {
                        ( (TileEntityMobSpawner) te ).getSpawnerBaseLogic().setEntityType( cfg.getSpawnerType( rand ) );
                    }
                }
            }

            if( cfg.hasSnow( rand ) ) {
                for( int x = - 2; x <= 7; x++ ) {
                    for( int z = - 2; z < 10; z++ ) {
                        for( int y = 5; y >= - 1; y-- ) {
                            if( getBlockStateFromPos( world, x, y, z, box ).isAir() ) {
                                if( getBlockStateFromPos( world, x, y, z, box ).isFullCube() ) {
                                    setBlockState( world, Blocks.SNOW.getDefaultState(), x, y, z, box );
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            return true;
        }

        private BlockPos pos( int x, int y, int z ) {
            return new BlockPos( getXWithOffset( x, z ), getYWithOffset( y ), getZWithOffset( x, z ) );
        }

        private void replaceAirAndLiquidDownwardsRandom( IWorld world, Picker statePicker, int x, int y, int z, MutableBoundingBox box, Random rand ) {
            int offx = getXWithOffset( x, z );
            int offy = getYWithOffset( y );
            int offz = getZWithOffset( x, z );
            if( box.isVecInside( new BlockPos( offx, offy, offz ) ) ) {
                while( ( world.isAirBlock( new BlockPos( offx, offy, offz ) ) || world.getBlockState( new BlockPos( offx, offy, offz ) ).getMaterial().isLiquid() ) && offy > 1 ) {
                    world.setBlockState( new BlockPos( offx, offy, offz ), statePicker.random( rand ), 2 );
                    -- offy;
                }

                world.setBlockState( new BlockPos( offx, offy, offz ), statePicker.random( rand ), 2 );
            }
        }

        private void fillWithRandomBlocksAndNoise( IWorld world, MutableBoundingBox box, int x0, int y0, int z0, int x1, int y1, int z1, boolean alwaysReplaceSolid, double noiseAddend, Random rand, BlockSelector selector ) {
            initNoiseBuffer();
            for( int y = y0; y <= y1; ++ y ) {
                for( int x = x0; x <= x1; ++ x ) {
                    for( int z = z0; z <= z1; ++ z ) {
                        double noise = getNoise( x, y, z ) + noiseAddend;
                        boolean replace = noise > 0;
                        if( alwaysReplaceSolid && ! getBlockStateFromPos( world, x, y, z, box ).isAir() ) {
                            replace = true;
                        }
                        if( replace ) {
                            selector.selectBlocks( rand, x, y, z, y == y0 || y == y1 || x == x0 || x == x1 || z == z0 || z == z1 );
                            setBlockState( world, selector.getBlockState(), x, y, z, box );
                        }
                    }
                }
            }
        }

        private double getNoise( int x, int y, int z ) {
            return noiseBuffer[ ( x * 4 + y ) * 9 + z ];
        }

        private int getShardType( IWorld world ) {
            int[] types = getShardTypes( world );
            return types[ type.config.getBiomeType() ];
        }

        protected int getXWithOffset( int x, int z ) {
            EnumFacing coordBase = getCoordBaseMode();
            if( coordBase == null ) {
                return x;
            } else {
                if( mirror == Mirror.FRONT_BACK ) coordBase = coordBase.getOpposite();
                switch( coordBase ) {
                    case NORTH:
                        return boundingBox.maxX - x;
                    case SOUTH:
                        return boundingBox.minX + x;
                    case WEST:
                        return boundingBox.maxX - z;
                    case EAST:
                        return boundingBox.minX + z;
                    default:
                        return x;
                }
            }
        }

        protected int getZWithOffset( int x, int z ) {
            EnumFacing coordBase = getCoordBaseMode();
            if( coordBase == null ) {
                return z;
            } else {
                if( mirror == Mirror.LEFT_RIGHT ) coordBase = coordBase.getOpposite();
                switch( coordBase ) {
                    case NORTH:
                        return boundingBox.maxZ - z;
                    case SOUTH:
                        return boundingBox.minZ + z;
                    case WEST:
                        return boundingBox.maxZ - x;
                    case EAST:
                        return boundingBox.minZ + x;
                    default:
                        return z;
                }
            }
        }

        private static int[] getShardTypes( IWorld world ) {
            long seed = world.getSeed();
            int[] order = { 1, 2, 3, 4, 0 };
            Random rand = new Random( seed * 88129 );
            for( int i = 0; i < 5; i++ ) {
                int index = rand.nextInt( 5 );
                if( index == i ) continue;
                int tmp = order[ index ];
                order[ index ] = order[ i ];
                order[ i ] = tmp;
            }
            return order;
        }
    }

    public enum Type implements IFeatureConfig {
        PLAINS( new PlainsConfig( false ) ),
        TUNDRA( new PlainsConfig( true ) ),
        TAIGA( new TaigaConfig( false ) ),
        SNOW_TAIGA( new TaigaConfig( true ) ),
        DESERT( new DesertConfig() ),
        MESA( new MesaConfig() ),
        FOREST( new ForestConfig() ),
        BIRCH_FOREST( new BirchForestConfig() ),
        FLOWER_FOREST( new FlowerForestConfig() ),
        SWAMP( new WarmConfig( 0 ) ),
        DARK_FOREST( new WarmConfig( 2 ) ),
        JUNGLE( new WarmConfig( 1 ) ),
        SAVANNA( new SavannaConfig() );
        private final Config config;

        Type( Config config ) {
            this.config = config;
        }
    }

    public interface Config {
        IBlockState getCorner( Random rand );
        IBlockState getWall( Random rand );
        IBlockState getDebris( Random rand );
        IBlockState getWallSlab( Random rand );
        IBlockState getRoof( Random rand );
        IBlockState getFloor( Random rand );
        IBlockState getFlowerPot( Random rand );
        IBlockState getTopBlock( Random rand );
        IBlockState getFillerBlock( Random rand );
        void placeWorkbench( Random rand, EnumFacing coordBase, IWorld world, BlockPos pos );

        boolean hasVines( Random rand );
        boolean hasSpiderWebs( Random rand );
        boolean hasSnow( Random rand );

        EntityType getSpawnerType( Random rand );

        ResourceLocation getChestLootTable( Random rand );

        int getBiomeType();
    }

    protected static abstract class AbstractConfig implements Config {
        protected static final int PLAINS_TYPE = 0;
        protected static final int FOREST_TYPE = 1;
        protected static final int TAIGA_TYPE = 2;
        protected static final int WARM_TYPE = 3;
        protected static final int DRY_TYPE = 4;

        protected final WeightedCollection<Picker> corners = new WeightedCollection<>();
        protected final WeightedCollection<Picker> walls = new WeightedCollection<>();
        protected final WeightedCollection<Picker> wallSlabs = new WeightedCollection<>();
        protected final WeightedCollection<Picker> debris = new WeightedCollection<>();
        protected final WeightedCollection<Picker> roofs = new WeightedCollection<>();
        protected final WeightedCollection<Picker> floors = new WeightedCollection<>();
        protected final WeightedCollection<Picker> flowerPots = new WeightedCollection<>();
        protected final WeightedCollection<Picker> topBlocks = new WeightedCollection<>();
        protected final WeightedCollection<Picker> fillerBlocks = new WeightedCollection<>();

        protected final WeightedCollection<EntityType> spawnerTypes = new WeightedCollection<>();
        protected final WeightedCollection<Workbench> workbenches = new WeightedCollection<>();
        protected final WeightedCollection<ResourceLocation> chestLootTables = new WeightedCollection<>();

        protected boolean vines;
        protected boolean webs;
        protected boolean snow;

        protected final int biomeType;

        protected AbstractConfig( int biomeType ) {
            this.biomeType = biomeType;
        }

        protected FurnaceWorkbench createFurnace() {
            FurnaceWorkbench workbench = new FurnaceWorkbench();
            workbench.furnaceFuel.add( new RandomItem( Items.COAL, 1, 10 ), 10 );
            workbench.furnaceFuel.add( new RandomItem( Items.CHARCOAL, 1, 10 ), 10 );
            workbench.furnaceFuel.add( new RandomItem( Items.STICK, 1, 10 ), 10 );
            workbench.furnaceFuel.add( new RandomItem( Items.AIR ), 25 );

            workbench.furnaceResult.add( new RandomItem( Items.IRON_INGOT, 1, 10 ), 100 );
            workbench.furnaceResult.add( new RandomItem( Items.GOLD_INGOT, 1, 10 ), 50 );
            workbench.furnaceResult.add( new RandomItem( Blocks.STONE, 1, 10 ), 100 );
            workbench.furnaceResult.add( new RandomItem( Items.ROTTEN_FLESH, 1, 10 ), 50 );
            workbench.furnaceResult.add( new RandomItem( Items.NETHER_BRICK, 1, 10 ), 50 );
            workbench.furnaceResult.add( new RandomItem( Items.CHARCOAL, 1, 10 ), 50 );
            workbench.furnaceResult.add( new RandomItem( Items.DIAMOND, 1, 10 ), 20 );
            workbench.furnaceResult.add( new RandomItem( Items.EMERALD, 1, 10 ), 6 );
            workbench.furnaceResult.add( new RandomItem( Items.AIR ), 230 );

            return workbench;
        }

        protected BrewingStandWorkbench createBrewingStand() {
            BrewingStandWorkbench workbench = new BrewingStandWorkbench();
            workbench.potions.add( new PotionEffect( MobEffects.LUCK, 6000 ), 10 );
            workbench.potions.add( new PotionEffect( MobEffects.LUCK, 12000 ), 3 );
            workbench.potions.add( new PotionEffect( MobEffects.HASTE, 4000 ), 10 );
            workbench.potions.add( new PotionEffect( MobEffects.HASTE, 8000 ), 3 );
            workbench.potions.add( new PotionEffect( MobEffects.FIRE_RESISTANCE, 4000 ), 3 );
            workbench.potions.add( new PotionEffect( MobEffects.INVISIBILITY, 4000 ), 7 );
            workbench.potions.add( new PotionEffect( MobEffects.BLINDNESS, 7000 ), 10 );
            workbench.potions.add( new PotionEffect( MobEffects.BLINDNESS, 12000 ), 4 );
            workbench.potions.add( new PotionEffect( MobEffects.HUNGER, 5000 ), 3 );
            workbench.potions.add( new PotionEffect( MobEffects.POISON, 6000 ), 12 );
            workbench.potions.add( new PotionEffect( MobEffects.NIGHT_VISION, 9600 ), 10 );
            workbench.potions.add( new PotionEffect( MobEffects.WEAKNESS, 9600 ), 6 );

            return workbench;
        }

        @Override
        public IBlockState getCorner( Random rand ) {
            return corners.random( rand ).random( rand );
        }

        @Override
        public IBlockState getWall( Random rand ) {
            return walls.random( rand ).random( rand );
        }

        @Override
        public IBlockState getDebris( Random rand ) {
            return debris.random( rand ).random( rand );
        }

        @Override
        public IBlockState getWallSlab( Random rand ) {
            return wallSlabs.random( rand ).random( rand );
        }

        @Override
        public IBlockState getRoof( Random rand ) {
            return roofs.random( rand ).random( rand );
        }

        @Override
        public IBlockState getFloor( Random rand ) {
            return floors.random( rand ).random( rand );
        }

        @Override
        public IBlockState getFlowerPot( Random rand ) {
            return flowerPots.random( rand ).random( rand );
        }

        @Override
        public IBlockState getTopBlock( Random rand ) {
            return topBlocks.random( rand ).random( rand );
        }

        @Override
        public IBlockState getFillerBlock( Random rand ) {
            return fillerBlocks.random( rand ).random( rand );
        }

        @Override
        public void placeWorkbench( Random rand, EnumFacing coordBase, IWorld world, BlockPos pos ) {
            workbenches.random( rand ).placeWorkbench( world, coordBase, pos, rand );
        }

        @Override
        public boolean hasVines( Random rand ) {
            return vines;
        }

        @Override
        public boolean hasSpiderWebs( Random rand ) {
            return webs;
        }

        @Override
        public boolean hasSnow( Random rand ) {
            return snow;
        }

        @Override
        public EntityType getSpawnerType( Random rand ) {
            return spawnerTypes.random( rand );
        }

        @Override
        public ResourceLocation getChestLootTable( Random rand ) {
            return chestLootTables.random( rand );
        }

        @Override
        public int getBiomeType() {
            return biomeType;
        }

        protected static ItemStack randomItem( WeightedCollection<RandomItem> items, Random rand ) {
            return items.random( rand ).generateStack( rand );
        }

        protected interface Workbench {
            void placeWorkbench( IWorld world, EnumFacing coordBase, BlockPos pos, Random rand );
        }

        protected enum DefaultWorkbench implements Workbench {
            CRAFTING_TABLE {
                @Override
                public void placeWorkbench( IWorld world, EnumFacing coordBase, BlockPos pos, Random rand ) {
                    world.setBlockState( pos, Blocks.CRAFTING_TABLE.getDefaultState(), 2 );
                }
            },
            CAULDRON {
                @Override
                public void placeWorkbench( IWorld world, EnumFacing coordBase, BlockPos pos, Random rand ) {
                    world.setBlockState( pos, Blocks.CAULDRON.getDefaultState().with( BlockCauldron.LEVEL, rand.nextInt( 4 ) ), 2 );
                }
            },
            EMPTY_CAULDRON {
                @Override
                public void placeWorkbench( IWorld world, EnumFacing coordBase, BlockPos pos, Random rand ) {
                    world.setBlockState( pos, Blocks.CAULDRON.getDefaultState(), 2 );
                }
            },
            ANVIL {
                @Override
                public void placeWorkbench( IWorld world, EnumFacing coordBase, BlockPos pos, Random rand ) {
                    IBlockState state;
                    int randDamage = rand.nextInt( 10 );
                    if( randDamage > 5 ) {
                        state = Blocks.DAMAGED_ANVIL.getDefaultState();
                    } else if( randDamage > 1 ) {
                        state = Blocks.CHIPPED_ANVIL.getDefaultState();
                    } else {
                        state = Blocks.ANVIL.getDefaultState();
                    }
                    world.setBlockState( pos, state.with( BlockAnvil.FACING, EnumFacing.Plane.HORIZONTAL.random( rand ) ), 2 );
                }
            },
            HIDDEN_TNT {
                @Override
                public void placeWorkbench( IWorld world, EnumFacing coordBase, BlockPos pos, Random rand ) {
                    world.setBlockState( pos, Blocks.STONE_PRESSURE_PLATE.getDefaultState(), 2 );
                    world.setBlockState( pos.down( 2 ), Blocks.TNT.getDefaultState(), 2 );
                }
            },
            REDSTONE {
                @Override
                public void placeWorkbench( IWorld world, EnumFacing coordBase, BlockPos pos, Random rand ) {
                    world.setBlockState( pos, Blocks.REDSTONE_BLOCK.getDefaultState(), 2 );
                }
            }
        }

        protected static class FurnaceWorkbench implements Workbench {

            protected final WeightedCollection<RandomItem> furnaceFuel = new WeightedCollection<>();
            protected final WeightedCollection<RandomItem> furnaceResult = new WeightedCollection<>();

            @Override
            public void placeWorkbench( IWorld world, EnumFacing coordBase, BlockPos pos, Random rand ) {
                IBlockState state = Blocks.FURNACE.getDefaultState().with( BlockFurnace.FACING, coordBase.getOpposite() );
                world.setBlockState( pos, state, 2 );
                TileEntity te = world.getTileEntity( pos );
                if( te instanceof TileEntityFurnace ) {
                    TileEntityFurnace furnace = (TileEntityFurnace) te;
                    furnace.setInventorySlotContents( 1, randomItem( furnaceFuel, rand ) );
                    furnace.setInventorySlotContents( 2, randomItem( furnaceResult, rand ) );
                }
            }
        }

        protected static class BrewingStandWorkbench implements Workbench {

            private final WeightedCollection<PotionEffect> potions = new WeightedCollection<>();

            @Override
            public void placeWorkbench( IWorld world, EnumFacing coordBase, BlockPos pos, Random rand ) {
                IBlockState state = Blocks.BREWING_STAND.getDefaultState();
                TileEntity te = world.getTileEntity( pos );
                if( te instanceof TileEntityBrewingStand ) {
                    TileEntityBrewingStand stand = (TileEntityBrewingStand) te;
                    int slot = rand.nextInt( 3 );
                    stand.setInventorySlotContents( slot, PotionUtils.appendEffects(
                            new ItemStack( rand.nextBoolean() ? Items.POTION : Items.SPLASH_POTION ),
                            Collections.singleton( potions.random( rand ) )
                    ) );
                    world.setBlockState( pos, state.with( BlockBrewingStand.HAS_BOTTLE[ slot ], true ), 2 );
                    if( rand.nextInt( 5 ) == 0 ) {
                        stand.setInventorySlotContents( 3, new ItemStack( Items.NETHER_WART, rand.nextInt( 4 ) + 1 ) );
                    }
                }
            }
        }

        protected static class RandomItem {

            private final IItemProvider item;
            private final int min;
            private final int max;

            public RandomItem( IItemProvider item, int min, int max ) {
                this.item = item;
                this.min = min;
                this.max = max;
            }

            public RandomItem( IItemProvider item, int amount ) {
                this( item, amount, amount );
            }

            public RandomItem( IItemProvider item ) {
                this( item, 1 );
            }

            protected ItemStack generateStack( Random rand ) {
                return new ItemStack( item, rand.nextInt( max - min + 1 ) + min );
            }
        }
    }

    protected static abstract class CobbleConfig extends AbstractConfig {
        public CobbleConfig( int biomeType ) {
            super( biomeType );

            corners.add( Picker.block( Blocks.OAK_LOG ), 1 );

            walls.add( Picker.block( Blocks.COBBLESTONE ), 12 );
            walls.add( Picker.block( Blocks.MOSSY_COBBLESTONE ), 4 );
            walls.add( Picker.block( Blocks.INFESTED_COBBLESTONE ), 1 );

            wallSlabs.add( Picker.block( Blocks.COBBLESTONE_SLAB ), 1 );

            debris.add( Picker.block( Blocks.COBBLESTONE_SLAB ), 18 );
            debris.add( Picker.randomFacing( Blocks.COBBLESTONE_STAIRS ), 10 );
            debris.add( Picker.block( Blocks.COBBLESTONE ), 3 );
            debris.add( Picker.block( Blocks.MOSSY_COBBLESTONE ), 1 );

            roofs.add( Picker.block( Blocks.COBBLESTONE ), 12 );
            roofs.add( Picker.block( Blocks.MOSSY_COBBLESTONE ), 4 );
            roofs.add( Picker.block( Blocks.INFESTED_COBBLESTONE ), 1 );

            floors.add( Picker.block( Blocks.STONE_BRICKS ), 150 );
            floors.add( Picker.block( Blocks.MOSSY_STONE_BRICKS ), 30 );
            floors.add( Picker.block( Blocks.CRACKED_STONE_BRICKS ), 30 );
            floors.add( Picker.block( Blocks.INFESTED_STONE_BRICKS ), 15 );
            floors.add( Picker.block( Blocks.INFESTED_CRACKED_STONE_BRICKS ), 3 );
            floors.add( Picker.block( Blocks.INFESTED_MOSSY_STONE_BRICKS ), 3 );

            topBlocks.add( Picker.block( Blocks.GRASS_BLOCK ), 1 );
            topBlocks.add( Picker.block( Blocks.GRASS_PATH ), 1 );

            fillerBlocks.add( Picker.block( Blocks.DIRT ), 1 );

            spawnerTypes.add( EntityType.SKELETON, 10 );
            spawnerTypes.add( EntityType.ZOMBIE, 10 );
            spawnerTypes.add( EntityType.SPIDER, 8 );
            spawnerTypes.add( EntityType.ENDERMAN, 1 );

            workbenches.add( DefaultWorkbench.CRAFTING_TABLE, 30 );
            workbenches.add( createFurnace(), 30 );
            workbenches.add( createBrewingStand(), 5 );
            workbenches.add( DefaultWorkbench.CAULDRON, 10 );
            workbenches.add( DefaultWorkbench.ANVIL, 10 );
            workbenches.add( DefaultWorkbench.HIDDEN_TNT, 10 );
            workbenches.add( DefaultWorkbench.REDSTONE, 5 );

            chestLootTables.add( MDLootTables.CURSE_RUIN_DEFAULT_CHEST, 1 );

            vines = true;
            webs = true;
        }
    }

    protected static abstract class SandstoneConfig extends AbstractConfig {
        public SandstoneConfig( int biomeType, boolean red ) {
            super( biomeType );

            corners.add( Picker.block( Blocks.SMOOTH_STONE ), 1 );

            Block sandstone = red ? Blocks.RED_SANDSTONE : Blocks.SANDSTONE;
            Block smoothSandstone = red ? Blocks.SMOOTH_RED_SANDSTONE : Blocks.SMOOTH_SANDSTONE;
            Block chiseledSandstone = red ? Blocks.CHISELED_RED_SANDSTONE : Blocks.CHISELED_SANDSTONE;
            Block cutSandstone = red ? Blocks.CUT_RED_SANDSTONE : Blocks.CUT_SANDSTONE;
            Block sandstoneSlab = red ? Blocks.RED_SANDSTONE_SLAB : Blocks.SANDSTONE_SLAB;
            Block sandstoneStairs = red ? Blocks.RED_SANDSTONE_STAIRS : Blocks.SANDSTONE_STAIRS;
            Block sand = red ? Blocks.RED_SAND : Blocks.SAND;

            walls.add( Picker.block( sandstone ), 12 );
            walls.add( Picker.block( cutSandstone ), 4 );
            walls.add( Picker.block( chiseledSandstone ), 1 );

            wallSlabs.add( Picker.block( sandstoneSlab ), 1 );

            debris.add( Picker.block( sandstoneSlab ), 18 );
            debris.add( Picker.randomFacing( sandstoneStairs ), 10 );
            debris.add( Picker.block( sandstone ), 2 );
            debris.add( Picker.block( sand ), 2 );

            roofs.add( Picker.block( sandstone ), 12 );
            roofs.add( Picker.block( cutSandstone ), 4 );
            roofs.add( Picker.block( chiseledSandstone ), 1 );

            floors.add( Picker.block( sandstone ), 3 );
            floors.add( Picker.block( cutSandstone ), 2 );
            floors.add( Picker.block( smoothSandstone ), 5 );

            topBlocks.add( Picker.block( sand ), 1 );

            fillerBlocks.add( Picker.block( sand ), 1 );

            spawnerTypes.add( EntityType.SKELETON, 10 );
            spawnerTypes.add( EntityType.ZOMBIE, 5 );
            spawnerTypes.add( EntityType.HUSK, 8 );
            spawnerTypes.add( EntityType.SPIDER, 6 );
            spawnerTypes.add( EntityType.ENDERMAN, 2 );

            workbenches.add( DefaultWorkbench.CRAFTING_TABLE, 30 );
            workbenches.add( createFurnace(), 30 );
            workbenches.add( createBrewingStand(), 5 );
            workbenches.add( DefaultWorkbench.EMPTY_CAULDRON, 10 );
            workbenches.add( DefaultWorkbench.ANVIL, 10 );
            workbenches.add( DefaultWorkbench.HIDDEN_TNT, 10 );
            workbenches.add( DefaultWorkbench.REDSTONE, 5 );

            chestLootTables.add( MDLootTables.CURSE_RUIN_DEFAULT_CHEST, 1 );

            webs = true;
        }
    }

    protected static abstract class StoneBrickConfig extends AbstractConfig {
        public StoneBrickConfig( int biomeType ) {
            super( biomeType );

            corners.add( Picker.block( Blocks.OAK_LOG ), 1 );

            walls.add( Picker.block( Blocks.STONE_BRICKS ), 120 );
            walls.add( Picker.block( Blocks.MOSSY_STONE_BRICKS ), 30 );
            walls.add( Picker.block( Blocks.CRACKED_STONE_BRICKS ), 30 );
            walls.add( Picker.block( Blocks.INFESTED_STONE_BRICKS ), 12 );
            walls.add( Picker.block( Blocks.INFESTED_MOSSY_STONE_BRICKS ), 3 );
            walls.add( Picker.block( Blocks.INFESTED_CRACKED_STONE_BRICKS ), 3 );

            wallSlabs.add( Picker.block( Blocks.STONE_BRICK_SLAB ), 1 );

            debris.add( Picker.block( Blocks.STONE_BRICK_SLAB ), 180 );
            debris.add( Picker.randomFacing( Blocks.STONE_BRICK_STAIRS ), 100 );
            debris.add( Picker.block( Blocks.CRACKED_STONE_BRICKS ), 30 );
            debris.add( Picker.block( Blocks.MOSSY_STONE_BRICKS ), 20 );
            debris.add( Picker.block( Blocks.STONE_BRICKS ), 20 );
            debris.add( Picker.block( Blocks.INFESTED_CRACKED_STONE_BRICKS ), 3 );
            debris.add( Picker.block( Blocks.INFESTED_MOSSY_STONE_BRICKS ), 2 );
            debris.add( Picker.block( Blocks.INFESTED_STONE_BRICKS ), 2 );

            roofs.add( Picker.block( Blocks.STONE_BRICKS ), 120 );
            roofs.add( Picker.block( Blocks.MOSSY_STONE_BRICKS ), 30 );
            roofs.add( Picker.block( Blocks.CRACKED_STONE_BRICKS ), 30 );
            roofs.add( Picker.block( Blocks.INFESTED_STONE_BRICKS ), 12 );
            roofs.add( Picker.block( Blocks.INFESTED_MOSSY_STONE_BRICKS ), 3 );
            roofs.add( Picker.block( Blocks.INFESTED_CRACKED_STONE_BRICKS ), 3 );

            floors.add( Picker.block( Blocks.STONE_BRICKS ), 120 );
            floors.add( Picker.block( Blocks.MOSSY_STONE_BRICKS ), 30 );
            floors.add( Picker.block( Blocks.CRACKED_STONE_BRICKS ), 30 );
            floors.add( Picker.block( Blocks.INFESTED_STONE_BRICKS ), 12 );
            floors.add( Picker.block( Blocks.INFESTED_MOSSY_STONE_BRICKS ), 3 );
            floors.add( Picker.block( Blocks.INFESTED_CRACKED_STONE_BRICKS ), 3 );

            topBlocks.add( Picker.block( Blocks.GRASS_BLOCK ), 1 );
            topBlocks.add( Picker.block( Blocks.GRASS_PATH ), 1 );

            fillerBlocks.add( Picker.block( Blocks.DIRT ), 1 );

            spawnerTypes.add( EntityType.SKELETON, 10 );
            spawnerTypes.add( EntityType.ZOMBIE, 10 );
            spawnerTypes.add( EntityType.SPIDER, 8 );
            spawnerTypes.add( EntityType.ENDERMAN, 1 );

            workbenches.add( DefaultWorkbench.CRAFTING_TABLE, 30 );
            workbenches.add( createFurnace(), 30 );
            workbenches.add( createBrewingStand(), 5 );
            workbenches.add( DefaultWorkbench.CAULDRON, 10 );
            workbenches.add( DefaultWorkbench.ANVIL, 10 );
            workbenches.add( DefaultWorkbench.HIDDEN_TNT, 10 );
            workbenches.add( DefaultWorkbench.REDSTONE, 5 );

            chestLootTables.add( MDLootTables.CURSE_RUIN_DEFAULT_CHEST, 1 );

            vines = true;
            webs = true;
        }
    }

    protected static class TaigaConfig extends CobbleConfig {
        public TaigaConfig( boolean withSnow ) {
            super( TAIGA_TYPE );
            corners.clear();
            corners.add( Picker.block( Blocks.SPRUCE_LOG ), 1 );
            snow = withSnow;
            vines = ! withSnow;

            flowerPots.add( Picker.block( Blocks.POTTED_SPRUCE_SAPLING ), 1 );
            flowerPots.add( Picker.block( Blocks.POTTED_BROWN_MUSHROOM ), 1 );
            flowerPots.add( Picker.block( Blocks.POTTED_RED_MUSHROOM ), 1 );
            flowerPots.add( Picker.block( Blocks.POTTED_FERN ), 1 );
            flowerPots.add( Picker.block( Blocks.POTTED_POPPY ), 1 );
            flowerPots.add( Picker.block( Blocks.POTTED_DANDELION ), 1 );
            flowerPots.add( Picker.block( Blocks.POTTED_DEAD_BUSH ), 1 );
            flowerPots.add( Picker.block( Blocks.FLOWER_POT ), 1 );

            topBlocks.clear();
            topBlocks.add( Picker.block( Blocks.GRASS_BLOCK ), 1 );
            if( ! withSnow ) topBlocks.add( Picker.block( Blocks.GRASS_PATH ), 1 );
            topBlocks.add( Picker.block( Blocks.PODZOL ), 2 );

            if( withSnow ) {
                spawnerTypes.add( EntityType.STRAY, 5 );
                spawnerTypes.set( EntityType.SKELETON, 5 );
            }
        }
    }

    protected static class PlainsConfig extends StoneBrickConfig {
        public PlainsConfig( boolean withSnow ) {
            super( PLAINS_TYPE );
            snow = withSnow;
            vines = ! withSnow;

            if( withSnow ) {
                corners.clear();
                corners.add( Picker.block( Blocks.SPRUCE_LOG ), 1 );
            }

            flowerPots.add( Picker.block( withSnow ? Blocks.POTTED_SPRUCE_SAPLING : Blocks.POTTED_OAK_SAPLING ), 1 );
            flowerPots.add( Picker.block( Blocks.POTTED_POPPY ), 1 );
            flowerPots.add( Picker.block( Blocks.POTTED_DANDELION ), 1 );
            if( ! withSnow ) {
                flowerPots.add( Picker.block( Blocks.POTTED_OXEYE_DAISY ), 1 );
                flowerPots.add( Picker.block( Blocks.POTTED_AZURE_BLUET ), 1 );
            }
            flowerPots.add( Picker.block( Blocks.FLOWER_POT ), 1 );

            topBlocks.clear();
            topBlocks.add( Picker.block( Blocks.GRASS_BLOCK ), 1 );
            if( ! withSnow ) topBlocks.add( Picker.block( Blocks.GRASS_PATH ), 1 );

            if( withSnow ) {
                spawnerTypes.add( EntityType.STRAY, 5 );
                spawnerTypes.set( EntityType.SKELETON, 5 );
            }
        }
    }

    protected static class SavannaConfig extends StoneBrickConfig {
        public SavannaConfig() {
            super( PLAINS_TYPE );
            corners.clear();
            corners.add( Picker.block( Blocks.ACACIA_LOG ), 1 );

            flowerPots.add( Picker.block( Blocks.POTTED_ACACIA_SAPLING ), 1 );
            flowerPots.add( Picker.block( Blocks.POTTED_POPPY ), 1 );
            flowerPots.add( Picker.block( Blocks.POTTED_DANDELION ), 1 );
            flowerPots.add( Picker.block( Blocks.POTTED_BROWN_MUSHROOM ), 1 );
            flowerPots.add( Picker.block( Blocks.POTTED_RED_MUSHROOM ), 1 );
            flowerPots.add( Picker.block( Blocks.POTTED_DEAD_BUSH ), 1 );
            flowerPots.add( Picker.block( Blocks.FLOWER_POT ), 1 );

            topBlocks.clear();
            topBlocks.add( Picker.block( Blocks.GRASS_BLOCK ), 1 );
            topBlocks.add( Picker.block( Blocks.GRASS_PATH ), 1 );

            spawnerTypes.add( EntityType.ENDERMAN, 2 );
        }
    }

    protected static class ForestConfig extends StoneBrickConfig {
        public ForestConfig() {
            super( FOREST_TYPE );

            flowerPots.add( Picker.block( Blocks.POTTED_OAK_SAPLING ), 1 );
            flowerPots.add( Picker.block( Blocks.POTTED_POPPY ), 1 );
            flowerPots.add( Picker.block( Blocks.POTTED_DANDELION ), 1 );
            flowerPots.add( Picker.block( Blocks.POTTED_OXEYE_DAISY ), 1 );
            flowerPots.add( Picker.block( Blocks.POTTED_AZURE_BLUET ), 1 );
            flowerPots.add( Picker.block( Blocks.FLOWER_POT ), 1 );
        }
    }

    protected static class BirchForestConfig extends ForestConfig {
        public BirchForestConfig() {
            corners.clear();
            corners.add( Picker.block( Blocks.BIRCH_LOG ), 1 );
        }
    }

    protected static class FlowerForestConfig extends ForestConfig {
        public FlowerForestConfig() {
            super();

            flowerPots.add( Picker.block( Blocks.POTTED_BLUE_ORCHID ), 1 );
            flowerPots.add( Picker.block( Blocks.POTTED_ALLIUM ), 1 );
            flowerPots.add( Picker.block( Blocks.POTTED_RED_TULIP ), 1 );
            flowerPots.add( Picker.block( Blocks.POTTED_ORANGE_TULIP ), 1 );
            flowerPots.add( Picker.block( Blocks.POTTED_WHITE_TULIP ), 1 );
            flowerPots.add( Picker.block( Blocks.POTTED_PINK_TULIP ), 1 );
        }
    }

    protected static class WarmConfig extends CobbleConfig {
        public WarmConfig( int treeType ) {
            super( WARM_TYPE );
            Block saplingPot = Blocks.POTTED_OAK_SAPLING;
            Block log = Blocks.OAK_LOG;
            switch( treeType ) {
                case 1:
                    saplingPot = Blocks.POTTED_JUNGLE_SAPLING;
                    log = Blocks.JUNGLE_LOG;
                    break;
                case 2:
                    saplingPot = Blocks.DARK_OAK_SAPLING;
                    log = Blocks.DARK_OAK_LOG;
                    break;
            }

            corners.clear();
            corners.add( Picker.block( log ), 1 );

            flowerPots.add( Picker.block( saplingPot ), 1 );
            flowerPots.add( Picker.block( Blocks.POTTED_POPPY ), 1 );
            flowerPots.add( Picker.block( Blocks.POTTED_DANDELION ), 1 );
            flowerPots.add( Picker.block( Blocks.POTTED_RED_MUSHROOM ), 1 );
            flowerPots.add( Picker.block( Blocks.POTTED_BROWN_MUSHROOM ), 1 );
            flowerPots.add( Picker.block( Blocks.FLOWER_POT ), 1 );
        }
    }

    protected static class DesertConfig extends SandstoneConfig {
        public DesertConfig() {
            super( DRY_TYPE, false );

            flowerPots.add( Picker.block( Blocks.POTTED_DEAD_BUSH ), 5 );
            flowerPots.add( Picker.block( Blocks.POTTED_RED_MUSHROOM ), 1 );
            flowerPots.add( Picker.block( Blocks.POTTED_BROWN_MUSHROOM ), 1 );
            flowerPots.add( Picker.block( Blocks.POTTED_CACTUS ), 5 );
            flowerPots.add( Picker.block( Blocks.FLOWER_POT ), 5 );
        }
    }

    protected static class MesaConfig extends SandstoneConfig {
        public MesaConfig() {
            super( DRY_TYPE, true );

            flowerPots.add( Picker.block( Blocks.POTTED_DEAD_BUSH ), 5 );
            flowerPots.add( Picker.block( Blocks.POTTED_RED_MUSHROOM ), 1 );
            flowerPots.add( Picker.block( Blocks.POTTED_BROWN_MUSHROOM ), 1 );
            flowerPots.add( Picker.block( Blocks.POTTED_CACTUS ), 5 );
            flowerPots.add( Picker.block( Blocks.FLOWER_POT ), 5 );
        }
    }

    protected interface Picker extends IPicker<IBlockState> {
        IBlockState random( Random rand );

        static Picker block( Block b ) {
            return r -> b.getDefaultState();
        }

        static Picker randomFacing( Block b ) {
            return r -> b.getDefaultState().with( BlockHorizontal.HORIZONTAL_FACING, EnumFacing.Plane.HORIZONTAL.random( r ) );
        }

        default <T extends Comparable<T>> Picker applyProperty( IProperty<T> prop, T value ) {
            return r -> random( r ).with( prop, value );
        }

        static StructurePiece.BlockSelector selector( Picker picker ) {
            return new StructurePiece.BlockSelector() {
                @Override
                public void selectBlocks( Random rand, int x, int y, int z, boolean wall ) {
                    blockstate = picker.random( rand );
                }
            };
        }
    }
}
