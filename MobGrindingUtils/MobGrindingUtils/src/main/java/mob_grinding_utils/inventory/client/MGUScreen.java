package mob_grinding_utils.inventory.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

import javax.annotation.Nonnull;

public class MGUScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
    protected final ResourceLocation TEX;
    public MGUScreen(T container, Inventory inventory, Component title, ResourceLocation texture) {
        super(container, inventory, title);
        TEX = texture;
    }

    @Override
    public void render(@Nonnull GuiGraphics gg, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(gg);
        super.render(gg, mouseX, mouseY, partialTicks);
        renderTooltip(gg, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics gg, float mX, int mY, int partialTicks) {
        gg.blit(TEX, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }

    @Override
    protected void renderLabels(GuiGraphics gg, int mouseX, int mouseY) {
        String title = getTitle().getString();
        gg.drawString(font, title, imageWidth / 2.0f - font.width(title) / 2.0f, 6, 4210752, false);
    }
}
