package mob_grinding_utils.inventory.client;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.inventory.server.ContainerFan;
import mob_grinding_utils.network.MessageFan;
import mob_grinding_utils.tile.TileEntityFan;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class GuiFan extends AbstractContainerScreen<ContainerFan> {

	private static final ResourceLocation GUI_FAN = new ResourceLocation("mob_grinding_utils:textures/gui/fan_gui.png");
	protected final ContainerFan container;
	private final TileEntityFan tile;
	private final Font fontRenderer = Minecraft.getInstance().font;

	public GuiFan(ContainerFan container, Inventory inventory, Component title) {
		super(container, inventory, title);
		this.container = container;
		this.tile = this.container.fan;
		imageHeight = 224;
	}

	@Override
	public void init() {
		super.init();
		int xOffSet = (width - imageWidth) / 2;
		int yOffSet = (height - imageHeight) / 2;
		addRenderableWidget(new GuiMGUButton(xOffSet + 54, yOffSet + 42, GuiMGUButton.Size.LARGE, 0, Component.empty(), (button) -> {
			MobGrindingUtils.NETWORK_WRAPPER.sendToServer(new MessageFan(0, tile.getBlockPos()));
			tile.showRenderBox = !tile.showRenderBox;
		}));
	}

	@Override
	public void render(@Nonnull GuiGraphics gg, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(gg);
		super.render(gg, mouseX, mouseY, partialTicks);
		renderTooltip(gg, mouseX, mouseY);
	}

	@Override
	protected void renderLabels(@Nonnull GuiGraphics gg, int mouseX, int mouseY) {
		String title = Component.translatable("block.mob_grinding_utils.fan").getString();
		gg.drawString(font, title, imageWidth / 2.0f - fontRenderer.width(title) / 2.0f, imageHeight - 218, 4210752, false);
		gg.drawString(font, !tile.showRenderBox ? "Show Area" : "Hide Area", imageWidth - 88 - fontRenderer.width(!tile.showRenderBox ? "Show Area" : "Hide Area") / 2.0f, imageHeight - 178, 14737632, true);
	}

	@Override
	protected void renderBg(@Nonnull GuiGraphics gg, float partialTicks, int mouseX, int mouseY) {
		int xOffSet = (width - imageWidth) / 2;
		int yOffSet = (height - imageHeight) / 2;
		gg.blit(GUI_FAN, xOffSet, yOffSet, 0, 0, imageWidth, imageHeight);
	}
}