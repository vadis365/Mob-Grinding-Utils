package mob_grinding_utils.recipe;

import mob_grinding_utils.ItemBlockRegister;
import mob_grinding_utils.ItemBlockRegister;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class RecipeChickenFeed implements IRecipe {

	@Override
	public boolean matches(InventoryCrafting craftMatrix, World world) {
		int size = craftMatrix.getSizeInventory();
		ItemStack is;
		ItemStack mobSwab = null;
		boolean hasSeeds = false;
		boolean hasBucket = false;
		int cnt = 0;

		for (int a = 0; a < size; a++) {
			if ((is = craftMatrix.getStackInSlot(a)) == null)
				continue;
			++cnt;

			if (is.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)) {
				IFluidHandler cap = is.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
				FluidStack available = cap.drain(new FluidStack(FluidRegistry.getFluid("xpjuice"), Fluid.BUCKET_VOLUME), false);
				if (available != null && available.amount > 0 && available.getFluid() == FluidRegistry.getFluid("xpjuice")) {
					hasBucket = true;
				}
			}

			else if (is.getItem() == Items.WHEAT_SEEDS)
				hasSeeds = true;

			else if (is.getItem() == ItemBlockRegister.MOB_SWAB && is.getItemDamage() == 1)
				mobSwab = is;
		}

		if (cnt == 3 && hasSeeds && hasBucket && mobSwab != null)
			try {
				return mobSwab.getTagCompound().hasKey("mguMobName");
			}
			catch (NullPointerException e) {

			}


		return false;

	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting craftMatrix) {
		ItemStack is = null;

		for (int a = 0; a < craftMatrix.getSizeInventory(); a++) {
			is = craftMatrix.getStackInSlot(a);
			if (is != null && is.getItem() == ItemBlockRegister.MOB_SWAB && is.getItemDamage() == 1)
				break;
		}

		if (is == null)
			return null;

		ItemStack chickenFeed = new ItemStack(ItemBlockRegister.GM_CHICKEN_FEED, 1, 0);
		
		if (!chickenFeed.hasTagCompound())
			chickenFeed.setTagCompound(new NBTTagCompound());

		if (!is.hasTagCompound())
			is.setTagCompound(new NBTTagCompound());

		if (is.getTagCompound().hasKey("mguMobName"))
			chickenFeed.getTagCompound().setString("mguMobName", is.getTagCompound().getString("mguMobName"));
		
		if(is.getTagCompound().hasKey("chickenType"))
			chickenFeed.getTagCompound().setInteger("chickenType", is.getTagCompound().getInteger("chickenType"));

		return chickenFeed;
	}

	@Override
	public int getRecipeSize() {
		return 10;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(ItemBlockRegister.GM_CHICKEN_FEED, 1, 0);
	}

	@Override
    public ItemStack[] getRemainingItems(InventoryCrafting inv) {
        ItemStack[] aitemstack = new ItemStack[inv.getSizeInventory()];
        for (int i = 0; i < aitemstack.length; ++i) {
            ItemStack itemstack = inv.getStackInSlot(i);
            aitemstack[i] = net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack);
        }
        return aitemstack;
    }
}
