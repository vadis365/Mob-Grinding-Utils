package mob_grinding_utils.blocks;

import mob_grinding_utils.tile.TileEntityJumboTank;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class BlockTankJumbo extends BlockTank {
	public BlockTankJumbo(Block.Properties properties) {
		super(properties);
	}

	@Override
	public TileEntity createNewTileEntity(IBlockReader world) {
		return new TileEntityJumboTank();
	}
}
