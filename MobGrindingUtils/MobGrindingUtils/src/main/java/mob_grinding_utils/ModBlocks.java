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
import mob_grinding_utils.blocks.BlockDreadfulDirt;
import mob_grinding_utils.blocks.BlockEnderInhibitorOff;
import mob_grinding_utils.blocks.BlockEnderInhibitorOn;
import mob_grinding_utils.blocks.BlockEntityConveyor;
import mob_grinding_utils.blocks.BlockFan;
import mob_grinding_utils.blocks.BlockSaw;
import mob_grinding_utils.blocks.BlockSolidXP;
import mob_grinding_utils.blocks.BlockSpikes;
import mob_grinding_utils.blocks.BlockTank;
import mob_grinding_utils.blocks.BlockTankJumbo;
import mob_grinding_utils.blocks.BlockTankSink;
import mob_grinding_utils.blocks.BlockTintedGlass;
import mob_grinding_utils.blocks.BlockWitherMuffler;
import mob_grinding_utils.blocks.BlockXPSolidifier;
import mob_grinding_utils.blocks.BlockXPTap;
import mob_grinding_utils.client.render.TileSawStackItemRenderer;
import mob_grinding_utils.client.render.TileTankStackItemRenderer;
import mob_grinding_utils.client.render.TileXPSolidifierStackItemRenderer;
import mob_grinding_utils.itemblocks.BlockItemTank;
import mob_grinding_utils.itemblocks.BlockItemTankJumbo;
import mob_grinding_utils.itemblocks.BlockItemTankSink;
import mob_grinding_utils.tile.TileEntityAbsorptionHopper;
import mob_grinding_utils.tile.TileEntityFan;
import mob_grinding_utils.tile.TileEntityJumboTank;
import mob_grinding_utils.tile.TileEntitySaw;
import mob_grinding_utils.tile.TileEntitySinkTank;
import mob_grinding_utils.tile.TileEntityTank;
import mob_grinding_utils.tile.TileEntityXPSolidifier;
import mob_grinding_utils.tile.TileEntityXPTap;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityClassification;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks {
	public static List<Block> BLOCKS = new LinkedList<Block>();
	public static List<BlockItem> ITEM_BLOCKS = new ArrayList<BlockItem>();
	public static List<TileEntityType<?>> TILE_ENTITIES = new LinkedList<TileEntityType<?>>();
	
	public static Block FAN;
	public static BlockItem FAN_ITEM;
	public static TileEntityType<TileEntityFan> FAN_TILE;

	public static Block SAW;
	public static BlockItem SAW_ITEM;
	public static TileEntityType<TileEntitySaw> SAW_TILE;

	public static Block ABSORPTION_HOPPER;
	public static BlockItem ABSORPTION_HOPPER_ITEM;
	public static TileEntityType<TileEntityAbsorptionHopper> ABSORPTION_HOPPER_TILE;

	public static Block SPIKES;
	public static BlockItem SPIKES_ITEM;

	public static Block TANK;
	public static BlockItem TANK_ITEM;
	public static TileEntityType<TileEntityTank> TANK_TILE;

	public static Block TANK_SINK;
	public static BlockItem TANK_SINK_ITEM;
	public static TileEntityType<TileEntitySinkTank> TANK_SINK_TILE;

	public static Block XP_TAP;
	public static BlockItem XP_TAP_ITEM;
	public static TileEntityType<TileEntityXPTap> XP_TAP_TILE;

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
	
	public static Fluid FLUID_XP;
	
	public static Block TINTED_GLASS;
	public static BlockItem TINTED_GLASS_ITEM;

	public static Block JUMBO_TANK;
	public static BlockItem JUMBO_TANK_ITEM;
	public static TileEntityType<TileEntityJumboTank> JUMBO_TANK_TILE;

	public static Block XPSOLIDIFIER;
	public static BlockItem XPSOLIDIFIER_ITEM;
	public static TileEntityType<TileEntityXPSolidifier> XPSOLIDIFIER_TILE;

	public static Block DREADFUL_DIRT;
	public static BlockItem DREADFUL_DIRT_ITEM;
	public static Material MATERIAL_DREADFUL_DIRT;
	
	public static Block SOLID_XP_BLOCK;
	public static BlockItem SOLID_XP_BLOCK_ITEM;

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
			FAN_TILE = TileEntityType.Builder.create(TileEntityFan::new, FAN).build(null);

			SAW = new BlockSaw(Block.Properties.create(Material.IRON, MaterialColor.STONE).hardnessAndResistance(10.0F, 2000.0F).sound(SoundType.METAL).notSolid());
			SAW_ITEM = new BlockItem(SAW, new Item.Properties().group(MobGrindingUtils.TAB).setISTER(() ->  TileSawStackItemRenderer::new)) {
				@Override
				@OnlyIn(Dist.CLIENT)
				   public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
					list.add(new TranslationTextComponent("tooltip.saw_1").mergeStyle(TextFormatting.YELLOW));
					list.add(new TranslationTextComponent("tooltip.saw_2").mergeStyle(TextFormatting.YELLOW));
					list.add(new TranslationTextComponent("tooltip.saw_3").mergeStyle(TextFormatting.YELLOW));
				}
			};
			SAW_TILE = TileEntityType.Builder.create(TileEntitySaw::new, SAW).build(null);

			ABSORPTION_HOPPER = new BlockAbsorptionHopper(Block.Properties.create(Material.IRON, MaterialColor.STONE).hardnessAndResistance(10.0F, 2000.0F).sound(SoundType.METAL).harvestLevel(0).harvestTool(ToolType.PICKAXE).notSolid());
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
			ABSORPTION_HOPPER_TILE = TileEntityType.Builder.create(TileEntityAbsorptionHopper::new, ABSORPTION_HOPPER).build(null);

			SPIKES = new BlockSpikes(Block.Properties.create(Material.IRON, MaterialColor.STONE).hardnessAndResistance(5.0F, 2000.0F).sound(SoundType.METAL).notSolid());
			SPIKES_ITEM = new BlockItem(SPIKES, new Item.Properties().group(MobGrindingUtils.TAB)) {
				@Override
				@OnlyIn(Dist.CLIENT)
				   public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
					list.add(new TranslationTextComponent("tooltip.spikes_1").mergeStyle(TextFormatting.YELLOW));
					list.add(new TranslationTextComponent("tooltip.spikes_2").mergeStyle(TextFormatting.YELLOW));
				}
			};

			TANK = new BlockTank(Block.Properties.create(Material.GLASS, MaterialColor.QUARTZ).hardnessAndResistance(1.0F, 2000.0F).sound(SoundType.GLASS).notSolid());
			TANK_ITEM = new BlockItemTank(TANK, new Item.Properties().group(MobGrindingUtils.TAB).setISTER(() ->  TileTankStackItemRenderer::new));
			TANK_TILE = TileEntityType.Builder.create(TileEntityTank::new, TANK).build(null);

			TANK_SINK = new BlockTankSink(Block.Properties.create(Material.GLASS, MaterialColor.QUARTZ).hardnessAndResistance(1.0F, 2000.0F).sound(SoundType.GLASS).notSolid());
			TANK_SINK_ITEM = new BlockItemTankSink(TANK_SINK, new Item.Properties().group(MobGrindingUtils.TAB).setISTER(() ->  TileTankStackItemRenderer::new));
			TANK_SINK_TILE = TileEntityType.Builder.create(TileEntitySinkTank::new, TANK_SINK).build(null);

			XP_TAP = new BlockXPTap(Block.Properties.create(Material.REDSTONE_LIGHT, MaterialColor.STONE).hardnessAndResistance(1.0F, 2000.0F).sound(SoundType.METAL).notSolid());
			XP_TAP_ITEM = new BlockItem(XP_TAP, new Item.Properties().group(MobGrindingUtils.TAB)) {
				@Override
				@OnlyIn(Dist.CLIENT)
				   public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
					list.add(new TranslationTextComponent("tooltip.xptap_1").mergeStyle(TextFormatting.YELLOW));
					list.add(new TranslationTextComponent("tooltip.xptap_2").mergeStyle(TextFormatting.YELLOW));
				}
			};
			XP_TAP_TILE = TileEntityType.Builder.create(TileEntityXPTap::new, XP_TAP).build(null);

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

			DARK_OAK_STONE= new BlockDarkOakStone(Block.Properties.create(Material.ROCK, MaterialColor.STONE).hardnessAndResistance(1.5F, 10F).sound(SoundType.STONE).setLightLevel(bState -> 7));
			DARK_OAK_STONE_ITEM = new BlockItem(DARK_OAK_STONE, new Item.Properties().group(MobGrindingUtils.TAB)) {
				@Override
				@OnlyIn(Dist.CLIENT)
				   public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
					list.add(new TranslationTextComponent("tooltip.darkoakstone").mergeStyle(TextFormatting.YELLOW));
				}
			};

			ENTITY_CONVEYOR = new BlockEntityConveyor(Block.Properties.create(Material.ROCK, MaterialColor.STONE).hardnessAndResistance(0.5F, 2000.0F).sound(SoundType.STONE).setAllowsSpawn((state, reader, pos, entitytype) -> {return true;}));
			ENTITY_CONVEYOR_ITEM = new BlockItem(ENTITY_CONVEYOR, new Item.Properties().group(MobGrindingUtils.TAB)) {
				@Override
				@OnlyIn(Dist.CLIENT)
				   public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
					list.add(new TranslationTextComponent("tooltip.entityconveyor_1").mergeStyle(TextFormatting.YELLOW));
					list.add(new TranslationTextComponent("tooltip.entityconveyor_2").mergeStyle(TextFormatting.YELLOW));
				}
			};
	
			ENDER_INHIBITOR_ON = new BlockEnderInhibitorOn(Block.Properties.create(Material.REDSTONE_LIGHT, MaterialColor.STONE).hardnessAndResistance(0.2F, 2000F).sound(SoundType.METAL).notSolid());
			ENDER_INHIBITOR_ON_ITEM = new BlockItem(ENDER_INHIBITOR_ON, new Item.Properties().group(MobGrindingUtils.TAB)) {
				@Override
				@OnlyIn(Dist.CLIENT)
				   public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
					list.add(new TranslationTextComponent("tooltip.enderinhibitor_1").mergeStyle(TextFormatting.YELLOW));
					list.add(new TranslationTextComponent("tooltip.enderinhibitor_2").mergeStyle(TextFormatting.YELLOW));
					list.add(new TranslationTextComponent("tooltip.enderinhibitor_3").mergeStyle(TextFormatting.YELLOW));
				}
			};

			ENDER_INHIBITOR_OFF = new BlockEnderInhibitorOff(Block.Properties.create(Material.REDSTONE_LIGHT, MaterialColor.STONE).hardnessAndResistance(0.2F, 2000F).sound(SoundType.METAL).notSolid());
			ENDER_INHIBITOR_OFF_ITEM = new BlockItem(ENDER_INHIBITOR_OFF, new Item.Properties().group(MobGrindingUtils.TAB)) {
				@Override
				@OnlyIn(Dist.CLIENT)
				   public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
					list.add(new TranslationTextComponent("tooltip.enderinhibitor_1").mergeStyle(TextFormatting.YELLOW));
					list.add(new TranslationTextComponent("tooltip.enderinhibitor_2").mergeStyle(TextFormatting.YELLOW));
					list.add(new TranslationTextComponent("tooltip.enderinhibitor_3").mergeStyle(TextFormatting.YELLOW));
				}
			};

			FLUID_XP = new ForgeFlowingFluid.Source(
					new ForgeFlowingFluid.Properties(() -> FLUID_XP, () -> FLUID_XP,
							FluidAttributes.builder(new ResourceLocation(Reference.MOD_ID, "fluids/fluid_xp"), new ResourceLocation(Reference.MOD_ID, "fluids/fluid_xp"))
									.luminosity(10)
									.density(800)
									.viscosity(1500)
									.translationKey("mob_grinding_utils.fluid_xp")
									.sound(SoundEvents.ENTITY_PLAYER_LEVELUP, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP)
					).bucket(() -> ModItems.FLUID_XP_BUCKET))
					.setRegistryName(Reference.MOD_ID, "fluid_xp");
			
			TINTED_GLASS = new BlockTintedGlass(Block.Properties.create(Material.GLASS, MaterialColor.BLACK).hardnessAndResistance(1.0F, 2000.0F).sound(SoundType.GLASS).notSolid());
			TINTED_GLASS_ITEM = new BlockItem(TINTED_GLASS, new Item.Properties().group(MobGrindingUtils.TAB)){
				@Override
				@OnlyIn(Dist.CLIENT)
				   public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
					list.add(new TranslationTextComponent("tooltip.tinted_glass_1").mergeStyle(TextFormatting.YELLOW));
					list.add(new TranslationTextComponent("tooltip.tinted_glass_2").mergeStyle(TextFormatting.YELLOW));
				}
			};

			JUMBO_TANK = new BlockTankJumbo(Block.Properties.create(Material.IRON, MaterialColor.GRAY).hardnessAndResistance(1.0F, 2000.0F).sound(SoundType.METAL).notSolid());
			JUMBO_TANK_ITEM = new BlockItemTankJumbo(JUMBO_TANK, new Item.Properties().group(MobGrindingUtils.TAB).setISTER(() ->  TileTankStackItemRenderer::new));
			JUMBO_TANK_TILE = TileEntityType.Builder.create(TileEntityJumboTank::new, JUMBO_TANK).build(null);

			XPSOLIDIFIER = new BlockXPSolidifier(Block.Properties.create(Material.IRON, MaterialColor.GRAY).hardnessAndResistance(1.0F, 2000.0F).sound(SoundType.METAL).notSolid());
			XPSOLIDIFIER_ITEM = new BlockItem(XPSOLIDIFIER, new Item.Properties().group(MobGrindingUtils.TAB).setISTER(() ->  TileXPSolidifierStackItemRenderer::new));
			XPSOLIDIFIER_TILE = TileEntityType.Builder.create(TileEntityXPSolidifier::new, XPSOLIDIFIER).build(null);
			
			MATERIAL_DREADFUL_DIRT = new Material(MaterialColor.DIRT, false, true, false, true, true, false, null);
			DREADFUL_DIRT = new BlockDreadfulDirt(Block.Properties.create(MATERIAL_DREADFUL_DIRT, MaterialColor.PURPLE).hardnessAndResistance(1.0F, 2000.0F).sound(SoundType.GROUND).tickRandomly().setAllowsSpawn((state, reader, pos, entitytype) -> {return entitytype.getClassification() == EntityClassification.MONSTER;}));
			DREADFUL_DIRT_ITEM = new BlockItem(DREADFUL_DIRT, new Item.Properties().group(MobGrindingUtils.TAB)) {
				@Override
				@OnlyIn(Dist.CLIENT)
				   public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
					list.add(new TranslationTextComponent("tooltip.dreadful_dirt_1").mergeStyle(TextFormatting.YELLOW));
					list.add(new TranslationTextComponent("tooltip.dreadful_dirt_2").mergeStyle(TextFormatting.YELLOW));
				}
			};

			SOLID_XP_BLOCK = new BlockSolidXP(Block.Properties.create(Material.CLAY, MaterialColor.GRASS).slipperiness(0.8F).sound(ModSounds.SOLID_XP_BLOCK).notSolid().hardnessAndResistance(1.5F, 10F));
			SOLID_XP_BLOCK_ITEM = new BlockItem(SOLID_XP_BLOCK, new Item.Properties().group(MobGrindingUtils.TAB)) {
				@Override
				@OnlyIn(Dist.CLIENT)
				   public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
					list.add(new TranslationTextComponent("tooltip.solid_xp_block").mergeStyle(TextFormatting.YELLOW));
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
				if (obj instanceof TileEntityType) {
					TileEntityType<?> tileEntity = (TileEntityType<?>) obj;
					String name = field.getName().toLowerCase(Locale.ENGLISH);
					registerTileEntity(name, tileEntity);
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

	public static void registerTileEntity(String name, TileEntityType<?> tileEntity) {
		String[] newName = name.split("_tile");
		TILE_ENTITIES.add(tileEntity);
		tileEntity.setRegistryName(Reference.MOD_ID, newName[0]);
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

		@SubscribeEvent
		  public static void registerTileEntities(RegistryEvent.Register<TileEntityType<?> > event) {
			IForgeRegistry<TileEntityType<?>> registry = event.getRegistry();
				for (TileEntityType<?> tileEntity : TILE_ENTITIES) {
				registry.register(tileEntity);
			}
		}

		@SubscribeEvent
		public static void registerFluids(final RegistryEvent.Register<Fluid> evt) {
			final IForgeRegistry<Fluid> registry = evt.getRegistry();
			registry.register(FLUID_XP);
		}
	}
}