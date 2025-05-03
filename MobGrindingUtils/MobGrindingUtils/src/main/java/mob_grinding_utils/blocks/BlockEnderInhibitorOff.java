package mob_grinding_utils.blocks;

import mob_grinding_utils.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class BlockEnderInhibitorOff extends BlockEnderInhibitorOn {

	public BlockEnderInhibitorOff(Block.Properties properties) {
		super(properties);
		registerDefaultState(this.stateDefinition.any().setValue(TYPE, EnumGemDirection.DOWN_NORTH));
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(@Nonnull BlockState stateIn, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull RandomSource rand) {
	}

	@Nonnull
	@Override
	public InteractionResult useWithoutItem(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull BlockHitResult hitResult) {
		if (!world.isClientSide) {
			BlockState activeState = ModBlocks.ENDER_INHIBITOR_ON.getBlock().defaultBlockState().setValue(BlockEnderInhibitorOn.TYPE, state.getValue(TYPE));
			world.setBlock(pos, activeState, 3);
			world.playSound(null, pos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.3F, 0.6F);
		}
		return InteractionResult.SUCCESS;
	}
}