package mob_grinding_utils.datagen;

import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.Reference;
import mob_grinding_utils.blocks.BlockDragonMuffler;
import mob_grinding_utils.blocks.BlockSpikes;
import mob_grinding_utils.blocks.BlockWitherMuffler;
import mob_grinding_utils.blocks.BlockXPSolidifier;
import mob_grinding_utils.util.RL;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.stream.Stream;

/** Generates blockstates while retaining the hand-authored block models. */
public class MGUBlockStates extends ModelProvider {
    public MGUBlockStates(PackOutput output) {
        super(output, Reference.MOD_ID);
    }

    @Override
    protected Stream<? extends net.minecraft.core.Holder<Block>> getKnownBlocks() {
        return Stream.of(
            ModBlocks.ABSORPTION_HOPPER.getBlock(), ModBlocks.DELIGHTFUL_DIRT.getBlock(), ModBlocks.DREADFUL_DIRT.getBlock(),
            ModBlocks.DARK_OAK_STONE.getBlock(), ModBlocks.SOLID_XP_BLOCK.getBlock(), ModBlocks.DRAGON_MUFFLER.getBlock(),
            ModBlocks.WITHER_MUFFLER.getBlock(), ModBlocks.SPIKES.getBlock(), ModBlocks.FLUID_XP_BLOCK.get(),
            ModBlocks.TANK.getBlock(), ModBlocks.TANK_SINK.getBlock(), ModBlocks.JUMBO_TANK.getBlock(), ModBlocks.XPSOLIDIFIER.getBlock()
        ).map(Block::builtInRegistryHolder);
    }

    @Override
    protected Stream<? extends net.minecraft.core.Holder<Item>> getKnownItems() {
        return Stream.empty();
    }

    @Override
    protected void registerModels(BlockModelGenerators blocks, ItemModelGenerators items) {
        simple(blocks, ModBlocks.ABSORPTION_HOPPER.getBlock(), "absorption_hopper");
        simple(blocks, ModBlocks.DELIGHTFUL_DIRT.getBlock(), "delightful_dirt");
        simple(blocks, ModBlocks.DREADFUL_DIRT.getBlock(), "dreadful_dirt");
        simple(blocks, ModBlocks.DARK_OAK_STONE.getBlock(), "dark_oak_stone");
        simple(blocks, ModBlocks.SOLID_XP_BLOCK.getBlock(), "solid_xp_block");
        simple(blocks, ModBlocks.FLUID_XP_BLOCK.get(), "fluid_xp");

        blocks.blockStateOutput.accept(MultiVariantGenerator.dispatch(ModBlocks.DRAGON_MUFFLER.getBlock()).with(
            PropertyDispatch.initial(BlockDragonMuffler.MODE).select(false, model("dragon_muffler")).select(true, model("dragon_muffler_boss_bar"))
        ));
        blocks.blockStateOutput.accept(MultiVariantGenerator.dispatch(ModBlocks.WITHER_MUFFLER.getBlock()).with(
            PropertyDispatch.initial(BlockWitherMuffler.MODE).select(false, model("wither_muffler")).select(true, model("wither_muffler_boss_bar"))
        ));
        blocks.blockStateOutput.accept(MultiVariantGenerator.dispatch(ModBlocks.SPIKES.getBlock()).with(
            PropertyDispatch.initial(BlockSpikes.FACING)
                .select(Direction.UP, model("spikes"))
                .select(Direction.DOWN, model("spikes").with(BlockModelGenerators.X_ROT_180))
                .select(Direction.NORTH, model("spikes").with(BlockModelGenerators.X_ROT_90))
                .select(Direction.SOUTH, model("spikes").with(BlockModelGenerators.X_ROT_270))
                .select(Direction.WEST, model("spikes").with(BlockModelGenerators.X_ROT_270.then(BlockModelGenerators.Y_ROT_90)))
                .select(Direction.EAST, model("spikes").with(BlockModelGenerators.X_ROT_90.then(BlockModelGenerators.Y_ROT_90)))
        ));

        simple(blocks, ModBlocks.TANK.getBlock(), "blank");
        simple(blocks, ModBlocks.TANK_SINK.getBlock(), "blank");
        simple(blocks, ModBlocks.JUMBO_TANK.getBlock(), "blank");
        blocks.blockStateOutput.accept(MultiVariantGenerator.dispatch(ModBlocks.XPSOLIDIFIER.getBlock()).with(
            PropertyDispatch.initial(BlockXPSolidifier.FACING).generate(direction -> model("blank"))
        ));
    }

    private static void simple(BlockModelGenerators generators, Block block, String model) {
        generators.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, model(model)));
    }

    private static MultiVariant model(String path) {
        return BlockModelGenerators.plainVariant(RL.mgu("block/" + path));
    }
}
