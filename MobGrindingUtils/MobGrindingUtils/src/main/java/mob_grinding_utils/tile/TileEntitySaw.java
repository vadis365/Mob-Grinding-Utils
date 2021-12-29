package mob_grinding_utils.tile;

import java.util.List;

import io.netty.buffer.Unpooled;
import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.ModItems;
import mob_grinding_utils.blocks.BlockSaw;
import mob_grinding_utils.fakeplayer.MGUFakePlayer;
import mob_grinding_utils.inventory.server.ContainerSaw;
import mob_grinding_utils.items.ItemSawUpgrade;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.AABB;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;

public class TileEntitySaw extends TileEntityInventoryHelper implements TickableBlockEntity, MenuProvider {

	public boolean active;
	public int animationTicks, prevAnimationTicks;
    private static final int[] SLOTS = new int[] {0, 1, 2, 3, 4, 5};

	public TileEntitySaw() {
		super(ModBlocks.SAW.getTileEntityType(), 6);
	}

	@Override
	public void tick() {
		if (getLevel().isClientSide && active) {
			prevAnimationTicks = animationTicks;
			if (animationTicks < 360)
				animationTicks += 18;
			if (animationTicks >= 360) {
				animationTicks -= 360;
				prevAnimationTicks -= 360;
			}
		}

		if (getLevel().isClientSide && !active)
			prevAnimationTicks = animationTicks = 0;

		if (!getLevel().isClientSide && getLevel().getGameTime() % 10 == 0 && getLevel().getBlockState(worldPosition).getBlock() instanceof BlockSaw)
			if (getLevel().getBlockState(worldPosition).getValue(BlockSaw.POWERED))
				activateBlock();
	}

	public void setActive(boolean isActive) {
		active = isActive;
		getLevel().sendBlockUpdated(worldPosition, getLevel().getBlockState(worldPosition), getLevel().getBlockState(worldPosition), 3);
	}

	protected Entity activateBlock() {
		List<LivingEntity> list = getLevel().getEntitiesOfClass(LivingEntity.class, new AABB(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), worldPosition.getX() + 1D, worldPosition.getY() + 1D, worldPosition.getZ() + 1D).inflate(0.0625D, 0.0625D, 0.0625D));
		for (int i = 0; i < list.size(); i++) {
			Entity entity = list.get(i);
			if (entity != null) {
				if (entity instanceof LivingEntity) {
					MGUFakePlayer fakePlayer = MGUFakePlayer.get((ServerLevel)getLevel(), this.worldPosition.getX(), -100D, this.worldPosition.getZ()).get();
					ItemStack tempSword = new ItemStack(ModItems.NULL_SWORD.get(), 1);

					if(!tempSword.hasTag())
						tempSword.setTag(new CompoundTag());

					if(hasSharpnessUpgrade())
						tempSword.enchant(Enchantments.SHARPNESS, getItems().get(0).getCount() * 10);
					if(hasLootingUpgrade())
						tempSword.enchant(Enchantments.MOB_LOOTING, getItems().get(1).getCount());
					if(hasFlameUpgrade())
						tempSword.enchant(Enchantments.FIRE_ASPECT, getItems().get(2).getCount());
					if(hasSmiteUpgrade())
						tempSword.enchant(Enchantments.SMITE, getItems().get(3).getCount() * 10);
					if(hasArthropodUpgrade())
						tempSword.enchant(Enchantments.BANE_OF_ARTHROPODS, getItems().get(4).getCount() * 10);
					if(hasBeheadingUpgrade())
						tempSword.getTag().putInt("beheadingValue", getItems().get(5).getCount());

					fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, tempSword);
					fakePlayer.attack(entity);
					fakePlayer.resetAttackStrengthTicker();
					fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
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
	public CompoundTag save(CompoundTag nbt) {
		super.save(nbt);
		nbt.putBoolean("active", active);
		return nbt;
	}

	@Override
	public void load(BlockState state, CompoundTag nbt) {
		super.load(state, nbt);
		active = nbt.getBoolean("active");
	}

	@Override
    public CompoundTag getUpdateTag() {
		CompoundTag tag = new CompoundTag();
        return save(tag);
    }

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		CompoundTag tag = new CompoundTag();
		save(tag);
		return new ClientboundBlockEntityDataPacket(getBlockPos(), 0, tag);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet) {
		super.onDataPacket(net, packet);
		load(getBlockState(), packet.getTag());
		if(!getLevel().isClientSide)
			getLevel().sendBlockUpdated(worldPosition, getLevel().getBlockState(worldPosition), getLevel().getBlockState(worldPosition), 3);
		return;
	}

	@Override
	public boolean canPlaceItem(int slot, ItemStack stack) {
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
	public ItemStack removeItemNoUpdate(int index) {
		return ContainerHelper.takeItem(getItems(), index);
	}

	@Override
	public int getMaxStackSize() {
		return 10;
	}

	@Override
	public int[] getSlotsForFace(Direction side) {
		return SLOTS;
	}

	@Override
	public boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction direction) {
		return canPlaceItem(slot, stack);
	}

	@Override
	public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
		return true;
	}

	@Override
	public AbstractContainerMenu createMenu(int windowID, Inventory playerInventory, Player player) {
		return new ContainerSaw(windowID, playerInventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(worldPosition));
	}

	@Override
	public Component getDisplayName() {
		return new TextComponent("Mob Masher"); //TODO localise
	}
}
