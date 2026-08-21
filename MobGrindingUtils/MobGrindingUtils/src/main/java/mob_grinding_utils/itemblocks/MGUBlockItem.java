package mob_grinding_utils.itemblocks;

import mob_grinding_utils.components.FluidContents;
import mob_grinding_utils.components.MGUComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class MGUBlockItem extends BlockItem {
    public MGUBlockItem(Block blockIn, Properties builder) {
        super(blockIn, builder);
        block = blockIn;
    }
    private final Block block;

    private boolean addTooltipLine(int line, Consumer<Component> tooltip) {
        if (I18n.exists(block.getDescriptionId() + ".tooltip_" + line)) {
            tooltip.accept(Component.translatable(block.getDescriptionId() + ".tooltip_" + line).withStyle(ChatFormatting.YELLOW));
            return true;
        }
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nonnull TooltipContext context, @Nonnull TooltipDisplay display, @Nonnull Consumer<Component> tooltip, @Nonnull TooltipFlag flagIn) {
        super.appendHoverText(stack, context, display, tooltip, flagIn);
        for (int i = 1; i <= 6; i++) {
            if (!addTooltipLine(i, tooltip)) {
                break;
            }
        }
        FluidStack fluid = stack.getOrDefault(MGUComponents.FLUID, FluidContents.EMPTY).get();
        if (!fluid.isEmpty()) {
            tooltip.accept(Component.literal("Contains: " + fluid.getHoverName().getString()).withStyle(ChatFormatting.GREEN));
            tooltip.accept(Component.literal(String.format("%dMb/%dMb", fluid.getAmount(), 16000)).withStyle(ChatFormatting.BLUE));
        }
    }
}
