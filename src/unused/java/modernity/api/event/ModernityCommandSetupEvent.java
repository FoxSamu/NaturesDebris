/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.api.event;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraftforge.eventbus.api.Event;

import java.util.List;

/**
 * Fired when setting up modernity command ({@code /modernity}) so that other mods can add commands...
 *
 * @author RGSW
 */
public class ModernityCommandSetupEvent extends Event {
    private final List<LiteralArgumentBuilder<CommandSource>> commandList;

    public ModernityCommandSetupEvent( List<LiteralArgumentBuilder<CommandSource>> commandList ) {
        this.commandList = commandList;
    }

    public void registerCommand( LiteralArgumentBuilder<CommandSource> builder ) {
        commandList.add( builder );
    }
}
