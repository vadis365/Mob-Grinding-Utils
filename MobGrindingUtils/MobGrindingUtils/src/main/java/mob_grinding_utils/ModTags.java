package mob_grinding_utils;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.material.Fluid;

public class ModTags {
    public static class Fluids {
        public static final TagKey<Fluid> EXPERIENCE = TagKey.create(Registries.FLUID, new ResourceLocation("forge", "experience"));
        public static final TagKey<Fluid> XPJUICE = TagKey.create(Registries.FLUID, new ResourceLocation("forge", "xpjuice"));
    }
    public static class Entities {
        public static final TagKey<EntityType<?>> NO_SWAB = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Reference.MOD_NAME, "no_swab"));
        public static final TagKey<EntityType<?>> NO_SPAWN = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Reference.MOD_NAME, "no_spawn"));
        public static final TagKey<EntityType<?>> NO_DIRT_SPAWN = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Reference.MOD_NAME, "no_dirt_spawn"));
        public static final TagKey<EntityType<?>> NO_DREADFUL_SPAWN = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Reference.MOD_NAME, "no_dreadful_spawn"));
        public static final TagKey<EntityType<?>> NO_DELIGHTFUL_SPAWN = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Reference.MOD_NAME, "no_delightful_spawn"));
    }
    public static class Biomes {
        public static final TagKey<Biome> PASSIVE_OVERRIDE = TagKey.create(Registries.BIOME, new ResourceLocation(Reference.MOD_ID, "passive_override"));
        public static final TagKey<Biome> HOSTILE_OVERRIDE = TagKey.create(Registries.BIOME, new ResourceLocation(Reference.MOD_ID, "hostile_override"));
    }
}
