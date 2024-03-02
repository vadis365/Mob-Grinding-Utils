package mob_grinding_utils.events;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.common.NeoForge;

public class DirtSpawnEvent extends Event {
    private final LevelAccessor world;
    private final double x;
    private final double y;
    private final double z;
    private final LivingEntity entityLiving;

    public enum DirtType {
        DREADFUL,
        DELIGHTFUL
    }

    private final DirtType dirt;

    public DirtSpawnEvent(LevelAccessor world, double x, double y, double z, LivingEntity mob, DirtType type) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.entityLiving = mob;
        this.dirt = type;
    }

    public LevelAccessor getWorld() {
        return world;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public DirtType getDirt() {
        return dirt;
    }

    public LivingEntity getEntityLiving() {
        return entityLiving;
    }

    public static Result checkEvent(Mob entity, LevelAccessor level, double x, double y, double z, DirtType type) {
        if (entity == null)
            return Result.DEFAULT;
        DirtSpawnEvent event = new DirtSpawnEvent(level, x, y, z, entity, type);
        NeoForge.EVENT_BUS.post(event);

        return event.getResult();
    }
}
