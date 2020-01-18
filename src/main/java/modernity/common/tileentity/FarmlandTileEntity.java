/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 18 - 2020
 * Author: rgsw
 */

package modernity.common.tileentity;

import modernity.api.util.NBTUtil;
import modernity.common.block.farmland.FarmlandBlock;
import modernity.common.block.farmland.Fertility;
import modernity.common.block.farmland.IFarmlandLogic;
import modernity.common.block.farmland.Wetness;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

import java.util.Random;

public class FarmlandTileEntity extends TileEntity implements ITickableTileEntity, IFarmlandLogic {
    private int maxFertility;
    private int maxSaltiness;
    private int maxDecay;
    private int maxFloodedUpdates;
    private boolean hasDecay;

    private int fertility;
    private int decay;
    private int wetness = - 1;

    private Wetness wetnessType;
    private Wetness lastWetnessType;
    private Fertility fertileType;
    private Fertility lastFertileType;

    private boolean needsUpdate;

    public FarmlandTileEntity() {
        this( 24, 24, 24, 17, true );
    }

    public FarmlandTileEntity( int maxFertility, int maxSaltiness, int maxDecay, int maxFloodedUpdates, boolean hasDecay ) {
        super( MDTileEntitiyTypes.FARMLAND );
        this.maxFertility = maxFertility;
        this.maxSaltiness = maxSaltiness;
        this.maxDecay = maxDecay;
        this.maxFloodedUpdates = maxFloodedUpdates;
        this.hasDecay = hasDecay;
    }

    @Override
    public void tick() {
        assert world != null;

        if( needsUpdate ) {
            updateProperties();
            needsUpdate = false;
        }

        if( wetnessType != lastWetnessType || fertileType != lastFertileType ) {
            lastWetnessType = wetnessType;
            lastFertileType = fertileType;

            world.setBlockState( pos, getBlockState().with( FarmlandBlock.FERTILITY, fertileType )
                                                     .with( FarmlandBlock.WETNESS, wetnessType ) );
        }

        if( ! hasDecay && decay != 0 ) {
            decay = 0;
        }
    }

    @Override
    public int useFertility( int amount ) {
        if( fertility > 0 ) {
            int last = fertility;
            fertility -= amount;
            if( fertility < 0 ) fertility = 0;
            markDirty();
            return last - fertility;
        }
        return 0;
    }

    @Override
    public int useSaltiness( int amount ) {
        if( fertility < 0 ) {
            int last = fertility;
            fertility += amount;
            if( fertility > 0 ) fertility = 0;
            markDirty();
            return fertility - last;
        }
        return 0;
    }

    @Override
    public boolean addFertility( int amount ) {
        if( fertility < 0 ) {
            return false;
        }
        if( fertility >= maxFertility ) {
            return false;
        }
        fertility += amount;
        if( fertility > maxFertility ) fertility = maxFertility;
        markDirty();
        return true;
    }

    @Override
    public boolean addSaltiness( int amount ) {
        if( fertility > 0 ) {
            return false;
        }
        if( - fertility >= maxSaltiness ) {
            return false;
        }
        fertility += amount;
        if( - fertility > maxSaltiness ) fertility = - maxSaltiness;
        markDirty();
        return true;
    }

    @Override
    public void flood() {
        wetness = maxFloodedUpdates;
    }

    @Override
    public void unflood( int amount ) {
        if( wetness > 0 ) {
            wetness -= amount;
            if( wetness < 0 ) wetness = 0;
            markDirty();
        }
    }

    @Override
    public void dryout( int amount ) {
        if( wetness > - 1 ) {
            wetness -= amount;
            if( wetness < - 1 ) wetness = - 1;
            markDirty();
        }
    }

    @Override
    public int decay( int amount ) {
        if( decay < maxDecay ) {
            int last = decay;
            decay += amount;
            if( decay > maxDecay ) decay = maxDecay;
            markDirty();
            return decay - last;
        }
        return 0;
    }

    @Override
    public int undecay( int amount ) {
        if( decay > 0 ) {
            int last = decay;
            decay -= amount;
            if( decay < 0 ) decay = 0;
            markDirty();
            return last - decay;
        }
        return 0;
    }

    @Override
    public boolean isFertile( int amount ) {
        return fertility >= amount;
    }

    @Override
    public boolean isSalty( int amount ) {
        return - fertility >= amount;
    }

    @Override
    public boolean isDecayed( int amount ) {
        return decay >= amount;
    }

    @Override
    public boolean isFlooded( int amount ) {
        return wetness >= amount;
    }

    @Override
    public boolean isFertile() {
        return isFertile( 1 );
    }

    @Override
    public boolean isSalty() {
        return isSalty( 1 );
    }

    @Override
    public boolean isDecayed() {
        return isDecayed( maxDecay );
    }

