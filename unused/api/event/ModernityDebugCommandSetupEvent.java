/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 11 - 2019
 */

package modernity.generic.event;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraftforge.eventbus.api.Event;

import java.util.List;

public class ModernityDebugCommandSetupEvent extends Event {
    private final List<LiteralArgumentBuilder<CommandSource>> commandList;

    public ModernityDebugCommandSetupEvent( List<LiteralArgumentBuilder<CommandSource>> commandList ) {
        this.commandList = commandList;
    }

    public void registerCommand( LiteralArgumentBuilder<CommandSource> builder ) {
        commandList.add( builder );
    }
}
