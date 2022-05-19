package mob_grinding_utils.events;

import java.util.Collection;
import java.util.Optional;

import com.mojang.authlib.GameProfile;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.fakeplayer.MGUFakePlayer;
import mob_grinding_utils.items.ItemImaginaryInvisibleNotReallyThereSword;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;

public class EntityHeadDropEvent {

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void dropEvent(LivingDropsEvent event) {
		if (event.getEntityLiving().level.isClientSide)
			return;
		if (event.getEntityLiving().getHealth() > 0.0F)
			return;
		int beheadingLevel = 0;
		if (event.getSource().getEntity() instanceof MGUFakePlayer fakePlayer) {
			if (fakePlayer.getMainHandItem().getItem() instanceof ItemImaginaryInvisibleNotReallyThereSword) {
				ItemStack tempSword = fakePlayer.getMainHandItem();
				if (tempSword.hasTag() && tempSword.getTag().contains("beheadingValue"))
					beheadingLevel = tempSword.getTag().getInt("beheadingValue");
				int dropChance = event.getEntityLiving().getCommandSenderWorld().random.nextInt(10);
				if (dropChance < beheadingLevel) {
					ItemStack stack = getHeadFromEntity(event.getEntityLiving());
					if (!stack.isEmpty())
						addDrop(stack, event.getEntityLiving(), event.getDrops());
				}
			}
		}
	}

	public static ItemStack getHeadFromEntity(LivingEntity target) {
		if (target.isBaby())
			return ItemStack.EMPTY;

		var recipeOptional = MobGrindingUtils.BEHEADING_RECIPES.stream().filter(recipe -> recipe.matches(target.getType())).findFirst();
		if (recipeOptional.isPresent()) {
			return recipeOptional.get().getResultItem();
		}
		
		if (target instanceof Mob) {
			if (ModList.get().isLoaded("player_mobs"))
				if (isPlayerMob(target))
				return createHeadFor(getPlayerByUsername(target.getName().getString()));
		}
		if (target instanceof Player)
			return createHeadFor((Player) target);

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