package mob_grinding_utils;

import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.ForgeTagHandler;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTags {
    public static class Fluids {

        public static final ITag.INamedTag<Fluid> EXPERIENCE = FluidTags.makeWrapperTag(new ResourceLocation("forge", "experience").toString());
        public static final ITag.INamedTag<Fluid> XPJUICE = FluidTags.makeWrapperTag(new ResourceLocation("forge", "xpjuice").toString());
    }
    public static class Entities {

        public static final ITag.INamedTag<EntityType<?>> NO_SWAB = EntityTypeTags.createOptional(new ResourceLocation(Reference.MOD_NAME, "no_swab"));
        public static final ITag.INamedTag<EntityType<?>> NO_SPAWN = EntityTypeTags.createOptional(new ResourceLocation(Reference.MOD_NAME, "no_spawn"));
        public static final ITag.INamedTag<EntityType<?>> NO_DIRT_SPAWN = EntityTypeTags.createOptional(new ResourceLocation(Reference.MOD_NAME, "no_dirt_spawn"));
    }
}
