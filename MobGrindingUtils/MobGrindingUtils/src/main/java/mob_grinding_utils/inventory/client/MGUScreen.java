package mob_grinding_utils.inventory.client;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

import javax.annotation.Nonnull;

public class MGUScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
    protected final Identifier TEX;
    public MGUScreen(T container, Inventory inventory, Component title, Identifier texture) {
        this(container, inventory, title, texture, 176, 166);
    }

    public MGUScreen(T container, Inventory inventory, Component title, Identifier texture, int imageWidth, int imageHeight) {
        super(container, inventory, title, imageWidth, imageHeight);
        TEX = texture;
    }

    @Override
    public void extractBackground(@Nonnull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        super.extractBackground(graphics, mouseX, mouseY, partialTicks);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEX, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        String title = getTitle().getString();
        graphics.text(font, title, (imageWidth - font.width(title)) / 2, 6, 4210752, false);
    }

    protected void drawCenteredString(GuiGraphicsExtractor graphics, Component text, int x, int y, int color) {
        graphics.text(font, text, x - font.width(text) / 2, y, color, false);
    }
}
