package mob_grinding_utils.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class BlockDragonMuffler extends Block {

	public static final BooleanProperty MODE = BooleanProperty.create("mode");

	public BlockDragonMuffler(Block.Properties properties) {
		super(properties);
		setDefaultState(this.stateContainer.getBaseState().with(MODE, false));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(MODE);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(MODE, false);
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		if (world.isRemote)
			return ActionResultType.SUCCESS;
		else {
			boolean swap = state.get(MODE) ? false : true;
			world.setBlockState(pos, state.with(MODE, swap), 3);
			System.out.println("Dragon Muffler block caps here");
			//TODO Fix caps
			//IBossBarCapability cap = player.getCapability(BossBarPlayerCapability.CAPABILITY_PLAYER_BOSS_BAR, null);
			//cap.setRenderEnderDragonBar(!state.get(MODE).booleanValue());
			return ActionResultType.SUCCESS;
		}
	}
}