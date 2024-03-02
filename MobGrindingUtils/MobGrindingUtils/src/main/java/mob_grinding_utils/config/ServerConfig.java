package mob_grinding_utils.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ServerConfig {
    private static final ModConfigSpec.Builder SERVER_BUILDER = new ModConfigSpec.Builder();
    public static ModConfigSpec SERVER_CONFIG;

    public static ModConfigSpec.IntValue MASHER_MAX_UPGRADES;
    public static ModConfigSpec.BooleanValue FAN_REINFORCED_BLADES;

    static {
        MASHER_MAX_UPGRADES = SERVER_BUILDER.comment("Max upgrades for masher").defineInRange("masherMaxUpgrade", 10,0,10);
        FAN_REINFORCED_BLADES = SERVER_BUILDER.comment("Fan blades are stronger, fan is only blocked by more solid blocks").define("fanStrongerBlades", false);

        SERVER_CONFIG = SERVER_BUILDER.build();
    }
}
