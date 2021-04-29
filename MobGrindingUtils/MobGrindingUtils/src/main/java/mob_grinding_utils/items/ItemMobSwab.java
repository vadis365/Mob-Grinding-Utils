package mob_grinding_utils.items;

import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import mob_grinding_utils.ModItems;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemMobSwab extends Item {
	public boolean used;
	public ItemMobSwab(Properties properties, boolean used) {
		super(properties);
		this.used = used;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(@Nonnull ItemStack stack, @Nullable World world, @Nonnull List<ITextComponent> list, @Nonnull ITooltipFlag flag) {
		if (!used)
			list.add(new TranslationTextComponent("tooltip.mobswab_1").mergeStyle(TextFormatting.YELLOW));
		else if (stack.hasTag() && Objects.requireNonNull(stack.getTag()).contains("mguMobName")) {
			list.add(new TranslationTextComponent("tooltip.mobswab_2").mergeStyle(TextFormatting.YELLOW));
			list.add(new TranslationTextComponent("tooltip.mobswab_3").mergeStyle(TextFormatting.GREEN).appendString( " " + Objects.requireNonNull(stack.getTag().get("mguMobName")).getString() + " 'DNA'."));
		}
	}

	@Nonnull
	@Override
	public ActionResultType itemInteractionForEntity(@Nonnull ItemStack stack, @Nonnull PlayerEntity player, @Nonnull LivingEntity target, @Nonnull Hand hand) {
		if (!(target instanceof PlayerEntity) && !used) {
				String mobName = Objects.requireNonNull(target.getType().getRegistryName()).toString();
				ItemStack stack2 = new ItemStack(ModItems.MOB_SWAB_USED.get(), 1);
				if (!stack2.hasTag())
					stack2.setTag(new CompoundNBT());
				if (!Objects.requireNonNull(stack2.getTag()).contains("mguMobName")) {
					stack2.getTag().putString("mguMobName", mobName);
					CompoundNBT nbt = new CompoundNBT();
					target.writeAdditional(nbt);
				}
			player.setHeldItem(hand, stack2);
		    return ActionResultType.SUCCESS;
		} else {
		      return ActionResultType.PASS;
		}
	}

}