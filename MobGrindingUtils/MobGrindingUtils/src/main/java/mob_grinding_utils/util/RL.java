package mob_grinding_utils.util;

import mob_grinding_utils.Reference;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Contract;

import javax.annotation.Nonnull;

public class RL {

    /**
     * Creates a ResourceLocation with the specified namespace and path.
     *
     * @param namespace the namespace for the resource location
     * @param path the path for the resource location
     * @return a new ResourceLocation instance
     */
    @Nonnull
    @Contract("_, _ -> new")
    public static ResourceLocation rl(String namespace, String path) {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
    }

    /**
     * Creates a ResourceLocation with the mob_grinding_utils namespace from Reference.MOD_ID and the specified path.
     *
     * @param path the path for the resource location
     * @return a new ResourceLocation instance
     */
    @Nonnull
    @Contract("_ -> new")
    public static ResourceLocation mgu(String path) {
        return ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, path);
    }

    /**
     * Creates a ResourceLocation with the minecraft namespace and the specified path.
     *
     * @param path the path for the resource location
     * @return a new ResourceLocation instance
     */
    @Nonnull
    @Contract("_ -> new")
    public static ResourceLocation mc(String path) {
        return ResourceLocation.fromNamespaceAndPath("minecraft", path);
    }
}
