/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 28 - 2019
 */

package modernity.common.block.base;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import modernity.common.block.MDBlocks;
import modernity.common.item.MDItems;

@SuppressWarnings( "deprecation" )
public class BlockPortalCauldron extends Block implements MDBlocks.Entry {
    protected static final VoxelShape INSIDE = Block.makeCuboidShape( 2, 4, 2, 14, 16, 14 );
    protected static final VoxelShape WALLS = VoxelShapes.combineAndSimplify( VoxelShapes.fullCube(), INSIDE, IBooleanFunction.ONLY_FIRST );

    protected final Item.Properties itemProps;

    public BlockPortalCauldron( String id, Block.Properties properties, Item.Properties itemProps ) {
        super( properties );
        this.itemProps = itemProps;
        setRegistryName( "modernity:" + id );
    }

    public BlockPortalCauldron( String id, Block.Properties properties ) {
        super( properties );
        this.itemProps = new Item.Properties();
        setRegistryName( "modernity:" + id );
    }

    public VoxelShape getShape( IBlockState state, IBlockReader world, BlockPos pos ) {
        return WALLS;
    }

    public boolean isSolid( IBlockState state ) {
        return false;
    }

    public VoxelShape getRaytraceShape( IBlockState state, IBlockReader worldIn, BlockPos pos ) {
        return INSIDE;
    }

    public boolean isFullCube( IBlockState state ) {
        return false;
    }

    public void onEntityCollision( IBlockState state, World world, BlockPos pos, Entity entity ) {
        double fluidHeight = pos.getY() + 0.9375;
        if( ! world.isRemote && entity.isBurning() && entity.getBoundingBox().minY <= fluidHeight ) {
            entity.extinguish();
            // TODO: Teleport to modernity, or back
        }
    }

    public boolean onBlockActivated( IBlockState state, World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ ) {
        // TODO
        // - Create from item

        ItemStack heldStack = player.getHeldItem( hand );
        if( heldStack.isEmpty() ) {
            return true;
        } else {
            Item heldItem = heldStack.getItem();
            if( heldItem == Items.BUCKET ) {
                if( ! world.isRemote ) {
                    if( ! player.abilities.isCreativeMode ) {
                        heldStack.shrink( 1 );
                        if( heldStack.isEmpty() ) {
                            player.setHeldItem( hand, new ItemStack( MDItems.PORTAL_BUCKET ) );
                        } else if( ! player.inventory.addItemStackToInventory( new ItemStack( MDItems.PORTAL_BUCKET ) ) ) {
                            player.dropItem( new ItemStack( MDItems.PORTAL_BUCKET ), false );
                        }
                    }

                    player.addStat( StatList.USE_CAULDRON );
                    setEmpty( world, pos, state );
                    world.playSound( null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1, 1 );
                }

                return true;
            }
        }
        return false;
    }

    public void setEmpty( World world, BlockPos pos, IBlockState state ) {
        world.setBlockState( pos, Blocks.CAULDRON.getDefaultState(), 2 );
        world.updateComparatorOutputLevel( pos, this );
    }

    public boolean hasComparatorInputOverride( IBlockState state ) {
        return true;
    }

    public int getComparatorInputOverride( IBlockState blockState, World worldIn, BlockPos pos ) {
        return 3;
    }

    public BlockFaceShape getBlockFaceShape( IBlockReader worldIn, IBlockState state, BlockPos pos, EnumFacing face ) {
        if( face == EnumFacing.UP ) {
            return BlockFaceShape.BOWL;
        } else {
            return face == EnumFacing.DOWN ? BlockFaceShape.UNDEFINED : BlockFaceShape.SOLID;
        }
    }

    public boolean allowsMovement( IBlockState state, IBlockReader worldIn, BlockPos pos, PathType type ) {
        return false;
    }

    @Override
    public IItemProvider getItemDropped( IBlockState state, World world, BlockPos pos, int fortune ) {
        return Blocks.CAULDRON;
    }

    @Override
    public Item getBlockItem() {
        return asItem();
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
