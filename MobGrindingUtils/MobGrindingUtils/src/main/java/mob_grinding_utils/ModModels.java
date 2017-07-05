package mob_grinding_utils;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = "mob_grinding_utils", value = Side.CLIENT)
@SideOnly(Side.CLIENT)
public class ModModels {
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		ModelLoader.setCustomModelResourceLocation(ModBlocks.SPIKES_ITEM, 0, new ModelResourceLocation("mob_grinding_utils:spikes", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModBlocks.FAN_ITEM, 0, new ModelResourceLocation("mob_grinding_utils:fan", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModBlocks.ABSORPTION_HOPPER_ITEM, 0, new ModelResourceLocation("mob_grinding_utils:absorption_hopper", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModBlocks.TANK_ITEM, 0, new ModelResourceLocation("mob_grinding_utils:tank", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModBlocks.TANK_SINK_ITEM, 0, new ModelResourceLocation("mob_grinding_utils:tank_sink", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModBlocks.XP_TAP_ITEM, 0, new ModelResourceLocation("mob_grinding_utils:xp_tap", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.MOB_SWAB, 0, new ModelResourceLocation("mob_grinding_utils:mob_swab", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.MOB_SWAB, 1, new ModelResourceLocation("mob_grinding_utils:mob_swab_used", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.GM_CHICKEN_FEED, 0, new ModelResourceLocation("mob_grinding_utils:gm_chicken_feed", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.FAN_UPGRADE, 0, new ModelResourceLocation("mob_grinding_utils:fan_upgrade_width", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.FAN_UPGRADE, 1, new ModelResourceLocation("mob_grinding_utils:fan_upgrade_height", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.FAN_UPGRADE, 2, new ModelResourceLocation("mob_grinding_utils:fan_upgrade_speed", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.ABSORPTION_UPGRADE, 0, new ModelResourceLocation("mob_grinding_utils:absorption_upgrade", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModBlocks.WITHER_MUFFLER_ITEM, 0, new ModelResourceLocation("mob_grinding_utils:wither_muffler", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModBlocks.DRAGON_MUFFLER_ITEM, 0, new ModelResourceLocation("mob_grinding_utils:dragon_muffler", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModBlocks.SAW_ITEM, 0, new ModelResourceLocation("mob_grinding_utils:saw", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.SAW_UPGRADE, 0, new ModelResourceLocation("mob_grinding_utils:saw_upgrade_sharpness", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.SAW_UPGRADE, 1, new ModelResourceLocation("mob_grinding_utils:saw_upgrade_looting", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.SAW_UPGRADE, 2, new ModelResourceLocation("mob_grinding_utils:saw_upgrade_fire", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.SAW_UPGRADE, 3, new ModelResourceLocation("mob_grinding_utils:saw_upgrade_smite", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.SAW_UPGRADE, 4, new ModelResourceLocation("mob_grinding_utils:saw_upgrade_arthropod", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.SAW_UPGRADE, 5, new ModelResourceLocation("mob_grinding_utils:saw_upgrade_beheading", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.NULL_SWORD, 0, new ModelResourceLocation("mob_grinding_utils:null_sword", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModBlocks.DARK_OAK_STONE_ITEM, 0, new ModelResourceLocation("mob_grinding_utils:dark_oak_stone", "inventory"));
	}

}
