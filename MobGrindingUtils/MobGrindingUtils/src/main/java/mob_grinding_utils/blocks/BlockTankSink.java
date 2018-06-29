package mob_grinding_utils.blocks;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.tile.TileEntitySinkTank;
import net.minecraft.block.SoundType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockTankSink extends BlockTank {
	public BlockTankSink() {
		setHardness(1.0F);
		setResistance(2000.0F);
		setSoundType(SoundType.GLASS);
		setCreativeTab(MobGrindingUtils.TAB);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntitySinkTank();
	}
}
