package mob_grinding_utils.recipe;

import mob_grinding_utils.ModItems;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class RecipeChickenFeed extends net.minecraftforge.registries.IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

	@Override
	public boolean matches(InventoryCrafting craftMatrix, World world) {
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
			if (is.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
				IFluidHandler cap = is.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
				FluidStack available = cap.drain(new FluidStack(FluidRegistry.getFluid("xpjuice"), Fluid.BUCKET_VOLUME), false);
				if (available != null && available.amount > 0 && available.getFluid() == FluidRegistry.getFluid("xpjuice")) {
					hasBucket = true;
				}
			}
			else if (is.getItem() == Items.WHEAT_SEEDS)
				hasSeeds = true;
			else if (is.getItem() == ModItems.MOB_SWAB && is.getItemDamage() == 1)
				mobSwab = is;
		}
		if (cnt == 3 && hasSeeds && hasBucket && !mobSwab.isEmpty())
			return mobSwab.getTagCompound().hasKey("mguMobName");
		return false;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting craftMatrix) {
		ItemStack is = ItemStack.EMPTY;

		for (int a = 0; a < craftMatrix.getSizeInventory(); a++) {
			ItemStack is2 = craftMatrix.getStackInSlot(a);
			if (!is2.isEmpty() && is2.getItem() == ModItems.MOB_SWAB && is2.getItemDamage() == 1)
				is = is2;
		}

		if (is.isEmpty())
			return ItemStack.EMPTY;

		ItemStack chickenFeed = new ItemStack(ModItems.GM_CHICKEN_FEED, 1, 0);

		if (!chickenFeed.hasTagCompound())
			chickenFeed.setTagCompound(new NBTTagCompound());

		if (!is.hasTagCompound())
			is.setTagCompound(new NBTTagCompound());

		if (is.getTagCompound().hasKey("mguMobName"))
			chickenFeed.getTagCompound().setString("mguMobName", is.getTagCompound().getString("mguMobName"));
		
		if(is.getTagCompound().hasKey("chickenType"))
			chickenFeed.getTagCompound().setString("chickenType", is.getTagCompound().getString("chickenType"));

		return chickenFeed;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(ModItems.GM_CHICKEN_FEED, 1, 0);
	}

	@Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
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
}
