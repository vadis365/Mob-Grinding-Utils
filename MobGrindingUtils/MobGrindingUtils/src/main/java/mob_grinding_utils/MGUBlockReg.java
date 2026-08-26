package mob_grinding_utils;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;


public class MGUBlockReg<B extends Block,I extends Item, T extends BlockEntity> implements Supplier<B> {
    private final String name;
    private final DeferredBlock<B> block;
    private final DeferredItem<I> item;
    private Supplier<BlockEntityType<T>> tile;

    @Override
    public B get() {
        return block.get();
    }

    public String getName() {
        return name;
    }

    public MGUBlockReg(String name, Function<Identifier, B> blockSupplier, Function<B, I> itemSupplier, BlockEntityType.BlockEntitySupplier<T> tileSupplier) {
        this.name = name;
        block = ModBlocks.BLOCKS.register(name, blockSupplier);
        item = ModItems.ITEMS.register(name, () -> itemSupplier.apply(block.get()));
        tile = ModBlocks.TILE_ENTITIES.register(name, () -> new BlockEntityType<>(tileSupplier, block.get()));
    }

    public MGUBlockReg(String name, Function<Identifier, B> blockSupplier, Function<B, I> itemSupplier) {
        this.name = name;
        block = ModBlocks.BLOCKS.register(name, blockSupplier);
        item = ModItems.ITEMS.register(name, () -> itemSupplier.apply(block.get()));
    }

    @Nonnull
    public B getBlock() {
        return block.get();
    }

    @Nonnull
    public I getItem() {
        return item.get();
    }

    @Nonnull
    public BlockEntityType<T> getTileEntityType() {
        //just in case...
        return Objects.requireNonNull(tile).get();
    }


}
