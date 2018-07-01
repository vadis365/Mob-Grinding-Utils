package mob_grinding_utils.network;

import io.netty.buffer.ByteBuf;
import mob_grinding_utils.MobGrindingUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageTapParticle implements IMessage, IMessageHandler<MessageTapParticle, MessageTapParticle> {

	public BlockPos tilePos;

	public MessageTapParticle() {
	}

	public MessageTapParticle(BlockPos pos) {
		tilePos = pos;
	}

	public MessageTapParticle(int x, int y, int z) {
		tilePos = new BlockPos(x, y, z);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketUtils.writeBlockPos(buf, tilePos);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		tilePos = PacketUtils.readBlockPos(buf);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public MessageTapParticle onMessage(MessageTapParticle message, MessageContext ctx) {

		World world = FMLClientHandler.instance().getWorldClient();

		if (world == null)
			return null;

		else if (world.isRemote)
			MobGrindingUtils.PROXY.spawnGlitterParticles(world, message.tilePos.getX() + world.rand.nextDouble() - 0.5 * 0.05, message.tilePos.getY() + 0.125D, message.tilePos.getZ() + world.rand.nextDouble() - 0.5 * 0.05, 0D, 0D, 0D, 10, 16776960, 1.5F);

		return null;
	}
}