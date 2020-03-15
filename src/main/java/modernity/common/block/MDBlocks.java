/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 15 - 2020
 * Author: rgsw
 */

package modernity.common.block;

import com.google.common.collect.Lists;
import modernity.api.block.IColoredBlock;
import modernity.common.block.base.ICustomBlockItem;
import modernity.common.block.loot.IBlockDrops;
import modernity.common.block.loot.MDBlockDrops;
import modernity.common.registry.RegistryEventHandler;
import modernity.common.registry.RegistryHandler;
import modernity.data.loot.MDBlockLootTables;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.Minecraft;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Object holder for modernity blocks.
 */
@ObjectHolder( "modernity" )
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
                MDBlockLootTables.addBlock( block, drops );
            }

            return block;
        }
    }
}
