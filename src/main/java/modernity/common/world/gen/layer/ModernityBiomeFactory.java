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
import net.minecraft.world.gen.IContext;
import net.minecraft.world.gen.IContextExtended;
import net.minecraft.world.gen.area.AreaDimension;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.area.IAreaFactory;

public class ModernityBiomeFactory <T extends IArea> implements IAreaFactory<T> {

    private final MDBiomes.GenProfile profile;
    private final IContextExtended<T> context;

    public ModernityBiomeFactory( EMDDimension dimen, IContextExtended<T> context ) {
        this.profile = MDBiomes.createGenProfile( dimen );
        this.context = context;
    }

    public int apply( IContext context, AreaDimension dimen, int x, int z ) {
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
    public T make( AreaDimension dimens ) {
        return context.makeArea( dimens, ( x, z ) -> {
            context.setPosition( x + dimens.getStartX(), z + dimens.getStartZ() );
            return apply( context, dimens, x, z );
        } );
    }
}
