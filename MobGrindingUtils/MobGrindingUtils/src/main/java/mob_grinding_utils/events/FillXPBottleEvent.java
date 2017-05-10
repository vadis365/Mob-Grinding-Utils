package mob_grinding_utils.events;

import mob_grinding_utils.blocks.BlockTank;
import mob_grinding_utils.tile.TileEntityTank;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FillXPBottleEvent {

	@SubscribeEvent
	public void clickBottle(RightClickBlock event) {
		if (!event.getWorld().isRemote && event.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			ItemStack handItem = player.getHeldItem(event.getHand());
			if (handItem != null) {
				if (handItem.getItem() == Items.GLASS_BOTTLE) {
					if (event.getWorld().getBlockState(event.getPos()).getBlock() instanceof BlockTank) {
						TileEntityTank tileentity = (TileEntityTank) event.getWorld().getTileEntity(event.getPos());
						if (tileentity != null) {
							if (tileentity.tank.getFluid() != null && tileentity.tank.getFluid().containsFluid(new FluidStack(FluidRegistry.getFluid("xpjuice"), 220))) {
								if (tileentity.tank.getFluidAmount() >= 220) {
									tileentity.tank.drain(new FluidStack(tileentity.tank.getFluid(), 220), true);
									event.getWorld().playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
									turnBottleIntoItem(handItem, player, new ItemStack(Items.EXPERIENCE_BOTTLE));
									--handItem.stackSize;
									tileentity.tank.onContentsChanged();
								}
							}
						}
					}
				}
			}
		}
	}

	protected ItemStack turnBottleIntoItem(ItemStack stackIn, EntityPlayer player, ItemStack stack) {
		if (stackIn.stackSize <= 0)
			return stack;
		else {
			if (!player.inventory.addItemStackToInventory(stack))
				player.dropItem(stack, false);
			return stackIn;
		}
	}
}
