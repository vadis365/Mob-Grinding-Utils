package mob_grinding_utils.tile;

import mob_grinding_utils.ModBlocks;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class TileEntityJumboTank extends TileEntityTank {

	public TileEntityJumboTank() {
		super(ModBlocks.JUMBO_TANK.getTileEntityType(), new FluidTank(FluidAttributes.BUCKET_VOLUME *  1024));
	}
}