    @Override
    public boolean isFlooded() {
        return isFlooded( 1 );
    }

    @Override
    public boolean isWet() {
        return wetness == 0;
    }

    @Override
    public boolean isWetOrFlooded() {
        return isFlooded( 0 );
    }

    @Override
    public boolean isDry() {
        return wetness < 0;
    }

    @Override
    public int getFertility() {
        return fertility;
    }

    @Override
    public int getWetness() {
        return wetness;
    }

    @Override
    public int getDecay() {
        return decay;
    }

    @Override
    public int getSaltiness() {
        return - fertility;
    }

    @Override
    public int getMaxFertility() {
        return maxFertility;
    }

    @Override
    public int getMaxDecay() {
        return maxDecay;
    }

    @Override
    public int getMaxSaltiness() {
        return maxSaltiness;
    }

    @Override
    public int getMaxFloodedUpdates() {
        return maxFloodedUpdates;
    }

    public void randomUpdate( Random rand ) {
        if( hasDecay ) {
            if( fertility > 0 && decay < maxDecay ) {
                boolean canDecay = true;
                int decayAmount = 3;
                if( fertility < maxFertility * 3 / 4 ) {
                    decayAmount = 1;
                }
                if( fertility < maxFertility / 2 ) {
                    canDecay = rand.nextBoolean();
                }
                if( fertility < maxFertility / 4 ) {
                    canDecay = rand.nextInt( 3 ) == 0;
                }
                if( canDecay ) {
                    decay( rand.nextInt( decayAmount ) + 1 );
                }
            }

            if( fertility < 0 && decay > 0 ) {
                int saltiness = - fertility;

                boolean canUndecay = true;
                int undecayAmount = 3;
                if( saltiness < maxSaltiness * 3 / 4 ) {
                    undecayAmount = 1;
                }
                if( saltiness < maxSaltiness / 2 ) {
                    canUndecay = rand.nextBoolean();
                }
                if( saltiness < maxSaltiness / 4 ) {
                    canUndecay = rand.nextInt( 3 ) == 0;
                }
                if( canUndecay ) {
                    undecay( rand.nextInt( undecayAmount ) + 1 );
                }
            }
        }

        if( isFlooded() ) {
            unflood( 1 );

            if( isSalty() && rand.nextBoolean() ) {
                useSaltiness( 1 );
            }
        }
    }

    private void updateProperties() {
        Fertility fertileType = Fertility.NOT_FERTILE;
        if( fertility > 0 ) {
            fertileType = Fertility.FERTILE;
        } else if( fertility < 0 ) {
            fertileType = Fertility.SALINE;
        }
        if( decay >= maxDecay ) {
            fertileType = Fertility.DECAYED;
        }

        Wetness wetnessType = Wetness.DRY;
        if( wetness > 0 ) {
            wetnessType = Wetness.FLOODED;
        } else if( wetness == 0 ) {
            wetnessType = Wetness.WET;
        }

        this.fertileType = fertileType;
        this.wetnessType = wetnessType;
    }

    private void invalidateState() {
        updateContainingBlockInfo(); // Make sure we use the right blockstate
        lastWetnessType = getBlockState().get( FarmlandBlock.WETNESS );
        lastFertileType = getBlockState().get( FarmlandBlock.FERTILITY );
    }

    @Override
    public void markDirty() {
        super.markDirty();
        needsUpdate = true;
    }

    @Override
    public CompoundNBT write( CompoundNBT nbt ) {
        nbt.putInt( "MaxFertility", maxFertility );
        nbt.putInt( "MaxSaltiness", maxSaltiness );
        nbt.putInt( "MaxDecay", maxDecay );
        nbt.putInt( "MaxFloodedUpdates", maxFloodedUpdates );
        nbt.putBoolean( "HasDecay", hasDecay );
        nbt.putInt( "Fertility", fertility );
        nbt.putInt( "Decay", decay );
        nbt.putInt( "Wetness", wetness );
        return nbt;
    }

    @Override
    public void read( CompoundNBT nbt ) {
        maxFertility = NBTUtil.getOrDefault( nbt, "MaxFertility", 24 );
        maxSaltiness = NBTUtil.getOrDefault( nbt, "MaxSaltiness", 24 );
        maxDecay = NBTUtil.getOrDefault( nbt, "MaxDecay", 24 );
        maxFloodedUpdates = NBTUtil.getOrDefault( nbt, "MaxFloodedUpdates", 17 );
        hasDecay = NBTUtil.getOrDefault( nbt, "HasDecay", true );
        fertility = NBTUtil.getOrDefault( nbt, "Fertility", 0 );
        decay = NBTUtil.getOrDefault( nbt, "Decay", 0 );
        wetness = NBTUtil.getOrDefault( nbt, "Wetness", 0 );

        needsUpdate = true;
        invalidateState();
    }
}
