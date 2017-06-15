package mob_grinding_utils.proxy;

import mob_grinding_utils.inventory.client.GuiAbsorptionHopper;
import mob_grinding_utils.inventory.client.GuiFan;
import mob_grinding_utils.inventory.client.GuiSaw;
import mob_grinding_utils.inventory.server.ContainerAbsorptionHopper;
import mob_grinding_utils.inventory.server.ContainerFan;
import mob_grinding_utils.inventory.server.ContainerSaw;
import mob_grinding_utils.tile.TileEntityAbsorptionHopper;
import mob_grinding_utils.tile.TileEntityFan;
import mob_grinding_utils.tile.TileEntitySaw;
import mob_grinding_utils.tile.TileEntitySinkTank;
import mob_grinding_utils.tile.TileEntityTank;
import mob_grinding_utils.tile.TileEntityXPTap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy implements IGuiHandler {
	public final int GUI_ID_ABSORPTION_HOPPER = 0, GUI_ID_FAN = 1, GUI_ID_SAW = 2;

	public void registerRenderers() {}

	public void registerTileEntities() {
		registerTileEntity(TileEntityFan.class, "fan");
		registerTileEntity(TileEntityAbsorptionHopper.class, "absorption_hopper");
		registerTileEntity(TileEntityTank.class, "tank");
		registerTileEntity(TileEntitySinkTank.class, "tank_sink");
		registerTileEntity(TileEntityXPTap.class, "xp_tap");
		registerTileEntity(TileEntitySaw.class, "saw");
	}

	private void registerTileEntity(Class<? extends TileEntity> cls, String baseName) {
		GameRegistry.registerTileEntity(cls, "tile.mob_grinding_utils." + baseName);
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == GUI_ID_ABSORPTION_HOPPER) {
			BlockPos pos = new BlockPos(x, y, z);
			TileEntity tileentity = world.getTileEntity(pos);
			if (tileentity instanceof TileEntityAbsorptionHopper)
				return new ContainerAbsorptionHopper(player, (TileEntityAbsorptionHopper) tileentity);
		}

		if (ID == GUI_ID_FAN) {
			BlockPos pos = new BlockPos(x, y, z);
			TileEntity tileentity = world.getTileEntity(pos);
			if (tileentity instanceof TileEntityFan)
				return new ContainerFan(player, (TileEntityFan) tileentity);
		}

		if (ID == GUI_ID_SAW) {
			BlockPos pos = new BlockPos(x, y, z);
			TileEntity tileentity = world.getTileEntity(pos);
			if (tileentity instanceof TileEntitySaw)
				return new ContainerSaw(player, (TileEntitySaw) tileentity);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == GUI_ID_ABSORPTION_HOPPER) {
			BlockPos pos = new BlockPos(x, y, z);
			TileEntity tileentity = world.getTileEntity(pos);
			if (tileentity instanceof TileEntityAbsorptionHopper)
				return new GuiAbsorptionHopper(player, (TileEntityAbsorptionHopper) tileentity);
		}

		if (ID == GUI_ID_FAN) {
			BlockPos pos = new BlockPos(x, y, z);
			TileEntity tileentity = world.getTileEntity(pos);
			if (tileentity instanceof TileEntityFan)
				return new GuiFan(player, (TileEntityFan) tileentity);
		}

		if (ID == GUI_ID_SAW) {
			BlockPos pos = new BlockPos(x, y, z);
			TileEntity tileentity = world.getTileEntity(pos);
			if (tileentity instanceof TileEntitySaw)
				return new GuiSaw(player, (TileEntitySaw) tileentity);
		}
		return null;
	}

	public void spawnGlitterParticles(World worldObj, double x, double y, double z, double velocityX, double velocityY, double velocityZ, int count, int color, float scale) {
	}

	public void postInit() {
	}

}
