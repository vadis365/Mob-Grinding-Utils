package mob_grinding_utils.events;

import mob_grinding_utils.ModItems;
import mob_grinding_utils.ModSounds;
import mob_grinding_utils.network.ChickenSyncPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.Nonnull;
import java.util.Optional;

public class ChickenFuseEvent {

	@Nonnull
	public static ItemStack getSpawnEgg(@Nonnull EntityType<?> entityType) {
		return SpawnEggItem.byId(entityType).map(holder -> new ItemStack(holder.value())).orElse(ItemStack.EMPTY);
	}

	@SubscribeEvent
	public void startChickenFuse(EntityTickEvent.Post event) { // TODO uh oh? is this right?!
		Entity entity = event.getEntity();
		if (entity instanceof Chicken) {
			Level world = entity.level();
			if (world instanceof ServerLevel serverLevel) {
				CompoundTag nbt = entity.getPersistentData();
				if (nbt.contains("shouldExplode")) {
					int startTime = event.getEntity().getPersistentData().getInt("countDown").orElse(0);

					if (startTime <= 19) {
						nbt.putInt("countDown", nbt.getInt("countDown").orElse(0) + 1);
						PacketDistributor.sendToAllPlayers(new ChickenSyncPacket((LivingEntity) entity, nbt));
					}

					if (startTime >= 20) {
					Optional<EntityType<?>> entityMob = EntityType.byString(event.getEntity().getPersistentData().getString("mguMobName").orElse(""));
						entityMob.ifPresent((mob) -> {
							ItemStack eggItem = getSpawnEgg(mob);
							if (eggItem != ItemStack.EMPTY)
								entity.spawnAtLocation(serverLevel, eggItem);
						});

						if (nbt.contains("nutritious") && nbt.getBoolean("nutritious").orElse(false))
							entity.spawnAtLocation(serverLevel, new ItemStack(ModItems.GOLDEN_EGG.get()));

						if (nbt.contains("cursed") && nbt.getBoolean("cursed").orElse(false)) {
							entity.spawnAtLocation(serverLevel, new ItemStack(ModItems.ROTTEN_EGG.get()));
							entity.playSound(ModSounds.SPOOPY_CHANGE.get(), 1F, 1F);
						}
						else {
							entity.playSound(SoundEvents.CHICKEN_SOUNDS.get(net.minecraft.world.entity.animal.chicken.ChickenSoundVariants.SoundSet.CLASSIC).adultSounds().deathSound().value(), 1F, 1F);
							entity.playSound(ModSounds.CHICKEN_RISE.get(), 0.5F, 1F);
						}

						for (int k = 0; k < 4; ++k) {
							ItemStack stack = new ItemStack(Items.FEATHER);
							ItemEntity feather = new ItemEntity(world, entity.getX() + (double) (world.getRandom().nextFloat() * entity.getBbWidth() * 2.0F) - (double) entity.getBbWidth(), entity.getY() + (double) (world.getRandom().nextFloat() * entity.getBbHeight()), entity.getZ() + (double) (world.getRandom().nextFloat() * entity.getBbWidth() * 2.0F) - (double) entity.getBbWidth(), stack);
							world.addFreshEntity(feather);
						}
						entity.remove(Entity.RemovalReason.DISCARDED);
					}
				}
			}
		}
	}

}
