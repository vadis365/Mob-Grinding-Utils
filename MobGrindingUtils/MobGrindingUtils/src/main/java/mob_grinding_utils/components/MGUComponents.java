package mob_grinding_utils.components;

import mob_grinding_utils.Reference;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MGUComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES = DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, Reference.MOD_ID);



    public static void init(IEventBus bus) {
        DATA_COMPONENT_TYPES.register(bus);
    }
}
