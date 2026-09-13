package mob_grinding_utils.inventory.client;

import mob_grinding_utils.BlockEntities.BlockEntityFan;
import mob_grinding_utils.inventory.server.ContainerFan;
import mob_grinding_utils.network.BEGuiClick;
import mob_grinding_utils.util.RL;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
public class GuiFan extends MGUScreen<ContainerFan> {
	protected final ContainerFan container;
	private final BlockEntityFan tile;
	private GuiMGUButton areaButton;

	public GuiFan(ContainerFan container, Inventory inventory, Component title) {
		super(container, inventory, title, RL.mgu("textures/gui/fan_gui.png"), 176, 150);
		this.container = container;
		this.tile = this.container.fan;
	}

	@Override
	public void init() {
		super.init();

		areaButton = addRenderableWidget(new GuiMGUButton(leftPos + 54, topPos + 42, GuiMGUButton.Size.LARGE, 0, areaLabel(), (button) -> {
			ClientPacketDistributor.sendToServer(new BEGuiClick(tile.getBlockPos(), 0));
			tile.showRenderBox = !tile.showRenderBox;
			button.setMessage(areaLabel());
		}));
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
}
