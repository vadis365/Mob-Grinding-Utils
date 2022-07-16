package mob_grinding_utils;

import java.util.UUID;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.chat.Component;


public class Reference {
	public static final String MOD_ID = "mob_grinding_utils";
	public static final String MOD_NAME = "mob_grinding_utils";
	public static final String VERSION = "0.3.13";
	public static final GameProfile GAME_PROFILE = new GameProfile(UUID.nameUUIDFromBytes("fakeplayer.mob_masher".getBytes()), Component.translatable("fakeplayer.mob_masher").getString());
}