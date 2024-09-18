package mob_grinding_utils.inventory.client;

import mob_grinding_utils.inventory.server.ContainerXPSolidifier;
import mob_grinding_utils.network.BEGuiClick;
import mob_grinding_utils.tile.TileEntityXPSolidifier;
import mob_grinding_utils.util.RL;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class GuiXPSolidifier extends MGUScreen<ContainerXPSolidifier> {
    protected final ContainerXPSolidifier container;
    private final TileEntityXPSolidifier tile;

    private TankGauge tankGauge;

    public GuiXPSolidifier(ContainerXPSolidifier screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn, RL.mgu("textures/gui/solidifier_gui.png"));
        container = screenContainer;
        tile = container.tile;

        imageHeight = 186;
        imageWidth = 176;
    }

    @Override
    protected void init() {
        super.init();

        tankGauge = new TankGauge(leftPos + 8, topPos + 18, 12, 70, tile.tank);
        addRenderableWidget(tankGauge);

        addRenderableWidget(new GuiMGUButton(leftPos + 62, topPos + 72, GuiMGUButton.Size.SOLIDIFIER, 0, Component.literal("Push") ,
            (button) -> PacketDistributor.sendToServer(new BEGuiClick(tile.getBlockPos(), 0))));

        addRenderableWidget(new GuiMGUButton(leftPos + 148, topPos + 8, GuiMGUButton.Size.SOLIDIFIER_ON, 0, Component.literal("") ,
            (button) -> PacketDistributor.sendToServer(new BEGuiClick(tile.getBlockPos(), 1))));
    }

    @Override
    protected void renderLabels(@Nonnull GuiGraphics gg, int x, int y) {
        gg.drawString(font, Component.translatable("block.mob_grinding_utils.xpsolidifier"), 7, 6, 0x404040, false);
        gg.drawString(font, Component.translatable("container.inventory"), 8, this.imageHeight - 96 + 2, 4210752, false);
        gg.drawString(font, tile.isOn ? "On" : "Off", 158 - font.width(tile.isOn ? "On" : "Off") / 2.0f, 12, 14737632, true);
    }

    @Override
    protected void renderBg(@Nonnull GuiGraphics gg, float partialTicks, int mouseX, int mouseY) {
        int zLevel = 0;
        gg.blit(TEX, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        gg.drawString(font, tile.outputDirection.getSerializedName(), leftPos + 124 - font.width(tile.outputDirection.getSerializedName()) / 2.0f, topPos + 76, 5285857, false);

        gg.blit(TEX, leftPos + 7, topPos + 17 , 178, 0, 6, 71);

        gg.blit(TEX, leftPos + 91, topPos + 36, 178, 73, tile.getProgressScaled(24), 17);
    }

    @Override
    protected void renderTooltip(@Nonnull GuiGraphics gg, int x, int y) {
        super.renderTooltip(gg, x, y);
        if (tankGauge.isHovered()) {
            List<Component> tooltip = new ArrayList<>();
            tooltip.add(tile.tank.getFluid().getHoverName());
            tooltip.add(Component.literal(tile.tank.getFluidAmount() + "/" + tile.tank.getCapacity()));
            gg.renderComponentTooltip(font, tooltip, x, y);
        }
    }
}
