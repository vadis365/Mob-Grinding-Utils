package mob_grinding_utils.util;

import mob_grinding_utils.Reference;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Contract;

import javax.annotation.Nonnull;

public class RL {

    /**
     * Creates a Identifier with the specified namespace and path.
     *
     * @param namespace the namespace for the resource location
     * @param path the path for the resource location
     * @return a new Identifier instance
     */
    @Nonnull
    @Contract("_, _ -> new")
    public static Identifier rl(String namespace, String path) {
        return Identifier.fromNamespaceAndPath(namespace, path);
    }

    /**
     * Creates a Identifier with the mob_grinding_utils namespace from Reference.MOD_ID and the specified path.
     *
     * @param path the path for the resource location
     * @return a new Identifier instance
     */
    @Nonnull
    @Contract("_ -> new")
    public static Identifier mgu(String path) {
        return Identifier.fromNamespaceAndPath(Reference.MOD_ID, path);
    }

    /**
     * Creates a Identifier with the minecraft namespace and the specified path.
     *
     * @param path the path for the resource location
     * @return a new Identifier instance
     */
    @Nonnull
    @Contract("_ -> new")
    public static Identifier mc(String path) {
        return Identifier.fromNamespaceAndPath("minecraft", path);
    }
}
