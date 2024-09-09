package mob_grinding_utils.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nonnull;

public class BlockDragonMuffler extends Block {

	public static final BooleanProperty MODE = BooleanProperty.create("mode");

	public BlockDragonMuffler(Block.Properties properties) {
		super(properties);
		registerDefaultState(this.stateDefinition.any().setValue(MODE, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(MODE);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(MODE, false);
	}

	@Nonnull
	@Override
	public InteractionResult useWithoutItem(BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull BlockHitResult hitResult) {
		boolean swap = !state.getValue(MODE);
		if (!world.isClientSide)
			world.setBlock(pos, state.setValue(MODE, swap), 3);
		CompoundTag nbt = player.getPersistentData();
		nbt.putBoolean("MGU_DragonMuffle" , swap);
		player.displayClientMessage(Component.literal(swap ? "Now hiding Dragon boss bars.":"Now showing Dragon boss bars."), true);
		return InteractionResult.SUCCESS;
	}
}