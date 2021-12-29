package mob_grinding_utils;

import javax.annotation.Nonnull;

import mob_grinding_utils.blocks.BlockSpikes;
import mob_grinding_utils.client.render.TileEntityAbsorptionRenderer;
import mob_grinding_utils.client.render.TileEntityFanRenderer;
import mob_grinding_utils.client.render.TileEntityMGUSpawnerRenderer;
import mob_grinding_utils.client.render.TileEntitySawRenderer;
import mob_grinding_utils.client.render.TileEntityTankRenderer;
import mob_grinding_utils.client.render.TileEntityXPSolidifierRenderer;
import mob_grinding_utils.datagen.Generator;
import mob_grinding_utils.events.BossBarHidingEvent;
import mob_grinding_utils.events.ChickenFuseEvent;
import mob_grinding_utils.events.EntityHeadDropEvent;
import mob_grinding_utils.events.EntityInteractionEvent;
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
import mob_grinding_utils.inventory.client.GuiMGUSpawner;
import mob_grinding_utils.inventory.client.GuiSaw;
import mob_grinding_utils.inventory.client.GuiXPSolidifier;
import mob_grinding_utils.network.MGUNetProxyClient;
import mob_grinding_utils.network.MGUNetProxyCommon;
import mob_grinding_utils.network.MGUNetwork;
import mob_grinding_utils.network.MessageFlagSync;
import mob_grinding_utils.recipe.ChickenFeedRecipe;
import mob_grinding_utils.recipe.FluidIngredient;
import mob_grinding_utils.recipe.SolidifyRecipe;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.AddReloadListenerEvent;
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

import java.util.ArrayList;
import java.util.List;


@Mod(Reference.MOD_ID)
public class MobGrindingUtils {
	public static SimpleChannel NETWORK_WRAPPER;
	public static DamageSource SPIKE_DAMAGE;

	//Tags
	public static final ITag.INamedTag<Fluid> EXPERIENCE = FluidTags.makeWrapperTag(new ResourceLocation("forge", "experience").toString());
	public static final ITag.INamedTag<Fluid> XPJUICE = FluidTags.makeWrapperTag(new ResourceLocation("forge", "xpjuice").toString());
	public static final ITag.INamedTag<EntityType<?>> NOSWAB = EntityTypeTags.createOptional(new ResourceLocation(Reference.MOD_NAME, "noswab"));

