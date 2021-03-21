package net.shadew.ndebris.common.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.sound.BlockSoundGroup;

public abstract class NdBlockTypes {
    public static FabricBlockSettings rock(boolean dark, double hardness, double resistance, BlockSoundGroup sound) {
        return FabricBlockSettings.of(Material.STONE, dark ? MaterialColor.BLACK : MaterialColor.STONE)
                                  .strength((float) hardness, (float) resistance)
                                  .sounds(sound)
                                  .requiresTool()
                                  .breakByHand(false)
                                  .breakByTool(FabricToolTags.PICKAXES, 0);
    }

    public static FabricBlockSettings rock(boolean dark, double hardness, double resistance) {
        return rock(dark, hardness, resistance, BlockSoundGroup.STONE);
    }

    public static FabricBlockSettings limestone(double hardness, double resistance, BlockSoundGroup sound) {
        return FabricBlockSettings.of(Material.STONE, MaterialColor.SAND)
                                  .strength((float) hardness, (float) resistance)
                                  .sounds(sound)
                                  .requiresTool()
                                  .breakByHand(false)
                                  .breakByTool(FabricToolTags.PICKAXES, 0);
    }

    public static FabricBlockSettings limestone(double hardness, double resistance) {
        return limestone(hardness, resistance, BlockSoundGroup.STONE);
    }

    public static FabricBlockSettings sumestone(boolean dark, double hardness, double resistance, BlockSoundGroup sound) {
        return FabricBlockSettings.of(Material.STONE, dark ? MaterialColor.BLACK : MaterialColor.WOOD)
                                  .strength((float) hardness, (float) resistance)
                                  .sounds(sound)
                                  .requiresTool()
                                  .breakByHand(false)
                                  .breakByTool(FabricToolTags.PICKAXES, 0);
    }

    public static FabricBlockSettings sumestone(boolean dark, double hardness, double resistance) {
        return sumestone(dark, hardness, resistance, BlockSoundGroup.STONE);
    }

    public static FabricBlockSettings soil(double strength, MaterialColor color, BlockSoundGroup sound) {
        return FabricBlockSettings.of(Material.SOIL, color)
                                  .strength((float) strength)
                                  .sounds(sound)
                                  .breakByTool(FabricToolTags.SHOVELS);
    }

    public static FabricBlockSettings wood(MaterialColor color, double strength) {
        return FabricBlockSettings.of(Material.WOOD, color)
                                  .sounds(BlockSoundGroup.WOOD)
                                  .strength((float) strength)
                                  .breakByTool(FabricToolTags.AXES);
    }
}
