package mob_grinding_utils.datagen;

import java.nio.file.Path;
import java.util.function.Consumer;

import com.google.gson.JsonObject;

import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.ModItems;
import mob_grinding_utils.Reference;
import net.minecraft.data.*;
import net.minecraft.item.Items;
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
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.ABSORPTION_HOPPER_ITEM)
                .patternLine(" E ")
                .patternLine(" O ")
                .patternLine("OHO")
                .key('E', Items.ENDER_EYE)
                .key('O', Tags.Items.OBSIDIAN)
                .key('H', Items.HOPPER)
                .addCriterion("", hasItem(Items.AIR))
                .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_absorbtion_hopper"));

        // Absorption Hopper Upgrade
        ShapedRecipeBuilder.shapedRecipe(ModItems.ABSORPTION_UPGRADE)
                .patternLine(" E ")
                .patternLine("ERE")
                .patternLine("OHO")
                .key('E', Tags.Items.ENDER_PEARLS)
                .key('O', Tags.Items.OBSIDIAN)
                .key('R', Tags.Items.DUSTS_REDSTONE)
                .key('H', Items.HOPPER)
                .addCriterion("", hasItem(Items.AIR))
                .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_absorbtion_upgrade"));

        // Spikes
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.SPIKES_ITEM)
                .patternLine(" S ")
                .patternLine("SIS")
                .key('S', Items.IRON_SWORD)
                .key('I', Tags.Items.STORAGE_BLOCKS_IRON)
                .addCriterion("", hasItem(Items.AIR))
                .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_spikes"));

        // Tank
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.TANK_ITEM)
                .patternLine("IGI")
                .patternLine("GGG")
                .patternLine("IGI")
                .key('I', Tags.Items.INGOTS_IRON)
                .key('G', Tags.Items.GLASS)
                .addCriterion("", hasItem(Items.AIR))
                .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_tank"));

        ShapelessRecipeBuilder.shapelessRecipe(ModBlocks.TANK_ITEM).addIngredient(ModBlocks.TANK_ITEM,1)
                .addCriterion("", hasItem(Items.AIR))
                .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_tank_reset"));

        // Tank Sink
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.TANK_SINK_ITEM)
                .patternLine(" I ")
                .patternLine("EHE")
                .patternLine(" T ")
                .key('I', Items.IRON_BARS)
                .key('E', Items.ENDER_EYE)
                .key('H', Items.HOPPER)
                .key('T', ModBlocks.TANK_ITEM)
                .addCriterion("", hasItem(Items.AIR))
                .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_tank_sink"));

        ShapelessRecipeBuilder.shapelessRecipe(ModBlocks.TANK_SINK_ITEM).addIngredient(ModBlocks.TANK_SINK_ITEM,1)
                .addCriterion("", hasItem(Items.AIR))
                .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_tank_sink_reset"));

        // XP TAP
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.XP_TAP_ITEM)
                .patternLine("O ")
                .patternLine("II")
                .patternLine("I ")
                .key('O', Tags.Items.OBSIDIAN)
                .key('I', Tags.Items.INGOTS_IRON)
                .addCriterion("", hasItem(Items.AIR))
                .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_xp_tap"));

        // Fan
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.FAN_ITEM)
                .patternLine("SIS")
                .patternLine("IRI")
                .patternLine("SIS")
                .key('S', Items.STONE_SLAB)
                .key('I', Tags.Items.INGOTS_IRON)
                .key('R', Tags.Items.STORAGE_BLOCKS_REDSTONE)
                .addCriterion("", hasItem(Items.AIR))
                .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_fan"));

        // Fan Upgrades
        ShapedRecipeBuilder.shapedRecipe(ModItems.FAN_UPGRADE_WIDTH)
                .patternLine("I I")
                .patternLine("FFF")
                .patternLine("I I")
                .key('I', Tags.Items.INGOTS_IRON)
                .key('F', Tags.Items.FEATHERS)
                .addCriterion("", hasItem(Items.AIR))
                .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_fan_upgrade_width"));

        ShapedRecipeBuilder.shapedRecipe(ModItems.FAN_UPGRADE_HEIGHT)
                .patternLine("IFI")
                .patternLine(" F ")
                .patternLine("IFI")
                .key('I', Tags.Items.INGOTS_IRON)
                .key('F', Tags.Items.FEATHERS)
                .addCriterion("", hasItem(Items.AIR))
                .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_fan_upgrade_height"));

        ShapedRecipeBuilder.shapedRecipe(ModItems.FAN_UPGRADE_SPEED)
                .patternLine("FIF")
                .patternLine("IRI")
                .patternLine("FIF")
                .key('I', Tags.Items.INGOTS_IRON)
                .key('F', Tags.Items.FEATHERS)
                .key('R', Tags.Items.DUSTS_REDSTONE)
                .addCriterion("", hasItem(Items.AIR))
                .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_fan_upgrade_speed"));

        // Mob Swab
        ShapedRecipeBuilder.shapedRecipe(ModItems.MOB_SWAB)
                .patternLine("  W")
                .patternLine(" S ")
                .patternLine("W  ")
                .key('W', ItemTags.WOOL)
                .key('S', Tags.Items.RODS_WOODEN)
                .addCriterion("", hasItem(Items.AIR))
                .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_mob_swab"));

        // Wither Muffler
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.WITHER_MUFFLER_ITEM)
                .patternLine("WWW")
                .patternLine("WSW")
                .patternLine("WWW")
                .key('W', ItemTags.WOOL)
                .key('S', Items.WITHER_SKELETON_SKULL)
                .addCriterion("", hasItem(Items.AIR))
                .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_wither_muffler"));

        // Dragon Muffler
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.DRAGON_MUFFLER_ITEM)
                .patternLine("WWW")
                .patternLine("WEW")
                .patternLine("WWW")
                .key('W', ItemTags.WOOL)
                .key('E', Items.DRAGON_EGG)
                .addCriterion("", hasItem(Items.AIR))
                .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_dragon_muffler"));

        // Mob Masher
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.SAW_ITEM)
                .patternLine("SDS")
                .patternLine("VRV")
                .patternLine("DID")
                .key('S', Items.IRON_SWORD)
                .key('D', Tags.Items.GEMS_DIAMOND)
                .key('V', ModBlocks.SPIKES_ITEM)
                .key('R', Tags.Items.STORAGE_BLOCKS_REDSTONE)
                .key('I', Tags.Items.STORAGE_BLOCKS_IRON)
                .addCriterion("", hasItem(Items.AIR))
                .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_saw"));

        // Mob Masher Upgrades
        ShapedRecipeBuilder.shapedRecipe(ModItems.SAW_UPGRADE_SHARPNESS)
                .patternLine("GSG")
                .patternLine("SRS")
                .patternLine("GSG")
                .key('G', Tags.Items.NUGGETS_GOLD)
                .key('S', Items.IRON_SWORD)
                .key('R', Tags.Items.DUSTS_REDSTONE)
                .addCriterion("", hasItem(Items.AIR))
                .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_saw_upgrade_sharpness"));

        ShapedRecipeBuilder.shapedRecipe(ModItems.SAW_UPGRADE_LOOTING)
                .patternLine("GLG")
                .patternLine("LRL")
                .patternLine("GLG")
                .key('G', Tags.Items.NUGGETS_GOLD)
                .key('L', Tags.Items.DYES_BLUE)
                .key('R', Tags.Items.DUSTS_REDSTONE)
                .addCriterion("", hasItem(Items.AIR))
                .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_saw_upgrade_looting"));

        ShapedRecipeBuilder.shapedRecipe(ModItems.SAW_UPGRADE_FIRE)
                .patternLine("GFG")
                .patternLine("FRF")
                .patternLine("GFG")
                .key('G', Tags.Items.NUGGETS_GOLD)
                .key('F', Items.FLINT_AND_STEEL)
                .key('R', Tags.Items.DUSTS_REDSTONE)
                .addCriterion("", hasItem(Items.AIR))
                .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_saw_upgrade_fire"));

        ShapedRecipeBuilder.shapedRecipe(ModItems.SAW_UPGRADE_SMITE)
                .patternLine("GFG")
                .patternLine("FRF")
                .patternLine("GFG")
                .key('G', Tags.Items.NUGGETS_GOLD)
                .key('F', Items.ROTTEN_FLESH)
                .key('R', Tags.Items.DUSTS_REDSTONE)
                .addCriterion("", hasItem(Items.AIR))
                .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_saw_upgrade_smite"));

        ShapedRecipeBuilder.shapedRecipe(ModItems.SAW_UPGRADE_ARTHROPOD)
                .patternLine("GSG")
                .patternLine("SRS")
                .patternLine("GSG")
                .key('G', Tags.Items.NUGGETS_GOLD)
                .key('S', Items.SPIDER_EYE)
                .key('R', Tags.Items.DUSTS_REDSTONE)
                .addCriterion("", hasItem(Items.AIR))
                .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_saw_upgrade_arthropod"));

        ShapedRecipeBuilder.shapedRecipe(ModItems.SAW_UPGRADE_BEHEADING)
                .patternLine("GHG")
                .patternLine("IRI")
                .patternLine("GHG")
                .key('G', Tags.Items.NUGGETS_GOLD)
                .key('H', Items.GOLDEN_HELMET)
                .key('I', Items.IRON_HELMET)
                .key('R', Tags.Items.DUSTS_REDSTONE)
                .addCriterion("", hasItem(Items.AIR))
                .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_saw_upgrade_beheading"));

        // Entity Conveyor
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.ENTITY_CONVEYOR_ITEM,6)
                .patternLine(" S ")
                .patternLine("IRI")
                .patternLine("ISI")
                .key('I', Tags.Items.INGOTS_IRON)
                .key('S', Tags.Items.SLIMEBALLS)
                .key('R', Tags.Items.DUSTS_REDSTONE)
                .addCriterion("", hasItem(Items.AIR))
                .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_entity_conveyor"));

        // Ender Inhibitor
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.ENDER_INHIBITOR_ON_ITEM)
                .patternLine(" R ")
                .patternLine("IEI")
                .patternLine(" G ")
                .key('I', Tags.Items.INGOTS_IRON)
                .key('E', Items.ENDER_EYE)
                .key('R', Tags.Items.DUSTS_REDSTONE)
                .key('G', Tags.Items.DUSTS_GLOWSTONE)
                .addCriterion("", hasItem(Items.AIR))
                .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_ender_inhibitor"));

        //Jumbo Tank
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.JUMBO_TANK_ITEM)
                .patternLine("ITI")
                .patternLine("T T")
                .patternLine("ITI")
                .key('I', Tags.Items.INGOTS_IRON)
                .key('T', ModBlocks.TANK_ITEM)
                .addCriterion("", hasItem(Items.AIR))
                .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_jumbotank"));

        ShapelessRecipeBuilder.shapelessRecipe(ModBlocks.JUMBO_TANK_ITEM).addIngredient(ModBlocks.JUMBO_TANK_ITEM,1)
                .addCriterion("", hasItem(Items.AIR))
                .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_jumbo_tank_reset"));

        //Tinted Glass
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.TINTED_GLASS_ITEM, 4)
                .patternLine("CGC")
                .patternLine("GCG")
                .patternLine("CGC")
                .key('C', ItemTags.COALS)
                .key('G', Tags.Items.GLASS)
                .addCriterion("", hasItem(Items.AIR))
                .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_tintedglass"));

        ShapedRecipeBuilder.shapedRecipe(ModItems.GM_CHICKEN_FEED_CURSED)
                .patternLine("BEB")
                .patternLine("RSX")
                .patternLine("BGB")
                .key('B', ModItems.FLUID_XP_BUCKET)
                .key('E', Items.SPIDER_EYE)
                .key('R', Items.ROTTEN_FLESH)
                .key('S', Tags.Items.SEEDS)
                .key('X', Tags.Items.BONES)
                .key('G', Tags.Items.GUNPOWDER)
                .addCriterion("", hasItem(Items.AIR))
                .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_cursed_feed"));

        ShapedRecipeBuilder.shapedRecipe(ModBlocks.XPSOLIDIFIER_ITEM)
                .patternLine(" P ")
                .patternLine("CHC")
                .patternLine(" T ")
                .key('P', Items.PISTON)
                .key('C', ModBlocks.ENTITY_CONVEYOR_ITEM)
                .key('H', Items.HOPPER)
                .key('T', ModBlocks.TANK_ITEM)
                .addCriterion("", hasItem(Items.AIR))
                .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_solidifier"));

        ShapelessRecipeBuilder.shapelessRecipe(ModBlocks.XPSOLIDIFIER_ITEM).addIngredient(ModBlocks.XPSOLIDIFIER_ITEM,1)
                .addCriterion("", hasItem(Items.AIR))
                .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_solidifier_reset"));

        //Blank Mould
        ShapedRecipeBuilder.shapedRecipe(ModItems.SOLID_XP_MOULD_BLANK)
                .patternLine("XXX")
                .patternLine("XBX")
                .patternLine("XXX")
                .key('X', Tags.Items.NUGGETS_GOLD)
                .key('B', ModItems.FLUID_XP_BUCKET)
                .addCriterion("", hasItem(Items.AIR))
                .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_mould_blank"));

        //Mould upgrade chain, starting with blank
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.SOLID_XP_MOULD_BABY)
                .addIngredient(ModItems.SOLID_XP_MOULD_BLANK)
                .addCriterion("", hasItem(Items.AIR))
                .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_mould_baby_upgrade"));

        //Last one in the chain should reset to blank
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.SOLID_XP_MOULD_BLANK)
                .addIngredient(ModItems.SOLID_XP_MOULD_BABY)
                .addCriterion("", hasItem(Items.AIR))
                .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_mould_reset"));

        //Solid XP Block
        ShapelessRecipeBuilder.shapelessRecipe(ModBlocks.SOLID_XP_BLOCK_ITEM)
                .addIngredient(ModItems.SOLID_XP_BABY, 9)
                .addCriterion("", hasItem(Items.AIR))
                .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_xp_block"));
        //Uncraft
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.SOLID_XP_BABY, 9)
                .addIngredient(ModBlocks.SOLID_XP_BLOCK_ITEM)
                .addCriterion("", hasItem(Items.AIR))
                .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_xp_block_uncraft"));

        //Solidifier upgrade
        ShapedRecipeBuilder.shapedRecipe(ModItems.XP_SOLIDIFIER_UPGRADE)
                .patternLine("SRS")
                .patternLine("BXB")
                .patternLine("SRS")
                .key('S', Items.SUGAR)
                .key('R', Tags.Items.DUSTS_REDSTONE)
                .key('B', Items.BLAZE_POWDER)
                .key('X', ModItems.FLUID_XP_BUCKET)
                .addCriterion("", hasItem(Items.AIR))
                .build(consumer, new ResourceLocation(Reference.MOD_ID, "recipe_xpsolidifier_upgrade"));

    }

    @Override
    protected void saveRecipeAdvancement(DirectoryCache cache, JsonObject cache2, Path advancementJson) {
        // No thank you.
    }
}
