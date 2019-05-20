package modernity.common.item;

import com.google.common.collect.Lists;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.registries.IForgeRegistry;

import modernity.common.item.base.ItemBase;

import java.util.ArrayList;

public class MDItems {
    private static final ArrayList<Entry> ENTRIES = Lists.newArrayList();

    public static final ItemBase TESTITEM = item( new ItemBase( "testitem", new Item.Properties().group( ItemGroup.REDSTONE ).rarity( EnumRarity.COMMON ) ) );

    public static void register( IForgeRegistry<Item> registry ) {
        for( Entry e : ENTRIES ) {
            registry.register( e.getItem() );
        }
    }

    private static <T extends Entry> T item( T item ) {
        ENTRIES.add( item );
        return item;
    }

    public interface Entry {
        Item getItem();
    }
}
