/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 3 - 2019
 */

package modernity.common.container;

import modernity.common.item.MDItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

/**
 * Represents a Nether Altar container.
 */
public class NetherAltarContainer extends Container {
    private final IInventory altarInventory;

    public NetherAltarContainer( int id, PlayerInventory playerInventory, IInventory altarInventory ) {
        super( MDContainerTypes.NETHER_ALTAR, id );
        this.altarInventory = altarInventory;

        // Altar slots
        this.addSlot( new InputSlot( altarInventory, 0, 52, 17 ) );
        this.addSlot( new InputSlot( altarInventory, 1, 52, 53 ) );
        this.addSlot( new InputSlot( altarInventory, 2, 108, 17 ) );
        this.addSlot( new InputSlot( altarInventory, 3, 108, 53 ) );
        this.addSlot( new ResultSlot( altarInventory, 4, 80, 35 ) );

        // Player Inventory
        for( int y = 0; y < 3; ++ y ) {
            for( int x = 0; x < 9; ++ x ) {
                this.addSlot( new Slot( playerInventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18 ) );
            }
        }

        // Hotbar
        for( int i = 0; i < 9; ++ i ) {
            this.addSlot( new Slot( playerInventory, i, 8 + i * 18, 142 ) );
        }
    }

    @Override
    public boolean canInteractWith( PlayerEntity player ) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot( PlayerEntity player, int index ) {
        ItemStack resultStack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get( index );
        if( slot != null && slot.getHasStack() ) {
            ItemStack slotStack = slot.getStack();
            resultStack = slotStack.copy();
            if( index > 4 ) {
                if( slotStack.getItem() == MDItems.CURSE_CRYSTAL_SHARD_1 ) {
                    if( ! this.mergeItemStack( slotStack, 3, 4, false ) ) {
                        return ItemStack.EMPTY;
                    }
                } else if( slotStack.getItem() == MDItems.CURSE_CRYSTAL_SHARD_2 ) {
                    if( ! this.mergeItemStack( slotStack, 2, 3, false ) ) {
                        return ItemStack.EMPTY;
                    }
                } else if( slotStack.getItem() == MDItems.CURSE_CRYSTAL_SHARD_3 ) {
                    if( ! this.mergeItemStack( slotStack, 1, 2, false ) ) {
                        return ItemStack.EMPTY;
                    }
                } else if( slotStack.getItem() == MDItems.CURSE_CRYSTAL_SHARD_4 ) {
                    if( ! this.mergeItemStack( slotStack, 0, 1, false ) ) {
                        return ItemStack.EMPTY;
                    }
                } else if( index < 32 ) {
                    if( ! this.mergeItemStack( slotStack, 32, 41, false ) ) {
                        return ItemStack.EMPTY;
                    }
                } else if( index < 41 && ! this.mergeItemStack( slotStack, 5, 32, false ) ) {
                    return ItemStack.EMPTY;
                }
            } else if( ! this.mergeItemStack( slotStack, 5, 41, false ) ) {
                return ItemStack.EMPTY;
            }

            if( slotStack.isEmpty() ) {
                slot.putStack( ItemStack.EMPTY );
            } else {
                slot.onSlotChanged();
            }

            if( slotStack.getCount() == resultStack.getCount() ) {
                return ItemStack.EMPTY;
            }

            slot.onTake( player, slotStack );
        }

        return resultStack;
    }

    /**
     * The result (center) slot of the Nether altar.
     */
    public static class ResultSlot extends Slot {
        public ResultSlot( IInventory inventory, int index, int x, int y ) {
            super( inventory, index, x, y );
        }

        @Override
        public int getSlotStackLimit() {
            return 1;
        }

        @Override
        public boolean isItemValid( ItemStack stack ) {
            return false;
        }
    }

    /**
     * The input (surrounding) slots of the Nether altar.
     */
    public static class InputSlot extends Slot {
        public InputSlot( IInventory inventory, int index, int x, int y ) {
            super( inventory, index, x, y );
        }

        @Override
        public int getSlotStackLimit() {
            return 1;
        }
    }
}
