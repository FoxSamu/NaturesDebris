/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.generic.util;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;

import java.util.UUID;

public final class NBTUtil {
    public static final int END = 0;
    public static final int BYTE = 1;
    public static final int SHORT = 2;
    public static final int INT = 3;
    public static final int LONG = 4;
    public static final int FLOAT = 5;
    public static final int DOUBLE = 6;
    public static final int BYTE_ARRAY = 7;
    public static final int STRING = 8;
    public static final int LIST = 9;
    public static final int COMPOUND = 10;
    public static final int INT_ARRAY = 11;
    public static final int LONG_ARRAY = 12;
    public static final int NUMBER = 99;

    private NBTUtil() {
    }

    public static int getOrDefault(CompoundNBT nbt, String key, int def) {
        return nbt.contains(key, NUMBER) ? nbt.getInt(key) : def;
    }

    public static short getOrDefault(CompoundNBT nbt, String key, short def) {
        return nbt.contains(key, NUMBER) ? nbt.getShort(key) : def;
    }

    public static byte getOrDefault(CompoundNBT nbt, String key, byte def) {
        return nbt.contains(key, NUMBER) ? nbt.getByte(key) : def;
    }

    public static boolean getOrDefault(CompoundNBT nbt, String key, boolean def) {
        return nbt.contains(key, NUMBER) ? nbt.getBoolean(key) : def;
    }

    public static long getOrDefault(CompoundNBT nbt, String key, long def) {
        return nbt.contains(key, NUMBER) ? nbt.getLong(key) : def;
    }

    public static float getOrDefault(CompoundNBT nbt, String key, float def) {
        return nbt.contains(key, NUMBER) ? nbt.getFloat(key) : def;
    }

    public static double getOrDefault(CompoundNBT nbt, String key, double def) {
        return nbt.contains(key, NUMBER) ? nbt.getDouble(key) : def;
    }

    public static String getOrDefault(CompoundNBT nbt, String key, String def) {
        return nbt.contains(key, STRING) ? nbt.getString(key) : def;
    }

    public static int[] getOrDefault(CompoundNBT nbt, String key, int[] def) {
        return nbt.contains(key, INT_ARRAY) ? nbt.getIntArray(key) : def;
    }

    public static byte[] getOrDefault(CompoundNBT nbt, String key, byte[] def) {
        return nbt.contains(key, BYTE_ARRAY) ? nbt.getByteArray(key) : def;
    }

    public static long[] getOrDefault(CompoundNBT nbt, String key, long[] def) {
        return nbt.contains(key, LONG_ARRAY) ? nbt.getLongArray(key) : def;
    }

    public static ListNBT getOrDefault(CompoundNBT nbt, String key, ListNBT def) {
        if(nbt.contains(key, LIST)) {
            return (ListNBT) nbt.get(key);
        }
        return def;
    }

    public static ListNBT getOrDefault(CompoundNBT nbt, String key, int type, ListNBT def) {
        if(nbt.contains(key, LIST)) {
            ListNBT value = (ListNBT) nbt.get(key);
            assert value != null;
            if(value.getTagType() != type) return def;
            return value;
        }
        return def;
    }

    public static CompoundNBT getOrDefault(CompoundNBT nbt, String key, CompoundNBT def) {
        return nbt.contains(key, COMPOUND) ? nbt.getCompound(key) : def;
    }

    public static UUID getOrDefault(CompoundNBT nbt, String key, UUID def) {
        return nbt.hasUniqueId(key) ? nbt.getUniqueId(key) : def;
    }
}
