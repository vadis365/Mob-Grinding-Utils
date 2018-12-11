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
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemMobSwab extends Item implements ISubItemsItem {

	public ItemMobSwab() {
		super();
		setMaxStackSize(1);
		setCreativeTab(MobGrindingUtils.TAB);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> list, ITooltipFlag flag) {
		if (!stack.hasTagCompound())
			list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.mobswab_1").getFormattedText());
		else if (stack.hasTagCompound() && stack.getTagCompound().hasKey("mguMobName")) {
			list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.mobswab_2").getFormattedText());
			if(stack.getTagCompound().hasKey("chickenType"))
				list.add(TextFormatting.GREEN + new TextComponentTranslation("tooltip.mobswab_3").getFormattedText()  + " " + stack.getTagCompound().getTag("chickenType") + TextFormatting.GREEN + " 'DNA'.");
			else
				list.add(TextFormatting.GREEN + new TextComponentTranslation("tooltip.mobswab_3").getFormattedText()  + " " + stack.getTagCompound().getTag("mguMobName") + TextFormatting.GREEN + " 'DNA'.");
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