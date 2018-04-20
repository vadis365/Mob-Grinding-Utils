package mob_grinding_utils.inventory.client;

import mob_grinding_utils.inventory.server.ContainerFan;
import mob_grinding_utils.tile.TileEntityFan;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiFan extends GuiContainer {

	private static final ResourceLocation GUI_FAN = new ResourceLocation("mob_grinding_utils:textures/gui/fan_gui.png");
	private final TileEntityFan tile;

	public GuiFan(EntityPlayer player, TileEntityFan tile) {
		super(new ContainerFan(player, tile));
		this.tile = tile;
		ySize = 224;
	}

	@Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		fontRenderer.drawString(I18n.format(new TextComponentTranslation("tile.mob_grinding_utils.fan.name").getFormattedText()), xSize / 2 - fontRenderer.getStringWidth(I18n.format(new TextComponentTranslation("tile.mob_grinding_utils.fan.name").getFormattedText())) / 2, ySize - 218, 4210752);	
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTickTime, int x, int y) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(GUI_FAN);
		int xOffSet = (width - xSize) / 2;
		int yOffSet = (height - ySize) / 2;
		drawTexturedModalRect(xOffSet, yOffSet, 0, 0, xSize, ySize);
	}
}