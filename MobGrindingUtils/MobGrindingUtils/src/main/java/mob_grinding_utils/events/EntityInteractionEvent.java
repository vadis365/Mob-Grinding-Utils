package mob_grinding_utils.events;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.ModItems;
import mob_grinding_utils.items.ItemGMChickenFeed;
import mob_grinding_utils.items.ItemMobSwab;
import mob_grinding_utils.network.MessageChickenSync;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.merchant.villager.WanderingTraderEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.PacketDistributor;

public class EntityInteractionEvent {

	@SubscribeEvent
	public void clickOnEntity(EntityInteract event) {
		if (event.getTarget() instanceof LivingEntity) {
			LivingEntity entity = (LivingEntity) event.getTarget();

			if (entity instanceof WanderingTraderEntity && !event.getItemStack().isEmpty() && event.getItemStack().getItem() instanceof ItemMobSwab) {
				event.getItemStack().interactWithEntity(event.getPlayer(), entity, event.getHand());
				return;
			}

			if (entity instanceof ChickenEntity && !entity.isChild()) {
				World world = entity.getEntityWorld();
				ItemStack eventItem = event.getItemStack();
				if (!eventItem.isEmpty() && eventItem.getItem() instanceof ItemGMChickenFeed) {
					if (!world.isRemote) {
						CompoundNBT nbt = entity.getPersistentData();
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
							if (event.getPlayer() instanceof ServerPlayerEntity) {
								MobGrindingUtils.NETWORK_WRAPPER.send(PacketDistributor.ALL.noArg(), new MessageChickenSync(entity, nbt));
							}
						}
						Vector3d vec3d = entity.getMotion();
						entity.setMotion(vec3d.x, 0.06D, vec3d.z);
						entity.setNoGravity(true);

						if (!event.getPlayer().abilities.isCreativeMode)
							event.getItemStack().shrink(1);
					}
				}

			}
		}
	}
}