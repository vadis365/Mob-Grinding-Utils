package mob_grinding_utils;

import static mob_grinding_utils.ItemBlockRegister.*;
import mob_grinding_utils.capability.base.EntityCapabilityHandler;
import mob_grinding_utils.capability.bossbars.BossBarPlayerCapability;
import mob_grinding_utils.events.BossBarHidingEvent;
import mob_grinding_utils.events.ChickenFuseEvent;
import mob_grinding_utils.events.ChickenInteractionEvent;
import mob_grinding_utils.events.EntityHeadDropEvent;
import mob_grinding_utils.events.FillXPBottleEvent;
import mob_grinding_utils.events.GlobalDragonSoundEvent;
import mob_grinding_utils.events.GlobalWitherSoundEvent;
import mob_grinding_utils.events.LocalDragonSoundEvent;
import mob_grinding_utils.events.LocalWitherSoundEvent;
import mob_grinding_utils.events.MGUZombieReinforcementEvent;
import mob_grinding_utils.events.ParticleTextureStitchEvent;
import mob_grinding_utils.events.RenderChickenSwell;
import mob_grinding_utils.network.AbsorptionHopperMessage;
import mob_grinding_utils.network.AbsorptionHopperPacketHandler;
import mob_grinding_utils.network.ChickenSyncMessage;
import mob_grinding_utils.network.ChickenSyncPacketHandler;
import mob_grinding_utils.network.MessageSyncEntityCapabilities;
import mob_grinding_utils.network.TapParticleHandler;
import mob_grinding_utils.network.TapParticleMessage;
import mob_grinding_utils.proxy.CommonProxy;
import mob_grinding_utils.recipe.ModRecipes;
import mob_grinding_utils.recipe.RecipeChickenFeed;
import mob_grinding_utils.tile.TileEntitySaw;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = "mob_grinding_utils", name = "mob_grinding_utils", version = "0.1.8.20")

public class MobGrindingUtils {

	@Instance("mob_grinding_utils")
	public static MobGrindingUtils INSTANCE;

	@SidedProxy(clientSide = "mob_grinding_utils.proxy.ClientProxy", serverSide = "mob_grinding_utils.proxy.CommonProxy")
	public static CommonProxy PROXY;
	public static Fluid FLUID_XP;
	public static SimpleNetworkWrapper NETWORK_WRAPPER;
	public static DamageSource SPIKE_DAMAGE;
	public static SoundEvent TAP_SQUEAK, ENTITY_WITHER_SPAWN_LOCAL, ENTITY_WITHER_DEATH_LOCAL, ENTITY_DRAGON_DEATH_LOCAL, CHICKEN_RISE;

	public static CreativeTabs TAB = new CreativeTabs("mob_grinding_utils") {
		@Override
		public Item getTabIconItem() {
			return SPIKES_ITEM;
		}
	};

	static { 
		FluidRegistry.enableUniversalBucket();
	} 

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		ItemBlockRegister.preinit();
		if (!Loader.isModLoaded("OpenBlocks") && !Loader.isModLoaded("EnderIO")) {
			FLUID_XP = new Fluid("xpjuice", new ResourceLocation("mob_grinding_utils:fluids/fluid_xp"), new ResourceLocation("mob_grinding_utils:fluids/fluid_xp")).setLuminosity(10).setDensity(800).setViscosity(1500).setUnlocalizedName("mob_grinding_utils.fluid_xp");
			FluidRegistry.registerFluid(FLUID_XP);
			FluidRegistry.addBucketForFluid(FLUID_XP);
			if (event.getSide() == Side.CLIENT)
				MinecraftForge.EVENT_BUS.register(ABSORPTION_HOPPER);
		}
		SPIKE_DAMAGE = new DamageSource("spikes").setDamageBypassesArmor();
		PROXY.registerTileEntities();
		PROXY.registerRenderers();
		NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, PROXY);
		NETWORK_WRAPPER = NetworkRegistry.INSTANCE.newSimpleChannel("mob_grinding_utils");
		NETWORK_WRAPPER.registerMessage(AbsorptionHopperPacketHandler.class, AbsorptionHopperMessage.class, 0, Side.SERVER);
		NETWORK_WRAPPER.registerMessage(ChickenSyncPacketHandler.class, ChickenSyncMessage.class, 1, Side.CLIENT);
		NETWORK_WRAPPER.registerMessage(TapParticleHandler.class, TapParticleMessage.class, 2, Side.CLIENT);
		NETWORK_WRAPPER.registerMessage(MessageSyncEntityCapabilities.class, MessageSyncEntityCapabilities.class, 5, Side.CLIENT);

		MinecraftForge.EVENT_BUS.register(SPIKES);
		MinecraftForge.EVENT_BUS.register(new ChickenInteractionEvent());
		MinecraftForge.EVENT_BUS.register(new ChickenFuseEvent());
		MinecraftForge.EVENT_BUS.register(new LocalWitherSoundEvent());
		MinecraftForge.EVENT_BUS.register(new LocalDragonSoundEvent());
		MinecraftForge.EVENT_BUS.register(new EntityHeadDropEvent());
		MinecraftForge.EVENT_BUS.register(EntityCapabilityHandler.class);
		MinecraftForge.EVENT_BUS.register(new MGUZombieReinforcementEvent());
		MinecraftForge.EVENT_BUS.register(new FillXPBottleEvent());
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
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		TAP_SQUEAK = new SoundEvent(new ResourceLocation("mob_grinding_utils", "tap_squeak")).setRegistryName("mob_grinding_utils", "tap_squeak");
		GameRegistry.register(TAP_SQUEAK);
		ENTITY_WITHER_SPAWN_LOCAL = new SoundEvent(new ResourceLocation("mob_grinding_utils", "entity_wither_spawn_local")).setRegistryName("mob_grinding_utils", "entity_wither_spawn_local");
		GameRegistry.register(ENTITY_WITHER_SPAWN_LOCAL);
		ENTITY_WITHER_DEATH_LOCAL = new SoundEvent(new ResourceLocation("mob_grinding_utils", "entity_wither_death_local")).setRegistryName("mob_grinding_utils", "entity_wither_death_local");
		GameRegistry.register(ENTITY_WITHER_DEATH_LOCAL);
		ENTITY_DRAGON_DEATH_LOCAL = new SoundEvent(new ResourceLocation("mob_grinding_utils", "entity_dragon_death_local")).setRegistryName("mob_grinding_utils", "entity_dragon_death_local");
		GameRegistry.register(ENTITY_DRAGON_DEATH_LOCAL);
		CHICKEN_RISE = new SoundEvent(new ResourceLocation("mob_grinding_utils", "chicken_rise")).setRegistryName("mob_grinding_utils", "chicken_rise");
		GameRegistry.register(CHICKEN_RISE);
		GameRegistry.addRecipe(new RecipeChickenFeed());
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		PROXY.postInit();
	}
}