package mob_grinding_utils.items;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemSolidXPMould extends Item {
	public String mouldType;
	
	public ItemSolidXPMould(Properties properties, String type) {
		super(properties);
		this.mouldType = type;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(@Nonnull ItemStack stack, @Nullable World world, @Nonnull List<ITextComponent> list, @Nonnull ITooltipFlag flag) {
		if(mouldType.equals("blank"))
			list.add(new TranslationTextComponent("tooltip.solid_xp_mould_blank").mergeStyle(TextFormatting.YELLOW));
		else
			list.add(new TranslationTextComponent("tooltip.solid_xp_mould").mergeStyle(TextFormatting.YELLOW));
	}
}