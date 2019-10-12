/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 9 - 8 - 2019
 */

package modernity.api.event;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFixer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.eventbus.api.Event;

public class FixNBTEvent extends Event {
    private final DataFixer dataFixer;
    private final DSL.TypeReference type;
    private CompoundNBT nbt;
    private final int fromVersion;
    private final int toVersion;

    public FixNBTEvent( DataFixer dataFixer, DSL.TypeReference type, CompoundNBT nbt, int fromVersion, int toVersion ) {
        this.dataFixer = dataFixer;
        this.type = type;
        this.nbt = nbt;
        this.fromVersion = fromVersion;
        this.toVersion = toVersion;
    }

    public DataFixer getDataFixer() {
        return dataFixer;
    }

    public DSL.TypeReference getType() {
        return type;
    }

    public CompoundNBT getNBT() {
        return nbt;
    }

    public void setNBT( CompoundNBT nbt ) {
        this.nbt = nbt;
    }

    public int getFromVersion() {
        return fromVersion;
    }

    public int getToVersion() {
        return toVersion;
    }
}
