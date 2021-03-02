package mob_grinding_utils.blocks;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

public class BlockDarkOakStone extends Block {

	public BlockDarkOakStone(Block.Properties properties) {
		super(properties);
	}

	@Nullable
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(this);
	}

}
