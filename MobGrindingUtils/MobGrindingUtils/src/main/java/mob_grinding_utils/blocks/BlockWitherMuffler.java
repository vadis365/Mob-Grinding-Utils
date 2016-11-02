package mob_grinding_utils.blocks;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.network.WitherBarMessage;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockWitherMuffler extends Block {

	public static final PropertyBool MODE = PropertyBool.create("mode");

	public BlockWitherMuffler() {
		super(Material.CLOTH);
		setDefaultState(this.getBlockState().getBaseState().withProperty(MODE, false));
		setHardness(0.5F);
		setSoundType(SoundType.CLOTH);
		setCreativeTab(MobGrindingUtils.TAB);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(MODE, Boolean.valueOf((meta & 1) > 0));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = 0;
		if ((Boolean) state.getValue(MODE).booleanValue())
			meta = 1;
		return meta;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {MODE});
	}

	@Override
	public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return this.getDefaultState().withProperty(MODE, false);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote)
			return true;
		else {
			state = state.cycleProperty(MODE);
			world.setBlockState(pos, state, 3);
			player.getEntityData().setBoolean("turnOffWitherBossBar", (Boolean) state.getValue(MODE).booleanValue());
			MobGrindingUtils.NETWORK_WRAPPER.sendTo(new WitherBarMessage((Boolean) state.getValue(MODE).booleanValue()), (EntityPlayerMP) player);
			return true;
		}

	}
}