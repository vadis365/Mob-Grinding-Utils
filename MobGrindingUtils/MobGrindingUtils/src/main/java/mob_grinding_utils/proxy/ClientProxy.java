package mob_grinding_utils.proxy;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.client.particles.ParticleFluidXP;
import mob_grinding_utils.client.render.TileEntitySawRenderer;
import mob_grinding_utils.client.render.TileEntityTankRenderer;
import mob_grinding_utils.tile.TileEntitySaw;
import mob_grinding_utils.tile.TileEntityTank;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerRenderers() {
		ModelLoader.setCustomModelResourceLocation(MobGrindingUtils.SPIKES_ITEM, 0, new ModelResourceLocation("mob_grinding_utils:spikes", "inventory"));
		ModelLoader.setCustomModelResourceLocation(MobGrindingUtils.FAN_ITEM, 0, new ModelResourceLocation("mob_grinding_utils:fan", "inventory"));
		ModelLoader.setCustomModelResourceLocation(MobGrindingUtils.ABSORPTION_HOPPER_ITEM, 0, new ModelResourceLocation("mob_grinding_utils:absorption_hopper", "inventory"));
		ModelLoader.setCustomModelResourceLocation(MobGrindingUtils.TANK_ITEM, 0, new ModelResourceLocation("mob_grinding_utils:tank", "inventory"));
		ModelLoader.setCustomModelResourceLocation(MobGrindingUtils.XP_TAP_ITEM, 0, new ModelResourceLocation("mob_grinding_utils:xp_tap", "inventory"));
		ModelLoader.setCustomModelResourceLocation(MobGrindingUtils.MOB_SWAB, 0, new ModelResourceLocation("mob_grinding_utils:mob_swab", "inventory"));
		ModelLoader.setCustomModelResourceLocation(MobGrindingUtils.MOB_SWAB, 1, new ModelResourceLocation("mob_grinding_utils:mob_swab_used", "inventory"));
		ModelLoader.setCustomModelResourceLocation(MobGrindingUtils.GM_CHICKEN_FEED, 0, new ModelResourceLocation("mob_grinding_utils:gm_chicken_feed", "inventory"));
		ModelLoader.setCustomModelResourceLocation(MobGrindingUtils.FAN_UPGRADE, 0, new ModelResourceLocation("mob_grinding_utils:fan_upgrade_width", "inventory"));
		ModelLoader.setCustomModelResourceLocation(MobGrindingUtils.FAN_UPGRADE, 1, new ModelResourceLocation("mob_grinding_utils:fan_upgrade_height", "inventory"));
		ModelLoader.setCustomModelResourceLocation(MobGrindingUtils.FAN_UPGRADE, 2, new ModelResourceLocation("mob_grinding_utils:fan_upgrade_speed", "inventory"));
		ModelLoader.setCustomModelResourceLocation(MobGrindingUtils.ABSORPTION_UPGRADE, 0, new ModelResourceLocation("mob_grinding_utils:absorption_upgrade", "inventory"));
		ModelLoader.setCustomModelResourceLocation(MobGrindingUtils.WITHER_MUFFLER_ITEM, 0, new ModelResourceLocation("mob_grinding_utils:wither_muffler", "inventory"));
		ModelLoader.setCustomModelResourceLocation(MobGrindingUtils.DRAGON_MUFFLER_ITEM, 0, new ModelResourceLocation("mob_grinding_utils:dragon_muffler", "inventory"));
		ModelLoader.setCustomModelResourceLocation(MobGrindingUtils.SAW_ITEM, 0, new ModelResourceLocation("mob_grinding_utils:saw", "inventory"));
		ModelLoader.setCustomModelResourceLocation(MobGrindingUtils.SAW_UPGRADE, 0, new ModelResourceLocation("mob_grinding_utils:saw_upgrade_sharpness", "inventory"));
		ModelLoader.setCustomModelResourceLocation(MobGrindingUtils.SAW_UPGRADE, 1, new ModelResourceLocation("mob_grinding_utils:saw_upgrade_looting", "inventory"));
		ModelLoader.setCustomModelResourceLocation(MobGrindingUtils.SAW_UPGRADE, 2, new ModelResourceLocation("mob_grinding_utils:saw_upgrade_fire", "inventory"));
		ModelLoader.setCustomModelResourceLocation(MobGrindingUtils.SAW_UPGRADE, 3, new ModelResourceLocation("mob_grinding_utils:saw_upgrade_smite", "inventory"));
		ModelLoader.setCustomModelResourceLocation(MobGrindingUtils.SAW_UPGRADE, 4, new ModelResourceLocation("mob_grinding_utils:saw_upgrade_arthropod", "inventory"));
		ModelLoader.setCustomModelResourceLocation(MobGrindingUtils.SAW_UPGRADE, 5, new ModelResourceLocation("mob_grinding_utils:saw_upgrade_beheading", "inventory"));
		ModelLoader.setCustomModelResourceLocation(MobGrindingUtils.NULL_SWORD, 0, new ModelResourceLocation("mob_grinding_utils:null_sword", "inventory"));
		ModelLoader.setCustomModelResourceLocation(MobGrindingUtils.DARK_OAK_STONE_ITEM, 0, new ModelResourceLocation("mob_grinding_utils:dark_oak_stone", "inventory"));
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTank.class, new TileEntityTankRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySaw.class, new TileEntitySawRenderer());
	}

	@Override
	public void postInit() {
		ForgeHooksClient.registerTESRItemStack(MobGrindingUtils.SAW_ITEM, 0, TileEntitySaw.class);
	}

	private static void spawnParticle(Particle spray) {
		Minecraft.getMinecraft().effectRenderer.addEffect(spray);
	}

	@Override
	public void spawnGlitterParticles(World worldObj, double x, double y, double z, double velocityX, double velocityY, double velocityZ, int count, int color, float scale) {
		spawnParticle(new ParticleFluidXP(worldObj, x, y, z, velocityX, velocityY, velocityZ, count, color, scale));
	}

}
