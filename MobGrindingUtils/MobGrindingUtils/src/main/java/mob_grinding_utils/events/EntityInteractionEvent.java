package mob_grinding_utils.events;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.ModItems;
import mob_grinding_utils.items.ItemGMChickenFeed;
import mob_grinding_utils.items.ItemMobSwab;
import mob_grinding_utils.network.MessageChickenSync;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.PacketDistributor.TargetPoint;

public class EntityInteractionEvent {

	@SubscribeEvent
	public void clickOnEntity(EntityInteract event) {
		if (event.getTarget() instanceof LivingEntity) {
			LivingEntity entity = (LivingEntity) event.getTarget();

			if (entity instanceof WanderingTrader && !event.getItemStack().isEmpty() && event.getItemStack().getItem() instanceof ItemMobSwab) {
				event.getItemStack().interactLivingEntity(event.getPlayer(), entity, event.getHand());
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
							if (event.getPlayer() instanceof ServerPlayer) {
								TargetPoint target = new TargetPoint(entity.getX(), entity.getY(), entity.getZ(), 32, entity.getCommandSenderWorld().dimension());
								MobGrindingUtils.NETWORK_WRAPPER.send(PacketDistributor.NEAR.with(()->target), new MessageChickenSync(entity, nbt));
							}
						}
						Vec3 vec3d = entity.getDeltaMovement();
						entity.setDeltaMovement(vec3d.x, 0.06D, vec3d.z);
						entity.setNoGravity(true);

						if (!event.getPlayer().abilities.instabuild)
							event.getItemStack().shrink(1);
					}
				}

			}
		}
	}
}