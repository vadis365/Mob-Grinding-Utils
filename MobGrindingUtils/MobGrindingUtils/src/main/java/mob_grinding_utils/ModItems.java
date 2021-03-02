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
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

// My Generic Item Registry ;)
public class ModItems {
	private static final List<Item> ITEMS = new LinkedList<Item>();

	public static Item FAN_UPGRADE;
	public static Item ABSORPTION_UPGRADE;
	public static Item SAW_UPGRADE;
	public static Item MOB_SWAB;
	public static Item GM_CHICKEN_FEED;
	public static SwordItem NULL_SWORD;
	
	public static void init() {
		FAN_UPGRADE = new ItemFanUpgrade(new Item.Properties().group(MobGrindingUtils.TAB).maxStackSize(64));
		ABSORPTION_UPGRADE = new ItemAbsorptionUpgrade(new Item.Properties().group(MobGrindingUtils.TAB).maxStackSize(64));
		SAW_UPGRADE = new ItemSawUpgrade(new Item.Properties().group(MobGrindingUtils.TAB).maxStackSize(64));
		MOB_SWAB = new ItemMobSwab(new Item.Properties().group(MobGrindingUtils.TAB).maxStackSize(1));
		GM_CHICKEN_FEED = new ItemGMChickenFeed(new Item.Properties().group(MobGrindingUtils.TAB).maxStackSize(1));
		NULL_SWORD = new ItemImaginaryInvisibleNotReallyThereSword(new Item.Properties().group(MobGrindingUtils.TAB));
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
/*
		@SideOnly(Side.CLIENT)
		@SubscribeEvent
		public static void registerModels(ModelRegistryEvent event) {
			for (Item item : ITEMS)
				if (item instanceof ISubItemsItem) {
					List<String> models = ((ISubItemsItem) item).getModels();
					for (int i = 0; i < models.size(); i++)
						ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(Reference.MOD_ID + ":" + models.get(i), "inventory"));
				} else {
					ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName().toString(), "inventory"));
				}
		}
*/
	}

	public static interface ISubItemsItem {
		List<String> getModels();
	}
}