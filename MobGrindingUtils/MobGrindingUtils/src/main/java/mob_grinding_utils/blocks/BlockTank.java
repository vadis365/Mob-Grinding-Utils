package mob_grinding_utils.blocks;

import java.util.Random;

import javax.annotation.Nullable;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.tile.TileEntityTank;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTank extends BlockContainer {
	public BlockTank() {
		super(Material.GLASS);
		setHardness(1.0F);
		setSoundType(SoundType.GLASS);
		setCreativeTab(MobGrindingUtils.TAB);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityTank();
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}
	//BlockRenderLayer.CUTOUT_MIPPED
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	/*@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		return true;
	}
*/
	@Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
        Block block = iblockstate.getBlock();


            if (blockState != iblockstate)
            {
                return true;
            }

            if (block == this)
            {
                return false;
            }


        return block == this ? false : super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

	@Nullable
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return null;
	}

	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		if (!world.isRemote && !player.capabilities.isCreativeMode) {
			TileEntity tileentity = world.getTileEntity(pos);
			if (tileentity instanceof TileEntityTank) {
				NBTTagCompound nbt = new NBTTagCompound();
				tileentity.writeToNBT(nbt);
				ItemStack stack = new ItemStack(Item.getItemFromBlock(this), 1, 0);
				if (((TileEntityTank) tileentity).tank.getFluidAmount() > 0)
					stack.setTagCompound(nbt);
				InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stack);
				world.removeTileEntity(pos);
			}
		}
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		super.onBlockPlacedBy(world, pos, state, placer, stack);
		if (!world.isRemote && stack.hasTagCompound()) {
			TileEntity tileentity = world.getTileEntity(pos);
			if (tileentity instanceof TileEntityTank) {
				if (!stack.getTagCompound().hasKey("Empty")) {
					FluidStack fluid = FluidStack.loadFluidStackFromNBT(stack.getTagCompound());
					((TileEntityTank) tileentity).tank.fillInternal(fluid, true);
				}
			}
		}
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		TileEntityTank tileentity = (TileEntityTank)world.getTileEntity(pos);
		boolean stopCheck = false;
		if (tileentity instanceof TileEntityTank) {
			final IFluidHandler fluidHandler = getFluidHandler(world, pos);
			IFluidHandler fluidHandlerUP = null;
			TileEntityTank tileentityUP = null;
			TileEntityTank tileentityBelow = null;
			for (int y = 0; y < 256; y++) {
				tileentityBelow = (TileEntityTank)world.getTileEntity(pos.up(y - 1));
				tileentityUP = (TileEntityTank)world.getTileEntity(pos.up(y));
				if (tileentityUP instanceof TileEntityTank) {
					fluidHandlerUP = getFluidHandler(world, pos.up(y));
						IFluidTankProperties[] tankProperties = fluidHandlerUP.getTankProperties();

						for (IFluidTankProperties properties : tankProperties) {
							if (properties.canFill() && properties.getCapacity() > 0) {
								FluidStack contents = properties.getContents();
								if (contents != null && !contents.containsFluid(new FluidStack(tileentity.tank.getFluid(), 0))) {
									System.out.println("1st tank in chain that cannot be filled contains: " + tileentityUP.tank.getFluid().getFluid().getName());
									stopCheck = true;
								}

								if (contents == null) {
									System.out.println("Tank chain contains empty tanks");
									tileentityBelow = tileentityUP;
									stopCheck = true;
								}
							}
							if(stopCheck)
								break;
						}
				}
				if (tileentityUP == null|| fluidHandlerUP == null || stopCheck)
					break;
			}

			if (tileentityBelow instanceof TileEntityTank) {
				fluidHandlerUP = getFluidHandler(world, tileentityBelow.getPos());
			}

			if (fluidHandler != null) {
				if (!FluidUtil.interactWithFluidHandler(heldItem, fluidHandler, playerIn))
					if (fluidHandlerUP != null) 
						FluidUtil.interactWithFluidHandler(heldItem, fluidHandlerUP, playerIn);
				return FluidUtil.getFluidHandler(heldItem) != null;
			}
		}
		return false;
	}

	@Nullable
	private IFluidHandler getFluidHandler(IBlockAccess world, BlockPos pos) {
		TileEntityTank tileentity = (TileEntityTank) world.getTileEntity(pos);
		return tileentity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
	}

}
