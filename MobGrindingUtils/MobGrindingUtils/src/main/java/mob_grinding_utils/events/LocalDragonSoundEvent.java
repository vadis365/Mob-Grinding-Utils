package mob_grinding_utils.events;

import mob_grinding_utils.ModSounds;
import mob_grinding_utils.blocks.BlockDragonMuffler;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

public class LocalDragonSoundEvent {
	@SubscribeEvent
	public void onDragonDeath(LivingDeathEvent event) {
		if (event.getEntity() instanceof EnderDragon dragon) {
			Level world = dragon.getCommandSenderWorld();
			BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
			boolean playsound = true;
			if (!world.isClientSide) {
				for (int x = -32; x < 32; x++)
					for (int y = -32; y < 32; y++)
						for (int z = -32; z < 32; z++)
							if ((world.getBlockState(mutablePos.set(dragon.getX() + x, dragon.getY() + y, dragon.getZ() + z)).getBlock() instanceof BlockDragonMuffler)) {
								playsound = false;
								break;
							}
				if (playsound) {
					world.playSound(null, dragon.getX(), dragon.getY(), dragon.getZ(), ModSounds.ENTITY_DRAGON_DEATH_LOCAL.get(), SoundSource.HOSTILE, 5.0F, 1.0F);
				}
			}
		}
	}
}
