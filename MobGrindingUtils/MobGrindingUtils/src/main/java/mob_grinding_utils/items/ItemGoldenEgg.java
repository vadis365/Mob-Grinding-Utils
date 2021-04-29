package mob_grinding_utils.items;

import java.util.List;

import javax.annotation.Nullable;

import mob_grinding_utils.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemGoldenEgg extends Item {

	public ItemGoldenEgg(Properties properties) {
		super(properties);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
		list.add(new TranslationTextComponent("tooltip.golden_egg_1").mergeStyle(TextFormatting.YELLOW));
		list.add(new TranslationTextComponent("tooltip.golden_egg_2").mergeStyle(TextFormatting.YELLOW));
	}

	@Override
	   public ActionResultType onItemUse(ItemUseContext context) {
		World world = context.getWorld();
		PlayerEntity player = context.getPlayer();
		Hand hand = context.getHand();
		BlockPos pos = context.getPos();
		ItemStack stackHeld = player.getHeldItem(hand);
		if (!world.isRemote) {
			for (int x = -2; x <= 2; x++)
				for (int z = -2; z <= 2; z++) {
					BlockState state = world.getBlockState(pos.add(x, 0, z));
					if (state.getBlock() == Blocks.GRASS_BLOCK || state.getBlock() == Blocks.DIRT || state.getBlock() == Blocks.MYCELIUM || state.getBlock() == Blocks.FARMLAND) {
						world.playEvent(null, 2001, pos.add(x, 0, z), Block.getStateId(world.getBlockState(pos.add(x, 0, z))));
						world.setBlockState(pos.add(x, 0, z), ModBlocks.DELIGHTFUL_DIRT.getBlock().getDefaultState(), 3);
						if (!player.abilities.isCreativeMode)
							stackHeld.shrink(1);
					}
				}
			return ActionResultType.SUCCESS;
		}
		else
			player.swingArm(hand);

		return ActionResultType.PASS;
	}
}