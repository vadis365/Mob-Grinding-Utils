package mob_grinding_utils.items;

import java.util.List;

import javax.annotation.Nullable;

import mob_grinding_utils.ModItems;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import net.minecraft.world.item.Item.Properties;

public class ItemMonocle extends ArmorItem {

	public ItemMonocle(ArmorMaterials material, EquipmentSlot slot, Properties properties) {
		super(material, slot, properties);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	   public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		tooltip.add(new TranslatableComponent("tooltip.monocle").withStyle(ChatFormatting.YELLOW));//applyTextStyle
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public String getArmorTexture(ItemStack is, Entity entity, EquipmentSlot slot, String type) {
		if (is.getItem() == ModItems.MONOCLE.get())
			return "mob_grinding_utils:textures/items/monocle_armour.png";
		else
			return null;
	}

	@Override
	public boolean isValidRepairItem(ItemStack armour, ItemStack material) {
		return material.getItem() == Items.IRON_INGOT;
	}
}