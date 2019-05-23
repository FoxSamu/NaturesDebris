package modernity.common.registry;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import modernity.common.fluid.FluidEntry;
import modernity.common.fluid.MDFluids;

public class MDRegistries {

    private static ForgeRegistry<FluidEntry> fluids;

    public static ForgeRegistry<FluidEntry> fluids() {
        return fluids;
    }

    public static void register() {
        RegistryBuilder<FluidEntry> commandRegistryBuilder = new RegistryBuilder<>();
        commandRegistryBuilder.setName( new ResourceLocation( "modernity:fluids" ) );
        commandRegistryBuilder.setType( FluidEntry.class );
        commandRegistryBuilder.setIDRange( 5, Integer.MAX_VALUE );
        commandRegistryBuilder.add( (IForgeRegistry.BakeCallback<FluidEntry>) ( owner, stage ) -> MDFluids.inject() );
        fluids = (ForgeRegistry<FluidEntry>) commandRegistryBuilder.create();
    }
}
