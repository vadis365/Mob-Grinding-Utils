package mob_grinding_utils.events;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.BossInfo;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BossBarHidingEvent {

	@SubscribeEvent
	public void onRenderHUD(BossInfo event) {
		if (event.getType().equals(RenderGameOverlayEvent.ElementType.BOSSINFO)) {
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			if (player != null && player.getEntityData().hasKey("turnOffWitherBossBar"))
				if (player.getEntityData().getBoolean("turnOffWitherBossBar"))
					if (event.getBossInfo().getName().getUnformattedText().equals("Wither"))
						event.setCanceled(true);
			if (player != null && player.getEntityData().hasKey("turnOffDragonBossBar"))
				if (player.getEntityData().getBoolean("turnOffDragonBossBar"))
					if (event.getBossInfo().getName().getUnformattedText().equals("Ender Dragon"))
						event.setCanceled(true);
		}
	}
}
