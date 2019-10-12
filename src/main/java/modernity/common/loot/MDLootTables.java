package modernity.common.loot;

import modernity.common.loot.func.MulCornerCount;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;

public class MDLootTables {
    public static void register() {
        LootFunctionManager.registerFunction( new MulCornerCount.Serializer() );
    }
}
