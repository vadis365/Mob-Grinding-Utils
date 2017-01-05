package mob_grinding_utils.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FakeSwordSwingSoundEvent {
	@SubscribeEvent
	public void onSwordSoundTriggered(PlaySoundAtEntityEvent event) {
		if (event.getCategory().equals(SoundCategory.PLAYERS)) {
			if (event.getEntity() instanceof EntityPlayer) {
				EntityPlayer fakePlayer = (EntityPlayer) event.getEntity();
				if (fakePlayer.getDisplayNameString().matches(new TextComponentTranslation("fakeplayer.mob_masher").getFormattedText())) {
					event.setCanceled(true);
				}
			}
		}
	}
}