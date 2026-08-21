package mob_grinding_utils.inventory.client;

import mob_grinding_utils.inventory.server.ContainerAbsorptionHopper;
import mob_grinding_utils.network.BEGuiClick;
import mob_grinding_utils.tile.TileEntityAbsorptionHopper;
import mob_grinding_utils.tile.TileEntityAbsorptionHopper.EnumStatus;
import mob_grinding_utils.util.RL;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class GuiAbsorptionHopper extends MGUScreen<ContainerAbsorptionHopper> {

	protected final ContainerAbsorptionHopper container;
	private final TileEntityAbsorptionHopper tile;
	private TankGauge tankGauge;
	public GuiAbsorptionHopper(ContainerAbsorptionHopper container, Inventory playerInventory, Component title) {
		super(container, playerInventory, title, RL.mgu("textures/gui/absorption_hopper_gui.png"), 248, 226);
		this.container = container;
		this.tile = this.container.hopper;
	}

	@Override
	public void init() {
		super.init();
		clearWidgets();

		tankGauge = new TankGauge(leftPos + 156, topPos + 8, 12, 120, tile.tank);
		addRenderableWidget(tankGauge);

		Button.OnPress message = button -> {
			if (button instanceof GuiMGUButton)
				ClientPacketDistributor.sendToServer(new BEGuiClick(tile.getBlockPos(), ((GuiMGUButton)button).id));
		};

		addRenderableWidget(new GuiMGUButton(leftPos + 7, topPos + 17, GuiMGUButton.Size.MEDIUM, 0, Component.literal("Down"), message));
		addRenderableWidget(new GuiMGUButton(leftPos + 7, topPos + 34, GuiMGUButton.Size.MEDIUM, 1, Component.literal("Up"), message));
		addRenderableWidget(new GuiMGUButton(leftPos + 7, topPos + 51, GuiMGUButton.Size.MEDIUM, 2, Component.literal("North"), message));
		addRenderableWidget(new GuiMGUButton(leftPos + 82, topPos + 17, GuiMGUButton.Size.MEDIUM, 3, Component.literal("South"), message));
		addRenderableWidget(new GuiMGUButton(leftPos + 82, topPos + 34, GuiMGUButton.Size.MEDIUM, 4, Component.literal("West"), message));
		addRenderableWidget(new GuiMGUButton(leftPos + 82, topPos + 51, GuiMGUButton.Size.MEDIUM, 5, Component.literal("East"), message));

		addRenderableWidget(new GuiMGUButton(leftPos + 173, topPos + 113, GuiMGUButton.Size.LARGE, 6, Component.empty(), (button) -> {
			ClientPacketDistributor.sendToServer(new BEGuiClick(tile.getBlockPos(), 6));
			tile.showRenderBox = !tile.showRenderBox;
		}));

		addRenderableWidget(new GuiMGUButton(leftPos + 173, topPos + 25, GuiMGUButton.Size.SMALL, 7, Component.literal("-"), message));
		addRenderableWidget(new GuiMGUButton(leftPos + 225, topPos + 25, GuiMGUButton.Size.SMALL, 8, Component.literal("+"), message));
		addRenderableWidget(new GuiMGUButton(leftPos + 173, topPos + 59, GuiMGUButton.Size.SMALL, 9, Component.literal("-"), message));
		addRenderableWidget(new GuiMGUButton(leftPos + 225, topPos + 59, GuiMGUButton.Size.SMALL, 10, Component.literal("+"), message));
		addRenderableWidget(new GuiMGUButton(leftPos + 173, topPos + 93, GuiMGUButton.Size.SMALL, 11, Component.literal("-"), message));
		addRenderableWidget(new GuiMGUButton(leftPos + 225, topPos + 93, GuiMGUButton.Size.SMALL, 12, Component.literal("+"), message));
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

	@Override
	protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
		graphics.text(font, getTitle(), 8, 6, 0xFF404040, false);

		graphics.text(font, Component.translatable("block.mob_grinding_utils.absorption_hopper_d_u"), 174, 14, 0xFF404040, false);
		graphics.text(font, Component.translatable("block.mob_grinding_utils.absorption_hopper_n_s"), 174, 48, 0xFF404040, false);
		graphics.text(font, Component.translatable("block.mob_grinding_utils.absorption_hopper_w_e"), 174, 82, 0xFF404040, false);

		String area = !tile.showRenderBox ? "Show Area" : "Hide Area";
		graphics.text(font, area, 207 - font.width(area) / 2, 117, 0xFFE0E0E0, true);

		EnumStatus DOWN = tile.getSideStatus(Direction.DOWN);
		EnumStatus UP = tile.getSideStatus(Direction.UP);
		EnumStatus NORTH = tile.getSideStatus(Direction.NORTH);
		EnumStatus SOUTH = tile.getSideStatus(Direction.SOUTH);
		EnumStatus WEST = tile.getSideStatus(Direction.WEST);
		EnumStatus EAST = tile.getSideStatus(Direction.EAST);

		center(graphics, DOWN.getSerializedName(), 58, 21, getModeColour(DOWN.ordinal()));
		center(graphics, UP.getSerializedName(), 58, 38, getModeColour(UP.ordinal()));
		center(graphics, NORTH.getSerializedName(), 58, 55, getModeColour(NORTH.ordinal()));
		center(graphics, SOUTH.getSerializedName(), 133, 21, getModeColour(SOUTH.ordinal()));
		center(graphics, WEST.getSerializedName(), 133, 38, getModeColour(WEST.ordinal()));
		center(graphics, EAST.getSerializedName(), 133, 55, getModeColour(EAST.ordinal()));
		center(graphics, String.valueOf(tile.getoffsetY()), 207, 29, 0xFF50A0A1);//NS
		center(graphics, String.valueOf(tile.getoffsetZ()), 207, 63, 0xFF50A0A1);//WE
		center(graphics, String.valueOf(tile.getoffsetX()), 207, 97, 0xFF50A0A1);//DU
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
		super.extractBackground(graphics, mouseX, mouseY, partialTicks);
		graphics.blit(RenderPipelines.GUI_TEXTURED, TEX, leftPos + 153, topPos + 8, 248, 0, 6, 120, 256, 256);
	}

	private void center(GuiGraphicsExtractor graphics, String text, int x, int y, int color) {
		graphics.text(font, text, x - font.width(text) / 2, y, color, false);
	}

	public int getModeColour(int index) {
		return switch (index) {
			case 0 -> 0xFFFF0000;
			case 1 -> 0xFF50A0A1;
			case 2 -> 0xFFFFFF00;
			default -> 0xFFFFFF00;
		};
	}
}
