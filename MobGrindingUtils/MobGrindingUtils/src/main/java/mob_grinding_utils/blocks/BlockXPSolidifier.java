package mob_grinding_utils.blocks;

import javax.annotation.Nullable;

import mob_grinding_utils.tile.TileEntityXPSolidifier;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockXPSolidifier extends ContainerBlock {
	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public BlockXPSolidifier(Properties properties) {
        super(properties);
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
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		Direction direction = context.getPlacementHorizontalFacing().getOpposite();
		return this.getDefaultState().with(FACING, direction);
	}

	@Override
	public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (!world.isRemote && !player.abilities.isCreativeMode) {
			TileEntityXPSolidifier tile = (TileEntityXPSolidifier) world.getTileEntity(pos);
			if (tile != null) {
				CompoundNBT nbt = new CompoundNBT();
				tile.write(nbt);
				ItemStack stack = new ItemStack(Item.getItemFromBlock(this), 1);
				if (tile.tank.getFluidAmount() > 0)
					stack.setTag(nbt);
				InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stack);
				if(!tile.inputSlots.getStackInSlot(0).isEmpty())
					InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), tile.inputSlots.getStackInSlot(0));
				if(!tile.inputSlots.getStackInSlot(1).isEmpty())
					InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), tile.inputSlots.getStackInSlot(1));
				if(!tile.outputSlot.getStackInSlot(0).isEmpty())
					InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), tile.outputSlot.getStackInSlot(0));
				world.removeTileEntity(pos);
			}
		}
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		super.onBlockPlacedBy(world, pos, state, placer, stack);
		if (!world.isRemote && stack.hasTag()) {
			TileEntity tileentity = world.getTileEntity(pos);
			if (tileentity instanceof TileEntityXPSolidifier) {
				if (!stack.getTag().contains("Empty")) {
					FluidStack fluid = FluidStack.loadFluidStackFromNBT(stack.getTag());
					((TileEntityXPSolidifier) tileentity).tank.fill(fluid, FluidAction.EXECUTE);
				}
			}
		}
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		if (world.isRemote) {
			return ActionResultType.SUCCESS;
		} else {
			TileEntity tileentity = world.getTileEntity(pos);
			if (tileentity instanceof TileEntityXPSolidifier) {
				if (!player.getHeldItem(hand).isEmpty() && player.getHeldItem(hand).getItem() instanceof BucketItem) { // fixy later
					LazyOptional<IFluidHandler> fluidHandler = tileentity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, hit.getFace());
					fluidHandler.ifPresent((handler) -> {
						if (player.getHeldItem(hand).isEmpty() && !handler.getFluidInTank(0).isEmpty())
							player.sendStatusMessage(new TranslationTextComponent(handler.getFluidInTank(0).getDisplayName().getString() + ": "+ handler.getFluidInTank(0).getAmount()+"/"+handler.getTankCapacity(0)), true);
						else
							FluidUtil.interactWithFluidHandler(player, hand, world, pos, hit.getFace());
					});
					return ActionResultType.SUCCESS;
				}
				else {
					NetworkHooks.openGui((ServerPlayerEntity) player, (TileEntityXPSolidifier) tileentity, pos);
					return ActionResultType.SUCCESS;
				}
			}
		}
		return ActionResultType.PASS;
    }
}
