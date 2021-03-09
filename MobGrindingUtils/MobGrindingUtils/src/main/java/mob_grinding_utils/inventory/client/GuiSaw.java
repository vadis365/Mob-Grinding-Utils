package mob_grinding_utils.inventory.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import mob_grinding_utils.inventory.server.ContainerSaw;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class GuiSaw extends ContainerScreen<ContainerSaw> {

	private static final ResourceLocation GUI_SAW = new ResourceLocation("mob_grinding_utils:textures/gui/saw_gui.png");

	public GuiSaw(ContainerSaw containerSaw, PlayerInventory playerInventory, ITextComponent name) {
		super(containerSaw, playerInventory, name);
		ySize = 224;
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
		String title = new TranslationTextComponent("block.mob_grinding_utils.saw").getString();
		Minecraft.getInstance().fontRenderer.drawString(matrixStack, title, xSize / 2 - Minecraft.getInstance().fontRenderer.getStringWidth(title) / 2, ySize - 218, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		getMinecraft().getTextureManager().bindTexture(GUI_SAW);
		int xOffSet = (width - xSize) / 2;
		int yOffSet = (height - ySize) / 2;
		this.blit(matrixStack, xOffSet, yOffSet, 0, 0, xSize, ySize);
	}
}