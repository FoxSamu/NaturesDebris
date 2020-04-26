/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 22 - 2019
 * Author: rgsw
 */

package modernity.generic.dimension;

import modernity.client.environment.Fog;
import modernity.client.environment.Light;
import modernity.client.environment.Precipitation;
import modernity.client.environment.Sky;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Dimensions implementing this interface receive events when they need to update specific environment factors.
 */
public interface IEnvironmentDimension {
    @OnlyIn( Dist.CLIENT )
    void updateFog( Fog fog );
    void updateSky( Sky sky );
    void updatePrecipitation( Precipitation prec );
    void updateLight( Light light );
}
