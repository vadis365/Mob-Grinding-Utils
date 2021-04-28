package mob_grinding_utils;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class MGUBlockReg<B extends Block,I extends Item, T extends TileEntity> implements Supplier<B> {
    private final String name;

    private RegistryObject<B> block;
    private RegistryObject<I> item;
    private RegistryObject<TileEntityType<T>> tile;

    private final Supplier<B> blockSupplier;
    private final Supplier<T> tileSupplier;
    private final Function<B, I> ItemSupplier;

    @Override
    public B get() {
        return block.get();
    }

    public String getName() {
        return name;
    }

    public MGUBlockReg(String name, Supplier<B> blockSupplier, Function<B, I> itemSupplier, Supplier<T> tileSupplier) {
        this.name = name;
        this.blockSupplier = blockSupplier;
        this.tileSupplier = tileSupplier;
        ItemSupplier = itemSupplier;
    }

    public MGUBlockReg(String name, Supplier<B> blockSupplier, Function<B, I> itemSupplier) {
        this.name = name;
        this.blockSupplier = blockSupplier;
        ItemSupplier = itemSupplier;
        this.tileSupplier = null;
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
