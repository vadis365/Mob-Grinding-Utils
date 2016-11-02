package mob_grinding_utils.events;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.blocks.BlockDragonMuffler;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


public class LocalDragonSoundEvent {
	@SubscribeEvent
	public void onDragonDeath(LivingDeathEvent event) {
		if (event.getEntity() instanceof EntityDragon) {
			EntityDragon dragon = (EntityDragon) event.getEntity();
			World world = dragon.worldObj;
			boolean playsound = true;
			if (!world.isRemote) {
				for (int x = -8; x < 8; x++)
					for (int y = -8; y < 8; y++)
						for (int z = -8; z < 8; z++)
							if ((world.getBlockState(new BlockPos(dragon.posX + x, dragon.posY + y, dragon.posZ + z)).getBlock() instanceof BlockDragonMuffler))
								playsound = false;
				if (playsound) {
					world.playSound(null, dragon.posX, dragon.posY, dragon.posZ, MobGrindingUtils.ENTITY_DRAGON_DEATH_LOCAL, SoundCategory.HOSTILE, 5.0F, 1.0F);
				}
			}
		}
	}
}
