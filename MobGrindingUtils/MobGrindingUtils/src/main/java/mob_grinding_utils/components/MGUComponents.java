package mob_grinding_utils.components;

import com.mojang.serialization.Codec;
import mob_grinding_utils.Reference;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MGUComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES = DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, Reference.MOD_ID);

    public static final Supplier<DataComponentType<ResourceLocation>> MOB_DNA = DATA_COMPONENT_TYPES.register("mob_dna", () ->
            DataComponentType.<ResourceLocation>builder()
                    .persistent(ResourceLocation.CODEC)
                    .networkSynchronized(ResourceLocation.STREAM_CODEC)
                    .build());

    public static final Supplier<DataComponentType<FluidContents>> FLUID = DATA_COMPONENT_TYPES.register("fluid", () ->
            DataComponentType.<FluidContents>builder()
                    .persistent(FluidContents.CODEC)
                    .networkSynchronized(FluidContents.STREAM_CODEC)
                    .build());

    public static final Supplier<DataComponentType<Integer>> BEHEADING = DATA_COMPONENT_TYPES.register("beheading", () ->
            DataComponentType.<Integer>builder()
                    .persistent(Codec.INT)
                    .networkSynchronized(ByteBufCodecs.INT)
                    .build());

    public static void init(IEventBus bus) {
        DATA_COMPONENT_TYPES.register(bus);
    }
}
