package mob_grinding_utils;

import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.common.util.Lazy;

public class TagLookup<T> {
    private final TagKey<T> tagKey;
    private final Lazy<HolderSet.Named<T>> lazy;

    public TagLookup(Registry<T> registry, TagKey<T> key) {
        this.tagKey = key;
        this.lazy = Lazy.of(() -> registry.getOrCreateTag(key));
    }

    public HolderSet.Named<T> get() {
        return this.lazy.get();
    }

    public TagKey<T> getKey() {
        return this.tagKey;
    }

    public boolean contains(T entry) {
        return this.get().stream().anyMatch($ -> $.value() == entry);
    }

    public boolean isEmpty() {
        return this.get().stream().findAny().isPresent();
    }
}
