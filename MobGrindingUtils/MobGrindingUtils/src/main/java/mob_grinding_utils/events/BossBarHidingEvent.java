package mob_grinding_utils.events;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.BossInfo;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class BossBarHidingEvent {

	@SubscribeEvent
	public void onRenderHUD(BossInfo event) {
		if (event.getType().equals(RenderGameOverlayEvent.ElementType.BOSSINFO)) {
			PlayerEntity player = Minecraft.getInstance().player;

			if (player != null) {
				CompoundNBT nbt = player.getPersistentData();
				if (nbt.getBoolean("MGU_WitherMuffle")) {
					if (event.getBossInfo().getName().getUnformattedComponentText().equals("Wither"))
						event.setCanceled(true);
				}

				if (nbt.getBoolean("MGU_DragonMuffle")) {
					if (event.getBossInfo().getName().getUnformattedComponentText().equals("Ender Dragon"))
						event.setCanceled(true);
				}
			}
		}
	}
}
