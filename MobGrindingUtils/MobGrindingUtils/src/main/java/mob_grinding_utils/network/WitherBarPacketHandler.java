package mob_grinding_utils.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WitherBarPacketHandler implements IMessageHandler<WitherBarMessage, IMessage> {

	@Override
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(WitherBarMessage message, MessageContext ctx) {

		World world = FMLClientHandler.instance().getWorldClient();

		if (world == null)
			return null;

		if (world.isRemote) {
			EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
			if (player != null)
				player.getEntityData().setBoolean("turnOffWitherBossBar", message.showBars);
			else
				System.out.println("WHY THE FUCK IS THE PLAYER NULL!!!!?");

		}
		return null;
	}
}