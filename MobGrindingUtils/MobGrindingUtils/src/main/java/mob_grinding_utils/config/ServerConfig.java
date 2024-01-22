package mob_grinding_utils.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfig {
    private static final ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();
    public static ForgeConfigSpec SERVER_CONFIG;

    public static ForgeConfigSpec.IntValue MASHER_MAX_UPGRADES;
    public static ForgeConfigSpec.BooleanValue FAN_REINFORCED_BLADES;

    static {
        MASHER_MAX_UPGRADES = SERVER_BUILDER.comment("Max upgrades for masher").defineInRange("masherMaxUpgrade", 10,0,10);
        FAN_REINFORCED_BLADES = SERVER_BUILDER.comment("Fan blades are stronger, fan is only blocked by more solid blocks").define("fanStrongerBlades", false);

        SERVER_CONFIG = SERVER_BUILDER.build();
    }
}
