package mob_grinding_utils.inventory.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiMediumButton extends GuiButton {
	private static final ResourceLocation TEXTURES = new ResourceLocation("mob_grinding_utils:textures/gui/absorption_hopper_gui.png");
	private int u;
	private int v;

	public GuiMediumButton(int id, int x, int y, int u, int v, String name) {
		super(id, x, y, 32, 16, name);
		this.u = u;
		this.v = v;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		FontRenderer fontrenderer = mc.fontRenderer;
		if (visible) {
			mc.getTextureManager().bindTexture(TEXTURES);
			GlStateManager.color(0.75F, 0.75F, 0.75F, 0.5F);
			boolean hover = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
			if(hover)
				GlStateManager.color(0.75F, 1, 0.75F, 1);	
			drawTexturedModalRect(x, y, u, v, width, height);
			
			int textColour = 14737632;
			if (packedFGColour != 0)
				textColour = packedFGColour;
			else if (!this.enabled)
				textColour = 10526880;
			else if (this.hovered)
				textColour = 16777120;
			drawCenteredString(fontrenderer, this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2, textColour);
		}
	}
}
