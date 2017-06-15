package mob_grinding_utils.capability.bossbars;

import mob_grinding_utils.capability.base.ISerializableCapability;

public interface IBossBarCapability extends ISerializableCapability {
	/**
	 * Sets whether the wither bar should be rendered
	 * @param render
	 */
	public void setRenderWitherBar(boolean render);
	
	/**
	 * Sets whether the ender dragon bar should be rendered
	 * @return
	 */
	public void setRenderEnderDragonBar(boolean render);
	
	/**
	 * Returns whether the wither bar should be rendered
	 * @return
	 */
	public boolean renderWitherBar();

	/**
	 * Returns whether the ender dragon bar should be rendered
	 * @return
	 */
	public boolean renderEnderDragonBar();
}
