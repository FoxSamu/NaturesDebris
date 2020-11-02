package natures.debris.common.item;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

import net.minecraftforge.registries.ObjectHolder;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.util.SoundEvent;

import natures.debris.core.util.IRegistry;
import natures.debris.common.NaturesDebris;
import natures.debris.common.sound.NdSoundEvents;

@ObjectHolder("ndebris")
public abstract class NdItems {
    public static final Item MUSIC_DISC_DARK = inj();
    public static final Item MUSIC_DISC_M1 = inj();

    public static void registerItems(IRegistry<Item> registry) {
        registry.registerAll(
            // Don't add these to music disks tag - it should not drop from creepers
            musicDisc("music_disc_dark", () -> NdSoundEvents.MUSIC_DISC_DARK, 0),
            musicDisc("music_disc_m1", () -> NdSoundEvents.MUSIC_DISC_M1, 0)
        );
    }

    private static <I extends Item> I item(String id, I item) {
        item.setRegistryName(NaturesDebris.resLoc(id));
        return item;
    }

    private static Item musicDisc(String id, Supplier<SoundEvent> sound, int comparator) {
        return item(id, new MusicDiscItem(comparator, sound, inGroup(ItemGroup.MISC).maxStackSize(1)));
    }

    private static Item.Properties inGroup(ItemGroup group) {
        return new Item.Properties().group(group);
    }

    @Nonnull
    @SuppressWarnings("ConstantConditions")
    private static Item inj() {
        return null;
    }
}
