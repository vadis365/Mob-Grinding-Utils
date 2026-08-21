package mob_grinding_utils;


import com.google.common.collect.ImmutableSet;
import mob_grinding_utils.blocks.*;
import mob_grinding_utils.itemblocks.BlockItemTank;
import mob_grinding_utils.itemblocks.MGUBlockItem;
import mob_grinding_utils.tile.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Set;

public class ModBlocks {
	public static DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Reference.MOD_ID);
	public static DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(BuiltInRegistries.FLUID, Reference.MOD_ID);
	public static DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, Reference.MOD_ID);
	public static DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Reference.MOD_ID);

	public static MGUBlockReg<BlockFan, MGUBlockItem, TileEntityFan> FAN = new MGUBlockReg<>("fan",
		properties -> new BlockFan(properties.mapColor(MapColor.COLOR_GRAY).strength(10.0F, 2000.0F).sound(SoundType.METAL)),
		MGUBlockItem::new, TileEntityFan::new);

	public static MGUBlockReg<BlockSaw, MGUBlockItem, TileEntitySaw> SAW = new MGUBlockReg<>("saw",
		properties -> new BlockSaw(properties.mapColor(MapColor.COLOR_GRAY).strength(10.0F, 2000.0F).sound(SoundType.METAL).noOcclusion()),
		MGUBlockItem::new, TileEntitySaw::new);

	public static MGUBlockReg<BlockAbsorptionHopper, MGUBlockItem, TileEntityAbsorptionHopper> ABSORPTION_HOPPER = new MGUBlockReg<>("absorption_hopper",
		properties -> new BlockAbsorptionHopper(properties.mapColor(MapColor.COLOR_BLACK).forceSolidOn().strength(10.0F, 2000.0F).sound(SoundType.METAL).noOcclusion()),
		MGUBlockItem::new, TileEntityAbsorptionHopper::new);

	public static MGUBlockReg<BlockSpikes, MGUBlockItem, ?> SPIKES = new MGUBlockReg<>("spikes",
		properties -> new BlockSpikes(properties.mapColor(MapColor.COLOR_GRAY).strength(5.0F, 2000.0F).sound(SoundType.METAL).noOcclusion()),
		MGUBlockItem::new);

	public static MGUBlockReg<BlockTank, BlockItemTank, TileEntityTank> TANK = new MGUBlockReg<>("tank",
		properties -> new BlockTank(properties.mapColor(MapColor.COLOR_GRAY).strength(1.0F, 2000.0F).sound(SoundType.GLASS).noOcclusion()),
		(b, properties) -> new BlockItemTank(b, 32000, properties), TileEntityTank::new);

	public static MGUBlockReg<BlockTankSink, BlockItemTank, TileEntitySinkTank> TANK_SINK = new MGUBlockReg<>("tank_sink",
		properties -> new BlockTankSink(properties.mapColor(MapColor.COLOR_GRAY).strength(1.0F, 2000.0F).sound(SoundType.GLASS).noOcclusion()),
		(b, properties) -> new BlockItemTank(b, 32000, properties), TileEntitySinkTank::new);

	public static MGUBlockReg<BlockXPTap, MGUBlockItem, TileEntityXPTap> XP_TAP = new MGUBlockReg<>("xp_tap",
		properties -> new BlockXPTap(properties.mapColor(MapColor.NONE).strength(1.0F, 2000.0F).forceSolidOn().sound(SoundType.METAL).noOcclusion()),
		MGUBlockItem::new, TileEntityXPTap::new);

	public static MGUBlockReg<BlockWitherMuffler, MGUBlockItem, ?> WITHER_MUFFLER = new MGUBlockReg<>("wither_muffler",
		properties -> new BlockWitherMuffler(properties.mapColor(MapColor.COLOR_BLACK).strength(0.5F, 2000F).sound(SoundType.WOOL)),
		MGUBlockItem::new);

	public static MGUBlockReg<BlockDragonMuffler, MGUBlockItem, ?> DRAGON_MUFFLER = new MGUBlockReg<>("dragon_muffler",
		properties -> new BlockDragonMuffler(properties.mapColor(MapColor.COLOR_BLACK).strength(0.5F, 2000F).sound(SoundType.WOOL)),
		MGUBlockItem::new);

	public static MGUBlockReg<BlockDarkOakStone, MGUBlockItem, ?> DARK_OAK_STONE = new MGUBlockReg<>("dark_oak_stone",
		properties -> new BlockDarkOakStone(properties.mapColor(MapColor.COLOR_BROWN).strength(1.5F, 10F).sound(SoundType.STONE).lightLevel(bState -> 7)),
		MGUBlockItem::new);

	public static MGUBlockReg<BlockEntityConveyor, MGUBlockItem, ?> ENTITY_CONVEYOR = new MGUBlockReg<>("entity_conveyor",
		properties -> new BlockEntityConveyor(properties.mapColor(MapColor.COLOR_GRAY).strength(0.5F, 2000.0F).sound(SoundType.STONE).isValidSpawn((state, reader, pos, entitytype) -> true)),
		MGUBlockItem::new);

	public static MGUBlockReg<BlockEnderInhibitorOn, MGUBlockItem, ?> ENDER_INHIBITOR_ON = new MGUBlockReg<>("ender_inhibitor_on",
		properties -> new BlockEnderInhibitorOn(properties.mapColor(MapColor.COLOR_GRAY).forceSolidOn().strength(0.2F, 2000F).sound(SoundType.METAL).noOcclusion()),
		MGUBlockItem::new);

	public static MGUBlockReg<BlockEnderInhibitorOff, MGUBlockItem, ?> ENDER_INHIBITOR_OFF = new MGUBlockReg<>("ender_inhibitor_off",
		properties -> new BlockEnderInhibitorOff(properties.mapColor(MapColor.COLOR_GRAY).forceSolidOn().strength(0.2F, 2000F).sound(SoundType.METAL).noOcclusion()),
		MGUBlockItem::new);

	public static MGUBlockReg<BlockTintedGlass, MGUBlockItem, ?> TINTED_GLASS = new MGUBlockReg<>("tinted_glass",
		properties -> new BlockTintedGlass(properties.mapColor(MapColor.COLOR_BLACK).strength(1.0F, 2000.0F).sound(SoundType.GLASS).noOcclusion()),
		MGUBlockItem::new);

	public static MGUBlockReg<BlockTankJumbo, BlockItemTank, TileEntityJumboTank> JUMBO_TANK = new MGUBlockReg<>("jumbo_tank",
		properties -> new BlockTankJumbo(properties.mapColor(MapColor.COLOR_GRAY).strength(1.0F, 2000.0F).sound(SoundType.METAL).noOcclusion()),
		(b, properties) -> new BlockItemTank(b, 1024000, properties), TileEntityJumboTank::new);

	public static MGUBlockReg<BlockXPSolidifier, MGUBlockItem, TileEntityXPSolidifier> XPSOLIDIFIER = new MGUBlockReg<>("xpsolidifier",
		properties -> new BlockXPSolidifier(properties.mapColor(MapColor.COLOR_GRAY).strength(1.0F, 2000.0F).sound(SoundType.METAL).noOcclusion()),
		MGUBlockItem::new, TileEntityXPSolidifier::new);

	//public static Material MATERIAL_DREADFUL_DIRT = new Material(MaterialColor.DIRT, false, true, false, true, true, false, PushReaction.NORMAL);
	public static MGUBlockReg<BlockDreadfulDirt, MGUBlockItem, ?> DREADFUL_DIRT = new MGUBlockReg<>("dreadful_dirt",
		properties -> new BlockDreadfulDirt(properties.mapColor(MapColor.COLOR_PURPLE).strength(1.0F, 2000.0F).sound(SoundType.GRAVEL).randomTicks()
				.isValidSpawn((state, level, pos, entitytype) -> entitytype.getCategory() == MobCategory.CREATURE)
		),
		MGUBlockItem::new);

	public static MGUBlockReg<BlockSolidXP, MGUBlockItem, ?> SOLID_XP_BLOCK = new MGUBlockReg<>("solid_xp_block",
		properties -> new BlockSolidXP(properties.mapColor(MapColor.COLOR_GREEN).friction(0.8F).sound(ModSounds.SOLID_XP_BLOCK).noOcclusion().strength(1.5F, 10F)),
		MGUBlockItem::new);

	public static MGUBlockReg<BlockDelightfulDirt, MGUBlockItem, ?> DELIGHTFUL_DIRT = new MGUBlockReg<>("delightful_dirt",
		properties -> new BlockDelightfulDirt(properties.mapColor(MapColor.COLOR_LIGHT_GREEN).strength(1.0F, 2000.0F).sound(SoundType.GRAVEL).randomTicks()
				.isValidSpawn((state, level, pos, entitytype) -> entitytype.getCategory() == MobCategory.MONSTER)
		),
		MGUBlockItem::new);

	public static MGUBlockReg<BlockEntitySpawner, MGUBlockItem, TileEntityMGUSpawner> ENTITY_SPAWNER = new MGUBlockReg<>("entity_spawner",
		properties -> new BlockEntitySpawner(properties.mapColor(MapColor.COLOR_GRAY).strength(10.0F, 2000.0F).sound(SoundType.METAL).noOcclusion().randomTicks()),
		MGUBlockItem::new, TileEntityMGUSpawner::new);

	public static DeferredHolder<FluidType, FluidType> XPTYPE = FLUID_TYPES.register("fluid_xp", () -> new FluidType(FluidType.Properties.create()
			.temperature(300)
			.lightLevel(10)
			.viscosity(1500)
			.density(800)
			.canConvertToSource(false)
			.canDrown(false)
			.canSwim(true)
			.descriptionId("mob_grinding_utils.fluid_xp")
			.sound(SoundActions.BUCKET_EMPTY, SoundEvents.EXPERIENCE_ORB_PICKUP)
			.sound(SoundActions.BUCKET_FILL, SoundEvents.PLAYER_LEVELUP))
			{
				@Override
				public ItemStack getBucket(FluidStack stack) {
					return new ItemStack(ModItems.FLUID_XP_BUCKET.get());
				}
			});
	public static DeferredHolder<Fluid, BaseFlowingFluid> FLUID_XP = FLUIDS.register("fluid_xp",
		() -> new BaseFlowingFluid.Source(ModBlocks.xp_properties) );
	public static DeferredHolder<Fluid, BaseFlowingFluid> FLUID_XP_FLOWING = FLUIDS.register("fluid_xp_flowing",
		() -> new BaseFlowingFluid.Flowing(ModBlocks.xp_properties) );
	public static DeferredBlock<MGUFlowingFluidBlock> FLUID_XP_BLOCK = BLOCKS.registerBlock("fluid_xp",
		properties -> new MGUFlowingFluidBlock(FLUID_XP, properties),
		() -> Block.Properties.of().liquid().noCollision().replaceable().strength(100.0F).pushReaction(PushReaction.DESTROY).noLootTable());

	private static final BaseFlowingFluid.Properties xp_properties = new BaseFlowingFluid.Properties(() -> XPTYPE.get(), () -> FLUID_XP.get(), () -> FLUID_XP_FLOWING.get())
			.block(() -> FLUID_XP_BLOCK.get())
			.bucket(() -> ModItems.FLUID_XP_BUCKET.get());

	public static final Set<MGUBlockReg<?,?,?>> TAB_ORDER = ImmutableSet.of(
		FAN, SAW, SPIKES, ABSORPTION_HOPPER, TANK, TANK_SINK, JUMBO_TANK,
		XP_TAP, WITHER_MUFFLER, DRAGON_MUFFLER, DARK_OAK_STONE, ENTITY_CONVEYOR, ENTITY_SPAWNER,
		ENDER_INHIBITOR_ON, ENDER_INHIBITOR_OFF, TINTED_GLASS, DREADFUL_DIRT, DELIGHTFUL_DIRT,
		XPSOLIDIFIER, SOLID_XP_BLOCK
	);

	public static void init(IEventBus evt) {
		BLOCKS.register(evt);
		TILE_ENTITIES.register(evt);
		FLUIDS.register(evt);
		FLUID_TYPES.register(evt);
	}
}
