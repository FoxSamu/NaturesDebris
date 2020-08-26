package natures.debris.common.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.StringTextComponent;

import static net.minecraft.command.Commands.*;

public class NdDebugCommand {
    private static boolean sendMobPaths = false;

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
            literal(
                "nddebug"
            ).requires(
                src -> src.hasPermissionLevel(3)
            ).then(
                literal(
                    "send_mob_paths"
                ).then(
                    literal(
                        "on"
                    ).executes(cmd -> {
                        sendMobPaths = true;
                        cmd.getSource().sendFeedback(new StringTextComponent("Showing mob paths."), true);
                        return 0;
                    })
                ).then(
                    literal(
                        "off"
                    ).executes(cmd -> {
                        sendMobPaths = false;
                        cmd.getSource().sendFeedback(new StringTextComponent("No longer showing mob paths."), true);
                        return 0;
                    })
                )
            )
        );
    }

    public static boolean sendMobPaths() {
        return sendMobPaths;
    }
}
