package mob_grinding_utils.network;

import mob_grinding_utils.tile.TileEntityAbsorptionHopper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class AbsorptionHopperPacketHandler implements IMessageHandler<AbsorptionHopperMessage, IMessage> {

	@Override
	public IMessage onMessage(final AbsorptionHopperMessage message, MessageContext ctx) {

		final World world = DimensionManager.getWorld(message.dimension);

		if (world == null)
			return null;

		else if (!world.isRemote)
			if (ctx.getServerHandler().player.getEntityId() == message.entityID) {
				final EntityPlayerMP player = ctx.getServerHandler().player;
				player.getServer().addScheduledTask(new Runnable() {
					public void run() {
						TileEntityAbsorptionHopper hopper = (TileEntityAbsorptionHopper ) world.getTileEntity(message.tilePos);
						if (hopper != null) {
							hopper.toggleMode(EnumFacing.getFront(message.buttonID));
							IBlockState state = world.getBlockState(message.tilePos);
							world.notifyBlockUpdate(message.tilePos, state, state, 3);
						}
					}
				});
			}
		return null;
	}
}