package mob_grinding_utils.inventory.client;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class MGUScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
    protected final Identifier TEX;
    public MGUScreen(T container, Inventory inventory, Component title, Identifier texture) {
        super(container, inventory, title);
        TEX = texture;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor gg, int mouseX, int mouseY, float partialTicks) {
        gg.blit(RenderPipelines.GUI_TEXTURED, TEX, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor gg, int mouseX, int mouseY) {
        gg.text(font, getTitle(), imageWidth / 2 - font.width(getTitle()) / 2, 6, 4210752, false);
    }

    protected void drawCenteredString(GuiGraphicsExtractor gg, Component text, int x, int y, int color) {
        gg.text(font, text, x - font.width(text) / 2, y, color, false);
    }
}
