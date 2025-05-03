package mob_grinding_utils.datagen;

import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.Reference;
import mob_grinding_utils.blocks.BlockDragonMuffler;
import mob_grinding_utils.blocks.BlockWitherMuffler;
import net.minecraft.data.DataGenerator;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

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
    }
}
