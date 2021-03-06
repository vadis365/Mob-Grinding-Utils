package mob_grinding_utils.events;

import mob_grinding_utils.ModSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ChickenFuseEvent {

	@SubscribeEvent
	public void startChickenFuse(LivingEvent event) {
		LivingEntity entity = (LivingEntity) event.getEntity();
		if (entity instanceof ChickenEntity) {
			World world = entity.getEntityWorld();
			if (!world.isRemote) {
				CompoundNBT nbt = new CompoundNBT();
				nbt = entity.getPersistentData();
				if (nbt.contains("shouldExplode")) {
					int startTime = event.getEntity().getPersistentData().getInt("countDown");

					if (startTime <= 19) {
						nbt.putInt("countDown", nbt.getInt("countDown") + 1);
						//MobGrindingUtils.NETWORK_WRAPPER.sendToAll(new MessageChickenSync(entity, nbt)); //todo
					}

					if (startTime >= 20) {
						entity.remove();
						entity.playSound(SoundEvents.ENTITY_CHICKEN_DEATH, 1F, 1F);
						entity.playSound(ModSounds.CHICKEN_RISE, 0.5F, 1F);
						
						//TODO create new spawn egg method
					/*	String name = event.getEntity().getPersistentData().getString("mguMobName");
						ResourceLocation resourcelocation = new ResourceLocation(name);
						
						
						if (EntityList.ENTITY_EGGS.containsKey(resourcelocation)) {
							ItemStack mobEgg = new ItemStack(Items.SPAWNER);
							CompoundNBT eggData = new CompoundNBT();
							CompoundNBT mobData = new CompoundNBT();
							mobData.setString("id", name);
							eggData.setTag("EntityTag", mobData);
							mobEgg.setTagCompound(eggData);
							entity.entityDropItem(mobEgg, 0.0F);
							//System.out.println("Should drop egg here");
						}
					*/	
						
						
						
						// Remove chickens mod stuff because it was a hack for compat
/*
						if (Loader.isModLoaded("chickens") && event.getEntity().getPersistentData().hasKey("chickenType")) {
							CompoundNBT eggData = new CompoundNBT();
							CompoundNBT mobData = new CompoundNBT();
							String type = event.getEntity().getPersistentData().getString("chickenType");
							mobData.setString("id", type);
							eggData.setTag("ChickenType", mobData);
							ItemStack mobEgg = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("chickens:spawn_egg")), 1);
							mobEgg.setTagCompound(eggData);
							entity.entityDropItem(mobEgg, 0.0F);
							//System.out.println("Should drop Modded Chicken egg here " + type);
						}
*/
						for (int k = 0; k < 4; ++k) {
							ItemStack stack = new ItemStack(Items.FEATHER);
							ItemEntity feather = new ItemEntity(world, entity.getPosX() + (double) (world.rand.nextFloat() * entity.getWidth() * 2.0F) - (double) entity.getWidth(), entity.getPosY() + (double) (world.rand.nextFloat() * entity.getHeight()), entity.getPosZ() + (double) (world.rand.nextFloat() * entity.getWidth() * 2.0F) - (double) entity.getWidth(), stack);
							world.addEntity(feather);
						}
					}
				}
			}
		}
	}

}