	public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Reference.MOD_ID);
	public static final DeferredRegister<IRecipeSerializer<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Reference.MOD_ID);

	public static final RegistryObject<BasicParticleType> PARTICLE_FLUID_XP = PARTICLES.register("fluid_xp_particles", () -> new BasicParticleType(true));

	public static final RegistryObject<IRecipeSerializer<?>> CHICKEN_FEED = RECIPES.register(ChickenFeedRecipe.NAME, ChickenFeedRecipe.Serializer::new);

	public static final List<SolidifyRecipe> SOLIDIFIER_RECIPES = new ArrayList<>();
	public static final RegistryObject<IRecipeSerializer<?>> SOLIDIFIER_RECIPE = RECIPES.register(SolidifyRecipe.NAME, SolidifyRecipe.Serializer::new);
	public static final IRecipeType<SolidifyRecipe> SOLIDIFIER_TYPE = IRecipeType.register(Reference.MOD_ID + ":solidify");

	public static final ItemGroup TAB = new ItemGroup(Reference.MOD_ID) {
		@Nonnull
		@Override
		public ItemStack createIcon() {
			return new ItemStack(ModBlocks.SPIKES.getItem());
		}
	};
	public static MGUNetProxyCommon NETPROXY;

	public MobGrindingUtils() {
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		ModBlocks.init(modBus);
		ModItems.init(modBus);
		ModContainers.init(modBus);
		PARTICLES.register(modBus);
		RECIPES.register(modBus);

		modBus.addListener(this::setup);
		modBus.addListener(this::doClientStuff);

		//Central Data generator, called on runData
		modBus.addListener(Generator::gatherData);

		NETPROXY = DistExecutor.safeRunForDist(() -> MGUNetProxyClient::new, () -> MGUNetProxyCommon::new);
	}

	public void setup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> CraftingHelper.register(FluidIngredient.Serializer.NAME, FluidIngredient.SERIALIZER));

		SPIKE_DAMAGE = new DamageSource("spikes").setDamageBypassesArmor();

		NETWORK_WRAPPER = MGUNetwork.getNetworkChannel();

		MinecraftForge.EVENT_BUS.addListener(BlockSpikes::dropXP);
		MinecraftForge.EVENT_BUS.register(new EntityInteractionEvent());
		MinecraftForge.EVENT_BUS.register(new ChickenFuseEvent());
		MinecraftForge.EVENT_BUS.register(new LocalWitherSoundEvent());
		MinecraftForge.EVENT_BUS.register(new LocalDragonSoundEvent());
		MinecraftForge.EVENT_BUS.register(new EntityHeadDropEvent());
		MinecraftForge.EVENT_BUS.register(new MGUZombieReinforcementEvent());
		MinecraftForge.EVENT_BUS.register(new FillXPBottleEvent());
		MinecraftForge.EVENT_BUS.register(new MGUEndermanInhibitEvent());
		MinecraftForge.EVENT_BUS.addListener(this::playerConnected);
		MinecraftForge.EVENT_BUS.addListener(this::changedDimension);
		MinecraftForge.EVENT_BUS.addListener(this::playerRespawn);
		MinecraftForge.EVENT_BUS.addListener(this::cloneEvent);
		MinecraftForge.EVENT_BUS.addListener(this::serverReloadListener);
		MinecraftForge.EVENT_BUS.addListener(this::clientRecipeReload);
	}

	private void doClientStuff(final FMLClientSetupEvent event) {
		MinecraftForge.EVENT_BUS.register(new FluidTextureStitchEvent());
		MinecraftForge.EVENT_BUS.register(new RenderChickenSwell());
		MinecraftForge.EVENT_BUS.register(new GlobalWitherSoundEvent());
		MinecraftForge.EVENT_BUS.register(new GlobalDragonSoundEvent());
		MinecraftForge.EVENT_BUS.register(new BossBarHidingEvent());
		MinecraftForge.EVENT_BUS.addListener(this::worldUnload);

		ClientRegistry.bindTileEntityRenderer(ModBlocks.FAN.getTileEntityType(), TileEntityFanRenderer::new);
		ClientRegistry.bindTileEntityRenderer(ModBlocks.SAW.getTileEntityType(), TileEntitySawRenderer::new);
		ClientRegistry.bindTileEntityRenderer(ModBlocks.ABSORPTION_HOPPER.getTileEntityType(), TileEntityAbsorptionRenderer::new);
		ClientRegistry.bindTileEntityRenderer(ModBlocks.TANK.getTileEntityType(), TileEntityTankRenderer::new);
		ClientRegistry.bindTileEntityRenderer(ModBlocks.TANK_SINK.getTileEntityType(), TileEntityTankRenderer::new);
		ClientRegistry.bindTileEntityRenderer(ModBlocks.JUMBO_TANK.getTileEntityType(), TileEntityTankRenderer::new);
		ClientRegistry.bindTileEntityRenderer(ModBlocks.XPSOLIDIFIER.getTileEntityType(), TileEntityXPSolidifierRenderer::new);
		ClientRegistry.bindTileEntityRenderer(ModBlocks.ENTITY_SPAWNER.getTileEntityType(), TileEntityMGUSpawnerRenderer::new);

		ScreenManager.registerFactory(ModContainers.ABSORPTION_HOPPER.get(), GuiAbsorptionHopper::new);
		ScreenManager.registerFactory(ModContainers.SOLIDIFIER.get(), GuiXPSolidifier::new);
		ScreenManager.registerFactory(ModContainers.FAN.get(), GuiFan::new);
		ScreenManager.registerFactory(ModContainers.SAW.get(), GuiSaw::new);
		ScreenManager.registerFactory(ModContainers.ENTITY_SPAWNER.get(), GuiMGUSpawner::new);

		RenderTypeLookup.setRenderLayer(ModBlocks.TANK.getBlock(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.TANK_SINK.getBlock(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.XP_TAP.getBlock(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.ENDER_INHIBITOR_ON.getBlock(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.ENDER_INHIBITOR_OFF.getBlock(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.SAW.getBlock(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.SPIKES.getBlock(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.ABSORPTION_HOPPER.getBlock(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.TINTED_GLASS.getBlock(), RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(ModBlocks.JUMBO_TANK.getBlock(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.DREADFUL_DIRT.getBlock(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.DELIGHTFUL_DIRT.getBlock(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.XPSOLIDIFIER.getBlock(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.SOLID_XP_BLOCK.getBlock(), RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(ModBlocks.FLUID_XP_FLOWING.get(), RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(ModBlocks.FLUID_XP.get(), RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(ModBlocks.ENTITY_SPAWNER.getBlock(), RenderType.getCutout());

		ModColourManager.registerColourHandlers();
	}

	private void serverReloadListener(final AddReloadListenerEvent event) {
		event.addListener(new ServerResourceReloader(event.getDataPackRegistries()));
	}
	private void clientRecipeReload(final RecipesUpdatedEvent event) {
		SOLIDIFIER_RECIPES.clear();
		SOLIDIFIER_RECIPES.addAll(event.getRecipeManager().getRecipesForType(SOLIDIFIER_TYPE));
	}

	private void playerConnected(final PlayerEvent.PlayerLoggedInEvent event) {
		if (event.getPlayer() instanceof ServerPlayerEntity) {
			sendPersistentData((ServerPlayerEntity) event.getPlayer());
		}
	}

	private void changedDimension(final PlayerEvent.PlayerChangedDimensionEvent event) {
		if (event.getPlayer() instanceof ServerPlayerEntity) {
			sendPersistentData((ServerPlayerEntity) event.getPlayer());
		}
	}
	private void playerRespawn(final PlayerEvent.PlayerRespawnEvent event) {
		if (event.getPlayer() instanceof ServerPlayerEntity) {
			sendPersistentData((ServerPlayerEntity) event.getPlayer());
		}
	}

	private void sendPersistentData(ServerPlayerEntity playerEntity) {
		CompoundNBT nbt = playerEntity.getPersistentData();
		if (nbt.contains("MGU_WitherMuffle") || nbt.contains("MGU_DragonMuffle")) {
			NETWORK_WRAPPER.send(PacketDistributor.PLAYER.with(() -> playerEntity), new MessageFlagSync(nbt.getBoolean("MGU_WitherMuffle"), nbt.getBoolean("MGU_DragonMuffle")));
		}
	}

	private void cloneEvent(PlayerEvent.Clone event) {
		CompoundNBT nbt = event.getOriginal().getPersistentData();
		if (nbt.contains("MGU_WitherMuffle") || nbt.contains("MGU_DragonMuffle")) {
			CompoundNBT newNBT = event.getPlayer().getPersistentData();
			newNBT.putBoolean("MGU_WitherMuffle", nbt.getBoolean("MGU_WitherMuffle"));
			newNBT.putBoolean("MGU_DragonMuffle", nbt.getBoolean("MGU_DragonMuffle"));
		}
	}

	private void worldUnload(final WorldEvent.Unload event) {
		if (event.getWorld() instanceof ServerWorld) {
			MGUFakePlayer.unload(event.getWorld());
		}
	}
}