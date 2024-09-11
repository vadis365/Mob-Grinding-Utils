package mob_grinding_utils.inventory.client;

import mob_grinding_utils.inventory.server.ContainerAbsorptionHopper;
import mob_grinding_utils.network.BEGuiClick;
import mob_grinding_utils.tile.TileEntityAbsorptionHopper;
import mob_grinding_utils.tile.TileEntityAbsorptionHopper.EnumStatus;
import mob_grinding_utils.util.RL;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

public class GuiAbsorptionHopper extends MGUScreen<ContainerAbsorptionHopper> {

	protected final ContainerAbsorptionHopper container;
	private final TileEntityAbsorptionHopper tile;
	private TankGauge tankGauge;
	public GuiAbsorptionHopper(ContainerAbsorptionHopper container, Inventory playerInventory, Component title) {
		super(container, playerInventory, title, RL.mgu("textures/gui/absorption_hopper_gui.png"));
		this.container = container;
		this.tile = this.container.hopper;
		imageHeight = 226;
		imageWidth = 248;
	}

	@Override
	public void init() {
		super.init();
		clearWidgets();

		tankGauge = new TankGauge(leftPos + 156, topPos + 8, 12, 120, tile.tank);
		addRenderableWidget(tankGauge);

		Button.OnPress message = button -> {
			if (button instanceof GuiMGUButton)
				PacketDistributor.sendToServer(new BEGuiClick(tile.getBlockPos(), ((GuiMGUButton)button).id));
		};

		addRenderableWidget(new GuiMGUButton(leftPos + 7, topPos + 17, GuiMGUButton.Size.MEDIUM, 0, Component.literal("Down"), message));
		addRenderableWidget(new GuiMGUButton(leftPos + 7, topPos + 34, GuiMGUButton.Size.MEDIUM, 1, Component.literal("Up"), message));
		addRenderableWidget(new GuiMGUButton(leftPos + 7, topPos + 51, GuiMGUButton.Size.MEDIUM, 2, Component.literal("North"), message));
		addRenderableWidget(new GuiMGUButton(leftPos + 82, topPos + 17, GuiMGUButton.Size.MEDIUM, 3, Component.literal("South"), message));
		addRenderableWidget(new GuiMGUButton(leftPos + 82, topPos + 34, GuiMGUButton.Size.MEDIUM, 4, Component.literal("West"), message));
		addRenderableWidget(new GuiMGUButton(leftPos + 82, topPos + 51, GuiMGUButton.Size.MEDIUM, 5, Component.literal("East"), message));

		addRenderableWidget(new GuiMGUButton(leftPos + 173, topPos + 113, GuiMGUButton.Size.LARGE, 6, Component.empty(), (button) -> {
			PacketDistributor.sendToServer(new BEGuiClick(tile.getBlockPos(), 6));
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
	protected void renderLabels(GuiGraphics gg, int mouseX, int mouseY) {
		gg.drawString(font, getTitle(), 8, 6, 4210752, false);

		gg.drawString(font, Component.translatable("block.mob_grinding_utils.absorption_hopper_d_u").getString(), 174, 14, 4210752, false);
		gg.drawString(font, Component.translatable("block.mob_grinding_utils.absorption_hopper_n_s").getString(), 174, 48, 4210752, false);
		gg.drawString(font, Component.translatable("block.mob_grinding_utils.absorption_hopper_w_e").getString(), 174, 82, 4210752, false);

		gg.drawString(font, !tile.showRenderBox ? "Show Area" : "Hide Area", 207 - font.width(!tile.showRenderBox ? "Show Area" : "Hide Area") / 2.0f, 117, 14737632, true);

		EnumStatus DOWN = tile.getSideStatus(Direction.DOWN);
		EnumStatus UP = tile.getSideStatus(Direction.UP);
		EnumStatus NORTH = tile.getSideStatus(Direction.NORTH);
		EnumStatus SOUTH = tile.getSideStatus(Direction.SOUTH);
		EnumStatus WEST = tile.getSideStatus(Direction.WEST);
		EnumStatus EAST = tile.getSideStatus(Direction.EAST);

		gg.drawCenteredString(font, DOWN.getSerializedName(), 58, 21, getModeColour(DOWN.ordinal()));
		gg.drawCenteredString(font, UP.getSerializedName(), 58, 38, getModeColour(UP.ordinal()));
		gg.drawCenteredString(font, NORTH.getSerializedName(), 58, 55, getModeColour(NORTH.ordinal()));
		gg.drawCenteredString(font, SOUTH.getSerializedName(), 133, 21, getModeColour(SOUTH.ordinal()));
		gg.drawCenteredString(font, WEST.getSerializedName(), 133, 38, getModeColour(WEST.ordinal()));
		gg.drawCenteredString(font, EAST.getSerializedName(), 133, 55, getModeColour(EAST.ordinal()));
		gg.drawCenteredString(font, String.valueOf(tile.getoffsetY()), 207, 29, 5285857);//NS
		gg.drawCenteredString(font, String.valueOf(tile.getoffsetZ()), 207, 63, 5285857);//WE
		gg.drawCenteredString(font, String.valueOf(tile.getoffsetX()), 207, 97, 5285857);//DU
	}

	@Override
	protected void renderBg(GuiGraphics gg, float partialTicks, int mouseX, int mouseY) {
		gg.blit(TEX, leftPos, topPos, 0, 0, imageWidth, imageHeight);

		gg.blit(TEX, leftPos + 153, topPos + 8 , 248, 0, 6, 120);
	}

	public int getModeColour(int index) {
		return switch (index) {
			case 0 -> 16711680;
			case 1 -> 5285857;
			case 2 -> 16776960;
			default -> 16776960;
		};
	}
}