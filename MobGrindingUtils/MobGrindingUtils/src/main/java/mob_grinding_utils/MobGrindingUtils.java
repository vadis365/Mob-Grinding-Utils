package mob_grinding_utils;

import mob_grinding_utils.blocks.BlockSpikes;
import mob_grinding_utils.client.render.TileEntityAbsorptionRenderer;
import mob_grinding_utils.client.render.TileEntityFanRenderer;
import mob_grinding_utils.client.render.TileEntitySawRenderer;
import mob_grinding_utils.client.render.TileEntityTankRenderer;
import mob_grinding_utils.client.render.TileEntityXPSolidifierRenderer;
import mob_grinding_utils.datagen.Generator;
import mob_grinding_utils.events.BossBarHidingEvent;
import mob_grinding_utils.events.ChickenFuseEvent;
import mob_grinding_utils.events.ChickenInteractionEvent;
import mob_grinding_utils.events.EntityHeadDropEvent;
import mob_grinding_utils.events.FillXPBottleEvent;
import mob_grinding_utils.events.FluidTextureStitchEvent;
import mob_grinding_utils.events.GlobalDragonSoundEvent;
import mob_grinding_utils.events.GlobalWitherSoundEvent;
import mob_grinding_utils.events.LocalDragonSoundEvent;
import mob_grinding_utils.events.LocalWitherSoundEvent;
import mob_grinding_utils.events.MGUEndermanInhibitEvent;
import mob_grinding_utils.events.MGUZombieReinforcementEvent;
import mob_grinding_utils.events.RenderChickenSwell;
import mob_grinding_utils.fakeplayer.MGUFakePlayer;
import mob_grinding_utils.inventory.client.GuiAbsorptionHopper;
import mob_grinding_utils.inventory.client.GuiFan;
import mob_grinding_utils.inventory.client.GuiSaw;
import mob_grinding_utils.inventory.client.GuiXPSolidifier;
import mob_grinding_utils.network.MGUNetProxyClient;
import mob_grinding_utils.network.MGUNetProxyCommon;
import mob_grinding_utils.network.MGUNetwork;
import mob_grinding_utils.network.MessageFlagSync;
import mob_grinding_utils.recipe.RecipeChickenFeed;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


@Mod(Reference.MOD_ID)
public class MobGrindingUtils {
	public static SimpleChannel NETWORK_WRAPPER;
	public static DamageSource SPIKE_DAMAGE;

	public static final ITag.INamedTag<Fluid> EXPERIENCE = FluidTags.makeWrapperTag(new ResourceLocation("forge", "experience").toString());
	public static final ITag.INamedTag<Fluid> XPJUICE = FluidTags.makeWrapperTag(new ResourceLocation("forge", "xpjuice").toString());

