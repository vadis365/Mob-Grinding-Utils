package mob_grinding_utils;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;

public class MGUBlockReg<B extends Block,I extends Item, T extends TileEntity> implements Supplier<B> {
    private String name;
    private RegistryObject<B> block;
    private RegistryObject<I> item;
    private RegistryObject<TileEntityType<T>> tile;

    @Override
    public B get() {
        return block.get();
    }

    public String getName() {
        return name;
    }

    public MGUBlockReg(String name, Supplier<B> blockSupplier, Function<B, I> itemSupplier, Supplier<T> tileSupplier) {
        this.name = name;
        block = ModBlocks.BLOCKS.register(name, blockSupplier);
        item = ModItems.ITEMS.register(name, () -> itemSupplier.apply(block.get()));
        tile = ModBlocks.TILE_ENTITIES.register(name, () -> TileEntityType.Builder.create(tileSupplier, block.get()).build(null));
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
    public TileEntityType<T> getTileEntityType() {
        //just in case...
        return Objects.requireNonNull(tile).get();
    }


}
