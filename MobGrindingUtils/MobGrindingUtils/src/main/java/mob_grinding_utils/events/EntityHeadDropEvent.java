package mob_grinding_utils.events;

import java.util.Collection;
import java.util.Optional;

import com.mojang.authlib.GameProfile;

import mob_grinding_utils.items.ItemImaginaryInvisibleNotReallyThereSword;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;

public class EntityHeadDropEvent {

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void dropEvent(LivingDropsEvent event) {
		if (event.getEntityLiving().getCommandSenderWorld().isClientSide)
			return;
		if (event.getEntityLiving().getHealth() > 0.0F)
			return;
		int beheadingLevel = 0;
		if (event.getSource().getEntity() instanceof FakePlayer) {
			FakePlayer fakePlayer = (FakePlayer) event.getSource().getEntity();
			if (fakePlayer.getDisplayName().getString().matches(new TranslatableComponent("fakeplayer.mob_masher").getString())) {
				if (fakePlayer.getMainHandItem().getItem() instanceof ItemImaginaryInvisibleNotReallyThereSword) {
					ItemStack tempSword = fakePlayer.getMainHandItem();
					if (tempSword.hasTag() && tempSword.getTag().contains("beheadingValue"))
						beheadingLevel = tempSword.getTag().getInt("beheadingValue");
					int dropChance = event.getEntityLiving().getCommandSenderWorld().random.nextInt(10);
					if (dropChance < beheadingLevel) {
						ItemStack stack = getHeadfromEntity(event.getEntityLiving());
						if (!stack.isEmpty())
							addDrop(stack, event.getEntityLiving(), event.getDrops());
					}
				}
			}
		}
	}

	public static ItemStack getHeadfromEntity(LivingEntity target) {
		if (target.isBaby())
			return ItemStack.EMPTY;
		
		/*TODO enable this and fix EndeIO compat 
		if (target instanceof EndermanEntity)
			if (ModList.get().isLoaded("enderio"))
				return new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("enderio:block_enderman_skull")), 1);
 		*/		
		
		if (target instanceof Mob) {
			if (ModList.get().isLoaded("player_mobs"))
				if (isPlayerMob(target))
				return createHeadFor(getPlayerByUsername(target.getName().getString()));
		}
		
		
		if (target instanceof Creeper)
			return new ItemStack(Items.CREEPER_HEAD, 1);
		if (target instanceof Skeleton)
			return new ItemStack(Items.SKELETON_SKULL, 1);
		if (target instanceof WitherSkeleton)
			return new ItemStack(Items.WITHER_SKELETON_SKULL, 1);
		if (target instanceof Zombie)// && !(target instanceof PigZombieEntity)) whatever these are now
			return new ItemStack(Items.ZOMBIE_HEAD, 1);
		if (target instanceof Player)
			return createHeadFor((Player) target);
		if (target instanceof EnderDragon)
			return new ItemStack(Items.DRAGON_HEAD, 1);
		return ItemStack.EMPTY;
	}
	

	public static boolean isPlayerMob(Entity entity) {
		Optional<EntityType<?>> entityMob = EntityType.byString("player_mobs:player_mob");
		return entityMob.isPresent() && entityMob.get().equals(entity.getType());
	}

	public static GameProfile getPlayerByUsername(String name) {
		return new GameProfile(null, name);
	}

	public static ItemStack createHeadFor(Player player) {
		return createHeadFor(player.getGameProfile());
	}

	public static ItemStack createHeadFor(GameProfile profile) {
		ItemStack stack = new ItemStack(Items.PLAYER_HEAD, 1);
		stack.setTag(new CompoundTag());
		CompoundTag profileData = new CompoundTag();
		NbtUtils.writeGameProfile(profileData, profile);
		stack.getTag().put("SkullOwner", profileData);
		return stack;
	}

	private void addDrop(ItemStack stack, LivingEntity entity, Collection<ItemEntity> collection) {
		if (stack.getCount() <= 0)
			return;
		ItemEntity entityItem = new ItemEntity(entity.getCommandSenderWorld(), entity.getX(), entity.getY(), entity.getZ(), stack);
		entityItem.setDefaultPickUpDelay();
		collection.add(entityItem);
	}
}