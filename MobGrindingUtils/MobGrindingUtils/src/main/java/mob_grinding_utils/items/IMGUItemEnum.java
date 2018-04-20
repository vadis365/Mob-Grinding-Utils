package mob_grinding_utils.items;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

public interface IMGUItemEnum extends IStringSerializable {

	default ItemStack createStack() {
		return createStack(1);
	}

	ItemStack createStack(int size);
}