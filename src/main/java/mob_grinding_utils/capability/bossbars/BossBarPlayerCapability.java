package mob_grinding_utils.capability.bossbars;

import mob_grinding_utils.capability.base.EntityCapability;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class BossBarPlayerCapability extends EntityCapability<BossBarPlayerCapability, IBossBarCapability, EntityPlayer> implements IBossBarCapability {
	@CapabilityInject(IBossBarCapability.class)
	public static final Capability<IBossBarCapability> CAPABILITY_PLAYER_BOSS_BAR = null;

	@Override
	public ResourceLocation getID() {
		return new ResourceLocation("mob_grinding_utils", "entity_gems");
	}

	@Override
	protected BossBarPlayerCapability getDefaultCapabilityImplementation() {
		return new BossBarPlayerCapability();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Capability<IBossBarCapability> getCapability() {
		return (Capability<IBossBarCapability>) (Capability<?>) CAPABILITY_PLAYER_BOSS_BAR;
	}

	@Override
	protected Class<IBossBarCapability> getCapabilityClass() {
		return IBossBarCapability.class;
	}

	@Override
	public boolean isApplicable(Entity entity) {
		return entity instanceof EntityPlayer;
	}

	@Override
	public int getTrackingTime() {
		return 1;
	}

	@Override
	public boolean isPersistent() {
		return true;
	}

	private boolean showWitherBar = true;
	private boolean showEnderDragonBar = true;

	@Override
	public boolean renderWitherBar() {
		return this.showWitherBar;
	}

	@Override
	public boolean renderEnderDragonBar() {
		return this.showEnderDragonBar;
	}
	
	@Override
	public void setRenderWitherBar(boolean render) {
		this.showWitherBar = render;
		this.setDirty(true);
	}

	@Override
	public void setRenderEnderDragonBar(boolean render) {
		this.showEnderDragonBar = render;
		this.setDirty(true);
	}

	@Override
	public void writeTrackingDataToNBT(NBTTagCompound nbt) {
		this.writeToNBT(nbt);
	}

	@Override
	public void readTrackingDataFromNBT(NBTTagCompound nbt) {
		this.readFromNBT(nbt);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		nbt.setBoolean("witherBar", this.showWitherBar);
		nbt.setBoolean("enderDragonBar", this.showEnderDragonBar);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		this.showWitherBar = nbt.getBoolean("witherBar");
		this.showEnderDragonBar = nbt.getBoolean("enderDragonBar");
		this.setDirty(true);
	}
}
