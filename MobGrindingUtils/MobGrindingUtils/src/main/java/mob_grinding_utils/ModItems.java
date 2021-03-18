package mob_grinding_utils;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import mob_grinding_utils.items.ItemAbsorptionUpgrade;
import mob_grinding_utils.items.ItemFanUpgrade;
import mob_grinding_utils.items.ItemGMChickenFeed;
import mob_grinding_utils.items.ItemImaginaryInvisibleNotReallyThereSword;
import mob_grinding_utils.items.ItemMobSwab;
import mob_grinding_utils.items.ItemSawUpgrade;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

// My Generic Item Registry ;)
public class ModItems {
	public static final List<Item> ITEMS = new LinkedList<Item>();

	public static Item FAN_UPGRADE_WIDTH, FAN_UPGRADE_HEIGHT, FAN_UPGRADE_SPEED;
	public static Item ABSORPTION_UPGRADE;
	public static Item SAW_UPGRADE_ARTHROPOD, SAW_UPGRADE_BEHEADING, SAW_UPGRADE_FIRE, SAW_UPGRADE_LOOTING, SAW_UPGRADE_SHARPNESS, SAW_UPGRADE_SMITE;
	public static Item MOB_SWAB, MOB_SWAB_USED;
	public static Item GM_CHICKEN_FEED;
	public static Item FLUID_XP_BUCKET;
	public static SwordItem NULL_SWORD;
	
	public static void init() {
		FAN_UPGRADE_WIDTH = new ItemFanUpgrade(new Item.Properties().group(MobGrindingUtils.TAB).maxStackSize(64), "width");
		FAN_UPGRADE_HEIGHT = new ItemFanUpgrade(new Item.Properties().group(MobGrindingUtils.TAB).maxStackSize(64), "height");
		FAN_UPGRADE_SPEED = new ItemFanUpgrade(new Item.Properties().group(MobGrindingUtils.TAB).maxStackSize(64), "speed");
		ABSORPTION_UPGRADE = new ItemAbsorptionUpgrade(new Item.Properties().group(MobGrindingUtils.TAB).maxStackSize(64));
		SAW_UPGRADE_ARTHROPOD = new ItemSawUpgrade(new Item.Properties().group(MobGrindingUtils.TAB).maxStackSize(64), "arthropod");
		SAW_UPGRADE_BEHEADING = new ItemSawUpgrade(new Item.Properties().group(MobGrindingUtils.TAB).maxStackSize(64), "beheading");
		SAW_UPGRADE_FIRE = new ItemSawUpgrade(new Item.Properties().group(MobGrindingUtils.TAB).maxStackSize(64), "fire");
		SAW_UPGRADE_LOOTING = new ItemSawUpgrade(new Item.Properties().group(MobGrindingUtils.TAB).maxStackSize(64), "looting");
		SAW_UPGRADE_SHARPNESS = new ItemSawUpgrade(new Item.Properties().group(MobGrindingUtils.TAB).maxStackSize(64), "sharpness");
		SAW_UPGRADE_SMITE = new ItemSawUpgrade(new Item.Properties().group(MobGrindingUtils.TAB).maxStackSize(64), "smite");
		MOB_SWAB = new ItemMobSwab(new Item.Properties().group(MobGrindingUtils.TAB).maxStackSize(1), false);
		MOB_SWAB_USED = new ItemMobSwab(new Item.Properties().group(MobGrindingUtils.TAB).maxStackSize(1), true);
		GM_CHICKEN_FEED = new ItemGMChickenFeed(new Item.Properties().group(MobGrindingUtils.TAB).maxStackSize(1));
		NULL_SWORD = new ItemImaginaryInvisibleNotReallyThereSword(new Item.Properties().group(MobGrindingUtils.TAB));
		FLUID_XP_BUCKET = new BucketItem(() -> ModBlocks.FLUID_XP, new Item.Properties().containerItem(Items.BUCKET).group(MobGrindingUtils.TAB).maxStackSize(1));
	}

	public static void initReg() {
		try {
			for (Field field : ModItems.class.getDeclaredFields()) {
				Object obj = field.get(null);
				if (obj instanceof Item) {
					Item item = (Item) obj;
					ITEMS.add(item);
					String name = field.getName().toLowerCase(Locale.ENGLISH);
					item.setRegistryName(Reference.MOD_ID, name);
				}
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistrationHandlerItems {

		@SubscribeEvent
		public static void registerItems(final RegistryEvent.Register<Item> event) {
			init();
			initReg();
			final IForgeRegistry<Item> registry = event.getRegistry();
			for (Item item : ITEMS)
				registry.register(item);
		}
	}
}