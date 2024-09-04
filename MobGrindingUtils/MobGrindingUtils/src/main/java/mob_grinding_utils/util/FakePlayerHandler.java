package mob_grinding_utils.util;

import com.mojang.authlib.GameProfile;
import mob_grinding_utils.Reference;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.FakePlayerFactory;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.UUID;

public class FakePlayerHandler {

    private static FakePlayer getDefault(ServerLevel level) {
        return FakePlayerFactory.get(level, Reference.GAME_PROFILE);
    }

    private static FakePlayer get(ServerLevel level, @Nullable UUID placer) {
        FakePlayer fakePlayer;
        if (placer == null)
            fakePlayer = getDefault(level);
        else
            fakePlayer = FakePlayerFactory.get(level, new GameProfile(placer, Component.translatable("fakeplayer.mob_masher").getString()));

        fakePlayer.getPersistentData().putBoolean(Reference.MOD_ID, true);
        return fakePlayer;
    }

    public static WeakReference<FakePlayer> get(WeakReference<FakePlayer> previous, ServerLevel level, @Nullable UUID placer, BlockPos pos) {
        FakePlayer fakePlayer = previous.get();
        if (fakePlayer == null) {
            fakePlayer = get(level, placer);
            fakePlayer.setPos(pos.getX(), pos.getY(), pos.getZ());
            return new WeakReference<>(fakePlayer);
        } else {
            fakePlayer.setPos(pos.getX(), pos.getY(), pos.getZ());
            return previous;
        }
    }

    public static boolean isMGUFakePlayer(FakePlayer fakePlayer) {
        return fakePlayer.getPersistentData().contains(Reference.MOD_ID);
    }
}
