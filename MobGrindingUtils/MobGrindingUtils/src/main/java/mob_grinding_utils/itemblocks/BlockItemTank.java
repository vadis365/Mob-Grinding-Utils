package mob_grinding_utils.itemblocks;

import mob_grinding_utils.blocks.BlockTank;
import mob_grinding_utils.components.MGUComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.List;

public class BlockItemTank extends BlockItem {

	public BlockItemTank(BlockTank blockIn, int capacity, Properties builder) {
		super(blockIn, builder);
		this.capacity = capacity;
	}
	private final int capacity;

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nonnull TooltipContext context, @Nonnull List<Component> list, @Nonnull TooltipFlag flag) {
		if (stack.has(MGUComponents.FLUID)) {
			FluidStack fluid = stack.getOrDefault(MGUComponents.FLUID, FluidStack.EMPTY);
			if (fluid != null) {
				list.add(Component.literal("Contains: " + fluid.getHoverName().getString()).withStyle(ChatFormatting.GREEN));
				list.add(Component.literal(String.format("%dMb/%dMb", fluid.getAmount(),capacity)).withStyle(ChatFormatting.BLUE));
			}
		}
		else
			list.add(Component.literal(String.format("Holds %dMb (%d Buckets)", capacity, capacity / 1000)).withStyle(ChatFormatting.BLUE));
	}

}
