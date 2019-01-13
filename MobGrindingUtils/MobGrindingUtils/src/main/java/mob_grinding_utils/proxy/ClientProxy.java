package mob_grinding_utils.proxy;

import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.client.particles.ParticleFluidXP;
import mob_grinding_utils.client.render.TileEntityAbsorptionRenderer;
import mob_grinding_utils.client.render.TileEntityFanRenderer;
import mob_grinding_utils.client.render.TileEntitySawRenderer;
import mob_grinding_utils.client.render.TileEntityTankRenderer;
import mob_grinding_utils.events.BossBarHidingEvent;
import mob_grinding_utils.events.GlobalDragonSoundEvent;
import mob_grinding_utils.events.GlobalWitherSoundEvent;
import mob_grinding_utils.events.ParticleTextureStitchEvent;
import mob_grinding_utils.events.RenderChickenSwell;
import mob_grinding_utils.tile.TileEntityAbsorptionHopper;
import mob_grinding_utils.tile.TileEntityFan;
import mob_grinding_utils.tile.TileEntitySaw;
import mob_grinding_utils.tile.TileEntitySinkTank;
import mob_grinding_utils.tile.TileEntityTank;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;

@OnlyIn(Dist.CLIENT)
public class ClientProxy extends CommonProxy {

	@Override
	public void registerRenderers() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTank.class, new TileEntityTankRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySinkTank.class, new TileEntityTankRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySaw.class, new TileEntitySawRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAbsorptionHopper.class, new TileEntityAbsorptionRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFan.class, new TileEntityFanRenderer());
	}

	@Override
	public void postInit() {
		ForgeHooksClient.registerTESRItemStack(ModBlocks.SAW_ITEM, 0, TileEntitySaw.class);
	}

	private static void spawnParticle(Particle spray) {
		Minecraft.getMinecraft().effectRenderer.addEffect(spray);
	}

	@Override
	public void spawnGlitterParticles(World worldObj, double x, double y, double z, double velocityX, double velocityY, double velocityZ, int count, int color, float scale) {
		spawnParticle(new ParticleFluidXP(worldObj, x, y, z, velocityX, velocityY, velocityZ, count, color, scale));
	}

	@Override
	public void registerSomeClientEvents() {
		MinecraftForge.EVENT_BUS.register(new RenderChickenSwell());
		MinecraftForge.EVENT_BUS.register(new ParticleTextureStitchEvent());
		MinecraftForge.EVENT_BUS.register(new BossBarHidingEvent());
		MinecraftForge.EVENT_BUS.register(new GlobalWitherSoundEvent());
		MinecraftForge.EVENT_BUS.register(new GlobalDragonSoundEvent());
		MinecraftForge.EVENT_BUS.register(new TileEntitySaw());
	}
}
