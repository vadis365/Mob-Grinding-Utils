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
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class MGUBlockStates extends ModelProvider {
    public MGUBlockStates(DataGenerator gen) {
        super(gen.getPackOutput(), Reference.MOD_ID);
    }

    @Override
    protected void registerModels(@Nonnull BlockModelGenerators blocks, @Nonnull ItemModelGenerators items) {
        simpleBlock(blocks, ModBlocks.ABSORPTION_HOPPER, "absorption_hopper");
        simpleBlock(blocks, ModBlocks.DELIGHTFUL_DIRT, "delightful_dirt");
        simpleBlock(blocks, ModBlocks.DREADFUL_DIRT, "dreadful_dirt");
        simpleBlock(blocks, ModBlocks.DARK_OAK_STONE, "dark_oak_stone");
        simpleBlock(blocks, ModBlocks.SOLID_XP_BLOCK, "solid_xp_block");

        blocks.blockStateOutput.accept(MultiVariantGenerator.dispatch(ModBlocks.DRAGON_MUFFLER.getBlock())
                .with(PropertyDispatch.initial(BlockDragonMuffler.MODE)
                        .select(false, existingModel("dragon_muffler"))
                        .select(true, existingModel("dragon_muffler_boss_bar"))));

        blocks.blockStateOutput.accept(MultiVariantGenerator.dispatch(ModBlocks.WITHER_MUFFLER.getBlock())
                .with(PropertyDispatch.initial(BlockWitherMuffler.MODE)
                        .select(false, existingModel("wither_muffler"))
                        .select(true, existingModel("wither_muffler_boss_bar"))));

        blocks.blockStateOutput.accept(MultiVariantGenerator.dispatch(ModBlocks.SPIKES.getBlock())
                .with(PropertyDispatch.initial(BlockSpikes.FACING)
                .select(Direction.UP, existingModel("spikes"))
                .select(Direction.DOWN, existingModel("spikes").with(BlockModelGenerators.X_ROT_180))
                .select(Direction.NORTH, existingModel("spikes").with(BlockModelGenerators.X_ROT_90))
                .select(Direction.SOUTH, existingModel("spikes").with(BlockModelGenerators.X_ROT_270))
                .select(Direction.WEST, existingModel("spikes").with(BlockModelGenerators.X_ROT_270).with(BlockModelGenerators.Y_ROT_90))
                .select(Direction.EAST, existingModel("spikes").with(BlockModelGenerators.X_ROT_90).with(BlockModelGenerators.Y_ROT_90))));

        simpleBlock(blocks, ModBlocks.FLUID_XP_BLOCK, "fluid_xp");

        entityBlock(blocks, ModBlocks.TANK);
        entityBlock(blocks, ModBlocks.TANK_SINK);
        entityBlock(blocks, ModBlocks.JUMBO_TANK);

        blocks.blockStateOutput.accept(MultiVariantGenerator.dispatch(ModBlocks.XPSOLIDIFIER.getBlock())
                .with(PropertyDispatch.initial(BlockXPSolidifier.FACING).generate(dir -> existingModel("blank"))));
    }

    private void entityBlock(BlockModelGenerators blocks, Supplier<? extends Block> blockSupplier) {
        blocks.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(blockSupplier.get(), existingModel("blank")));
    }

    private static void simpleBlock(BlockModelGenerators blocks, Supplier<? extends Block> blockSupplier, String modelName) {
        blocks.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(blockSupplier.get(), existingModel(modelName)));
    }

    @Nonnull
    private static MultiVariant existingModel(String modelName) {
        return BlockModelGenerators.plainVariant(RL.mgu("block/" + modelName));
    }
}
