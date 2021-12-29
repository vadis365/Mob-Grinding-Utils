package mob_grinding_utils.blocks;

import java.util.Random;

import mob_grinding_utils.ModBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockEnderInhibitorOff extends BlockEnderInhibitorOn {

	public BlockEnderInhibitorOff(Block.Properties properties) {
		super(properties);
		registerDefaultState(this.stateDefinition.any().setValue(TYPE, EnumGemDirection.DOWN_NORTH));
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, Level world, BlockPos pos, Random rand) {
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (!world.isClientSide) {
			BlockState activeState = ModBlocks.ENDER_INHIBITOR_ON.getBlock().defaultBlockState().setValue(BlockEnderInhibitorOn.TYPE, state.getValue(TYPE));
			world.setBlock(pos, activeState, 3);
			world.playSound(null, pos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.3F, 0.6F);
		}
		return InteractionResult.SUCCESS;
	}
}