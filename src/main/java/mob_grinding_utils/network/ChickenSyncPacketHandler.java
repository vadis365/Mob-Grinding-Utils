package mob_grinding_utils.network;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ChickenSyncPacketHandler implements IMessageHandler<ChickenSyncMessage, IMessage> {

	@Override
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(ChickenSyncMessage message, MessageContext ctx) {

		World world = FMLClientHandler.instance().getWorldClient();

		if (world == null)
			return null;

		else if (world.isRemote) {
			EntityLivingBase chicken = (EntityChicken) world.getEntityByID(message.chickenID);
			if (chicken instanceof EntityChicken) {
				NBTTagCompound nbt = new NBTTagCompound();
				nbt = chicken.getEntityData();
				nbt.setBoolean("shouldExplode", message.nbt.getBoolean("shouldExplode"));
				nbt.setInteger("countDown", message.nbt.getInteger("countDown"));
				if (message.nbt.getInteger("countDown") >= 20) {
					for (int k = 0; k < 20; ++k) {
						double xSpeed = world.rand.nextGaussian() * 0.02D;
						double ySpeed = world.rand.nextGaussian() * 0.02D;
						double zSpeed = world.rand.nextGaussian() * 0.02D;
						world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, chicken.posX + (double) (world.rand.nextFloat() * chicken.width * 2.0F) - (double) chicken.width, chicken.posY + (double) (world.rand.nextFloat() * chicken.height), chicken.posZ + (double) (world.rand.nextFloat() * chicken.width * 2.0F) - (double) chicken.width, xSpeed, ySpeed, zSpeed, new int[0]);
						world.spawnParticle(EnumParticleTypes.LAVA, chicken.posX + (double) (world.rand.nextFloat() * chicken.width * 2.0F) - (double) chicken.width, chicken.posY + (double) (world.rand.nextFloat() * chicken.height), chicken.posZ + (double) (world.rand.nextFloat() * chicken.width * 2.0F) - (double) chicken.width, xSpeed, ySpeed, zSpeed, new int[0]);
					}
				}
			} else {
				System.out.println("WHY THE FUCK IS THE CHICKEN NULL!!!!?");
			}
		}
		return null;
	}
}