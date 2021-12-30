package mob_grinding_utils;

import javax.annotation.Nonnull;

import mob_grinding_utils.blocks.BlockSpikes;
import mob_grinding_utils.client.ModelLayers;
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
import mob_grinding_utils.network.MGUNetwork;
import mob_grinding_utils.network.MessageFlagSync;
import mob_grinding_utils.recipe.ChickenFeedRecipe;
import mob_grinding_utils.recipe.FluidIngredient;
import mob_grinding_utils.recipe.SolidifyRecipe;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;


@Mod(Reference.MOD_ID)
public class MobGrindingUtils {
	public static SimpleChannel NETWORK_WRAPPER;
	public static DamageSource SPIKE_DAMAGE;

	//Tags
	public static final Tag.Named<Fluid> EXPERIENCE = FluidTags.bind(new ResourceLocation("forge", "experience").toString());
	public static final Tag.Named<Fluid> XPJUICE = FluidTags.bind(new ResourceLocation("forge", "xpjuice").toString());
	public static final Tag.Named<EntityType<?>> NOSWAB = EntityTypeTags.createOptional(new ResourceLocation(Reference.MOD_NAME, "noswab"));

	public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Reference.MOD_ID);
	public static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Reference.MOD_ID);

	public static final RegistryObject<SimpleParticleType> PARTICLE_FLUID_XP = PARTICLES.register("fluid_xp_particles", () -> new SimpleParticleType(true));

	public static final RegistryObject<RecipeSerializer<?>> CHICKEN_FEED = RECIPES.register(ChickenFeedRecipe.NAME, ChickenFeedRecipe.Serializer::new);

	public static final List<SolidifyRecipe> SOLIDIFIER_RECIPES = new ArrayList<>();
	public static final RegistryObject<RecipeSerializer<?>> SOLIDIFIER_RECIPE = RECIPES.register(SolidifyRecipe.NAME, SolidifyRecipe.Serializer::new);
	public static final RecipeType<SolidifyRecipe> SOLIDIFIER_TYPE = RecipeType.register(Reference.MOD_ID + ":solidify");

	public static final CreativeModeTab TAB = new CreativeModeTab(Reference.MOD_ID) {
		@Nonnull
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(ModBlocks.SPIKES.getItem());
		}
	};

	public MobGrindingUtils() {
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		ModBlocks.init(modBus);
		ModItems.init(modBus);
		ModContainers.init(modBus);
		PARTICLES.register(modBus);
		RECIPES.register(modBus);
		ModelLayers.init(modBus);

		modBus.addListener(this::setup);
		modBus.addListener(this::doClientStuff);

		//Central Data generator, called on runData
		modBus.addListener(Generator::gatherData);
	}

	public void setup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> CraftingHelper.register(FluidIngredient.Serializer.NAME, FluidIngredient.SERIALIZER));

		SPIKE_DAMAGE = new DamageSource("spikes").bypassArmor();

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

		MenuScreens.register(ModContainers.ABSORPTION_HOPPER.get(), GuiAbsorptionHopper::new);
		MenuScreens.register(ModContainers.SOLIDIFIER.get(), GuiXPSolidifier::new);
		MenuScreens.register(ModContainers.FAN.get(), GuiFan::new);
		MenuScreens.register(ModContainers.SAW.get(), GuiSaw::new);
		MenuScreens.register(ModContainers.ENTITY_SPAWNER.get(), GuiMGUSpawner::new);

		ItemBlockRenderTypes.setRenderLayer(ModBlocks.TANK.getBlock(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.TANK_SINK.getBlock(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.XP_TAP.getBlock(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.ENDER_INHIBITOR_ON.getBlock(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.ENDER_INHIBITOR_OFF.getBlock(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.SAW.getBlock(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.SPIKES.getBlock(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.ABSORPTION_HOPPER.getBlock(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.TINTED_GLASS.getBlock(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.JUMBO_TANK.getBlock(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.DREADFUL_DIRT.getBlock(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.DELIGHTFUL_DIRT.getBlock(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.XPSOLIDIFIER.getBlock(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.SOLID_XP_BLOCK.getBlock(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.FLUID_XP_FLOWING.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.FLUID_XP.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.ENTITY_SPAWNER.getBlock(), RenderType.cutout());

		ModColourManager.registerColourHandlers();
	}

	private void serverReloadListener(final AddReloadListenerEvent event) {
		event.addListener(new ServerResourceReloader(event.getDataPackRegistries()));
	}
	private void clientRecipeReload(final RecipesUpdatedEvent event) {
		SOLIDIFIER_RECIPES.clear();
		SOLIDIFIER_RECIPES.addAll(event.getRecipeManager().getAllRecipesFor(SOLIDIFIER_TYPE));
	}

	private void playerConnected(final PlayerEvent.PlayerLoggedInEvent event) {
		if (event.getPlayer() instanceof ServerPlayer) {
			sendPersistentData((ServerPlayer) event.getPlayer());
		}
	}

	private void changedDimension(final PlayerEvent.PlayerChangedDimensionEvent event) {
		if (event.getPlayer() instanceof ServerPlayer) {
			sendPersistentData((ServerPlayer) event.getPlayer());
		}
	}
	private void playerRespawn(final PlayerEvent.PlayerRespawnEvent event) {
		if (event.getPlayer() instanceof ServerPlayer) {
			sendPersistentData((ServerPlayer) event.getPlayer());
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
			CompoundTag newNBT = event.getPlayer().getPersistentData();
			newNBT.putBoolean("MGU_WitherMuffle", nbt.getBoolean("MGU_WitherMuffle"));
			newNBT.putBoolean("MGU_DragonMuffle", nbt.getBoolean("MGU_DragonMuffle"));
		}
	}

	private void worldUnload(final WorldEvent.Unload event) {
		if (event.getWorld() instanceof ServerLevel) {
			MGUFakePlayer.unload(event.getWorld());
		}
	}
}