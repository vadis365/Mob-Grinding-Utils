package mob_grinding_utils.capability.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import com.google.common.base.Preconditions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.StartTracking;
import net.minecraftforge.event.entity.player.PlayerEvent.StopTracking;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EntityCapabilityHandler {
	private static final List<EntityCapability<?, ?, ? extends Entity>> REGISTERED_CAPABILITIES = new ArrayList<EntityCapability<?, ?, ? extends Entity>>();
	private static final Map<ResourceLocation, EntityCapability<?, ?, ? extends Entity>> ID_CAPABILITY_MAP = new HashMap<ResourceLocation, EntityCapability<?, ?, ? extends Entity>>();

	private static final Map<ServerPlayerEntity, List<EntityCapabilityTracker>> TRACKER_MAP = new HashMap<ServerPlayerEntity, List<EntityCapabilityTracker>>();

	private static int updateTimer = 0;

	/**
	 * Registers an entity capability
	 * @param entityCapability
	 */
	public static <T, F extends EntityCapability<F, T, E>, E extends Entity> void registerEntityCapability(EntityCapability<F, T, E> entityCapability) {
		//Make sure the entity capability is the implementation of the capability
		Preconditions.checkState(entityCapability.getCapabilityClass().isAssignableFrom(entityCapability.getClass()), "Entity capability %s must implement %s", entityCapability.getClass().getName(), entityCapability.getCapabilityClass().getName());
		REGISTERED_CAPABILITIES.add(entityCapability);
	}

	/**
	 * Registers all capabilities to the {@link CapabilityManager}. Must be called during pre init.
	 */
	public static void registerCapabilities() {
		//Preconditions.checkState(Loader.instance().isInState(LoaderState.PREINITIALIZATION)); //todo dunno where this goes now...
		for(EntityCapability<?, ?, ?> capability : REGISTERED_CAPABILITIES) {
			registerCapability(capability);
		}
	}

	/**
	 * Returns the capability with the specified ID
	 * @param id
	 */
	@SuppressWarnings("unchecked")
	public static <E extends Entity> EntityCapability<?, ?, E> getCapability(ResourceLocation id, E entity) {
		EntityCapability<?, ?, ?> entityCapability = ID_CAPABILITY_MAP.get(id);
		/* //todo change to optional check...
		if(entityCapability != null && entity.hasCapability(entityCapability.getCapability(), null)) {
			return (EntityCapability<?, ?, E>) entity.getCapability(entityCapability.getCapability(), null);
		}
		 */
		return null;
	}

	private static <T, E extends Entity> void registerCapability(final EntityCapability<?, T, E> capability) {
		CapabilityManager.INSTANCE.register(capability.getCapabilityClass(), new IStorage<T>() {
			@Override
			public final INBT writeNBT(Capability<T> capability, T instance, Direction side) {
				if(instance instanceof ISerializableCapability) {
					CompoundNBT nbt = new CompoundNBT();
					((ISerializableCapability)instance).writeToNBT(nbt);
					return nbt;
				}
				return null;
			}

			@Override
			public final void readNBT(Capability<T> capability, T instance, Direction side, INBT nbt) {
				if(instance instanceof ISerializableCapability && nbt instanceof CompoundNBT) {
					((ISerializableCapability)instance).readFromNBT((CompoundNBT)nbt);
				}
			}
		}, new Callable<T>() {
			@SuppressWarnings("unchecked")
			@Override
			public final T call() throws Exception {
				return (T) capability.getDefaultCapabilityImplementation();
			}
		});
		ID_CAPABILITY_MAP.put(capability.getID(), capability);
	}

	@SubscribeEvent
	public static void onAttachCapabilities(final AttachCapabilitiesEvent<Entity> event) {
		for(EntityCapability<?, ?, ?> entityCapability : REGISTERED_CAPABILITIES) {
			if(entityCapability.isApplicable(event.getObject())) {
				final Capability<?> capabilityInstance = entityCapability.getCapability();
/*
				event.addCapability(entityCapability.getID(), new ICapabilitySerializable<CompoundNBT>() { //todo
					private Object entityCapability = this.getNewInstance();

					private EntityCapability<?, ?, ?> getNewInstance() {
						EntityCapability<?, ?, ?> entityCapability = (EntityCapability<?, ?, ?>)capabilityInstance.getDefaultInstance();
						entityCapability.setEntity(event.getObject());
						entityCapability.init();
						return entityCapability;
					}
					@SuppressWarnings("unchecked")
					@Override //todo this has entirely changed, removed hascapability too
					public <T> T getCapability(Capability<T> capability, Direction facing) {
						return capability == capabilityInstance ? (T)this.entityCapability : null;
					}

					@Override
					public CompoundNBT serializeNBT() {
						return this.serialize(capabilityInstance, this.entityCapability);
					}

					@SuppressWarnings("unchecked")
					private <T> CompoundNBT serialize(Capability<T> capability, Object instance) {
						return (CompoundNBT) capability.getStorage().writeNBT(capability, (T)instance, null);
					}

					@Override
					public void deserializeNBT(CompoundNBT nbt) {
						this.deserialize(capabilityInstance, this.entityCapability, nbt);
					}

					@SuppressWarnings("unchecked")
					private <T> void deserialize(Capability<T> capability, Object instance, CompoundNBT nbt) {
						capability.getStorage().readNBT(capability, (T)instance, null, nbt);
					}
				});
*/
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerJoin(EntityJoinWorldEvent event) {
		if (!event.getWorld().isRemote && event.getEntity() instanceof ServerPlayerEntity) {
			ServerPlayerEntity player = (ServerPlayerEntity) event.getEntity();
			addTrackers(player, player);
		}
	}

	@SubscribeEvent
	public static void onEntityStartTracking(StartTracking event) {
		if(event.getPlayer() instanceof ServerPlayerEntity) {
			addTrackers((ServerPlayerEntity)event.getPlayer(), event.getTarget());
		}
	}

	@SubscribeEvent
	public static void onEntityStopTracking(StopTracking event) {
		if(event.getPlayer() instanceof ServerPlayerEntity) {
			removeTrackers((ServerPlayerEntity)event.getPlayer(), event.getTarget());
		}
	}

	@SubscribeEvent
	public static void onEntityUpdate(LivingUpdateEvent event) {
		if(!event.getEntity().getEntityWorld().isRemote && event.getEntity() instanceof ServerPlayerEntity)  {
			ServerPlayerEntity player = (ServerPlayerEntity) event.getEntity();
			List<EntityCapabilityTracker> trackers = TRACKER_MAP.get(player);
			if(trackers != null) {
				for(EntityCapabilityTracker tracker : trackers) {
					tracker.update();
				}
			}
		}
	}

	@SubscribeEvent
	public static void onEntityChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
		if(!event.getPlayer().getEntityWorld().isRemote && event.getPlayer() instanceof ServerPlayerEntity)  {
			ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
			List<EntityCapabilityTracker> trackers = TRACKER_MAP.get(player);
			if(trackers != null) {
				for(EntityCapabilityTracker tracker : trackers) {
					tracker.sendPacket();
				}
			}
		}
	}

	@SubscribeEvent
	public static void onServerTickEvent(TickEvent.ServerTickEvent event) {
		if(event.phase == TickEvent.Phase.END) {
			updateTimer++;
			if(updateTimer > 20) {
				updateTimer = 0;
				Iterator<Entry<ServerPlayerEntity, List<EntityCapabilityTracker>>> it = TRACKER_MAP.entrySet().iterator();
				while(it.hasNext()) {
					Entry<ServerPlayerEntity, List<EntityCapabilityTracker>> entry = it.next();
					ServerPlayerEntity player = entry.getKey();
					if(!player.getServerWorld().getServer().getPlayerList().getPlayers().contains(player))
						it.remove();
				}
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerClone(PlayerEvent.Clone event) {
		//Clone persistent capability properties
		PlayerEntity oldPlayer = event.getOriginal();
		PlayerEntity newPlayer = event.getPlayer();
		List<EntityCapability<?, ?, PlayerEntity>> capabilities = getEntityCapabilities(oldPlayer);
		for(EntityCapability<?, ?, PlayerEntity> capability : capabilities) {
			if(capability.isPersistent() && capability instanceof ISerializableCapability) {
				CompoundNBT nbt = new CompoundNBT();
				((ISerializableCapability)capability).writeToNBT(nbt);
				EntityCapability<?, ?, PlayerEntity> newCapability = capability.getEntityCapability(newPlayer);
				if(newCapability != null && newCapability instanceof ISerializableCapability)
					((ISerializableCapability)newCapability).readFromNBT(nbt);
			}
		}
	}

	/**
	 * Returns a list of all found registered capabilities on an entity
	 * @param entity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static <E extends Entity> List<EntityCapability<?, ?, E>> getEntityCapabilities(E entity) {
		List<EntityCapability<?, ?, E>> capabilities = new ArrayList<EntityCapability<?, ?, E>>();

		for(EntityCapability<?, ?, ?> capability : REGISTERED_CAPABILITIES) {
			//if(entity.hasCapability(capability.getCapability(), null)) //todo this needs to be converted to an optional check
			//	capabilities.add((EntityCapability<?, ?, E>) entity.getCapability(capability.getCapability(), null));
		}

		return capabilities;
	}

	/**
	 * Adds all necessary trackers for an entity
	 * @param watcher
	 * @param target
	 */
	private static void addTrackers(ServerPlayerEntity watcher, Entity target) {
		List<EntityCapability<?, ?, Entity>> entityCapabilities = getEntityCapabilities(target);
		for(EntityCapability<?, ?, Entity> capability : entityCapabilities) {
			if(capability.getTrackingTime() >= 0) {
				List<EntityCapabilityTracker> trackers = TRACKER_MAP.get(watcher);
				if(trackers == null)
					TRACKER_MAP.put(watcher, trackers = new ArrayList<EntityCapabilityTracker>());
				trackers.add(new EntityCapabilityTracker(capability, watcher));
			}
		}
	}

	/**
	 * Removes all necessary trackers for an entity
	 * @param watcher
	 * @param target
	 */
	private static void removeTrackers(ServerPlayerEntity watcher, Entity target) {
		List<EntityCapability<?, ?, Entity>> entityCapabilities = getEntityCapabilities(target);
		for(EntityCapability<?, ?, Entity> capability : entityCapabilities) {
			if(capability.getTrackingTime() >= 0) {
				List<EntityCapabilityTracker> trackers = TRACKER_MAP.get(watcher);
				if(trackers != null) {
					Iterator<EntityCapabilityTracker> it = trackers.iterator();
					while(it.hasNext()) {
						EntityCapabilityTracker tracker = it.next();
						if(tracker.getWatcher() == watcher)
							it.remove();
					}

					if(trackers.isEmpty()) {
						TRACKER_MAP.remove(watcher);
					}
				}
			}
		}
	}
}
