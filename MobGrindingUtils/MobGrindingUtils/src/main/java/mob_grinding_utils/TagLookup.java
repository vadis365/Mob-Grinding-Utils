package mob_grinding_utils;

import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.common.util.Lazy;

public class TagLookup<T> {
    private final TagKey<T> tagKey;
    private final Lazy<Iterable<net.minecraft.core.Holder<T>>> lazy;

    public TagLookup(Registry<T> registry, TagKey<T> key) {
        this.tagKey = key;
		this.lazy = Lazy.of(() -> registry.getTagOrEmpty(key));
    }

    public Iterable<net.minecraft.core.Holder<T>> get() {
        return this.lazy.get();
    }

    public TagKey<T> getKey() {
        return this.tagKey;
    }

    public boolean contains(T entry) {
        for (var holder : get()) if (holder.value() == entry) return true;
        return false;
    }

    public boolean isEmpty() {
        return !this.get().iterator().hasNext();
    }
}
