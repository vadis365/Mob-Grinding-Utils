package mob_grinding_utils.inventory.client;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.inventory.server.ContainerFan;
import mob_grinding_utils.network.MessageFan;
import mob_grinding_utils.tile.TileEntityFan;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class GuiFan extends MGUScreen<ContainerFan> {
	protected final ContainerFan container;
	private final TileEntityFan tile;

	public GuiFan(ContainerFan container, Inventory inventory, Component title) {
		super(container, inventory, title, new ResourceLocation("mob_grinding_utils:textures/gui/fan_gui.png"));
		this.container = container;
		this.tile = this.container.fan;
		imageHeight = 150;
	}

	@Override
	public void init() {
		super.init();

		addRenderableWidget(new GuiMGUButton(leftPos + 54, topPos + 42, GuiMGUButton.Size.LARGE, 0, Component.empty(), (button) -> {
			MobGrindingUtils.NETWORK_WRAPPER.sendToServer(new MessageFan(0, tile.getBlockPos()));
			tile.showRenderBox = !tile.showRenderBox;
		}));
	}

	@Override
	protected void renderLabels(@Nonnull GuiGraphics gg, int mouseX, int mouseY) {
		super.renderLabels(gg, mouseX, mouseY);

		gg.drawString(font, !tile.showRenderBox ? "Show Area" : "Hide Area", imageWidth - 88 - font.width(!tile.showRenderBox ? "Show Area" : "Hide Area") / 2.0f, 46, 14737632, true);
	}
}