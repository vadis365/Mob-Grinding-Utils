package mob_grinding_utils;

import com.google.common.collect.ImmutableSet;
import mob_grinding_utils.items.*;
import mob_grinding_utils.util.RL;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.item.equipment.Equippable;
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

	public static final ResourceKey<EquipmentAsset> MONOCLE_EQUIPMENT =
			ResourceKey.create(EquipmentAssets.ROOT_ID, RL.mgu("monocle"));

	public static DeferredItem<Item> FAN_UPGRADE_WIDTH = ITEMS.registerItem("fan_upgrade_width", props -> new ItemFanUpgrade(props, ItemFanUpgrade.UpgradeType.WIDTH), p -> p.stacksTo(64));
	public static DeferredItem<Item> FAN_UPGRADE_HEIGHT = ITEMS.registerItem("fan_upgrade_height", props -> new ItemFanUpgrade(props, ItemFanUpgrade.UpgradeType.HEIGHT), p -> p.stacksTo(64));
	public static DeferredItem<Item> FAN_UPGRADE_SPEED = ITEMS.registerItem("fan_upgrade_speed", props -> new ItemFanUpgrade(props, ItemFanUpgrade.UpgradeType.SPEED), p -> p.stacksTo(64));
	public static DeferredItem<Item> ABSORPTION_UPGRADE = ITEMS.registerItem("absorption_upgrade", ItemAbsorptionUpgrade::new, p -> p.stacksTo(64));
	public static DeferredItem<Item> SAW_UPGRADE_ARTHROPOD = ITEMS.registerItem("saw_upgrade_arthropod", props -> new ItemSawUpgrade(props, ItemSawUpgrade.SawUpgradeType.ARTHROPOD), p -> p.stacksTo(64));
	public static DeferredItem<Item> SAW_UPGRADE_BEHEADING = ITEMS.registerItem("saw_upgrade_beheading", props -> new ItemSawUpgrade(props, ItemSawUpgrade.SawUpgradeType.BEHEADING), p -> p.stacksTo(64));
	public static DeferredItem<Item> SAW_UPGRADE_FIRE = ITEMS.registerItem("saw_upgrade_fire", props -> new ItemSawUpgrade(props, ItemSawUpgrade.SawUpgradeType.FIRE), p -> p.stacksTo(64));
	public static DeferredItem<Item> SAW_UPGRADE_LOOTING = ITEMS.registerItem("saw_upgrade_looting", props -> new ItemSawUpgrade(props, ItemSawUpgrade.SawUpgradeType.LOOTING), p -> p.stacksTo(64));
	public static DeferredItem<Item> SAW_UPGRADE_SHARPNESS = ITEMS.registerItem("saw_upgrade_sharpness", props -> new ItemSawUpgrade(props, ItemSawUpgrade.SawUpgradeType.SHARPNESS), p -> p.stacksTo(64));
	public static DeferredItem<Item> SAW_UPGRADE_SMITE = ITEMS.registerItem("saw_upgrade_smite", props -> new ItemSawUpgrade(props, ItemSawUpgrade.SawUpgradeType.SMITE), p -> p.stacksTo(64));
	public static DeferredItem<Item> MOB_SWAB = ITEMS.registerItem("mob_swab", props -> new ItemMobSwab(props, false), p -> p.stacksTo(1));
	public static DeferredItem<Item> MOB_SWAB_USED = ITEMS.registerItem("mob_swab_used", props -> new ItemMobSwab(props, true), p -> p.stacksTo(1));
	public static DeferredItem<Item> GM_CHICKEN_FEED = ITEMS.registerItem("gm_chicken_feed", props -> new ItemGMChickenFeed(props, ItemGMChickenFeed.FeedType.MOB), p -> p.stacksTo(1));
	public static DeferredItem<Item> GM_CHICKEN_FEED_CURSED = ITEMS.registerItem("gm_chicken_feed_cursed", props -> new ItemGMChickenFeed(props, ItemGMChickenFeed.FeedType.CURSED), p -> p.stacksTo(1));
	public static DeferredItem<Item> NUTRITIOUS_CHICKEN_FEED = ITEMS.registerItem("nutritious_chicken_feed", props -> new ItemGMChickenFeed(props, ItemGMChickenFeed.FeedType.NUTRITIOUS), p -> p.stacksTo(1));
	public static DeferredItem<Item> FLUID_XP_BUCKET = ITEMS.registerItem("fluid_xp_bucket", props -> new BucketItem(ModBlocks.FLUID_XP.get(), props), p -> p.craftRemainder(Items.BUCKET).stacksTo(1));
	public static DeferredItem<Item> NULL_SWORD = ITEMS.registerItem("null_sword", ItemImaginaryInvisibleNotReallyThereSword::new);
	public static DeferredItem<Item> ROTTEN_EGG = ITEMS.registerItem("rotten_egg", ItemRottenEgg::new, p -> p.stacksTo(1));
	public static DeferredItem<Item> GOLDEN_EGG = ITEMS.registerItem("golden_egg", ItemGoldenEgg::new, p -> p.stacksTo(1));
	public static DeferredItem<Item> SOLID_XP_MOULD_BLANK = ITEMS.registerItem("solid_xp_mould_blank", props -> new ItemSolidXPMould(props, ItemSolidXPMould.Mould.BLANK), p -> p.stacksTo(64));
	public static DeferredItem<Item> SOLID_XP_MOULD_BABY = ITEMS.registerItem("solid_xp_mould_baby", props -> new ItemSolidXPMould(props, ItemSolidXPMould.Mould.BABY), p -> p.stacksTo(64));
	public static DeferredItem<Item> SOLID_XP_BABY = ITEMS.registerItem("solid_xp_baby", props -> new ItemSolidXP(props, 50), p -> p.stacksTo(64).food((new FoodProperties.Builder()).nutrition(0).saturationModifier(0F).alwaysEdible().build()));
	public static DeferredItem<Item> XP_SOLIDIFIER_UPGRADE = ITEMS.registerItem("xp_solidifier_upgrade", ItemSolidifierUpgrade::new, p -> p.stacksTo(64));
	public static DeferredItem<Item> SPAWNER_UPGRADE_WIDTH = ITEMS.registerItem("spawner_upgrade_width", props -> new ItemSpawnerUpgrade(props, ItemSpawnerUpgrade.SpawnerUpgrade.WIDTH), p -> p.stacksTo(64));
	public static DeferredItem<Item> SPAWNER_UPGRADE_HEIGHT = ITEMS.registerItem("spawner_upgrade_height", props -> new ItemSpawnerUpgrade(props, ItemSpawnerUpgrade.SpawnerUpgrade.HEIGHT), p -> p.stacksTo(64));
	public static DeferredItem<Item> MONOCLE = ITEMS.registerItem("monocle", ItemMonocle::new, p -> p.stacksTo(1)
			.humanoidArmor(ArmorMaterials.CHAINMAIL, ArmorType.HELMET)
			.repairable(Items.IRON_INGOT)
			.component(DataComponents.EQUIPPABLE, Equippable.builder(EquipmentSlot.HEAD)
					.setEquipSound(SoundEvents.ARMOR_EQUIP_CHAIN)
					.setAsset(MONOCLE_EQUIPMENT)
					.build()));

	public static final Set<DeferredItem<Item>> TAB_ORDER = ImmutableSet.of(
		FAN_UPGRADE_HEIGHT, FAN_UPGRADE_WIDTH, FAN_UPGRADE_SPEED,
		SAW_UPGRADE_FIRE, SAW_UPGRADE_SMITE, SAW_UPGRADE_ARTHROPOD, SAW_UPGRADE_BEHEADING, SAW_UPGRADE_LOOTING, SAW_UPGRADE_SHARPNESS,
		ABSORPTION_UPGRADE, MOB_SWAB, MOB_SWAB_USED, FLUID_XP_BUCKET, ROTTEN_EGG, GOLDEN_EGG,
		SOLID_XP_MOULD_BLANK, SOLID_XP_MOULD_BABY, SOLID_XP_BABY, XP_SOLIDIFIER_UPGRADE,
		SPAWNER_UPGRADE_HEIGHT, SPAWNER_UPGRADE_WIDTH, MONOCLE, NUTRITIOUS_CHICKEN_FEED, GM_CHICKEN_FEED, GM_CHICKEN_FEED_CURSED
	);
}
