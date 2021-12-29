package mob_grinding_utils.items;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import net.minecraft.world.item.Item.Properties;

public class ItemSawUpgrade extends Item  {
	public String upgradeType;

	public ItemSawUpgrade(Properties properties, String type) {
		super(properties);
		upgradeType = type;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level world, @Nonnull List<Component> list, @Nonnull TooltipFlag flag) {
		if (upgradeType.equals("sharpness"))
			list.add(new TranslatableComponent("tooltip.sawupgrade_sharpness").withStyle(ChatFormatting.YELLOW));
		if (upgradeType.equals("looting"))
			list.add(new TranslatableComponent("tooltip.sawupgrade_looting").withStyle(ChatFormatting.YELLOW));
		if (upgradeType.equals("fire"))
			list.add(new TranslatableComponent("tooltip.sawupgrade_fire").withStyle(ChatFormatting.YELLOW));
		if (upgradeType.equals("smite"))
			list.add(new TranslatableComponent("tooltip.sawupgrade_smite").withStyle(ChatFormatting.YELLOW));
		if (upgradeType.equals("arthropod"))
			list.add(new TranslatableComponent("tooltip.sawupgrade_arthropods").withStyle(ChatFormatting.YELLOW));
		if (upgradeType.equals("beheading"))
			list.add(new TranslatableComponent("tooltip.sawupgrade_beheading").withStyle(ChatFormatting.YELLOW));
	}

}