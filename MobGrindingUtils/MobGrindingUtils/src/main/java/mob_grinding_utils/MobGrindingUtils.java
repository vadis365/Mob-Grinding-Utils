package mob_grinding_utils;

import mob_grinding_utils.blocks.BlockSpikes;
import mob_grinding_utils.client.render.TileEntityAbsorptionRenderer;
import mob_grinding_utils.client.render.TileEntityFanRenderer;
import mob_grinding_utils.client.render.TileEntitySawRenderer;
import mob_grinding_utils.datagen.Generator;
import mob_grinding_utils.events.BossBarHidingEvent;
import mob_grinding_utils.events.ChickenFuseEvent;
import mob_grinding_utils.events.ChickenInteractionEvent;
import mob_grinding_utils.events.EntityHeadDropEvent;
import mob_grinding_utils.events.FillXPBottleEvent;
import mob_grinding_utils.events.GlobalDragonSoundEvent;
import mob_grinding_utils.events.GlobalWitherSoundEvent;
import mob_grinding_utils.events.LocalDragonSoundEvent;
import mob_grinding_utils.events.LocalWitherSoundEvent;
import mob_grinding_utils.events.MGUEndermanInhibitEvent;
import mob_grinding_utils.events.MGUZombieReinforcementEvent;
import mob_grinding_utils.events.RenderChickenSwell;
import mob_grinding_utils.inventory.client.GuiAbsorptionHopper;
import mob_grinding_utils.inventory.client.GuiFan;
import mob_grinding_utils.inventory.client.GuiSaw;
import mob_grinding_utils.network.MGUNetwork;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


@Mod(Reference.MOD_ID)
public class MobGrindingUtils {
	public static SimpleChannel NETWORK_WRAPPER;
	public static DamageSource SPIKE_DAMAGE;

	public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Reference.MOD_ID);
	public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Reference.MOD_ID);

	public static final RegistryObject<BasicParticleType> PARTICLE_FLUIDXP = PARTICLES.register("fluid_xp", () -> new BasicParticleType(false));

	public static final ItemGroup TAB = new ItemGroup(Reference.MOD_ID) {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(ModBlocks.SPIKES_ITEM);
		}
	};
/*
	static { 
		FluidRegistry.enableUniversalBucket();
	} 
*/

	public MobGrindingUtils() {
		ModContainers modContainers = new ModContainers();

		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		CONTAINERS.register(modBus);
		PARTICLES.register(modBus);

		modBus.addListener(this::setup);
		modBus.addListener(this::doClientStuff);

		//Central Data generator, called on runData
		modBus.addListener(Generator::gatherData);
	}

	public void setup(FMLCommonSetupEvent event) {
		/*
		if (!ModList.get().isLoaded("openblocks") && !ModList.get().isLoaded("enderio")) {
			FLUID_XP = new Fluid("xpjuice", new ResourceLocation("mob_grinding_utils:fluids/fluid_xp"), new ResourceLocation("mob_grinding_utils:fluids/fluid_xp")).setLuminosity(10).setDensity(800).setViscosity(1500).setUnlocalizedName("mob_grinding_utils.fluid_xp");
			FluidRegistry.registerFluid(FLUID_XP);
			FluidRegistry.addBucketForFluid(FLUID_XP);
			if (event.getSide() == Side.CLIENT)
				MinecraftForge.EVENT_BUS.register(new BlockAbsorptionHopper());
		}
*/
		SPIKE_DAMAGE = new DamageSource("spikes").setDamageBypassesArmor();

		NETWORK_WRAPPER = MGUNetwork.getNetworkChannel();
		
		MinecraftForge.EVENT_BUS.addListener(BlockSpikes::dropXP);
		MinecraftForge.EVENT_BUS.register(new ChickenInteractionEvent());
		MinecraftForge.EVENT_BUS.register(new ChickenFuseEvent());
		MinecraftForge.EVENT_BUS.register(new LocalWitherSoundEvent());
		MinecraftForge.EVENT_BUS.register(new LocalDragonSoundEvent());
		MinecraftForge.EVENT_BUS.register(new EntityHeadDropEvent());
		MinecraftForge.EVENT_BUS.register(new MGUZombieReinforcementEvent());
		MinecraftForge.EVENT_BUS.register(new FillXPBottleEvent());
		MinecraftForge.EVENT_BUS.register(new MGUEndermanInhibitEvent());
	}
	
	private void doClientStuff(final FMLClientSetupEvent event) {
		MinecraftForge.EVENT_BUS.register(new RenderChickenSwell());
		MinecraftForge.EVENT_BUS.register(new GlobalWitherSoundEvent());
		MinecraftForge.EVENT_BUS.register(new GlobalDragonSoundEvent());
		MinecraftForge.EVENT_BUS.register(new BossBarHidingEvent());

	/*	ClientRegistry.bindTileEntityRenderer(ModBlocks.TANK_TILE, TileEntityTankRenderer::new);
		ClientRegistry.bindTileEntityRenderer(ModBlocks.TANK_SINK_TILE, TileEntityTankRenderer::new);
	*/
		ClientRegistry.bindTileEntityRenderer(ModBlocks.FAN_TILE, TileEntityFanRenderer::new);
		ClientRegistry.bindTileEntityRenderer(ModBlocks.SAW_TILE, TileEntitySawRenderer::new);
		ClientRegistry.bindTileEntityRenderer(ModBlocks.ABSORPTION_HOPPER_TILE, TileEntityAbsorptionRenderer::new);
		
		ScreenManager.registerFactory(ModContainers.ABSORPTION_HOPPER.get(), GuiAbsorptionHopper::new);
		ScreenManager.registerFactory(ModContainers.FAN.get(), GuiFan::new);
		ScreenManager.registerFactory(ModContainers.SAW.get(), GuiSaw::new);
		
		 RenderTypeLookup.setRenderLayer(ModBlocks.TANK, RenderType.getCutout());
		 RenderTypeLookup.setRenderLayer(ModBlocks.TANK_SINK, RenderType.getCutout());
	}
}