package mob_grinding_utils;

import mob_grinding_utils.blocks.BlockAbsorptionHopper;
import mob_grinding_utils.blocks.BlockSpikes;
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
import mob_grinding_utils.events.MGUEndermanInhibitEvent;
import mob_grinding_utils.events.MGUZombieReinforcementEvent;
import mob_grinding_utils.events.ParticleTextureStitchEvent;
import mob_grinding_utils.events.RenderChickenSwell;
import mob_grinding_utils.network.MessageAbsorptionHopper;
import mob_grinding_utils.network.MessageChickenSync;
import mob_grinding_utils.network.MessageFan;
import mob_grinding_utils.network.MessageSyncEntityCapabilities;
import mob_grinding_utils.network.MessageTapParticle;
import mob_grinding_utils.proxy.CommonProxy;
import mob_grinding_utils.tile.TileEntitySaw;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
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
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)

public class MobGrindingUtils {

	@Instance(Reference.MOD_ID)
	public static MobGrindingUtils INSTANCE;

	@SidedProxy(clientSide = Reference.PROXY_CLIENT, serverSide = Reference.PROXY_COMMON)
	public static CommonProxy PROXY;
	public static Fluid FLUID_XP;
	public static SimpleNetworkWrapper NETWORK_WRAPPER;
	public static DamageSource SPIKE_DAMAGE;

	public static CreativeTabs TAB = new CreativeTabs(Reference.MOD_NAME) {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(ModBlocks.SPIKES_ITEM);
		}
	};

	static { 
		FluidRegistry.enableUniversalBucket();
	} 

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		if (!Loader.isModLoaded("openblocks") && !Loader.isModLoaded("enderio")) {
			FLUID_XP = new Fluid("xpjuice", new ResourceLocation("mob_grinding_utils:fluids/fluid_xp"), new ResourceLocation("mob_grinding_utils:fluids/fluid_xp")).setLuminosity(10).setDensity(800).setViscosity(1500).setUnlocalizedName("mob_grinding_utils.fluid_xp");
			FluidRegistry.registerFluid(FLUID_XP);
			FluidRegistry.addBucketForFluid(FLUID_XP);
			if (event.getSide() == Side.CLIENT)
				MinecraftForge.EVENT_BUS.register(new BlockAbsorptionHopper());
		}

		SPIKE_DAMAGE = new DamageSource("spikes").setDamageBypassesArmor();

		PROXY.registerTileEntities();
		PROXY.registerRenderers();
		NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, PROXY);
		NETWORK_WRAPPER = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_NAME);
		NETWORK_WRAPPER.registerMessage(MessageAbsorptionHopper.class, MessageAbsorptionHopper.class, 0, Side.SERVER);
		NETWORK_WRAPPER.registerMessage(MessageChickenSync.class, MessageChickenSync.class, 1, Side.CLIENT);
		NETWORK_WRAPPER.registerMessage(MessageTapParticle.class, MessageTapParticle.class, 2, Side.CLIENT);
		NETWORK_WRAPPER.registerMessage(MessageSyncEntityCapabilities.class, MessageSyncEntityCapabilities.class, 3, Side.CLIENT);
		NETWORK_WRAPPER.registerMessage(MessageFan.class, MessageFan.class, 4, Side.SERVER);
		
		MinecraftForge.EVENT_BUS.register(new BlockSpikes());
		MinecraftForge.EVENT_BUS.register(new ChickenInteractionEvent());
		MinecraftForge.EVENT_BUS.register(new ChickenFuseEvent());
		MinecraftForge.EVENT_BUS.register(new LocalWitherSoundEvent());
		MinecraftForge.EVENT_BUS.register(new LocalDragonSoundEvent());
		MinecraftForge.EVENT_BUS.register(new EntityHeadDropEvent());
		MinecraftForge.EVENT_BUS.register(EntityCapabilityHandler.class);
		MinecraftForge.EVENT_BUS.register(new MGUZombieReinforcementEvent());
		MinecraftForge.EVENT_BUS.register(new FillXPBottleEvent());
		MinecraftForge.EVENT_BUS.register(new MGUEndermanInhibitEvent());

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
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		PROXY.postInit();
	}

}