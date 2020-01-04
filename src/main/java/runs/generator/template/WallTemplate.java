/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 04 - 2020
 * Author: rgsw
 */

package runs.generator.template;

import runs.generator.FileCopy;

public class WallTemplate extends Template {
    public WallTemplate( String wallName, String postName, String sideName, String fullName ) {
        super( modIDFromName( wallName ) );
        postName = postName.replaceAll( "^%", wallName );
        sideName = sideName.replaceAll( "^%", wallName );
        addCopy(
            new FileCopy( "templates/blockstates/wall.json", wrapIntoFolder( wallName, "assets", "blockstates", ".json" ) )
                .property( "side_name", toSubfolder( sideName, "block" ) )
                .property( "post_name", toSubfolder( postName, "block" ) )
        );
        addCopy(
            new FileCopy( "templates/models/block/wall_post.json", wrapIntoFolder( postName, "assets", "models/block", ".json" ) )
                .property( "name", toSubfolder( fullName, "block" ) )
        );
        addCopy(
            new FileCopy( "templates/models/block/wall_side.json", wrapIntoFolder( sideName, "assets", "models/block", ".json" ) )
                .property( "name", toSubfolder( fullName, "block" ) )
        );
        addCopy(
            new FileCopy( "templates/models/item/wall.json", wrapIntoFolder( wallName, "assets", "models/item", ".json" ) )
                .property( "name", toSubfolder( fullName, "block" ) )
        );
        addCopy(
            new FileCopy( "templates/loot_tables/basic.json", wrapIntoFolder( wallName, "data", "loot_tables/blocks", ".json" ) )
                .property( "dropped_item", wallName )
        );
        addCopy(
            new FileCopy( "templates/recipes/wall.json", wrapIntoFolder( wallName, "data", "recipes/walls_fences", ".json" ) )
                .property( "ingredient", fullName )
                .property( "result", wallName )
        );
    }
}
