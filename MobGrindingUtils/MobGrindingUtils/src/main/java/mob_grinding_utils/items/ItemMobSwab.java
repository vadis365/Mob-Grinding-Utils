package mob_grinding_utils.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nullable;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.ModItems;
import mob_grinding_utils.ModItems.ISubItemsItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemMobSwab extends Item implements ISubItemsItem {

	public ItemMobSwab(Properties properties) {
		super(properties);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
		if (!stack.hasTag())
			list.add(new TranslationTextComponent("tooltip.mobswab_1").mergeStyle(TextFormatting.YELLOW));
		else if (stack.hasTag() && stack.getTag().contains("mguMobName")) {
			list.add(new TranslationTextComponent("tooltip.mobswab_2").mergeStyle(TextFormatting.YELLOW));
			if(stack.getTag().contains("chickenType"))
				list.add(new TranslationTextComponent("tooltip.mobswab_3").mergeStyle(TextFormatting.GREEN).appendString(" " + stack.getTag().get("chickenType").getString() + " 'DNA'."));
			else
				list.add(new TranslationTextComponent("tooltip.mobswab_3").mergeStyle(TextFormatting.GREEN).appendString( " " + stack.getTag().get("mguMobName").getString() + " 'DNA'."));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
		if (tab == MobGrindingUtils.TAB) {
			list.add(new ItemStack(this, 1, 0));
		}
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
		if (target instanceof EntityLiving && !(target instanceof EntityPlayer) && getDamage(stack) == 0) {
			String mobName = EntityList.getKey(target).toString();
			ItemStack stack2 = new ItemStack(ModItems.MOB_SWAB, 1, 1);
			if (!stack2.hasTagCompound())
				stack2.setTagCompound(new NBTTagCompound());
			if (!stack2.getTagCompound().hasKey("mguMobName")) {
				stack2.getTagCompound().setString("mguMobName", mobName);
				NBTTagCompound nbt = new NBTTagCompound();
				target.writeEntityToNBT(nbt);
				if (Loader.isModLoaded("chickens")) {
					if (target instanceof EntityChicken && nbt.hasKey("Type"))
						stack2.getTagCompound().setString("chickenType", nbt.getString("Type"));
				}
			}
			player.swingArm(hand);
			player.setHeldItem(hand, stack2);
			return true;
		} else {
			return super.itemInteractionForEntity(stack, player, target, hand);
		}
	}
	
	@Override
	public List<String> getModels() {
		List<String> models = new ArrayList<String>();
		for (EnumSwabType type : EnumSwabType.values())
			models.add(type.getName());
		return models;
	}

	public enum EnumSwabType implements IMGUItemEnum {
		MOB_SWAB,
		MOB_SWAB_USED;

		@Override
		public String getName() {
			return name().toLowerCase(Locale.ENGLISH);
		}

		@Override
		public ItemStack createStack(int size) {
			return new ItemStack(ModItems.MOB_SWAB, size, ordinal());
		}
	}
}