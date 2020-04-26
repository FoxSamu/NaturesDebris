package modernity.tests.models;

import modernity.generic.IModernityOld;
import modernity.client.model.MDModelLoaders;
import modernity.common.block.farmland.ITopTextureConnectionBlock;
import modernity.common.block.plant.BushBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class ModelsTest implements IModernityOld {
    public static Block EMPTY;
    public static Block BUSH;
    public static Block FARMLAND;

    @SubscribeEvent
    public void onRegisterBlocks( RegistryEvent.Register<Block> event ) {
        IForgeRegistry<Block> registry = event.getRegistry();
        registry.register( EMPTY );
        registry.register( BUSH );
        registry.register( FARMLAND );
    }

    @SubscribeEvent
    public void onRegisterItems( RegistryEvent.Register<Item> event ) {
        IForgeRegistry<Item> registry = event.getRegistry();
        BlockItem empty = new BlockItem( EMPTY, new Item.Properties().group( ItemGroup.REDSTONE ) );
        empty.setRegistryName( "modernitytest:models/empty" );
        registry.register( empty );

        BlockItem bush = new BlockItem( BUSH, new Item.Properties().group( ItemGroup.REDSTONE ) );
        bush.setRegistryName( "modernitytest:models/bush" );
        registry.register( bush );

        BlockItem farmland = new BlockItem( FARMLAND, new Item.Properties().group( ItemGroup.REDSTONE ) );
        farmland.setRegistryName( "modernitytest:models/farmland" );
        registry.register( farmland );
    }

    @Override
    public void preInit() {
        EMPTY = new Block( Block.Properties.create( Material.ROCK ) );
        EMPTY.setRegistryName( "modernitytest:models/empty" );

        BUSH = new TestBushBlock( Block.Properties.create( Material.ROCK ).notSolid() );
        BUSH.setRegistryName( "modernitytest:models/bush" );

        FARMLAND = new TestCTMBlock( Block.Properties.create( Material.ROCK ).notSolid() );
        FARMLAND.setRegistryName( "modernitytest:models/farmland" );
        MDModelLoaders.register();
    }

    @Override
    public void init() {
        RenderTypeLookup.setRenderLayer( EMPTY, RenderType.getCutout() );
        RenderTypeLookup.setRenderLayer( BUSH, RenderType.getCutout() );
        RenderTypeLookup.setRenderLayer( FARMLAND, RenderType.getCutout() );
    }

    public static class TestBushBlock extends BushBlock {
        TestBushBlock( Properties properties ) {
            super( properties );
        }
    }

    public static class TestCTMBlock extends Block implements ITopTextureConnectionBlock {
        TestCTMBlock( Properties properties ) {
            super( properties );
        }
    }
}
