package mob_grinding_utils.itemblocks;

import mob_grinding_utils.blocks.BlockTank;
import mob_grinding_utils.components.FluidContents;
import mob_grinding_utils.components.MGUComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class BlockItemTank extends BlockItem {

	public BlockItemTank(BlockTank blockIn, int capacity, Properties builder) {
		super(blockIn, builder);
		this.capacity = capacity;
	}
	private final int capacity;

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nonnull TooltipContext context, @Nonnull TooltipDisplay display, @Nonnull Consumer<Component> list, @Nonnull TooltipFlag flag) {
		if (stack.has(MGUComponents.FLUID)) {
			FluidStack fluid = stack.getOrDefault(MGUComponents.FLUID, FluidContents.EMPTY).get();
			if (!fluid.isEmpty()) {
				list.accept(Component.literal("Contains: " + fluid.getHoverName().getString()).withStyle(ChatFormatting.GREEN));
				list.accept(Component.literal(String.format("%dMb/%dMb", fluid.getAmount(),capacity)).withStyle(ChatFormatting.BLUE));
			}
		}
		else
			list.accept(Component.literal(String.format("Holds %dMb (%d Buckets)", capacity, capacity / 1000)).withStyle(ChatFormatting.BLUE));
	}

}
