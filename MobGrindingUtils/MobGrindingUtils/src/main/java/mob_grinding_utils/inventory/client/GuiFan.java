package mob_grinding_utils.inventory.client;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.inventory.server.ContainerFan;
import mob_grinding_utils.network.MessageFan;
import mob_grinding_utils.tile.TileEntityFan;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@OnlyIn(Dist.CLIENT)
public class GuiFan extends GuiContainer {

	private static final ResourceLocation GUI_FAN = new ResourceLocation("mob_grinding_utils:textures/gui/fan_gui.png");
	private final TileEntityFan tile;

	public GuiFan(EntityPlayer player, TileEntityFan tile) {
		super(new ContainerFan(player, tile));
		this.tile = tile;
		ySize = 224;
	}

	@Override
	public void initGui() {
		super.initGui();
		buttons.clear();
		int xOffSet = (width - xSize) / 2;
		int yOffSet = (height - ySize) / 2;
		buttons.add(new GuiBigButton(0, xOffSet + 54, yOffSet + 42, 33, 228, ""));
	}

	@Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        super.render(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		fontRenderer.drawString(I18n.format(new TextComponentTranslation("tile.mob_grinding_utils.fan.name").getFormattedText()), xSize / 2 - fontRenderer.getStringWidth(I18n.format(new TextComponentTranslation("tile.mob_grinding_utils.fan.name").getFormattedText())) / 2, ySize - 218, 4210752);	
		fontRenderer.drawStringWithShadow(I18n.format(!tile.showRenderBox ? "Show Area" : "Hide Area"), xSize - 88 - fontRenderer.getStringWidth(I18n.format(!tile.showRenderBox ? "Show Area" : "Hide Area")) / 2, ySize - 178, 14737632);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTickTime, int x, int y) {
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(GUI_FAN);
		int xOffSet = (width - xSize) / 2;
		int yOffSet = (height - ySize) / 2;
		drawTexturedModalRect(xOffSet, yOffSet, 0, 0, xSize, ySize);
	}
	
	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (guibutton instanceof GuiButton)
			MobGrindingUtils.NETWORK_WRAPPER.sendToServer(new MessageFan(mc.player, guibutton.id, tile.getPos()));
	}
}