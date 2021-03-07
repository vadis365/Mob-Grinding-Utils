package mob_grinding_utils.datagen;

import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.Reference;
import net.minecraft.block.Block;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class MGUBlockTags extends BlockTagsProvider {
    public MGUBlockTags(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
        super(generatorIn, Reference.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerTags() {
        Builder<Block> witherTag = this.getOrCreateBuilder(BlockTags.WITHER_IMMUNE);
        Builder<Block> dragonTag = this.getOrCreateBuilder(BlockTags.DRAGON_IMMUNE);
        witherTag.add(ModBlocks.ABSORPTION_HOPPER);
        witherTag.add(ModBlocks.DARK_OAK_STONE);
        witherTag.add(ModBlocks.DRAGON_MUFFLER);
        witherTag.add(ModBlocks.ENDER_INHIBITOR_OFF);
        witherTag.add(ModBlocks.ENDER_INHIBITOR_ON);
        witherTag.add(ModBlocks.ENTITY_CONVEYOR);
        witherTag.add(ModBlocks.FAN);
        witherTag.add(ModBlocks.SAW);
        witherTag.add(ModBlocks.SPIKES);
        witherTag.add(ModBlocks.TANK);
        witherTag.add(ModBlocks.TANK_SINK);
        witherTag.add(ModBlocks.WITHER_MUFFLER);
        witherTag.add(ModBlocks.XP_TAP);

        dragonTag.add(ModBlocks.ABSORPTION_HOPPER);
        dragonTag.add(ModBlocks.DARK_OAK_STONE);
        dragonTag.add(ModBlocks.DRAGON_MUFFLER);
        dragonTag.add(ModBlocks.ENDER_INHIBITOR_OFF);
        dragonTag.add(ModBlocks.ENDER_INHIBITOR_ON);
        dragonTag.add(ModBlocks.ENTITY_CONVEYOR);
        dragonTag.add(ModBlocks.FAN);
        dragonTag.add(ModBlocks.SAW);
        dragonTag.add(ModBlocks.SPIKES);
        dragonTag.add(ModBlocks.TANK);
        dragonTag.add(ModBlocks.TANK_SINK);
        dragonTag.add(ModBlocks.WITHER_MUFFLER);
        dragonTag.add(ModBlocks.XP_TAP);
    }
}
