package mob_grinding_utils.datagen;

import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.Reference;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockStates extends BlockStateProvider {
    public BlockStates(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Reference.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        generateXPSolidifier();
    }

    private void generateXPSolidifier() {
        ModelFile.UncheckedModelFile model = new ModelFile.UncheckedModelFile(modLoc("block/xpsolidifier"));

        getVariantBuilder(ModBlocks.XPSOLIDIFIER).partialState().modelForState().modelFile(model).addModel();
    }
}
