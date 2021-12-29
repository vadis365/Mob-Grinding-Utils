package mob_grinding_utils.datagen;

import java.nio.file.Path;
import java.util.function.Consumer;

import com.google.gson.JsonObject;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.ModItems;
import mob_grinding_utils.Reference;
import mob_grinding_utils.recipe.FluidIngredient;
import mob_grinding_utils.recipe.SolidifyRecipe;
import mob_grinding_utils.recipe.WrappedRecipe;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class Recipes extends RecipeProvider {
    public Recipes(DataGenerator generatorIn) {
        super(generatorIn);
    }
    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        /*
        //Template
        ShapedRecipeBuilder.shapedRecipe( )
                .patternLine("")
                .patternLine("")
                .patternLine("")
                .key('', )
                .key('', )
                .key('', )
                .addCriterion("", hasItem(Items.AIR))
                .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_"));
        */


        //Absorbtion Hopper
        InventoryChangeTrigger.Instance noneItem = hasItem(Items.AIR);
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.ABSORPTION_HOPPER.getItem())
            .patternLine(" E ")
            .patternLine(" O ")
            .patternLine("OHO")
            .key('E', Items.ENDER_EYE)
            .key('O', Tags.Items.OBSIDIAN)
            .key('H', Items.HOPPER)
            .addCriterion("", noneItem)
            .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_absorbtion_hopper"));

        // Absorption Hopper Upgrade
        ShapedRecipeBuilder.shapedRecipe(ModItems.ABSORPTION_UPGRADE.get())
            .patternLine(" E ")
            .patternLine("ERE")
            .patternLine("OHO")
            .key('E', Tags.Items.ENDER_PEARLS)
            .key('O', Tags.Items.OBSIDIAN)
            .key('R', Tags.Items.DUSTS_REDSTONE)
            .key('H', Items.HOPPER)
            .addCriterion("", noneItem)
            .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_absorbtion_upgrade"));

        // Spikes
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.SPIKES.getItem())
            .patternLine(" S ")
            .patternLine("SIS")
            .key('S', Items.IRON_SWORD)
            .key('I', Tags.Items.STORAGE_BLOCKS_IRON)
            .addCriterion("", noneItem)
            .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_spikes"));

        // Tank
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.TANK.getItem())
            .patternLine("IGI")
            .patternLine("GGG")
            .patternLine("IGI")
            .key('I', Tags.Items.INGOTS_IRON)
            .key('G', Tags.Items.GLASS)
            .addCriterion("", noneItem)
            .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_tank"));

        ShapelessRecipeBuilder.shapelessRecipe(ModBlocks.TANK.getItem()).addIngredient(ModBlocks.TANK.getItem(),1)
            .addCriterion("", noneItem)
            .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_tank_reset"));

        // Tank Sink
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.TANK_SINK.getItem())
            .patternLine(" I ")
            .patternLine("EHE")
            .patternLine(" T ")
            .key('I', Items.IRON_BARS)
            .key('E', Items.ENDER_EYE)
            .key('H', Items.HOPPER)
            .key('T', ModBlocks.TANK.getItem())
            .addCriterion("", noneItem)
            .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_tank_sink"));

        ShapelessRecipeBuilder.shapelessRecipe(ModBlocks.TANK_SINK.getItem()).addIngredient(ModBlocks.TANK_SINK.getItem(),1)
            .addCriterion("", noneItem)
            .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_tank_sink_reset"));

        // XP TAP
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.XP_TAP.getItem())
            .patternLine("O ")
            .patternLine("II")
            .patternLine("I ")
            .key('O', Tags.Items.OBSIDIAN)
            .key('I', Tags.Items.INGOTS_IRON)
            .addCriterion("", noneItem)
            .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_xp_tap"));

        // Fan
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.FAN.getItem())
            .patternLine("SIS")
            .patternLine("IRI")
            .patternLine("SIS")
            .key('S', Items.STONE_SLAB)
            .key('I', Tags.Items.INGOTS_IRON)
            .key('R', Tags.Items.STORAGE_BLOCKS_REDSTONE)
            .addCriterion("", noneItem)
            .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_fan"));

        // Fan Upgrades
        ShapedRecipeBuilder.shapedRecipe(ModItems.FAN_UPGRADE_WIDTH.get())
            .patternLine("I I")
            .patternLine("FFF")
            .patternLine("I I")
            .key('I', Tags.Items.INGOTS_IRON)
            .key('F', Tags.Items.FEATHERS)
            .addCriterion("", noneItem)
            .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_fan_upgrade_width"));

        ShapedRecipeBuilder.shapedRecipe(ModItems.FAN_UPGRADE_HEIGHT.get())
            .patternLine("IFI")
            .patternLine(" F ")
            .patternLine("IFI")
            .key('I', Tags.Items.INGOTS_IRON)
            .key('F', Tags.Items.FEATHERS)
            .addCriterion("", noneItem)
            .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_fan_upgrade_height"));

        ShapedRecipeBuilder.shapedRecipe(ModItems.FAN_UPGRADE_SPEED.get())
            .patternLine("FIF")
            .patternLine("IRI")
            .patternLine("FIF")
            .key('I', Tags.Items.INGOTS_IRON)
            .key('F', Tags.Items.FEATHERS)
            .key('R', Tags.Items.DUSTS_REDSTONE)
            .addCriterion("", noneItem)
            .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_fan_upgrade_speed"));

        // Mob Swab
        ShapedRecipeBuilder.shapedRecipe(ModItems.MOB_SWAB.get())
            .patternLine("  W")
            .patternLine(" S ")
            .patternLine("W  ")
            .key('W', ItemTags.WOOL)
            .key('S', Tags.Items.RODS_WOODEN)
            .addCriterion("", noneItem)
            .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_mob_swab"));

        // Wither Muffler
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.WITHER_MUFFLER.getItem())
            .patternLine("WWW")
            .patternLine("WSW")
            .patternLine("WWW")
            .key('W', ItemTags.WOOL)
            .key('S', Items.WITHER_SKELETON_SKULL)
            .addCriterion("", noneItem)
            .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_wither_muffler"));

        // Dragon Muffler
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.DRAGON_MUFFLER.getItem())
            .patternLine("WWW")
            .patternLine("WEW")
            .patternLine("WWW")
            .key('W', ItemTags.WOOL)
            .key('E', Items.DRAGON_EGG)
            .addCriterion("", noneItem)
            .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_dragon_muffler"));

        // Mob Masher
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.SAW.getItem())
            .patternLine("SDS")
            .patternLine("VRV")
            .patternLine("DID")
            .key('S', Items.IRON_SWORD)
            .key('D', Tags.Items.GEMS_DIAMOND)
            .key('V', ModBlocks.SPIKES.getItem())
            .key('R', Tags.Items.STORAGE_BLOCKS_REDSTONE)
            .key('I', Tags.Items.STORAGE_BLOCKS_IRON)
            .addCriterion("", noneItem)
            .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_saw"));

        // Mob Masher Upgrades
        ShapedRecipeBuilder.shapedRecipe(ModItems.SAW_UPGRADE_SHARPNESS.get())
            .patternLine("GSG")
            .patternLine("SRS")
            .patternLine("GSG")
            .key('G', Tags.Items.NUGGETS_GOLD)
            .key('S', Items.IRON_SWORD)
            .key('R', Tags.Items.DUSTS_REDSTONE)
            .addCriterion("", noneItem)
            .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_saw_upgrade_sharpness"));

        ShapedRecipeBuilder.shapedRecipe(ModItems.SAW_UPGRADE_LOOTING.get())
            .patternLine("GLG")
            .patternLine("LRL")
            .patternLine("GLG")
            .key('G', Tags.Items.NUGGETS_GOLD)
            .key('L', Tags.Items.DYES_BLUE)
            .key('R', Tags.Items.DUSTS_REDSTONE)
            .addCriterion("", noneItem)
            .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_saw_upgrade_looting"));

        ShapedRecipeBuilder.shapedRecipe(ModItems.SAW_UPGRADE_FIRE.get())
            .patternLine("GFG")
            .patternLine("FRF")
            .patternLine("GFG")
            .key('G', Tags.Items.NUGGETS_GOLD)
            .key('F', Items.FLINT_AND_STEEL)
            .key('R', Tags.Items.DUSTS_REDSTONE)
            .addCriterion("", noneItem)
            .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_saw_upgrade_fire"));

        ShapedRecipeBuilder.shapedRecipe(ModItems.SAW_UPGRADE_SMITE.get())
            .patternLine("GFG")
            .patternLine("FRF")
            .patternLine("GFG")
            .key('G', Tags.Items.NUGGETS_GOLD)
            .key('F', Items.ROTTEN_FLESH)
            .key('R', Tags.Items.DUSTS_REDSTONE)
            .addCriterion("", noneItem)
            .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_saw_upgrade_smite"));

        ShapedRecipeBuilder.shapedRecipe(ModItems.SAW_UPGRADE_ARTHROPOD.get())
            .patternLine("GSG")
            .patternLine("SRS")
            .patternLine("GSG")
            .key('G', Tags.Items.NUGGETS_GOLD)
            .key('S', Items.SPIDER_EYE)
            .key('R', Tags.Items.DUSTS_REDSTONE)
            .addCriterion("", noneItem)
            .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_saw_upgrade_arthropod"));

        ShapedRecipeBuilder.shapedRecipe(ModItems.SAW_UPGRADE_BEHEADING.get())
            .patternLine("GHG")
            .patternLine("IRI")
            .patternLine("GHG")
            .key('G', Tags.Items.NUGGETS_GOLD)
            .key('H', Items.GOLDEN_HELMET)
            .key('I', Items.IRON_HELMET)
            .key('R', Tags.Items.DUSTS_REDSTONE)
            .addCriterion("", noneItem)
            .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_saw_upgrade_beheading"));

        // Entity Conveyor
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.ENTITY_CONVEYOR.getItem(),6)
            .patternLine(" S ")
            .patternLine("IRI")
            .patternLine("ISI")
            .key('I', Tags.Items.INGOTS_IRON)
            .key('S', Tags.Items.SLIMEBALLS)
            .key('R', Tags.Items.DUSTS_REDSTONE)
            .addCriterion("", noneItem)
            .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_entity_conveyor"));

        // Ender Inhibitor
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.ENDER_INHIBITOR_ON.getItem())
            .patternLine(" R ")
            .patternLine("IEI")
            .patternLine(" G ")
            .key('I', Tags.Items.INGOTS_IRON)
            .key('E', Items.ENDER_EYE)
            .key('R', Tags.Items.DUSTS_REDSTONE)
            .key('G', Tags.Items.DUSTS_GLOWSTONE)
            .addCriterion("", noneItem)
            .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_ender_inhibitor"));

        //Jumbo Tank
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.JUMBO_TANK.getItem())
            .patternLine("ITI")
            .patternLine("T T")
            .patternLine("ITI")
            .key('I', Tags.Items.INGOTS_IRON)
            .key('T', ModBlocks.TANK.getItem())
            .addCriterion("", noneItem)
            .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_jumbotank"));

        ShapelessRecipeBuilder.shapelessRecipe(ModBlocks.JUMBO_TANK.getItem()).addIngredient(ModBlocks.JUMBO_TANK.getItem(),1)
            .addCriterion("", noneItem)
            .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_jumbo_tank_reset"));

        //Tinted Glass
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.TINTED_GLASS.getItem(), 4)
            .patternLine("CGC")
            .patternLine("GCG")
            .patternLine("CGC")
            .key('C', ItemTags.COALS)
            .key('G', Tags.Items.GLASS)
            .addCriterion("", noneItem)
            .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_tintedglass"));

        ShapedRecipeBuilder.shapedRecipe(ModItems.GM_CHICKEN_FEED_CURSED.get())
            .patternLine("BEB")
            .patternLine("RSX")
            .patternLine("BGB")
            .key('B', ModItems.FLUID_XP_BUCKET.get())
            .key('E', Items.SPIDER_EYE)
            .key('R', Items.ROTTEN_FLESH)
            .key('S', Tags.Items.SEEDS)
            .key('X', Tags.Items.BONES)
            .key('G', Tags.Items.GUNPOWDER)
            .addCriterion("", noneItem)
            .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_cursed_feed"));

        ShapedRecipeBuilder.shapedRecipe(ModBlocks.XPSOLIDIFIER.getItem())
            .patternLine(" P ")
            .patternLine("CHC")
            .patternLine(" T ")
            .key('P', Items.PISTON)
            .key('C', ModBlocks.ENTITY_CONVEYOR.getItem())
            .key('H', Items.HOPPER)
            .key('T', ModBlocks.TANK.getItem())
            .addCriterion("", noneItem)
            .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_solidifier"));

        ShapelessRecipeBuilder.shapelessRecipe(ModBlocks.XPSOLIDIFIER.getItem()).addIngredient(ModBlocks.XPSOLIDIFIER.getItem(),1)
            .addCriterion("", noneItem)
            .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_solidifier_reset"));

        ShapedRecipeBuilder.shapedRecipe(ModBlocks.ENTITY_SPAWNER.getItem())
            .patternLine("EEE")
            .patternLine("XRX")
            .patternLine("IPI")
            .key('P', Items.PISTON)
            .key('I', Tags.Items.STORAGE_BLOCKS_IRON)
            .key('R', Tags.Items.STORAGE_BLOCKS_REDSTONE)
            .key('X', ModBlocks.SOLID_XP_BLOCK.getItem())
            .key('E', Items.ENDER_EYE)
            .addCriterion("", noneItem)
            .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_entity_spawner"));

        //Blank Mould
        ShapedRecipeBuilder.shapedRecipe(ModItems.SOLID_XP_MOULD_BLANK.get())
            .patternLine("XXX")
            .patternLine("XBX")
            .patternLine("XXX")
            .key('X', Tags.Items.NUGGETS_GOLD)
            .key('B', ModItems.FLUID_XP_BUCKET.get())
            .addCriterion("", noneItem)
            .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_mould_blank"));

        //Mould upgrade chain, starting with blank
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.SOLID_XP_MOULD_BABY.get())
            .addIngredient(ModItems.SOLID_XP_MOULD_BLANK.get())
            .addCriterion("", noneItem)
            .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_mould_baby_upgrade"));

        //Last one in the chain should reset to blank
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.SOLID_XP_MOULD_BLANK.get())
            .addIngredient(ModItems.SOLID_XP_MOULD_BABY.get())
            .addCriterion("", noneItem)
            .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_mould_reset"));

        //Solid XP Block
        ShapelessRecipeBuilder.shapelessRecipe(ModBlocks.SOLID_XP_BLOCK.getItem())
            .addIngredient(ModItems.SOLID_XP_BABY.get(), 9)
            .addCriterion("", noneItem)
            .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_xp_block"));
        //Uncraft
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.SOLID_XP_BABY.get(), 9)
            .addIngredient(ModBlocks.SOLID_XP_BLOCK.getItem())
            .addCriterion("", noneItem)
            .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_xp_block_uncraft"));

        //Solidifier upgrade
        ShapedRecipeBuilder.shapedRecipe(ModItems.XP_SOLIDIFIER_UPGRADE.get())
            .patternLine("SRS")
            .patternLine("BXB")
            .patternLine("SRS")
            .key('S', Items.SUGAR)
            .key('R', Tags.Items.DUSTS_REDSTONE)
            .key('B', Items.BLAZE_POWDER)
            .key('X', ModItems.FLUID_XP_BUCKET.get())
            .addCriterion("", noneItem)
            .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_xpsolidifier_upgrade"));

        ShapedRecipeBuilder.shapedRecipe(ModItems.NUTRITIOUS_CHICKEN_FEED.get())
            .patternLine("BCB")
            .patternLine("PSX")
            .patternLine("BWB")
            .key('B', ModItems.FLUID_XP_BUCKET.get())
            .key('C', Items.CARROT)
            .key('P', Items.POTATO)
            .key('S', Tags.Items.SEEDS)
            .key('X', Items.BEETROOT)
            .key('W', Items.WHEAT)
            .addCriterion("", noneItem)
            .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_nutritious_feed"));

        //Spawner width upgrade
        ShapedRecipeBuilder.shapedRecipe(ModItems.SPAWNER_UPGRADE_WIDTH.get())
            .patternLine("EEE")
            .patternLine("BXB")
            .patternLine("EEE")
            .key('E', Items.EGG)
            .key('B', Items.BLAZE_POWDER)
            .key('X', ModItems.FLUID_XP_BUCKET.get())
            .addCriterion("", noneItem)
            .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_spawner_upgrade_width"));

        //Spawner height upgrade
        ShapedRecipeBuilder.shapedRecipe(ModItems.SPAWNER_UPGRADE_HEIGHT.get())
            .patternLine("EBE")
            .patternLine("EXE")
            .patternLine("EBE")
            .key('E', Items.EGG)
            .key('B', Items.BLAZE_POWDER)
            .key('X', ModItems.FLUID_XP_BUCKET.get())
            .addCriterion("", noneItem)
            .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_spawner_upgrade_height"));

        ShapelessRecipeBuilder.shapelessRecipe(ModItems.GM_CHICKEN_FEED.get())
            .addIngredient(Tags.Items.SEEDS)
            .addIngredient(ModItems.MOB_SWAB_USED.get())
            .addIngredient(new FluidIngredient(MobGrindingUtils.EXPERIENCE, false))
            .addCriterion("", noneItem)
            .build(WrappedRecipe.Inject(consumer, MobGrindingUtils.CHICKEN_FEED.get()), new ResourceLocation(Reference.MOD_ID, "gm_chicken_feed"));


        //Solidifier
        consumer.accept(new SolidifyRecipe.FinishedRecipe(new ResourceLocation(Reference.MOD_ID, "solidify/jelly_baby"), Ingredient.fromItems(ModItems.SOLID_XP_MOULD_BABY.get()), new ItemStack(ModItems.SOLID_XP_BABY.get()), 1000));
    }

    @Override
    protected void saveRecipeAdvancement(DirectoryCache cache, JsonObject cache2, Path advancementJson) {
        // No thank you.
    }
}
