package mob_grinding_utils;

import mob_grinding_utils.inventory.server.ContainerAbsorptionHopper;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;

public class ModContainers {
    public static final RegistryObject<ContainerType<ContainerAbsorptionHopper>> ABSORBTION_HOPPER = MobGrindingUtils.CONTAINERS.register("absorbtion_hopper_container", () -> IForgeContainerType.create(ContainerAbsorptionHopper::new));
}
