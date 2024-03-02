package mob_grinding_utils;

import com.google.common.collect.ImmutableSet;
import mob_grinding_utils.items.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Set;

// My Generic Item Registry ;)
public class ModItems {
	public static void init(IEventBus bus) {
		ITEMS.register(bus);
	}
	public static DeferredRegister.Items ITEMS = DeferredRegister.createItems(Reference.MOD_ID);

	public static DeferredItem<Item> FAN_UPGRADE_WIDTH = ITEMS.register("fan_upgrade_width", () -> new ItemFanUpgrade(new Item.Properties().stacksTo(64), ItemFanUpgrade.UpgradeType.WIDTH));
	public static DeferredItem<Item> FAN_UPGRADE_HEIGHT = ITEMS.register("fan_upgrade_height", () -> new ItemFanUpgrade(new Item.Properties().stacksTo(64), ItemFanUpgrade.UpgradeType.HEIGHT));
	public static DeferredItem<Item> FAN_UPGRADE_SPEED = ITEMS.register("fan_upgrade_speed", () -> new ItemFanUpgrade(new Item.Properties().stacksTo(64), ItemFanUpgrade.UpgradeType.SPEED));
	public static DeferredItem<Item> ABSORPTION_UPGRADE = ITEMS.register("absorption_upgrade", () -> new ItemAbsorptionUpgrade(new Item.Properties().stacksTo(64)));
	public static DeferredItem<Item> SAW_UPGRADE_ARTHROPOD = ITEMS.register("saw_upgrade_arthropod", () -> new ItemSawUpgrade(new Item.Properties().stacksTo(64), ItemSawUpgrade.SawUpgradeType.ARTHROPOD));
	public static DeferredItem<Item> SAW_UPGRADE_BEHEADING = ITEMS.register("saw_upgrade_beheading", () -> new ItemSawUpgrade(new Item.Properties().stacksTo(64), ItemSawUpgrade.SawUpgradeType.BEHEADING));
	public static DeferredItem<Item> SAW_UPGRADE_FIRE = ITEMS.register("saw_upgrade_fire", () -> new ItemSawUpgrade(new Item.Properties().stacksTo(64), ItemSawUpgrade.SawUpgradeType.FIRE));
	public static DeferredItem<Item> SAW_UPGRADE_LOOTING = ITEMS.register("saw_upgrade_looting", () -> new ItemSawUpgrade(new Item.Properties().stacksTo(64), ItemSawUpgrade.SawUpgradeType.LOOTING	));
	public static DeferredItem<Item> SAW_UPGRADE_SHARPNESS = ITEMS.register("saw_upgrade_sharpness", () -> new ItemSawUpgrade(new Item.Properties().stacksTo(64), ItemSawUpgrade.SawUpgradeType.SHARPNESS));
	public static DeferredItem<Item> SAW_UPGRADE_SMITE = ITEMS.register("saw_upgrade_smite", () -> new ItemSawUpgrade(new Item.Properties().stacksTo(64), ItemSawUpgrade.SawUpgradeType.SMITE));
	public static DeferredItem<Item> MOB_SWAB = ITEMS.register("mob_swab", () -> new ItemMobSwab(new Item.Properties().stacksTo(1), false));
	public static DeferredItem<Item> MOB_SWAB_USED = ITEMS.register("mob_swab_used", () -> new ItemMobSwab(new Item.Properties().stacksTo(1), true));
	public static DeferredItem<Item> GM_CHICKEN_FEED = ITEMS.register("gm_chicken_feed", () -> new ItemGMChickenFeed(new Item.Properties().stacksTo(1), ItemGMChickenFeed.FeedType.MOB));
	public static DeferredItem<Item> GM_CHICKEN_FEED_CURSED = ITEMS.register("gm_chicken_feed_cursed", () -> new ItemGMChickenFeed(new Item.Properties().stacksTo(1), ItemGMChickenFeed.FeedType.CURSED));
	public static DeferredItem<Item> NUTRITIOUS_CHICKEN_FEED = ITEMS.register("nutritious_chicken_feed", () -> new ItemGMChickenFeed(new Item.Properties().stacksTo(1), ItemGMChickenFeed.FeedType.NUTRITIOUS));
	public static DeferredItem<Item> FLUID_XP_BUCKET = ITEMS.register("fluid_xp_bucket", () -> new BucketItem(() -> ModBlocks.FLUID_XP.get(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
	public static DeferredItem<Item> NULL_SWORD = ITEMS.register("null_sword", () -> new ItemImaginaryInvisibleNotReallyThereSword(new Item.Properties()));
	public static DeferredItem<Item> ROTTEN_EGG = ITEMS.register("rotten_egg", () -> new ItemRottenEgg(new Item.Properties().stacksTo(1)));
	public static DeferredItem<Item> GOLDEN_EGG = ITEMS.register("golden_egg", () -> new ItemGoldenEgg(new Item.Properties().stacksTo(1)));
	public static DeferredItem<Item> SOLID_XP_MOULD_BLANK = ITEMS.register("solid_xp_mould_blank", () -> new ItemSolidXPMould(new Item.Properties().stacksTo(64), ItemSolidXPMould.Mould.BLANK));
	public static DeferredItem<Item> SOLID_XP_MOULD_BABY = ITEMS.register("solid_xp_mould_baby", () -> new ItemSolidXPMould(new Item.Properties().stacksTo(64), ItemSolidXPMould.Mould.BABY));
	public static DeferredItem<Item> SOLID_XP_BABY = ITEMS.register("solid_xp_baby", () -> new ItemSolidXP(new Item.Properties().stacksTo(64).food((new FoodProperties.Builder()).nutrition(0).saturationMod(0F).alwaysEat().build()), 50));
	public static DeferredItem<Item> XP_SOLIDIFIER_UPGRADE = ITEMS.register("xp_solidifier_upgrade", () -> new ItemSolidifierUpgrade(new Item.Properties().stacksTo(64)));
	public static DeferredItem<Item> SPAWNER_UPGRADE_WIDTH = ITEMS.register("spawner_upgrade_width", () -> new ItemSpawnerUpgrade(new Item.Properties().stacksTo(64), ItemSpawnerUpgrade.SpawnerUpgrade.WIDTH));
	public static DeferredItem<Item> SPAWNER_UPGRADE_HEIGHT = ITEMS.register("spawner_upgrade_height", () -> new ItemSpawnerUpgrade(new Item.Properties().stacksTo(64), ItemSpawnerUpgrade.SpawnerUpgrade.HEIGHT));
	public static DeferredItem<Item> MONOCLE = ITEMS.register("monocle", () -> new ItemMonocle(ArmorMaterials.CHAIN, ArmorItem.Type.HELMET, new Item.Properties().stacksTo(1).durability(256).defaultDurability(ArmorMaterials.CHAIN.getDurabilityForType(ArmorItem.Type.HELMET)))); // pointless shit
	//public static RegistryObject<Item> SPAWNEGG = ITEMS.register("witheregg", () -> new SpawnEggItem(EntityType.WITHER, 0x0, 0xffffff, new Item.Properties().group(MobGrindingUtils.TAB)));

	public static final Set<DeferredItem<Item>> TAB_ORDER = ImmutableSet.of(
		FAN_UPGRADE_HEIGHT, FAN_UPGRADE_WIDTH, FAN_UPGRADE_SPEED,
		SAW_UPGRADE_FIRE, SAW_UPGRADE_SMITE, SAW_UPGRADE_ARTHROPOD, SAW_UPGRADE_BEHEADING, SAW_UPGRADE_LOOTING, SAW_UPGRADE_SHARPNESS,
		ABSORPTION_UPGRADE, MOB_SWAB, MOB_SWAB_USED, FLUID_XP_BUCKET, ROTTEN_EGG, GOLDEN_EGG,
		SOLID_XP_MOULD_BLANK, SOLID_XP_MOULD_BABY, SOLID_XP_BABY, XP_SOLIDIFIER_UPGRADE,
		SPAWNER_UPGRADE_HEIGHT, SPAWNER_UPGRADE_WIDTH, MONOCLE, NUTRITIOUS_CHICKEN_FEED, GM_CHICKEN_FEED, GM_CHICKEN_FEED_CURSED
	);
}