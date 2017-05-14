package mob_grinding_utils;

import java.util.List;

import mob_grinding_utils.blocks.BlockAbsorptionHopper;
import mob_grinding_utils.blocks.BlockDarkOakStone;
import mob_grinding_utils.blocks.BlockDragonMuffler;
import mob_grinding_utils.blocks.BlockFan;
import mob_grinding_utils.blocks.BlockSaw;
import mob_grinding_utils.blocks.BlockSpikes;
import mob_grinding_utils.blocks.BlockTank;
import mob_grinding_utils.blocks.BlockTankSink;
import mob_grinding_utils.blocks.BlockWitherMuffler;
import mob_grinding_utils.blocks.BlockXPTap;
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
import mob_grinding_utils.items.ItemAbsorptionUpgrade;
import mob_grinding_utils.items.ItemFanUpgrade;
import mob_grinding_utils.items.ItemGMChickenFeed;
import mob_grinding_utils.items.ItemImaginaryInvisibleNotReallyThereSword;
import mob_grinding_utils.items.ItemMobSwab;
import mob_grinding_utils.items.ItemSawUpgrade;
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
	public static Block SPIKES, FAN, ABSORPTION_HOPPER, TANK, TANK_SINK, XP_TAP, WITHER_MUFFLER, DRAGON_MUFFLER, SAW, DARK_OAK_STONE;
	public static Item SPIKES_ITEM, FAN_ITEM, ABSORPTION_HOPPER_ITEM, TANK_ITEM, TANK_SINK_ITEM, FAN_UPGRADE, ABSORPTION_UPGRADE, XP_TAP_ITEM, MOB_SWAB, GM_CHICKEN_FEED, WITHER_MUFFLER_ITEM, DRAGON_MUFFLER_ITEM, SAW_ITEM, SAW_UPGRADE, DARK_OAK_STONE_ITEM;
	public static ItemSword NULL_SWORD;
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
		FAN = new BlockFan();
		FAN_ITEM = new ItemBlock(FAN){
			@Override
			@SideOnly(Side.CLIENT)
			public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.fan_1").getFormattedText());
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.fan_2").getFormattedText());
			}
		};
		GameRegistry.register(FAN.setRegistryName("mob_grinding_utils", "fan").setUnlocalizedName("mob_grinding_utils.fan"));
		GameRegistry.register(FAN_ITEM.setRegistryName(FAN.getRegistryName()).setUnlocalizedName("mob_grinding_utils.fan"));

		SAW = new BlockSaw();
		SAW_ITEM = new ItemBlock(SAW){
			@Override
			@SideOnly(Side.CLIENT)
			public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.saw_1").getFormattedText());
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.saw_2").getFormattedText());
			}
		};
		GameRegistry.register(SAW.setRegistryName("mob_grinding_utils", "saw").setUnlocalizedName("mob_grinding_utils.saw"));
		GameRegistry.register(SAW_ITEM.setRegistryName(SAW.getRegistryName()).setUnlocalizedName("mob_grinding_utils.saw"));

		ABSORPTION_HOPPER = new BlockAbsorptionHopper();
		ABSORPTION_HOPPER_ITEM = new ItemBlock(ABSORPTION_HOPPER) {
			@Override
			@SideOnly(Side.CLIENT)
			public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.hopper_1").getFormattedText());
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.hopper_2").getFormattedText());
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.hopper_3").getFormattedText());
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.hopper_4").getFormattedText());
			}
		};
		GameRegistry.register(ABSORPTION_HOPPER.setRegistryName("mob_grinding_utils", "absorption_hopper").setUnlocalizedName("mob_grinding_utils.absorption_hopper"));
		GameRegistry.register(ABSORPTION_HOPPER_ITEM.setRegistryName(ABSORPTION_HOPPER.getRegistryName()).setUnlocalizedName("mob_grinding_utils.absorption_hopper"));

		SPIKES = new BlockSpikes();
		SPIKES_ITEM = new ItemBlock(SPIKES) {
			@Override
			@SideOnly(Side.CLIENT)
			public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.spikes_1").getFormattedText());
			}
		};
		GameRegistry.register(SPIKES.setRegistryName("mob_grinding_utils", "spikes").setUnlocalizedName("mob_grinding_utils.spikes"));
		GameRegistry.register(SPIKES_ITEM.setRegistryName(SPIKES.getRegistryName()).setUnlocalizedName("mob_grinding_utils.spikes"));

		TANK = new BlockTank();
		TANK_ITEM = new ItemBlock(TANK) {
			@Override
			@SideOnly(Side.CLIENT)
			public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
				if(stack.hasTagCompound() && !stack.getTagCompound().hasKey("Empty")) {
					FluidStack fluid = FluidStack.loadFluidStackFromNBT(stack.getTagCompound());
					if(fluid !=null) {
						list.add(TextFormatting.GREEN + "Contains: "+ fluid.getFluid().getLocalizedName(fluid));
						list.add(TextFormatting.BLUE + ""+ fluid.amount +"Mb/32000Mb");
					}
				}
			}
		};
		GameRegistry.register(TANK.setRegistryName("mob_grinding_utils", "tank").setUnlocalizedName("mob_grinding_utils.tank"));
		GameRegistry.register(TANK_ITEM.setRegistryName(TANK.getRegistryName()).setUnlocalizedName("mob_grinding_utils.tank"));

		TANK_SINK = new BlockTankSink();
		TANK_SINK_ITEM = new ItemBlock(TANK_SINK) {
			@Override
			@SideOnly(Side.CLIENT)
			public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
				if(stack.hasTagCompound() && !stack.getTagCompound().hasKey("Empty")) {
					FluidStack fluid = FluidStack.loadFluidStackFromNBT(stack.getTagCompound());
					if(fluid !=null) {
						list.add(TextFormatting.GREEN + "Contains: "+ fluid.getFluid().getLocalizedName(fluid));
						list.add(TextFormatting.BLUE + ""+ fluid.amount +"Mb/32000Mb");
					}
				}
			}
		};
		GameRegistry.register(TANK_SINK.setRegistryName("mob_grinding_utils", "tank_sink").setUnlocalizedName("mob_grinding_utils.tank_sink"));
		GameRegistry.register(TANK_SINK_ITEM.setRegistryName(TANK_SINK.getRegistryName()).setUnlocalizedName("mob_grinding_utils.tank_sink"));

		XP_TAP = new BlockXPTap();
		XP_TAP_ITEM = new ItemBlock(XP_TAP) {
			@Override
			@SideOnly(Side.CLIENT)
			public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.xptap_1").getFormattedText());
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.xptap_2").getFormattedText());
			}
		};
		GameRegistry.register(XP_TAP.setRegistryName("mob_grinding_utils", "xp_tap").setUnlocalizedName("mob_grinding_utils.xp_tap"));
		GameRegistry.register(XP_TAP_ITEM.setRegistryName(XP_TAP.getRegistryName()).setUnlocalizedName("mob_grinding_utils.xp_tap"));

		WITHER_MUFFLER = new BlockWitherMuffler();
		WITHER_MUFFLER_ITEM = new ItemBlock(WITHER_MUFFLER) {
			@Override
			@SideOnly(Side.CLIENT)
			public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.withermuffler_1").getFormattedText());
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.withermuffler_2").getFormattedText());
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.withermuffler_3").getFormattedText());
			}
		};
		GameRegistry.register(WITHER_MUFFLER.setRegistryName("mob_grinding_utils", "wither_muffler").setUnlocalizedName("mob_grinding_utils.wither_muffler"));
		GameRegistry.register(WITHER_MUFFLER_ITEM.setRegistryName(WITHER_MUFFLER.getRegistryName()).setUnlocalizedName("mob_grinding_utils.wither_muffler"));

		DRAGON_MUFFLER = new BlockDragonMuffler();
		DRAGON_MUFFLER_ITEM = new ItemBlock(DRAGON_MUFFLER) {
			@Override
			@SideOnly(Side.CLIENT)
			public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.dragonmuffler_1").getFormattedText());
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.dragonmuffler_2").getFormattedText());
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.dragonmuffler_3").getFormattedText());
			}
		};
		GameRegistry.register(DRAGON_MUFFLER.setRegistryName("mob_grinding_utils", "dragon_muffler").setUnlocalizedName("mob_grinding_utils.dragon_muffler"));
		GameRegistry.register(DRAGON_MUFFLER_ITEM.setRegistryName(DRAGON_MUFFLER.getRegistryName()).setUnlocalizedName("mob_grinding_utils.dragon_muffler"));

		DARK_OAK_STONE= new BlockDarkOakStone();
		DARK_OAK_STONE_ITEM = new ItemBlock(DARK_OAK_STONE) {
			@Override
			@SideOnly(Side.CLIENT)
			public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.darkoakstone").getFormattedText());
			}
		};
		GameRegistry.register(DARK_OAK_STONE.setRegistryName("mob_grinding_utils", "dark_oak_stone").setUnlocalizedName("mob_grinding_utils.dark_oak_stone"));
		GameRegistry.register(DARK_OAK_STONE_ITEM.setRegistryName(DARK_OAK_STONE.getRegistryName()).setUnlocalizedName("mob_grinding_utils.dark_oak_stone"));

		FAN_UPGRADE = new ItemFanUpgrade();
		GameRegistry.register(FAN_UPGRADE.setRegistryName("mob_grinding_utils", "fan_upgrade").setUnlocalizedName("mob_grinding_utils.fan_upgrade"));

		ABSORPTION_UPGRADE = new ItemAbsorptionUpgrade();
		GameRegistry.register(ABSORPTION_UPGRADE.setRegistryName("mob_grinding_utils", "absorption_upgrade").setUnlocalizedName("mob_grinding_utils.absorption_upgrade"));

		SAW_UPGRADE = new ItemSawUpgrade();
		GameRegistry.register(SAW_UPGRADE.setRegistryName("mob_grinding_utils", "saw_upgrade").setUnlocalizedName("mob_grinding_utils.saw_upgrade"));

		MOB_SWAB = new ItemMobSwab();
		GameRegistry.register(MOB_SWAB.setRegistryName("mob_grinding_utils", "mob_swab").setUnlocalizedName("mob_grinding_utils.mob_swab"));

		GM_CHICKEN_FEED = new ItemGMChickenFeed();
		GameRegistry.register(GM_CHICKEN_FEED.setRegistryName("mob_grinding_utils", "gm_chicken_feed").setUnlocalizedName("mob_grinding_utils.gm_chicken_feed"));

		if (!Loader.isModLoaded("OpenBlocks") && !Loader.isModLoaded("EnderIO")) {
			FLUID_XP = new Fluid("xpjuice", new ResourceLocation("mob_grinding_utils:fluids/fluid_xp"), new ResourceLocation("mob_grinding_utils:fluids/fluid_xp")).setLuminosity(10).setDensity(800).setViscosity(1500).setUnlocalizedName("mob_grinding_utils.fluid_xp");
			FluidRegistry.registerFluid(FLUID_XP);
			FluidRegistry.addBucketForFluid(FLUID_XP);
			if (event.getSide() == Side.CLIENT)
				MinecraftForge.EVENT_BUS.register(ABSORPTION_HOPPER);
		}

		NULL_SWORD = new ItemImaginaryInvisibleNotReallyThereSword();
		GameRegistry.register(NULL_SWORD.setRegistryName("mob_grinding_utils", "null_sword").setUnlocalizedName("mob_grinding_utils.null_sword"));

		SPIKE_DAMAGE = new DamageSource("spikes").setDamageBypassesArmor();

		PROXY.registerTileEntities();
		PROXY.registerRenderers();
		ModRecipes.addRecipes();

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