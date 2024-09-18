package mob_grinding_utils.events;

import com.mojang.authlib.GameProfile;
import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.components.MGUComponents;
import mob_grinding_utils.items.ItemImaginaryInvisibleNotReallyThereSword;
import mob_grinding_utils.util.FakePlayerHandler;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ResolvableProfile;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;

import java.util.Collection;
import java.util.Optional;

public class EntityHeadDropEvent {

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void dropEvent(LivingDropsEvent event) {
		if (event.getEntity().level().isClientSide)
			return;
		if (event.getEntity().getHealth() > 0.0F)
			return;
		int beheadingLevel = 0;
		if (event.getSource().getEntity() instanceof FakePlayer fakePlayer && FakePlayerHandler.isMGUFakePlayer(fakePlayer)) {
			if (fakePlayer.getMainHandItem().getItem() instanceof ItemImaginaryInvisibleNotReallyThereSword) {
				ItemStack tempSword = fakePlayer.getMainHandItem();
				if (tempSword.has(MGUComponents.BEHEADING))
					beheadingLevel = tempSword.getOrDefault(MGUComponents.BEHEADING, 0);
				int dropChance = event.getEntity().getCommandSenderWorld().random.nextInt(10);
				if (dropChance < beheadingLevel) {
					ItemStack stack = getHeadFromEntity(event.getEntity());
					if (!stack.isEmpty())
						addDrop(stack, event.getEntity(), event.getDrops());
				}
			}
		}
	}

	public static ItemStack getHeadFromEntity(LivingEntity target) {
		if (target.isBaby())
			return ItemStack.EMPTY;

		var recipeOptional = MobGrindingUtils.BEHEADING_RECIPES.stream().filter(recipe -> recipe.value().matches(target.getType())).findFirst();
		if (recipeOptional.isPresent()) {
			return recipeOptional.get().value().getResultItem(RegistryAccess.EMPTY);
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
		stack.set(DataComponents.PROFILE, new ResolvableProfile(profile));
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