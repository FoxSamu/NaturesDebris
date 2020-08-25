/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package runs.generator;

import runs.generator.template.FullBlockTemplate;
import runs.generator.template.Template;

import java.io.File;
import java.util.ArrayList;

public final class Generator {
    public static final File ROOT = new File("src/main/resources");

    private static final ArrayList<Template> TEMPLATES = new ArrayList<>();

    private static void addTemplate(Template tem) {
        TEMPLATES.add(tem);
    }

    private static void go() {
        for (Template tem : TEMPLATES) {
            tem.go();
        }
        TEMPLATES.clear();
    }

    private Generator() {
    }

    public static void main(String[] args) {
        addTemplate(new FullBlockTemplate("modernity:chiseled_rock", "modernity:rock_chiseled"));
        addTemplate(new FullBlockTemplate("modernity:chiseled_darkrock", "modernity:darkrock_chiseled"));
        addTemplate(new FullBlockTemplate("modernity:chiseled_lightrock", "modernity:lightrock_chiseled"));
        addTemplate(new FullBlockTemplate("modernity:chiseled_redrock", "modernity:redrock_chiseled"));

        go();
    }
}
