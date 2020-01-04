/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 04 - 2020
 * Author: rgsw
 */

package runs.generator.template;

public class FullSetTemplate extends Template {
    public FullSetTemplate( String name, boolean addWall ) {
        super( modIDFromName( name ) );

        addChild( new FullBlockTemplate( name ) );
        addChild( new SlabTemplate( name + "_slab", name ) );
        addChild( new CornerTemplate( name + "_corner", name ) );
        addChild( new StairsTemplate( name + "_stairs", "%_inner", "%_outer", name ) );
        addChild( new StepTemplate( name + "_step", "%_inner", "%_outer", name ) );

        if( addWall ) {
            addChild( new WallTemplate( name + "_wall", "%_post", "%_side", name ) );
        }
    }
}
