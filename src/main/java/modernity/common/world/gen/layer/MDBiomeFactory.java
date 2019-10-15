/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 11 - 2019
 */

package modernity.common.world.gen.layer;

import modernity.api.util.EMDDimension;
import modernity.common.biome.MDBiomes;
import net.minecraft.world.gen.IExtendedNoiseRandom;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.area.IAreaFactory;

/**
 * Area factory that generates the main Modernity biome layer. It randomly selects biomes based on weights defined in a
 * {@link MDBiomes.GenProfile}.
 */
public class MDBiomeFactory<T extends IArea> implements IAreaFactory<T> {

    private final MDBiomes.GenProfile profile;
    private final IExtendedNoiseRandom<T> context;

    public MDBiomeFactory( EMDDimension dimen, IExtendedNoiseRandom<T> context ) {
        this.profile = MDBiomes.createGenProfile( dimen );
        this.context = context;
    }

    /**
     * Generates a biome at the specific position (such a position is usually a wide area that spans a full biome).
     */
    public int apply( INoiseRandom context, int x, int z ) {
        int rand = context.random( profile.totalWeight );
        int wg = 0;
        int biomeID = - 1;
        for( int i = 0; i < profile.biomeIDs.length; i++ ) {
            wg += profile.weights[ i ];
            if( wg > rand ) {
                biomeID = profile.biomeIDs[ i ];
                break;
            }
        }
        return biomeID;
    }

    @Override
    public T make() {
        return context.func_212861_a_( ( x, z ) -> {
            context.setPosition( x, z );
            return apply( context, x, z );
        } );
    }
}
