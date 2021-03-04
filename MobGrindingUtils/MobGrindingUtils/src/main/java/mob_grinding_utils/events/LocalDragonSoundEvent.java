package mob_grinding_utils.events;

import mob_grinding_utils.ModSounds;
import mob_grinding_utils.blocks.BlockDragonMuffler;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LocalDragonSoundEvent {
	@SubscribeEvent
	public void onDragonDeath(LivingDeathEvent event) {
		if (event.getEntity() instanceof EnderDragonEntity) {
			EnderDragonEntity dragon = (EnderDragonEntity) event.getEntity();
			World world = dragon.getEntityWorld();
			boolean playsound = true;
			if (!world.isRemote) {
				for (int x = -32; x < 32; x++)
					for (int y = -32; y < 32; y++)
						for (int z = -32; z < 32; z++)
							if ((world.getBlockState(new BlockPos(dragon.getPosX() + x, dragon.getPosY() + y, dragon.getPosZ() + z)).getBlock() instanceof BlockDragonMuffler))
								playsound = false;
				if (playsound) {
					world.playSound(null, dragon.getPosX(), dragon.getPosY(), dragon.getPosZ(), ModSounds.ENTITY_DRAGON_DEATH_LOCAL, SoundCategory.HOSTILE, 5.0F, 1.0F);
				}
			}
		}
	}
}
