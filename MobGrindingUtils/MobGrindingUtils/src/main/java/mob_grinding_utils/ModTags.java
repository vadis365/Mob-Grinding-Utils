package mob_grinding_utils;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.material.Fluid;

public class ModTags {
    public static class Fluids {
        public static final TagKey<Fluid> EXPERIENCE = TagKey.create(Registry.FLUID_REGISTRY, new ResourceLocation("forge", "experience"));
        public static final TagKey<Fluid> XPJUICE = TagKey.create(Registry.FLUID_REGISTRY, new ResourceLocation("forge", "xpjuice"));
    }
    public static class Entities {
        public static final TagKey<EntityType<?>> NO_SWAB = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(Reference.MOD_NAME, "no_swab"));
        public static final TagKey<EntityType<?>> NO_SPAWN = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(Reference.MOD_NAME, "no_spawn"));
        public static final TagKey<EntityType<?>> NO_DIRT_SPAWN = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(Reference.MOD_NAME, "no_dirt_spawn"));
    }
    public static class Biomes {
        public static final TagKey<Biome> PASSIVE_OVERRIDE = TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(Reference.MOD_ID, "passive_override"));
        public static final TagKey<Biome> HOSTILE_OVERRIDE = TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(Reference.MOD_ID, "hostile_override"));
    }
}
