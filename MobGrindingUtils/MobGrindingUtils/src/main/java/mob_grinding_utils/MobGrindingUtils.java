package mob_grinding_utils;

import mob_grinding_utils.blocks.BlockSpikes;
import mob_grinding_utils.capability.base.EntityCapabilityHandler;
import mob_grinding_utils.client.render.TileEntityAbsorptionRenderer;
import mob_grinding_utils.client.render.TileEntityFanRenderer;
import mob_grinding_utils.client.render.TileEntitySawRenderer;
import mob_grinding_utils.client.render.TileEntityTankRenderer;
import mob_grinding_utils.datagen.Generator;
import mob_grinding_utils.events.ChickenFuseEvent;
import mob_grinding_utils.events.ChickenInteractionEvent;
import mob_grinding_utils.events.EntityHeadDropEvent;
import mob_grinding_utils.events.FillXPBottleEvent;
import mob_grinding_utils.events.LocalDragonSoundEvent;
import mob_grinding_utils.events.LocalWitherSoundEvent;
import mob_grinding_utils.events.MGUEndermanInhibitEvent;
import mob_grinding_utils.events.MGUZombieReinforcementEvent;
import mob_grinding_utils.inventory.client.GuiAbsorptionHopper;
import mob_grinding_utils.inventory.client.GuiFan;
import mob_grinding_utils.inventory.server.ContainerAbsorptionHopper;
import mob_grinding_utils.network.MGUNetwork;
import mob_grinding_utils.tile.TileEntityAbsorptionHopper;
import mob_grinding_utils.tile.TileEntityFan;
import mob_grinding_utils.tile.TileEntitySaw;
import mob_grinding_utils.tile.TileEntitySinkTank;
import mob_grinding_utils.tile.TileEntityTank;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
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
	public static Fluid FLUID_XP;
	public static SimpleChannel NETWORK_WRAPPER;
	public static DamageSource SPIKE_DAMAGE;

	public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Reference.MOD_ID);

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
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

		modBus.addListener(this::setup);
		modBus.addListener(this::doClientStuff);

		CONTAINERS.register(modBus);

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

		//PROXY.registerRenderers();
		//NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, PROXY);
		NETWORK_WRAPPER = MGUNetwork.getNetworkChannel();
		
		MinecraftForge.EVENT_BUS.register(new BlockSpikes(null));
		MinecraftForge.EVENT_BUS.register(new ChickenInteractionEvent());
		MinecraftForge.EVENT_BUS.register(new ChickenFuseEvent());
		MinecraftForge.EVENT_BUS.register(new LocalWitherSoundEvent());
		MinecraftForge.EVENT_BUS.register(new LocalDragonSoundEvent());
		MinecraftForge.EVENT_BUS.register(new EntityHeadDropEvent());
		MinecraftForge.EVENT_BUS.register(EntityCapabilityHandler.class);
		MinecraftForge.EVENT_BUS.register(new MGUZombieReinforcementEvent());
		MinecraftForge.EVENT_BUS.register(new FillXPBottleEvent());
		MinecraftForge.EVENT_BUS.register(new MGUEndermanInhibitEvent());
/*
		if (event.getSide() == Side.CLIENT) {
			MinecraftForge.EVENT_BUS.register(new RenderChickenSwell());
			MinecraftForge.EVENT_BUS.register(new ParticleTextureStitchEvent());
			MinecraftForge.EVENT_BUS.register(new BossBarHidingEvent());
			MinecraftForge.EVENT_BUS.register(new GlobalWitherSoundEvent());
			MinecraftForge.EVENT_BUS.register(new GlobalDragonSoundEvent());
			MinecraftForge.EVENT_BUS.register(new TileEntitySaw());
		}
		EntityCapabilityHandler.registerEntityCapability(new BossBarPlayerCapability());
		EntityCapabilityHandler.registerCapabilities();

 */ //todo
	}
	
	private void doClientStuff(final FMLClientSetupEvent event) {
	/*	ClientRegistry.bindTileEntityRenderer(ModBlocks.TANK_TILE, TileEntityTankRenderer::new);
		ClientRegistry.bindTileEntityRenderer(ModBlocks.TANK_SINK_TILE, TileEntityTankRenderer::new);
		ClientRegistry.bindTileEntityRenderer(ModBlocks.SAW_TILE, TileEntitySawRenderer::new);
		ClientRegistry.bindTileEntityRenderer(ModBlocks.ABSORPTION_HOPPER_TILE, TileEntityAbsorptionRenderer::new);
	*/
		ClientRegistry.bindTileEntityRenderer(ModBlocks.FAN_TILE, TileEntityFanRenderer::new);

		ScreenManager.registerFactory(ModContainers.ABSORBTION_HOPPER.get(), GuiAbsorptionHopper::new);
		ScreenManager.registerFactory(ModContainers.FAN.get(), GuiFan::new);
	}
}