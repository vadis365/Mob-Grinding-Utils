package mob_grinding_utils.events;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.CustomizeGuiOverlayEvent;

@OnlyIn(Dist.CLIENT)
public class BossBarHidingEvent {

	@SubscribeEvent
	public void onRenderHUD(CustomizeGuiOverlayEvent.BossEventProgress event) {
		Player player = Minecraft.getInstance().player;
		if (player != null) {
			CompoundTag nbt = player.getPersistentData();
			if (nbt.getBoolean("MGU_WitherMuffle")) {
				if (event.getBossEvent().getName().getString().contains("Wither"))
					event.setCanceled(true);
			}

			if (nbt.getBoolean("MGU_DragonMuffle")) {
				if (event.getBossEvent().getName().getString().contains("Dragon"))
					event.setCanceled(true);
			}
		}
	}
}
