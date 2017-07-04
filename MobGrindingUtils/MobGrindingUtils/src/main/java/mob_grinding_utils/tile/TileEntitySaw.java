package mob_grinding_utils.tile;

import java.util.List;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

import mob_grinding_utils.ModItems;
import mob_grinding_utils.blocks.BlockSaw;
import mob_grinding_utils.items.ItemSawUpgrade;
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
import net.minecraft.util.text.TextComponentTranslation;
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
		if (getWorld().isRemote && active) {
			prevAnimationTicks = animationTicks;
			if (animationTicks < 360)
				animationTicks += 18;
			if (animationTicks >= 360) {
				animationTicks -= 360;
				prevAnimationTicks -= 360;
			}
		}

		if (!getWorld().isRemote && getWorld().getTotalWorldTime() % 10 == 0 && getWorld().getBlockState(pos).getBlock() != null)
			if (getWorld().getBlockState(pos).getValue(BlockSaw.POWERED))
				activateBlock();
	}

	public void setActive(boolean isActive) {
		active = isActive;
		getWorld().notifyBlockUpdate(pos, getWorld().getBlockState(pos), getWorld().getBlockState(pos), 3);
	}

	@SuppressWarnings("unchecked")
	protected Entity activateBlock() {
		List<EntityLivingBase> list = getWorld().getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1D, pos.getY() + 1D, pos.getZ() + 1D));
		for (int i = 0; i < list.size(); i++) {
			Entity entity = list.get(i);
			if (entity != null) {
				if (entity instanceof EntityLivingBase) {
					EntityPlayerMP fakePlayer = FakePlayerFactory.get((WorldServer)getWorld(), new GameProfile(UUID.nameUUIDFromBytes(new TextComponentTranslation("fakeplayer.mob_masher").getFormattedText().getBytes()), new TextComponentTranslation("fakeplayer.mob_masher").getFormattedText()));
					fakePlayer.setPosition(this.pos.getX(), -100D, this.pos.getZ());
					ItemStack tempSword = new ItemStack(ModItems.NULL_SWORD, 1, 0);

					if(!tempSword.hasTagCompound())
						tempSword.setTagCompound(new NBTTagCompound());

					if(hasSharpnessUpgrade())
						tempSword.addEnchantment(Enchantment.getEnchantmentByLocation("sharpness"), getItems().get(0).getCount() * 10);
					if(hasLootingUpgrade())
						tempSword.addEnchantment(Enchantment.getEnchantmentByLocation("looting"), getItems().get(1).getCount());
					if(hasFlameUpgrade())
						tempSword.addEnchantment(Enchantment.getEnchantmentByLocation("fire_aspect"), getItems().get(2).getCount());
					if(hasSmiteUpgrade())
						tempSword.addEnchantment(Enchantment.getEnchantmentByLocation("smite"), getItems().get(3).getCount() * 10);
					if(hasArthropodUpgrade())
						tempSword.addEnchantment(Enchantment.getEnchantmentByLocation("bane_of_arthropods"), getItems().get(4).getCount() * 10);
					if(hasBeheadingUpgrade())
						tempSword.getTagCompound().setInteger("beheadingValue", getItems().get(5).getCount());

					fakePlayer.setHeldItem(EnumHand.MAIN_HAND, tempSword);
					fakePlayer.attackTargetEntityWithCurrentItem(entity);
					fakePlayer.resetCooldown();
					fakePlayer.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
				}
			}
		}
		return null;
	}

	private boolean hasSharpnessUpgrade() {
		return !getItems().get(0).isEmpty() && getItems().get(0).getItem() == ModItems.SAW_UPGRADE && getItems().get(0).getItemDamage() == 0;
	}
	
	private boolean hasLootingUpgrade() {
		return !getItems().get(1).isEmpty() && getItems().get(1).getItem() == ModItems.SAW_UPGRADE && getItems().get(1).getItemDamage() == 1;
	}

	private boolean hasFlameUpgrade() {
		return !getItems().get(2).isEmpty() && getItems().get(2).getItem() == ModItems.SAW_UPGRADE && getItems().get(2).getItemDamage() == 2;
	}
	
	private boolean hasSmiteUpgrade() {
		return !getItems().get(3).isEmpty() && getItems().get(3).getItem() == ModItems.SAW_UPGRADE && getItems().get(3).getItemDamage() == 3;
	}
	
	private boolean hasArthropodUpgrade() {
		return !getItems().get(4).isEmpty() && getItems().get(4).getItem() == ModItems.SAW_UPGRADE && getItems().get(4).getItemDamage() == 4;
	}

	private boolean hasBeheadingUpgrade() {
		return !getItems().get(5).isEmpty() && getItems().get(5).getItem() == ModItems.SAW_UPGRADE && getItems().get(5).getItemDamage() == 5;
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
		if(!getWorld().isRemote)
			getWorld().notifyBlockUpdate(pos, getWorld().getBlockState(pos), getWorld().getBlockState(pos), 3);
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
