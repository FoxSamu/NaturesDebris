/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 04 - 2020
 * Author: rgsw
 */

package runs.generator.template;

import runs.generator.FileCopy;

public class CornerTemplate extends Template {
    public CornerTemplate( String cornerName, String fullName ) {
        super( modIDFromName( cornerName ) );
        addCopy(
            new FileCopy( "templates/blockstates/corner.json", wrapIntoFolder( cornerName, "assets", "blockstates", ".json" ) )
                .property( "name", toSubfolder( cornerName, "block" ) )
        );
        addCopy(
            new FileCopy( "templates/models/block/corner.json", wrapIntoFolder( cornerName, "assets", "models/block", ".json" ) )
                .property( "name", toSubfolder( fullName, "block" ) )
        );
        addCopy(
            new FileCopy( "templates/models/item/parent.json", wrapIntoFolder( cornerName, "assets", "models/item", ".json" ) )
                .property( "name", toSubfolder( cornerName, "block" ) )
        );
        addCopy(
            new FileCopy( "templates/loot_tables/corner.json", wrapIntoFolder( cornerName, "data", "loot_tables/blocks", ".json" ) )
                .property( "dropped_item", cornerName )
        );
        addCopy(
            new FileCopy( "templates/recipes/corner.json", wrapIntoFolder( cornerName, "data", "recipes/corners", ".json" ) )
                .property( "ingredient", fullName )
                .property( "result", cornerName )
        );
    }
}
