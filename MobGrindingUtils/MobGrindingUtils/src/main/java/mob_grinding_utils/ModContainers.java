package mob_grinding_utils;

import mob_grinding_utils.inventory.server.ContainerAbsorptionHopper;
import mob_grinding_utils.inventory.server.ContainerFan;
import mob_grinding_utils.inventory.server.ContainerSaw;
import mob_grinding_utils.inventory.server.ContainerXPSolidifier;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModContainers {
    public static void init(IEventBus bus) {
        CONTAINERS.register(bus);
    }
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Reference.MOD_ID);

    public static final RegistryObject<ContainerType<ContainerAbsorptionHopper>> ABSORPTION_HOPPER = CONTAINERS.register("absorption_hopper_container", () -> IForgeContainerType.create(ContainerAbsorptionHopper::new));
    public static final RegistryObject<ContainerType<ContainerFan>> FAN = CONTAINERS.register("fan_container", () -> IForgeContainerType.create(ContainerFan::new));
    public static final RegistryObject<ContainerType<ContainerSaw>> SAW = CONTAINERS.register("saw_container", () -> IForgeContainerType.create(ContainerSaw::new));

    public static final RegistryObject<ContainerType<ContainerXPSolidifier>> SOLIDIFIER = CONTAINERS.register("solidifier_container", () -> IForgeContainerType.create(ContainerXPSolidifier::new));
}
