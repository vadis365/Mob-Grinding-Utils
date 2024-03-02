package mob_grinding_utils.events;

import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.blocks.BlockTank;
import mob_grinding_utils.tile.TileEntityTank;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class FillXPBottleEvent {

	@SubscribeEvent
	public void clickBottle(PlayerInteractEvent.RightClickBlock event) {
		if (!event.getLevel().isClientSide && event.getEntity() != null) {
			Player player = event.getEntity();
			ItemStack handItem = player.getItemInHand(event.getHand());
			if (!handItem.isEmpty()) {
				if (handItem.getItem() == Items.GLASS_BOTTLE) {
					if (event.getLevel().getBlockState(event.getPos()).getBlock() instanceof BlockTank) {
						TileEntityTank tileentity = (TileEntityTank) event.getLevel().getBlockEntity(event.getPos());
						if (tileentity != null) {
							if (tileentity.tank.getFluid() != null && tileentity.tank.getFluid().containsFluid(new FluidStack(ModBlocks.FLUID_XP.get(), 220))) {
								if (tileentity.tank.getFluidAmount() >= 200) {
									tileentity.tank.drain(new FluidStack(tileentity.tank.getFluid(), 200), IFluidHandler.FluidAction.EXECUTE);
									event.getLevel().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
									turnBottleIntoItem(handItem, player, new ItemStack(Items.EXPERIENCE_BOTTLE));
									handItem.shrink(1);
									event.getLevel().sendBlockUpdated(tileentity.getBlockPos(), event.getLevel().getBlockState(tileentity.getBlockPos()), event.getLevel().getBlockState(tileentity.getBlockPos()), 3);
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
			if (!player.getInventory().add(stack))
				player.drop(stack, false);
			return stackIn;
		}
	}
}
