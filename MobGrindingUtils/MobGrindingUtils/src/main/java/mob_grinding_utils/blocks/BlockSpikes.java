package mob_grinding_utils.blocks;

import java.lang.reflect.Method;

import mob_grinding_utils.MobGrindingUtils;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockSpikes extends BlockDirectional {

	public static final AxisAlignedBB SPIKES_AABB = new AxisAlignedBB(0.0625D, 0.0625D, 0.0625D, 0.9375D, 0.9375D, 0.9375D);

	public BlockSpikes() {
		super(Material.IRON);
		setHardness(5.0F);
		setResistance(2000.0F);
		setSoundType(SoundType.METAL);
		setCreativeTab(MobGrindingUtils.TAB);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return SPIKES_AABB;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
		return FULL_BLOCK_AABB;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.byIndex(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return ((EnumFacing) state.getValue(FACING)).getIndex();
	}

	@Override
	 public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return this.getDefaultState().withProperty(FACING, facing);
	}

	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(FACING, rot.rotate((EnumFacing) state.getValue(FACING)));
	}

	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		return state.withRotation(mirrorIn.toRotation((EnumFacing) state.getValue(FACING)));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING });
	}

	@Override
	public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
      return !(entity instanceof EntityWither) && !(entity instanceof EntityDragon);
	}

	@Override
	public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entity) {
		if (!worldIn.isRemote && entity instanceof EntityLivingBase)
			entity.attackEntityFrom(MobGrindingUtils.SPIKE_DAMAGE, 5);
	}

	public static final Method xpPoints = getExperiencePoints();

	@SubscribeEvent
	public void dropXP(LivingDropsEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		World world = entity.getEntityWorld();
		if (entity != null) {
			if (!world.isRemote && !event.isRecentlyHit() && event.getSource() == MobGrindingUtils.SPIKE_DAMAGE) {
				int xp = 0;
				try {
					xp = (Integer) xpPoints.invoke(entity, FakePlayerFactory.getMinecraft((WorldServer) world));
				} catch (Exception e) {
				}
				while (xp > 0) {
					int cap = EntityXPOrb.getXPSplit(xp);
					xp -= cap;
					entity.getEntityWorld().spawnEntity(new EntityXPOrb(entity.getEntityWorld(), entity.posX, entity.posY, entity.posZ, cap));
				}
			}
		}
	}

	public static Method getExperiencePoints() {
		Method method = null;
		try {
			method = EntityLiving.class.getDeclaredMethod("getExperiencePoints", EntityPlayer.class);
			method.setAccessible(true);
		} catch (Exception e) {
		}
		try {
			method = EntityLiving.class.getDeclaredMethod("func_70693_a", EntityPlayer.class);
			method.setAccessible(true);
		} catch (Exception e) {
		}
		return method;
	}
}
