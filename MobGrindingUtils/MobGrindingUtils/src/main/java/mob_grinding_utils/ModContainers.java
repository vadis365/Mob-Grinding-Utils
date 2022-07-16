package mob_grinding_utils;

import mob_grinding_utils.inventory.server.ContainerAbsorptionHopper;
import mob_grinding_utils.inventory.server.ContainerFan;
import mob_grinding_utils.inventory.server.ContainerMGUSpawner;
import mob_grinding_utils.inventory.server.ContainerSaw;
import mob_grinding_utils.inventory.server.ContainerXPSolidifier;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModContainers {
    public static void init(IEventBus bus) {
        CONTAINERS.register(bus);
    }
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Reference.MOD_ID);

    public static final RegistryObject<MenuType<ContainerAbsorptionHopper>> ABSORPTION_HOPPER = CONTAINERS.register("absorption_hopper_container", () -> IForgeMenuType.create(ContainerAbsorptionHopper::new));
    public static final RegistryObject<MenuType<ContainerFan>> FAN = CONTAINERS.register("fan_container", () -> IForgeMenuType.create(ContainerFan::new));
    public static final RegistryObject<MenuType<ContainerSaw>> SAW = CONTAINERS.register("saw_container", () -> IForgeMenuType.create(ContainerSaw::new));

    public static final RegistryObject<MenuType<ContainerXPSolidifier>> SOLIDIFIER = CONTAINERS.register("solidifier_container", () -> IForgeMenuType.create(ContainerXPSolidifier::new));
    public static final RegistryObject<MenuType<ContainerMGUSpawner>> ENTITY_SPAWNER = CONTAINERS.register("entity_spawner_container", () -> IForgeMenuType.create(ContainerMGUSpawner::new));
}
