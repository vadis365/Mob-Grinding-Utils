package mob_grinding_utils.itemblocks;

import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.List;

public class MGUBlockItem extends BlockItem {
    public MGUBlockItem(Block blockIn, Properties builder) {
        super(blockIn, builder);
        block = blockIn;
    }
    private final Block block;

    private boolean addTooltipLine(int line, List<Component> tooltip) {
        if (I18n.exists(block.getDescriptionId() + ".tooltip_" + line)) {
            tooltip.add(Component.translatable(block.getDescriptionId() + ".tooltip_" + line).withStyle(ChatFormatting.YELLOW));
            return true;
        }
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nonnull TooltipContext context, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flagIn) {
        super.appendHoverText(stack, context, tooltip, flagIn);
        for (int i = 1; i <= 6; i++) {
            if (!addTooltipLine(i, tooltip)) {
                break;
            }
        }
    }
}
