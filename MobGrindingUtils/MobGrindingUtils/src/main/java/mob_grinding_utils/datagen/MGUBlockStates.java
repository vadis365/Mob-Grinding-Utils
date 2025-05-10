package mob_grinding_utils.datagen;

import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.Reference;
import mob_grinding_utils.blocks.BlockDragonMuffler;
import mob_grinding_utils.blocks.BlockSpikes;
import mob_grinding_utils.blocks.BlockWitherMuffler;
import mob_grinding_utils.blocks.BlockXPSolidifier;
import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.function.Supplier;

public class MGUBlockStates extends BlockStateProvider {
    public MGUBlockStates(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen.getPackOutput(), Reference.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(ModBlocks.ABSORPTION_HOPPER.getBlock(), models().getExistingFile(modLoc("absorption_hopper")));
        simpleBlock(ModBlocks.DELIGHTFUL_DIRT.getBlock(), models().getExistingFile(modLoc("delightful_dirt")));
        simpleBlock(ModBlocks.DREADFUL_DIRT.getBlock(), models().getExistingFile(modLoc("dreadful_dirt")));
        simpleBlock(ModBlocks.DARK_OAK_STONE.getBlock(), models().getExistingFile(modLoc("dark_oak_stone")));
        simpleBlock(ModBlocks.SOLID_XP_BLOCK.getBlock(), models().getExistingFile(modLoc("solid_xp_block")));

        var dragonMufflerBuilder = getVariantBuilder(ModBlocks.DRAGON_MUFFLER.getBlock());
        dragonMufflerBuilder.partialState().with(BlockDragonMuffler.MODE, false).modelForState().modelFile(models().getExistingFile(modLoc("dragon_muffler"))).addModel();
        dragonMufflerBuilder.partialState().with(BlockDragonMuffler.MODE, true).modelForState().modelFile(models().getExistingFile(modLoc("dragon_muffler_boss_bar"))).addModel();

        var witherMufflerBuilder = getVariantBuilder(ModBlocks.WITHER_MUFFLER.getBlock());
        witherMufflerBuilder.partialState().with(BlockWitherMuffler.MODE, false).modelForState().modelFile(models().getExistingFile(modLoc("wither_muffler"))).addModel();
        witherMufflerBuilder.partialState().with(BlockWitherMuffler.MODE, true).modelForState().modelFile(models().getExistingFile(modLoc("wither_muffler_boss_bar"))).addModel();

        var spikesBuilder = getVariantBuilder(ModBlocks.SPIKES.getBlock());
        spikesBuilder.partialState().with(BlockSpikes.FACING, Direction.UP).modelForState().modelFile(models().getExistingFile(modLoc("spikes"))).addModel();
        spikesBuilder.partialState().with(BlockSpikes.FACING, Direction.DOWN).modelForState().modelFile(models().getExistingFile(modLoc("spikes"))).rotationX(180).addModel();
        spikesBuilder.partialState().with(BlockSpikes.FACING, Direction.NORTH).modelForState().modelFile(models().getExistingFile(modLoc("spikes"))).rotationX(90).addModel();
        spikesBuilder.partialState().with(BlockSpikes.FACING, Direction.SOUTH).modelForState().modelFile(models().getExistingFile(modLoc("spikes"))).rotationX(270).addModel();
        spikesBuilder.partialState().with(BlockSpikes.FACING, Direction.WEST).modelForState().modelFile(models().getExistingFile(modLoc("spikes"))).rotationX(270).rotationY(90).addModel();
        spikesBuilder.partialState().with(BlockSpikes.FACING, Direction.EAST).modelForState().modelFile(models().getExistingFile(modLoc("spikes"))).rotationX(90).rotationY(90).addModel();

        simpleBlock(ModBlocks.FLUID_XP_BLOCK.get(), models().getExistingFile(modLoc("fluid_xp")));

        entityBlock(ModBlocks.TANK);
        entityBlock(ModBlocks.TANK_SINK);
        entityBlock(ModBlocks.JUMBO_TANK);

        var solidifierBuilder = getVariantBuilder(ModBlocks.XPSOLIDIFIER.getBlock());
        solidifierBuilder.partialState().with(BlockXPSolidifier.FACING, Direction.NORTH).setModels(new ConfiguredModel(models().getExistingFile(modLoc("blank"))));
        solidifierBuilder.partialState().with(BlockXPSolidifier.FACING, Direction.SOUTH).setModels(new ConfiguredModel(models().getExistingFile(modLoc("blank"))));
        solidifierBuilder.partialState().with(BlockXPSolidifier.FACING, Direction.WEST).setModels(new ConfiguredModel(models().getExistingFile(modLoc("blank"))));
        solidifierBuilder.partialState().with(BlockXPSolidifier.FACING, Direction.EAST).setModels(new ConfiguredModel(models().getExistingFile(modLoc("blank"))));
    }

    private void entityBlock(Supplier<? extends Block> blockSupplier) {
        getVariantBuilder(blockSupplier.get()).partialState().setModels(new ConfiguredModel(models().getExistingFile(modLoc("blank"))));
    }
}
