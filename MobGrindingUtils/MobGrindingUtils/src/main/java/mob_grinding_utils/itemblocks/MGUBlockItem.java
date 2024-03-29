package mob_grinding_utils.itemblocks;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.world.level.block.Block;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import net.minecraft.world.item.Item.Properties;

public class MGUBlockItem extends BlockItem {
    public MGUBlockItem(Block blockIn, Properties builder) {
        super(blockIn, builder);
        block = blockIn;
    }
    private final Block block;

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level worldIn, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (I18n.exists(block.getDescriptionId() + ".tooltip_1")) {
            tooltip.add(new TranslatableComponent(block.getDescriptionId() + ".tooltip_1").withStyle(ChatFormatting.YELLOW));
            if (I18n.exists(block.getDescriptionId() + ".tooltip_2")) {
                tooltip.add(new TranslatableComponent(block.getDescriptionId() + ".tooltip_2").withStyle(ChatFormatting.YELLOW));
                if (I18n.exists(block.getDescriptionId() + ".tooltip_3")) {
                    tooltip.add(new TranslatableComponent(block.getDescriptionId() + ".tooltip_3").withStyle(ChatFormatting.YELLOW));
                    if (I18n.exists(block.getDescriptionId() + ".tooltip_4")) {
                        tooltip.add(new TranslatableComponent(block.getDescriptionId() + ".tooltip_4").withStyle(ChatFormatting.YELLOW));
                        if (I18n.exists(block.getDescriptionId() + ".tooltip_5")) {
                            tooltip.add(new TranslatableComponent(block.getDescriptionId() + ".tooltip_5").withStyle(ChatFormatting.YELLOW));
                            if (I18n.exists(block.getDescriptionId() + ".tooltip_6")) {
                                tooltip.add(new TranslatableComponent(block.getDescriptionId() + ".tooltip_6").withStyle(ChatFormatting.YELLOW));
                            }
                        }
                    }
                }
            }
        }
    }
}
