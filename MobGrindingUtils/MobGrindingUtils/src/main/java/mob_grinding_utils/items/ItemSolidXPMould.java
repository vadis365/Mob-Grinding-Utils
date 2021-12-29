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

public class ItemSolidXPMould extends Item {
	public String mouldType;
	
	public ItemSolidXPMould(Properties properties, String type) {
		super(properties);
		this.mouldType = type;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level world, @Nonnull List<Component> list, @Nonnull TooltipFlag flag) {
		if(mouldType.equals("blank"))
			list.add(new TranslatableComponent("tooltip.solid_xp_mould_blank").withStyle(ChatFormatting.YELLOW));
		else
			list.add(new TranslatableComponent("tooltip.solid_xp_mould").withStyle(ChatFormatting.YELLOW));
	}
}