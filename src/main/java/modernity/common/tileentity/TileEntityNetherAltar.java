/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 3 - 2019
 */

package modernity.common.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import modernity.common.block.MDBlocks;
import modernity.common.container.NetherAltarContainer;
import modernity.common.item.MDItems;
import modernity.common.world.gen.structure.MDStructures;

import javax.annotation.Nullable;

public class TileEntityNetherAltar extends TileEntityContainer implements ITickable {
    private static final ITextComponent NAME = new TextComponentTranslation( Util.makeTranslationKey( "gui", new ResourceLocation( "modernity:nether_altar" ) ) );

    private boolean combinePossible;

    public TileEntityNetherAltar() {
        super( MDTileEntities.NETHER_ALTAR );
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return write( new NBTTagCompound() );
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity( pos, 0, getUpdateTag() );
    }

    @Override
    public void onDataPacket( NetworkManager net, SPacketUpdateTileEntity pkt ) {
        read( pkt.getNbtCompound() );
    }

    @Override
    public int getSizeInventory() {
        return 5;
    }

    @Override
    public Container createContainer( InventoryPlayer playerInventory, EntityPlayer player ) {
        return new NetherAltarContainer( playerInventory, this );
    }

    @Override
    public String getGuiID() {
        return "modernity:nether_altar";
    }

    @Override
    public ITextComponent getName() {
        return NAME;
    }

    @Override
    public void onSlotChanged( int index ) {
        // Combine is scheduled for next tick to prevent it happening during player interaction
        combinePossible = ! world.isRemote && checkSlots();
    }

    private void combine() {
        setInventorySlotContents( 0, ItemStack.EMPTY );
        setInventorySlotContents( 1, ItemStack.EMPTY );
        setInventorySlotContents( 2, ItemStack.EMPTY );
        setInventorySlotContents( 3, ItemStack.EMPTY );
        setInventorySlotContents( 4, new ItemStack( MDItems.CURSE_CRYSTAL ) );
        markDirty(); // Notify the update so that clients see this change
        combinePossible = false;
    }

    private boolean checkEnvironment() {
        // Check biome instead of dimension to support buffet/flat world types with nether theme...
        if( world.getBiome( pos ) != Biomes.NETHER ) return false;
        if( ! checkStructure() ) return false;
        return checkWallCarves();
    }

    private boolean checkSlots() {
        // Uwww, bad programming here: 0123 = 4321...
        if( getStackInSlot( 0 ).isEmpty() || getStackInSlot( 0 ).getItem() != MDItems.CURSE_CRYSTAL_SHARD_4 )
            return false;

        if( getStackInSlot( 1 ).isEmpty() || getStackInSlot( 1 ).getItem() != MDItems.CURSE_CRYSTAL_SHARD_3 )
            return false;

        if( getStackInSlot( 2 ).isEmpty() || getStackInSlot( 2 ).getItem() != MDItems.CURSE_CRYSTAL_SHARD_2 )
            return false;

        if( getStackInSlot( 3 ).isEmpty() || getStackInSlot( 3 ).getItem() != MDItems.CURSE_CRYSTAL_SHARD_1 )
            return false;

        // Result must be empty
        return getStackInSlot( 4 ).isEmpty();
    }

    private boolean checkStructure() {
        return MDStructures.NETHER_ALTAR_STRUCTURE.isPositionInStructure( world, pos );
    }

    private boolean checkWallCarves() {
        int flags = 0;
        for( EnumFacing facing : EnumFacing.Plane.HORIZONTAL ) {
            flags |= checkSide( facing );
        }
        return flags == 127;
    }

    private int checkSide( EnumFacing side ) {
        EnumFacing.Axis axis = side.getAxis();
        if( axis == EnumFacing.Axis.Y ) return 0;

        BlockPos.MutableBlockPos mpos = new BlockPos.MutableBlockPos();

        int out = 0;

        for( int h = - 1; h <= 1; h++ ) {
            int x = axis == EnumFacing.Axis.X ? 3 * side.getXOffset() : h;
            int z = axis == EnumFacing.Axis.Z ? 3 * side.getZOffset() : h;

            for( int y = 0; y < 5; y++ ) {
                mpos.setPos( pos );
                mpos.move( x, y, z );

                IBlockState state = world.getBlockState( mpos );

                if( state.has( BlockStateProperties.HORIZONTAL_FACING ) ) {
                    if( side.getOpposite() == state.get( BlockStateProperties.HORIZONTAL_FACING ) ) {
                        if( state.getBlock() == MDBlocks.GOLD_CARVED_NETHER_BRICKS_CURSE ) out |= 1;
                        if( state.getBlock() == MDBlocks.GOLD_CARVED_NETHER_BRICKS_CYEN ) out |= 2;
                        if( state.getBlock() == MDBlocks.GOLD_CARVED_NETHER_BRICKS_FYREN ) out |= 4;
                        if( state.getBlock() == MDBlocks.GOLD_CARVED_NETHER_BRICKS_TIMEN ) out |= 8;
                        if( state.getBlock() == MDBlocks.GOLD_CARVED_NETHER_BRICKS_PORTAL ) out |= 16;
                        if( state.getBlock() == MDBlocks.GOLD_CARVED_NETHER_BRICKS_NATURE ) out |= 32;
                        if( state.getBlock() == MDBlocks.GOLD_CARVED_NETHER_BRICKS_RGSW ) out |= 64;
                    }
                }
            }
        }
        return out;
    }

    @Override
    public void tick() {
        // Do environment check here so that combine happens immediately when environment is made valid
        if( combinePossible && checkEnvironment() ) {
            combine();
        }
    }
}
