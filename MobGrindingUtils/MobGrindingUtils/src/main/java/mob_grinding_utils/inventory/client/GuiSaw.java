package mob_grinding_utils.inventory.client;

import org.lwjgl.opengl.GL11;

import mob_grinding_utils.inventory.server.ContainerSaw;
import mob_grinding_utils.tile.TileEntitySaw;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiSaw extends GuiContainer {

	private static final ResourceLocation GUI_SAW = new ResourceLocation("mob_grinding_utils:textures/gui/saw_gui.png");
	private final TileEntitySaw tile;

	public GuiSaw(EntityPlayer player, TileEntitySaw tile) {
		super(new ContainerSaw(player, tile));
		this.tile = tile;
		ySize = 224;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		fontRendererObj.drawString(I18n.format("Mob Masher"), xSize / 2 - fontRendererObj.getStringWidth(I18n.format("Mob Masher")) / 2, ySize - 218, 4210752);	
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTickTime, int x, int y) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(GUI_SAW);
		int xOffSet = (width - xSize) / 2;
		int yOffSet = (height - ySize) / 2;
		drawTexturedModalRect(xOffSet, yOffSet, 0, 0, xSize, ySize);
	}
}