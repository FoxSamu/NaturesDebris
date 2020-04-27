/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.block.dirt.logic;

import modernity.common.block.dirt.DirtlikeBlock;
import modernity.common.block.dirt.ISnowyDirtlikeBlock;
import modernity.common.util.MDLightUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.redgalaxy.util.Lazy;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Supplier;

public abstract class DirtLogic {
    private final Lazy<DirtlikeBlock> block;
    private final IDirtLogicType type;

    public DirtLogic( Supplier<? extends DirtlikeBlock> block, IDirtLogicType type ) {
        this.block = Lazy.of( block );
        this.type = type;
    }

    public static boolean canRemain( World world, BlockPos pos, BlockState state ) {
        BlockPos up = pos.up();
        BlockState upState = world.getBlockState( up );
        if( upState.getBlock() == Blocks.SNOW && upState.get( SnowBlock.LAYERS ) == 1 ) {
            return true;
        } else {
            int opacity = MDLightUtil.getEffectiveOpacity( world, state, pos, upState, up, Direction.UP, upState.getOpacity( world, up ) );
            return opacity < world.getMaxLightLevel();
        }
    }

    public IDirtLogicType getType() {
        return type;
    }

    public DirtlikeBlock getBlock() {
        return block.get();
    }

    public BlockState switchState( IWorldReader world, BlockPos pos, BlockState oldState ) {
        BlockState state = getBlock().getDefaultState();
        if( getBlock() instanceof ISnowyDirtlikeBlock ) {
            state = ISnowyDirtlikeBlock.makeSnowy( world, pos, state );
        }
        return state;
    }

    public abstract DirtLogic switchTo( IDirtLogicType type );

    public abstract boolean canSwitchTo( IDirtLogicType type );

    public static boolean switchType( World world, BlockPos pos, IDirtLogicType type ) {
        BlockState state = world.getBlockState( pos );
        Block block = state.getBlock();
        if( block instanceof DirtlikeBlock ) {
            DirtLogic logic = ( (DirtlikeBlock) block ).getLogic();

            if( logic.canSwitchTo( type ) && logic.getType().canSwitchTo( logic, type ) ) {
                DirtLogic newLogic = logic.switchTo( type );
                world.setBlockState( pos, newLogic.switchState( world, pos, state ) );
                return true;
            }
        }
        return false;
    }

    @Nullable
    public static DirtLogic getLogic( World world, BlockPos pos ) {
        BlockState state = world.getBlockState( pos );
        Block block = state.getBlock();
        if( block instanceof DirtlikeBlock ) {
            return ( (DirtlikeBlock) block ).getLogic();
        }
        return null;
    }

    public void blockUpdate( World world, BlockPos pos, BlockState state, Random rand ) {
        type.blockUpdate( world, pos, state, rand, this );
    }

    public boolean randomTicks() {
        return type.randomTicks();
    }

    public boolean canGrow( ItemStack stack ) {
        return type.canGrow( stack );
    }

    public void grow( World world, BlockPos pos, BlockState state, Random rand ) {
        type.grow( world, pos, state, rand );
    }
}
