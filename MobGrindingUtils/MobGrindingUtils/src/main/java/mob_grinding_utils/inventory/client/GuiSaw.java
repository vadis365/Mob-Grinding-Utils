package mob_grinding_utils.inventory.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mob_grinding_utils.inventory.server.ContainerSaw;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.Nonnull;

public class GuiSaw extends AbstractContainerScreen<ContainerSaw> {

	private static final ResourceLocation GUI_SAW = new ResourceLocation("mob_grinding_utils:textures/gui/saw_gui.png");
	private final Font fontRenderer = Minecraft.getInstance().font;

	public GuiSaw(ContainerSaw containerSaw, Inventory playerInventory, Component name) {
		super(containerSaw, playerInventory, name);
		imageHeight = 224;
	}

	@Override
	public void render(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		renderTooltip(matrixStack, mouseX, mouseY);
	}

	@Override
	protected void renderLabels(@Nonnull PoseStack matrixStack, int x, int y) {
		String title = Component.translatable("block.mob_grinding_utils.saw").getString();
		fontRenderer.draw(matrixStack, title, imageWidth / 2.0f - fontRenderer.width(title) / 2.0f, imageHeight - 218, 4210752);
	}

	@Override
	protected void renderBg(@Nonnull PoseStack matrixStack, float partialTicks, int x, int y) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, GUI_SAW);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		int xOffSet = (width - imageWidth) / 2;
		int yOffSet = (height - imageHeight) / 2;
		this.blit(matrixStack, xOffSet, yOffSet, 0, 0, imageWidth, imageHeight);
	}
}