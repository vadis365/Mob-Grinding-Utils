package mob_grinding_utils.inventory.client;

import mob_grinding_utils.inventory.server.ContainerXPSolidifier;
import mob_grinding_utils.network.BEGuiClick;
import mob_grinding_utils.tile.TileEntityXPSolidifier;
import mob_grinding_utils.util.RL;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class GuiXPSolidifier extends MGUScreen<ContainerXPSolidifier> {
    protected final ContainerXPSolidifier container;
    private final TileEntityXPSolidifier tile;

    private TankGauge tankGauge;

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

        addRenderableWidget(new GuiMGUButton(leftPos + 148, topPos + 8, GuiMGUButton.Size.SOLIDIFIER_ON, 0, Component.literal("") ,
            (button) -> ClientPacketDistributor.sendToServer(new BEGuiClick(tile.getBlockPos(), 1))));
    }

    @Override
    protected void extractLabels(@Nonnull GuiGraphicsExtractor graphics, int x, int y) {
        graphics.text(font, Component.translatable("block.mob_grinding_utils.xpsolidifier"), 7, 6, 0xFF404040, false);
        graphics.text(font, Component.translatable("container.inventory"), 8, this.imageHeight - 94, 0xFF404040, false);
        String power = tile.isOn ? "On" : "Off";
        graphics.text(font, power, 158 - font.width(power) / 2, 12, 0xFFE0E0E0, true);
    }

    @Override
    public void extractBackground(@Nonnull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        super.extractBackground(graphics, mouseX, mouseY, partialTicks);

        String direction = tile.outputDirection.getSerializedName();
        graphics.text(font, direction, leftPos + 124 - font.width(direction) / 2, topPos + 76, 0xFF50A0A1, false);

        graphics.blit(RenderPipelines.GUI_TEXTURED, TEX, leftPos + 7, topPos + 17, 178, 0, 6, 71, 256, 256);

        graphics.blit(RenderPipelines.GUI_TEXTURED, TEX, leftPos + 91, topPos + 36, 178, 73, tile.getProgressScaled(24), 17, 256, 256);
    }

    @Override
    protected void extractTooltip(@Nonnull GuiGraphicsExtractor graphics, int x, int y) {
        super.extractTooltip(graphics, x, y);
        if (tankGauge.isHovered()) {
            List<Component> tooltip = new ArrayList<>();
            tooltip.add(tile.tank.stack().getHoverName());
            tooltip.add(Component.literal(tile.tank.amount() + "/" + tile.tank.capacity()));
            graphics.setTooltipForNextFrame(font, tooltip, java.util.Optional.empty(), x, y);
        }
    }
}
