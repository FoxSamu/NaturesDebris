/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 23 - 2020
 * Author: rgsw
 */

package modernity.common.block;

import com.google.common.collect.Lists;
import modernity.api.IModernity;
import modernity.api.block.IColoredBlock;
import modernity.api.data.IRecipeData;
import modernity.common.block.base.ICustomBlockItem;
import modernity.common.block.loot.IBlockDrops;
import modernity.common.block.loot.MDBlockDrops;
import modernity.common.recipes.data.RecipeDataTypes;
import modernity.common.registry.RegistryEventHandler;
import modernity.common.registry.RegistryHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.Minecraft;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tags.Tag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.registries.ForgeRegistries;
import net.redgalaxy.util.Lazy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Object holder for modernity blocks.
 */
public final class MDBlocks {
    private static final ArrayList<Block> ITEM_BLOCKS = new ArrayList<>();
    private static final RegistryHandler<Block> BLOCKS = new RegistryHandler<>( "modernity" );
    private static final RegistryHandler<Item> ITEMS = new RegistryHandler<>( "modernity" );

    static {
        MDNatureBlocks.init();
        MDTreeBlocks.init();
        MDBuildingBlocks.init();
        MDPlantBlocks.init();
        MDMineralBlocks.init();
    }

    public static <T extends Block> BlockConfig<T> block( String name, T block ) {
        return new BlockConfig<T>( name ).block( block );
    }

    public static BlockConfig<Block> simple( String name ) {
        return new BlockConfig<>( name ).block( Block::new );
    }

    public static <T extends Block> BlockConfig<T> function( String name, Function<Block.Properties, ? extends T> block ) {
        return new BlockConfig<T>( name ).block( block );
    }

    public static Supplier<BlockState> supply( String id ) {
        return new BlockStateSupplier( new ResourceLocation( "modernity", id ) );
    }

    public static Supplier<IItemProvider> supplyI( String id ) {
        return Lazy.of( () -> ForgeRegistries.ITEMS.getValue( new ResourceLocation( "modernity", id ) ) );
    }

