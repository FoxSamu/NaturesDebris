package modernity.common.block;

import modernity.api.block.IColoredBlock;
import modernity.common.block.base.*;
import modernity.common.fluid.MDFluids;
import modernity.common.item.MDItemGroups;
import modernity.common.registry.RegistryEventHandler;
import modernity.common.registry.RegistryHandler;
import modernity.common.world.gen.feature.MDFeatures;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.Minecraft;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;

/**
 * Object holder for modernity blocks.
 */
@ObjectHolder( "modernity" )
public final class MDBlocks {
    private static final ArrayList<Block> ITEM_BLOCKS = new ArrayList<>();
    private static final RegistryHandler<Block> BLOCKS = new RegistryHandler<>( "modernity" );
    private static final RegistryHandler<Item> ITEMS = new RegistryHandler<>( "modernity" );

    // V I0.2.0
    public static final Block ROCK = blockItem( "rock", new Block( rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroups.BLOCKS );
    public static final Block DARKROCK = blockItem( "darkrock", new Block( rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroups.BLOCKS );
    public static final Block LIGHTROCK = blockItem( "lightrock", new Block( rock( MaterialColor.SNOW, 1.5, 6 ).lightValue( 15 ) ), MDItemGroups.BLOCKS );
    public static final Block REDROCK = blockItem( "redrock", new Block( rock( MaterialColor.TNT, 1.5, 6 ) ), MDItemGroups.BLOCKS );
    public static final Block MODERN_BEDROCK = blockItem( "modern_bedrock", new Block( rock( MaterialColor.BLACK, - 1, 3600000 ) ), MDItemGroups.BLOCKS );
    public static final Block BASALT = blockItem( "basalt", new Block( rock( MaterialColor.BLACK, 3, 9 ) ), MDItemGroups.BLOCKS );
    public static final Block LIMESTONE = blockItem( "limestone", new Block( rock( MaterialColor.WHITE_TERRACOTTA, 1, 4 ) ), MDItemGroups.BLOCKS );

    public static final DirtBlock DARK_DIRT = blockItem( "dark_dirt", new DirtBlock( DirtBlock.TYPE_DIRT, dirt( MaterialColor.DIRT, false ) ), MDItemGroups.BLOCKS );
    public static final DirtBlock DARK_GRASS_BLOCK = blockItem( "dark_grass_block", new DirtBlock.ColoredGrass( DirtBlock.TYPE_GRASS, dirt( MaterialColor.GRASS, true ) ), MDItemGroups.BLOCKS );
    public static final DigableFallBlock DARK_SAND = blockItem( "dark_sand", new DigableFallBlock( 0x584c39, dust( MaterialColor.SAND, true ) ), MDItemGroups.BLOCKS );
    public static final DigableBlock DARK_CLAY = blockItem( "dark_clay", new DigableBlock( clay( MaterialColor.GRAY_TERRACOTTA ) ), MDItemGroups.BLOCKS );
    public static final DirtBlock COARSE_DARK_DIRT = blockItem( "coarse_dark_dirt", new DirtBlock( DirtBlock.TYPE_DIRT, dirt( MaterialColor.DIRT, false ) ), MDItemGroups.BLOCKS );
    public static final DirtBlock HUMUS = blockItem( "humus", new DirtBlock( DirtBlock.TYPE_HUMUS, dirt( MaterialColor.ORANGE_TERRACOTTA, true ) ), MDItemGroups.BLOCKS );
    public static final DirtBlock DARK_PODZOL = blockItem( "dark_podzol", new DirtBlock( DirtBlock.TYPE_PODZOL, dirt( MaterialColor.ORANGE_TERRACOTTA, true ) ), MDItemGroups.BLOCKS );
    public static final StickyBlock MUD = blockItem( "mud", new StickyBlock.Digable( dirt( MaterialColor.BROWN_TERRACOTTA, false ) ), MDItemGroups.BLOCKS );
    public static final DigableFallBlock ROCK_GRAVEL = blockItem( "rock_gravel", new DigableFallBlock( 0x584c39, dust( MaterialColor.STONE, true ) ), MDItemGroups.BLOCKS );

    public static final RegularFluidBlock MODERNIZED_WATER = blockOnly( "modernized_water", new RegularFluidBlock( MDFluids.MODERNIZED_WATER, fluid( Material.WATER, MaterialColor.WATER ) ) );
    public static final RegularFluidBlock HEATROCK = blockOnly( "heatrock", new RegularFluidBlock( MDFluids.HEATROCK, fluid( Material.LAVA, MaterialColor.TNT ).lightValue( 15 ) ) );
    public static final RegularFluidBlock OIL = blockOnly( "oil", new OilFluidBlock( MDFluids.OIL, fluid( MDMaterial.OIL, MaterialColor.BLACK ) ) );

    public static final Block ROCK_BRICKS = blockItem( "rock_bricks", new Block( rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroups.BLOCKS );
    public static final Block DARKROCK_BRICKS = blockItem( "darkrock_bricks", new Block( rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroups.BLOCKS );
    public static final Block ASPHALT_CONCRETE = blockItem( "asphalt_concrete", new Block( asphalt() ), MDItemGroups.BLOCKS );

    public static final VerticalSlabBlock ROCK_SLAB = blockItem( "rock_slab", new VerticalSlabBlock( rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroups.BLOCKS );
    public static final VerticalSlabBlock DARKROCK_SLAB = blockItem( "darkrock_slab", new VerticalSlabBlock( rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroups.BLOCKS );
    public static final VerticalSlabBlock LIMESTONE_SLAB = blockItem( "limestone_slab", new VerticalSlabBlock( rock( MaterialColor.WHITE_TERRACOTTA, 1, 4 ) ), MDItemGroups.BLOCKS );
    public static final VerticalSlabBlock ROCK_BRICKS_SLAB = blockItem( "rock_bricks_slab", new VerticalSlabBlock( rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroups.BLOCKS );
    public static final VerticalSlabBlock DARKROCK_BRICKS_SLAB = blockItem( "darkrock_bricks_slab", new VerticalSlabBlock( rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroups.BLOCKS );
    public static final VerticalSlabBlock ASPHALT_CONCRETE_SLAB = blockItem( "asphalt_concrete_slab", new VerticalSlabBlock( asphalt() ), MDItemGroups.BLOCKS );

    public static final StairsBlock ROCK_STAIRS = blockItem( "rock_stairs", new StairsBlock( false, rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroups.BLOCKS );
    public static final StairsBlock DARKROCK_STAIRS = blockItem( "darkrock_stairs", new StairsBlock( false, rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroups.BLOCKS );
    public static final StairsBlock LIMESTONE_STAIRS = blockItem( "limestone_stairs", new StairsBlock( false, rock( MaterialColor.WHITE_TERRACOTTA, 1, 4 ) ), MDItemGroups.BLOCKS );
    public static final StairsBlock ROCK_BRICKS_STAIRS = blockItem( "rock_bricks_stairs", new StairsBlock( false, rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroups.BLOCKS );
    public static final StairsBlock DARKROCK_BRICKS_STAIRS = blockItem( "darkrock_bricks_stairs", new StairsBlock( false, rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroups.BLOCKS );
    public static final StairsBlock ASPHALT_CONCRETE_STAIRS = blockItem( "asphalt_concrete_stairs", new StairsBlock( false, asphalt() ), MDItemGroups.BLOCKS );

    public static final StairsBlock ROCK_STEP = blockItem( "rock_step", new StairsBlock( true, rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroups.BLOCKS );
    public static final StairsBlock DARKROCK_STEP = blockItem( "darkrock_step", new StairsBlock( true, rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroups.BLOCKS );
    public static final StairsBlock LIMESTONE_STEP = blockItem( "limestone_step", new StairsBlock( true, rock( MaterialColor.WHITE_TERRACOTTA, 1, 4 ) ), MDItemGroups.BLOCKS );
    public static final StairsBlock ROCK_BRICKS_STEP = blockItem( "rock_bricks_step", new StairsBlock( true, rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroups.BLOCKS );
    public static final StairsBlock DARKROCK_BRICKS_STEP = blockItem( "darkrock_bricks_step", new StairsBlock( true, rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroups.BLOCKS );
    public static final StairsBlock ASPHALT_CONCRETE_STEP = blockItem( "asphalt_concrete_step", new StairsBlock( true, asphalt() ), MDItemGroups.BLOCKS );

    public static final CornerBlock ROCK_CORNER = blockItem( "rock_corner", new CornerBlock( rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroups.BLOCKS );
    public static final CornerBlock DARKROCK_CORNER = blockItem( "darkrock_corner", new CornerBlock( rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroups.BLOCKS );
    public static final CornerBlock LIMESTONE_CORNER = blockItem( "limestone_corner", new CornerBlock( rock( MaterialColor.WHITE_TERRACOTTA, 1, 4 ) ), MDItemGroups.BLOCKS );
    public static final CornerBlock ROCK_BRICKS_CORNER = blockItem( "rock_bricks_corner", new CornerBlock( rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroups.BLOCKS );
    public static final CornerBlock DARKROCK_BRICKS_CORNER = blockItem( "darkrock_bricks_corner", new CornerBlock( rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroups.BLOCKS );
    public static final CornerBlock ASPHALT_CONCRETE_CORNER = blockItem( "asphalt_concrete_corner", new CornerBlock( asphalt() ), MDItemGroups.BLOCKS );

    public static final WallBlock ROCK_WALL = blockItem( "rock_wall", new WallBlock( rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroups.DECORATIVES );
    public static final WallBlock DARKROCK_WALL = blockItem( "darkrock_wall", new WallBlock( rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroups.DECORATIVES );
    public static final WallBlock LIMESTONE_WALL = blockItem( "limestone_wall", new WallBlock( rock( MaterialColor.WHITE_TERRACOTTA, 1, 4 ) ), MDItemGroups.DECORATIVES );
    public static final WallBlock ROCK_BRICKS_WALL = blockItem( "rock_bricks_wall", new WallBlock( rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroups.DECORATIVES );
    public static final WallBlock DARKROCK_BRICKS_WALL = blockItem( "darkrock_bricks_wall", new WallBlock( rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroups.DECORATIVES );
    public static final WallBlock ASPHALT_CONCRETE_WALL = blockItem( "asphalt_concrete_wall", new WallBlock( asphalt() ), MDItemGroups.DECORATIVES );

    public static final Block BLACKWOOD_PLANKS = blockItem( "blackwood_planks", new Block( wood( MaterialColor.BLACK_TERRACOTTA ) ), MDItemGroups.BLOCKS );
    public static final Block INVER_PLANKS = blockItem( "inver_planks", new Block( wood( MaterialColor.WOOD ) ), MDItemGroups.BLOCKS );

    public static final VerticalSlabBlock BLACKWOOD_SLAB = blockItem( "blackwood_slab", new VerticalSlabBlock( wood( MaterialColor.BLACK_TERRACOTTA ) ), MDItemGroups.BLOCKS );
    public static final VerticalSlabBlock INVER_SLAB = blockItem( "inver_slab", new VerticalSlabBlock( wood( MaterialColor.WOOD ) ), MDItemGroups.BLOCKS );

    public static final StairsBlock BLACKWOOD_STAIRS = blockItem( "blackwood_stairs", new StairsBlock( false, wood( MaterialColor.BLACK_TERRACOTTA ) ), MDItemGroups.BLOCKS );
    public static final StairsBlock INVER_STAIRS = blockItem( "inver_stairs", new StairsBlock( false, wood( MaterialColor.WOOD ) ), MDItemGroups.BLOCKS );

    public static final StairsBlock BLACKWOOD_STEP = blockItem( "blackwood_step", new StairsBlock( true, wood( MaterialColor.BLACK_TERRACOTTA ) ), MDItemGroups.BLOCKS );
    public static final StairsBlock INVER_STEP = blockItem( "inver_step", new StairsBlock( true, wood( MaterialColor.WOOD ) ), MDItemGroups.BLOCKS );

    public static final CornerBlock BLACKWOOD_CORNER = blockItem( "blackwood_corner", new CornerBlock( wood( MaterialColor.BLACK_TERRACOTTA ) ), MDItemGroups.BLOCKS );
    public static final CornerBlock INVER_CORNER = blockItem( "inver_corner", new CornerBlock( wood( MaterialColor.WOOD ) ), MDItemGroups.BLOCKS );

    public static final FenceBlock BLACKWOOD_FENCE = blockItem( "blackwood_fence", new FenceBlock( wood( MaterialColor.BLACK_TERRACOTTA ) ), MDItemGroups.DECORATIVES );
    public static final FenceBlock INVER_FENCE = blockItem( "inver_fence", new FenceBlock( wood( MaterialColor.WOOD ) ), MDItemGroups.DECORATIVES );

    public static final DoorBlock BLACKWOOD_DOOR = blockItem( "blackwood_door", new DoorBlock( wood( MaterialColor.BLACK_TERRACOTTA ) ), MDItemGroups.DECORATIVES );
    public static final DoorBlock INVER_DOOR = blockItem( "inver_door", new DoorBlock( wood( MaterialColor.WOOD ) ), MDItemGroups.DECORATIVES );
    public static final DoorBlock ALUMINIUM_DOOR = blockItem( "aluminium_door", new DoorBlock( metal( MaterialColor.IRON ) ), MDItemGroups.DECORATIVES );

    public static final FenceGateBlock BLACKWOOD_FENCE_GATE = blockItem( "blackwood_fence_gate", new FenceGateBlock( wood( MaterialColor.BLACK_TERRACOTTA ) ), MDItemGroups.DECORATIVES );
    public static final FenceGateBlock INVER_FENCE_GATE = blockItem( "inver_fence_gate", new FenceGateBlock( wood( MaterialColor.WOOD ) ), MDItemGroups.DECORATIVES );

    public static final GlassBlock DARK_GLASS = blockItem( "dark_glass", new GlassBlock( glass() ), MDItemGroups.BLOCKS );
    public static final PaneBlock DARK_GLASS_PANE = blockItem( "dark_glass_pane", new PaneBlock( glass() ), MDItemGroups.DECORATIVES );

    public static final FallBlock ASH_BLOCK = blockItem( "ash_block", new DigableFallBlock( 0x333333, ash( MaterialColor.GRAY ) ), MDItemGroups.BLOCKS );
    public static final FallBlock SALT_DUST_BLOCK = blockItem( "salt_dust_block", new DigableFallBlock( 0xffffff, dust( MaterialColor.SNOW, false ) ), MDItemGroups.BLOCKS );

    public static final TallPlantBlock DARK_TALLGRASS = blockItem( "dark_tall_grass", new TallPlantBlock.ColoredGrass( weakPlant( MaterialColor.GRASS, 0 ) ), MDItemGroups.PLANTS );
    public static final TallWaterloggingPlantBlock REEDS = blockItem( "reeds", new TallWaterloggingPlantBlock.Reeds( weakPlant( MaterialColor.GRASS, 0 ) ), MDItemGroups.PLANTS );

    public static final SinglePlantBlock RED_MELION = blockItem( "melion/red", new SinglePlantBlock.Melion( weakPlant( MaterialColor.GRASS, 0 ) ), MDItemGroups.PLANTS );
    public static final SinglePlantBlock ORANGE_MELION = blockItem( "melion/orange", new SinglePlantBlock.Melion( weakPlant( MaterialColor.GRASS, 0 ) ), MDItemGroups.PLANTS );
    public static final SinglePlantBlock YELLOW_MELION = blockItem( "melion/yellow", new SinglePlantBlock.Melion( weakPlant( MaterialColor.GRASS, 0 ) ), MDItemGroups.PLANTS );
    public static final SinglePlantBlock WHITE_MELION = blockItem( "melion/white", new SinglePlantBlock.Melion( weakPlant( MaterialColor.GRASS, 0 ) ), MDItemGroups.PLANTS );
    public static final SinglePlantBlock BLUE_MELION = blockItem( "melion/blue", new SinglePlantBlock.Melion( weakPlant( MaterialColor.GRASS, 0 ) ), MDItemGroups.PLANTS );
    public static final SinglePlantBlock INDIGO_MELION = blockItem( "melion/indigo", new SinglePlantBlock.Melion( weakPlant( MaterialColor.GRASS, 0 ) ), MDItemGroups.PLANTS );
    public static final SinglePlantBlock MAGENTA_MELION = blockItem( "melion/magenta", new SinglePlantBlock.Melion( weakPlant( MaterialColor.GRASS, 0 ) ), MDItemGroups.PLANTS );

    public static final SinglePlantBlock RED_MILLIUM = blockItem( "millium/red", new SinglePlantBlock.Millium( weakPlant( MaterialColor.GRASS, 0 ) ), MDItemGroups.PLANTS );
    public static final SinglePlantBlock YELLOW_MILLIUM = blockItem( "millium/yellow", new SinglePlantBlock.Millium( weakPlant( MaterialColor.GRASS, 0 ) ), MDItemGroups.PLANTS );
    public static final SinglePlantBlock WHITE_MILLIUM = blockItem( "millium/white", new SinglePlantBlock.Millium( weakPlant( MaterialColor.GRASS, 0 ) ), MDItemGroups.PLANTS );
    public static final SinglePlantBlock CYAN_MILLIUM = blockItem( "millium/cyan", new SinglePlantBlock.Millium( weakPlant( MaterialColor.GRASS, 0 ) ), MDItemGroups.PLANTS );
    public static final SinglePlantBlock GREEN_MILLIUM = blockItem( "millium/green", new SinglePlantBlock.Millium( weakPlant( MaterialColor.GRASS, 0 ) ), MDItemGroups.PLANTS );
    public static final SinglePlantBlock MAGENTA_MILLIUM = blockItem( "millium/magenta", new SinglePlantBlock.Millium( weakPlant( MaterialColor.GRASS, 0 ) ), MDItemGroups.PLANTS );
    public static final SinglePlantBlock BLUE_MILLIUM = blockItem( "millium/blue", new SinglePlantBlock.Millium( weakPlant( MaterialColor.GRASS, 0 ) ), MDItemGroups.PLANTS );

    public static final SinglePlantBlock MINT_PLANT = blockItem( "mint_plant", new SinglePlantBlock.Mint( weakPlant( MaterialColor.GRASS, 0.2 ) ), MDItemGroups.PLANTS );
    public static final SinglePlantBlock REDWOLD = blockItem( "redwold", new SinglePlantBlock.Redwold( weakPlant( MaterialColor.GRASS, 0 ) ), MDItemGroups.PLANTS );
    public static final DangerousPlantBlock NETTLES = blockItem( "nettles", new DangerousPlantBlock.Nettles( weakPlant( MaterialColor.GRASS, 0.5 ) ), MDItemGroups.PLANTS );
    public static final SaltCrystalBlock SALT_CRYSTAL = blockItem( "salt_crystal", new SaltCrystalBlock( crystal( MaterialColor.SNOW, 0.2 ) ), MDItemGroups.PLANTS );
    public static final HangingPlantBlock MURINA = blockItem( "murina", new HangingPlantBlock.ColoredMurina( weakPlant( MaterialColor.GRASS, 0 ) ), MDItemGroups.PLANTS );

    public static final AxisBlock STRIPPED_BLACKWOOD_LOG = blockItem( "stripped_blackwood_log", new AxisBlock( wood( MaterialColor.BLACK_TERRACOTTA ) ), MDItemGroups.BLOCKS );
    public static final AxisBlock BLACKWOOD_LOG = blockItem( "blackwood_log", new StripableLogBlock( () -> STRIPPED_BLACKWOOD_LOG, wood( MaterialColor.BLACK_TERRACOTTA ) ), MDItemGroups.BLOCKS );
    public static final Block STRIPPED_BLACKWOOD = blockItem( "stripped_blackwood", new Block( wood( MaterialColor.BLACK_TERRACOTTA ) ), MDItemGroups.BLOCKS );
    public static final Block BLACKWOOD = blockItem( "blackwood", new StripableBlock( () -> STRIPPED_BLACKWOOD, wood( MaterialColor.BLACK_TERRACOTTA ) ), MDItemGroups.BLOCKS );
    public static final SaplingBlock BLACKWOOD_SAPLING = blockItem( "blackwood_sapling", new SaplingBlock( () -> MDFeatures.BLACKWOOD_TREE::generate, strongPlant( MaterialColor.GRASS, 0 ) ), MDItemGroups.PLANTS );
    public static final HangLeavesBlock BLACKWOOD_LEAVES = blockItem( "blackwood_leaves", new HangLeavesBlock.ColoredBlackwood( MDBlockTags.BLACKWOOD_LOG, leaves( MaterialColor.FOLIAGE, 0.2 ) ), MDItemGroups.PLANTS );

    public static final AxisBlock STRIPPED_INVER_LOG = blockItem( "stripped_inver_log", new AxisBlock( wood( MaterialColor.WOOD ) ), MDItemGroups.BLOCKS );
    public static final AxisBlock INVER_LOG = blockItem( "inver_log", new StripableLogBlock( () -> STRIPPED_INVER_LOG, wood( MaterialColor.WOOD ) ), MDItemGroups.BLOCKS );
    public static final Block STRIPPED_INVER = blockItem( "stripped_inver_wood", new Block( wood( MaterialColor.WOOD ) ), MDItemGroups.BLOCKS );
    public static final Block INVER = blockItem( "inver_wood", new StripableBlock( () -> STRIPPED_INVER, wood( MaterialColor.WOOD ) ), MDItemGroups.BLOCKS );
    public static final SaplingBlock INVER_SAPLING = blockItem( "inver_sapling", new SaplingBlock( () -> MDFeatures.INVER_TREE::generate, strongPlant( MaterialColor.GRASS, 0 ) ), MDItemGroups.PLANTS );
    public static final DecayLeavesBlock INVER_LEAVES = blockItem( "inver_leaves", new DecayLeavesBlock.ColoredInver( MDBlockTags.INVER_LOG, leaves( MaterialColor.FOLIAGE, 0.2 ) ), MDItemGroups.PLANTS );

    public static final Block SALT_ORE = blockItem( "salt_ore", new Block( Block.Properties.create( Material.ROCK, MaterialColor.STONE ).hardnessAndResistance( 3F, 3F ).sound( SoundType.STONE ) ), MDItemGroups.BLOCKS );
    public static final Block ALUMINIUM_ORE = blockItem( "aluminium_ore", new Block( Block.Properties.create( Material.ROCK, MaterialColor.STONE ).hardnessAndResistance( 3F, 3F ).sound( SoundType.STONE ) ), MDItemGroups.BLOCKS );
    public static final Block ANTHRACITE_ORE = blockItem( "anthracite_ore", new Block( Block.Properties.create( Material.ROCK, MaterialColor.STONE ).hardnessAndResistance( 3F, 3F ).sound( SoundType.STONE ) ), MDItemGroups.BLOCKS );

    public static final TranslucentBlock SALT_BLOCK = blockItem( "salt_block", new TranslucentBlock.Salt( Block.Properties.create( Material.GLASS, MaterialColor.SNOW ).hardnessAndResistance( 0.3F ).sound( SoundType.GLASS ) ), MDItemGroups.BLOCKS );
    public static final Block ALUMINIUM_BLOCK = blockItem( "aluminium_block", new Block( Block.Properties.create( Material.IRON, MaterialColor.IRON ).hardnessAndResistance( 5F, 6F ).sound( SoundType.METAL ) ), MDItemGroups.BLOCKS );
    public static final Block ANTHRACITE_BLOCK = blockItem( "anthracite_block", new Block( Block.Properties.create( Material.ROCK, MaterialColor.BLACK ).hardnessAndResistance( 3F, 6F ).sound( SoundType.STONE ) ), MDItemGroups.BLOCKS );

    public static final TorchBlock EXTINGUISHED_ANTHRACITE_TORCH = blockItem( "extinguished_anthracite_torch", new TorchBlock( false, Block.Properties.create( Material.MISCELLANEOUS, MaterialColor.SNOW ).hardnessAndResistance( 0 ).doesNotBlockMovement().sound( SoundType.WOOD ) ), MDItemGroups.DECORATIVES );
    public static final ExtinguishableTorchBlock ANTHRACITE_TORCH = blockItem( "anthracite_torch", new ExtinguishableTorchBlock( true, EXTINGUISHED_ANTHRACITE_TORCH, Block.Properties.create( Material.MISCELLANEOUS, MaterialColor.SNOW ).doesNotBlockMovement().hardnessAndResistance( 0 ).lightValue( 15 ).sound( SoundType.WOOD ) ), MDItemGroups.DECORATIVES );
    public static final TorchBlock LIGHTROCK_TORCH = blockItem( "lightrock_torch", new TorchBlock( false, Block.Properties.create( Material.MISCELLANEOUS, MaterialColor.SNOW ).hardnessAndResistance( 0 ).doesNotBlockMovement().lightValue( 15 ).sound( SoundType.METAL ) ), MDItemGroups.DECORATIVES );

    public static final NetherAltarBlock NETHER_ALTAR = blockItem( "nether_altar", new NetherAltarBlock( Block.Properties.create( Material.ROCK, MaterialColor.STONE ).hardnessAndResistance( 2F ) ), MDItemGroups.DECORATIVES );
    public static final HorizontalFacingBlock GOLD_CARVED_NETHER_BRICKS_NATURE = blockItem( "gold_carved_nether_bricks/nature", new HorizontalFacingBlock( Block.Properties.create( Material.ROCK, MaterialColor.NETHERRACK ).hardnessAndResistance( 2, 6 ) ), MDItemGroups.DECORATIVES );
    public static final HorizontalFacingBlock GOLD_CARVED_NETHER_BRICKS_CURSE = blockItem( "gold_carved_nether_bricks/curse", new HorizontalFacingBlock( Block.Properties.create( Material.ROCK, MaterialColor.NETHERRACK ).hardnessAndResistance( 2, 6 ) ), MDItemGroups.DECORATIVES );
    public static final HorizontalFacingBlock GOLD_CARVED_NETHER_BRICKS_CYEN = blockItem( "gold_carved_nether_bricks/cyen", new HorizontalFacingBlock( Block.Properties.create( Material.ROCK, MaterialColor.NETHERRACK ).hardnessAndResistance( 2, 6 ) ), MDItemGroups.DECORATIVES );
    public static final HorizontalFacingBlock GOLD_CARVED_NETHER_BRICKS_FYREN = blockItem( "gold_carved_nether_bricks/fyren", new HorizontalFacingBlock( Block.Properties.create( Material.ROCK, MaterialColor.NETHERRACK ).hardnessAndResistance( 2, 6 ) ), MDItemGroups.DECORATIVES );
    public static final HorizontalFacingBlock GOLD_CARVED_NETHER_BRICKS_TIMEN = blockItem( "gold_carved_nether_bricks/timen", new HorizontalFacingBlock( Block.Properties.create( Material.ROCK, MaterialColor.NETHERRACK ).hardnessAndResistance( 2, 6 ) ), MDItemGroups.DECORATIVES );
    public static final HorizontalFacingBlock GOLD_CARVED_NETHER_BRICKS_PORTAL = blockItem( "gold_carved_nether_bricks/portal", new HorizontalFacingBlock( Block.Properties.create( Material.ROCK, MaterialColor.NETHERRACK ).hardnessAndResistance( 2, 6 ) ), MDItemGroups.DECORATIVES );
    public static final HorizontalFacingBlock GOLD_CARVED_NETHER_BRICKS_RGSW = blockItem( "gold_carved_nether_bricks/rgsw", new HorizontalFacingBlock( Block.Properties.create( Material.ROCK, MaterialColor.NETHERRACK ).hardnessAndResistance( 2, 6 ) ), MDItemGroups.DECORATIVES );

    private static <T extends Block> T blockOnly( String name, T block, String... aliases ) {
        BLOCKS.register( name, block, aliases );
        return block;
    }

    private static <T extends Block> T blockItem( String name, T block, Item.Properties itemProps, String... aliases ) {
        BLOCKS.register( name, block, aliases );
        ITEMS.register( name, createBlockItem( block, itemProps ), aliases );
        ITEM_BLOCKS.add( block );
        return block;
    }

    private static <T extends Block> T blockItem( String name, T block, ItemGroup group, String... aliases ) {
        BLOCKS.register( name, block, aliases );
        ITEMS.register( name, createBlockItem( block, new Item.Properties().group( group ) ), aliases );
        ITEM_BLOCKS.add( block );
        return block;
    }

    private static <T extends Block> T blockItem( String name, T block, String... aliases ) {
        BLOCKS.register( name, block, aliases );
        ITEMS.register( name, createBlockItem( block, new Item.Properties() ), aliases );
        ITEM_BLOCKS.add( block );
        return block;
    }

    /**
     * Registers the block and item registry handlers to the {@link RegistryEventHandler}. Should be called internally
     * only by the {@link RegistryEventHandler}.
     */
    public static void setup( RegistryEventHandler handler ) {
        handler.addHandler( Block.class, BLOCKS );
        handler.addHandler( Item.class, ITEMS );
    }

    /**
     * Registers all colored blocks (blocks that implement {@link IColoredBlock}).
     */
    @OnlyIn( Dist.CLIENT )
    public static void initBlockColors() {
        for( Block block : BLOCKS ) {
            if( block instanceof IColoredBlock ) {
                Minecraft.getInstance().getBlockColors().register( ( (IColoredBlock) block )::colorMultiplier, block );
            }
        }
        for( Block block : ITEM_BLOCKS ) {
            if( block instanceof IColoredBlock ) {
                Minecraft.getInstance().getItemColors().register( ( (IColoredBlock) block )::colorMultiplier, block );
            }
        }
    }

    private static Item createBlockItem( Block t, Item.Properties props ) {
        if( t instanceof ICustomBlockItem ) {
            return ( (ICustomBlockItem) t ).createBlockItem( props );
        }
        return new BlockItem( t, props );
    }

    private static Block.Properties rock( MaterialColor color, double hardness, double resistance ) {
        return Block.Properties.create( Material.ROCK, color )
                               .hardnessAndResistance( (float) hardness, (float) resistance )
                               .sound( SoundType.STONE );
    }

    private static Block.Properties asphalt() {
        return Block.Properties.create( Material.ROCK, MaterialColor.BLACK )
                               .hardnessAndResistance( 1, 4 )
                               .sound( SoundType.STONE );
    }

    private static Block.Properties glass() {
        return Block.Properties.create( Material.GLASS, MaterialColor.GRAY )
                               .hardnessAndResistance( 0.3F )
                               .sound( SoundType.GLASS );
    }

    private static Block.Properties dirt( MaterialColor color, boolean overgrown ) {
        return Block.Properties.create( Material.EARTH, color )
                               .hardnessAndResistance( overgrown ? 0.6F : 0.5F )
                               .sound( overgrown ? SoundType.PLANT : SoundType.GROUND );
    }

    private static Block.Properties dust( MaterialColor color, boolean gravel ) {
        return Block.Properties.create( ! gravel ? Material.SAND : Material.EARTH, color )
                               .hardnessAndResistance( gravel ? 0.6F : 0.5F )
                               .sound( ! gravel ? SoundType.SAND : SoundType.GROUND );
    }

    private static Block.Properties ash( MaterialColor color ) {
        return Block.Properties.create( MDMaterial.ASH, color )
                               .hardnessAndResistance( 0.5F )
                               .sound( SoundType.SAND );
    }

    private static Block.Properties clay( MaterialColor color ) {
        return Block.Properties.create( Material.CLAY, color )
                               .hardnessAndResistance( 0.5F )
                               .sound( SoundType.GROUND );
    }

    private static Block.Properties fluid( Material mat, MaterialColor color ) {
        return Block.Properties.create( mat, color )
                               .hardnessAndResistance( 100F )
                               .doesNotBlockMovement();
    }

    private static Block.Properties wood( MaterialColor color ) {
        return Block.Properties.create( Material.WOOD, color ).hardnessAndResistance( 2, 3 ).sound( SoundType.WOOD );
    }

    private static Block.Properties metal( MaterialColor color ) {
        return Block.Properties.create( Material.IRON, color ).hardnessAndResistance( 5, 6 ).sound( SoundType.METAL );
    }

    private static Block.Properties weakPlant( MaterialColor color, double hardness ) {
        return Block.Properties.create( Material.TALL_PLANTS, color ).hardnessAndResistance( (float) hardness ).doesNotBlockMovement().sound( SoundType.PLANT );
    }

    private static Block.Properties strongPlant( MaterialColor color, double hardness ) {
        return Block.Properties.create( Material.PLANTS, color ).hardnessAndResistance( (float) hardness ).doesNotBlockMovement().sound( SoundType.PLANT );
    }

    private static Block.Properties crystal( MaterialColor color, double hardness ) {
        return Block.Properties.create( MDMaterial.CRYSTAL, color ).hardnessAndResistance( (float) hardness ).doesNotBlockMovement().sound( SoundType.GLASS );
    }

    private static Block.Properties leaves( MaterialColor color, double hardness ) {
        return Block.Properties.create( Material.LEAVES, color ).hardnessAndResistance( (float) hardness ).sound( SoundType.PLANT );
    }

    private MDBlocks() {
    }
}
