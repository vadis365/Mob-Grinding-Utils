package mob_grinding_utils.events;

import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.blocks.BlockTank;
import mob_grinding_utils.tile.TileEntityTank;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class FillXPBottleEvent {

	@SubscribeEvent
	public void clickBottle(RightClickBlock event) {
		if (!event.getWorld().isClientSide && event.getEntityLiving() instanceof Player) {
			Player player = (Player) event.getEntityLiving();
			ItemStack handItem = player.getItemInHand(event.getHand());
			if (!handItem.isEmpty()) {
				if (handItem.getItem() == Items.GLASS_BOTTLE) {
					if (event.getWorld().getBlockState(event.getPos()).getBlock() instanceof BlockTank) {
						TileEntityTank tileentity = (TileEntityTank) event.getWorld().getBlockEntity(event.getPos());
						if (tileentity != null) {
							if (tileentity.tank.getFluid() != null && tileentity.tank.getFluid().containsFluid(new FluidStack(ModBlocks.FLUID_XP.get(), 220))) {
								if (tileentity.tank.getFluidAmount() >= 200) {
									tileentity.tank.drain(new FluidStack(tileentity.tank.getFluid(), 200), FluidAction.EXECUTE);
									event.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
									turnBottleIntoItem(handItem, player, new ItemStack(Items.EXPERIENCE_BOTTLE));
									handItem.shrink(1);
									event.getWorld().sendBlockUpdated(tileentity.getBlockPos(), event.getWorld().getBlockState(tileentity.getBlockPos()), event.getWorld().getBlockState(tileentity.getBlockPos()), 3);
								}
							}
						}
					}
				}
			}
		}
	}

	protected ItemStack turnBottleIntoItem(ItemStack stackIn, Player player, ItemStack stack) {
		if (stackIn.getCount() <= 0)
			return stack;
		else {
			if (!player.inventory.add(stack))
				player.drop(stack, false);
			return stackIn;
		}
	}
}
