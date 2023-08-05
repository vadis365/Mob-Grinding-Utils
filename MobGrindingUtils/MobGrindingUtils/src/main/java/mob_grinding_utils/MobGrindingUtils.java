package mob_grinding_utils;

import mob_grinding_utils.blocks.BlockSpikes;
import mob_grinding_utils.client.ModelLayers;
import mob_grinding_utils.datagen.Generator;
import mob_grinding_utils.events.*;
import mob_grinding_utils.fakeplayer.MGUFakePlayer;
import mob_grinding_utils.inventory.client.*;
import mob_grinding_utils.network.MGUNetwork;
import mob_grinding_utils.network.MessageFlagSync;
import mob_grinding_utils.recipe.BeheadingRecipe;
import mob_grinding_utils.recipe.ChickenFeedRecipe;
import mob_grinding_utils.recipe.FluidIngredient;
import mob_grinding_utils.recipe.SolidifyRecipe;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


@Mod(Reference.MOD_ID)
public class MobGrindingUtils {
	public static final Logger LOGGER = LoggerFactory.getLogger(Reference.MOD_ID);
	public static SimpleChannel NETWORK_WRAPPER;

	private static DamageSource SPIKE_DAMAGE;
	public static final ResourceKey<DamageType> SPIKE_TYPE = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(Reference.MOD_ID, "spikes"));

	public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Reference.MOD_ID);
	public static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Reference.MOD_ID);
	public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, Reference.MOD_ID);
	public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Reference.MOD_ID);

	public static final RegistryObject<CreativeModeTab> TAB = TABS.register("tab", () -> CreativeModeTab.builder()
				.icon(() -> new ItemStack(ModBlocks.SPIKES.get()))
				.title(Component.translatable("itemGroup.mob_grinding_utils")).displayItems((params, output) -> {
					ModBlocks.TAB_ORDER.forEach(block -> output.accept(block.getItem()));
					ModItems.TAB_ORDER.forEach(item -> output.accept(item.get()));
				}
			).build());

	public static final RegistryObject<SimpleParticleType> PARTICLE_FLUID_XP = PARTICLES.register("fluid_xp_particles", () -> new SimpleParticleType(true));

	public static final RegistryObject<RecipeSerializer<?>> CHICKEN_FEED = RECIPES.register(ChickenFeedRecipe.NAME, ChickenFeedRecipe.Serializer::new);

	public static final List<SolidifyRecipe> SOLIDIFIER_RECIPES = new ArrayList<>();
	public static final List<BeheadingRecipe> BEHEADING_RECIPES = new ArrayList<>();
	public static final RegistryObject<RecipeSerializer<?>> SOLIDIFIER_RECIPE = RECIPES.register(SolidifyRecipe.NAME, SolidifyRecipe.Serializer::new);
	public static final RegistryObject<RecipeSerializer<?>> BEHEADING_RECIPE = RECIPES.register(BeheadingRecipe.NAME, BeheadingRecipe.Serializer::new);
	public static final RegistryObject<RecipeType<SolidifyRecipe>> SOLIDIFIER_TYPE = RECIPE_TYPES.register("solidify", () -> new RecipeType<>() {});
	public static final RegistryObject<RecipeType<BeheadingRecipe>> BEHEADING_TYPE = RECIPE_TYPES.register("beheading", () -> new RecipeType<>() {});

	public MobGrindingUtils() {
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		ModBlocks.init(modBus);
		ModItems.init(modBus);
		ModContainers.init(modBus);
		PARTICLES.register(modBus);
		RECIPES.register(modBus);
		RECIPE_TYPES.register(modBus);
		TABS.register(modBus);
		ModSounds.init(modBus);

		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ModelLayers.init(modBus));

		modBus.addListener(this::setup);
		modBus.addListener(this::doClientStuff);

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

		//Central Data generator, called on runData
		modBus.addListener(Generator::gatherData);
	}

	public void setup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> CraftingHelper.register(FluidIngredient.Serializer.NAME, FluidIngredient.SERIALIZER));

		NETWORK_WRAPPER = MGUNetwork.getNetworkChannel();
	}

	private void doClientStuff(final FMLClientSetupEvent event) {
		MinecraftForge.EVENT_BUS.register(new FluidTextureStitchEvent());
		MinecraftForge.EVENT_BUS.register(new RenderChickenSwell());
		MinecraftForge.EVENT_BUS.register(new GlobalWitherSoundEvent());
		MinecraftForge.EVENT_BUS.register(new GlobalDragonSoundEvent());
		MinecraftForge.EVENT_BUS.register(new BossBarHidingEvent());
		MinecraftForge.EVENT_BUS.addListener(this::worldUnload);

		MenuScreens.register(ModContainers.ABSORPTION_HOPPER.get(), GuiAbsorptionHopper::new);
		MenuScreens.register(ModContainers.SOLIDIFIER.get(), GuiXPSolidifier::new);
		MenuScreens.register(ModContainers.FAN.get(), GuiFan::new);
		MenuScreens.register(ModContainers.SAW.get(), GuiSaw::new);
		MenuScreens.register(ModContainers.ENTITY_SPAWNER.get(), GuiMGUSpawner::new);

		ItemBlockRenderTypes.setRenderLayer(ModBlocks.FLUID_XP_FLOWING.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.FLUID_XP.get(), RenderType.translucent());

		event.enqueueWork(ModColourManager::registerColourHandlers);
	}

	private void serverReloadListener(final AddReloadListenerEvent event) {
		event.addListener(new ServerResourceReloader(event.getServerResources()));
	}
	private void clientRecipeReload(final RecipesUpdatedEvent event) {
		SOLIDIFIER_RECIPES.clear();
		SOLIDIFIER_RECIPES.addAll(event.getRecipeManager().getAllRecipesFor(SOLIDIFIER_TYPE.get()));
	}

	private void playerConnected(final PlayerEvent.PlayerLoggedInEvent event) {
		if (event.getEntity() instanceof ServerPlayer) {
			sendPersistentData((ServerPlayer) event.getEntity());
		}
	}

	private void changedDimension(final PlayerEvent.PlayerChangedDimensionEvent event) {
		if (event.getEntity() instanceof ServerPlayer) {
			sendPersistentData((ServerPlayer) event.getEntity());
		}
	}
	private void playerRespawn(final PlayerEvent.PlayerRespawnEvent event) {
		if (event.getEntity() instanceof ServerPlayer) {
			sendPersistentData((ServerPlayer) event.getEntity());
		}
	}

	private void sendPersistentData(ServerPlayer playerEntity) {
		CompoundTag nbt = playerEntity.getPersistentData();
		if (nbt.contains("MGU_WitherMuffle") || nbt.contains("MGU_DragonMuffle")) {
			NETWORK_WRAPPER.send(PacketDistributor.PLAYER.with(() -> playerEntity), new MessageFlagSync(nbt.getBoolean("MGU_WitherMuffle"), nbt.getBoolean("MGU_DragonMuffle")));
		}
	}

	private void cloneEvent(PlayerEvent.Clone event) {
		CompoundTag nbt = event.getOriginal().getPersistentData();
		if (nbt.contains("MGU_WitherMuffle") || nbt.contains("MGU_DragonMuffle")) {
			CompoundTag newNBT = event.getEntity().getPersistentData();
			newNBT.putBoolean("MGU_WitherMuffle", nbt.getBoolean("MGU_WitherMuffle"));
			newNBT.putBoolean("MGU_DragonMuffle", nbt.getBoolean("MGU_DragonMuffle"));
		}
	}

	private void worldUnload(final LevelEvent.Unload event) {
		if (event.getLevel() instanceof ServerLevel) {
			MGUFakePlayer.unload(event.getLevel());
		}
	}

	public static DamageSource getSpikeDamage(Level level) {
		if (SPIKE_DAMAGE == null)
			SPIKE_DAMAGE = new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(MobGrindingUtils.SPIKE_TYPE));

		return SPIKE_DAMAGE;
	}
}