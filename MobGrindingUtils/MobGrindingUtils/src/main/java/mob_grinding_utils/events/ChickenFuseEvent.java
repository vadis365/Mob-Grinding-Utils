package mob_grinding_utils.events;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.network.ChickenSyncMessage;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChickenFuseEvent {

	@SubscribeEvent
	public void startChickenFuse(LivingEvent event) {
		EntityLivingBase entity = (EntityLivingBase) event.getEntity();
		if (entity instanceof EntityChicken) {
			World world = entity.getEntityWorld();
			if (!world.isRemote) {
				NBTTagCompound nbt = new NBTTagCompound();
				nbt = entity.getEntityData();
				if (nbt.hasKey("shouldExplode")) {
					int startTime = event.getEntity().getEntityData().getInteger("countDown");

					if (startTime <= 19) {
						nbt.setInteger("countDown", nbt.getInteger("countDown") + 1);
						MobGrindingUtils.NETWORK_WRAPPER.sendToAll(new ChickenSyncMessage(entity, nbt));
					}

					if (startTime >= 20) {
						entity.setDead();
						entity.playSound(SoundEvents.ENTITY_CHICKEN_DEATH, 1F, 1F);
						entity.playSound(MobGrindingUtils.CHICKEN_RISE, 0.5F, 1F);
						String name = event.getEntity().getEntityData().getString("mguMobName");
						ResourceLocation resourcelocation = new ResourceLocation(name);
						if (EntityList.ENTITY_EGGS.containsKey(resourcelocation)) {
							ItemStack mobEgg = new ItemStack(Items.SPAWN_EGG);
							NBTTagCompound eggData = new NBTTagCompound();
							NBTTagCompound mobData = new NBTTagCompound();
							mobData.setString("id", name);
							eggData.setTag("EntityTag", mobData);
							mobEgg.setTagCompound(eggData);
							entity.entityDropItem(mobEgg, 0.0F);
							System.out.println("Should drop egg here");
						}

						if (Loader.isModLoaded("chickens") && event.getEntity().getEntityData().hasKey("chickenType")) {
							int type = event.getEntity().getEntityData().getInteger("chickenType");
							ItemStack mobEgg = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("chickens:spawn_egg")), 1, type);
							entity.entityDropItem(mobEgg, 0.0F);
						}

						for (int k = 0; k < 4; ++k) {
							ItemStack stack = new ItemStack(Items.FEATHER);
							EntityItem feather = new EntityItem(world, entity.posX + (double) (world.rand.nextFloat() * entity.width * 2.0F) - (double) entity.width, entity.posY + (double) (world.rand.nextFloat() * entity.height), entity.posZ + (double) (world.rand.nextFloat() * entity.width * 2.0F) - (double) entity.width, stack);
							world.spawnEntity(feather);
						}
					}
				}
			}
		}
	}

}
