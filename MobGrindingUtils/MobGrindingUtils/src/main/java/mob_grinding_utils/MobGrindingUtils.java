package mob_grinding_utils;

import mob_grinding_utils.blocks.BlockSpikes;
import mob_grinding_utils.client.ModelLayers;
import mob_grinding_utils.client.render.TileSawStackItemRenderer;
import mob_grinding_utils.client.render.TileTankStackItemRenderer;
import mob_grinding_utils.client.render.TileXPSolidifierStackItemRenderer;
import mob_grinding_utils.components.MGUComponents;
import mob_grinding_utils.config.ServerConfig;
import mob_grinding_utils.datagen.Generator;
import mob_grinding_utils.events.*;
import mob_grinding_utils.inventory.client.*;
import mob_grinding_utils.network.FlagSyncPacket;
import mob_grinding_utils.network.MGUNetwork;
import mob_grinding_utils.recipe.BeheadingRecipe;
import mob_grinding_utils.recipe.ChickenFeedRecipe;
import mob_grinding_utils.recipe.FluidIngredient;
import mob_grinding_utils.recipe.SolidifyRecipe;
import mob_grinding_utils.tile.TileEntityAbsorptionHopper;
import mob_grinding_utils.tile.TileEntityMGUSpawner;
import mob_grinding_utils.tile.TileEntityTank;
import mob_grinding_utils.tile.TileEntityXPSolidifier;
import mob_grinding_utils.util.FakePlayerHandler;
import mob_grinding_utils.util.RL;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
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
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.RecipesUpdatedEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;


@Mod(Reference.MOD_ID)
public class MobGrindingUtils {
	public static final Logger LOGGER = LoggerFactory.getLogger(Reference.MOD_ID);

	private static DamageSource SPIKE_DAMAGE;
	public static final ResourceKey<DamageType> SPIKE_TYPE = ResourceKey.create(Registries.DAMAGE_TYPE, RL.mgu("spikes"));

