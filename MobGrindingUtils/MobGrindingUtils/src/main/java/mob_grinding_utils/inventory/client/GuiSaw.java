package mob_grinding_utils.inventory.client;

import mob_grinding_utils.inventory.server.ContainerSaw;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
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
	public void render(@Nonnull GuiGraphics gg, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(gg);
		super.render(gg, mouseX, mouseY, partialTicks);
		renderTooltip(gg, mouseX, mouseY);
	}

	@Override
	protected void renderLabels(@Nonnull GuiGraphics gg, int x, int y) {
		String title = Component.translatable("block.mob_grinding_utils.saw").getString();
		gg.drawString(font, title, imageWidth / 2.0f - fontRenderer.width(title) / 2.0f, imageHeight - 218, 4210752, false);
	}

	@Override
	protected void renderBg(@Nonnull GuiGraphics gg, float partialTicks, int x, int y) {
		int xOffSet = (width - imageWidth) / 2;
		int yOffSet = (height - imageHeight) / 2;
		gg.blit(GUI_SAW, xOffSet, yOffSet, 0, 0, imageWidth, imageHeight);
	}
}