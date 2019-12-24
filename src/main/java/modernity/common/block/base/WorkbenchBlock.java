/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 24 - 2019
 * Author: rgsw
 */

package modernity.common.block.base;

import modernity.common.container.WorkbenchContainer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.stats.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

@SuppressWarnings( "deprecation" )
public class WorkbenchBlock extends HorizontalFacingBlock {
    private static final ITextComponent TITLE = new TranslationTextComponent( "container.crafting" );

    public WorkbenchBlock( Properties properties ) {
        super( properties );
    }

    @Override
    public boolean onBlockActivated( BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit ) {
        player.openContainer( state.getContainer( world, pos ) );
        player.addStat( Stats.INTERACT_WITH_CRAFTING_TABLE );
        return true;
    }

    @Override
    public INamedContainerProvider getContainer( BlockState state, World world, BlockPos pos ) {
        return new SimpleNamedContainerProvider(
            ( windowID, playerInv, player ) -> new WorkbenchContainer(
                windowID, playerInv,
                IWorldPosCallable.of( world, pos )
            ),
            TITLE
        );
    }
}
