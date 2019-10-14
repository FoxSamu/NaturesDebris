package modernity.api.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

/**
 * Names all events for {@linkplain IWorld#playEvent(int, BlockPos, int) playEvent} calls
 */
public final class Events {
    public static final int DISPENSER_DISPENSE = 1000;
    public static final int DISPENSER_FAIL = 1001;
    public static final int DISPENSER_LAUNCH = 1002;
    public static final int ENDER_EYE_LAUNCH = 1003;
    public static final int FIREWORK_SHOOT = 1004;
    public static final int IRON_DOOR_OPEN = 1005;
    public static final int WOODEN_DOOR_OPEN = 1006;
    public static final int WOODEN_TRAPDOOR_OPEN = 1007;
    public static final int FENCE_GATE_OPEN = 1008;
    public static final int FIRE_EXTINGUISH = 1009;
    public static final int PLAY_RECORD = 1010;
    public static final int IRON_DOOR_CLOSE = 1011;
    public static final int WOODEN_DOOR_CLOSE = 1012;
    public static final int WOODEN_TRAPDOOR_CLOSE = 1013;
    public static final int FENCE_GATE_CLOSE = 1014;
    public static final int GHAST_WARN = 1015;
    public static final int GHAST_SHOOT = 1016;
    public static final int ENDER_DRAGON_SHOOT = 1017;
    public static final int BLAZE_SHOOT = 1018;
    public static final int ZOMBIE_ATTACK_WOODEN_DOOR = 1019;
    public static final int ZOMBIE_ATTACK_IRON_DOOR = 1020;
    public static final int ZOMBIE_BREAK_WOODEN_DOOR = 1021;
    public static final int WITHER_BREAK_BLOCK = 1022;
    public static final int WITHER_SHOOT = 1024;
    public static final int BAT_TAKEOFF = 1025;
    public static final int ZOMBIE_INFECT = 1026;
    public static final int ZOMBIE_VILLAGER_CONVERTED = 1027;
    public static final int ANVIL_DESTROY = 1029;
    public static final int ANVIL_USE = 1030;
    public static final int ANVIL_LAND = 1031;
    public static final int TRAVEL_PORTAL = 1032;
    public static final int CHORUS_GROW = 1033;
    public static final int CHORUS_DEATH = 1034;
    public static final int BREWING_STAND_BREW = 1035;
    public static final int IRON_TRAPDOOR_CLOSE = 1036;
    public static final int IRON_TRAPDOOR_OPEN = 1037;
    public static final int PHANTOM_BITE = 1039;
    public static final int ZOMBIE_TO_DROWNED = 1040;
    public static final int HUSK_TO_ZOMBIE = 1041;

    public static final int DISPENSER_PARTICLES = 2000;
    public static final int BREAK_BLOCK = 2001;
    public static final int POTION_SPLASH = 2002;
    public static final int ENDER_EYE_BREAK = 2003;
    public static final int MOB_SPAWNER_AMBIENT = 2004;
    public static final int FERTILIZER_USE = 2005;
    public static final int DRAGON_FIREBALL_HIT = 2006;
    public static final int INSTANT_POTION_SPLASH = 2007;

    public static final int SUMMON_END_GATEWAY = 3000;
    public static final int ENDER_DRAGON_GROWL = 3001;

    private Events() {
    }
}