package modernity.common.block.base;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

import modernity.common.block.MDBlocks;


public class BlockBase extends Block implements MDBlocks.Entry {
    public final Item.Properties itemProps;

    public BlockBase( String id, Properties properties, Item.Properties itemProps ) {
        super( properties );
        this.itemProps = itemProps;
        setRegistryName( "modernity:" + id );
    }

    public BlockBase( String id, Properties properties ) {
        this( id, properties, new Item.Properties() );
    }


    @Override
    public String getTranslationKey() {
        return super.getTranslationKey();
    }

    @Override
    public Item getBlockItem() {
        return this.asItem();
    }

    @Override
    public Item createBlockItem() {
        return new ItemBlock( this, itemProps ).setRegistryName( getRegistryName() );
    }

    @Override
    public Block getThisBlock() {
        return this;
    }
}
