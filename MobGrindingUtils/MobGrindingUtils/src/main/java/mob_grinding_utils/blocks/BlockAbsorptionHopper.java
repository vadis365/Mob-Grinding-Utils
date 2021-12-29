package mob_grinding_utils.blocks;

import mob_grinding_utils.tile.TileEntityAbsorptionHopper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class BlockAbsorptionHopper extends BaseEntityBlock {

	public static final VoxelShape HOPPER_AABB = Block.box(4D, 4D, 4D, 12D, 12D, 12D);

	public BlockAbsorptionHopper(Block.Properties properties) {
		super(properties);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityAbsorptionHopper();
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return HOPPER_AABB;
	}

	@Override
	public VoxelShape getInteractionShape(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return HOPPER_AABB;
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (world.isClientSide)
			return InteractionResult.SUCCESS;
		if (!world.isClientSide) {
			BlockEntity tile = world.getBlockEntity(pos);

			if (tile instanceof TileEntityAbsorptionHopper) {
				TileEntityAbsorptionHopper vacuum = (TileEntityAbsorptionHopper) tile;

				if (!player.isShiftKeyDown()) {
					world.sendBlockUpdated(pos, state, state, 3);
					NetworkHooks.openGui((ServerPlayer) player, (TileEntityAbsorptionHopper)vacuum, pos);
				} else {
					vacuum.toggleMode(hit.getDirection());
					world.playSound(null, pos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.3F, 0.6F);
					world.sendBlockUpdated(pos, state, state, 3);
				}
			}
		}
		return InteractionResult.SUCCESS;
	}

/* TODO
	@Nullable
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(this);
	}
*/

	@Override
	public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
		if (!world.isClientSide) {
			TileEntityAbsorptionHopper tile = (TileEntityAbsorptionHopper) world.getBlockEntity(pos);
			if (tile != null) {
				Containers.dropContents(world, pos, tile);
				world.removeBlockEntity(pos);
			}
		}
	}
}
