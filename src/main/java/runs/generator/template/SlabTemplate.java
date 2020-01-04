/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 04 - 2020
 * Author: rgsw
 */

package runs.generator.template;

import runs.generator.FileCopy;

public class SlabTemplate extends Template {
    public SlabTemplate( String slabName, String fullName ) {
        super( modIDFromName( slabName ) );
        addCopy(
            new FileCopy( "templates/blockstates/slab.json", wrapIntoFolder( slabName, "assets", "blockstates", ".json" ) )
                .property( "slab_name", toSubfolder( slabName, "block" ) )
                .property( "full_name", toSubfolder( fullName, "block" ) )
        );
        addCopy(
            new FileCopy( "templates/models/block/slab.json", wrapIntoFolder( slabName, "assets", "models/block", ".json" ) )
                .property( "name", toSubfolder( fullName, "block" ) )
        );
        addCopy(
            new FileCopy( "templates/models/item/parent.json", wrapIntoFolder( slabName, "assets", "models/item", ".json" ) )
                .property( "name", toSubfolder( slabName, "block" ) )
        );
        addCopy(
            new FileCopy( "templates/loot_tables/slab.json", wrapIntoFolder( slabName, "data", "loot_tables/blocks", ".json" ) )
                .property( "block", slabName )
                .property( "dropped_item", slabName )
        );
        addCopy(
            new FileCopy( "templates/recipes/slab.json", wrapIntoFolder( slabName, "data", "recipes/slabs", ".json" ) )
                .property( "ingredient", fullName )
                .property( "result", slabName )
        );
    }
}
