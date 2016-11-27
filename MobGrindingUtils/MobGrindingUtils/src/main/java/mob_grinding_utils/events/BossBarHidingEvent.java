package mob_grinding_utils.events;

import mob_grinding_utils.capability.bossbars.BossBarPlayerCapability;
import mob_grinding_utils.capability.bossbars.IBossBarCapability;
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
			if (player != null) {
				IBossBarCapability cap = player.getCapability(BossBarPlayerCapability.CAPABILITY_PLAYER_BOSS_BAR, null);

				if (!cap.renderWitherBar()) {
					if (event.getBossInfo().getName().getUnformattedText().equals("Wither") || isWitherCrumbsBoss(event.getBossInfo().getName().getUnformattedText()))
						event.setCanceled(true);
				}

				if (!cap.renderEnderDragonBar()) {
					if (event.getBossInfo().getName().getUnformattedText().equals("Ender Dragon"))
						event.setCanceled(true);
				}
			}
		}
	}

	//Maybe a little over the top on the checking - but meh it's safe :)
	public boolean isWitherCrumbsBoss (String fullName) {
		if(fullName.equals("- WitherCrumb -"))
			return true;
		if(!fullName.startsWith("- ") || !fullName.endsWith(" -"))
			return false;
		String[] entry = fullName.split(" ");
		if (entry.length != 3)
			return false;
		return entry[0].equals("-") && entry[2].equals("-");
	}
}
