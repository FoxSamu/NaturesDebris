/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 21 - 2020
 * Author: rgsw
 */

package modernity.common.tileentity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import modernity.common.block.utils.CleanerBlock;
import modernity.common.container.CleanerContainer;
import modernity.common.container.inventory.ICleaningInventory;
import modernity.common.container.slot.CleanerBucketSlot;
import modernity.common.container.slot.CleanerFuelSlot;
import modernity.common.recipes.CleaningRecipe;
import modernity.common.recipes.MDRecipeTypes;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class CleanerTileEntity extends ContainerTileEntity implements ICleaningInventory, ITickableTileEntity, IRecipeHolder, IRecipeHelperPopulator, ISidedInventory {
    public static final int BUCKET_INDEX = 0;
    public static final int INPUT_INDEX = 1;
    public static final int FUEL_INDEX = 2;
    public static final int RESULT_INDEX = 3;

    private static final TranslationTextComponent TITLE = new TranslationTextComponent( "container.modernity.cleaner" );

    private int fluidAmount;
    private Fluid fluid;
    private int burnTime;
    private int burnTimeTotal;
    private int cookTime;
    private int cookTimeTotal;
    private int fluidUseTotal;

    protected final IIntArray cleanerData = new IIntArray() {
        @Override
        public int get( int index ) {
            switch( index ) {
                case 0:
                    return burnTime;
                case 1:
                    return burnTimeTotal;
                case 2:
                    return cookTime;
                case 3:
                    return cookTimeTotal;
                case 4:
                    return fluidAmount;
                case 5:
                    return fluidUseTotal;
                case 6:
                    if( fluid == null ) return - 1;
                    return ( (ForgeRegistry<Fluid>) ForgeRegistries.FLUIDS ).getID( fluid );
                default:
                    return 0;
            }
        }

        @Override
        public void set( int index, int value ) {
            switch( index ) {
                case 0:
                    burnTime = value;
                    break;
                case 1:
                    burnTimeTotal = value;
                    break;
                case 2:
                    cookTime = value;
                    break;
                case 3:
                    cookTimeTotal = value;
                    break;
                case 4:
                    fluidAmount = value;
                    break;
                case 5:
                    fluidUseTotal = value;
                    break;
                case 6:
                    if( value == - 1 ) fluid = null;
                    fluid = ( (ForgeRegistry<Fluid>) ForgeRegistries.FLUIDS ).getValue( value );
                    break;
            }
            markDirty();
        }

        @Override
        public int size() {
            return 7;
        }
    };

    private final Map<ResourceLocation, Integer> usedRecipes = Maps.newHashMap();

    public CleanerTileEntity() {
        super( MDTileEntitiyTypes.CLEANER );
    }

    @Override
    protected ITextComponent getDefaultName() {
        return TITLE;
    }

    @Override
    protected Container createMenu( int id, PlayerInventory player ) {
        return new CleanerContainer( id, this, player, cleanerData );
    }

    @Override
    public int getSizeInventory() {
        return 4;
    }

    @Override
    public Fluid getFluid() {
        return fluid;
    }

    @Override
    public int getFluidAmount() {
        return fluid == null ? 0 : fluidAmount;
    }

    @Override
    public void setFluid( Fluid fluid ) {
        this.fluid = fluid;
    }

    @Override
    public void setFluidAmount( int fluidAmount ) {
        this.fluidAmount = fluidAmount;
    }

    @Override
    public CompoundNBT write( CompoundNBT nbt ) {
        nbt = super.write( nbt );

        nbt.putInt( "FluidAmount", fluidAmount );
        nbt.putInt( "Fluid", ( (ForgeRegistry<Fluid>) ForgeRegistries.FLUIDS ).getID( fluid ) );
        nbt.putInt( "BurnTime", burnTime );
        nbt.putInt( "TotalBurnTime", burnTimeTotal );
        nbt.putInt( "CookTime", cookTime );
        nbt.putInt( "TotalCookTime", cookTimeTotal );
        nbt.putInt( "TotalFluidUse", fluidUseTotal );

        CompoundNBT usedRecsNBT = new CompoundNBT();
        for( Map.Entry<ResourceLocation, Integer> entry : usedRecipes.entrySet() ) {
            usedRecsNBT.putInt( entry.getKey().toString(), entry.getValue() );
        }

        nbt.put( "UsedRecipes", usedRecsNBT );

        return nbt;
    }

    @Override
    public void read( CompoundNBT nbt ) {
        super.read( nbt );

        fluidAmount = nbt.getInt( "FluidAmount" );
        fluid = ( (ForgeRegistry<Fluid>) ForgeRegistries.FLUIDS ).getValue( nbt.getInt( "Fluid" ) );
        burnTime = nbt.getInt( "BurnTime" );
        burnTimeTotal = nbt.getInt( "TotalBurnTime" );
        cookTime = nbt.getInt( "CookTime" );
        cookTimeTotal = nbt.getInt( "TotalCookTime" );
        fluidUseTotal = nbt.getInt( "TotalFluidUse" );

        usedRecipes.clear();
        CompoundNBT usedRecsNBT = nbt.getCompound( "UsedRecipes" );
        for( String key : usedRecsNBT.keySet() ) {
            int value = usedRecsNBT.getInt( key );
            ResourceLocation loc = new ResourceLocation( key );
            usedRecipes.put( loc, value );
        }
    }

    @Override
    public void tick() {
        boolean burning = isBurning();
        boolean dirty = false;

        if( burning ) {
            burnTime--;
            dirty = true;
        }

        assert world != null;

        if( ! world.isRemote ) {
            ItemStack fuel = getStackInSlot( FUEL_INDEX );

            if( isBurning() || ! fuel.isEmpty() && ! getStackInSlot( INPUT_INDEX ).isEmpty() ) {
                CleaningRecipe rec = world.getRecipeManager()
                                          .getRecipe( MDRecipeTypes.CLEANING, this, world )
                                          .orElse( null );

                if( ! isBurning() && canClean( rec ) ) {
                    burnTime = getBurnTime( fuel );
                    burnTimeTotal = burnTime;

                    if( isBurning() ) {
                        // Consume fuel item
                        if( fuel.hasContainerItem() ) {
                            setInventorySlotContents( FUEL_INDEX, fuel.getContainerItem() );
                        } else if( ! fuel.isEmpty() ) {
                            fuel.shrink( 1 );
                            if( fuel.isEmpty() ) {
                                setInventorySlotContents( FUEL_INDEX, fuel.getContainerItem() );
                            }
                        }
                    }
                }

                if( isBurning() && canClean( rec ) ) {
                    cookTime++;
                    if( fluidUseTotal == 0 ) {
                        fluidUseTotal = rec == null ? 10 : rec.getFluidAmount();
                    }

                    if( cookTime == cookTimeTotal ) {
                        CleaningRecipe rec1 = world.getRecipeManager()
                                                   .getRecipe( MDRecipeTypes.CLEANING, this, world )
                                                   .orElse( null );

                        clean( rec );
                        consumeFluid();
                        cookTime = 0;
                        cookTimeTotal = rec1 == null ? 200 : rec1.getCookTime();
                        fluidUseTotal = rec1 == null ? 10 : rec1.getFluidAmount();
                    }
                } else {
                    consumeFluid();
                    cookTime = 0;
                }
                dirty = true;
            } else if( ! isBurning() && cookTime > 0 ) {
                consumeFluid();
                cookTime = MathHelper.clamp( cookTime - 2, 0, cookTimeTotal );
                dirty = true;
            }

            if( burning != isBurning() ) {
                dirty = true;
                world.setBlockState( pos, world.getBlockState( pos ).with( CleanerBlock.LIT, isBurning() ), 3 );
            }
        }

        if( dirty ) {
            markDirty();
        }
    }

    private void consumeFluid() {
        if( fluidUseTotal > 0 ) {
            int fluidUsed = cookTime * fluidUseTotal / cookTimeTotal;
            fluidAmount -= fluidUsed;
            fluidUseTotal = 0;
        }
    }

    public boolean isBurning() {
        return burnTime > 0;
    }

    protected boolean canClean( @Nullable CleaningRecipe rec ) {
        if( rec == null ) return false;

        if( rec.getFluidAmount() > getFluidAmount() ) return false;
        if( ! rec.getRequiredFluid().test( getFluid() ) ) return false;

        if( ! getStackInSlot( INPUT_INDEX ).isEmpty() ) {
            ItemStack out = rec.getRecipeOutput();
            if( out.isEmpty() ) {
                return false;
            } else {
                ItemStack currResult = getStackInSlot( RESULT_INDEX );
                if( currResult.isEmpty() ) {
                    return true;
                } else if( ! currResult.isItemEqual( out ) ) {
                    return false;
                } else if( currResult.getCount() + out.getCount() <= getInventoryStackLimit() && currResult.getCount() + out.getCount() <= currResult.getMaxStackSize() ) {
                    return true;
                } else {
                    return currResult.getCount() + out.getCount() <= out.getMaxStackSize();
                }
            }
        } else {
            return false;
        }
    }

    protected int getBurnTime( ItemStack stack ) {
        if( stack.isEmpty() ) {
            return 0;
        } else {
            return ForgeHooks.getBurnTime( stack );
        }
    }

    protected int getCookTimeForCurrentRecipe() {
        assert world != null;
        return world.getRecipeManager()
                    .getRecipe( MDRecipeTypes.CLEANING, this, world )
                    .map( CleaningRecipe::getCookTime )
                    .orElse( 200 );
    }

    private void clean( @Nullable CleaningRecipe rec ) {
        assert world != null;
        if( rec != null && canClean( rec ) ) {
            ItemStack in = getStackInSlot( INPUT_INDEX );
            ItemStack result = rec.getRecipeOutput();
            ItemStack out = getStackInSlot( RESULT_INDEX );
            if( out.isEmpty() ) {
                setInventorySlotContents( RESULT_INDEX, result.copy() );
            } else if( out.getItem() == result.getItem() ) {
                out.grow( result.getCount() );
            }

            if( ! world.isRemote ) {
                setRecipeUsed( rec );
            }

            in.shrink( FUEL_INDEX );
        }
    }

    @Override
    public void setRecipeUsed( @Nullable IRecipe<?> recipe ) {
        if( recipe != null ) {
            usedRecipes.compute( recipe.getId(), ( loc, amount ) -> 1 + ( amount == null ? 0 : amount ) );
        }
    }

    @Nullable
    @Override
    public IRecipe<?> getRecipeUsed() {
        return null;
    }

    private static final int[] SLOTS_UP = { INPUT_INDEX };
    private static final int[] SLOTS_DOWN = { RESULT_INDEX, FUEL_INDEX };
    private static final int[] SLOTS_HORIZONTAL = { BUCKET_INDEX, FUEL_INDEX };

    @Override
    public int[] getSlotsForFace( Direction dir ) {
        if( dir == Direction.DOWN ) return SLOTS_DOWN;
        if( dir == Direction.UP ) return SLOTS_UP;
        return SLOTS_HORIZONTAL;
    }

    @Override
    public boolean canInsertItem( int slot, ItemStack stack, @Nullable Direction dir ) {
        if( slot == FUEL_INDEX ) return CleanerFuelSlot.isFuel( stack );
        if( slot == RESULT_INDEX ) return false;
        if( slot == BUCKET_INDEX ) return CleanerBucketSlot.isFilledBucket( stack );
        return true;
    }

    @Override
    public boolean canExtractItem( int slot, ItemStack stack, Direction dir ) {
        if( slot == FUEL_INDEX ) return CleanerBucketSlot.isEmptyBucket( stack );
        if( slot == RESULT_INDEX ) return true;
        if( slot == BUCKET_INDEX ) return CleanerBucketSlot.isEmptyBucket( stack );
        return false;
    }

    @Override
    public void setInventorySlotContents( int index, ItemStack stack ) {
        assert world != null;

        ItemStack old = getStackInSlot( index );
        boolean equal = ! stack.isEmpty() && stack.isItemEqual( old ) && ItemStack.areItemStackTagsEqual( stack, old );
        super.setInventorySlotContents( index, stack );
        if( stack.getCount() > getInventoryStackLimit() ) {
            stack.setCount( getInventoryStackLimit() );
        }

        if( index == INPUT_INDEX && ! equal ) {
            consumeFluid();
            CleaningRecipe rec1 = world.getRecipeManager()
                                       .getRecipe( MDRecipeTypes.CLEANING, this, world )
                                       .orElse( null );
            cookTime = 0;
            cookTimeTotal = rec1 == null ? 200 : rec1.getCookTime();
            fluidUseTotal = rec1 == null ? 10 : rec1.getFluidAmount();
            markDirty();
        }
    }

    @Override
    public boolean isUsableByPlayer( PlayerEntity player ) {
        if( world == null ) return false;
        if( world.getTileEntity( pos ) != this ) return false;
        return player.getDistanceSq( pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5 ) <= 64;
    }

    @Override
    public boolean isItemValidForSlot( int index, ItemStack stack ) {
        if( index == RESULT_INDEX ) {
            return false;
        } else if( index == INPUT_INDEX ) {
            return true;
        } else if( index == BUCKET_INDEX ) {
            return CleanerBucketSlot.isBucket( stack );
        } else {
            return CleanerFuelSlot.isFuel( stack );
        }
    }

    @Override
    public void onCrafting( PlayerEntity player ) {
    }

    public void unlockUsedRecipesAndGainXP( PlayerEntity player ) {
        List<IRecipe<?>> toUnlock = Lists.newArrayList();

        for( Map.Entry<ResourceLocation, Integer> entry : usedRecipes.entrySet() ) {
            player.world.getRecipeManager().getRecipe( entry.getKey() ).ifPresent( recipe -> {
                toUnlock.add( recipe );
                gainExperience( player, entry.getValue(), ( (CleaningRecipe) recipe ).getExperience() );
            } );
        }

        player.unlockRecipes( toUnlock );
        usedRecipes.clear();
    }

    private static void gainExperience( PlayerEntity player, int usedRecipes, float xp ) {
        if( xp == 0 ) {
            usedRecipes = 0;
        } else if( xp < 1 ) {
            int amount = MathHelper.floor( usedRecipes * xp );
            if( amount < MathHelper.ceil( usedRecipes * xp ) && Math.random() < usedRecipes * xp - amount ) {
                ++ amount;
            }

            usedRecipes = amount;
        }

        while( usedRecipes > 0 ) {
            int part = ExperienceOrbEntity.getXPSplit( usedRecipes );
            usedRecipes -= part;
            player.world.addEntity( new ExperienceOrbEntity( player.world, player.posX, player.posY + 0.5, player.posZ + 0.5, part ) );
        }
    }

    @Override
    public void fillStackedContents( RecipeItemHelper helper ) {
        for( ItemStack stack : stacks ) {
            helper.accountStack( stack );
        }
    }
}
