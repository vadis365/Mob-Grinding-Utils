package mob_grinding_utils.events;

import java.lang.reflect.Method;
import java.util.List;

import com.mojang.authlib.GameProfile;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.blocks.BlockDragonMuffler;
import mob_grinding_utils.blocks.BlockWitherMuffler;
import mob_grinding_utils.items.ItemImaginaryInvisibleNotReallyThereSword;
import mob_grinding_utils.network.ChickenSyncMessage;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.SkeletonType;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ServerEventHandlers {

	//Mob Swab Event
	@SubscribeEvent
	public void clickOnMob(EntityInteract event) {
		EntityLivingBase entity = (EntityLivingBase) event.getTarget();
		World world = entity.worldObj;
		if (!(entity instanceof EntityPlayer)) {
			if (event.getItemStack() != null && event.getItemStack().getItem() == MobGrindingUtils.MOB_SWAB && event.getItemStack().getItemDamage() == 0) {
				if (!world.isRemote) {
					String mobName = EntityList.getEntityString(entity);
					ItemStack stack2 = new ItemStack(MobGrindingUtils.MOB_SWAB, 1, 1);
					if (!stack2.hasTagCompound())
						stack2.setTagCompound(new NBTTagCompound());
					if (!stack2.getTagCompound().hasKey("mguMobName"))
						stack2.getTagCompound().setString("mguMobName", mobName);
					event.getItemStack().stackSize--;
					event.getEntityPlayer().setHeldItem(event.getHand(), stack2);
				}
			}
		}
	}

	//Chicken Interaction
	@SubscribeEvent
	public void clickOnChicken(EntityInteract event) {
		if (event.getTarget() instanceof EntityLivingBase) {
			EntityLivingBase entity = (EntityLivingBase) event.getTarget();
			if (entity instanceof EntityChicken && !entity.isChild()) {
				World world = entity.worldObj;
				if (event.getItemStack() != null && event.getItemStack().getItem() == MobGrindingUtils.GM_CHICKEN_FEED) {
					if (!world.isRemote) {
						NBTTagCompound nbt = new NBTTagCompound();
						nbt = entity.getEntityData();
						if (!nbt.hasKey("shouldExplode")) {
							entity.setEntityInvulnerable(true);
							nbt.setBoolean("shouldExplode", true);
							nbt.setInteger("countDown", 0);
							if (event.getItemStack().hasTagCompound() && event.getItemStack().getTagCompound().hasKey("mguMobName"))
								nbt.setString("mguMobName", event.getItemStack().getTagCompound().getString("mguMobName"));
							MobGrindingUtils.NETWORK_WRAPPER.sendToAll(new ChickenSyncMessage(entity, nbt));
						}
						entity.motionY += (0.06D * (double) (10) - entity.motionY) * 0.2D;
						((EntityChicken) entity).func_189654_d(true);

						if (!event.getEntityPlayer().capabilities.isCreativeMode)
							event.getItemStack().stackSize--;
					}
				}
			}
		}
	}

	//Chicken Fuse Event
	@SubscribeEvent
	public void startChickenFuse(LivingEvent event) {
		EntityLivingBase entity = (EntityLivingBase) event.getEntity();
		if (entity instanceof EntityChicken) {
			World world = entity.worldObj;
			if (!world.isRemote) {
				NBTTagCompound nbt = new NBTTagCompound();
				nbt = entity.getEntityData();
				if (nbt.hasKey("shouldExplode")) {
					int startTime = event.getEntity().getEntityData().getInteger("countDown");

					if (startTime <= 19) {
						nbt.setInteger("countDown", nbt.getInteger("countDown") + 1);
						MobGrindingUtils.NETWORK_WRAPPER.sendToAll(new ChickenSyncMessage(entity, nbt));
					}

					if (startTime >= 20) {
						entity.setDead();
						entity.playSound(SoundEvents.ENTITY_CHICKEN_DEATH, 1F, 1F);
						entity.playSound(MobGrindingUtils.CHICKEN_RISE, 0.5F, 1F);
						String name = event.getEntity().getEntityData().getString("mguMobName");

						if (EntityList.ENTITY_EGGS.containsKey(name)) {
							ItemStack mobEgg = new ItemStack(Items.SPAWN_EGG);
							NBTTagCompound eggData = new NBTTagCompound();
							NBTTagCompound mobData = new NBTTagCompound();
							mobData.setString("id", name);
							eggData.setTag("EntityTag", mobData);
							mobEgg.setTagCompound(eggData);
							entity.entityDropItem(mobEgg, 0.0F);
						}

						for (int k = 0; k < 4; ++k) {
							ItemStack stack = new ItemStack(Items.FEATHER);
							EntityItem feather = new EntityItem(world, entity.posX + (double) (world.rand.nextFloat() * entity.width * 2.0F) - (double) entity.width, entity.posY + (double) (world.rand.nextFloat() * entity.height), entity.posZ + (double) (world.rand.nextFloat() * entity.width * 2.0F) - (double) entity.width, stack);
							world.spawnEntityInWorld(feather);
						}
					}
				}
			}
		}
	}
	
	//Head Drops Event
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

	//Local Dragon Death
	@SubscribeEvent
	public void onDragonDeath(LivingDeathEvent event) {
		if (event.getEntity() instanceof EntityDragon) {
			EntityDragon dragon = (EntityDragon) event.getEntity();
			World world = dragon.worldObj;
			boolean playsound = true;
			if (!world.isRemote) {
				for (int x = -8; x < 8; x++)
					for (int y = -8; y < 8; y++)
						for (int z = -8; z < 8; z++)
							if ((world.getBlockState(new BlockPos(dragon.posX + x, dragon.posY + y, dragon.posZ + z)).getBlock() instanceof BlockDragonMuffler))
								playsound = false;
				if (playsound) {
					world.playSound(null, dragon.posX, dragon.posY, dragon.posZ, MobGrindingUtils.ENTITY_DRAGON_DEATH_LOCAL, SoundCategory.HOSTILE, 5.0F, 1.0F);
				}
			}
		}
	}

	//Local Wither Boss	
	@SubscribeEvent
	public void onWitherBossDeath(LivingDeathEvent event) {
		if (event.getEntity() instanceof EntityWither) {
			EntityWither wither = (EntityWither) event.getEntity();
			World world = wither.worldObj;
			boolean playsound = true;
			if (!world.isRemote) {
				for (int x = -8; x < 8; x++)
					for (int y = -8; y < 8; y++)
						for (int z = -8; z < 8; z++)
							if ((world.getBlockState(new BlockPos(wither.posX + x, wither.posY + y, wither.posZ + z)).getBlock() instanceof BlockWitherMuffler))
								playsound = false;
				if (playsound) {
					world.playSound(null, wither.posX, wither.posY, wither.posZ, MobGrindingUtils.ENTITY_WITHER_SPAWN_LOCAL, SoundCategory.HOSTILE, 1.0F, 1.0F);
					world.playSound(null, wither.posX, wither.posY, wither.posZ, MobGrindingUtils.ENTITY_WITHER_DEATH_LOCAL, SoundCategory.HOSTILE, 1.0F, 1.0F);
				}
			}
		}
	}

	//Spike XP Drop Event
	public static final Method xpPoints = getExperiencePoints();

	@SubscribeEvent
	public void dropXP(LivingDropsEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		World world = entity.worldObj;
		if (entity != null) {
			if (!world.isRemote && !event.isRecentlyHit() && event.getSource() == MobGrindingUtils.SPIKE_DAMAGE) {
				int xp = 0;
				try {
					xp = (Integer) xpPoints.invoke(entity, FakePlayerFactory.getMinecraft((WorldServer) world));
				} catch (Exception e) {
				}
				while (xp > 0) {
					int cap = EntityXPOrb.getXPSplit(xp);
					xp -= cap;
					entity.worldObj.spawnEntityInWorld(new EntityXPOrb(entity.worldObj, entity.posX, entity.posY, entity.posZ, cap));
				}
			}
		}
	}

	public static Method getExperiencePoints() {
		Method method = null;
		try {
			method = EntityLiving.class.getDeclaredMethod("getExperiencePoints", EntityPlayer.class);
			method.setAccessible(true);
		} catch (Exception e) {
		}
		try {
			method = EntityLiving.class.getDeclaredMethod("func_70693_a", EntityPlayer.class);
			method.setAccessible(true);
		} catch (Exception e) {
		}
		return method;
	}
}
