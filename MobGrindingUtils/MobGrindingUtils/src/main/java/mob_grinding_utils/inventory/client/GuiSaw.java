package mob_grinding_utils.inventory.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

import mob_grinding_utils.inventory.server.ContainerSaw;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class GuiSaw extends AbstractContainerScreen<ContainerSaw> {

	private static final ResourceLocation GUI_SAW = new ResourceLocation("mob_grinding_utils:textures/gui/saw_gui.png");

	public GuiSaw(ContainerSaw containerSaw, Inventory playerInventory, Component name) {
		super(containerSaw, playerInventory, name);
		imageHeight = 224;
	}

	@Override
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        renderTooltip(matrixStack, mouseX, mouseY);
    }

	@Override
	protected void renderLabels(PoseStack matrixStack, int x, int y) {
		String title = new TranslatableComponent("block.mob_grinding_utils.saw").getString();
		Minecraft.getInstance().font.draw(matrixStack, title, imageWidth / 2 - Minecraft.getInstance().font.width(title) / 2, imageHeight - 218, 4210752);
	}

	@Override
	protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		getMinecraft().getTextureManager().bind(GUI_SAW);
		int xOffSet = (width - imageWidth) / 2;
		int yOffSet = (height - imageHeight) / 2;
		this.blit(matrixStack, xOffSet, yOffSet, 0, 0, imageWidth, imageHeight);
	}
}