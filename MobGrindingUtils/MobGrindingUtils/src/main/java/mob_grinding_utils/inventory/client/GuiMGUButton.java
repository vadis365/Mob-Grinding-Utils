package mob_grinding_utils.inventory.client;

import mob_grinding_utils.util.RL;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

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
    protected void extractContents(@Nonnull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        int tint = this.isHoveredOrFocused() ? 0xFFBFFFBF : 0x80BFBFBF;
        graphics.blit(RenderPipelines.GUI_TEXTURED, getTextures(size), getX(), getY(), size.u, size.v, width, height, 256, 256, tint);

        int textColour = packedFGColor != UNSET_FG_COLOR ? packedFGColor : !this.active ? 10526880 : this.isHoveredOrFocused() ? 16777120 : 14737632;
        graphics.text(Minecraft.getInstance().font, getMessage(), getX() + (width - Minecraft.getInstance().font.width(getMessage())) / 2, getY() + (height - 8) / 2, textColour, true);
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
