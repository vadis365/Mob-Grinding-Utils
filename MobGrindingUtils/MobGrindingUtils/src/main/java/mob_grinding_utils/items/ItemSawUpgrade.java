package mob_grinding_utils.items;

import mob_grinding_utils.config.ServerConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class ItemSawUpgrade extends Item  {
	public enum SawUpgradeType {
		SHARPNESS,
		LOOTING,
		FIRE,
		SMITE,
		ARTHROPOD,
		BEHEADING
	}
	public SawUpgradeType upgradeType;

	public ItemSawUpgrade(Properties properties, SawUpgradeType type) {
		super(properties);
		upgradeType = type;
	}

	@Override
	public void appendHoverText(@Nonnull ItemStack stack, @Nonnull TooltipContext context, @Nonnull TooltipDisplay display, @Nonnull Consumer<Component> builder, @Nonnull TooltipFlag tooltipFlag) {
		switch (upgradeType) {
			case SHARPNESS -> builder.accept(Component.translatable("tooltip.sawupgrade_sharpness", ServerConfig.MASHER_MAX_UPGRADES.get()).withStyle(ChatFormatting.YELLOW));
			case LOOTING -> builder.accept(Component.translatable("tooltip.sawupgrade_looting", ServerConfig.MASHER_MAX_UPGRADES.get()).withStyle(ChatFormatting.YELLOW));
			case FIRE -> builder.accept(Component.translatable("tooltip.sawupgrade_fire", ServerConfig.MASHER_MAX_UPGRADES.get()).withStyle(ChatFormatting.YELLOW));
			case SMITE -> builder.accept(Component.translatable("tooltip.sawupgrade_smite", ServerConfig.MASHER_MAX_UPGRADES.get()).withStyle(ChatFormatting.YELLOW));
			case ARTHROPOD -> builder.accept(Component.translatable("tooltip.sawupgrade_arthropods", ServerConfig.MASHER_MAX_UPGRADES.get()).withStyle(ChatFormatting.YELLOW));
			case BEHEADING -> builder.accept(Component.translatable("tooltip.sawupgrade_beheading", ServerConfig.MASHER_MAX_UPGRADES.get()).withStyle(ChatFormatting.YELLOW));
		}
	}

}
