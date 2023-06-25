package mob_grinding_utils.itemblocks;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;

import net.minecraft.world.item.Item.Properties;

public class BlockItemTank extends BlockItem {

	public BlockItemTank(Block blockIn, Properties builder) {
		super(blockIn, builder);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> list, TooltipFlag flag) {
		if (stack.hasTag() && !stack.getTag().contains("Empty")) {
			FluidStack fluid = FluidStack.loadFluidStackFromNBT(stack.getTag());
			if (fluid != null) {
				list.add(new TranslatableComponent("Contains: " + fluid.getDisplayName().getString()).withStyle(ChatFormatting.GREEN));
				list.add(new TranslatableComponent("" + fluid.getAmount() + "/32000 mB").withStyle(ChatFormatting.BLUE));
			}
		}
		else
			list.add(new TranslatableComponent("Holds 32000 mB (32 Buckets)").withStyle(ChatFormatting.BLUE));
	}

}
