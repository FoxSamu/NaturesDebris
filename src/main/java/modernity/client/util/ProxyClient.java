package modernity.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import modernity.client.render.block.MDBlockRendererDispatcher;
import modernity.common.block.MDBlocks;
import modernity.common.item.MDItems;
import modernity.common.util.ProxyCommon;


public class ProxyClient extends ProxyCommon {
    public final Minecraft mc = Minecraft.getInstance();

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void loadComplete() {
        super.loadComplete();

        MDBlockRendererDispatcher dispatcher = new MDBlockRendererDispatcher( mc.getModelManager().getBlockModelShapes(), mc.getBlockColors() );
        ObfuscationReflectionHelper.setPrivateValue( Minecraft.class, mc, dispatcher, "field_175618_aM" );

        addResourceManagerReloadListener( dispatcher );

        MDBlocks.registerClient( mc.getBlockColors(), mc.getItemColors() );
        MDItems.registerClient( mc.getItemColors() );
    }

    @Override
    public void registerListeners() {
        super.registerListeners();
//        MinecraftForge.EVENT_BUS.register( new FogEffectHandler() );
    }

    @SuppressWarnings( "deprecation" )
    public void addResourceManagerReloadListener( IResourceManagerReloadListener listener ) {
        IResourceManager manager = mc.getResourceManager();
        if( manager instanceof IReloadableResourceManager ) {
            ( (IReloadableResourceManager) manager ).addReloadListener( listener );
        }
    }
}