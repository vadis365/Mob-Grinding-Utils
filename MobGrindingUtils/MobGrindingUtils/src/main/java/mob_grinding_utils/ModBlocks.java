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
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks {
	
	public static List<Block> BLOCKS = new LinkedList<Block>();
	public static List<BlockItem> ITEM_BLOCKS = new ArrayList<BlockItem>();

	public static Block FAN;
	public static BlockItem FAN_ITEM;

	public static Block SAW;
	public static BlockItem SAW_ITEM;

	public static Block ABSORPTION_HOPPER;
	public static BlockItem ABSORPTION_HOPPER_ITEM;

	public static Block SPIKES;
	public static BlockItem SPIKES_ITEM;

	public static Block TANK;
	public static BlockItem TANK_ITEM;

	public static Block TANK_SINK;
	public static BlockItem TANK_SINK_ITEM;

	public static Block XP_TAP;
	public static BlockItem XP_TAP_ITEM;

	public static Block WITHER_MUFFLER;
	public static BlockItem WITHER_MUFFLER_ITEM;

	public static Block DRAGON_MUFFLER;
	public static BlockItem DRAGON_MUFFLER_ITEM;

	public static Block DARK_OAK_STONE;
	public static BlockItem DARK_OAK_STONE_ITEM;

	public static Block ENTITY_CONVEYOR;
	public static BlockItem ENTITY_CONVEYOR_ITEM;

	public static Block ENDER_INHIBITOR_ON;
	public static BlockItem ENDER_INHIBITOR_ON_ITEM;

	public static Block ENDER_INHIBITOR_OFF;
	public static BlockItem ENDER_INHIBITOR_OFF_ITEM;

	public static void init() {
		FAN = new BlockFan(Block.Properties.create(Material.IRON, MaterialColor.STONE).hardnessAndResistance(10.0F, 2000.0F).sound(SoundType.METAL));
		FAN_ITEM = new BlockItem(FAN, new Item.Properties().group(MobGrindingUtils.TAB)) {
			@Override
			@OnlyIn(Dist.CLIENT)
			   public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
					list.add(new TranslationTextComponent("tooltip.fan_1").mergeStyle(TextFormatting.YELLOW));
					list.add(new TranslationTextComponent("tooltip.fan_2").mergeStyle(TextFormatting.YELLOW));
					list.add(new TranslationTextComponent("tooltip.fan_3").mergeStyle(TextFormatting.YELLOW));
				}
			};

			SAW = new BlockSaw(Block.Properties.create(Material.IRON, MaterialColor.STONE).hardnessAndResistance(10.0F, 2000.0F).sound(SoundType.METAL));
			SAW_ITEM = new BlockItem(SAW, new Item.Properties().group(MobGrindingUtils.TAB)) {
				@Override
				@OnlyIn(Dist.CLIENT)
				   public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
					list.add(new TranslationTextComponent("tooltip.saw_1").mergeStyle(TextFormatting.YELLOW));
					list.add(new TranslationTextComponent("tooltip.saw_2").mergeStyle(TextFormatting.YELLOW));
					list.add(new TranslationTextComponent("tooltip.saw_3").mergeStyle(TextFormatting.YELLOW));
				}
			};

			ABSORPTION_HOPPER = new BlockAbsorptionHopper(Block.Properties.create(Material.IRON, MaterialColor.STONE).hardnessAndResistance(10.0F, 2000.0F).sound(SoundType.METAL).harvestLevel(0).harvestTool(ToolType.PICKAXE));
			ABSORPTION_HOPPER_ITEM = new BlockItem(ABSORPTION_HOPPER, new Item.Properties().group(MobGrindingUtils.TAB)) {
				@Override
				@OnlyIn(Dist.CLIENT)
				   public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
					list.add(new TranslationTextComponent("tooltip.hopper_1").mergeStyle(TextFormatting.YELLOW));
					list.add(new TranslationTextComponent("tooltip.hopper_2").mergeStyle(TextFormatting.YELLOW));
					list.add(new TranslationTextComponent("tooltip.hopper_3").mergeStyle(TextFormatting.YELLOW));
					list.add(new TranslationTextComponent("tooltip.hopper_4").mergeStyle(TextFormatting.YELLOW));
					list.add(new TranslationTextComponent("tooltip.hopper_5").mergeStyle(TextFormatting.YELLOW));
					list.add(new TranslationTextComponent("tooltip.hopper_6").mergeStyle(TextFormatting.YELLOW));
				}
			};
			
			SPIKES = new BlockSpikes(Block.Properties.create(Material.IRON, MaterialColor.STONE).hardnessAndResistance(5.0F, 2000.0F).sound(SoundType.METAL));
			SPIKES_ITEM = new BlockItem(SPIKES, new Item.Properties().group(MobGrindingUtils.TAB)) {
				@Override
				@OnlyIn(Dist.CLIENT)
				   public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
					list.add(new TranslationTextComponent("tooltip.spikes_1").mergeStyle(TextFormatting.YELLOW));
					list.add(new TranslationTextComponent("tooltip.spikes_2").mergeStyle(TextFormatting.YELLOW));
				}
			};

			TANK = new BlockTank(Block.Properties.create(Material.GLASS, MaterialColor.QUARTZ).hardnessAndResistance(1.0F, 2000.0F).sound(SoundType.GLASS));
			TANK_ITEM = new BlockItem(TANK, new Item.Properties().group(MobGrindingUtils.TAB)) {
				@Override
				@OnlyIn(Dist.CLIENT)
				   public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
					if(stack.hasTag() && !stack.getTag().contains("Empty")) {
						FluidStack fluid = FluidStack.loadFluidStackFromNBT(stack.getTag());
						if(fluid !=null) {
							list.add(new TranslationTextComponent("Contains: "+ fluid.getFluid().getRegistryName().toString()).mergeStyle(TextFormatting.GREEN));
							list.add(new TranslationTextComponent(""+ fluid.getAmount() +"Mb/32000Mb").mergeStyle(TextFormatting.BLUE));
						}
					}
				}
			};

			TANK_SINK = new BlockTankSink(Block.Properties.create(Material.GLASS, MaterialColor.QUARTZ).hardnessAndResistance(1.0F, 2000.0F).sound(SoundType.GLASS));
			TANK_SINK_ITEM = new BlockItem(TANK_SINK, new Item.Properties().group(MobGrindingUtils.TAB)) {
				@Override
				@OnlyIn(Dist.CLIENT)
				   public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
					if(stack.hasTag() && !stack.getTag().contains("Empty")) {
						FluidStack fluid = FluidStack.loadFluidStackFromNBT(stack.getTag());
						if(fluid !=null) {
							list.add(new TranslationTextComponent("Contains: "+ fluid.getFluid().getRegistryName().toString()).mergeStyle(TextFormatting.GREEN));
							list.add(new TranslationTextComponent(""+ fluid.getAmount() +"Mb/32000Mb").mergeStyle(TextFormatting.BLUE));
						}
					}
				}
			};

			XP_TAP = new BlockXPTap(Block.Properties.create(Material.REDSTONE_LIGHT, MaterialColor.STONE).hardnessAndResistance(1.0F, 2000.0F).sound(SoundType.METAL));
			XP_TAP_ITEM = new BlockItem(XP_TAP, new Item.Properties().group(MobGrindingUtils.TAB)) {
				@Override
				@OnlyIn(Dist.CLIENT)
				   public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
					list.add(new TranslationTextComponent("tooltip.xptap_1").mergeStyle(TextFormatting.YELLOW));
					list.add(new TranslationTextComponent("tooltip.xptap_2").mergeStyle(TextFormatting.YELLOW));
				}
			};

			WITHER_MUFFLER = new BlockWitherMuffler(Block.Properties.create(Material.WOOL, MaterialColor.STONE).hardnessAndResistance(0.5F, 2000F).sound(SoundType.CLOTH));
			WITHER_MUFFLER_ITEM = new BlockItem(WITHER_MUFFLER, new Item.Properties().group(MobGrindingUtils.TAB)) {
				@Override
				@OnlyIn(Dist.CLIENT)
				   public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
					list.add(new TranslationTextComponent("tooltip.withermuffler_1").mergeStyle(TextFormatting.YELLOW));
					list.add(new TranslationTextComponent("tooltip.withermuffler_2").mergeStyle(TextFormatting.YELLOW));
					list.add(new TranslationTextComponent("tooltip.withermuffler_3").mergeStyle(TextFormatting.YELLOW));
				}
			};

			DRAGON_MUFFLER = new BlockDragonMuffler(Block.Properties.create(Material.WOOL, MaterialColor.STONE).hardnessAndResistance(0.5F, 2000F).sound(SoundType.CLOTH));
			DRAGON_MUFFLER_ITEM = new BlockItem(DRAGON_MUFFLER, new Item.Properties().group(MobGrindingUtils.TAB)) {
				@Override
				@OnlyIn(Dist.CLIENT)
				   public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
					list.add(new TranslationTextComponent("tooltip.dragonmuffler_1").mergeStyle(TextFormatting.YELLOW));
					list.add(new TranslationTextComponent("tooltip.dragonmuffler_2").mergeStyle(TextFormatting.YELLOW));
					list.add(new TranslationTextComponent("tooltip.dragonmuffler_3").mergeStyle(TextFormatting.YELLOW));
				}
			};

			DARK_OAK_STONE= new BlockDarkOakStone(Block.Properties.create(Material.ROCK, MaterialColor.STONE).hardnessAndResistance(1.5F, 10F).sound(SoundType.STONE).setLightLevel(null));
			DARK_OAK_STONE_ITEM = new BlockItem(DARK_OAK_STONE, new Item.Properties().group(MobGrindingUtils.TAB)) {
				@Override
				@OnlyIn(Dist.CLIENT)
				   public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
					list.add(new TranslationTextComponent("tooltip.darkoakstone").mergeStyle(TextFormatting.YELLOW));
				}
			};

			ENTITY_CONVEYOR = new BlockEntityConveyor(Block.Properties.create(Material.ROCK, MaterialColor.STONE).hardnessAndResistance(0.5F, 2000.0F).sound(SoundType.STONE));
			ENTITY_CONVEYOR_ITEM = new BlockItem(ENTITY_CONVEYOR, new Item.Properties().group(MobGrindingUtils.TAB)) {
				@Override
				@OnlyIn(Dist.CLIENT)
				   public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
					list.add(new TranslationTextComponent("tooltip.entityconveyor_1").mergeStyle(TextFormatting.YELLOW));
					list.add(new TranslationTextComponent("tooltip.entityconveyor_2").mergeStyle(TextFormatting.YELLOW));
				}
			};
	
			ENDER_INHIBITOR_ON = new BlockEnderInhibitorOn(Block.Properties.create(Material.REDSTONE_LIGHT, MaterialColor.STONE).hardnessAndResistance(0.2F, 2000F).sound(SoundType.METAL));
			ENDER_INHIBITOR_ON_ITEM = new BlockItem(ENDER_INHIBITOR_ON, new Item.Properties().group(MobGrindingUtils.TAB)) {
				@Override
				@OnlyIn(Dist.CLIENT)
				   public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
					list.add(new TranslationTextComponent("tooltip.enderinhibitor_1").mergeStyle(TextFormatting.YELLOW));
					list.add(new TranslationTextComponent("tooltip.enderinhibitor_2").mergeStyle(TextFormatting.YELLOW));
					list.add(new TranslationTextComponent("tooltip.enderinhibitor_3").mergeStyle(TextFormatting.YELLOW));
				}
			};

			ENDER_INHIBITOR_OFF = new BlockEnderInhibitorOff(Block.Properties.create(Material.REDSTONE_LIGHT, MaterialColor.STONE).hardnessAndResistance(0.2F, 2000F).sound(SoundType.METAL));
			ENDER_INHIBITOR_OFF_ITEM = new BlockItem(ENDER_INHIBITOR_OFF, new Item.Properties().group(MobGrindingUtils.TAB)) {
				@Override
				@OnlyIn(Dist.CLIENT)
				   public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
					list.add(new TranslationTextComponent("tooltip.enderinhibitor_1").mergeStyle(TextFormatting.YELLOW));
					list.add(new TranslationTextComponent("tooltip.enderinhibitor_2").mergeStyle(TextFormatting.YELLOW));
					list.add(new TranslationTextComponent("tooltip.enderinhibitor_3").mergeStyle(TextFormatting.YELLOW));
				}
			};
	}

	public static void initReg() {
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
					registerBlockItem(name, blockItem);
				}
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public static void registerBlock(String name, Block block) {
		BLOCKS.add(block);
		block.setRegistryName(Reference.MOD_ID, name);
	}

	public static void registerBlockItem(String name, BlockItem item) {
		String[] newName = name.split("_item");
		ITEM_BLOCKS.add(item);
		item.setRegistryName(Reference.MOD_ID, newName[0]);
	}

	@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistrationHandlerBlocks {

		@SubscribeEvent
		public static void registerBlocks(final RegistryEvent.Register<Block> event) {
			init();
			initReg();
			final IForgeRegistry<Block> registry = event.getRegistry();
			for (Block block : BLOCKS) {
				registry.register(block);
			}
		}

		@SubscribeEvent
		public static void registerBlockItems(final RegistryEvent.Register<Item> event) {
			final IForgeRegistry<Item> registry = event.getRegistry();
				for (BlockItem item : ITEM_BLOCKS) {
				registry.register(item);
			}
		}
/*
		@SideOnly(Side.CLIENT)
		@SubscribeEvent
		public static void registerModels(ModelRegistryEvent event) {
			for (BlockItem item : ITEM_BLOCKS) {
				ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName().toString(), "inventory"));
			}
		}*/
	}
}