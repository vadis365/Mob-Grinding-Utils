package mob_grinding_utils.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class ItemImaginaryInvisibleNotReallyThereSword extends Item {
	public ItemImaginaryInvisibleNotReallyThereSword(Properties properties) {
		// Diamond sword baseline: +3 attack damage, -2.4 attack speed
		super(properties.sword(ToolMaterial.DIAMOND, 3, -2.4F));
	}

	@Override
	public void appendHoverText(@Nonnull ItemStack stack, @Nonnull TooltipContext context, @Nonnull TooltipDisplay display, @Nonnull Consumer<Component> consumer, @Nonnull TooltipFlag flag) {
		consumer.accept(Component.literal("Nothing to see here - Move along."));
	}
}
