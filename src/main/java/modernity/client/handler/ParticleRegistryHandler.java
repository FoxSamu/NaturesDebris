package modernity.client.handler;

import modernity.common.particle.MDParticleTypes;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public enum ParticleRegistryHandler {
    INSTANCE;

    @SubscribeEvent
    public void registerParticleFactories( ParticleFactoryRegisterEvent event ) {
        MDParticleTypes.setupFactories( Minecraft.getInstance().particles );
    }
}
