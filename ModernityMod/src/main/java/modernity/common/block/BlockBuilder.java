/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.block;

import com.google.common.collect.Lists;
import modernity.common.item.ItemBuilder;
import modernity.common.item.group.ItemGroupCategory;
import modernity.common.block.processor.BlockLayer;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class BlockBuilder extends ItemBuilder {
    private final List<Consumer<Block>> postProcess = Lists.newArrayList();
    private Function<Block.Properties, Block> blockFactory = Block::new;
    private BiFunction<Block, Item.Properties, Item> blockItemFactory = BlockItem::new;
    private Material material;
    private MaterialColor color;
    private boolean blocksMovement = true;
    private SoundType sound = SoundType.STONE;
    private int emission;
    private float resistance;
    private float hardness;
    private boolean ticksRandomly;
    private float slipperiness = .6f;
    private float speedFactor = 1;
    private float jumpFactor = 1;
    private boolean noDrops;
    private Block blockDrops;
    private boolean solid = true;
    private boolean variableOpacity;
    private int harvestLevel = -1;
    private ToolType harvestTool;

    public BlockBuilder blockFactory(Function<Block.Properties, Block> blockFactory) {
        this.blockFactory = blockFactory;
        return this;
    }


    public BlockBuilder blockProcessor(Consumer<Block> processor) {
        postProcess.add(processor);
        return this;
    }

    @Override
    public BlockBuilder itemFactory(Function<Item.Properties, Item> factory) {
        blockItemFactory = (block, props) -> factory.apply(props);
        return (BlockBuilder) super.itemFactory(factory);
    }

    public BlockBuilder itemFactory(BiFunction<Block, Item.Properties, Item> factory) {
        blockItemFactory = factory;
        return this;
    }

    @Override
    public BlockBuilder itemProcessor(Consumer<Item> processor) {
        return (BlockBuilder) super.itemProcessor(processor);
    }

    @Override
    public BlockBuilder category(ItemGroupCategory category) {
        return (BlockBuilder) super.category(category);
    }

    public BlockBuilder layer(BlockLayer layer) {
        return blockProcessor(layer);
    }

    public BlockBuilder material(Material material) {
        this.material = material;
        return this;
    }

    public BlockBuilder color(MaterialColor color) {
        this.color = color;
        return this;
    }

    public BlockBuilder color(DyeColor color) {
        this.color = color.getMapColor();
        return this;
    }

    public BlockBuilder allowMovement() {
        this.blocksMovement = false;
        return this;
    }

    public BlockBuilder notSolid() {
        this.solid = false;
        return this;
    }

    public BlockBuilder sound(SoundType sound) {
        this.sound = sound;
        return this;
    }

    public BlockBuilder emission(int emission) {
        this.emission = emission;
        return this;
    }

    public BlockBuilder hardness(double hardness) {
        this.hardness = (float) hardness;
        return this;
    }

    public BlockBuilder resistance(double resistance) {
        this.resistance = (float) resistance;
        return this;
    }

    public BlockBuilder hardnessResistance(double hardness, double resistance) {
        this.hardness = (float) hardness;
        this.resistance = (float) resistance;
        return this;
    }

    public BlockBuilder hardnessResistance(double hardness) {
        this.hardness = (float) hardness;
        this.resistance = (float) hardness;
        return this;
    }

    public BlockBuilder unbreakable() {
        this.hardness = -1;
        this.resistance = 3600000;
        return this;
    }

    public BlockBuilder ticksRandomly() {
        this.ticksRandomly = true;
        return this;
    }

    public BlockBuilder slipperiness(double slipperiness) {
        this.slipperiness = (float) slipperiness;
        return this;
    }

    public BlockBuilder speedFactor(double speedFactor) {
        this.speedFactor = (float) speedFactor;
        return this;
    }

    public BlockBuilder jumpFactor(double jumpFactor) {
        this.jumpFactor = (float) jumpFactor;
        return this;
    }

    public BlockBuilder noDrops() {
        this.noDrops = true;
        return this;
    }

    public BlockBuilder copyDrops(Block block) {
        this.blockDrops = block;
        return this;
    }

    public BlockBuilder variableOpacity() {
        this.variableOpacity = true;
        return this;
    }

    public BlockBuilder requiredTool(ToolType type, int level) {
        this.harvestTool = type;
        this.harvestLevel = level;
        return this;
    }

    @Override
    public BlockBuilder food(Food food) {
        return (BlockBuilder) super.food(food);
    }

    @Override
    public BlockBuilder stackability(int stackability) {
        return (BlockBuilder) super.stackability(stackability);
    }

    @Override
    public BlockBuilder durability(int durability) {
        return (BlockBuilder) super.durability(durability);
    }

    @Override
    public BlockBuilder container(Supplier<Item> container) {
        return (BlockBuilder) super.container(container);
    }

    @Override
    public BlockBuilder group(ItemGroup group) {
        return (BlockBuilder) super.group(group);
    }

    @Override
    public BlockBuilder rarity(Rarity rarity) {
        return (BlockBuilder) super.rarity(rarity);
    }

    @Override
    public BlockBuilder noRepair() {
        return (BlockBuilder) super.noRepair();
    }

    @Override
    public BlockBuilder toolType(ToolType type, int level) {
        return (BlockBuilder) super.toolType(type, level);
    }

    @Override
    public BlockBuilder renderer(Supplier<Callable<ItemStackTileEntityRenderer>> renderer) {
        return (BlockBuilder) super.renderer(renderer);
    }

    protected Block.Properties makeBlockProps() {
        Block.Properties props = Block.Properties.create(material, color);
        props.sound(sound);
        props.lightValue(emission);
        props.hardnessAndResistance(hardness, resistance);
        props.slipperiness(slipperiness);
        props.speedFactor(speedFactor);
        props.jumpFactor(jumpFactor);
        props.harvestLevel(harvestLevel);
        props.harvestTool(harvestTool);
        if(ticksRandomly) props.tickRandomly();
        if(blockDrops != null) props.lootFrom(blockDrops);
        if(noDrops) props.noDrops();
        if(variableOpacity) props.variableOpacity();
        if(!blocksMovement) props.doesNotBlockMovement();
        if(!solid) props.notSolid();
        return props;
    }

    protected Block processBlock(Block block) {
        for(Consumer<Block> processor : postProcess) {
            processor.accept(block);
        }
        return block;
    }

    @OnlyIn(Dist.CLIENT)
    protected void registerLayer(Block block) {

    }

    public Block makeBlockFactory(Function<Block.Properties, Block> factory) {
        return factory.apply(makeBlockProps());
    }

    public Block makeBlock() {
        if(blockFactory == null) {
            throw new NullPointerException("No factory specified");
        }
        return makeBlockFactory(blockFactory);
    }

    public Item makeItemBlock(Block block) {
        if(blockItemFactory == null) {
            throw new NullPointerException("No factory specified");
        }
        return blockItemFactory.apply(block, makeItemProps());
    }

    @Override
    @Deprecated
    public Item makeItem() {
        return makeItemBlock(makeBlock());
    }

    public boolean hasItem() {
        return hasFactory();
    }
}
