package mob_grinding_utils.inventory.client;

import mob_grinding_utils.util.RL;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.data.AtlasIds;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.fluid.FluidTintSource;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;

import javax.annotation.Nonnull;

public class TankGauge extends AbstractWidget {
    private final FluidStacksResourceHandler tank;
    private Fluid oldFluid;
    private TextureAtlasSprite sprite;
    private int color = 0xFFFFFFFF;

    public TankGauge(int pX, int pY, int pWidth, int pHeight, FluidStacksResourceHandler tankIn) {
        super(pX, pY, pWidth, pHeight, Component.empty());
        tank = tankIn;
    }

    @Override
    protected void extractWidgetRenderState(@Nonnull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        float fluidLevel = getFluidLevel();

        if (tank == null)
            return;

        FluidStack stack = FluidUtil.getStack(tank, 0);

        if (fluidLevel > 0 && !stack.isEmpty()) {
            if (this.sprite == null || this.oldFluid != stack.getFluid()) {
                this.oldFluid = stack.getFluid();
                resolveSprite(stack);
            }

            if (this.sprite != null) {
                double tankLevel = fluidLevel * height;
                int count = 1 + ((int) Math.ceil(tankLevel)) / 16;
                for (int i = 0; i < count; i++) {
                    float subHeight = (float) Math.min(16.0f, tankLevel - (16.0f * i));
                    double offsetY = height - 16.0 * i - subHeight;
                    graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.sprite, getX(), (int) (getY() + offsetY), width, (int) subHeight, color);
                }
            }
        }
    }

    private void resolveSprite(FluidStack stack) {
        FluidState fluidState = stack.getFluid().defaultFluidState();
        FluidModel fluidModel = Minecraft.getInstance().getModelManager().getFluidStateModelSet().get(fluidState);
        this.sprite = fluidModel.stillMaterial().sprite();
        FluidTintSource tint = fluidModel.fluidTintSource();
        this.color = tint != null ? tint.colorAsStack(stack) : 0xFFFFFFFF;
        if (this.sprite == null) {
            // AtlasManager.getAtlasOrThrow expects AtlasIds.* (e.g. minecraft:blocks), not TextureAtlas.LOCATION_* texture paths
            this.sprite = Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(AtlasIds.BLOCKS).getSprite(RL.mgu("block/fluid_xp"));
            this.color = 0xFFFFFFFF;
        }
    }

    public float getFluidLevel() {
        if (tank == null || tank.getResource(0).isEmpty())
            return 0.0f;
        return (float) tank.getAmountAsInt(0) / tank.getCapacityAsInt(0, tank.getResource(0));
    }

    @Override
    protected void updateWidgetNarration(@Nonnull NarrationElementOutput narrationElementOutput) {
    }
}
