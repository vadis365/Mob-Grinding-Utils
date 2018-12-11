package mob_grinding_utils.events;

import java.util.List;

import com.mojang.authlib.GameProfile;

import mob_grinding_utils.items.ItemImaginaryInvisibleNotReallyThereSword;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EntityHeadDropEvent {

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void dropEvent(LivingDropsEvent event) {
		if (event.getEntityLiving().getEntityWorld().isRemote)
			return;
		if (event.getEntityLiving().getHealth() > 0.0F)
			return;
		int beheadingLevel = 0;
		if (event.getSource().getTrueSource() instanceof EntityPlayer) {
			EntityPlayer fakePlayer = (EntityPlayer) event.getSource().getTrueSource();
			if (fakePlayer.getDisplayNameString().matches(new TextComponentTranslation("fakeplayer.mob_masher").getFormattedText())) {
				if (fakePlayer.getHeldItemMainhand().getItem() instanceof ItemImaginaryInvisibleNotReallyThereSword) {
					ItemStack tempSword = fakePlayer.getHeldItemMainhand();
					if (tempSword.hasTagCompound() && tempSword.getTagCompound().hasKey("beheadingValue"))
						beheadingLevel = tempSword.getTagCompound().getInteger("beheadingValue");
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

	public static ItemStack getHeadfromEntity(EntityLivingBase target) {
		if (target.isChild())
			return ItemStack.EMPTY;

		if (target instanceof EntityEnderman)
			if (Loader.isModLoaded("enderio"))
				return new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("enderio:block_enderman_skull")), 1, 0);
		if (target instanceof EntityMob) {
			if (Loader.isModLoaded("headcrumbs"))
				if (isHeadCrumb(target))
				return createHeadFor(getPlayerByUsername(target.getName()));
			if (Loader.isModLoaded("raiders"))
				if (isPlayerRaider(target))
				return createHeadFor(getPlayerByUsername(target.getName()));
		}
		if (target instanceof EntityCreeper)
			return new ItemStack(Items.SKULL, 1, 4);
		if (target instanceof EntitySkeleton)
			return new ItemStack(Items.SKULL, 1, 0);
		if (target instanceof EntityWitherSkeleton)
			return new ItemStack(Items.SKULL, 1, 1);
		if (target instanceof EntityZombie && !(target instanceof EntityPigZombie))
			return new ItemStack(Items.SKULL, 1, 2);
		if (target instanceof EntityPlayer)
			return createHeadFor((EntityPlayer) target);
		return ItemStack.EMPTY;
	}

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

	public static GameProfile getPlayerByUsername(String name) {
		return new GameProfile(null, name);
	}

	public static ItemStack createHeadFor(EntityPlayer player) {
		return createHeadFor(player.getGameProfile());
	}

	public static ItemStack createHeadFor(GameProfile profile) {
		ItemStack stack = new ItemStack(Items.SKULL, 1, 3);
		stack.setTagCompound(new NBTTagCompound());
		NBTTagCompound profileData = new NBTTagCompound();
		NBTUtil.writeGameProfile(profileData, profile);
		stack.getTagCompound().setTag("SkullOwner", profileData);
		return stack;
	}

	private void addDrop(ItemStack stack, EntityLivingBase entity, List<EntityItem> list) {
		if (stack.getCount() <= 0)
			return;
		EntityItem entityItem = new EntityItem(entity.getEntityWorld(), entity.posX, entity.posY, entity.posZ, stack);
		entityItem.setDefaultPickupDelay();
		list.add(entityItem);
	}
}