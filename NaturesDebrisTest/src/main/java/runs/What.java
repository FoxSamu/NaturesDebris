/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package runs;

import com.google.gson.JsonPrimitive;
import net.minecraft.advancements.criterion.NBTPredicate;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.JsonToNBT;

public final class What {
    private What() {
    }

    public static void main(String[] args) throws Exception {
        INBT nbt = JsonToNBT.getTagFromJson("{\"bounced\": false}");
        NBTPredicate pred = NBTPredicate.deserialize(new JsonPrimitive("{\"bounced\": true}"));
        System.out.println(pred.test(nbt));
    }
}
