package mob_grinding_utils;

import mob_grinding_utils.inventory.server.ContainerAbsorptionHopper;
import mob_grinding_utils.inventory.server.ContainerFan;
import mob_grinding_utils.inventory.server.ContainerSaw;
import mob_grinding_utils.inventory.server.ContainerXPSolidifier;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;

public class ModContainers {
    public static final RegistryObject<ContainerType<ContainerAbsorptionHopper>> ABSORPTION_HOPPER = MobGrindingUtils.CONTAINERS.register("absorption_hopper_container", () -> IForgeContainerType.create(ContainerAbsorptionHopper::new));
    public static final RegistryObject<ContainerType<ContainerFan>> FAN = MobGrindingUtils.CONTAINERS.register("fan_container", () -> IForgeContainerType.create(ContainerFan::new));
    public static final RegistryObject<ContainerType<ContainerSaw>> SAW = MobGrindingUtils.CONTAINERS.register("saw_container", () -> IForgeContainerType.create(ContainerSaw::new));

    public static final RegistryObject<ContainerType<ContainerXPSolidifier>> SOLIDIFIER = MobGrindingUtils.CONTAINERS.register("solidifier_container", () -> IForgeContainerType.create(ContainerXPSolidifier::new));
}
