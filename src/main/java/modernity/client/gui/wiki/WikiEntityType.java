/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 23 - 2019
 */

package modernity.client.gui.wiki;

import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NBTTagCompound;

public class WikiEntityType {
    public final EntityType type;
    public final NBTTagCompound entityNBT;

    public WikiEntityType( EntityType type, NBTTagCompound entityNBT ) {
        this.type = type;
        this.entityNBT = entityNBT;
    }
}
