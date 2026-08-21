package mob_grinding_utils.inventory.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import mob_grinding_utils.util.FluidTankStorage;

import javax.annotation.Nonnull;

public class TankGauge extends AbstractWidget {
    private final FluidTankStorage tank;
    private Fluid oldFluid;
    private TextureAtlasSprite sprite;
    public TankGauge(int pX, int pY, int pWidth, int pHeight, FluidTankStorage tankIn) {
        super(pX, pY, pWidth, pHeight, Component.empty());
        tank = tankIn;
    }

    @Override
    protected void extractWidgetRenderState(@Nonnull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        float fluidLevel = getFluidLevel();

        if (tank == null)
            return;

        FluidStack stack = tank.stack();

        if (fluidLevel > 0) {
			int color = -1;

            if (this.sprite == null || this.oldFluid != stack.getFluid()) {
                this.oldFluid = stack.getFluid();

				this.sprite = Minecraft.getInstance().getModelManager().getFluidStateModelSet().get(stack.getFluid().defaultFluidState()).stillMaterial().sprite();
            }

            if (this.sprite != null) {
                double tankLevel = fluidLevel * height;
                int count = 1 + ((int) Math.ceil(tankLevel)) / 16;
                for (int i = 0; i < count; i++) {
                    int subHeight = Math.min(16, (int) Math.ceil(tankLevel - 16.0 * i));
                    if (subHeight <= 0) {
                        continue;
                    }
                    int y = getY() + height - 16 * i - subHeight;
                    graphics.enableScissor(getX(), y, getX() + width, y + subHeight);
                    graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, getX(), y + subHeight - 16, width, 16, color);
                    graphics.disableScissor();
                }
            }
        }
    }

    public float getFluidLevel() {
        return tank != null ? (float) tank.amount() / tank.capacity() : 0.0f;
    }

    @Override
    protected void updateWidgetNarration(@Nonnull NarrationElementOutput narrationElementOutput) {

    }
}
