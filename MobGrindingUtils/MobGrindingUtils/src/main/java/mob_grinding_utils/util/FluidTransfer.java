package mob_grinding_utils.util;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public final class FluidTransfer {
    private FluidTransfer() {}

    public static boolean interact(Player player, InteractionHand hand, ResourceHandler<FluidResource> tank) {
        ItemAccess access = ItemAccess.forPlayerInteraction(player, hand);
        ResourceHandler<FluidResource> held = access.getCapability(Capabilities.Fluid.ITEM);
        if (held == null) return false;

        try (Transaction transaction = Transaction.openRoot()) {
            FluidResource heldFluid = held.getResource(0);
            int heldAmount = held.getAmountAsInt(0);
            if (!heldFluid.isEmpty() && heldAmount > 0) {
                int moved = tank.insert(heldFluid, heldAmount, transaction);
                if (moved > 0 && held.extract(heldFluid, moved, transaction) == moved) {
                    transaction.commit();
                    return true;
                }
            }

            FluidResource tankFluid = tank.getResource(0);
            int moved = !tankFluid.isEmpty() ? held.insert(tankFluid, Math.min(1000, tank.getAmountAsInt(0)), transaction) : 0;
            if (moved > 0 && tank.extract(tankFluid, moved, transaction) == moved) {
                transaction.commit();
                return true;
            }
        }
        return false;
    }
}
