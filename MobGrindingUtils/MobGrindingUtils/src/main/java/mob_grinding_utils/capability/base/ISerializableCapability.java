package mob_grinding_utils.capability.base;

import net.minecraft.nbt.CompoundNBT;

/**
 * Capabilities can implement this interface to save/load data to/from NBT
 */
public interface ISerializableCapability {
	/**
	 * Writes the data to the nbt
	 * @param nbt
	 */
	public void writeToNBT(CompoundNBT nbt);

	/**
	 * Reads the data from the nbt
	 * @param nbt
	 */
	public void readFromNBT(CompoundNBT nbt);
}
