package mob_grinding_utils;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

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
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks {
	
	public static Block SPIKES, FAN, ABSORPTION_HOPPER, TANK, TANK_SINK, XP_TAP, WITHER_MUFFLER, DRAGON_MUFFLER, SAW, DARK_OAK_STONE;
	public static ItemBlock SPIKES_ITEM, FAN_ITEM, ABSORPTION_HOPPER_ITEM, TANK_ITEM, TANK_SINK_ITEM, XP_TAP_ITEM, WITHER_MUFFLER_ITEM, DRAGON_MUFFLER_ITEM, SAW_ITEM, DARK_OAK_STONE_ITEM;
	
	public static void init() {
		FAN = new BlockFan();
		FAN_ITEM = new ItemBlock(FAN) {
			@Override
			@SideOnly(Side.CLIENT)
			public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> list, ITooltipFlag flag) {
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.fan_1").getFormattedText());
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.fan_2").getFormattedText());
			}
		};
		FAN.setRegistryName("mob_grinding_utils", "fan").setUnlocalizedName("mob_grinding_utils.fan");
		FAN_ITEM.setRegistryName(FAN.getRegistryName()).setUnlocalizedName("mob_grinding_utils.fan");

		SAW = new BlockSaw();
		SAW_ITEM = new ItemBlock(SAW) {
			@Override
			@SideOnly(Side.CLIENT)
			public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> list, ITooltipFlag flag) {
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.saw_1").getFormattedText());
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.saw_2").getFormattedText());
			}
		};
		SAW.setRegistryName("mob_grinding_utils", "saw").setUnlocalizedName("mob_grinding_utils.saw");
		SAW_ITEM.setRegistryName(SAW.getRegistryName()).setUnlocalizedName("mob_grinding_utils.saw");

		ABSORPTION_HOPPER = new BlockAbsorptionHopper();
		ABSORPTION_HOPPER_ITEM = new ItemBlock(ABSORPTION_HOPPER) {
			@Override
			@SideOnly(Side.CLIENT)
			public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> list, ITooltipFlag flag) {
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.hopper_1").getFormattedText());
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.hopper_2").getFormattedText());
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.hopper_3").getFormattedText());
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.hopper_4").getFormattedText());
			}
		};
		ABSORPTION_HOPPER.setRegistryName("mob_grinding_utils", "absorption_hopper").setUnlocalizedName("mob_grinding_utils.absorption_hopper");
		ABSORPTION_HOPPER_ITEM.setRegistryName(ABSORPTION_HOPPER.getRegistryName()).setUnlocalizedName("mob_grinding_utils.absorption_hopper");

		SPIKES = new BlockSpikes();
		SPIKES_ITEM = new ItemBlock(SPIKES) {
			@Override
			@SideOnly(Side.CLIENT)
			public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> list, ITooltipFlag flag) {
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.spikes_1").getFormattedText());
			}
		};
		SPIKES.setRegistryName("mob_grinding_utils", "spikes").setUnlocalizedName("mob_grinding_utils.spikes");
		SPIKES_ITEM.setRegistryName(SPIKES.getRegistryName()).setUnlocalizedName("mob_grinding_utils.spikes");

		TANK = new BlockTank();
		TANK_ITEM = new ItemBlock(TANK) {
			@Override
			@SideOnly(Side.CLIENT)
			public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> list, ITooltipFlag flag) {
				if(stack.hasTagCompound() && !stack.getTagCompound().hasKey("Empty")) {
					FluidStack fluid = FluidStack.loadFluidStackFromNBT(stack.getTagCompound());
					if(fluid !=null) {
						list.add(TextFormatting.GREEN + "Contains: "+ fluid.getFluid().getLocalizedName(fluid));
						list.add(TextFormatting.BLUE + ""+ fluid.amount +"Mb/32000Mb");
					}
				}
			}
		};
		TANK.setRegistryName("mob_grinding_utils", "tank").setUnlocalizedName("mob_grinding_utils.tank");
		TANK_ITEM.setRegistryName(TANK.getRegistryName()).setUnlocalizedName("mob_grinding_utils.tank");

		TANK_SINK = new BlockTankSink();
		TANK_SINK_ITEM = new ItemBlock(TANK_SINK) {
			@Override
			@SideOnly(Side.CLIENT)
			public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> list, ITooltipFlag flag) {
				if(stack.hasTagCompound() && !stack.getTagCompound().hasKey("Empty")) {
					FluidStack fluid = FluidStack.loadFluidStackFromNBT(stack.getTagCompound());
					if(fluid !=null) {
						list.add(TextFormatting.GREEN + "Contains: "+ fluid.getFluid().getLocalizedName(fluid));
						list.add(TextFormatting.BLUE + ""+ fluid.amount +"Mb/32000Mb");
					}
				}
			}
		};
		TANK_SINK.setRegistryName("mob_grinding_utils", "tank_sink").setUnlocalizedName("mob_grinding_utils.tank_sink");
		TANK_SINK_ITEM.setRegistryName(TANK_SINK.getRegistryName()).setUnlocalizedName("mob_grinding_utils.tank_sink");

		XP_TAP = new BlockXPTap();
		XP_TAP_ITEM = new ItemBlock(XP_TAP) {
			@Override
			@SideOnly(Side.CLIENT)
			public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> list, ITooltipFlag flag) {
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.xptap_1").getFormattedText());
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.xptap_2").getFormattedText());
			}
		};
		XP_TAP.setRegistryName("mob_grinding_utils", "xp_tap").setUnlocalizedName("mob_grinding_utils.xp_tap");
		XP_TAP_ITEM.setRegistryName(XP_TAP.getRegistryName()).setUnlocalizedName("mob_grinding_utils.xp_tap");

		WITHER_MUFFLER = new BlockWitherMuffler();
		WITHER_MUFFLER_ITEM = new ItemBlock(WITHER_MUFFLER) {
			@Override
			@SideOnly(Side.CLIENT)
			public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> list, ITooltipFlag flag) {
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.withermuffler_1").getFormattedText());
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.withermuffler_2").getFormattedText());
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.withermuffler_3").getFormattedText());
			}
		};
		WITHER_MUFFLER.setRegistryName("mob_grinding_utils", "wither_muffler").setUnlocalizedName("mob_grinding_utils.wither_muffler");
		WITHER_MUFFLER_ITEM.setRegistryName(WITHER_MUFFLER.getRegistryName()).setUnlocalizedName("mob_grinding_utils.wither_muffler");

		DRAGON_MUFFLER = new BlockDragonMuffler();
		DRAGON_MUFFLER_ITEM = new ItemBlock(DRAGON_MUFFLER) {
			@Override
			@SideOnly(Side.CLIENT)
			public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> list, ITooltipFlag flag) {
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.dragonmuffler_1").getFormattedText());
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.dragonmuffler_2").getFormattedText());
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.dragonmuffler_3").getFormattedText());
			}
		};
		DRAGON_MUFFLER.setRegistryName("mob_grinding_utils", "dragon_muffler").setUnlocalizedName("mob_grinding_utils.dragon_muffler");
		DRAGON_MUFFLER_ITEM.setRegistryName(DRAGON_MUFFLER.getRegistryName()).setUnlocalizedName("mob_grinding_utils.dragon_muffler");

		DARK_OAK_STONE= new BlockDarkOakStone();
		DARK_OAK_STONE_ITEM = new ItemBlock(DARK_OAK_STONE) {
			@Override
			@SideOnly(Side.CLIENT)
			public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> list, ITooltipFlag flag) {
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.darkoakstone").getFormattedText());
			}
		};
		DARK_OAK_STONE.setRegistryName("mob_grinding_utils", "dark_oak_stone").setUnlocalizedName("mob_grinding_utils.dark_oak_stone");
		DARK_OAK_STONE_ITEM.setRegistryName(DARK_OAK_STONE.getRegistryName()).setUnlocalizedName("mob_grinding_utils.dark_oak_stone");	
	}

	@Mod.EventBusSubscriber(modid = "mob_grinding_utils")
	public static class RegistrationHandlerBlocks {
		public static final List<Block> BLOCKS = new ArrayList<Block>();
		public static final List<Item> ITEM_BLOCKS = new ArrayList<Item>();

		@SubscribeEvent
		public static void registerBlocks(final RegistryEvent.Register<Block> event) {
			final Block[] blocks = {
					SPIKES,
					FAN,
					ABSORPTION_HOPPER,
					TANK,
					TANK_SINK,
					XP_TAP,
					WITHER_MUFFLER,
					DRAGON_MUFFLER,
					SAW,
					DARK_OAK_STONE
			};
			final IForgeRegistry<Block> registry = event.getRegistry();
			for (final Block block : blocks) {
				registry.register(block);
				BLOCKS.add(block);
			}
		}

		@SubscribeEvent
		public static void registerItemBlocks(final RegistryEvent.Register<Item> event) {
			final ItemBlock[] items = {
					SPIKES_ITEM,
					FAN_ITEM,
					ABSORPTION_HOPPER_ITEM,
					TANK_ITEM,
					TANK_SINK_ITEM,
					XP_TAP_ITEM,
					WITHER_MUFFLER_ITEM,
					DRAGON_MUFFLER_ITEM,
					SAW_ITEM,
					DARK_OAK_STONE_ITEM
			};
			final IForgeRegistry<Item> registry = event.getRegistry();
			for (final ItemBlock item : items) {
				registry.register(item);
				ITEM_BLOCKS.add(item);
			}
		}
	}
}
