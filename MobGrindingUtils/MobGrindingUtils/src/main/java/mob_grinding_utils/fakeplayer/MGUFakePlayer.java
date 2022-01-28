package mob_grinding_utils.fakeplayer;

import java.lang.ref.WeakReference;

import com.mojang.authlib.GameProfile;

import mob_grinding_utils.Reference;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayer;

public class MGUFakePlayer extends FakePlayer {
    public MGUFakePlayer(ServerWorld world, GameProfile name) {
        super(world, name);
    }

    private static MGUFakePlayer INSTANCE;
    protected Vector3d pos = new Vector3d(0, 0, 0);
    protected BlockPos.Mutable blockPos = new BlockPos.Mutable(0, 0, 0);

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
        INSTANCE.setPosition(x,y,z);
        return new WeakReference<>(INSTANCE);
    }

    public void setOurPosition(double x, double y, double z) {
        if (pos.x != x || pos.y != y || pos.z != z) {
            pos = new Vector3d(x, y, z);
            blockPos.setPos(Math.floor(x), Math.floor(y), Math.floor(z));
        }
    }

    @Override
    public boolean isPotionApplicable(EffectInstance potioneffectIn) {
        return false;
    }

    public static void unload(IWorld world) {
        if (INSTANCE != null && INSTANCE.world == world)
            INSTANCE = null;
    }

    @Override
    public Vector3d getPositionVec() {
        return this.pos;
    }

    @Override
    public BlockPos getPosition() {
        return this.blockPos;
    }
}
