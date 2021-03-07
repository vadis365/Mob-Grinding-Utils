package mob_grinding_utils.blocks;

import mob_grinding_utils.tile.TileEntityTank;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class BlockTank extends ContainerBlock {
	public BlockTank(Block.Properties properties) {
		super(properties);
	}

	@Override
	public TileEntity createNewTileEntity(IBlockReader world) {
		return new TileEntityTank();
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
/*
	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(BlockState blockState, BlockAccess blockAccess, BlockPos pos, Direction side) {
		return true;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	@Nullable
	@Override
	public Item getItemDropped(BlockState state, Random rand, int fortune) {
		return null;
	}
*/
	@Override
	public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (!world.isRemote && !player.abilities.isCreativeMode) {
			TileEntity tileentity = world.getTileEntity(pos);
			if (tileentity instanceof TileEntityTank) {
				CompoundNBT nbt = new CompoundNBT();
				tileentity.write(nbt);
				ItemStack stack = new ItemStack(Item.getItemFromBlock(this), 1);
				if (((TileEntityTank) tileentity).tank.getFluidAmount() > 0)
					stack.setTag(nbt);
				InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stack);
				world.removeTileEntity(pos);
			}
		}
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		super.onBlockPlacedBy(world, pos, state, placer, stack);
		if (!world.isRemote && stack.hasTag()) {
			TileEntity tileentity = world.getTileEntity(pos);
			if (tileentity instanceof TileEntityTank) {
				if (!stack.getTag().contains("Empty")) {
					FluidStack fluid = FluidStack.loadFluidStackFromNBT(stack.getTag());
					((TileEntityTank) tileentity).tank.fill(fluid, FluidAction.EXECUTE);
				}
			}
		}
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		TileEntity tileentity = world.getTileEntity(pos);
		if (tileentity instanceof TileEntityTank) {
			LazyOptional<IFluidHandler> fluidHandler = tileentity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, hit.getFace());

			if (fluidHandler != null) {
				FluidUtil.interactWithFluidHandler(player, hand, world, pos, hit.getFace());
				if (FluidUtil.getFluidHandler(player.getHeldItem(hand)) != null)
					return ActionResultType.SUCCESS;
			}
		}
			return ActionResultType.PASS;
	}
}
