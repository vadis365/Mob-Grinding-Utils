package mob_grinding_utils.datagen;

import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.ModItems;
import mob_grinding_utils.ModTags;
import mob_grinding_utils.recipe.BeheadingRecipe;
import mob_grinding_utils.recipe.ChickenFeedRecipe;
import mob_grinding_utils.recipe.FluidIngredient;
import mob_grinding_utils.recipe.SolidifyRecipe;
import mob_grinding_utils.util.NoAdvRecipeOutput;
import mob_grinding_utils.util.RL;
import mob_grinding_utils.util.RecipeInjector;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.neoforged.neoforge.common.Tags;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public class MGURecipes extends RecipeProvider.Runner {
    public MGURecipes(DataGenerator generatorIn, CompletableFuture<HolderLookup.Provider> registries) {
        super(generatorIn.getPackOutput(), registries);
    }

    @Nonnull
    @Override
    protected RecipeProvider createRecipeProvider(@Nonnull HolderLookup.Provider registries, @Nonnull RecipeOutput output) {
        return new Provider(registries, output);
    }

    @Nonnull
    @Override
    public String getName() {
        return "MGU Recipes";
    }


    static class Provider extends RecipeProvider {
        public Provider(HolderLookup.Provider registries, RecipeOutput output) {
            super(registries, output);
        }

        @Override
        protected void buildRecipes() {
            var consumer = new NoAdvRecipeOutput(output);
            //Absorption Hopper
            var noneItem = has(Items.AIR);
            Shaped(ModBlocks.ABSORPTION_HOPPER.getItem())
                    .pattern(" E ")
                    .pattern(" O ")
                    .pattern("OHO")
                    .define('E', Items.ENDER_EYE)
                    .define('O', Tags.Items.OBSIDIANS)
                    .define('H', Items.HOPPER)
                    .unlockedBy("", noneItem)
                    .save(consumer, Key("recipe_absorption_hopper"));

            // Absorption Hopper Upgrade
            Shaped(ModItems.ABSORPTION_UPGRADE.get())
                    .pattern(" E ")
                    .pattern("ERE")
                    .pattern("OHO")
                    .define('E', Tags.Items.ENDER_PEARLS)
                    .define('O', Tags.Items.OBSIDIANS)
                    .define('R', Tags.Items.DUSTS_REDSTONE)
                    .define('H', Items.HOPPER)
                    .unlockedBy("", noneItem)
                    .save(consumer, Key("recipe_absorbtion_upgrade"));

            // Spikes
            Shaped(ModBlocks.SPIKES.getItem())
                    .pattern(" S ")
                    .pattern("SIS")
                    .define('S', Items.IRON_SWORD)
                    .define('I', Tags.Items.STORAGE_BLOCKS_IRON)
                    .unlockedBy("", noneItem)
                    .save(consumer, Key("recipe_spikes"));

            // Tank
            Shaped(ModBlocks.TANK.getItem())
                    .pattern("IGI")
                    .pattern("GGG")
                    .pattern("IGI")
                    .define('I', Tags.Items.INGOTS_IRON)
                    .define('G', Tags.Items.GLASS_BLOCKS)
                    .unlockedBy("", noneItem)
                    .save(consumer, Key("recipe_tank"));

            Shapeless(ModBlocks.TANK.getItem()).requires(ModBlocks.TANK.getItem(), 1)
                    .unlockedBy("", noneItem)
                    .save(consumer, Key("recipe_tank_reset"));

            // Tank Sink
            Shaped(ModBlocks.TANK_SINK.getItem())
                    .pattern(" I ")
                    .pattern("EHE")
                    .pattern(" T ")
                    .define('I', Items.IRON_BARS)
                    .define('E', Items.ENDER_EYE)
                    .define('H', Items.HOPPER)
                    .define('T', ModBlocks.TANK.getItem())
                    .unlockedBy("", noneItem)
                    .save(consumer, Key("recipe_tank_sink"));

            Shapeless(ModBlocks.TANK_SINK.getItem()).requires(ModBlocks.TANK_SINK.getItem(), 1)
                    .unlockedBy("", noneItem)
                    .save(consumer, Key("recipe_tank_sink_reset"));

            // XP TAP
            Shaped(ModBlocks.XP_TAP.getItem())
                    .pattern("O ")
                    .pattern("II")
                    .pattern("I ")
                    .define('O', Tags.Items.OBSIDIANS)
                    .define('I', Tags.Items.INGOTS_IRON)
                    .unlockedBy("", noneItem)
                    .save(consumer, Key("recipe_xp_tap"));

            // Fan
            Shaped(ModBlocks.FAN.getItem())
                    .pattern("SIS")
                    .pattern("IRI")
                    .pattern("SIS")
                    .define('S', Items.STONE_SLAB)
                    .define('I', Tags.Items.INGOTS_IRON)
                    .define('R', Tags.Items.STORAGE_BLOCKS_REDSTONE)
                    .unlockedBy("", noneItem)
                    .save(consumer, Key("recipe_fan"));

            // Fan Upgrades
            Shaped(ModItems.FAN_UPGRADE_WIDTH.get())
                    .pattern("I I")
                    .pattern("FFF")
                    .pattern("I I")
                    .define('I', Tags.Items.INGOTS_IRON)
                    .define('F', Tags.Items.FEATHERS)
                    .unlockedBy("", noneItem)
                    .save(consumer, Key("recipe_fan_upgrade_width"));

            Shaped(ModItems.FAN_UPGRADE_HEIGHT.get())
                    .pattern("IFI")
                    .pattern(" F ")
                    .pattern("IFI")
                    .define('I', Tags.Items.INGOTS_IRON)
                    .define('F', Tags.Items.FEATHERS)
                    .unlockedBy("", noneItem)
                    .save(consumer, Key("recipe_fan_upgrade_height"));

            Shaped(ModItems.FAN_UPGRADE_SPEED.get())
                    .pattern("FIF")
                    .pattern("IRI")
                    .pattern("FIF")
                    .define('I', Tags.Items.INGOTS_IRON)
                    .define('F', Tags.Items.FEATHERS)
                    .define('R', Tags.Items.DUSTS_REDSTONE)
                    .unlockedBy("", noneItem)
                    .save(consumer, Key("recipe_fan_upgrade_speed"));

            // Mob Swab
            Shaped(ModItems.MOB_SWAB.get())
                    .pattern("  W")
                    .pattern(" S ")
                    .pattern("W  ")
                    .define('W', ItemTags.WOOL)
                    .define('S', Tags.Items.RODS_WOODEN)
                    .unlockedBy("", noneItem)
                    .save(consumer, Key("recipe_mob_swab"));

            // Wither Muffler
            Shaped(ModBlocks.WITHER_MUFFLER.getItem())
                    .pattern("WWW")
                    .pattern("WSW")
                    .pattern("WWW")
                    .define('W', ItemTags.WOOL)
                    .define('S', Items.WITHER_SKELETON_SKULL)
                    .unlockedBy("", noneItem)
                    .save(consumer, Key("recipe_wither_muffler"));

            // Dragon Muffler
            Shaped(ModBlocks.DRAGON_MUFFLER.getItem())
                    .pattern("WWW")
                    .pattern("WEW")
                    .pattern("WWW")
                    .define('W', ItemTags.WOOL)
                    .define('E', Items.DRAGON_EGG)
                    .unlockedBy("", noneItem)
                    .save(consumer, Key("recipe_dragon_muffler"));

            // Mob Masher
            Shaped(ModBlocks.SAW.getItem())
                    .pattern("SDS")
                    .pattern("VRV")
                    .pattern("DID")
                    .define('S', Items.IRON_SWORD)
                    .define('D', Tags.Items.GEMS_DIAMOND)
                    .define('V', ModBlocks.SPIKES.getItem())
                    .define('R', Tags.Items.STORAGE_BLOCKS_REDSTONE)
                    .define('I', Tags.Items.STORAGE_BLOCKS_IRON)
                    .unlockedBy("", noneItem)
                    .save(consumer, Key("recipe_saw"));

            // Mob Masher Upgrades
            Shaped(ModItems.SAW_UPGRADE_SHARPNESS.get())
                    .pattern("GSG")
                    .pattern("SRS")
                    .pattern("GSG")
                    .define('G', Tags.Items.NUGGETS_GOLD)
                    .define('S', Items.IRON_SWORD)
                    .define('R', Tags.Items.DUSTS_REDSTONE)
                    .unlockedBy("", noneItem)
                    .save(consumer, Key("recipe_saw_upgrade_sharpness"));

            Shaped(ModItems.SAW_UPGRADE_LOOTING.get())
                    .pattern("GLG")
                    .pattern("LRL")
                    .pattern("GLG")
                    .define('G', Tags.Items.NUGGETS_GOLD)
                    .define('L', Tags.Items.DYES_BLUE)
                    .define('R', Tags.Items.DUSTS_REDSTONE)
                    .unlockedBy("", noneItem)
                    .save(consumer, Key("recipe_saw_upgrade_looting"));

            Shaped(ModItems.SAW_UPGRADE_FIRE.get())
                    .pattern("GFG")
                    .pattern("FRF")
                    .pattern("GFG")
                    .define('G', Tags.Items.NUGGETS_GOLD)
                    .define('F', Items.FLINT_AND_STEEL)
                    .define('R', Tags.Items.DUSTS_REDSTONE)
                    .unlockedBy("", noneItem)
                    .save(consumer, Key("recipe_saw_upgrade_fire"));

            Shaped(ModItems.SAW_UPGRADE_SMITE.get())
                    .pattern("GFG")
                    .pattern("FRF")
                    .pattern("GFG")
                    .define('G', Tags.Items.NUGGETS_GOLD)
                    .define('F', Items.ROTTEN_FLESH)
                    .define('R', Tags.Items.DUSTS_REDSTONE)
                    .unlockedBy("", noneItem)
                    .save(consumer, Key("recipe_saw_upgrade_smite"));

            Shaped(ModItems.SAW_UPGRADE_ARTHROPOD.get())
                    .pattern("GSG")
                    .pattern("SRS")
                    .pattern("GSG")
                    .define('G', Tags.Items.NUGGETS_GOLD)
                    .define('S', Items.SPIDER_EYE)
                    .define('R', Tags.Items.DUSTS_REDSTONE)
                    .unlockedBy("", noneItem)
                    .save(consumer, Key("recipe_saw_upgrade_arthropod"));

            Shaped(ModItems.SAW_UPGRADE_BEHEADING.get())
                    .pattern("GHG")
                    .pattern("IRI")
                    .pattern("GHG")
                    .define('G', Tags.Items.NUGGETS_GOLD)
                    .define('H', Items.GOLDEN_HELMET)
                    .define('I', Items.IRON_HELMET)
                    .define('R', Tags.Items.DUSTS_REDSTONE)
                    .unlockedBy("", noneItem)
                    .save(consumer, Key("recipe_saw_upgrade_beheading"));

            // Entity Conveyor
            Shaped(ModBlocks.ENTITY_CONVEYOR.getItem(), 6)
                    .pattern(" S ")
                    .pattern("IRI")
                    .pattern("ISI")
                    .define('I', Tags.Items.INGOTS_IRON)
                    .define('S', Tags.Items.SLIME_BALLS)
                    .define('R', Tags.Items.DUSTS_REDSTONE)
                    .unlockedBy("", noneItem)
                    .save(consumer, Key("recipe_entity_conveyor"));

            // Ender Inhibitor
            Shaped(ModBlocks.ENDER_INHIBITOR_ON.getItem())
                    .pattern(" R ")
                    .pattern("IEI")
                    .pattern(" G ")
                    .define('I', Tags.Items.INGOTS_IRON)
                    .define('E', Items.ENDER_EYE)
                    .define('R', Tags.Items.DUSTS_REDSTONE)
                    .define('G', Tags.Items.DUSTS_GLOWSTONE)
                    .unlockedBy("", noneItem)
                    .save(consumer, Key("recipe_ender_inhibitor"));

            //Jumbo Tank
            Shaped(ModBlocks.JUMBO_TANK.getItem())
                    .pattern("ITI")
                    .pattern("T T")
                    .pattern("ITI")
                    .define('I', Tags.Items.INGOTS_IRON)
                    .define('T', ModBlocks.TANK.getItem())
                    .unlockedBy("", noneItem)
                    .save(consumer, Key("recipe_jumbotank"));

            Shapeless(ModBlocks.JUMBO_TANK.getItem()).requires(ModBlocks.JUMBO_TANK.getItem(), 1)
                    .unlockedBy("", noneItem)
                    .save(consumer, Key("recipe_jumbo_tank_reset"));

            //Tinted Glass
            Shaped(ModBlocks.TINTED_GLASS.getItem(), 4)
                    .pattern("CGC")
                    .pattern("GCG")
                    .pattern("CGC")
                    .define('C', ItemTags.COALS)
                    .define('G', Tags.Items.GLASS_BLOCKS)
                    .unlockedBy("", noneItem)
                    .save(consumer, Key("recipe_tintedglass"));

            Shaped(ModItems.GM_CHICKEN_FEED_CURSED.get())
                    .pattern("BEB")
                    .pattern("RSX")
                    .pattern("BGB")
                    .define('B', new FluidIngredient(ModTags.Fluids.EXPERIENCE).toVanilla())
                    .define('E', Items.SPIDER_EYE)
                    .define('R', Items.ROTTEN_FLESH)
                    .define('S', Tags.Items.SEEDS)
                    .define('X', Tags.Items.BONES)
                    .define('G', Tags.Items.GUNPOWDERS)
                    .unlockedBy("", noneItem)
                    .save(consumer, Key("recipe_cursed_feed"));

            Shaped(ModBlocks.XPSOLIDIFIER.getItem())
                    .pattern(" P ")
                    .pattern("CHC")
                    .pattern(" T ")
                    .define('P', Items.PISTON)
                    .define('C', ModBlocks.ENTITY_CONVEYOR.getItem())
                    .define('H', Items.HOPPER)
                    .define('T', ModBlocks.TANK.getItem())
                    .unlockedBy("", noneItem)
                    .save(consumer, Key("recipe_solidifier"));

            Shapeless(ModBlocks.XPSOLIDIFIER.getItem()).requires(ModBlocks.XPSOLIDIFIER.getItem(), 1)
                    .unlockedBy("", noneItem)
                    .save(consumer, Key("recipe_solidifier_reset"));

            Shaped(ModBlocks.ENTITY_SPAWNER.getItem())
                    .pattern("EEE")
                    .pattern("XRX")
                    .pattern("IPI")
                    .define('P', Items.PISTON)
                    .define('I', Tags.Items.STORAGE_BLOCKS_IRON)
                    .define('R', Tags.Items.STORAGE_BLOCKS_REDSTONE)
                    .define('X', ModBlocks.SOLID_XP_BLOCK.getItem())
                    .define('E', Items.ENDER_EYE)
                    .unlockedBy("", noneItem)
                    .save(consumer, Key("recipe_entity_spawner"));

            //Blank Mould
            Shaped(ModItems.SOLID_XP_MOULD_BLANK.get())
                    .pattern("XXX")
                    .pattern("XBX")
                    .pattern("XXX")
                    .define('X', Tags.Items.NUGGETS_GOLD)
                    .define('B', new FluidIngredient(ModTags.Fluids.EXPERIENCE).toVanilla())
                    .unlockedBy("", noneItem)
                    .save(consumer, Key("recipe_mould_blank"));

            //Mould upgrade chain, starting with blank
            Shapeless(ModItems.SOLID_XP_MOULD_BABY.get())
                    .requires(ModItems.SOLID_XP_MOULD_BLANK.get())
                    .unlockedBy("", noneItem)
                    .save(consumer, Key("recipe_mould_baby_upgrade"));

            //Last one in the chain should reset to blank
            Shapeless(ModItems.SOLID_XP_MOULD_BLANK.get())
                    .requires(ModItems.SOLID_XP_MOULD_BABY.get())
                    .unlockedBy("", noneItem)
                    .save(consumer, Key("recipe_mould_reset"));

            //Solid XP Block
            Shapeless(ModBlocks.SOLID_XP_BLOCK.getItem())
                    .requires(ModItems.SOLID_XP_BABY.get(), 9)
                    .unlockedBy("", noneItem)
                    .save(consumer, Key("recipe_xp_block"));
            //Uncraft
            Shapeless(ModItems.SOLID_XP_BABY.get(), 9)
                    .requires(ModBlocks.SOLID_XP_BLOCK.getItem())
                    .unlockedBy("", noneItem)
                    .save(consumer, Key("recipe_xp_block_uncraft"));

            //Solidifier upgrade
            Shaped(ModItems.XP_SOLIDIFIER_UPGRADE.get())
                    .pattern("SRS")
                    .pattern("BXB")
                    .pattern("SRS")
                    .define('S', Items.SUGAR)
                    .define('R', Tags.Items.DUSTS_REDSTONE)
                    .define('B', Items.BLAZE_POWDER)
                    .define('X', new FluidIngredient(ModTags.Fluids.EXPERIENCE).toVanilla())
                    .unlockedBy("", noneItem)
                    .save(consumer, Key("recipe_xpsolidifier_upgrade"));

            Shaped(ModItems.NUTRITIOUS_CHICKEN_FEED.get())
                    .pattern("BCB")
                    .pattern("PSX")
                    .pattern("BWB")
                    .define('B', new FluidIngredient(ModTags.Fluids.EXPERIENCE).toVanilla())
                    .define('C', Items.CARROT)
                    .define('P', Items.POTATO)
                    .define('S', Tags.Items.SEEDS)
                    .define('X', Items.BEETROOT)
                    .define('W', Items.WHEAT)
                    .unlockedBy("", noneItem)
                    .save(consumer, Key("recipe_nutritious_feed"));

            //Spawner width upgrade
            Shaped(ModItems.SPAWNER_UPGRADE_WIDTH.get())
                    .pattern("EEE")
                    .pattern("BXB")
                    .pattern("EEE")
                    .define('E', Items.EGG)
                    .define('B', Items.BLAZE_POWDER)
                    .define('X', new FluidIngredient(ModTags.Fluids.EXPERIENCE).toVanilla())
                    .unlockedBy("", noneItem)
                    .save(consumer, Key("recipe_spawner_upgrade_width"));

            //Spawner height upgrade
            Shaped(ModItems.SPAWNER_UPGRADE_HEIGHT.get())
                    .pattern("EBE")
                    .pattern("EXE")
                    .pattern("EBE")
                    .define('E', Items.EGG)
                    .define('B', Items.BLAZE_POWDER)
                    .define('X', new FluidIngredient(ModTags.Fluids.EXPERIENCE).toVanilla())
                    .unlockedBy("", noneItem)
                    .save(consumer, Key("recipe_spawner_upgrade_height"));

            Shapeless(ModItems.GM_CHICKEN_FEED.get())
                    .requires(Tags.Items.SEEDS)
                    .requires(ModItems.MOB_SWAB_USED.get())
                    .requires(new FluidIngredient(ModTags.Fluids.EXPERIENCE).toVanilla())
                    .unlockedBy("", noneItem)
                    .save(new RecipeInjector<ShapelessRecipe>(consumer, ChickenFeedRecipe::new), Key("gm_chicken_feed"));


            //Solidifier recipes
            consumer.accept(Key("solidify/jelly_baby"), new SolidifyRecipe(Ingredient.of(ModItems.SOLID_XP_MOULD_BABY.get()), new ItemStack(ModItems.SOLID_XP_BABY.get()), 1000), null);

            generateBeheading(consumer);
        }

        private void generateBeheading(RecipeOutput consumer) {
            Head(consumer, "creeper", EntityType.CREEPER, Items.CREEPER_HEAD);
            Head(consumer, "skeleton", EntityType.SKELETON, Items.SKELETON_SKULL);
            Head(consumer, "wither_skeleton", EntityType.WITHER_SKELETON, Items.WITHER_SKELETON_SKULL);
            Head(consumer, "zombie", EntityType.ZOMBIE, Items.ZOMBIE_HEAD);
            Head(consumer, "dragon", EntityType.ENDER_DRAGON, Items.DRAGON_HEAD);

    /*
            //Heads
            OptionalHead(consumer, "blaze", "tconstruct", EntityType.BLAZE, new ResourceLocation("tconstruct", "blaze_head"));
            OptionalHead(consumer, "enderman", "tconstruct", EntityType.ENDERMAN, new ResourceLocation("tconstruct", "enderman_head"));
            OptionalHead(consumer, "husk", "tconstruct", EntityType.HUSK, new ResourceLocation("tconstruct", "husk_head"));
            OptionalHead(consumer, "drowned", "tconstruct", EntityType.DROWNED, new ResourceLocation("tconstruct", "drowned_head"));
            OptionalHead(consumer, "spider", "tconstruct", EntityType.SPIDER, new ResourceLocation("tconstruct", "spider_head"));
            OptionalHead(consumer, "cave_spider", "tconstruct", EntityType.CAVE_SPIDER, new ResourceLocation("tconstruct", "cave_spider_head"));
            OptionalHead(consumer, "piglin", "tconstruct", EntityType.PIGLIN, new ResourceLocation("tconstruct", "piglin_head"));
            OptionalHead(consumer, "piglin_brute", "tconstruct", EntityType.PIGLIN_BRUTE, new ResourceLocation("tconstruct", "piglin_brute_head"));
            OptionalHead(consumer, "zombified_piglin_brute", "tconstruct", EntityType.ZOMBIFIED_PIGLIN, new ResourceLocation("tconstruct", "zombified_piglin_head"));
    */
        }

        private BeheadingRecipe HeadRecipe(EntityType<?> type, Item item) {
            return new BeheadingRecipe(type, new ItemStack(item));
        }

    /*    private BeheadingRecipe HeadRecipe(EntityType<?> type, ResourceLocation item) {
            return new BeheadingRecipe(type, item);
        }*/

        /*    private void OptionalHead(RecipeOutput consumer, String name, String modid, EntityType<?> type, ResourceLocation item) {
                consumer.accept(RL.rl( "beheading/" + name), HeadRecipe(type, item), null,
                        new ModLoadedCondition(modid));
            }*/
        private void Head(RecipeOutput consumer, String name, EntityType<?> type, Item item) {
            consumer.accept(Key("beheading/" + name), HeadRecipe(type, item), null);
        }

        private ShapelessRecipeBuilder Shapeless(Item result) {
            return ShapelessRecipeBuilder.shapeless(items, RecipeCategory.MISC, result);
        }

        private ShapelessRecipeBuilder Shapeless(Item result, int count) {
            return ShapelessRecipeBuilder.shapeless(items, RecipeCategory.MISC, result, count);
        }

        private ShapedRecipeBuilder Shaped(Item result) {
            return ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, result);
        }

        private ShapedRecipeBuilder Shaped(Item result, int count) {
            return ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, result, count);
        }

        private ResourceKey<Recipe<?>> Key(String name) {
            return ResourceKey.create(Registries.RECIPE, RL.mgu(name));
        }
    }
}