	public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, Reference.MOD_ID);
	public static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, Reference.MOD_ID);
	public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, Reference.MOD_ID);
	public static final DeferredRegister<IngredientType<?>> INGREDIENTS = DeferredRegister.create(NeoForgeRegistries.Keys.INGREDIENT_TYPES, Reference.MOD_ID);
	public static DeferredHolder<IngredientType<?>, IngredientType<FluidIngredient>> FLUID_INGREDIENT = INGREDIENTS.register("fluid", () -> new IngredientType<>(FluidIngredient.CODEC));

	public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Reference.MOD_ID);

	public static final Supplier<CreativeModeTab> TAB = TABS.register("tab", () -> CreativeModeTab.builder()
				.icon(() -> new ItemStack(ModBlocks.SPIKES.get()))
				.title(Component.translatable("itemGroup.mob_grinding_utils")).displayItems((params, output) -> {
					ModBlocks.TAB_ORDER.forEach(block -> output.accept(block.getItem()));
					ModItems.TAB_ORDER.forEach(item -> output.accept(item.get()));
				}
			).build());

	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> PARTICLE_FLUID_XP = PARTICLES.register("fluid_xp_particles", () -> new SimpleParticleType(true));

	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> CHICKEN_FEED = RECIPES.register(ChickenFeedRecipe.NAME, ChickenFeedRecipe.Serializer::new);

	public static final List<RecipeHolder<SolidifyRecipe>> SOLIDIFIER_RECIPES = new ArrayList<>();
	public static final List<RecipeHolder<BeheadingRecipe>> BEHEADING_RECIPES = new ArrayList<>();
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> SOLIDIFIER_RECIPE = RECIPES.register(SolidifyRecipe.NAME, SolidifyRecipe.Serializer::new);
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> BEHEADING_RECIPE = RECIPES.register(BeheadingRecipe.NAME, BeheadingRecipe.Serializer::new);
	public static final DeferredHolder<RecipeType<?>, RecipeType<SolidifyRecipe>> SOLIDIFIER_TYPE = RECIPE_TYPES.register("solidify", RecipeType::simple);
	public static final DeferredHolder<RecipeType<?>, RecipeType<BeheadingRecipe>> BEHEADING_TYPE = RECIPE_TYPES.register("beheading", RecipeType::simple);

	public MobGrindingUtils(IEventBus modBus, ModContainer container, Dist dist) {
		IEventBus neoBus = NeoForge.EVENT_BUS;
		ModBlocks.init(modBus);
		ModItems.init(modBus);
		ModContainers.init(modBus);
		PARTICLES.register(modBus);
		RECIPES.register(modBus);
		RECIPE_TYPES.register(modBus);
		TABS.register(modBus);
		INGREDIENTS.register(modBus);
		MGUComponents.init(modBus);
		ModSounds.init(modBus);

		if (dist.isClient()) {
			ModelLayers.init(modBus);
			modBus.addListener(this::doClientStuff);
			modBus.addListener(this::menuScreenEvent);

			modBus.addListener(this::onClientExtensions);
		}

		modBus.addListener(this::setup);


		container.registerConfig(ModConfig.Type.SERVER, ServerConfig.SERVER_CONFIG);

		neoBus.addListener(BlockSpikes::dropXP);
		neoBus.register(new EntityInteractionEvent());
		neoBus.register(new ChickenFuseEvent());
		neoBus.register(new LocalWitherSoundEvent());
		neoBus.register(new LocalDragonSoundEvent());
		neoBus.register(new EntityHeadDropEvent());
		neoBus.register(new MGUZombieReinforcementEvent());
		neoBus.register(new FillXPBottleEvent());
		neoBus.register(new MGUEndermanInhibitEvent());
		neoBus.addListener(this::playerConnected);
		neoBus.addListener(this::changedDimension);
		neoBus.addListener(this::playerRespawn);
		neoBus.addListener(this::cloneEvent);
		neoBus.addListener(this::serverReloadListener);
		neoBus.addListener(this::clientRecipeReload);
		neoBus.addListener(this::effectApplicable);
		modBus.addListener(this::registerCaps);

		modBus.addListener(MGUNetwork::register);

		//Central Data generator, called on runData
		modBus.addListener(Generator::gatherData);
	}

	public void setup(FMLCommonSetupEvent event) {
		//event.enqueueWork(() -> CraftingHelper.register(FluidIngredient.Serializer.NAME, FluidIngredient.SERIALIZER));
	}

	private void doClientStuff(final FMLClientSetupEvent event) {
		IEventBus neoBus = NeoForge.EVENT_BUS;

		//neoBus.register(new FluidTextureStitchEvent());
		neoBus.register(new RenderChickenSwell());
		neoBus.register(new GlobalWitherSoundEvent());
		neoBus.register(new GlobalDragonSoundEvent());
		neoBus.register(new BossBarHidingEvent());
		neoBus.addListener(this::worldUnload);

		ItemBlockRenderTypes.setRenderLayer(ModBlocks.FLUID_XP_FLOWING.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.FLUID_XP.get(), RenderType.translucent());

		event.enqueueWork(ModColourManager::registerColourHandlers);
	}

	private void menuScreenEvent(final RegisterMenuScreensEvent event) {
		event.register(ModContainers.ABSORPTION_HOPPER.get(), GuiAbsorptionHopper::new);
		event.register(ModContainers.SOLIDIFIER.get(), GuiXPSolidifier::new);
		event.register(ModContainers.FAN.get(), GuiFan::new);
		event.register(ModContainers.SAW.get(), GuiSaw::new);
		event.register(ModContainers.ENTITY_SPAWNER.get(), GuiMGUSpawner::new);
	}

	public void onClientExtensions(RegisterClientExtensionsEvent event) {
		event.registerItem(new IClientItemExtensions() {
			@Nonnull
			@Override
			public BlockEntityWithoutLevelRenderer getCustomRenderer() {
				return new TileTankStackItemRenderer(null, null);
			}
		}, ModBlocks.JUMBO_TANK.getItem());

		event.registerItem(new IClientItemExtensions() {
			@Nonnull
			@Override
			public BlockEntityWithoutLevelRenderer getCustomRenderer() {
				return new TileXPSolidifierStackItemRenderer(null, null);
			}
		}, ModBlocks.XPSOLIDIFIER.getItem());

		event.registerItem(new IClientItemExtensions() {
			@Nonnull
			@Override
			public BlockEntityWithoutLevelRenderer getCustomRenderer() {
				return new TileTankStackItemRenderer(null, null);
			}
		}, ModBlocks.TANK.getItem());

		event.registerItem(new IClientItemExtensions() {
			@Nonnull
			@Override
			public BlockEntityWithoutLevelRenderer getCustomRenderer() {
				return new TileTankStackItemRenderer(null, null);
			}
		}, ModBlocks.TANK_SINK.getItem());

		event.registerItem(new IClientItemExtensions() {
			@Nonnull
			@Override
			public BlockEntityWithoutLevelRenderer getCustomRenderer() {
				return new TileSawStackItemRenderer(null, null);
			}
		}, ModBlocks.SAW.getItem());

		event.registerFluidType(new IClientFluidTypeExtensions() {
			ResourceLocation texture = RL.mgu("block/fluid_xp");
			@Override
			public ResourceLocation getStillTexture() {
				return texture;
			}

			@Override
			public ResourceLocation getFlowingTexture() {
				return texture;
			}

			@Override
			public int getTintColor(FluidStack stack) {
				return IClientFluidTypeExtensions.super.getTintColor(stack);
			}


		}, ModBlocks.XPTYPE.get());
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
			PacketDistributor.sendToPlayer(playerEntity, new FlagSyncPacket(nbt.getBoolean("MGU_WitherMuffle"), nbt.getBoolean("MGU_DragonMuffle")));
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
			SPIKE_DAMAGE = null;
		}
	}

	public static DamageSource getSpikeDamage(Level level) {
		if (SPIKE_DAMAGE == null)
			SPIKE_DAMAGE = new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(MobGrindingUtils.SPIKE_TYPE));

		return SPIKE_DAMAGE;
	}

	public void registerCaps(final RegisterCapabilitiesEvent evt) {
		evt.registerBlockEntity(Capabilities.FluidHandler.BLOCK, ModBlocks.TANK.getTileEntityType(), TileEntityTank::getTank);
		evt.registerBlockEntity(Capabilities.FluidHandler.BLOCK, ModBlocks.JUMBO_TANK.getTileEntityType(), TileEntityTank::getTank);
		evt.registerBlockEntity(Capabilities.FluidHandler.BLOCK, ModBlocks.TANK_SINK.getTileEntityType(), TileEntityTank::getTank);
		evt.registerBlockEntity(Capabilities.FluidHandler.BLOCK, ModBlocks.XPSOLIDIFIER.getTileEntityType(), TileEntityXPSolidifier::getTank);
		evt.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlocks.XPSOLIDIFIER.getTileEntityType(), TileEntityXPSolidifier::getOutput);
		evt.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlocks.ENTITY_SPAWNER.getTileEntityType(), TileEntityMGUSpawner::getFuelSlot);
		evt.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlocks.ABSORPTION_HOPPER.getTileEntityType(), TileEntityAbsorptionHopper::getItemHandler);
		evt.registerBlockEntity(Capabilities.FluidHandler.BLOCK, ModBlocks.ABSORPTION_HOPPER.getTileEntityType(), TileEntityAbsorptionHopper::getTank);
	}

	public void effectApplicable(MobEffectEvent.Applicable event) {
		//Makes it so that the mob masher cant have effects applied to it
		if (event.getEntity() instanceof FakePlayer fakePlayer && FakePlayerHandler.isMGUFakePlayer(fakePlayer)) {
			event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
		}
	}
}