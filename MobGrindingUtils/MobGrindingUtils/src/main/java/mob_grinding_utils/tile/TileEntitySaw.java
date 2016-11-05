package mob_grinding_utils.tile;

import java.util.List;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.blocks.BlockSaw;
import mob_grinding_utils.items.ItemSawUpgrade;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayerFactory;

public class TileEntitySaw extends TileEntityInventoryHelper implements ITickable {

	public boolean active;
	public int animationTicks, prevAnimationTicks;
    private static final int[] SLOTS = new int[] {0, 1, 2, 3, 4, 5};

	public TileEntitySaw() {
		super(6);
	}

	@Override
	public void update() {
		if (worldObj.isRemote && active) {
			prevAnimationTicks = animationTicks;
			if (animationTicks < 360)
				animationTicks += 18;
			if (animationTicks >= 360) {
				animationTicks = 0;
				prevAnimationTicks = 0;
			}
		}

		if (!worldObj.isRemote && worldObj.getWorldTime() % 10 == 0 && worldObj.getBlockState(pos).getBlock() != null)
			if (worldObj.getBlockState(pos).getValue(BlockSaw.POWERED))
				activateBlock();
	}

	public void setActive(boolean isActive) {
		active = isActive;
		worldObj.notifyBlockUpdate(pos, worldObj.getBlockState(pos), worldObj.getBlockState(pos), 3);
	}

	@SuppressWarnings("unchecked")
	protected Entity activateBlock() {
		IBlockState state = getWorld().getBlockState(pos);

		List<EntityLivingBase> list = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1D, pos.getY() + 1D, pos.getZ() + 1D));
		for (int i = 0; i < list.size(); i++) {
			Entity entity = list.get(i);
			if (entity != null) {
				if (entity instanceof EntityLivingBase) {
					EntityPlayerMP fakePlayer = FakePlayerFactory.get((WorldServer)worldObj, new GameProfile(UUID.nameUUIDFromBytes("mob_grinding_utils".getBytes()), "[" + "mob_grinding_utils" + "]"));
					ItemStack tempSword = new ItemStack(MobGrindingUtils.NULL_SWORD, 1, 0);

					if(!tempSword.hasTagCompound())
						tempSword.setTagCompound(new NBTTagCompound());

					if(hasSharpnessUpgrade())
						tempSword.addEnchantment(Enchantment.getEnchantmentByLocation("sharpness"), inventory[0].stackSize * 10);
					if(hasLootingUpgrade())
						tempSword.addEnchantment(Enchantment.getEnchantmentByLocation("looting"), inventory[1].stackSize);
					if(hasFlameUpgrade())
						tempSword.addEnchantment(Enchantment.getEnchantmentByLocation("fire_aspect"), inventory[2].stackSize);
					if(hasSmiteUpgrade())
						tempSword.addEnchantment(Enchantment.getEnchantmentByLocation("smite"), inventory[3].stackSize * 10);
					if(hasArthropodUpgrade())
						tempSword.addEnchantment(Enchantment.getEnchantmentByLocation("bane_of_arthropods"), inventory[4].stackSize * 10);
					if(hasBeheadingUpgrade())
						tempSword.getTagCompound().setInteger("beheadingValue", inventory[5].stackSize);

					fakePlayer.setHeldItem(EnumHand.MAIN_HAND, tempSword);
					fakePlayer.attackTargetEntityWithCurrentItem(entity);
					fakePlayer.resetCooldown();
					fakePlayer.setHeldItem(EnumHand.MAIN_HAND, null);

				}
			}
		}
		return null;
	}

	private boolean hasSharpnessUpgrade() {
		return inventory[0] != null && inventory[0].getItem() == MobGrindingUtils.SAW_UPGRADE && inventory[0].getItemDamage() == 0;
	}
	
	private boolean hasLootingUpgrade() {
		return inventory[1] != null && inventory[1].getItem() == MobGrindingUtils.SAW_UPGRADE && inventory[1].getItemDamage() == 1;
	}

	private boolean hasFlameUpgrade() {
		return inventory[2] != null && inventory[2].getItem() == MobGrindingUtils.SAW_UPGRADE && inventory[2].getItemDamage() == 2;
	}
	
	private boolean hasSmiteUpgrade() {
		return inventory[3] != null && inventory[3].getItem() == MobGrindingUtils.SAW_UPGRADE && inventory[3].getItemDamage() == 3;
	}
	
	private boolean hasArthropodUpgrade() {
		return inventory[4] != null && inventory[4].getItem() == MobGrindingUtils.SAW_UPGRADE && inventory[4].getItemDamage() == 4;
	}

	private boolean hasBeheadingUpgrade() {
		return inventory[5] != null && inventory[5].getItem() == MobGrindingUtils.SAW_UPGRADE && inventory[5].getItemDamage() == 5;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setBoolean("active", active);
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		active = nbt.getBoolean("active");
	}

	@Override
    public NBTTagCompound getUpdateTag() {
		NBTTagCompound tag = new NBTTagCompound();
        return writeToNBT(tag);
    }

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		return new SPacketUpdateTileEntity(getPos(), 0, tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		super.onDataPacket(net, packet);
		readFromNBT(packet.getNbtCompound());
		worldObj.notifyBlockUpdate(pos, worldObj.getBlockState(pos), worldObj.getBlockState(pos), 3);
		return;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return stack.getItem() instanceof ItemSawUpgrade && stack.getItemDamage() == slot;
	}

	@Override
	public ItemStack removeStackFromSlot(int slot) {
		return null;
	}

	@Override
	public int getInventoryStackLimit() {
		return 10;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return SLOTS;
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, EnumFacing direction) {
		return isItemValidForSlot(slot, stack);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, EnumFacing direction) {
		return true;
	}

}
