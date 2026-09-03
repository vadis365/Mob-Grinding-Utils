package mob_grinding_utils;


import com.google.common.collect.ImmutableSet;
import mob_grinding_utils.blocks.*;
import mob_grinding_utils.itemblocks.BlockItemTank;
import mob_grinding_utils.itemblocks.MGUBlockItem;
import mob_grinding_utils.BlockEntities.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;
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

    public static MGUBlockReg<BlockFan, MGUBlockItem, BlockEntityFan> FAN = new MGUBlockReg<>("fan",
        BlockFan::new,
        fan -> new MGUBlockItem(fan, new Item.Properties().useBlockDescriptionPrefix()),
        BlockEntityFan::new);

    public static MGUBlockReg<BlockSaw, MGUBlockItem, BlockEntitySaw> SAW = new MGUBlockReg<>("saw",
        BlockSaw::new,
        saw -> new MGUBlockItem(saw, new Item.Properties().useBlockDescriptionPrefix()),
        BlockEntitySaw::new);

    public static MGUBlockReg<BlockAbsorptionHopper, MGUBlockItem, BlockEntityAbsorptionHopper> ABSORPTION_HOPPER = new MGUBlockReg<>("absorption_hopper",
        BlockAbsorptionHopper::new,
        absorptionHopper -> new MGUBlockItem(absorptionHopper, new Item.Properties().useBlockDescriptionPrefix()),
        BlockEntityAbsorptionHopper::new);

    public static MGUBlockReg<BlockSpikes, MGUBlockItem, ?> SPIKES = new MGUBlockReg<>("spikes",
        BlockSpikes::new,
        spikes -> new MGUBlockItem(spikes, new Item.Properties().useBlockDescriptionPrefix()));

    public static MGUBlockReg<BlockTank, BlockItemTank, BlockEntityTank> TANK = new MGUBlockReg<>("tank",
        BlockTank::new,
        tank -> new BlockItemTank(tank, 32000,new Item.Properties().useBlockDescriptionPrefix()),
        BlockEntityTank::new);

    public static MGUBlockReg<BlockTankSink, BlockItemTank, BlockEntitySinkTank> TANK_SINK = new MGUBlockReg<>("tank_sink",
        BlockTankSink::new,
        tankSink -> new BlockItemTank(tankSink, 32000, new Item.Properties().useBlockDescriptionPrefix()),
        BlockEntitySinkTank::new);

    public static MGUBlockReg<BlockXPTap, MGUBlockItem, BlockEntityXPTap> XP_TAP = new MGUBlockReg<>("xp_tap",
        BlockXPTap::new,
        xpTap -> new MGUBlockItem(xpTap, new Item.Properties().useBlockDescriptionPrefix()),
        BlockEntityXPTap::new);

    public static MGUBlockReg<BlockWitherMuffler, MGUBlockItem, ?> WITHER_MUFFLER = new MGUBlockReg<>("wither_muffler",
        BlockWitherMuffler::new,
        witherMuffler -> new MGUBlockItem(witherMuffler, new Item.Properties().useBlockDescriptionPrefix()));

    public static MGUBlockReg<BlockDragonMuffler, MGUBlockItem, ?> DRAGON_MUFFLER = new MGUBlockReg<>("dragon_muffler",
        BlockDragonMuffler::new,
        dragonMuffler -> new MGUBlockItem(dragonMuffler, new Item.Properties().useBlockDescriptionPrefix()));

    public static MGUBlockReg<BlockDarkOakStone, MGUBlockItem, ?> DARK_OAK_STONE = new MGUBlockReg<>("dark_oak_stone",
        BlockDarkOakStone::new,
        darkOakStone -> new MGUBlockItem(darkOakStone, new Item.Properties().useBlockDescriptionPrefix()));

    public static MGUBlockReg<BlockEntityConveyor, MGUBlockItem, ?> ENTITY_CONVEYOR = new MGUBlockReg<>("entity_conveyor",
        BlockEntityConveyor::new,
        entityConveyor -> new MGUBlockItem(entityConveyor, new Item.Properties().useBlockDescriptionPrefix()));

    public static MGUBlockReg<BlockEnderInhibitorOn, MGUBlockItem, ?> ENDER_INHIBITOR_ON = new MGUBlockReg<>("ender_inhibitor_on",
        BlockEnderInhibitorOn::new,
        enderInhibitorOn -> new MGUBlockItem(enderInhibitorOn, new Item.Properties().useBlockDescriptionPrefix()));

    public static MGUBlockReg<BlockEnderInhibitorOff, MGUBlockItem, ?> ENDER_INHIBITOR_OFF = new MGUBlockReg<>("ender_inhibitor_off",
        BlockEnderInhibitorOff::new,
        enderInhibitorOff -> new MGUBlockItem(enderInhibitorOff, new Item.Properties().useBlockDescriptionPrefix()));

    public static MGUBlockReg<BlockTintedGlass, MGUBlockItem, ?> TINTED_GLASS = new MGUBlockReg<>("tinted_glass",
        BlockTintedGlass::new,
        tintedGlass -> new MGUBlockItem(tintedGlass, new Item.Properties().useBlockDescriptionPrefix()));

    public static MGUBlockReg<BlockTankJumbo, BlockItemTank, BlockEntityJumboTank> JUMBO_TANK = new MGUBlockReg<>("jumbo_tank",
        BlockTankJumbo::new,
        tankJumbo -> new BlockItemTank(tankJumbo, 1024000, new Item.Properties().useBlockDescriptionPrefix()),
        BlockEntityJumboTank::new);

    public static MGUBlockReg<BlockXPSolidifier, MGUBlockItem, BlockEntityXPSolidifier> XPSOLIDIFIER = new MGUBlockReg<>("xpsolidifier",
        BlockXPSolidifier::new,
        xpSolidifier -> new MGUBlockItem(xpSolidifier, new Item.Properties().useBlockDescriptionPrefix()),
        BlockEntityXPSolidifier::new);

    //public static Material MATERIAL_DREADFUL_DIRT = new Material(MaterialColor.DIRT, false, true, false, true, true, false, PushReaction.NORMAL);
    public static MGUBlockReg<BlockDreadfulDirt, MGUBlockItem, ?> DREADFUL_DIRT = new MGUBlockReg<>("dreadful_dirt",
        BlockDreadfulDirt::new
        ,
        dreadfulDirt -> new MGUBlockItem(dreadfulDirt, new Item.Properties().useBlockDescriptionPrefix()));

    public static MGUBlockReg<BlockDelightfulDirt, MGUBlockItem, ?> DELIGHTFUL_DIRT = new MGUBlockReg<>("delightful_dirt",
            BlockDelightfulDirt::new,
            delightfulDirt -> new MGUBlockItem(delightfulDirt, new Item.Properties().useBlockDescriptionPrefix()));

    public static MGUBlockReg<BlockSolidXP, MGUBlockItem, ?> SOLID_XP_BLOCK = new MGUBlockReg<>("solid_xp_block",
        BlockSolidXP::new,
        solidXP -> new MGUBlockItem(solidXP, new Item.Properties()));

    public static MGUBlockReg<BlockEntitySpawner, MGUBlockItem, BlockEntityMGUSpawner> ENTITY_SPAWNER = new MGUBlockReg<>("entity_spawner",
        BlockEntitySpawner::new,
        entitySpawner -> new MGUBlockItem(entitySpawner, new Item.Properties().useBlockDescriptionPrefix()),
        BlockEntityMGUSpawner::new);

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
    public static DeferredBlock<MGUFlowingFluidBlock> FLUID_XP_BLOCK = BLOCKS.register("fluid_xp",
        () -> new MGUFlowingFluidBlock(FLUID_XP,Block.Properties.of().liquid().noCollision().replaceable().strength(100.0F).pushReaction(PushReaction.DESTROY).noLootTable()));

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