package mob_grinding_utils.inventory.client;

import mob_grinding_utils.inventory.server.ContainerFan;
import mob_grinding_utils.network.BEGuiClick;
import mob_grinding_utils.tile.TileEntityFan;
import mob_grinding_utils.util.RL;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class GuiFan extends MGUScreen<ContainerFan> {
	protected final ContainerFan container;
	private final TileEntityFan tile;

	public GuiFan(ContainerFan container, Inventory inventory, Component title) {
		super(container, inventory, title, RL.mgu("textures/gui/fan_gui.png"), 176, 150);
		this.container = container;
		this.tile = this.container.fan;
	}

	@Override
	public void init() {
		super.init();

		addRenderableWidget(new GuiMGUButton(leftPos + 54, topPos + 42, GuiMGUButton.Size.LARGE, 0, Component.empty(), (button) -> {
			ClientPacketDistributor.sendToServer(new BEGuiClick(tile.getBlockPos(), 0));
			tile.showRenderBox = !tile.showRenderBox;
		}));
	}

	@Override
	protected void extractLabels(@Nonnull GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
		super.extractLabels(graphics, mouseX, mouseY);

		String area = !tile.showRenderBox ? "Show Area" : "Hide Area";
		graphics.text(font, area, imageWidth - 88 - font.width(area) / 2, 46, 14737632, true);
	}
}
