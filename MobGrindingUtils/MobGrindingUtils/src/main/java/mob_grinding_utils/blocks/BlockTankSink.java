package mob_grinding_utils.blocks;

import mob_grinding_utils.tile.TileEntitySinkTank;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockTankSink extends BlockTank {
	public BlockTankSink(Block.Properties properties) {
		super(properties);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntitySinkTank();
	}
}
