package mob_grinding_utils.blocks;

import java.util.Random;

import mob_grinding_utils.ModBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockEnderInhibitorOff extends BlockEnderInhibitorOn {

	public BlockEnderInhibitorOff() {
		super();
		setDefaultState(blockState.getBaseState().withProperty(TYPE, EnumGemDirection.DOWN_NORTH));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World world, BlockPos pos, Random rand) {
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) {
			return true;
		} else {
			IBlockState activeState = ModBlocks.ENDER_INHIBITOR_ON.getDefaultState().withProperty(BlockEnderInhibitorOn.TYPE, state.getValue(TYPE));
			world.setBlockState(pos, activeState, 3);
			world.playSound((EntityPlayer) null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, 0.6F);
			return true;
		}
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(ModBlocks.ENDER_INHIBITOR_ON.getDefaultState().withProperty(BlockEnderInhibitorOn.TYPE, BlockEnderInhibitorOn.EnumGemDirection.DOWN_NORTH).getBlock());
	}

}