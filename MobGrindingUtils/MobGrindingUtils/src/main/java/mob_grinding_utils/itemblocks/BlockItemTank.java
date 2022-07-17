package mob_grinding_utils.itemblocks;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class BlockItemTank extends BlockItem {

	public BlockItemTank(Block blockIn, Properties builder) {
		super(blockIn, builder);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level world, @Nonnull List<Component> list, @Nonnull TooltipFlag flag) {
		if (stack.hasTag() && !stack.getTag().contains("Empty")) {
			FluidStack fluid = FluidStack.loadFluidStackFromNBT(stack.getTag());
			if (fluid != null) {
				list.add(Component.literal("Contains: " + fluid.getDisplayName().getString()).withStyle(ChatFormatting.GREEN));
				list.add(Component.literal("" + fluid.getAmount() + "Mb/32000Mb").withStyle(ChatFormatting.BLUE));
			}
		}
		else
			list.add(Component.literal("Holds 32000Mb (32 Buckets)").withStyle(ChatFormatting.BLUE));
	}

}
