package mob_grinding_utils.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ChickenSyncMessage implements IMessage {

	public int chickenID;
	public NBTTagCompound nbt;

	public ChickenSyncMessage() {
	}

	public ChickenSyncMessage(EntityLivingBase chicken, NBTTagCompound chickenNBT) {
		chickenID = chicken.getEntityId();
		nbt = chickenNBT;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(chickenID);
		ByteBufUtils.writeTag(buf, nbt);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		chickenID = buf.readInt();
		nbt = ByteBufUtils.readTag(buf);
	}
}