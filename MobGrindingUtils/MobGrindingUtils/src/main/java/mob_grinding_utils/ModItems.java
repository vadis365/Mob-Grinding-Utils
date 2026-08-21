package mob_grinding_utils;

import com.google.common.collect.ImmutableSet;
import mob_grinding_utils.items.*;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.item.equipment.ArmorType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Set;

// My Generic Item Registry ;)
public class ModItems {
	public static void init(IEventBus bus) {
		ITEMS.register(bus);
	}
	public static DeferredRegister.Items ITEMS = DeferredRegister.createItems(Reference.MOD_ID);

	public static DeferredItem<Item> FAN_UPGRADE_WIDTH = ITEMS.registerItem("fan_upgrade_width", properties -> new ItemFanUpgrade(properties.stacksTo(64), ItemFanUpgrade.UpgradeType.WIDTH));
	public static DeferredItem<Item> FAN_UPGRADE_HEIGHT = ITEMS.registerItem("fan_upgrade_height", properties -> new ItemFanUpgrade(properties.stacksTo(64), ItemFanUpgrade.UpgradeType.HEIGHT));
	public static DeferredItem<Item> FAN_UPGRADE_SPEED = ITEMS.registerItem("fan_upgrade_speed", properties -> new ItemFanUpgrade(properties.stacksTo(64), ItemFanUpgrade.UpgradeType.SPEED));
	public static DeferredItem<Item> ABSORPTION_UPGRADE = ITEMS.registerItem("absorption_upgrade", properties -> new ItemAbsorptionUpgrade(properties.stacksTo(64)));
	public static DeferredItem<Item> SAW_UPGRADE_ARTHROPOD = ITEMS.registerItem("saw_upgrade_arthropod", properties -> new ItemSawUpgrade(properties.stacksTo(64), ItemSawUpgrade.SawUpgradeType.ARTHROPOD));
	public static DeferredItem<Item> SAW_UPGRADE_BEHEADING = ITEMS.registerItem("saw_upgrade_beheading", properties -> new ItemSawUpgrade(properties.stacksTo(64), ItemSawUpgrade.SawUpgradeType.BEHEADING));
	public static DeferredItem<Item> SAW_UPGRADE_FIRE = ITEMS.registerItem("saw_upgrade_fire", properties -> new ItemSawUpgrade(properties.stacksTo(64), ItemSawUpgrade.SawUpgradeType.FIRE));
	public static DeferredItem<Item> SAW_UPGRADE_LOOTING = ITEMS.registerItem("saw_upgrade_looting", properties -> new ItemSawUpgrade(properties.stacksTo(64), ItemSawUpgrade.SawUpgradeType.LOOTING));
	public static DeferredItem<Item> SAW_UPGRADE_SHARPNESS = ITEMS.registerItem("saw_upgrade_sharpness", properties -> new ItemSawUpgrade(properties.stacksTo(64), ItemSawUpgrade.SawUpgradeType.SHARPNESS));
	public static DeferredItem<Item> SAW_UPGRADE_SMITE = ITEMS.registerItem("saw_upgrade_smite", properties -> new ItemSawUpgrade(properties.stacksTo(64), ItemSawUpgrade.SawUpgradeType.SMITE));
	public static DeferredItem<Item> MOB_SWAB = ITEMS.registerItem("mob_swab", properties -> new ItemMobSwab(properties.stacksTo(1), false));
	public static DeferredItem<Item> MOB_SWAB_USED = ITEMS.registerItem("mob_swab_used", properties -> new ItemMobSwab(properties.stacksTo(1), true));
	public static DeferredItem<Item> GM_CHICKEN_FEED = ITEMS.registerItem("gm_chicken_feed", properties -> new ItemGMChickenFeed(properties.stacksTo(1), ItemGMChickenFeed.FeedType.MOB));
	public static DeferredItem<Item> GM_CHICKEN_FEED_CURSED = ITEMS.registerItem("gm_chicken_feed_cursed", properties -> new ItemGMChickenFeed(properties.stacksTo(1), ItemGMChickenFeed.FeedType.CURSED));
	public static DeferredItem<Item> NUTRITIOUS_CHICKEN_FEED = ITEMS.registerItem("nutritious_chicken_feed", properties -> new ItemGMChickenFeed(properties.stacksTo(1), ItemGMChickenFeed.FeedType.NUTRITIOUS));
	public static DeferredItem<Item> FLUID_XP_BUCKET = ITEMS.registerItem("fluid_xp_bucket", properties -> new BucketItem(ModBlocks.FLUID_XP.get(), properties.craftRemainder(Items.BUCKET).stacksTo(1)));
	public static DeferredItem<Item> NULL_SWORD = ITEMS.registerItem("null_sword", ItemImaginaryInvisibleNotReallyThereSword::new);
	public static DeferredItem<Item> ROTTEN_EGG = ITEMS.registerItem("rotten_egg", properties -> new ItemRottenEgg(properties.stacksTo(1)));
	public static DeferredItem<Item> GOLDEN_EGG = ITEMS.registerItem("golden_egg", properties -> new ItemGoldenEgg(properties.stacksTo(1)));
	public static DeferredItem<Item> SOLID_XP_MOULD_BLANK = ITEMS.registerItem("solid_xp_mould_blank", properties -> new ItemSolidXPMould(properties.stacksTo(64), ItemSolidXPMould.Mould.BLANK));
	public static DeferredItem<Item> SOLID_XP_MOULD_BABY = ITEMS.registerItem("solid_xp_mould_baby", properties -> new ItemSolidXPMould(properties.stacksTo(64), ItemSolidXPMould.Mould.BABY));
	public static DeferredItem<Item> SOLID_XP_BABY = ITEMS.registerItem("solid_xp_baby", properties -> new ItemSolidXP(properties.stacksTo(64).food((new FoodProperties.Builder()).nutrition(0).saturationModifier(0F).alwaysEdible().build()), 50));
	public static DeferredItem<Item> XP_SOLIDIFIER_UPGRADE = ITEMS.registerItem("xp_solidifier_upgrade", properties -> new ItemSolidifierUpgrade(properties.stacksTo(64)));
	public static DeferredItem<Item> SPAWNER_UPGRADE_WIDTH = ITEMS.registerItem("spawner_upgrade_width", properties -> new ItemSpawnerUpgrade(properties.stacksTo(64), ItemSpawnerUpgrade.SpawnerUpgrade.WIDTH));
	public static DeferredItem<Item> SPAWNER_UPGRADE_HEIGHT = ITEMS.registerItem("spawner_upgrade_height", properties -> new ItemSpawnerUpgrade(properties.stacksTo(64), ItemSpawnerUpgrade.SpawnerUpgrade.HEIGHT));
	public static DeferredItem<Item> MONOCLE = ITEMS.registerItem("monocle", properties -> new ItemMonocle(properties.stacksTo(1).humanoidArmor(ArmorMaterials.CHAINMAIL, ArmorType.HELMET)));
	//public static RegistryObject<Item> SPAWNEGG = ITEMS.register("witheregg", () -> new SpawnEggItem(EntityType.WITHER, 0x0, 0xffffff, new Item.Properties().group(MobGrindingUtils.TAB)));

	public static final Set<DeferredItem<Item>> TAB_ORDER = ImmutableSet.of(
		FAN_UPGRADE_HEIGHT, FAN_UPGRADE_WIDTH, FAN_UPGRADE_SPEED,
		SAW_UPGRADE_FIRE, SAW_UPGRADE_SMITE, SAW_UPGRADE_ARTHROPOD, SAW_UPGRADE_BEHEADING, SAW_UPGRADE_LOOTING, SAW_UPGRADE_SHARPNESS,
		ABSORPTION_UPGRADE, MOB_SWAB, MOB_SWAB_USED, FLUID_XP_BUCKET, ROTTEN_EGG, GOLDEN_EGG,
		SOLID_XP_MOULD_BLANK, SOLID_XP_MOULD_BABY, SOLID_XP_BABY, XP_SOLIDIFIER_UPGRADE,
		SPAWNER_UPGRADE_HEIGHT, SPAWNER_UPGRADE_WIDTH, MONOCLE, NUTRITIOUS_CHICKEN_FEED, GM_CHICKEN_FEED, GM_CHICKEN_FEED_CURSED
	);
}
