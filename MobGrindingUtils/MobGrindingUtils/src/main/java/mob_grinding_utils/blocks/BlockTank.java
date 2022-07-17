package mob_grinding_utils.blocks;

import mob_grinding_utils.tile.TileEntityTank;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockTank extends BaseEntityBlock {
	public BlockTank(Block.Properties properties) {
		super(properties);
	}

	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new TileEntityTank(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, @Nonnull BlockState pState, @Nonnull BlockEntityType<T> pBlockEntityType) {
		return pLevel.isClientSide ? null : TileEntityTank::serverTick;
	}

	@Nonnull
	@Override
	public RenderShape getRenderShape(@Nonnull BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
		if (!world.isClientSide && !player.getAbilities().instabuild) {
			BlockEntity tileentity = world.getBlockEntity(pos);
			if (tileentity instanceof TileEntityTank) {
				CompoundTag nbt = new CompoundTag();
				((TileEntityTank) tileentity).saveAdditional(nbt);
				ItemStack stack = new ItemStack(Item.byBlock(this), 1);
				if (((TileEntityTank) tileentity).tank.getFluidAmount() > 0)
					stack.setTag(nbt);
				Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stack);
				world.removeBlockEntity(pos);
			}
		}
	}

	@Override
	public void setPlacedBy(@Nonnull Level world, @Nonnull BlockPos pos, @Nonnull BlockState state, LivingEntity placer, @Nonnull ItemStack stack) {
		super.setPlacedBy(world, pos, state, placer, stack);
		if (!world.isClientSide && stack.hasTag()) {
			BlockEntity tileentity = world.getBlockEntity(pos);
			if (tileentity instanceof TileEntityTank) {
				if (!stack.getTag().contains("Empty")) {
					FluidStack fluid = FluidStack.loadFluidStackFromNBT(stack.getTag());
					((TileEntityTank) tileentity).tank.fill(fluid, FluidAction.EXECUTE);
				}
			}
		}
	}

	@Nonnull
	@Override
	public InteractionResult use(@Nonnull BlockState state, Level world, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult hit) {
		if (world.isClientSide)
			return InteractionResult.SUCCESS;
		BlockEntity tileentity = world.getBlockEntity(pos);
		if (tileentity instanceof TileEntityTank) {
			LazyOptional<IFluidHandler> fluidHandler = tileentity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, hit.getDirection());
			fluidHandler.ifPresent((handler) -> {
				if (player.getItemInHand(hand).isEmpty() && !handler.getFluidInTank(0).isEmpty())
					player.displayClientMessage(Component.translatable(handler.getFluidInTank(0).getDisplayName().getString() + ": " + handler.getFluidInTank(0).getAmount() + "/" + handler.getTankCapacity(0)), true);
				else
					FluidUtil.interactWithFluidHandler(player, hand, world, pos, hit.getDirection());
			});
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}
}
