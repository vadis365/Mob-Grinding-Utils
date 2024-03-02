package mob_grinding_utils.items;

import mob_grinding_utils.config.ServerConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
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
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level world, @Nonnull List<Component> list, @Nonnull TooltipFlag flag) {
		switch (upgradeType) {
			case SHARPNESS -> list.add(Component.translatable("tooltip.sawupgrade_sharpness", ServerConfig.MASHER_MAX_UPGRADES.get()).withStyle(ChatFormatting.YELLOW));
			case LOOTING -> list.add(Component.translatable("tooltip.sawupgrade_looting", ServerConfig.MASHER_MAX_UPGRADES.get()).withStyle(ChatFormatting.YELLOW));
			case FIRE -> list.add(Component.translatable("tooltip.sawupgrade_fire", ServerConfig.MASHER_MAX_UPGRADES.get()).withStyle(ChatFormatting.YELLOW));
			case SMITE -> list.add(Component.translatable("tooltip.sawupgrade_smite", ServerConfig.MASHER_MAX_UPGRADES.get()).withStyle(ChatFormatting.YELLOW));
			case ARTHROPOD -> list.add(Component.translatable("tooltip.sawupgrade_arthropods", ServerConfig.MASHER_MAX_UPGRADES.get()).withStyle(ChatFormatting.YELLOW));
			case BEHEADING -> list.add(Component.translatable("tooltip.sawupgrade_beheading", ServerConfig.MASHER_MAX_UPGRADES.get()).withStyle(ChatFormatting.YELLOW));
		}
	}

}