package mob_grinding_utils.events;

import mob_grinding_utils.ModSounds;
import mob_grinding_utils.blocks.BlockWitherMuffler;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LocalWitherSoundEvent {
	@SubscribeEvent
	public void onWitherBossDeath(LivingDeathEvent event) {
		if (event.getEntity() instanceof WitherBoss wither) {
			Level world = wither.getCommandSenderWorld();
			BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
			boolean playsound = true;
			if (!world.isClientSide) {
				for (int x = -8; x < 8; x++)
					for (int y = -8; y < 8; y++)
						for (int z = -8; z < 8; z++)
							if ((world.getBlockState(mutablePos.set(wither.getX() + x, wither.getY() + y, wither.getZ() + z)).getBlock() instanceof BlockWitherMuffler)) {
								playsound = false;
								break;
							}
				if (playsound) {
					world.playSound(null, wither.getX(), wither.getY(), wither.getZ(), ModSounds.ENTITY_WITHER_SPAWN_LOCAL.get(), SoundSource.HOSTILE, 1.0F, 1.0F);
					world.playSound(null, wither.getX(), wither.getY(), wither.getZ(), ModSounds.ENTITY_WITHER_DEATH_LOCAL.get(), SoundSource.HOSTILE, 1.0F, 1.0F);
				}
			}
		}
	}
}
