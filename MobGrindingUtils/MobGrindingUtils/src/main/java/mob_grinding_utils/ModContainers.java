package mob_grinding_utils;

import mob_grinding_utils.inventory.server.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModContainers {
    public static void init(IEventBus bus) {
        CONTAINERS.register(bus);
    }
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(BuiltInRegistries.MENU, Reference.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<ContainerAbsorptionHopper>> ABSORPTION_HOPPER = CONTAINERS.register("absorption_hopper_container", () -> IMenuTypeExtension.create(ContainerAbsorptionHopper::new));
    public static final DeferredHolder<MenuType<?>, MenuType<ContainerFan>> FAN = CONTAINERS.register("fan_container", () -> IMenuTypeExtension.create(ContainerFan::new));
    public static final DeferredHolder<MenuType<?>, MenuType<ContainerSaw>> SAW = CONTAINERS.register("saw_container", () -> IMenuTypeExtension.create(ContainerSaw::new));

    public static final DeferredHolder<MenuType<?>, MenuType<ContainerXPSolidifier>> SOLIDIFIER = CONTAINERS.register("solidifier_container", () -> IMenuTypeExtension.create(ContainerXPSolidifier::new));
    public static final DeferredHolder<MenuType<?>, MenuType<ContainerMGUSpawner>> ENTITY_SPAWNER = CONTAINERS.register("entity_spawner_container", () -> IMenuTypeExtension.create(ContainerMGUSpawner::new));
}
