package mob_grinding_utils.util;

import mob_grinding_utils.Reference;
import net.minecraft.resources.ResourceLocation;

public class RL {
    public static ResourceLocation rl(String namespace, String path) {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
    }

    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, path);
    }
}
