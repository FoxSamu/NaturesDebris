package modernity.api.event;

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
