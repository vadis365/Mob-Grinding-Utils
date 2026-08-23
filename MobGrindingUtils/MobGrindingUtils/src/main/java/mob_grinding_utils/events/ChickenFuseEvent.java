package mob_grinding_utils.events;

import mob_grinding_utils.ModItems;
import mob_grinding_utils.ModSounds;
import mob_grinding_utils.network.ChickenSyncPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.entity.animal.chicken.ChickenSoundVariants;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.Nonnull;
import java.util.Optional;

public class ChickenFuseEvent {

    @Nonnull
    public static ItemStack getSpawnEgg(@Nonnull EntityType<?> entityType) {
        return SpawnEggItem.byId(entityType).map(ItemStack::new).orElse(ItemStack.EMPTY);
    }

    @SubscribeEvent
    public void startChickenFuse(EntityTickEvent.Post event) { // TODO uh oh? is this right?!
        Entity entity = event.getEntity();
        if (entity instanceof Chicken) {
            Level world = entity.level();
            if (world instanceof ServerLevel serverLevel) {
                CompoundTag nbt = entity.getPersistentData();
                if (nbt.contains("shouldExplode") && nbt.contains("countDown") && nbt.contains("mguMobName")) {
                    int startTime = event.getEntity().getPersistentData().getIntOr("countDown", 0);

                    if (startTime <= 19) {
                        nbt.putInt("countDown", nbt.getIntOr("countDown", 0) + 1);
                        PacketDistributor.sendToAllPlayers(new ChickenSyncPacket((LivingEntity) entity, nbt));
                    }

                    if (startTime >= 20) {
                        Optional<EntityType<?>> entityMob = EntityType.byString(event.getEntity().getPersistentData().getStringOr("mguMobName", ""));
                        entityMob.ifPresent((mob) -> {
                            ItemStack eggItem = getSpawnEgg(mob);
                            if (eggItem != ItemStack.EMPTY)
                                entity.spawnAtLocation(serverLevel, eggItem, 0.0F);
                        });

                        if (nbt.getBooleanOr("nutritious", false))
                            entity.spawnAtLocation(serverLevel, new ItemStack(ModItems.GOLDEN_EGG.get()), 0.0F);

                        if (nbt.getBooleanOr("cursed", false)) {
                            entity.spawnAtLocation(serverLevel, new ItemStack(ModItems.ROTTEN_EGG.get()), 0.0F);
                            entity.playSound(ModSounds.SPOOPY_CHANGE.get(), 1F, 1F);
                        }
                        else {
                            entity.playSound(SoundEvents.CHICKEN_SOUNDS.get(ChickenSoundVariants.SoundSet.CLASSIC).adultSounds().deathSound().value(), 1F, 1F);
                            entity.playSound(ModSounds.CHICKEN_RISE.get(), 0.5F, 1F);
                        }

                        for (int k = 0; k < 4; ++k) {
                            ItemStack stack = new ItemStack(Items.FEATHER);
                            RandomSource random = world.getRandom();
                            ItemEntity feather = new ItemEntity(world, 
                                    entity.getX() + (double) (random.nextFloat() * entity.getBbWidth() * 2.0F) - (double) entity.getBbWidth(), 
                                    entity.getY() + (double) (random.nextFloat() * entity.getBbHeight()), 
                                    entity.getZ() + (double) (random.nextFloat() * entity.getBbWidth() * 2.0F) - (double) entity.getBbWidth(), 
                                    stack);
                            world.addFreshEntity(feather);
                        }
                        entity.remove(Entity.RemovalReason.DISCARDED);
                    }
                }
            }
        }
    }
}
