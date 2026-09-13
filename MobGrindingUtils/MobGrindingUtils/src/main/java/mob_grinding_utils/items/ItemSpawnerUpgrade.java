package mob_grinding_utils.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemSpawnerUpgrade extends Item {
	public enum SpawnerUpgrade {
		WIDTH, HEIGHT
	}
	
	public SpawnerUpgrade upgradeType;

	public ItemSpawnerUpgrade(Properties properties, SpawnerUpgrade type) {
		super(properties);
		upgradeType = type;
	}

	@Override
	public void appendHoverText(@Nonnull ItemStack stack, @Nonnull TooltipContext context, @Nonnull TooltipDisplay display, @Nonnull Consumer<Component> builder, @Nonnull TooltipFlag tooltipFlag) {
		switch (upgradeType) {
			case WIDTH -> builder.accept(Component.translatable("tooltip.spawner_upgrade_width").withStyle(ChatFormatting.YELLOW));
			case HEIGHT -> builder.accept(Component.translatable("tooltip.spawner_upgrade_height").withStyle(ChatFormatting.YELLOW));
		}
	}

}