package net.shadew.ndebris.common.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.registry.Registry;

import net.shadew.ndebris.common.NaturesDebris;
import net.shadew.ndebris.common.sound.NdSoundEvents;

public abstract class NdItems {
    public static final Item MUSIC_DISC_DARK = musicDisc("music_disc_dark", NdSoundEvents.MUSIC_DISC_DARK, 0);
    public static final Item MUSIC_DISC_M1 = musicDisc("music_disc_m1", NdSoundEvents.MUSIC_DISC_M1, 0);

    private static <I extends Item> I item(String id, I item) {
        Registry.register(Registry.ITEM, NaturesDebris.id(id), item);
        return item;
    }

    private static Item musicDisc(String id, SoundEvent sound, int comparator) {
        return item(id, new NdMusicDiscItem(comparator, sound, inGroup(ItemGroup.MISC).maxCount(1)));
    }

    private static Item.Settings inGroup(ItemGroup group) {
        return new Item.Settings().group(group);
    }
}
