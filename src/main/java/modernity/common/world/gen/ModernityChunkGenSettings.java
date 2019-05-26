package modernity.common.world.gen;

import net.minecraft.block.state.IBlockState;
import net.minecraft.world.gen.IChunkGenSettings;

public class ModernityChunkGenSettings implements IChunkGenSettings {
    @Override
    public int getVillageDistance() {
        return 0;
    }

    @Override
    public int getVillageSeparation() {
        return 0;
    }

    @Override
    public int getOceanMonumentSpacing() {
        return 0;
    }

    @Override
    public int getOceanMonumentSeparation() {
        return 0;
    }

    @Override
    public int getStrongholdDistance() {
        return 0;
    }

    @Override
    public int getStrongholdCount() {
        return 0;
    }

    @Override
    public int getStrongholdSpread() {
        return 0;
    }

    @Override
    public int getBiomeFeatureDistance() {
        return 0;
    }

    @Override
    public int getBiomeFeatureSeparation() {
        return 0;
    }

    @Override
    public int getShipwreckDistance() {
        return 0;
    }

    @Override
    public int getShipwreckSeparation() {
        return 0;
    }

    @Override
    public int getOceanRuinDistance() {
        return 0;
    }

    @Override
    public int getOceanRuinSeparation() {
        return 0;
    }

    @Override
    public int getEndCityDistance() {
        return 0;
    }

    @Override
    public int getEndCitySeparation() {
        return 0;
    }

    @Override
    public int getMansionDistance() {
        return 0;
    }

    @Override
    public int getMansionSeparation() {
        return 0;
    }

    @Override
    public IBlockState getDefaultBlock() {
        return null;
    }

    @Override
    public IBlockState getDefaultFluid() {
        return null;
    }
}
