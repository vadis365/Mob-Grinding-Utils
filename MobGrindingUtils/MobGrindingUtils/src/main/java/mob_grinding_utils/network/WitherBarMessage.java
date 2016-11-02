package mob_grinding_utils.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class WitherBarMessage implements IMessage {

	public boolean showBars;

	public WitherBarMessage() {
	}

	public WitherBarMessage(boolean bars) {
		showBars = bars;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(showBars);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		showBars = buf.readBoolean();
	}
}