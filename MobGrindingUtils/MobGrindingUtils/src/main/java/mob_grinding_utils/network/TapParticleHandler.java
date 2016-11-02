package mob_grinding_utils.network;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.tile.TileEntityXPTap;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TapParticleHandler implements IMessageHandler<TapParticleMessage, IMessage> {

	@Override
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(TapParticleMessage message, MessageContext ctx) {

		World world = FMLClientHandler.instance().getWorldClient();

		if (world == null)
			return null;

		else if (world.isRemote) {
			TileEntity tileentity = world.getTileEntity(message.tilePos);
			if (tileentity instanceof TileEntityXPTap)
				MobGrindingUtils.PROXY.spawnGlitterParticles(world, message.tilePos.getX() + world.rand.nextDouble() - 0.5 * 0.05, message.tilePos.getY() + 0.125D, message.tilePos.getZ() + world.rand.nextDouble() - 0.5 * 0.05, 0D, 0D, 0D, 10, 16776960, 1.5F);
		}
		return null;
	}
}