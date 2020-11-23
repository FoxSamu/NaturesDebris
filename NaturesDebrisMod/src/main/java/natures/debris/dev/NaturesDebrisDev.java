package natures.debris.dev;

import org.lwjgl.glfw.GLFW;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.InputMappings;

import natures.debris.common.dev.IDevManager;
import natures.debris.client.NaturesDebrisClient;

public class NaturesDebrisDev extends NaturesDebrisClient {
    private boolean devScreenEnabled = false;
    private boolean f7Held = false;

    @Override
    protected void registerEventListeners() {
        super.registerEventListeners();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Minecraft mc = Minecraft.getInstance();
            if (InputMappings.isKeyDown(mc.getWindow().getHandle(), GLFW.GLFW_KEY_F7)) {
                if (!f7Held) {
                    devScreenEnabled = !devScreenEnabled;
                    f7Held = true;
                }
            } else {
                f7Held = false;
            }

            boolean render = devScreenEnabled && !mc.gameSettings.hideGUI;
            Debug.render(event.renderTickTime, mc.mouseHelper.getMouseX(), mc.mouseHelper.getMouseY(), render);
        }
    }

    @Override
    public IDevManager getDevManager() {
        return IdeDevManager.INSTANCE;
    }
}
