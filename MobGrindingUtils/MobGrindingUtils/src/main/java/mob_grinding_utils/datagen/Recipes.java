package mob_grinding_utils.datagen;

import com.google.gson.JsonObject;
import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.ModItems;
import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

import java.nio.file.Path;
import java.util.function.Consumer;

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
                .build(consumer, new ResourceLocation(MobGrindingUtils.MOD_ID, "recipe_"));
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
                .build(consumer, new ResourceLocation(MobGrindingUtils.MOD_ID, "recipe_absorbtion_hopper"));

        // Absorption Hopper Upgrade
        ShapedRecipeBuilder.shapedRecipe(ModItems.ABSORPTION_UPGRADE)
                .patternLine(" E ")
                .patternLine("ERE")
                .patternLine("OHO")
                .key('E', Tags.Items.ENDER_PEARLS)
                .key('O', Tags.Items.OBSIDIAN)
                .key('R', Tags.Items.DUSTS_REDSTONE)
                .addCriterion("", hasItem(Items.AIR))
                .build(consumer, new ResourceLocation(MobGrindingUtils.MOD_ID, "recipe_absorbtion_upgrade"));

        // Spikes
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.SPIKES_ITEM)
                .patternLine(" S ")
                .patternLine("SIS")
                .key('S', Items.IRON_SWORD)
                .key('I', Tags.Items.STORAGE_BLOCKS_IRON)
                .addCriterion("", hasItem(Items.AIR))
                .build(consumer, new ResourceLocation(MobGrindingUtils.MOD_ID, "recipe_spikes"));

        // Tank
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.TANK_ITEM)
                .patternLine("IGI")
                .patternLine("GGG")
                .patternLine("IGI")
                .key('I', Tags.Items.INGOTS_IRON)
                .key('G', Tags.Items.GLASS)
                .addCriterion("", hasItem(Items.AIR))
                .build(consumer, new ResourceLocation(MobGrindingUtils.MOD_ID, "recipe_tank"));

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
                .build(consumer, new ResourceLocation(MobGrindingUtils.MOD_ID, "recipe_tank_sink"));

        // XP TAP
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.XP_TAP_ITEM)
                .patternLine("O ")
                .patternLine("II")
                .patternLine("I ")
                .key('O', Tags.Items.OBSIDIAN)
                .key('I', Tags.Items.INGOTS_IRON)
                .addCriterion("", hasItem(Items.AIR))
                .build(consumer, new ResourceLocation(MobGrindingUtils.MOD_ID, "recipe_xp_tap"));

        // Fan
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.FAN_ITEM)
                .patternLine("SIS")
                .patternLine("IRI")
                .patternLine("SIS")
                .key('S', Items.STONE_SLAB)
                .key('I', Tags.Items.INGOTS_IRON)
                .key('R', Tags.Items.STORAGE_BLOCKS_REDSTONE)
                .addCriterion("", hasItem(Items.AIR))
                .build(consumer, new ResourceLocation(MobGrindingUtils.MOD_ID, "recipe_fan"));
    }

    @Override
    protected void saveRecipeAdvancement(DirectoryCache cache, JsonObject cache2, Path advancementJson) {
        // No thank you.
    }
}
