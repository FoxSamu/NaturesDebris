package modernity.tests.models;

import modernity.tests.IModernityTest;
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

public class EmptyModelTest implements IModernityTest {
    public static Block BLOCK;

    @SubscribeEvent
    public void onRegisterBlocks( RegistryEvent.Register<Block> event ) {
        IForgeRegistry<Block> registry = event.getRegistry();
        registry.register( BLOCK );
    }

    @SubscribeEvent
    public void onRegisterItems( RegistryEvent.Register<Item> event ) {
        IForgeRegistry<Item> registry = event.getRegistry();
        registry.register( new BlockItem( BLOCK, new Item.Properties().group( ItemGroup.REDSTONE ) ) );
    }

    @Override
    public void preInit() {
        BLOCK = new Block( Block.Properties.create( Material.ROCK ) );
        BLOCK.setRegistryName( "modernity:empty_model/empty_model_block" );
    }

    @Override
    public void init() {
        RenderTypeLookup.setRenderLayer( BLOCK, RenderType.getCutout() );
    }
}
