package mob_grinding_utils.inventory.client;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
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
    public void extractBackground(@Nonnull GuiGraphicsExtractor gg, int mouseX, int mouseY, float partialTicks) {
        gg.blit(RenderPipelines.GUI_TEXTURED, TEX, leftPos, topPos, 0.0F, 0.0F, imageWidth, imageHeight, 256, 256);
    }

    @Override
    protected void extractLabels(@Nonnull GuiGraphicsExtractor gg, int mouseX, int mouseY) {
        String title = getTitle().getString();
        gg.text(font, title, (int) (imageWidth / 2.0f - font.width(title) / 2.0f), 6, ARGB.opaque(4210752), false);
    }

    protected void drawCenteredString(GuiGraphicsExtractor gg, Component text, int x, int y, int color) {
        gg.centeredText(font, text, x, y, ARGB.opaque(color));
    }
}
