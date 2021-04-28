package mob_grinding_utils.recipe;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.ModItems;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

public class RecipeChickenFeed extends SpecialRecipe {

	public RecipeChickenFeed(ResourceLocation idIn) {
		super(idIn);
	}

	@Override
	public boolean matches(CraftingInventory craftMatrix, World world) {
		int size = craftMatrix.getSizeInventory();
		ItemStack mobSwab = ItemStack.EMPTY;
		boolean hasSeeds = false;
		boolean hasBucket = false;
		int cnt = 0;
		for (int a = 0; a < size; a++) {
			ItemStack is = craftMatrix.getStackInSlot(a);
			if (is.isEmpty())
				continue;
			++cnt;
			
			if (is.getItem() == ModItems.FLUID_XP_BUCKET) {
			LazyOptional<IFluidHandlerItem> bucketHandler = is.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
			bucketHandler.ifPresent((receptacle) -> {
				if(!is.isEmpty() && receptacle.getFluidInTank(1).getAmount() > 0 && receptacle.getFluidInTank(1).containsFluid(new FluidStack(ModBlocks.FLUID_XP, 1))) {
					receptacle.drain(FluidAttributes.BUCKET_VOLUME, FluidAction.EXECUTE);
				}
			});
			hasBucket = true;
			}
			else if (is.getItem() == Items.WHEAT_SEEDS)
				hasSeeds = true;
			else if (is.getItem() == ModItems.MOB_SWAB_USED)
				mobSwab = is;
		}
		if (cnt == 3 && hasSeeds && hasBucket && !mobSwab.isEmpty())
			return mobSwab.getTag().contains("mguMobName");
		return false;
	}

	@Override
	public ItemStack getCraftingResult(CraftingInventory  craftMatrix) {
		ItemStack is = ItemStack.EMPTY;

		for (int a = 0; a < craftMatrix.getSizeInventory(); a++) {
			ItemStack is2 = craftMatrix.getStackInSlot(a);
			if (!is2.isEmpty() && is2.getItem() == ModItems.MOB_SWAB_USED)
				is = is2;
		}

		if (is.isEmpty())
			return ItemStack.EMPTY;

		ItemStack chickenFeed = new ItemStack(ModItems.GM_CHICKEN_FEED, 1);

		if (!chickenFeed.hasTag())
			chickenFeed.setTag(new CompoundNBT());

		if (!is.hasTag())
			is.setTag(new CompoundNBT());

		if (is.getTag().contains("mguMobName"))
			chickenFeed.getTag().putString("mguMobName", is.getTag().getString("mguMobName"));

		return chickenFeed;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(ModItems.GM_CHICKEN_FEED, 1);
	}

	@Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInventory inv) {
        NonNullList<ItemStack> aitemstack = NonNullList.<ItemStack>withSize(inv.getSizeInventory(), ItemStack.EMPTY);
        for (int i = 0; i < aitemstack.size(); ++i) {
            ItemStack itemstack = inv.getStackInSlot(i);
            aitemstack.set(i, ForgeHooks.getContainerItem(itemstack));
        }
        return aitemstack;
    }

	@Override
	public boolean canFit(int width, int height) {
		return width * height >= 2;
	}

	public IRecipeSerializer<?> getSerializer() {
		return MobGrindingUtils.RECIPE_CHICKEN_FEED.get();
	}
}