package mob_grinding_utils;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;


public class MGUBlockReg<B extends Block,I extends Item, T extends BlockEntity> implements Supplier<B> {
    private String name;
    private RegistryObject<B> block;
    private RegistryObject<I> item;
    private RegistryObject<BlockEntityType<T>> tile;

    @Override
    public B get() {
        return block.get();
    }

    public String getName() {
        return name;
    }

    public MGUBlockReg(String name, Supplier<B> blockSupplier, Function<B, I> itemSupplier, BlockEntityType.BlockEntitySupplier<T> tileSupplier) {
        this.name = name;
        block = ModBlocks.BLOCKS.register(name, blockSupplier);
        item = ModItems.ITEMS.register(name, () -> itemSupplier.apply(block.get()));
        tile = ModBlocks.TILE_ENTITIES.register(name, () -> BlockEntityType.Builder.of(tileSupplier, block.get()).build(null));
    }

    public MGUBlockReg(String name, Supplier<B> blockSupplier, Function<B, I> itemSupplier) {
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
