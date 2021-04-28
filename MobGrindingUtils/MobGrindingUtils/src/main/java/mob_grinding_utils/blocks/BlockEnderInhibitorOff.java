package mob_grinding_utils.blocks;

import java.util.Random;

import mob_grinding_utils.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockEnderInhibitorOff extends BlockEnderInhibitorOn {

	public BlockEnderInhibitorOff(Block.Properties properties) {
		super(properties);
		setDefaultState(this.stateContainer.getBaseState().with(TYPE, EnumGemDirection.DOWN_NORTH));
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World world, BlockPos pos, Random rand) {
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		if (!world.isRemote) {
			BlockState activeState = ModBlocks.ENDER_INHIBITOR_ON.getDefaultState().with(BlockEnderInhibitorOn.TYPE, state.get(TYPE));
			world.setBlockState(pos, activeState, 3);
			world.playSound(null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, 0.6F);
		}
		return ActionResultType.SUCCESS;
	}
}