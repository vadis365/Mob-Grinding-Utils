package mob_grinding_utils;

import net.minecraft.tags.TagKey;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.tags.ITag;

public class TagLookup<T> {
    private final TagKey<T> tagKey;
    private final Lazy<ITag<T>> lazy;

    public TagLookup(IForgeRegistry<T> registry, TagKey<T> key) {
        this.tagKey = key;
        this.lazy = Lazy.of(() -> registry.tags().getTag(key));
    }

    public ITag<T> get() {
        return this.lazy.get();
    }

    public TagKey<T> getKey() {
        return this.tagKey;
    }

    public boolean contains(T entry) {
        return this.get().contains(entry);
    }

    public boolean isEmpty() {
        return this.get().isEmpty();
    }
}
