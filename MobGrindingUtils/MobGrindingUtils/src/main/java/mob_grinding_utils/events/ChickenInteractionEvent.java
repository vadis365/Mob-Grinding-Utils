package mob_grinding_utils.events;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.ModItems;
import mob_grinding_utils.network.MessageChickenSync;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChickenInteractionEvent {

	@SubscribeEvent
	public void clickOnChicken(EntityInteract event) {
		if (event.getTarget() instanceof EntityLivingBase) {
			EntityLivingBase entity = (EntityLivingBase) event.getTarget();

			if (entity instanceof EntityChicken && !entity.isChild()) {
					World world = entity.getEntityWorld();
					if (!event.getItemStack().isEmpty() && event.getItemStack().getItem() == ModItems.GM_CHICKEN_FEED) {
						if (!world.isRemote) {
							NBTTagCompound nbt = new NBTTagCompound();
							nbt = entity.getEntityData();
							if (!nbt.hasKey("shouldExplode")) {
								entity.setEntityInvulnerable(true);
								nbt.setBoolean("shouldExplode", true);
								nbt.setInteger("countDown", 0);
								if (event.getItemStack().hasTagCompound() && event.getItemStack().getTagCompound().hasKey("mguMobName"))
									nbt.setString("mguMobName", event.getItemStack().getTagCompound().getString("mguMobName"));
								if (event.getItemStack().hasTagCompound() && event.getItemStack().getTagCompound().hasKey("chickenType"))
									nbt.setString("chickenType", event.getItemStack().getTagCompound().getString("chickenType"));
								MobGrindingUtils.NETWORK_WRAPPER.sendToAll(new MessageChickenSync(entity, nbt));
							}
							entity.motionY += (0.06D * (double) (10) - entity.motionY) * 0.2D;
							((EntityChicken) entity).setNoGravity(true);

							if (!event.getEntityPlayer().capabilities.isCreativeMode)
								event.getItemStack().shrink(1);
						}
					}
				}
			}
		}
}