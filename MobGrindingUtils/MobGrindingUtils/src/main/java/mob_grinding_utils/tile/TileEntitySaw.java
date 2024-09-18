package mob_grinding_utils.tile;

import io.netty.buffer.Unpooled;
import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.ModItems;
import mob_grinding_utils.blocks.BlockSaw;
import mob_grinding_utils.components.MGUComponents;
import mob_grinding_utils.inventory.server.ContainerSaw;
import mob_grinding_utils.items.ItemSawUpgrade;
import mob_grinding_utils.util.FakePlayerHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.util.FakePlayer;

import javax.annotation.Nonnull;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.UUID;

public class TileEntitySaw extends TileEntityInventoryHelper implements MenuProvider {

	public boolean active;
	public int animationTicks, prevAnimationTicks;
	private static final int[] SLOTS = new int[] {0, 1, 2, 3, 4, 5};

	private UUID placer = null;
	private WeakReference<FakePlayer> fakePlayer = new WeakReference<>(null);

	public TileEntitySaw(BlockPos pos, BlockState state) {
		super(ModBlocks.SAW.getTileEntityType(), 6, pos, state);
	}

	public static <T extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, T t) {
		if (t instanceof TileEntitySaw tile ) {
			if (tile.active) {
				tile.prevAnimationTicks = tile.animationTicks;
				if (tile.animationTicks < 360)
					tile.animationTicks += 18;
				if (tile.animationTicks >= 360) {
					tile.animationTicks -= 360;
					tile.prevAnimationTicks -= 360;
				}
			} else
				tile.prevAnimationTicks = tile.animationTicks = 0;
		}
	}
	public static <T extends BlockEntity > void serverTick(Level level, BlockPos blockPos, BlockState blockState, T t) {
		if (t instanceof TileEntitySaw tile) {
			if (level.getGameTime() % 10 == 0 && level.getBlockState(blockPos).getBlock() instanceof BlockSaw)
				if (level.getBlockState(blockPos).getValue(BlockSaw.POWERED))
					tile.activateBlock();
		}
	}

	public void setActive(boolean isActive) {
		active = isActive;
		getLevel().sendBlockUpdated(worldPosition, getLevel().getBlockState(worldPosition), getLevel().getBlockState(worldPosition), 3);
	}

	protected void activateBlock() {
		List<LivingEntity> list = getLevel().getEntitiesOfClass(LivingEntity.class, new AABB(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), worldPosition.getX() + 1D, worldPosition.getY() + 1D, worldPosition.getZ() + 1D).inflate(0.0625D, 0.0625D, 0.0625D));
		if (list.isEmpty() || level == null)
			return;

		fakePlayer = FakePlayerHandler.get(fakePlayer, (ServerLevel) getLevel(), placer, this.worldPosition.atY(-100));
		FakePlayer fakePlayer = this.fakePlayer.get();
		if (fakePlayer == null) return;
		ItemStack tempSword = new ItemStack(ModItems.NULL_SWORD.get(), 1);

		if(hasSharpnessUpgrade())
			tempSword.enchant(level.holderOrThrow(Enchantments.SHARPNESS), getItems().get(0).getCount() * 10);
		if(hasLootingUpgrade())
			tempSword.enchant(level.holderOrThrow(Enchantments.LOOTING), getItems().get(1).getCount());
		if(hasFlameUpgrade())
			tempSword.enchant(level.holderOrThrow(Enchantments.FIRE_ASPECT), getItems().get(2).getCount());
		if(hasSmiteUpgrade())
			tempSword.enchant(level.holderOrThrow(Enchantments.SMITE), getItems().get(3).getCount() * 10);
		if(hasArthropodUpgrade())
			tempSword.enchant(level.holderOrThrow(Enchantments.BANE_OF_ARTHROPODS), getItems().get(4).getCount() * 10);
		if(hasBeheadingUpgrade())
			tempSword.set(MGUComponents.BEHEADING, getItems().get(5).getCount());

		fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, tempSword);

        for (Entity entity : list) {
            if (entity != null) {
                if (entity instanceof LivingEntity) {
                    fakePlayer.attack(entity);
                    fakePlayer.resetAttackStrengthTicker();
                }
            }
        }

		fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
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
	public void loadAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
		super.loadAdditional(nbt, registries);
		active = nbt.getBoolean("active");
		placer = nbt.hasUUID("placer") ? nbt.getUUID("placer") : null;
	}

	@Override
	public void saveAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
		super.saveAdditional(nbt, registries);
		nbt.putBoolean("active", active);
		if (placer != null) nbt.putUUID("placer", placer);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		CompoundTag tag = new CompoundTag();
		saveAdditional(tag, registries);
		return tag;
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		CompoundTag tag = new CompoundTag();
		saveAdditional(tag, level.registryAccess()); //TODO uhhh
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet, HolderLookup.Provider registries) {
		super.onDataPacket(net, packet, registries);
		loadAdditional(packet.getTag(), registries);
		if(!level.isClientSide)
			level.sendBlockUpdated(worldPosition, level.getBlockState(worldPosition), level.getBlockState(worldPosition), 3);
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

	@Nonnull
	@Override
	public Component getDisplayName() {
		return Component.translatable("block.mob_grinding_utils.saw");
	}

	public void setPlacer(Player player) {
		placer = player.getUUID();
	}
}
