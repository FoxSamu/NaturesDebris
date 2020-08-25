/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package runs.generator.template;

import runs.generator.FileCopy;

import java.util.ArrayList;

public abstract class Template {
    private final ArrayList<FileCopy> fileCopies = new ArrayList<>();
    private final ArrayList<Template> children = new ArrayList<>();
    protected final String modID;

    protected Template(String modID) {
        this.modID = modID;
    }

    protected void addCopy(FileCopy copy) {
        fileCopies.add(copy);
    }

    protected void addChild(Template template) {
        children.add(template);
    }

    public void go() {
        for (FileCopy copy : fileCopies) {
            copy.doCopy();
        }

        for (Template tem : children) {
            tem.go();
        }
    }

    protected static String modIDFromName(String name) {
        int index = name.indexOf(':');
        if (index >= 0) {
            return name.substring(0, index);
        }
        return "minecraft";
    }

    protected static String filenameFromName(String name) {
        int index = name.indexOf(':');
        if (index >= 0) {
            return name.substring(index + 1);
        }
        return name;
    }

    protected static String wrapIntoFolder(String name, String rootFolder, String subfolder, String extension) {
        return String.format("%s/%s/%s/%s%s", rootFolder, modIDFromName(name), subfolder, filenameFromName(name), extension);
    }

    protected static String toSubfolder(String name, String subfolder) {
        return String.format("%s:%s/%s", modIDFromName(name), subfolder, filenameFromName(name));
    }
}
