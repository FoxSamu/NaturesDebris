/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.generator.biome.profile;

public enum DefaultBiomeRarity implements IBiomeRarity {
    EXTREMELY_COMMON(1250),
    VERY_COMMON(1000),
    COMMON(800),
    RELATIVELY_COMMON(600),
    RELATIVELY_UNCOMMON(400),
    UNCOMMON(400),
    RELATIVELY_RARE(100),
    RARE(75),
    VERY_RARE(50),
    EXTREMELY_RARE(25),
    NOT_GENERATING(0);

    private final int weight;

    DefaultBiomeRarity(int weight) {
        this.weight = weight;
    }

    @Override
    public int weight() {
        return weight;
    }
}
