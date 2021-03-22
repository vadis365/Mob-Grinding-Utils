package mob_grinding_utils.itemblocks;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;

public class BlockItemTankSink extends BlockItem {

	public BlockItemTankSink(Block blockIn, Properties builder) {
		super(blockIn, builder);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	   public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
		
		if(stack.hasTag() && !stack.getTag().contains("Empty")) {
			FluidStack fluid = FluidStack.loadFluidStackFromNBT(stack.getTag());
			if(fluid !=null) {
				list.add(new TranslationTextComponent("Contains: "+ fluid.getDisplayName().getString()).mergeStyle(TextFormatting.GREEN));
				list.add(new TranslationTextComponent(""+ fluid.getAmount() +"Mb/32000Mb").mergeStyle(TextFormatting.BLUE));
			}
		}
		else
			list.add(new TranslationTextComponent("Holds 32000Mb (32 Buckets)").mergeStyle(TextFormatting.BLUE));
	}

}
