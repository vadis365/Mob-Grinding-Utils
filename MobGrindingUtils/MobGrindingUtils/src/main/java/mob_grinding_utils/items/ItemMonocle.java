package mob_grinding_utils.items;

import java.util.List;

import javax.annotation.Nullable;

import mob_grinding_utils.ModItems;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemMonocle extends ArmorItem {

	public ItemMonocle(ArmorMaterial material, EquipmentSlotType slot, Properties properties) {
		super(material, slot, properties);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	   public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		tooltip.add(new TranslationTextComponent("tooltip.monocle").mergeStyle(TextFormatting.YELLOW));//applyTextStyle
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public String getArmorTexture(ItemStack is, Entity entity, EquipmentSlotType slot, String type) {
		if (is.getItem() == ModItems.MONOCLE.get())
			return "mob_grinding_utils:textures/items/monocle_armour.png";
		else
			return null;
	}

	@Override
	public boolean getIsRepairable(ItemStack armour, ItemStack material) {
		return material.getItem() == Items.IRON_INGOT;
	}
}