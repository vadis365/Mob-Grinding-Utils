package mob_grinding_utils.tile;

import mob_grinding_utils.ModBlocks;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityXPSolidifier extends TileEntity implements ITickableTileEntity {
    public TileEntityXPSolidifier() {
        super(ModBlocks.XPSOLIDIFIER_TILE);
    }

    @Override
    public void tick() {

    }
}
