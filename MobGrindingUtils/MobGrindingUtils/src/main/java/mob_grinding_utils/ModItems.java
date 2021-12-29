package mob_grinding_utils;

import mob_grinding_utils.items.ItemAbsorptionUpgrade;
import mob_grinding_utils.items.ItemFanUpgrade;
import mob_grinding_utils.items.ItemGMChickenFeed;
import mob_grinding_utils.items.ItemGoldenEgg;
import mob_grinding_utils.items.ItemImaginaryInvisibleNotReallyThereSword;
import mob_grinding_utils.items.ItemMobSwab;
import mob_grinding_utils.items.ItemMonocle;
import mob_grinding_utils.items.ItemRottenEgg;
import mob_grinding_utils.items.ItemSawUpgrade;
import mob_grinding_utils.items.ItemSolidXP;
import mob_grinding_utils.items.ItemSolidXPMould;
import mob_grinding_utils.items.ItemSolidifierUpgrade;
import mob_grinding_utils.items.ItemSpawnerUpgrade;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

// My Generic Item Registry ;)
public class ModItems {
	public static void init(IEventBus bus) {
		ITEMS.register(bus);
	}
	public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MOD_ID);

	public static RegistryObject<Item> FAN_UPGRADE_WIDTH = ITEMS.register("fan_upgrade_width", () -> new ItemFanUpgrade(new Item.Properties().tab(MobGrindingUtils.TAB).stacksTo(64), "width"));
	public static RegistryObject<Item> FAN_UPGRADE_HEIGHT = ITEMS.register("fan_upgrade_height", () -> new ItemFanUpgrade(new Item.Properties().tab(MobGrindingUtils.TAB).stacksTo(64), "height"));
	public static RegistryObject<Item> FAN_UPGRADE_SPEED = ITEMS.register("fan_upgrade_speed", () -> new ItemFanUpgrade(new Item.Properties().tab(MobGrindingUtils.TAB).stacksTo(64), "speed"));
	public static RegistryObject<Item> ABSORPTION_UPGRADE = ITEMS.register("absorption_upgrade", () -> new ItemAbsorptionUpgrade(new Item.Properties().tab(MobGrindingUtils.TAB).stacksTo(64)));
	public static RegistryObject<Item> SAW_UPGRADE_ARTHROPOD = ITEMS.register("saw_upgrade_arthropod", () -> new ItemSawUpgrade(new Item.Properties().tab(MobGrindingUtils.TAB).stacksTo(64), "arthropod"));
	public static RegistryObject<Item> SAW_UPGRADE_BEHEADING = ITEMS.register("saw_upgrade_beheading", () -> new ItemSawUpgrade(new Item.Properties().tab(MobGrindingUtils.TAB).stacksTo(64), "beheading"));
	public static RegistryObject<Item> SAW_UPGRADE_FIRE = ITEMS.register("saw_upgrade_fire", () -> new ItemSawUpgrade(new Item.Properties().tab(MobGrindingUtils.TAB).stacksTo(64), "fire"));
	public static RegistryObject<Item> SAW_UPGRADE_LOOTING = ITEMS.register("saw_upgrade_looting", () -> new ItemSawUpgrade(new Item.Properties().tab(MobGrindingUtils.TAB).stacksTo(64), "looting"));
	public static RegistryObject<Item> SAW_UPGRADE_SHARPNESS = ITEMS.register("saw_upgrade_sharpness", () -> new ItemSawUpgrade(new Item.Properties().tab(MobGrindingUtils.TAB).stacksTo(64), "sharpness"));
	public static RegistryObject<Item> SAW_UPGRADE_SMITE = ITEMS.register("saw_upgrade_smite", () -> new ItemSawUpgrade(new Item.Properties().tab(MobGrindingUtils.TAB).stacksTo(64), "smite"));
	public static RegistryObject<Item> MOB_SWAB = ITEMS.register("mob_swab", () -> new ItemMobSwab(new Item.Properties().tab(MobGrindingUtils.TAB).stacksTo(1), false));
	public static RegistryObject<Item> MOB_SWAB_USED = ITEMS.register("mob_swab_used", () -> new ItemMobSwab(new Item.Properties().tab(MobGrindingUtils.TAB).stacksTo(1), true));
	public static RegistryObject<Item> GM_CHICKEN_FEED = ITEMS.register("gm_chicken_feed", () -> new ItemGMChickenFeed(new Item.Properties().tab(MobGrindingUtils.TAB).stacksTo(1), "mob"));
	public static RegistryObject<Item> GM_CHICKEN_FEED_CURSED = ITEMS.register("gm_chicken_feed_cursed", () -> new ItemGMChickenFeed(new Item.Properties().tab(MobGrindingUtils.TAB).stacksTo(1), "cursed"));
	public static RegistryObject<Item> NUTRITIOUS_CHICKEN_FEED = ITEMS.register("nutritious_chicken_feed", () -> new ItemGMChickenFeed(new Item.Properties().tab(MobGrindingUtils.TAB).stacksTo(1), "nutritious"));
	public static RegistryObject<Item> FLUID_XP_BUCKET = ITEMS.register("fluid_xp_bucket", () -> new BucketItem(() -> ModBlocks.FLUID_XP.get(), new Item.Properties().craftRemainder(Items.BUCKET).tab(MobGrindingUtils.TAB).stacksTo(1)));
	public static RegistryObject<Item> NULL_SWORD = ITEMS.register("null_sword", () -> new ItemImaginaryInvisibleNotReallyThereSword(new Item.Properties().tab(MobGrindingUtils.TAB)));
	public static RegistryObject<Item> ROTTEN_EGG = ITEMS.register("rotten_egg", () -> new ItemRottenEgg(new Item.Properties().tab(MobGrindingUtils.TAB).stacksTo(1)));
	public static RegistryObject<Item> GOLDEN_EGG = ITEMS.register("golden_egg", () -> new ItemGoldenEgg(new Item.Properties().tab(MobGrindingUtils.TAB).stacksTo(1)));
	public static RegistryObject<Item> SOLID_XP_MOULD_BLANK = ITEMS.register("solid_xp_mould_blank", () -> new ItemSolidXPMould(new Item.Properties().tab(MobGrindingUtils.TAB).stacksTo(64), "blank"));
	public static RegistryObject<Item> SOLID_XP_MOULD_BABY = ITEMS.register("solid_xp_mould_baby", () -> new ItemSolidXPMould(new Item.Properties().tab(MobGrindingUtils.TAB).stacksTo(64), "baby"));
	public static RegistryObject<Item> SOLID_XP_BABY = ITEMS.register("solid_xp_baby", () -> new ItemSolidXP(new Item.Properties().tab(MobGrindingUtils.TAB).stacksTo(64).food((new FoodProperties.Builder()).nutrition(0).saturationMod(0F).alwaysEat().build()), 50));
	public static RegistryObject<Item> XP_SOLIDIFIER_UPGRADE = ITEMS.register("xp_solidifier_upgrade", () -> new ItemSolidifierUpgrade(new Item.Properties().tab(MobGrindingUtils.TAB).stacksTo(64)));
	public static RegistryObject<Item> SPAWNER_UPGRADE_WIDTH = ITEMS.register("spawner_upgrade_width", () -> new ItemSpawnerUpgrade(new Item.Properties().tab(MobGrindingUtils.TAB).stacksTo(64), "width"));
	public static RegistryObject<Item> SPAWNER_UPGRADE_HEIGHT = ITEMS.register("spawner_upgrade_height", () -> new ItemSpawnerUpgrade(new Item.Properties().tab(MobGrindingUtils.TAB).stacksTo(64), "height"));
	public static RegistryObject<Item> MONOCLE = ITEMS.register("monocle", () -> new ItemMonocle(ArmorMaterials.CHAIN, EquipmentSlot.HEAD, new Item.Properties().tab(MobGrindingUtils.TAB).stacksTo(1).durability(256).defaultDurability(ArmorMaterials.CHAIN.getDurabilityForSlot(EquipmentSlot.HEAD)))); // pointless shit
	//public static RegistryObject<Item> SPAWNEGG = ITEMS.register("witheregg", () -> new SpawnEggItem(EntityType.WITHER, 0x0, 0xffffff, new Item.Properties().group(MobGrindingUtils.TAB)));
}