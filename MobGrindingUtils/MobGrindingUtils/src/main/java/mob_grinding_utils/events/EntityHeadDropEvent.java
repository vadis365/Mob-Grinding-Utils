package mob_grinding_utils.events;

import java.util.List;

import com.mojang.authlib.GameProfile;

import mob_grinding_utils.items.ItemImaginaryInvisibleNotReallyThereSword;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.SkeletonType;
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
		if (event.getEntityLiving().worldObj.isRemote)
			return;
		if (event.getEntityLiving().getHealth() > 0.0F)
			return;
		int beheadingLevel = 0;
		if (event.getSource().getSourceOfDamage() instanceof EntityPlayer) {
			EntityPlayer fakePlayer = (EntityPlayer) event.getSource().getSourceOfDamage();
			if (fakePlayer.getDisplayNameString().matches(new TextComponentTranslation("fakeplayer.mob_masher").getFormattedText())) {
				if (fakePlayer.getHeldItemMainhand().getItem() instanceof ItemImaginaryInvisibleNotReallyThereSword) {
					ItemStack tempSword = fakePlayer.getHeldItemMainhand();
					if (tempSword.hasTagCompound() && tempSword.getTagCompound().hasKey("beheadingValue"))
						beheadingLevel = tempSword.getTagCompound().getInteger("beheadingValue");
					int dropChance = 0 + event.getEntityLiving().worldObj.rand.nextInt(11 - beheadingLevel);
					if (dropChance == 0 && beheadingLevel != 0) {
						ItemStack stack = getHeadfromEntity(event.getEntityLiving());
						if (stack != null)
							addDrop(stack, event.getEntityLiving(), event.getDrops());
					}
				}
			}
		}
	}

	public static ItemStack getHeadfromEntity(EntityLivingBase target) {
		if (target.isChild())
			return null;

		if (target instanceof EntityEnderman)
			if (Loader.isModLoaded("EnderIO"))
				return new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("enderio:blockEndermanSkull")), 1, 0);
		if (target instanceof EntityCreeper)
			return new ItemStack(Items.SKULL, 1, 4);
		if (target instanceof EntitySkeleton) {
			SkeletonType type = ((EntitySkeleton) target).func_189771_df();
			if (type == SkeletonType.WITHER)
				return new ItemStack(Items.SKULL, 1, 1);
			else if (type == SkeletonType.NORMAL)
				return new ItemStack(Items.SKULL, 1, 0);
		}
		if (target instanceof EntityZombie && !(target instanceof EntityPigZombie))
			return new ItemStack(Items.SKULL, 1, 2);
		if (target instanceof EntityPlayer)
			return createHeadFor((EntityPlayer) target);
		return null;
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
		if (stack.stackSize <= 0)
			return;
		EntityItem entityItem = new EntityItem(entity.worldObj, entity.posX, entity.posY, entity.posZ, stack);
		entityItem.setDefaultPickupDelay();
		list.add(entityItem);
	}
}