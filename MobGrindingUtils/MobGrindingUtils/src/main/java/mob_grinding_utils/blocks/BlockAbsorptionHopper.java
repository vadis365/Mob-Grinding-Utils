package mob_grinding_utils.blocks;

import com.mojang.serialization.MapCodec;
import mob_grinding_utils.tile.TileEntityAbsorptionHopper;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockAbsorptionHopper extends BaseEntityBlock {

	public static final VoxelShape HOPPER_AABB = Block.box(4D, 4D, 4D, 12D, 12D, 12D);
	public static final MapCodec<BlockAbsorptionHopper> CODEC = simpleCodec(BlockAbsorptionHopper::new);

	public BlockAbsorptionHopper(Block.Properties properties) {
		super(properties);
	}

	@Nonnull
	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityAbsorptionHopper(pos, state);
	}

	@Nonnull
	@Override
	public RenderShape getRenderShape(@Nonnull BlockState state) {
		return RenderShape.MODEL;
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, @Nonnull BlockState pState, @Nonnull BlockEntityType<T> pBlockEntityType) {
		return pLevel.isClientSide ? null : TileEntityAbsorptionHopper::serverTick;
	}

	@Nonnull
	@Override
	public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return HOPPER_AABB;
	}

	@Nonnull
	@Override
	public VoxelShape getInteractionShape(@Nonnull BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos) {
		return HOPPER_AABB;
	}

	@Nonnull
	@Override
	public InteractionResult useWithoutItem(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull BlockHitResult hitResult) {
		if (!world.isClientSide) {
			BlockEntity tile = world.getBlockEntity(pos);

			if (tile instanceof TileEntityAbsorptionHopper vacuum) {

				if (!player.isShiftKeyDown()) {
					world.sendBlockUpdated(pos, state, state, 3);
					player.openMenu(vacuum, pos);
				} else {
					vacuum.toggleMode(hitResult.getDirection());
					world.playSound(null, pos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.3F, 0.6F);
					world.sendBlockUpdated(pos, state, state, 3);
				}
			}
		}
		return InteractionResult.SUCCESS;
	}

	@Nonnull
	@Override
	public BlockState playerWillDestroy(Level world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull Player player) {
		if (!world.isClientSide) {
			TileEntityAbsorptionHopper tile = (TileEntityAbsorptionHopper) world.getBlockEntity(pos);
			if (tile != null) {
				Containers.dropContents(world, pos, tile);
				world.removeBlockEntity(pos);
			}
		}
		return super.playerWillDestroy(world, pos, state, player);
	}
}
