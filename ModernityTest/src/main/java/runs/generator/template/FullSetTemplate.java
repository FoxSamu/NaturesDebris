/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
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

    public FullSetTemplate( String name, String textureName, boolean addWall ) {
        super( modIDFromName( name ) );

        addChild( new FullBlockTemplate( name, textureName ) );
        addChild( new SlabTemplate( name + "_slab", name, textureName ) );
        addChild( new CornerTemplate( name + "_corner", name, textureName ) );
        addChild( new StairsTemplate( name + "_stairs", "%_inner", "%_outer", name, textureName ) );
        addChild( new StepTemplate( name + "_step", "%_inner", "%_outer", name, textureName ) );

        if( addWall ) {
            addChild( new WallTemplate( name + "_wall", "%_post", "%_side", name, textureName ) );
        }
    }
}
