package mob_grinding_utils.inventory.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

import net.minecraft.client.gui.components.Button.OnPress;

public class GuiMGUButton extends Button {
    private static final ResourceLocation TEXTURES = new ResourceLocation("mob_grinding_utils:textures/gui/absorption_hopper_gui.png");
    private static final ResourceLocation SOLIDIFIER_TEXTURES = new ResourceLocation("mob_grinding_utils:textures/gui/solidifier_gui.png");
    public Size size;
    public int id;

    public GuiMGUButton(int x, int y, Size s, int idIn, Component title, OnPress pressedAction) {
        super(x, y, s.width, s.height, title, pressedAction);
        size = s;
        id = idIn;
    }

    @Override
    public void renderButton(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        Font fontrenderer = mc.font;
        if (visible) {
            mc.getTextureManager().bind(getTextures(size));
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
    
	public ResourceLocation getTextures(Size size) {
		switch (size) {
		case SMALL:
		case MEDIUM:
		case LARGE:
			return TEXTURES;
		case SOLIDIFIER:
		case SOLIDIFIER_ON:
			return SOLIDIFIER_TEXTURES;
		}
		return TEXTURES;
	}

    enum Size {
        SMALL(16 , 16, 103, 228),
        MEDIUM(32, 16, 0, 228),
        LARGE(68, 16, 33, 228),
        SOLIDIFIER(34, 16, 178, 92),
        SOLIDIFIER_ON(20, 16, 178, 110);

        int width, height, u, v;
        Size(int w, int h, int U, int V) {width = w; height = h; u = U; v = V;}
    }
}
