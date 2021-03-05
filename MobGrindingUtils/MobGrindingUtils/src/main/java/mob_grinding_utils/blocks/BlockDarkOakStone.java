package mob_grinding_utils.blocks;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;

public class BlockDarkOakStone extends Block {

	public BlockDarkOakStone(Block.Properties properties) {
		super(properties);
	}

	@Nullable
	public Item getItemDropped(BlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(this);
	}

}
