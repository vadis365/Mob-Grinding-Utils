package mob_grinding_utils.datagen;

import javax.annotation.Nullable;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.Reference;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.EntityTypeTagsProvider;
import net.minecraft.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;

public class MGUEntityTypeTags extends EntityTypeTagsProvider {
    public MGUEntityTypeTags(DataGenerator p_i50784_1_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_i50784_1_, Reference.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerTags() {
        this.getOrCreateBuilder(MobGrindingUtils.NOSWAB).add(EntityType.WITHER);
        this.getOrCreateBuilder(MobGrindingUtils.NOSWAB).add(EntityType.ENDER_DRAGON);
    }
}
