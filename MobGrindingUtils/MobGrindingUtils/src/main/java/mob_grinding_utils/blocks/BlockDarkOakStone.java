package mob_grinding_utils.blocks;

import java.util.Random;

import javax.annotation.Nullable;

import mob_grinding_utils.MobGrindingUtils;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

public class BlockDarkOakStone extends Block {

	public BlockDarkOakStone() {
		super(Material.ROCK);
		setHardness(1.5F);
		setResistance(10.0F);
		setSoundType(SoundType.STONE);
		setLightLevel(0.5F);
		setCreativeTab(MobGrindingUtils.TAB);
	}

	@Nullable
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(this);
	}

}
