package mob_grinding_utils.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketSetExperience;

// By Funwayguy
public class XPHelper {
	// Pre-calculated XP levels at 1M intervals for speed searching
	private static long[] QUICK_XP = new long[2147];

	static {
		for (int i = 0; i < QUICK_XP.length; i++)
			QUICK_XP[i] = getLevelXP(i * 1000000);
	}

	public static void addXP(EntityPlayer player, long xp, boolean sync) {
		long experience = getPlayerXP(player) + xp;
		player.experienceTotal = experience >= Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) experience;
		player.experienceLevel = getXPLevel(experience);
		long expForLevel = getLevelXP(player.experienceLevel);
		player.experience = (float) ((double) (experience - expForLevel) / (double) xpBarCap(player));

		if (sync && player instanceof EntityPlayerMP)
			syncXP((EntityPlayerMP) player);

	}

	public static void syncXP(EntityPlayerMP player) {
		// Make sure the client isn't being stupid about syncing the experience bars which routinely fail
		player.connection.sendPacket(new SPacketSetExperience(player.experience, player.experienceTotal, player.experienceLevel));
	}

	public static long getPlayerXP(EntityPlayer player) {
		return getLevelXP(player.experienceLevel) + (long) (xpBarCap(player) * (double) player.experience);
	}

	//public static long xpBarCap(EntityPlayer player) {
	//	return player.experienceLevel >= 30L ? 62L + (player.experienceLevel - 30L) * 7L : (player.experienceLevel >= 15L ? 17L + (player.experienceLevel - 15L) * 3L : 17);
	//}
	
    public static long xpBarCap(EntityPlayer player) {
        return player.experienceLevel >= 30L ? 112L + (player.experienceLevel - 30L) * 9L : (player.experienceLevel >= 15L ? 37L + (player.experienceLevel - 15L) * 5L : 7L + player.experienceLevel * 2L);
    }

	public static int getXPLevel(long xp) {
		if (xp < 0)
			return 0;

		int i = 0;

		while (i < QUICK_XP.length && QUICK_XP[i] <= xp)
			i++;

		if (i > 0)
			i = (i - 1) * 1000000;

		while (i < Integer.MAX_VALUE && getLevelXP(i) <= xp)
			i++;

		return i - 1;
	}

	public static long getLevelXP(int level) {
		if (level <= 0)
			return 0;
		if (level > 0 && level < 16)
			return (long) (level * 17D);
		else if (level > 15 && level < 31)
			return (long) (1.5D * Math.pow(level, 2) - (level * 29.5D) + 360L);
		else
			return (long) (3.5D * Math.pow(level, 2) - (level * 151.5D) + 2220L);
	}

}
