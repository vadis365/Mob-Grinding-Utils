package mob_grinding_utils.inventory.client;

import mob_grinding_utils.inventory.server.ContainerSaw;
import mob_grinding_utils.tile.TileEntitySaw;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

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
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		fontRenderer.drawString(I18n.format(new TextComponentTranslation("tile.mob_grinding_utils.saw.name").getFormattedText()), xSize / 2 - fontRenderer.getStringWidth(I18n.format(new TextComponentTranslation("tile.mob_grinding_utils.saw.name").getFormattedText())) / 2, ySize - 218, 4210752);	
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTickTime, int x, int y) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(GUI_SAW);
		int xOffSet = (width - xSize) / 2;
		int yOffSet = (height - ySize) / 2;
		drawTexturedModalRect(xOffSet, yOffSet, 0, 0, xSize, ySize);
	}
}