    private static Item createBlockItem( Block t, Item.Properties props ) {
        if( t instanceof ICustomBlockItem ) {
            return ( (ICustomBlockItem) t ).createBlockItem( props );
        }
        return new BlockItem( t, props );
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


    private MDBlocks() {
    }

    public static class BlockConfig<T extends Block> {
        private String id;
        private Function<Block.Properties, ? extends T> blockFunc;
        private BiFunction<? super T, Item.Properties, Item> itemFunc;
        private Item.Properties itemProps;
        private Block.Properties blockProps;
        private IBlockDrops drops;
        private final List<String> aliases = Lists.newArrayList();
        private final List<IRecipeData> recipes = Lists.newArrayList();

        BlockConfig( String id ) {
            this.id = id;
        }

        private BlockConfig<T> block( T block ) {
            blockFunc = props -> block;
            return this;
        }

        private BlockConfig<T> block( Function<Block.Properties, ? extends T> func ) {
            blockFunc = func;
            return this;
        }

        public BlockConfig<T> item() {
            itemProps = new Item.Properties();
            itemFunc = MDBlocks::createBlockItem;
            return this;
        }

        public BlockConfig<T> item( ItemGroup group ) {
            itemProps = new Item.Properties().group( group );
            itemFunc = MDBlocks::createBlockItem;
            return this;
        }

        public BlockConfig<T> item( Item.Properties props ) {
            itemProps = props;
            itemFunc = MDBlocks::createBlockItem;
            return this;
        }

        public BlockConfig<T> props( Block.Properties props ) {
            blockProps = props;
            return this;
        }

        public BlockConfig<T> props( Material mat ) {
            blockProps = Block.Properties.create( mat );
            return this;
        }

        public BlockConfig<T> props( Material mat, MaterialColor col ) {
            blockProps = Block.Properties.create( mat, col );
            return this;
        }

        public BlockConfig<T> props( Function<Block.Properties, Block.Properties> func ) {
            blockProps = func.apply( blockProps );
            return this;
        }

        public BlockConfig<T> sound( SoundType type ) {
            blockProps.sound( type );
            return this;
        }

        public BlockConfig<T> light( int light ) {
            blockProps.lightValue( light );
            return this;
        }

        public BlockConfig<T> slipperiness( float slipperiness ) {
            blockProps.slipperiness( slipperiness );
            return this;
        }

        public BlockConfig<T> ticks() {
            blockProps.tickRandomly();
            return this;
        }

        public BlockConfig<T> allowMovement() {
            blockProps.doesNotBlockMovement();
            return this;
        }

        public BlockConfig<T> hardness( float hr ) {
            blockProps.hardnessAndResistance( hr );
            return this;
        }

        public BlockConfig<T> hardness( float h, float r ) {
            blockProps.hardnessAndResistance( h, r );
            return this;
        }

        public BlockConfig<T> variableOpacity() {
            blockProps.variableOpacity();
            return this;
        }

        public BlockConfig<T> tool( ToolType tool, int level ) {
            blockProps.harvestTool( tool ).harvestLevel( level );
            return this;
        }

        public BlockConfig<T> rock( MaterialColor color, double hardness, double resistance ) {
            blockProps = Block.Properties.create( Material.ROCK, color )
                                         .hardnessAndResistance( (float) hardness, (float) resistance )
                                         .sound( SoundType.STONE );
            return this;
        }

        public BlockConfig<T> bedrock( MaterialColor color ) {
            blockProps = Block.Properties.create( Material.ROCK, color )
                                         .hardnessAndResistance( - 1, 3600000 )
                                         .sound( SoundType.STONE );
            return this;
        }

        public BlockConfig<T> asphalt() {
            blockProps = Block.Properties.create( Material.ROCK, MaterialColor.BLACK )
                                         .hardnessAndResistance( 1, 4 )
                                         .sound( MDSoundTypes.ASPHALT );
            return this;
        }

        public BlockConfig<T> glass() {
            blockProps = Block.Properties.create( Material.GLASS, MaterialColor.GRAY )
                                         .hardnessAndResistance( 0.3F )
                                         .sound( SoundType.GLASS );
            return this;
        }

        public BlockConfig<T> dirt( MaterialColor color, boolean overgrown ) {
            blockProps = Block.Properties.create( Material.EARTH, color )
                                         .hardnessAndResistance( overgrown ? 0.6F : 0.5F )
                                         .sound( overgrown ? SoundType.PLANT : SoundType.GROUND );
            return this;
        }

        public BlockConfig<T> dust( MaterialColor color, boolean gravel ) {
            blockProps = Block.Properties.create( ! gravel ? Material.SAND : Material.EARTH, color )
                                         .hardnessAndResistance( gravel ? 0.6F : 0.5F )
                                         .sound( ! gravel ? SoundType.SAND : SoundType.GROUND );
            return this;
        }

        public BlockConfig<T> ash( MaterialColor color ) {
            blockProps = Block.Properties.create( MDMaterial.ASH, color )
                                         .hardnessAndResistance( 0.5F )
                                         .sound( SoundType.SAND );
            return this;
        }

        public BlockConfig<T> clay( MaterialColor color ) {
            blockProps = Block.Properties.create( Material.CLAY, color )
                                         .hardnessAndResistance( 0.5F )
                                         .sound( SoundType.GROUND );
            return this;
        }

        public BlockConfig<T> fluid( Material mat, MaterialColor color ) {
            blockProps = Block.Properties.create( mat, color )
                                         .hardnessAndResistance( 100F )
                                         .doesNotBlockMovement();
            return this;
        }

        public BlockConfig<T> wood( MaterialColor color ) {
            blockProps = Block.Properties.create( Material.WOOD, color )
                                         .hardnessAndResistance( 2, 3 )
                                         .sound( SoundType.WOOD );
            return this;
        }

        public BlockConfig<T> metal( MaterialColor color ) {
            blockProps = Block.Properties.create( Material.IRON, color )
                                         .hardnessAndResistance( 5, 6 )
                                         .sound( SoundType.METAL );
            return this;
        }

        public BlockConfig<T> weakPlant( MaterialColor color, double hardness ) {
            blockProps = Block.Properties.create( Material.TALL_PLANTS, color )
                                         .hardnessAndResistance( (float) hardness )
                                         .doesNotBlockMovement()
                                         .sound( SoundType.PLANT );
            return this;
        }

        public BlockConfig<T> strongPlant( MaterialColor color, double hardness ) {
            blockProps = Block.Properties.create( Material.PLANTS, color )
                                         .hardnessAndResistance( (float) hardness )
                                         .doesNotBlockMovement()
                                         .sound( SoundType.PLANT );
            return this;
        }

        public BlockConfig<T> crystal( MaterialColor color, double hardness ) {
            blockProps = Block.Properties.create( MDMaterial.CRYSTAL, color )
                                         .hardnessAndResistance( (float) hardness )
                                         .doesNotBlockMovement()
                                         .sound( MDSoundTypes.CRYSTAL );
            return this;
        }

        public BlockConfig<T> leaves( MaterialColor color, double hardness ) {
            blockProps = Block.Properties.create( Material.LEAVES, color )
                                         .hardnessAndResistance( (float) hardness )
                                         .sound( SoundType.PLANT );
            return this;
        }

        public BlockConfig<T> torch( MaterialColor color, int light ) {
            blockProps = Block.Properties.create( Material.MISCELLANEOUS, color )
                                         .lightValue( light )
                                         .hardnessAndResistance( 0 )
                                         .doesNotBlockMovement()
                                         .sound( SoundType.WOOD );
            return this;
        }

        public BlockConfig<T> drops( IBlockDrops drops ) {
            this.drops = drops;
            return this;
        }

        public BlockConfig<T> dropSelf() {
            this.drops = MDBlockDrops.SIMPLE;
            return this;
        }

        public BlockConfig<T> alias( String... aliases ) {
            this.aliases.addAll( Arrays.asList( aliases ) );
            return this;
        }

        public BlockConfig<T> recipe( IRecipeData recipe ) {
            this.recipes.add( recipe );
            return this;
        }

        public BlockConfig<T> recipeBlock4( Supplier<IItemProvider> in, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.block4( in, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public BlockConfig<T> recipeBlock9( Supplier<IItemProvider> in, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.block9( in, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public BlockConfig<T> recipeOne( Supplier<IItemProvider> in, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.one( in, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public BlockConfig<T> recipeOne( Tag<Item> in, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.one( in, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public BlockConfig<T> recipeJoin( Supplier<IItemProvider> in1, Supplier<IItemProvider> in2, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.join( in1, in2, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public BlockConfig<T> recipeAdd( Supplier<IItemProvider> add, Supplier<IItemProvider> into, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.add( add, into, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public BlockConfig<T> recipeSlab( Supplier<IItemProvider> in, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.slab( in, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public BlockConfig<T> recipeStairs( Supplier<IItemProvider> in, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.stairs( in, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public BlockConfig<T> recipeStep( Supplier<IItemProvider> in, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.step( in, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public BlockConfig<T> recipeWall( Supplier<IItemProvider> in, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.wall( in, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public BlockConfig<T> recipeCorner( Supplier<IItemProvider> in, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.corner( in, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public BlockConfig<T> recipeStonecutting( Supplier<IItemProvider> in, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.stonecutting( in, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public BlockConfig<T> recipeSmelting( Supplier<IItemProvider> in, double exp, int time, String id ) {
            this.recipes.add( RecipeDataTypes.smelting( in, supplyI( this.id ), (float) exp, time, "", id ) );
            return this;
        }

        public BlockConfig<T> recipeSmoking( Supplier<IItemProvider> in, double exp, int time, String id ) {
            this.recipes.add( RecipeDataTypes.smoking( in, supplyI( this.id ), (float) exp, time, "", id ) );
            return this;
        }

        public BlockConfig<T> recipeBlasting( Supplier<IItemProvider> in, double exp, int time, String id ) {
            this.recipes.add( RecipeDataTypes.blasting( in, supplyI( this.id ), (float) exp, time, "", id ) );
            return this;
        }

        public BlockConfig<T> recipeFence( Supplier<IItemProvider> stick, Supplier<IItemProvider> wood, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.fence( stick, wood, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public BlockConfig<T> recipeDoor( Supplier<IItemProvider> in, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.door( in, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public BlockConfig<T> recipeVert( Supplier<IItemProvider> in, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.vert( in, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public BlockConfig<T> recipeHoriz( Supplier<IItemProvider> in, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.horiz( in, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public BlockConfig<T> recipeVert3( Supplier<IItemProvider> in, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.vert3( in, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public BlockConfig<T> recipeHoriz3( Supplier<IItemProvider> in, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.horiz3( in, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public BlockConfig<T> recipeTorch( Supplier<IItemProvider> coal, Supplier<IItemProvider> stick, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.torch( coal, stick, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public BlockConfig<T> recipeTorch( Supplier<IItemProvider> coal, Tag<Item> stick, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.torch( coal, stick, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public BlockConfig<T> recipeJoinVert( Supplier<IItemProvider> u, Supplier<IItemProvider> d, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.joinVert( u, d, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public BlockConfig<T> recipeJoinHoriz( Supplier<IItemProvider> l, Supplier<IItemProvider> r, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.joinHoriz( l, r, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public BlockConfig<T> recipeH( Supplier<IItemProvider> in, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.h( in, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public BlockConfig<T> recipeX( Supplier<IItemProvider> in, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.x( in, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public BlockConfig<T> recipeO4( Supplier<IItemProvider> in, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.o4( in, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public BlockConfig<T> recipeO8( Supplier<IItemProvider> in, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.o8( in, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public BlockConfig<T> recipeO8( Tag<Item> in, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.o8( in, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public BlockConfig<T> recipeJoin3( Supplier<IItemProvider> in1, Supplier<IItemProvider> in2, Supplier<IItemProvider> in3, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.join3( in1, in2, in3, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public BlockConfig<T> recipeAsphalt( Supplier<IItemProvider> goo, Supplier<IItemProvider> coal, Supplier<IItemProvider> gravel, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.asphalt( goo, coal, gravel, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public T create() {
            if( blockFunc == null ) {
                throw new IllegalStateException( "No block function specified" );
            }

            T block = blockFunc.apply( blockProps );

            BLOCKS.register( id, block, aliases.toArray( new String[ 0 ] ) );
            if( itemFunc != null ) {
                Item item = itemFunc.apply( block, itemProps );
                ITEMS.register( id, item, aliases.toArray( new String[ 0 ] ) );
                ITEM_BLOCKS.add( block );
            }
            if( drops != null ) {
                IModernity.get().getDataService().addBlockDrops( block, drops );
            }
            for( IRecipeData recipe : recipes ) {
                IModernity.get().getDataService().addRecipe( recipe );
            }

            return block;
        }
    }
}
