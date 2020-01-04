/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 04 - 2020
 * Author: rgsw
 */

package runs.generator.template;

import runs.generator.FileCopy;

public class FullBlockTemplate extends Template {
    public FullBlockTemplate( String name ) {
        super( modIDFromName( name ) );
        addCopy(
            new FileCopy( "templates/blockstates/full_block.json", wrapIntoFolder( name, "assets", "blockstates", ".json" ) )
                .property( "name", toSubfolder( name, "block" ) )
        );
        addCopy(
            new FileCopy( "templates/models/block/full_block.json", wrapIntoFolder( name, "assets", "models/block", ".json" ) )
                .property( "name", toSubfolder( name, "block" ) )
        );
        addCopy(
            new FileCopy( "templates/models/item/parent.json", wrapIntoFolder( name, "assets", "models/item", ".json" ) )
                .property( "name", toSubfolder( name, "block" ) )
        );
        addCopy(
            new FileCopy( "templates/loot_tables/basic.json", wrapIntoFolder( name, "data", "loot_tables/blocks", ".json" ) )
                .property( "dropped_item", name )
        );
    }
}
