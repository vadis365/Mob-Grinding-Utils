package mob_grinding_utils.items;

import mob_grinding_utils.ModBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemRottenEgg extends Item {

	public ItemRottenEgg(Properties properties) {
		super(properties);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(@Nonnull ItemStack stack, @Nonnull TooltipContext context, List<Component> list, @Nonnull TooltipFlag flag) {
		list.add(Component.translatable("tooltip.rotten_egg_1").withStyle(ChatFormatting.YELLOW));
		list.add(Component.translatable("tooltip.rotten_egg_2").withStyle(ChatFormatting.YELLOW));
	}

	@Nonnull
	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level world = context.getLevel();
		Player player = context.getPlayer();
		InteractionHand hand = context.getHand();
		BlockPos pos = context.getClickedPos();
		ItemStack stackHeld = player.getItemInHand(hand);
		if (!world.isClientSide) {
			for (int x = -2; x <= 2; x++)
				for (int z = -2; z <= 2; z++) {
					BlockState state = world.getBlockState(pos.offset(x, 0, z));
					if (state.getBlock() == Blocks.GRASS_BLOCK || state.getBlock() == Blocks.DIRT || state.getBlock() == Blocks.MYCELIUM || state.getBlock() == Blocks.FARMLAND) {
						world.levelEvent(null, 2001, pos.offset(x, 0, z), Block.getId(world.getBlockState(pos.offset(x, 0, z))));
						world.setBlock(pos.offset(x, 0, z), ModBlocks.DREADFUL_DIRT.getBlock().defaultBlockState(), 3);
						if (!player.getAbilities().instabuild)
							stackHeld.shrink(1);
					}
				}
			return InteractionResult.SUCCESS;
		}
		else
			player.swing(hand);

		return InteractionResult.PASS;
	}
}