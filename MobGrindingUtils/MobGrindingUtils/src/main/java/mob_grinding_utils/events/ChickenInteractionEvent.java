package mob_grinding_utils.events;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.ModItems;
import mob_grinding_utils.network.MessageChickenSync;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.PacketDistributor;

public class ChickenInteractionEvent {

	@SubscribeEvent
	public void clickOnChicken(EntityInteract event) {
		if (event.getTarget() instanceof LivingEntity) {
			LivingEntity entity = (LivingEntity) event.getTarget();

			if (entity instanceof ChickenEntity && !entity.isChild()) {
				World world = entity.getEntityWorld();
				if (!event.getItemStack().isEmpty() && (event.getItemStack().getItem() == ModItems.GM_CHICKEN_FEED || event.getItemStack().getItem() == ModItems.GM_CHICKEN_FEED_CURSED)) {
					if (!world.isRemote) {
						CompoundNBT nbt = new CompoundNBT();
						nbt = entity.getPersistentData();
						if (!nbt.contains("shouldExplode")) {
							entity.setInvulnerable(true);
							nbt.putBoolean("shouldExplode", true);
							nbt.putInt("countDown", 0);
							if (event.getItemStack().hasTag() && event.getItemStack().getTag().contains("mguMobName"))
								nbt.putString("mguMobName", event.getItemStack().getTag().getString("mguMobName"));
							if (event.getItemStack().getItem() == ModItems.GM_CHICKEN_FEED_CURSED)
								nbt.putBoolean("cursed", true);

							if (event.getPlayer() instanceof ServerPlayerEntity) {
								MobGrindingUtils.NETWORK_WRAPPER.send(PacketDistributor.ALL.noArg(), new MessageChickenSync(entity, nbt));
							}
						}
						Vector3d vec3d = entity.getMotion();
						entity.setMotion(vec3d.x, (double) 0.06D, vec3d.z);
						((ChickenEntity) entity).setNoGravity(true);

						if (!event.getPlayer().abilities.isCreativeMode)
							event.getItemStack().shrink(1);
					}
				}

			}
		}
	}
}