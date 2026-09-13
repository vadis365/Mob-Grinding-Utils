package mob_grinding_utils.inventory.client;

import mob_grinding_utils.util.RL;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;

import javax.annotation.Nonnull;

public class GuiMGUButton extends Button {
    private static final Identifier TEXTURES = RL.mgu("textures/gui/absorption_hopper_gui.png");
    private static final Identifier SOLIDIFIER_TEXTURES = RL.mgu("textures/gui/solidifier_gui.png");
    public Size size;
    public int id;

    public GuiMGUButton(int x, int y, Size s, int idIn, Component title, OnPress pressedAction) {
        super(x, y, s.width, s.height, title, pressedAction, DEFAULT_NARRATION);
        size = s;
        id = idIn;
    }

    @Override
    protected void extractContents(@Nonnull GuiGraphicsExtractor gg, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            boolean hover = mouseX >= getX() && mouseY >= getY() && mouseX < getX() + width && mouseY < getY() + height;
            int tint = hover ? ARGB.colorFromFloat(1f, 0.75f, 1f, 0.75f) : ARGB.colorFromFloat(0.5f, 0.75f, 0.75f, 0.75f);
            gg.blit(RenderPipelines.GUI_TEXTURED, getTextures(size), getX(), getY(), size.u, size.v, width, height, 256, 256, tint);

            // 26.1 GuiGraphicsExtractor skips text when ARGB.alpha(color) == 0
            int textColour = ARGB.opaque(14737632);
            if (this.packedFGColor != UNSET_FG_COLOR)
                textColour = ARGB.opaque(this.packedFGColor);
            else if (!this.active)
                textColour = ARGB.opaque(10526880);
            else if (this.isHoveredOrFocused())
                textColour = ARGB.opaque(16777120);
            Font fontrenderer = Minecraft.getInstance().font;
            gg.centeredText(fontrenderer, getMessage(), getX() + this.width / 2, getY() + (this.height - 8) / 2, textColour);
        }
    }

    public Identifier getTextures(Size size) {
        return switch (size) {
            case SMALL, MEDIUM, LARGE -> TEXTURES;
            case SOLIDIFIER, SOLIDIFIER_ON -> SOLIDIFIER_TEXTURES;
        };
    }

    enum Size {
        SMALL(16 , 16, 103, 228),
        MEDIUM(32, 16, 0, 228),
        LARGE(68, 16, 33, 228),
        SOLIDIFIER(34, 16, 178, 92),
        SOLIDIFIER_ON(20, 16, 178, 110);

        final int width;
        final int height;
        final int u;
        final int v;
        Size(int w, int h, int U, int V) {width = w; height = h; u = U; v = V;}
    }
}
