package mob_grinding_utils.events;

import java.util.Optional;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.ModItems;
import mob_grinding_utils.ModSounds;
import mob_grinding_utils.network.MessageChickenSync;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.PacketDistributor;

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
						MobGrindingUtils.NETWORK_WRAPPER.send(PacketDistributor.ALL.noArg(), new MessageChickenSync(entity, nbt));
					}

					if (startTime >= 20) {
						Optional<EntityType<?>> entityMob = EntityType.byKey(event.getEntity().getPersistentData().getString("mguMobName"));
						entityMob.ifPresent((mob) -> {
							for (SpawnEggItem eggItem : SpawnEggItem.getEggs()) {
								if (eggItem.getType(null).equals(mob)) {
									entity.entityDropItem(new ItemStack(eggItem), 0.0F);
									break;
								}
							}
						});

						if (nbt.contains("nutritious") && nbt.getBoolean("nutritious"))
							entity.entityDropItem(new ItemStack(ModItems.GOLDEN_EGG), 0.0F);

						if (nbt.contains("cursed") && nbt.getBoolean("cursed")) {
							entity.entityDropItem(new ItemStack(ModItems.ROTTEN_EGG), 0.0F);
							entity.playSound(ModSounds.SPOOPY_CHANGE, 1F, 1F);
						}
						else {
							entity.playSound(SoundEvents.ENTITY_CHICKEN_DEATH, 1F, 1F);
							entity.playSound(ModSounds.CHICKEN_RISE, 0.5F, 1F);
						}

						for (int k = 0; k < 4; ++k) {
							ItemStack stack = new ItemStack(Items.FEATHER);
							ItemEntity feather = new ItemEntity(world, entity.getPosX() + (double) (world.rand.nextFloat() * entity.getWidth() * 2.0F) - (double) entity.getWidth(), entity.getPosY() + (double) (world.rand.nextFloat() * entity.getHeight()), entity.getPosZ() + (double) (world.rand.nextFloat() * entity.getWidth() * 2.0F) - (double) entity.getWidth(), stack);
							world.addEntity(feather);
						}
						entity.remove();
					}
				}
			}
		}
	}

}
