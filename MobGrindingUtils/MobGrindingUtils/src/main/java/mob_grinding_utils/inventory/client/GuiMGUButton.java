package mob_grinding_utils.inventory.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class GuiMGUButton extends Button {
    private static final ResourceLocation TEXTURES = new ResourceLocation("mob_grinding_utils:textures/gui/absorption_hopper_gui.png");
    public Size size;
    public int id;

    public GuiMGUButton(int x, int y, Size s, int idIn, ITextComponent title, IPressable pressedAction) {
        super(x, y, s.width, s.height, title, pressedAction);
        size = s;
        id = idIn;
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        FontRenderer fontrenderer = mc.fontRenderer;
        if (visible) {
            mc.getTextureManager().bindTexture(TEXTURES);
            RenderSystem.color4f(0.75F, 0.75F, 0.75F, 0.5F);
            boolean hover = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
            if(hover)
                RenderSystem.color4f(0.75F, 1, 0.75F, 1);
            blit(matrixStack, x, y, size.u, size.v, width, height);

            int textColour = 14737632;
            if (getFGColor() != 0)
                textColour = getFGColor();
            else if (!this.active)
                textColour = 10526880;
            else if (this.isHovered())
                textColour = 16777120;
            drawCenteredString(matrixStack, fontrenderer, getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, textColour);
        }
    }

    enum Size {
        SMALL(16 , 16, 103, 228),
        MEDIUM(32, 16, 0, 228),
        LARGE(68, 16, 33, 228);

        int width, height, u, v;
        Size(int w, int h, int U, int V) {width = w; height = h; u = U; v = V;}
    }
}
