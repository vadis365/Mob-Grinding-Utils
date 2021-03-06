package mob_grinding_utils.events;

import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.blocks.BlockTank;
import mob_grinding_utils.tile.TileEntityTank;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class FillXPBottleEvent {

	@SubscribeEvent
	public void clickBottle(RightClickBlock event) {
		if (!event.getWorld().isRemote && event.getEntityLiving() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getEntityLiving();
			ItemStack handItem = player.getHeldItem(event.getHand());
			if (!handItem.isEmpty()) {
				if (handItem.getItem() == Items.GLASS_BOTTLE) {
					if (event.getWorld().getBlockState(event.getPos()).getBlock() instanceof BlockTank) {
						TileEntityTank tileentity = (TileEntityTank) event.getWorld().getTileEntity(event.getPos());
						if (tileentity != null) {
							if (tileentity.tank.getFluid() != null && tileentity.tank.getFluid().containsFluid(new FluidStack(ModBlocks.FLUID_XP, 220))) {
								if (tileentity.tank.getFluidAmount() >= 200) {
									tileentity.tank.drain(new FluidStack(tileentity.tank.getFluid(), 200), FluidAction.EXECUTE);
									event.getWorld().playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
									turnBottleIntoItem(handItem, player, new ItemStack(Items.EXPERIENCE_BOTTLE));
									handItem.shrink(1);
									event.getWorld().notifyBlockUpdate(tileentity.getPos(), event.getWorld().getBlockState(tileentity.getPos()), event.getWorld().getBlockState(tileentity.getPos()), 3);
								}
							}
						}
					}
				}
			}
		}
	}

	protected ItemStack turnBottleIntoItem(ItemStack stackIn, PlayerEntity player, ItemStack stack) {
		if (stackIn.getCount() <= 0)
			return stack;
		else {
			if (!player.inventory.addItemStackToInventory(stack))
				player.dropItem(stack, false);
			return stackIn;
		}
	}
}
