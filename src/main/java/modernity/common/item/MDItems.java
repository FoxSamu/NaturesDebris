package modernity.common.item;

import com.google.common.collect.Lists;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.IForgeRegistry;

import modernity.api.item.IColoredItem;

import java.util.ArrayList;

public class MDItems {
    private static final ArrayList<Entry> ENTRIES = Lists.newArrayList();

    public static void register( IForgeRegistry<Item> registry ) {
        for( Entry e : ENTRIES ) {
            registry.register( e.getItem() );
        }
    }

    @OnlyIn( Dist.CLIENT )
    public static void registerClient( ItemColors itemColors ) {
        for( Entry e : ENTRIES ) {
            if( e instanceof IColoredItem ) {
                itemColors.register( ( (IColoredItem) e )::colorMultiplier );
            }
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
