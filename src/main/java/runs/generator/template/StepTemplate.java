/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 04 - 2020
 * Author: rgsw
 */

package runs.generator.template;

import runs.generator.FileCopy;

public class StepTemplate extends Template {
    public StepTemplate( String stairsName, String innerName, String outerName, String fullName ) {
        super( modIDFromName( stairsName ) );
        innerName = innerName.replaceAll( "^%", stairsName );
        outerName = outerName.replaceAll( "^%", stairsName );
        addCopy(
            new FileCopy( "templates/blockstates/stairs_step.json", wrapIntoFolder( stairsName, "assets", "blockstates", ".json" ) )
                .property( "regular_name", toSubfolder( stairsName, "block" ) )
                .property( "outer_name", toSubfolder( outerName, "block" ) )
                .property( "inner_name", toSubfolder( innerName, "block" ) )
        );
        addCopy(
            new FileCopy( "templates/models/block/step.json", wrapIntoFolder( stairsName, "assets", "models/block", ".json" ) )
                .property( "name", toSubfolder( fullName, "block" ) )
        );
        addCopy(
            new FileCopy( "templates/models/block/outer_step.json", wrapIntoFolder( outerName, "assets", "models/block", ".json" ) )
                .property( "name", toSubfolder( fullName, "block" ) )
        );
        addCopy(
            new FileCopy( "templates/models/block/inner_step.json", wrapIntoFolder( innerName, "assets", "models/block", ".json" ) )
                .property( "name", toSubfolder( fullName, "block" ) )
        );
        addCopy(
            new FileCopy( "templates/models/item/parent.json", wrapIntoFolder( stairsName, "assets", "models/item", ".json" ) )
                .property( "name", toSubfolder( stairsName, "block" ) )
        );
        addCopy(
            new FileCopy( "templates/loot_tables/basic.json", wrapIntoFolder( stairsName, "data", "loot_tables/blocks", ".json" ) )
                .property( "dropped_item", stairsName )
        );
        addCopy(
            new FileCopy( "templates/recipes/step.json", wrapIntoFolder( stairsName, "data", "recipes/steps", ".json" ) )
                .property( "ingredient", fullName )
                .property( "result", stairsName )
        );
    }
}
