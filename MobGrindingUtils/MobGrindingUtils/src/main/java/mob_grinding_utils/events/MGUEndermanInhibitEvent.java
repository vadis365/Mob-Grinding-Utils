package mob_grinding_utils.events;

import mob_grinding_utils.blocks.BlockEnderInhibitorOff;
import mob_grinding_utils.blocks.BlockEnderInhibitorOn;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MGUEndermanInhibitEvent {

	@SuppressWarnings("resource")
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void teleportEvent(EntityTeleportEvent event) {
		if (event.getEntity().getCommandSenderWorld().isClientSide || event instanceof EntityTeleportEvent.TeleportCommand || event instanceof EntityTeleportEvent.SpreadPlayersCommand)
			return;
		if (event.getEntity() instanceof LivingEntity && !(event.getEntity() instanceof Player)) {
			LivingEntity entity = (LivingEntity) event.getEntity();
			if (getIsInhibited(entity))
				event.setCanceled(true);
		}
	}

	public boolean getIsInhibited(LivingEntity entity) {
		AABB axisalignedbb = entity.getBoundingBox().inflate(8.0D, 8.0D, 8.0D);
		int n = Mth.floor(axisalignedbb.minX);
		int o = Mth.floor(axisalignedbb.maxX);
		int p = Mth.floor(axisalignedbb.minY);
		int q = Mth.floor(axisalignedbb.maxY);
		int n1 = Mth.floor(axisalignedbb.minZ);
		int o1 = Mth.floor(axisalignedbb.maxZ);
		BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

		for (int p1 = n; p1 < o; p1++)
			for (int q1 = p; q1 < q; q1++)
				for (int n2 = n1; n2 < o1; n2++) {
					BlockState state = entity.getCommandSenderWorld().getBlockState(mutablePos.set(p1, q1, n2));
					if (state.getBlock() instanceof BlockEnderInhibitorOn && !(state.getBlock() instanceof BlockEnderInhibitorOff))
						return true;
				}
		return false;
	}

}
