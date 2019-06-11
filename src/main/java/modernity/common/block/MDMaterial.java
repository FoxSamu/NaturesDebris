/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 12 - 2019
 */

package modernity.common.block;

import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;

public class MDMaterial {
    public static final Material ASH = new Builder( MaterialColor.BLACK_TERRACOTTA ).build();
    public static final Material ASPHALT = new Builder( MaterialColor.BLACK ).requiresTool().build();


    public static class Builder {
        private EnumPushReaction pushReaction = EnumPushReaction.NORMAL;
        private boolean blocksMovement = true;
        private boolean canBurn;
        private boolean requiresNoTool = true;
        private boolean isLiquid;
        private boolean isReplaceable;
        private boolean isSolid = true;
        private final MaterialColor color;
        private boolean isOpaque = true;

        public Builder( MaterialColor color ) {
            this.color = color;
        }

        public Builder liquid() {
            this.isLiquid = true;
            return this;
        }

        public Builder notSolid() {
            this.isSolid = false;
            return this;
        }

        public Builder doesNotBlockMovement() {
            this.blocksMovement = false;
            return this;
        }

        public Builder notOpaque() {
            this.isOpaque = false;
            return this;
        }

        public Builder requiresTool() {
            this.requiresNoTool = false;
            return this;
        }

        public Builder flammable() {
            this.canBurn = true;
            return this;
        }

        public Builder replaceable() {
            this.isReplaceable = true;
            return this;
        }

        public Builder pushDestroys() {
            this.pushReaction = EnumPushReaction.DESTROY;
            return this;
        }

        public Builder pushBlocks() {
            this.pushReaction = EnumPushReaction.BLOCK;
            return this;
        }

        public Material build() {
            return new Material( this.color, this.isLiquid, this.isSolid, this.blocksMovement, this.isOpaque, this.requiresNoTool, this.canBurn, this.isReplaceable, this.pushReaction );
        }
    }
}
