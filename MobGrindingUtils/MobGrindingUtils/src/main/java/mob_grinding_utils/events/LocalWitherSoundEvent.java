package mob_grinding_utils.events;

import mob_grinding_utils.ModSounds;
import mob_grinding_utils.blocks.BlockWitherMuffler;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LocalWitherSoundEvent {
	@SubscribeEvent
	public void onWitherBossDeath(LivingDeathEvent event) {
		if (event.getEntity() instanceof WitherEntity) {
			WitherEntity wither = (WitherEntity) event.getEntity();
			World world = wither.getEntityWorld();
			boolean playsound = true;
			if (!world.isRemote) {
				for (int x = -8; x < 8; x++)
					for (int y = -8; y < 8; y++)
						for (int z = -8; z < 8; z++)
							if ((world.getBlockState(new BlockPos(wither.getPosX() + x, wither.getPosY() + y, wither.getPosZ() + z)).getBlock() instanceof BlockWitherMuffler))
								playsound = false;
				if (playsound) {
					world.playSound(null, wither.getPosX(), wither.getPosY(), wither.getPosZ(), ModSounds.ENTITY_WITHER_SPAWN_LOCAL, SoundCategory.HOSTILE, 1.0F, 1.0F);
					world.playSound(null, wither.getPosX(), wither.getPosY(), wither.getPosZ(), ModSounds.ENTITY_WITHER_DEATH_LOCAL, SoundCategory.HOSTILE, 1.0F, 1.0F);
				}
			}
		}
	}
}
