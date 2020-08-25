/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package runs.generator.template;

import runs.generator.FileCopy;

public class CornerTemplate extends Template {
    public CornerTemplate(String cornerName, String fullName) {
        this(cornerName, fullName, fullName);
    }

    public CornerTemplate(String cornerName, String fullName, String textureName) {
        super(modIDFromName(cornerName));
        addCopy(
            new FileCopy("templates/blockstates/corner.json", wrapIntoFolder(cornerName, "assets", "blockstates", ".json"))
                .property("name", toSubfolder(cornerName, "block"))
        );
        addCopy(
            new FileCopy("templates/models/block/corner.json", wrapIntoFolder(cornerName, "assets", "models/block", ".json"))
                .property("name", toSubfolder(textureName, "block"))
        );
        addCopy(
            new FileCopy("templates/models/item/parent.json", wrapIntoFolder(cornerName, "assets", "models/item", ".json"))
                .property("name", toSubfolder(cornerName, "block"))
        );
        addCopy(
            new FileCopy("templates/loot_tables/corner.json", wrapIntoFolder(cornerName, "data", "loot_tables/blocks", ".json"))
                .property("dropped_item", cornerName)
        );
        addCopy(
            new FileCopy("templates/recipes/corner.json", wrapIntoFolder(cornerName, "data", "recipes/corners", ".json"))
                .property("ingredient", fullName)
                .property("result", cornerName)
        );
    }
}
