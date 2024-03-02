package mob_grinding_utils.events;

import mob_grinding_utils.ModItems;
import mob_grinding_utils.items.ItemGMChickenFeed;
import mob_grinding_utils.items.ItemMobSwab;
import mob_grinding_utils.network.ChickenSyncPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public class EntityInteractionEvent {

	@SubscribeEvent
	public void clickOnEntity(PlayerInteractEvent.EntityInteract event) {
		if (event.getTarget() instanceof LivingEntity) {
			LivingEntity entity = (LivingEntity) event.getTarget();

			if (entity instanceof WanderingTrader && !event.getItemStack().isEmpty() && event.getItemStack().getItem() instanceof ItemMobSwab) {
				event.getItemStack().interactLivingEntity(event.getEntity(), entity, event.getHand());
				return;
			}

			if (entity instanceof Chicken && !entity.isBaby()) {
				Level world = entity.getCommandSenderWorld();
				ItemStack eventItem = event.getItemStack();
				if (!eventItem.isEmpty() && eventItem.getItem() instanceof ItemGMChickenFeed) {
					if (!world.isClientSide) {
						CompoundTag nbt = entity.getPersistentData();
						if (!nbt.contains("shouldExplode")) {
							entity.setInvulnerable(true);
							nbt.putBoolean("shouldExplode", true);
							nbt.putInt("countDown", 0);
							if (eventItem.hasTag() && eventItem.getTag().contains("mguMobName"))
								nbt.putString("mguMobName", eventItem.getTag().getString("mguMobName"));
							if (eventItem.getItem() == ModItems.GM_CHICKEN_FEED_CURSED.get())
								nbt.putBoolean("cursed", true);
							if (eventItem.getItem() == ModItems.NUTRITIOUS_CHICKEN_FEED.get())
								nbt.putBoolean("nutritious", true);
							if (event.getEntity() instanceof ServerPlayer) {
								PacketDistributor.TargetPoint target = new PacketDistributor.TargetPoint(entity.getX(), entity.getY(), entity.getZ(), 32, entity.getCommandSenderWorld().dimension());
								PacketDistributor.NEAR.with(target).send(new ChickenSyncPacket(entity, nbt));
							}
						}
						Vec3 vec3d = entity.getDeltaMovement();
						entity.setDeltaMovement(vec3d.x, 0.06D, vec3d.z);
						entity.setNoGravity(true);

						if (!event.getEntity().getAbilities().instabuild)
							event.getItemStack().shrink(1);
					}
				}

			}
		}
	}
}