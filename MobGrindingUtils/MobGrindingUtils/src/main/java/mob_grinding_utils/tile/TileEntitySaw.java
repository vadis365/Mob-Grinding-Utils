package mob_grinding_utils.tile;

import java.util.List;

import io.netty.buffer.Unpooled;
import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.ModItems;
import mob_grinding_utils.blocks.BlockSaw;
import mob_grinding_utils.fakeplayer.MGUFakePlayer;
import mob_grinding_utils.inventory.server.ContainerSaw;
import mob_grinding_utils.items.ItemSawUpgrade;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;

public class TileEntitySaw extends TileEntityInventoryHelper implements ITickableTileEntity, INamedContainerProvider {

	public boolean active;
	public int animationTicks, prevAnimationTicks;
    private static final int[] SLOTS = new int[] {0, 1, 2, 3, 4, 5};

	public TileEntitySaw() {
		super(ModBlocks.SAW.getTileEntityType(), 6);
	}

	@Override
	public void tick() {
		if (getWorld().isRemote && active) {
			prevAnimationTicks = animationTicks;
			if (animationTicks < 360)
				animationTicks += 18;
			if (animationTicks >= 360) {
				animationTicks -= 360;
				prevAnimationTicks -= 360;
			}
		}

		if (getWorld().isRemote && !active)
			prevAnimationTicks = animationTicks = 0;

		if (!getWorld().isRemote && getWorld().getGameTime() % 10 == 0 && getWorld().getBlockState(pos).getBlock() instanceof BlockSaw)
			if (getWorld().getBlockState(pos).get(BlockSaw.POWERED))
				activateBlock();
	}

	public void setActive(boolean isActive) {
		active = isActive;
		getWorld().notifyBlockUpdate(pos, getWorld().getBlockState(pos), getWorld().getBlockState(pos), 3);
	}

	protected Entity activateBlock() {
		List<LivingEntity> list = getWorld().getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1D, pos.getY() + 1D, pos.getZ() + 1D).grow(0.0625D, 0.0625D, 0.0625D));
		for (int i = 0; i < list.size(); i++) {
			Entity entity = list.get(i);
			if (entity != null) {
				if (entity instanceof LivingEntity) {
					MGUFakePlayer fakePlayer = MGUFakePlayer.get((ServerWorld)getWorld(), this.pos.getX(), -100D, this.pos.getZ()).get();
					ItemStack tempSword = new ItemStack(ModItems.NULL_SWORD.get(), 1);

					if(!tempSword.hasTag())
						tempSword.setTag(new CompoundNBT());

					if(hasSharpnessUpgrade())
						tempSword.addEnchantment(Enchantments.SHARPNESS, getItems().get(0).getCount() * 10);
					if(hasLootingUpgrade())
						tempSword.addEnchantment(Enchantments.LOOTING, getItems().get(1).getCount());
					if(hasFlameUpgrade())
						tempSword.addEnchantment(Enchantments.FIRE_ASPECT, getItems().get(2).getCount());
					if(hasSmiteUpgrade())
						tempSword.addEnchantment(Enchantments.SMITE, getItems().get(3).getCount() * 10);
					if(hasArthropodUpgrade())
						tempSword.addEnchantment(Enchantments.BANE_OF_ARTHROPODS, getItems().get(4).getCount() * 10);
					if(hasBeheadingUpgrade())
						tempSword.getTag().putInt("beheadingValue", getItems().get(5).getCount());

					fakePlayer.setHeldItem(Hand.MAIN_HAND, tempSword);
					fakePlayer.attackTargetEntityWithCurrentItem(entity);
					fakePlayer.resetCooldown();
					fakePlayer.setHeldItem(Hand.MAIN_HAND, ItemStack.EMPTY);
				}
			}
		}
		return null;
	}

	private boolean hasSharpnessUpgrade() {
		return !getItems().get(0).isEmpty() && getItems().get(0).getItem() == ModItems.SAW_UPGRADE_SHARPNESS.get();
	}
	
	private boolean hasLootingUpgrade() {
		return !getItems().get(1).isEmpty() && getItems().get(1).getItem() == ModItems.SAW_UPGRADE_LOOTING.get();
	}

	private boolean hasFlameUpgrade() {
		return !getItems().get(2).isEmpty() && getItems().get(2).getItem() == ModItems.SAW_UPGRADE_FIRE.get();
	}
	
	private boolean hasSmiteUpgrade() {
		return !getItems().get(3).isEmpty() && getItems().get(3).getItem() == ModItems.SAW_UPGRADE_SMITE.get();
	}
	
	private boolean hasArthropodUpgrade() {
		return !getItems().get(4).isEmpty() && getItems().get(4).getItem() == ModItems.SAW_UPGRADE_ARTHROPOD.get();
	}

	private boolean hasBeheadingUpgrade() {
		return !getItems().get(5).isEmpty() && getItems().get(5).getItem() == ModItems.SAW_UPGRADE_BEHEADING.get();
	}

	@Override
	public CompoundNBT write(CompoundNBT nbt) {
		super.write(nbt);
		nbt.putBoolean("active", active);
		return nbt;
	}

	@Override
	public void read(BlockState state, CompoundNBT nbt) {
		super.read(state, nbt);
		active = nbt.getBoolean("active");
	}

	@Override
    public CompoundNBT getUpdateTag() {
		CompoundNBT tag = new CompoundNBT();
        return write(tag);
    }

	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		CompoundNBT tag = new CompoundNBT();
		write(tag);
		return new SUpdateTileEntityPacket(getPos(), 0, tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
		super.onDataPacket(net, packet);
		read(getBlockState(), packet.getNbtCompound());
		if(!getWorld().isRemote)
			getWorld().notifyBlockUpdate(pos, getWorld().getBlockState(pos), getWorld().getBlockState(pos), 3);
		return;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if (stack.getItem() instanceof ItemSawUpgrade) {
			switch (slot) {
			case 0:
				if (stack.getItem() == ModItems.SAW_UPGRADE_SHARPNESS.get())
					return true;
			case 1:
				if (stack.getItem() == ModItems.SAW_UPGRADE_LOOTING.get())
					return true;
			case 2:
				if (stack.getItem() == ModItems.SAW_UPGRADE_FIRE.get())
					return true;
			case 3:
				if (stack.getItem() == ModItems.SAW_UPGRADE_SMITE.get())
					return true;
			case 4:
				if (stack.getItem() == ModItems.SAW_UPGRADE_ARTHROPOD.get())
					return true;
			case 5:
				if (stack.getItem() == ModItems.SAW_UPGRADE_BEHEADING.get())
					return true;
			}
		}
		return false;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(getItems(), index);
	}

	@Override
	public int getInventoryStackLimit() {
		return 10;
	}

	@Override
	public int[] getSlotsForFace(Direction side) {
		return SLOTS;
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, Direction direction) {
		return isItemValidForSlot(slot, stack);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
		return true;
	}

	@Override
	public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity player) {
		return new ContainerSaw(windowID, playerInventory, new PacketBuffer(Unpooled.buffer()).writeBlockPos(pos));
	}

	@Override
	public ITextComponent getDisplayName() {
		return new StringTextComponent("Mob Masher"); //TODO localise
	}
}
