package mob_grinding_utils.events;

import java.util.Optional;

import javax.annotation.Nonnull;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.ModItems;
import mob_grinding_utils.ModSounds;
import mob_grinding_utils.network.MessageChickenSync;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;

public class ChickenFuseEvent {

	@Nonnull
	public static ItemStack getSpawnEgg(@Nonnull EntityType<?> entityType) {
		//Check the spawn egg array
		for (SpawnEggItem eggItem : SpawnEggItem.eggs()) {
			if (eggItem.getType(null).equals(entityType)) {
				return new ItemStack(eggItem);
			}
		}
		//It wasnt there, try grabbing the common naming convention from the item registry.
		return new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(entityType.getRegistryName() + "_spawn_egg")));
	}

	@SubscribeEvent
	public void startChickenFuse(LivingEvent event) {
		LivingEntity entity = (LivingEntity) event.getEntity();
		if (entity instanceof Chicken) {
			Level world = entity.getCommandSenderWorld();
			if (!world.isClientSide) {
				CompoundTag nbt = entity.getPersistentData();
				if (nbt.contains("shouldExplode")) {
					int startTime = event.getEntity().getPersistentData().getInt("countDown");

					if (startTime <= 19) {
						nbt.putInt("countDown", nbt.getInt("countDown") + 1);
						MobGrindingUtils.NETWORK_WRAPPER.send(PacketDistributor.ALL.noArg(), new MessageChickenSync(entity, nbt));
					}

					if (startTime >= 20) {
						Optional<EntityType<?>> entityMob = EntityType.byString(event.getEntity().getPersistentData().getString("mguMobName"));
						entityMob.ifPresent((mob) -> {
							ItemStack eggItem = getSpawnEgg(mob);
							if (eggItem != ItemStack.EMPTY)
								entity.spawnAtLocation(eggItem, 0.0F);
						});

						if (nbt.contains("nutritious") && nbt.getBoolean("nutritious"))
							entity.spawnAtLocation(new ItemStack(ModItems.GOLDEN_EGG.get()), 0.0F);

						if (nbt.contains("cursed") && nbt.getBoolean("cursed")) {
							entity.spawnAtLocation(new ItemStack(ModItems.ROTTEN_EGG.get()), 0.0F);
							entity.playSound(ModSounds.SPOOPY_CHANGE, 1F, 1F);
						}
						else {
							entity.playSound(SoundEvents.CHICKEN_DEATH, 1F, 1F);
							entity.playSound(ModSounds.CHICKEN_RISE, 0.5F, 1F);
						}

						for (int k = 0; k < 4; ++k) {
							ItemStack stack = new ItemStack(Items.FEATHER);
							ItemEntity feather = new ItemEntity(world, entity.getX() + (double) (world.random.nextFloat() * entity.getBbWidth() * 2.0F) - (double) entity.getBbWidth(), entity.getY() + (double) (world.random.nextFloat() * entity.getBbHeight()), entity.getZ() + (double) (world.random.nextFloat() * entity.getBbWidth() * 2.0F) - (double) entity.getBbWidth(), stack);
							world.addFreshEntity(feather);
						}
						entity.remove(Entity.RemovalReason.DISCARDED);
					}
				}
			}
		}
	}

}
