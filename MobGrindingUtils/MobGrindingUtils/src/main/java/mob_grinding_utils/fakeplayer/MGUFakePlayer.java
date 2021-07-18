package mob_grinding_utils.fakeplayer;

import java.lang.ref.WeakReference;

import com.mojang.authlib.GameProfile;

import mob_grinding_utils.Reference;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayer;

public class MGUFakePlayer extends FakePlayer {
    public MGUFakePlayer(ServerWorld world, GameProfile name) {
        super(world, name);
    }

    private static MGUFakePlayer INSTANCE;

    public static WeakReference<MGUFakePlayer> get(ServerWorld world) {
        if (INSTANCE == null) {
            INSTANCE = new MGUFakePlayer(world, Reference.GAME_PROFILE);
        }
        INSTANCE.world = world;
        return new WeakReference<>(INSTANCE);
    }

    public static WeakReference<MGUFakePlayer> get(ServerWorld world, double x, double y, double z) {
        if (INSTANCE == null) {
            INSTANCE = new MGUFakePlayer(world, Reference.GAME_PROFILE);
            INSTANCE.connection = new FakeNetHandler(world.getServer(), INSTANCE);
        }
        INSTANCE.world = world;
        INSTANCE.setPosition(x,y,z);
        return new WeakReference<>(INSTANCE);
    }

    @Override
    public boolean isPotionApplicable(EffectInstance potioneffectIn) {
        return false;
    }

    public static void unload(IWorld world) {
        if (INSTANCE != null && INSTANCE.world == world)
            INSTANCE = null;
    }
}
