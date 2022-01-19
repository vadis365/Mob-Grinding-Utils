package mob_grinding_utils.blocks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import mob_grinding_utils.tile.TileEntityXPSolidifier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.network.NetworkHooks;


public class BlockXPSolidifier extends BaseEntityBlock {
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public BlockXPSolidifier(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return new TileEntityXPSolidifier(pos, state);
    }

    @Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
		return TileEntityXPSolidifier::tick;
	}

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction direction = context.getHorizontalDirection().getOpposite();
		return this.defaultBlockState().setValue(FACING, direction);
	}

	@Override
	public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
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
	}

	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		super.setPlacedBy(world, pos, state, placer, stack);
		if (!world.isClientSide && stack.hasTag()) {
			BlockEntity tileentity = world.getBlockEntity(pos);
			if (tileentity instanceof TileEntityXPSolidifier) {
				if (!stack.getTag().contains("Empty")) {
					FluidStack fluid = FluidStack.loadFluidStackFromNBT(stack.getTag());
					((TileEntityXPSolidifier) tileentity).tank.fill(fluid, FluidAction.EXECUTE);
				}
			}
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (world.isClientSide) {
			return InteractionResult.SUCCESS;
		} else {
			BlockEntity tileentity = world.getBlockEntity(pos);
			if (tileentity instanceof TileEntityXPSolidifier) {
				if (!player.getItemInHand(hand).isEmpty() && player.getItemInHand(hand).getItem() instanceof BucketItem) { // fixy later
					LazyOptional<IFluidHandler> fluidHandler = tileentity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, hit.getDirection());
					fluidHandler.ifPresent((handler) -> {
						if (player.getItemInHand(hand).isEmpty() && !handler.getFluidInTank(0).isEmpty())
							player.displayClientMessage(new TranslatableComponent(handler.getFluidInTank(0).getDisplayName().getString() + ": "+ handler.getFluidInTank(0).getAmount()+"/"+handler.getTankCapacity(0)), true);
						else
							FluidUtil.interactWithFluidHandler(player, hand, world, pos, hit.getDirection());
					});
					return InteractionResult.SUCCESS;
				}
				else {
					NetworkHooks.openGui((ServerPlayer) player, (TileEntityXPSolidifier) tileentity, pos);
					return InteractionResult.SUCCESS;
				}
			}
		}
		return InteractionResult.PASS;
    }
}
