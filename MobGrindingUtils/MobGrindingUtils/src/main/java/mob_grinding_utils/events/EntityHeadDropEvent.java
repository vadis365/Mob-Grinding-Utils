package mob_grinding_utils.events;

import java.util.Collection;

import com.mojang.authlib.GameProfile;

import mob_grinding_utils.items.ItemImaginaryInvisibleNotReallyThereSword;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.WitherSkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EntityHeadDropEvent {

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void dropEvent(LivingDropsEvent event) {
		if (event.getEntityLiving().getEntityWorld().isRemote)
			return;
		if (event.getEntityLiving().getHealth() > 0.0F)
			return;
		int beheadingLevel = 0;
		if (event.getSource().getTrueSource() instanceof PlayerEntity) {
			PlayerEntity fakePlayer = (PlayerEntity) event.getSource().getTrueSource();
			if (fakePlayer.getDisplayName().toString().matches(new TranslationTextComponent("fakeplayer.mob_masher").toString())) {
				if (fakePlayer.getHeldItemMainhand().getItem() instanceof ItemImaginaryInvisibleNotReallyThereSword) {
					ItemStack tempSword = fakePlayer.getHeldItemMainhand();
					if (tempSword.hasTag() && tempSword.getTag().contains("beheadingValue"))
						beheadingLevel = tempSword.getTag().getInt("beheadingValue");
					int dropChance = event.getEntityLiving().getEntityWorld().rand.nextInt(10);
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
		if (target.isChild())
			return ItemStack.EMPTY;
		
		/*TODO enable this and fix EndeIO compat 
		if (target instanceof EndermanEntity)
			if (Loader.isModLoaded("enderio"))
				return new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("enderio:block_enderman_skull")), 1, 0);
 		*/		
		
		
		/* TODO remove hardcoded mod compat with raiders and WitherCrumbs/HeadCrumbs
		if (target instanceof MobEntity) {
			if (Loader.isModLoaded("headcrumbs"))
				if (isHeadCrumb(target))
				return createHeadFor(getPlayerByUsername(target.getName()));
			if (Loader.isModLoaded("raiders"))
				if (isPlayerRaider(target))
				return createHeadFor(getPlayerByUsername(target.getName()));
		}
		*/
		
		if (target instanceof CreeperEntity)
			return new ItemStack(Items.CREEPER_HEAD, 1);
		if (target instanceof SkeletonEntity)
			return new ItemStack(Items.SKELETON_SKULL, 1);
		if (target instanceof WitherSkeletonEntity)
			return new ItemStack(Items.WITHER_SKELETON_SKULL, 1);
		if (target instanceof ZombieEntity)// && !(target instanceof PigZombieEntity)) whatever these are now
			return new ItemStack(Items.ZOMBIE_HEAD, 1);
		if (target instanceof PlayerEntity)
			return createHeadFor((PlayerEntity) target);
		return ItemStack.EMPTY;
	}
	
	// TODO remove hardcoded mod compat with raiders and WitherCrumbs/HeadCrumbs
/*
	public static boolean isHeadCrumb(Entity entity) {
		ResourceLocation resourcelocation = EntityList.getKey(entity);
		return resourcelocation.toString().equals("headcrumbs:human");
	}

	public static boolean isPlayerRaider(Entity entity) {
		ResourceLocation resourcelocation = EntityList.getKey(entity);
		if(resourcelocation.toString().equals("raiders:raider"))
			return true;
		if(resourcelocation.toString().equals("raiders:brute"))
			return true;
		if(resourcelocation.toString().equals("raiders:witch"))
			return true;
		if(resourcelocation.toString().equals("raiders:tweaker"))
			return true;
		if(resourcelocation.toString().equals("raiders:pyromaniac"))
			return true;
		if(resourcelocation.toString().equals("raiders:ranger"))
			return true;
		return false;
	}
*/
	public static GameProfile getPlayerByUsername(String name) {
		return new GameProfile(null, name);
	}

	public static ItemStack createHeadFor(PlayerEntity player) {
		return createHeadFor(player.getGameProfile());
	}

	public static ItemStack createHeadFor(GameProfile profile) {
		ItemStack stack = new ItemStack(Items.PLAYER_HEAD, 1);
		stack.setTag(new CompoundNBT());
		CompoundNBT profileData = new CompoundNBT();
		NBTUtil.writeGameProfile(profileData, profile);
		stack.getTag().put("SkullOwner", profileData);
		return stack;
	}

	private void addDrop(ItemStack stack, LivingEntity entity, Collection<ItemEntity> collection) {
		if (stack.getCount() <= 0)
			return;
		ItemEntity entityItem = new ItemEntity(entity.getEntityWorld(), entity.getPosX(), entity.getPosY(), entity.getPosZ(), stack);
		entityItem.setDefaultPickupDelay();
		collection.add(entityItem);
	}
}