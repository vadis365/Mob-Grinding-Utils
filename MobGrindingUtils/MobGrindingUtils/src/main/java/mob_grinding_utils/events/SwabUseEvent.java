package mob_grinding_utils.events;

import mob_grinding_utils.MobGrindingUtils;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SwabUseEvent {

	@SubscribeEvent
	public void clickOnMob(EntityInteract event) {
		EntityLivingBase entity = (EntityLivingBase) event.getTarget();
		World world = entity.worldObj;
		if (!(entity instanceof EntityPlayer)) {
			if (event.getItemStack() != null && event.getItemStack().getItem() == MobGrindingUtils.MOB_SWAB && event.getItemStack().getItemDamage() == 0) {
				if (!world.isRemote) {
					String mobName = EntityList.getEntityString(entity);
					ItemStack stack2 = new ItemStack(MobGrindingUtils.MOB_SWAB, 1, 1);
					if (!stack2.hasTagCompound())
						stack2.setTagCompound(new NBTTagCompound());
					if (!stack2.getTagCompound().hasKey("mguMobName"))
						stack2.getTagCompound().setString("mguMobName", mobName);
					event.getItemStack().stackSize--;
					event.getEntityPlayer().setHeldItem(event.getHand(), stack2);
				}
			}
		}
	}
}