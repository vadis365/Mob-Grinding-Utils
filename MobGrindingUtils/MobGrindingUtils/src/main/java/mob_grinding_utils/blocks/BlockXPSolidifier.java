package mob_grinding_utils.blocks;

import mob_grinding_utils.tile.TileEntitySaw;
import mob_grinding_utils.tile.TileEntityXPSolidifier;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class BlockXPSolidifier extends ContainerBlock {
    public BlockXPSolidifier() {
        super(AbstractBlock.Properties.create(Material.IRON, MaterialColor.GRAY));
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new TileEntityXPSolidifier();
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (world.isRemote) {
            return ActionResultType.SUCCESS;
        } else {
            TileEntity tileentity = world.getTileEntity(pos);
            if (tileentity instanceof TileEntityXPSolidifier)
                NetworkHooks.openGui((ServerPlayerEntity) player, (TileEntityXPSolidifier) tileentity, pos);
            return ActionResultType.SUCCESS;
        }
    }
}
