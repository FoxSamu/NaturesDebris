/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 20 - 2019
 * Author: rgsw
 */

package modernity.common.environment.precipitation;

import net.rgsw.MathUtil;

@FunctionalInterface
public interface IPrecipitationFunction {
    IPrecipitation computePrecipitation( int level );

    static IPrecipitationFunction selectFrom( IPrecipitation... types ) {
        return level -> types[ MathUtil.clamp( level, 0, types.length - 1 ) ];
    }

    static IPrecipitationFunction none() {
        return level -> EDefaultPrecipitation.NONE;
    }

    static IPrecipitationFunction standard() {
        return selectFrom(
            EDefaultPrecipitation.NONE,
            EDefaultPrecipitation.DRIZZLE,
            EDefaultPrecipitation.SHOWERS,
            EDefaultPrecipitation.RAIN,
            EDefaultPrecipitation.HAIL
        );
    }

    static IPrecipitationFunction cold() {
        return selectFrom(
            EDefaultPrecipitation.NONE,
            EDefaultPrecipitation.DRIZZLE,
            EDefaultPrecipitation.WET_SNOW,
            EDefaultPrecipitation.SNOW,
            EDefaultPrecipitation.HEAVY_SNOW
        );
    }

    static IPrecipitationFunction swampy() {
        return selectFrom(
            EDefaultPrecipitation.NONE,
            EDefaultPrecipitation.SHOWERS,
            EDefaultPrecipitation.RAIN,
            EDefaultPrecipitation.RAIN
        );
    }
}
