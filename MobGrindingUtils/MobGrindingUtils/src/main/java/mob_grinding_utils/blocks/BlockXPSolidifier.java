package mob_grinding_utils.blocks;

import com.mojang.serialization.MapCodec;
import mob_grinding_utils.tile.TileEntityXPSolidifier;
import mob_grinding_utils.util.CapHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;


public class BlockXPSolidifier extends BaseEntityBlock {
	public static final MapCodec<BlockXPSolidifier> CODEC = simpleCodec(BlockXPSolidifier::new);
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public BlockXPSolidifier(Properties properties) {
        super(properties);
    }

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	@Nullable
    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return new TileEntityXPSolidifier(pos, state);
    }

    @Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@Nonnull Level pLevel, @Nonnull BlockState pState, @Nonnull BlockEntityType<T> pBlockEntityType) {
		return TileEntityXPSolidifier::tick;
	}

    @Nonnull
	@Override
    public RenderShape getRenderShape(@Nonnull BlockState state) {
        return RenderShape.MODEL;
    }

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction direction = context.getHorizontalDirection().getOpposite();
		return this.defaultBlockState().setValue(FACING, direction);
	}

	@Override
	public BlockState playerWillDestroy(Level world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull Player player) {
		if (!world.isClientSide && !player.getAbilities().instabuild) {
			TileEntityXPSolidifier tile = (TileEntityXPSolidifier) world.getBlockEntity(pos);
			if (tile != null) {
				CompoundTag nbt = new CompoundTag();
				tile.saveAdditional(nbt);
				ItemStack stack = new ItemStack(Item.byBlock(this), 1);
				if (tile.tank.getFluidAmount() > 0)
					stack.setTag(nbt);
				Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stack);
				if(!tile.inputSlots.getStackInSlot(0).isEmpty())
					Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), tile.inputSlots.getStackInSlot(0));
				if(!tile.inputSlots.getStackInSlot(1).isEmpty())
					Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), tile.inputSlots.getStackInSlot(1));
				if(!tile.outputSlot.getStackInSlot(0).isEmpty())
					Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), tile.outputSlot.getStackInSlot(0));
				world.removeBlockEntity(pos);
			}
		}
		return super.playerWillDestroy(world, pos, state, player);
	}

	@Override
	public void setPlacedBy(@Nonnull Level world, @Nonnull BlockPos pos, @Nonnull BlockState state, LivingEntity placer, @Nonnull ItemStack stack) {
		super.setPlacedBy(world, pos, state, placer, stack);
		if (!world.isClientSide && stack.hasTag()) {
			BlockEntity tileentity = world.getBlockEntity(pos);
			if (tileentity instanceof TileEntityXPSolidifier) {
				if (!stack.getTag().contains("Empty")) {
					FluidStack fluid = FluidStack.loadFluidStackFromNBT(stack.getTag());
					((TileEntityXPSolidifier) tileentity).tank.fill(fluid, IFluidHandler.FluidAction.EXECUTE);
				}
			}
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

    @Nonnull
	@Override
    public InteractionResult use(@Nonnull BlockState state, Level world, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult hit) {
		if (world.isClientSide) {
			return InteractionResult.SUCCESS;
		} else {
			BlockEntity tileentity = world.getBlockEntity(pos);
			if (tileentity instanceof TileEntityXPSolidifier) {
				if (!player.getItemInHand(hand).isEmpty() && player.getItemInHand(hand).getItem() instanceof BucketItem) { // fixy later, Flanks: ?!?
					Optional<IFluidHandler> fluidHandler = CapHelper.getFluidHandler(world, pos, hit.getDirection());
					fluidHandler.ifPresent((handler) -> {
						if (player.getItemInHand(hand).isEmpty() && !handler.getFluidInTank(0).isEmpty())
							player.displayClientMessage(Component.translatable(handler.getFluidInTank(0).getDisplayName().getString() + ": "+ handler.getFluidInTank(0).getAmount()+"/"+handler.getTankCapacity(0)), true);
						else
							FluidUtil.interactWithFluidHandler(player, hand, world, pos, hit.getDirection());
					});
					return InteractionResult.SUCCESS;
				}
				else {
					player.openMenu((TileEntityXPSolidifier) tileentity, pos);
					return InteractionResult.SUCCESS;
				}
			}
		}
		return InteractionResult.PASS;
    }
}
