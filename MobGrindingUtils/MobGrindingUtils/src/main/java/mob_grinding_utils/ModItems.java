package mob_grinding_utils;

import java.util.ArrayList;
import java.util.List;

import mob_grinding_utils.items.ItemAbsorptionUpgrade;
import mob_grinding_utils.items.ItemFanUpgrade;
import mob_grinding_utils.items.ItemGMChickenFeed;
import mob_grinding_utils.items.ItemImaginaryInvisibleNotReallyThereSword;
import mob_grinding_utils.items.ItemMobSwab;
import mob_grinding_utils.items.ItemSawUpgrade;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class ModItems {
	public static Item FAN_UPGRADE, ABSORPTION_UPGRADE, MOB_SWAB, GM_CHICKEN_FEED, SAW_UPGRADE;
	public static ItemSword NULL_SWORD;

	public static void init() {
		FAN_UPGRADE = new ItemFanUpgrade();
		FAN_UPGRADE.setRegistryName("mob_grinding_utils", "fan_upgrade").setUnlocalizedName("mob_grinding_utils.fan_upgrade");

		ABSORPTION_UPGRADE = new ItemAbsorptionUpgrade();
		ABSORPTION_UPGRADE.setRegistryName("mob_grinding_utils", "absorption_upgrade").setUnlocalizedName("mob_grinding_utils.absorption_upgrade");

		SAW_UPGRADE = new ItemSawUpgrade();
		SAW_UPGRADE.setRegistryName("mob_grinding_utils", "saw_upgrade").setUnlocalizedName("mob_grinding_utils.saw_upgrade");

		MOB_SWAB = new ItemMobSwab();
		MOB_SWAB.setRegistryName("mob_grinding_utils", "mob_swab").setUnlocalizedName("mob_grinding_utils.mob_swab");

		GM_CHICKEN_FEED = new ItemGMChickenFeed();
		GM_CHICKEN_FEED.setRegistryName("mob_grinding_utils", "gm_chicken_feed").setUnlocalizedName("mob_grinding_utils.gm_chicken_feed");

		NULL_SWORD = new ItemImaginaryInvisibleNotReallyThereSword();
		NULL_SWORD.setRegistryName("mob_grinding_utils", "null_sword").setUnlocalizedName("mob_grinding_utils.null_sword");
	}

	@Mod.EventBusSubscriber(modid = "mob_grinding_utils")
	public static class RegistrationHandlerItems {
		public static final List<Item> ITEMS = new ArrayList<Item>();

		@SubscribeEvent
		public static void registerItems(final RegistryEvent.Register<Item> event) {
			final Item[] items = {
					FAN_UPGRADE,
					ABSORPTION_UPGRADE,
					MOB_SWAB,
					GM_CHICKEN_FEED,
					SAW_UPGRADE,
					NULL_SWORD
			};
			final IForgeRegistry<Item> registry = event.getRegistry();

			for (final Item item : items) {
				registry.register(item);
				ITEMS.add(item);
			}
		}
	}

}
