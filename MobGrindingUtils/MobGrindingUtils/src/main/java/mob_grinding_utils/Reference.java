package mob_grinding_utils;

import java.util.UUID;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.chat.Component;


public class Reference {
	public static final String MOD_ID = "mob_grinding_utils";
	public static final String MOD_NAME = "mob_grinding_utils";
	public static final String VERSION = "0.3.13";
	public static final GameProfile GAME_PROFILE = new GameProfile(
			UUID.fromString("b18836e2-b89d-3cde-a2b0-b130b0af3bdb"),
			"[%s]".formatted(Component.translatable("fakeplayer.mob_masher").getString())
			// Different name from the player controlled ones so the name is different between the two types
	);
}