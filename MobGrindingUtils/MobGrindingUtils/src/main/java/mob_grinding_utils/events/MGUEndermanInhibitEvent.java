package mob_grinding_utils.events;

import mob_grinding_utils.blocks.BlockEnderInhibitorOff;
import mob_grinding_utils.blocks.BlockEnderInhibitorOn;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.EntityTeleportEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MGUEndermanInhibitEvent {

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void teleportEvent(EntityTeleportEvent event) {
		if (event.getEntity().getEntityWorld().isRemote || event instanceof EntityTeleportEvent.TeleportCommand || event instanceof EntityTeleportEvent.SpreadPlayersCommand)
			return;
		if (event.getEntity() instanceof LivingEntity) {
			LivingEntity entity = (LivingEntity) event.getEntity();
			if (getIsInhibited(entity))
				event.setCanceled(true);
		}
	}

	public boolean getIsInhibited(LivingEntity entity) {
		AxisAlignedBB axisalignedbb = entity.getBoundingBox().grow(8.0D, 8.0D, 8.0D);
		int n = MathHelper.floor(axisalignedbb.minX);
		int o = MathHelper.floor(axisalignedbb.maxX);
		int p = MathHelper.floor(axisalignedbb.minY);
		int q = MathHelper.floor(axisalignedbb.maxY);
		int n1 = MathHelper.floor(axisalignedbb.minZ);
		int o1 = MathHelper.floor(axisalignedbb.maxZ);
		BlockPos.Mutable mutablePos = new BlockPos.Mutable();

		for (int p1 = n; p1 < o; p1++)
			for (int q1 = p; q1 < q; q1++)
				for (int n2 = n1; n2 < o1; n2++) {
					BlockState state = entity.getEntityWorld().getBlockState(mutablePos.setPos(p1, q1, n2));
					if (state.getBlock() instanceof BlockEnderInhibitorOn && !(state.getBlock() instanceof BlockEnderInhibitorOff))
						return true;
				}
		return false;
	}

}
