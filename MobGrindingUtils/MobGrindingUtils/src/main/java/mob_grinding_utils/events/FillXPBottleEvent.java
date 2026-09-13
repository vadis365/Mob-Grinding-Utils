package mob_grinding_utils.events;

import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.BlockEntities.BlockEntityTank;
import mob_grinding_utils.blocks.BlockTank;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public class FillXPBottleEvent {

	@SubscribeEvent
	public void clickBottle(PlayerInteractEvent.RightClickBlock event) {
		if (!event.getLevel().isClientSide() && event.getEntity() != null) {
			Player player = event.getEntity();
			ItemStack handItem = player.getItemInHand(event.getHand());
			if (!handItem.isEmpty()) {
				if (handItem.getItem() == Items.GLASS_BOTTLE) {
					if (event.getLevel().getBlockState(event.getPos()).getBlock() instanceof BlockTank) {
						BlockEntityTank tileentity = (BlockEntityTank) event.getLevel().getBlockEntity(event.getPos());
						if (tileentity != null) {
							FluidResource resource = tileentity.tank.getResource(0);
							if (!resource.isEmpty() && resource.getFluid().isSame(ModBlocks.FLUID_XP.get()) && tileentity.tank.getAmountAsInt(0) >= 200) {
								try (Transaction tx = Transaction.openRoot()) {
									tileentity.tank.extract(0, resource, 200, tx);
									tx.commit();
								}
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

	protected ItemStack turnBottleIntoItem(ItemStack bottleIn, Player player, ItemStack stack) {
		bottleIn.shrink(1);
		if (bottleIn.isEmpty())
			return stack;
		if (!player.getInventory().add(stack))
			player.drop(stack, false);
		return bottleIn;
	}
}
