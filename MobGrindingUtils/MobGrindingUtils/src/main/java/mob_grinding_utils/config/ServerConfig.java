package mob_grinding_utils.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfig {
    private static final ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();
    public static ForgeConfigSpec SERVER_CONFIG;

    public static ForgeConfigSpec.IntValue MASHER_MAX_UPGRADES;

    static {
        MASHER_MAX_UPGRADES = SERVER_BUILDER.comment("Max upgrades for masher").defineInRange("masherMaxUpgrade", 10,0,10);

        SERVER_CONFIG = SERVER_BUILDER.build();
    }
}
