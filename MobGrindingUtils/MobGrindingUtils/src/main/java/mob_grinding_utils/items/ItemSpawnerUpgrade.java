package mob_grinding_utils.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

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
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(@Nonnull ItemStack stack, @Nonnull TooltipContext context, @Nonnull List<Component> list, @Nonnull TooltipFlag flag) {
		switch (upgradeType) {
			case WIDTH -> list.add(Component.translatable("tooltip.spawner_upgrade_width").withStyle(ChatFormatting.YELLOW));
			case HEIGHT -> list.add(Component.translatable("tooltip.spawner_upgrade_height").withStyle(ChatFormatting.YELLOW));
		}
	}

}