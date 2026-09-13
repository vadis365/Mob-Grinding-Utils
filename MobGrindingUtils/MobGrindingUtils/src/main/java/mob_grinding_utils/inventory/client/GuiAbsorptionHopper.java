package mob_grinding_utils.inventory.client;

import mob_grinding_utils.inventory.server.ContainerAbsorptionHopper;
import mob_grinding_utils.network.BEGuiClick;
import mob_grinding_utils.BlockEntities.BlockEntityAbsorptionHopper;
import mob_grinding_utils.BlockEntities.BlockEntityAbsorptionHopper.EnumStatus;
import mob_grinding_utils.util.RL;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class GuiAbsorptionHopper extends MGUScreen<ContainerAbsorptionHopper> {

	protected final ContainerAbsorptionHopper container;
	private final BlockEntityAbsorptionHopper tile;
	private TankGauge tankGauge;
	private GuiMGUButton areaButton;

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

		areaButton = addRenderableWidget(new GuiMGUButton(leftPos + 173, topPos + 113, GuiMGUButton.Size.LARGE, 6, areaLabel(), (button) -> {
			ClientPacketDistributor.sendToServer(new BEGuiClick(tile.getBlockPos(), 6));
			tile.showRenderBox = !tile.showRenderBox;
			button.setMessage(areaLabel());
		}));

		addRenderableWidget(new GuiMGUButton(leftPos + 173, topPos + 25, GuiMGUButton.Size.SMALL, 7, Component.literal("-"), message));
		addRenderableWidget(new GuiMGUButton(leftPos + 225, topPos + 25, GuiMGUButton.Size.SMALL, 8, Component.literal("+"), message));
		addRenderableWidget(new GuiMGUButton(leftPos + 173, topPos + 59, GuiMGUButton.Size.SMALL, 9, Component.literal("-"), message));
		addRenderableWidget(new GuiMGUButton(leftPos + 225, topPos + 59, GuiMGUButton.Size.SMALL, 10, Component.literal("+"), message));
		addRenderableWidget(new GuiMGUButton(leftPos + 173, topPos + 93, GuiMGUButton.Size.SMALL, 11, Component.literal("-"), message));
		addRenderableWidget(new GuiMGUButton(leftPos + 225, topPos + 93, GuiMGUButton.Size.SMALL, 12, Component.literal("+"), message));
	}

	@Override
	protected void containerTick() {
		super.containerTick();
		if (areaButton != null)
			areaButton.setMessage(areaLabel());
	}

	private Component areaLabel() {
		return Component.literal(!tile.showRenderBox ? "Show Area" : "Hide Area");
	}

	@Override
	protected void extractTooltip(@Nonnull GuiGraphicsExtractor gg, int x, int y) {
		super.extractTooltip(gg, x, y);
		if (tankGauge.isHovered()) {
			List<Component> tooltip = new ArrayList<>();
			var fluid = FluidUtil.getStack(tile.tank, 0);
			tooltip.add(fluid.getHoverName());
			tooltip.add(Component.literal(tile.tank.getAmountAsInt(0) + "/" + tile.tank.getCapacityAsInt(0, tile.tank.getResource(0))));
			gg.setTooltipForNextFrame(font, tooltip, java.util.Optional.empty(), x, y);
		}
	}

	@Override
	protected void extractLabels(GuiGraphicsExtractor gg, int mouseX, int mouseY) {
		gg.text(font, getTitle(), 8, 6, ARGB.opaque(4210752), false);

		gg.text(font, Component.translatable("block.mob_grinding_utils.absorption_hopper_d_u").getString(), 174, 14, ARGB.opaque(4210752), false);
		gg.text(font, Component.translatable("block.mob_grinding_utils.absorption_hopper_n_s").getString(), 174, 48, ARGB.opaque(4210752), false);
		gg.text(font, Component.translatable("block.mob_grinding_utils.absorption_hopper_w_e").getString(), 174, 82, ARGB.opaque(4210752), false);

		EnumStatus down = tile.getSideStatus(Direction.DOWN);
		EnumStatus up = tile.getSideStatus(Direction.UP);
		EnumStatus north = tile.getSideStatus(Direction.NORTH);
		EnumStatus south = tile.getSideStatus(Direction.SOUTH);
		EnumStatus west = tile.getSideStatus(Direction.WEST);
		EnumStatus east = tile.getSideStatus(Direction.EAST);

		gg.centeredText(font, down.getSerializedName(), 58, 21, getModeColour(down.ordinal()));
		gg.centeredText(font, up.getSerializedName(), 58, 38, getModeColour(up.ordinal()));
		gg.centeredText(font, north.getSerializedName(), 58, 55, getModeColour(north.ordinal()));
		gg.centeredText(font, south.getSerializedName(), 133, 21, getModeColour(south.ordinal()));
		gg.centeredText(font, west.getSerializedName(), 133, 38, getModeColour(west.ordinal()));
		gg.centeredText(font, east.getSerializedName(), 133, 55, getModeColour(east.ordinal()));

		gg.centeredText(font, String.valueOf(tile.getoffsetY()), 207, 29, ARGB.opaque(5285857));
		gg.centeredText(font, String.valueOf(tile.getoffsetZ()), 207, 63, ARGB.opaque(5285857));
		gg.centeredText(font, String.valueOf(tile.getoffsetX()), 207, 97, ARGB.opaque(5285857));
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor gg, int mouseX, int mouseY, float partialTicks) {
		gg.blit(RenderPipelines.GUI_TEXTURED, TEX, leftPos, topPos, 0.0F, 0.0F, imageWidth, imageHeight, 256, 256);
		gg.blit(RenderPipelines.GUI_TEXTURED, TEX, leftPos + 153, topPos + 8, 248.0F, 0.0F, 6, 120, 256, 256);
	}

	public int getModeColour(int index) {
		return switch (index) {
			case 0 -> ARGB.opaque(16711680);
			case 1 -> ARGB.opaque(5285857);
			case 2 -> ARGB.opaque(16776960);
			default -> ARGB.opaque(16776960);
		};
	}
}
