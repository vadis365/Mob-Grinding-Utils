package mob_grinding_utils.inventory.client;

import mob_grinding_utils.inventory.server.ContainerMGUSpawner;
import mob_grinding_utils.network.BELinkClick;
import mob_grinding_utils.tile.TileEntityMGUSpawner;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.Nonnull;

public class GuiMGUSpawner extends MGUScreen<ContainerMGUSpawner> {
	protected final ContainerMGUSpawner container;
	private final TileEntityMGUSpawner tile;

	public GuiMGUSpawner(ContainerMGUSpawner container, Inventory playerInventory, Component name) {
		super(container, playerInventory, name, new ResourceLocation("mob_grinding_utils:textures/gui/entity_spawner_gui.png"));
		this.container = container;
		this.tile = this.container.tile;
		imageHeight = 226;
		imageWidth = 176;
	}

	@Override
	public void init() {
		super.init();
		clearWidgets();

		Button.OnPress message = button -> {
			if (button instanceof GuiMGUButton)
				PacketDistributor.SERVER.noArg().send(new BELinkClick(tile.getBlockPos(), ((GuiMGUButton)button).id));
		};

		addRenderableWidget(new GuiMGUButton(leftPos + 101, topPos + 113, GuiMGUButton.Size.LARGE, 0, Component.empty(), (button) -> {
			PacketDistributor.SERVER.noArg().send(new BELinkClick(tile.getBlockPos(), 0));
			tile.showRenderBox = !tile.showRenderBox;
		}));

		addRenderableWidget(new GuiMGUButton(leftPos + 101, topPos + 25, GuiMGUButton.Size.SMALL, 1, Component.literal("-"), message));
		addRenderableWidget(new GuiMGUButton(leftPos + 153, topPos + 25, GuiMGUButton.Size.SMALL, 2, Component.literal("+"), message));
		addRenderableWidget(new GuiMGUButton(leftPos + 101, topPos + 59, GuiMGUButton.Size.SMALL, 3, Component.literal("-"), message));
		addRenderableWidget(new GuiMGUButton(leftPos + 153, topPos + 59, GuiMGUButton.Size.SMALL, 4, Component.literal("+"), message));
		addRenderableWidget(new GuiMGUButton(leftPos + 101, topPos + 93, GuiMGUButton.Size.SMALL, 5, Component.literal("-"), message));
		addRenderableWidget(new GuiMGUButton(leftPos + 153, topPos + 93, GuiMGUButton.Size.SMALL, 6, Component.literal("+"), message));
	}

	@Override
	protected void renderLabels(@Nonnull GuiGraphics gg, int mouseX, int mouseY) {
		gg.drawString(font, title, 8, imageHeight - 220, 4210752, false);

		gg.drawString(font, Component.translatable("block.mob_grinding_utils.absorption_hopper_d_u"), 102, 14, 4210752, false);

		gg.drawString(font, Component.translatable("block.mob_grinding_utils.absorption_hopper_n_s"), 102, 48, 4210752, false);
		gg.drawString(font, Component.translatable("block.mob_grinding_utils.absorption_hopper_w_e"), 102, 82, 4210752, false);

		gg.drawCenteredString(font, !tile.showRenderBox ? "Show Area" : "Hide Area", 135, 117, 14737632);

		if(tile.getProgress() > 0)
			gg.drawCenteredString(font, "Attempting Spawn", 52, 98, 4210752);

		gg.drawCenteredString(font, String.valueOf(tile.getoffsetY()), 135, 29, 5285857);//NS
		gg.drawCenteredString(font, String.valueOf(tile.getoffsetZ()), 135, 63, 5285857);//WE
		gg.drawCenteredString(font, String.valueOf(tile.getoffsetX()), 135, 97, 5285857);//DU
	}

	@Override
	protected void renderBg(@Nonnull GuiGraphics gg, float partialTicks, int mouseX, int mouseY) {
		super.renderBg(gg, partialTicks, mouseX, mouseY);

		gg.blit(TEX, leftPos + 44, topPos + 71 - tile.getProgressScaled(28), 178, 28 - tile.getProgressScaled(28), 16, 28);
	}

}