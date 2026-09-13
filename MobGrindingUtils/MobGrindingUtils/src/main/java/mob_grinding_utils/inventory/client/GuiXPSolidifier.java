package mob_grinding_utils.inventory.client;

import mob_grinding_utils.inventory.server.ContainerXPSolidifier;
import mob_grinding_utils.network.BEGuiClick;
import mob_grinding_utils.BlockEntities.BlockEntityXPSolidifier;
import mob_grinding_utils.util.RL;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GuiXPSolidifier extends MGUScreen<ContainerXPSolidifier> {
    protected final ContainerXPSolidifier container;
    private final BlockEntityXPSolidifier tile;

    private TankGauge tankGauge;
    private GuiMGUButton onOffButton;

    public GuiXPSolidifier(ContainerXPSolidifier screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn, RL.mgu("textures/gui/solidifier_gui.png"), 176, 186);
        container = screenContainer;
        tile = container.tile;
    }

    @Override
    protected void init() {
        super.init();

        tankGauge = new TankGauge(leftPos + 8, topPos + 18, 12, 70, tile.tank);
        addRenderableWidget(tankGauge);

        addRenderableWidget(new GuiMGUButton(leftPos + 62, topPos + 72, GuiMGUButton.Size.SOLIDIFIER, 0, Component.literal("Push") ,
            (button) -> ClientPacketDistributor.sendToServer(new BEGuiClick(tile.getBlockPos(), 0))));

        onOffButton = addRenderableWidget(new GuiMGUButton(leftPos + 148, topPos + 8, GuiMGUButton.Size.SOLIDIFIER_ON, 0, onOffLabel(),
            (button) -> ClientPacketDistributor.sendToServer(new BEGuiClick(tile.getBlockPos(), 1))));
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        if (onOffButton != null)
            onOffButton.setMessage(onOffLabel());
    }

    private Component onOffLabel() {
        return Component.literal(tile.isOn ? "On" : "Off");
    }

    @Override
    protected void extractLabels(@Nonnull GuiGraphicsExtractor gg, int x, int y) {
        gg.text(font, Component.translatable("block.mob_grinding_utils.xpsolidifier"), 7, 6, ARGB.opaque(0x404040), false);
        gg.text(font, Component.translatable("container.inventory"), 8, this.imageHeight - 96 + 2, ARGB.opaque(4210752), false);
    }

    @Override
    public void extractBackground(@Nonnull GuiGraphicsExtractor gg, int mouseX, int mouseY, float partialTicks) {
        gg.blit(RenderPipelines.GUI_TEXTURED, TEX, leftPos, topPos, 0.0F, 0.0F, imageWidth, imageHeight, 256, 256);

        String dir = tile.outputDirection.getSerializedName();
        gg.text(font, dir, (int) (leftPos + 124 - font.width(dir) / 2.0f), topPos + 76, ARGB.opaque(5285857), false);

        gg.blit(RenderPipelines.GUI_TEXTURED, TEX, leftPos + 7, topPos + 17, 178.0F, 0.0F, 6, 71, 256, 256);
        gg.blit(RenderPipelines.GUI_TEXTURED, TEX, leftPos + 91, topPos + 36, 178.0F, 73.0F, tile.getProgressScaled(24), 17, 256, 256);
    }

    @Override
    protected void extractTooltip(@Nonnull GuiGraphicsExtractor gg, int x, int y) {
        super.extractTooltip(gg, x, y);
        if (tankGauge.isHovered()) {
            List<Component> tooltip = new ArrayList<>();
            var fluid = FluidUtil.getStack(tile.tank, 0);
            tooltip.add(fluid.getHoverName());
            tooltip.add(Component.literal(tile.tank.getAmountAsInt(0) + "/" + tile.tank.getCapacityAsInt(0, tile.tank.getResource(0))));
            gg.setTooltipForNextFrame(font, tooltip, Optional.empty(), x, y);
        }
    }
}