	public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Reference.MOD_ID);
	public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Reference.MOD_ID);
	public static final DeferredRegister<IRecipeSerializer<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Reference.MOD_ID);

	public static final RegistryObject<BasicParticleType> PARTICLE_FLUID_XP = PARTICLES.register("fluid_xp_particles", () -> new BasicParticleType(true));

	public static final RegistryObject<IRecipeSerializer<RecipeChickenFeed>> RECIPE_CHICKEN_FEED = RECIPES.register("chicken_feed_recipe", () ->  new SpecialRecipeSerializer<RecipeChickenFeed>(RecipeChickenFeed::new));

	public static final ItemGroup TAB = new ItemGroup(Reference.MOD_ID) {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(ModBlocks.SPIKES_ITEM);
		}
	};
	public static MGUNetProxyCommon NETPROXY;

	public MobGrindingUtils() {
		ModContainers modContainers = new ModContainers();

		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		CONTAINERS.register(modBus);
		PARTICLES.register(modBus);
		RECIPES.register(modBus);

		modBus.addListener(this::setup);
		modBus.addListener(this::doClientStuff);

		//Central Data generator, called on runData
		modBus.addListener(Generator::gatherData);

		NETPROXY = DistExecutor.safeRunForDist(() -> MGUNetProxyClient::new, () -> MGUNetProxyCommon::new);
	}

	public void setup(FMLCommonSetupEvent event) {
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
		MinecraftForge.EVENT_BUS.addListener(this::playerConnected);
	}
	
	private void doClientStuff(final FMLClientSetupEvent event) {
		MinecraftForge.EVENT_BUS.register(new FluidTextureStitchEvent());
		MinecraftForge.EVENT_BUS.register(new RenderChickenSwell());
		MinecraftForge.EVENT_BUS.register(new GlobalWitherSoundEvent());
		MinecraftForge.EVENT_BUS.register(new GlobalDragonSoundEvent());
		MinecraftForge.EVENT_BUS.register(new BossBarHidingEvent());
		MinecraftForge.EVENT_BUS.addListener(this::worldUnload);

		ClientRegistry.bindTileEntityRenderer(ModBlocks.FAN_TILE, TileEntityFanRenderer::new);
		ClientRegistry.bindTileEntityRenderer(ModBlocks.SAW_TILE, TileEntitySawRenderer::new);
		ClientRegistry.bindTileEntityRenderer(ModBlocks.ABSORPTION_HOPPER_TILE, TileEntityAbsorptionRenderer::new);
		ClientRegistry.bindTileEntityRenderer(ModBlocks.TANK_TILE, TileEntityTankRenderer::new);
		ClientRegistry.bindTileEntityRenderer(ModBlocks.TANK_SINK_TILE, TileEntityTankRenderer::new);
		ClientRegistry.bindTileEntityRenderer(ModBlocks.JUMBO_TANK_TILE, TileEntityTankRenderer::new);
		ClientRegistry.bindTileEntityRenderer(ModBlocks.XPSOLIDIFIER_TILE, TileEntityXPSolidifierRenderer::new);
		
		ScreenManager.registerFactory(ModContainers.ABSORPTION_HOPPER.get(), GuiAbsorptionHopper::new);
		ScreenManager.registerFactory(ModContainers.SOLIDIFIER.get(), GuiXPSolidifier::new);
		ScreenManager.registerFactory(ModContainers.FAN.get(), GuiFan::new);
		ScreenManager.registerFactory(ModContainers.SAW.get(), GuiSaw::new);
		
		 RenderTypeLookup.setRenderLayer(ModBlocks.TANK, RenderType.getCutout());
		 RenderTypeLookup.setRenderLayer(ModBlocks.TANK_SINK, RenderType.getCutout());
		 RenderTypeLookup.setRenderLayer(ModBlocks.XP_TAP, RenderType.getCutout());
		 RenderTypeLookup.setRenderLayer(ModBlocks.ENDER_INHIBITOR_ON, RenderType.getCutout());
		 RenderTypeLookup.setRenderLayer(ModBlocks.ENDER_INHIBITOR_OFF, RenderType.getCutout());
		 RenderTypeLookup.setRenderLayer(ModBlocks.SAW, RenderType.getCutout());
		 RenderTypeLookup.setRenderLayer(ModBlocks.SPIKES, RenderType.getCutout());
		 RenderTypeLookup.setRenderLayer(ModBlocks.ABSORPTION_HOPPER, RenderType.getCutout());
		 RenderTypeLookup.setRenderLayer(ModBlocks.TINTED_GLASS, RenderType.getTranslucent());
		 RenderTypeLookup.setRenderLayer(ModBlocks.JUMBO_TANK, RenderType.getCutout());
		 RenderTypeLookup.setRenderLayer(ModBlocks.DREADFUL_DIRT, RenderType.getCutout());
		 RenderTypeLookup.setRenderLayer(ModBlocks.DELIGHTFUL_DIRT, RenderType.getCutout());
		 RenderTypeLookup.setRenderLayer(ModBlocks.XPSOLIDIFIER, RenderType.getCutout());
		 RenderTypeLookup.setRenderLayer(ModBlocks.SOLID_XP_BLOCK, RenderType.getTranslucent());

		 ModColourManager.registerColourHandlers();
	}

	private void playerConnected(final PlayerEvent.PlayerLoggedInEvent event) {
		if (event.getPlayer() instanceof ServerPlayerEntity) {
			ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
			CompoundNBT nbt = player.getPersistentData();
			if (nbt.contains("MGU_WitherMuffle") || nbt.contains("MGU_DragonMuffle")) {
				NETWORK_WRAPPER.send(PacketDistributor.PLAYER.with(() -> player), new MessageFlagSync(nbt.getBoolean("MGU_WitherMuffle"), nbt.getBoolean("MGU_DragonMuffle")));
			}
		}
	}

	private void worldUnload(final WorldEvent.Unload event) {
		if (event.getWorld() instanceof ServerWorld) {
			MGUFakePlayer.unload(event.getWorld());
		}
	}
}