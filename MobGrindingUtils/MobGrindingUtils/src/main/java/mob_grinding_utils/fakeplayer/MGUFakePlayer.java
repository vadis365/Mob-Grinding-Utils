package mob_grinding_utils.fakeplayer;

import java.lang.ref.WeakReference;

import com.mojang.authlib.GameProfile;

import mob_grinding_utils.Reference;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.util.FakePlayer;

public class MGUFakePlayer extends FakePlayer {
    public MGUFakePlayer(ServerLevel world, GameProfile name) {
        super(world, name);
    }

    private static MGUFakePlayer INSTANCE;

    public static WeakReference<MGUFakePlayer> get(ServerLevel world) {
        if (INSTANCE == null) {
            INSTANCE = new MGUFakePlayer(world, Reference.GAME_PROFILE);
        }
        INSTANCE.level = world;
        return new WeakReference<>(INSTANCE);
    }

    public static WeakReference<MGUFakePlayer> get(ServerLevel world, double x, double y, double z) {
        if (INSTANCE == null) {
            INSTANCE = new MGUFakePlayer(world, Reference.GAME_PROFILE);
            INSTANCE.connection = new FakeNetHandler(world.getServer(), INSTANCE);
        }
        INSTANCE.level = world;
        INSTANCE.setPos(x,y,z);
        return new WeakReference<>(INSTANCE);
    }

    @Override
    public boolean canBeAffected(MobEffectInstance potioneffectIn) {
        return false;
    }

    public static void unload(LevelAccessor world) {
        if (INSTANCE != null && INSTANCE.level == world)
            INSTANCE = null;
    }
}
