package mob_grinding_utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nullable;

import mob_grinding_utils.blocks.BlockAbsorptionHopper;
import mob_grinding_utils.blocks.BlockDarkOakStone;
import mob_grinding_utils.blocks.BlockDragonMuffler;
import mob_grinding_utils.blocks.BlockEnderInhibitorOff;
import mob_grinding_utils.blocks.BlockEnderInhibitorOn;
import mob_grinding_utils.blocks.BlockEntityConveyor;
import mob_grinding_utils.blocks.BlockFan;
import mob_grinding_utils.blocks.BlockSaw;
import mob_grinding_utils.blocks.BlockSpikes;
import mob_grinding_utils.blocks.BlockTank;
import mob_grinding_utils.blocks.BlockTankSink;
import mob_grinding_utils.blocks.BlockWitherMuffler;
import mob_grinding_utils.blocks.BlockXPTap;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks {
	private static final List<Block> BLOCKS = new LinkedList<Block>();
	public static final List<BlockItem> ITEM_BLOCKS = new ArrayList<BlockItem>();

	public static final Block FAN = new BlockFan();
	public static final BlockItem FAN_ITEM = new BlockItem(FAN) {
			@OnlyIn(Dist.CLIENT)
			@Override
			public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flag) {
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.fan_1").getFormattedText());
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.fan_2").getFormattedText());
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.fan_3").getFormattedText());
			}
		};

		public static final Block SAW = new BlockSaw();
		public static final BlockItem SAW_ITEM = new BlockItem(SAW) {
			@OnlyIn(Dist.CLIENT)
			@Override
			public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flag) {
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.saw_1").getFormattedText());
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.saw_2").getFormattedText());
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.saw_3").getFormattedText());
			}
		};

		public static final Block ABSORPTION_HOPPER = new BlockAbsorptionHopper();
		public static final BlockItem ABSORPTION_HOPPER_ITEM = new BlockItem(ABSORPTION_HOPPER) {
			@OnlyIn(Dist.CLIENT)
			@Override
			public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flag) {
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.hopper_1").getFormattedText());
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.hopper_2").getFormattedText());
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.hopper_3").getFormattedText());
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.hopper_4").getFormattedText());
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.hopper_5").getFormattedText());
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.hopper_6").getFormattedText());
			}
		};

		public static final Block SPIKES = new BlockSpikes();
		public static final BlockItem SPIKES_ITEM = new BlockItem(SPIKES) {
			@OnlyIn(Dist.CLIENT)
			@Override
			public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flag) {
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.spikes_1").getFormattedText());
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.spikes_2").getFormattedText());
			}
		};

		public static final Block TANK = new BlockTank();
		public static final BlockItem TANK_ITEM = new BlockItem(TANK) {
			@OnlyIn(Dist.CLIENT)
			@Override
			public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flag) {
				if(stack.hasTagCompound() && !stack.getTagCompound().hasKey("Empty")) {
					FluidStack fluid = FluidStack.loadFluidStackFromNBT(stack.getTagCompound());
					if(fluid !=null) {
						list.add(TextFormatting.GREEN + "Contains: "+ fluid.getFluid().getLocalizedName(fluid));
						list.add(TextFormatting.BLUE + ""+ fluid.amount +"Mb/32000Mb");
					}
				}
			}
		};

		public static final Block TANK_SINK = new BlockTankSink();
		public static final BlockItem TANK_SINK_ITEM = new BlockItem(TANK_SINK) {
			@OnlyIn(Dist.CLIENT)
			@Override
			public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flag) {
				if(stack.hasTagCompound() && !stack.getTagCompound().hasKey("Empty")) {
					FluidStack fluid = FluidStack.loadFluidStackFromNBT(stack.getTagCompound());
					if(fluid !=null) {
						list.add(TextFormatting.GREEN + "Contains: "+ fluid.getFluid().getLocalizedName(fluid));
						list.add(TextFormatting.BLUE + ""+ fluid.amount +"Mb/32000Mb");
					}
				}
			}
		};

		public static final Block XP_TAP = new BlockXPTap();
		public static final BlockItem XP_TAP_ITEM = new BlockItem(XP_TAP) {
			@OnlyIn(Dist.CLIENT)
			@Override
			public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flag) {
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.xptap_1").getFormattedText());
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.xptap_2").getFormattedText());
			}
		};

		public static final Block WITHER_MUFFLER = new BlockWitherMuffler();
		public static final BlockItem WITHER_MUFFLER_ITEM = new BlockItem(WITHER_MUFFLER) {
			@OnlyIn(Dist.CLIENT)
			@Override
			public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flag) {
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.withermuffler_1").getFormattedText());
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.withermuffler_2").getFormattedText());
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.withermuffler_3").getFormattedText());
			}
		};

		public static final Block DRAGON_MUFFLER = new BlockDragonMuffler();
		public static final ItemBlock DRAGON_MUFFLER_ITEM = new ItemBlock(DRAGON_MUFFLER) {
			@OnlyIn(Dist.CLIENT)
			@Override
			public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flag) {
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.dragonmuffler_1").getFormattedText());
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.dragonmuffler_2").getFormattedText());
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.dragonmuffler_3").getFormattedText());
			}
		};

		public static final Block DARK_OAK_STONE= new BlockDarkOakStone();
		public static final BlockItem DARK_OAK_STONE_ITEM = new BlockItem(DARK_OAK_STONE) {
			@OnlyIn(Dist.CLIENT)
			@Override
			public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flag) {
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.darkoakstone").getFormattedText());
			}
		};

		public static final Block ENTITY_CONVEYOR = new BlockEntityConveyor();
		public static final BlockItem ENTITY_CONVEYOR_ITEM = new BlockItem(ENTITY_CONVEYOR) {
			@OnlyIn(Dist.CLIENT)
			@Override
			public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flag) {
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.entityconveyor_1").getFormattedText());
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.entityconveyor_2").getFormattedText());
			}
		};

		public static final Block ENDER_INHIBITOR_ON = new BlockEnderInhibitorOn().setCreativeTab(MobGrindingUtils.TAB);
		public static final BlockItem ENDER_INHIBITOR_ON_ITEM = new BlockItem(ENDER_INHIBITOR_ON) {
			@OnlyIn(Dist.CLIENT)
			@Override
			public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flag) {
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.enderinhibitor_1").getFormattedText());
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.enderinhibitor_2").getFormattedText());
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.enderinhibitor_3").getFormattedText());
			}
		};

		public static final Block ENDER_INHIBITOR_OFF = new BlockEnderInhibitorOff();
		public static final BlockItem ENDER_INHIBITOR_OFF_ITEM = new BlockItem(ENDER_INHIBITOR_OFF) {
			@OnlyIn(Dist.CLIENT)
			@Override
			public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flag) {
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.enderinhibitor_1").getFormattedText());
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.enderinhibitor_2").getFormattedText());
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.enderinhibitor_3").getFormattedText());
			}
		};

	public static void init() {
		try {
			for (Field field : ModBlocks.class.getDeclaredFields()) {
				Object obj = field.get(null);
				if (obj instanceof Block) {
					Block block = (Block) obj;
					String name = field.getName().toLowerCase(Locale.ENGLISH);
					registerBlock(name, block);
				}
				if (obj instanceof BlockItem) {
					BlockItem blockItem = (BlockItem) obj;
					String name = field.getName().toLowerCase(Locale.ENGLISH);
					registerItemBlock(name, blockItem);
				}
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public static void registerBlock(String name, Block block) {
		BLOCKS.add(block);
		block.setRegistryName(Reference.MOD_ID, name).setTranslationKey(Reference.MOD_ID + "." + name);
	}

	public static void registerItemBlock(String name, BlockItem item) {
		String[] newName = name.split("_item");
		ITEM_BLOCKS.add(item);
		item.setRegistryName(Reference.MOD_ID, newName[0]).setTranslationKey(Reference.MOD_ID + "." + newName[0]);
	}

	@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
	public static class RegistrationHandlerBlocks {

		@SubscribeEvent
		public static void registerBlocks(final RegistryEvent.Register<Block> event) {
			init();
			final IForgeRegistry<Block> registry = event.getRegistry();
			for (Block block : BLOCKS) {
				registry.register(block);
			}
		}

		@SubscribeEvent
		public static void registerItemBlocks(final RegistryEvent.Register<Item> event) {
			final IForgeRegistry<Item> registry = event.getRegistry();
				for (BlockItem item : ITEM_BLOCKS) {
				registry.register(item);
			}
		}

		@SideOnly(Side.CLIENT)
		@SubscribeEvent
		public static void registerModels(ModelRegistryEvent event) {
			for (BlockItem item : ITEM_BLOCKS) {
				ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName().toString(), "inventory"));
			}
		}
	}
}