/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package runs.generator.template;

import runs.generator.FileCopy;

public class FullBlockTemplate extends Template {
    public FullBlockTemplate( String name ) {
        this( name, name );
    }

    public FullBlockTemplate( String name, String textureName ) {
        super( modIDFromName( name ) );
        addCopy(
            new FileCopy( "templates/blockstates/full_block.json", wrapIntoFolder( name, "assets", "blockstates", ".json" ) )
                .property( "name", toSubfolder( name, "block" ) )
        );
        addCopy(
            new FileCopy( "templates/models/block/full_block.json", wrapIntoFolder( name, "assets", "models/block", ".json" ) )
                .property( "name", toSubfolder( textureName, "block" ) )
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
