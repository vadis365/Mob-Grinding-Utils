package mob_grinding_utils;

import javafx.geometry.Side;
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
import mob_grinding_utils.proxy.ClientProxy;
import mob_grinding_utils.proxy.CommonProxy;
import mob_grinding_utils.tile.TileEntitySaw;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.javafmlmod.FMLModLoadingContext;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

@Mod(Reference.MOD_ID)
public class MobGrindingUtils {

	public MobGrindingUtils() {
		// Register the preInit method for modloading
		FMLModLoadingContext.get().getModEventBus().addListener(this::preInit);
		// Register the init method for modloading
		FMLModLoadingContext.get().getModEventBus().addListener(this::init);
		// Register ourselves for server, registry and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(this);
	}

	 public static CommonProxy PROXY = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);
	 public static Fluid FLUID_XP;
	 public static SimpleChannel NETWORK_WRAPPER;
	 public static DamageSource SPIKE_DAMAGE;
		
	 public static ItemGroup TAB = new ItemGroup(Reference.MOD_ID) {
		 @Override
		 public ItemStack createIcon() {
			 return new ItemStack(ModBlocks.SPIKES_ITEM);
		 }
	 };

	static { 
		 FluidRegistry.enableUniversalBucket();
	 } 

	public void preInit(FMLPreInitializationEvent event) {
		//if (!Loader.isModLoaded("openblocks") && !Loader.isModLoaded("enderio")) {
		//	FLUID_XP = new Fluid("xpjuice", new ResourceLocation("mob_grinding_utils:fluids/fluid_xp"), new ResourceLocation("mob_grinding_utils:fluids/fluid_xp")).setLuminosity(10).setDensity(800).setViscosity(1500).setUnlocalizedName("mob_grinding_utils.fluid_xp");
		//	FluidRegistry.registerFluid(FLUID_XP);
		//	FluidRegistry.addBucketForFluid(FLUID_XP);
		//	if (event.getSide() == Side.CLIENT)
		//		MinecraftForge.EVENT_BUS.register(new BlockAbsorptionHopper());
		//}

		SPIKE_DAMAGE = new DamageSource("spikes").setDamageBypassesArmor();

		PROXY.registerTileEntities();
		PROXY.registerRenderers();
		//NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, PROXY);
		NETWORK_WRAPPER = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(Reference.MOD_ID, "net")).simpleChannel();
		
		NETWORK_WRAPPER.registerMessage(0, MessageAbsorptionHopper.class, MessageAbsorptionHopper::fromBytes, MessageAbsorptionHopper::toBytes, NetworkDirection.PLAY_TO_SERVER);
		NETWORK_WRAPPER.registerMessage(1, MessageChickenSync.class, MessageChickenSync::fromBytes, MessageChickenSync::toBytes, NetworkDirection.PLAY_TO_CLIENT);
		NETWORK_WRAPPER.registerMessage(2, MessageTapParticle.class, MessageTapParticle::fromBytes, MessageTapParticle::toBytes, NetworkDirection.PLAY_TO_CLIENT);
		NETWORK_WRAPPER.registerMessage(3, MessageSyncEntityCapabilities.class, MessageSyncEntityCapabilities::fromBytes, MessageSyncEntityCapabilities::toBytes, NetworkDirection.PLAY_TO_CLIENT);
		NETWORK_WRAPPER.registerMessage(4, MessageFan.class, MessageFan::fromBytes, MessageFan::toBytes, NetworkDirection.PLAY_TO_SERVER);

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

		PROXY.registerSomeClientEvents();

		EntityCapabilityHandler.registerEntityCapability(new BossBarPlayerCapability());
		EntityCapabilityHandler.registerCapabilities();
	}

	public void init(FMLInitializationEvent event) {
	}

	public void postInit(FMLPostInitializationEvent event) {
		PROXY.postInit();
	}

}