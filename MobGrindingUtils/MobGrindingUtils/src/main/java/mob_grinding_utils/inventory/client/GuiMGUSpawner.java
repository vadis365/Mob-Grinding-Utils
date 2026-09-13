package mob_grinding_utils.inventory.client;

import mob_grinding_utils.BlockEntities.BlockEntityMGUSpawner;
import mob_grinding_utils.inventory.server.ContainerMGUSpawner;
import mob_grinding_utils.network.BEGuiClick;
import mob_grinding_utils.util.RL;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import javax.annotation.Nonnull;

public class GuiMGUSpawner extends MGUScreen<ContainerMGUSpawner> {
	protected final ContainerMGUSpawner container;
	private final BlockEntityMGUSpawner tile;
	private GuiMGUButton areaButton;

	public GuiMGUSpawner(ContainerMGUSpawner container, Inventory playerInventory, Component name) {
		super(container, playerInventory, name, RL.mgu("textures/gui/entity_spawner_gui.png"), 176, 226);
		this.container = container;
		this.tile = this.container.tile;
	}

	@Override
	public void init() {
		super.init();
		clearWidgets();

		Button.OnPress message = button -> {
			if (button instanceof GuiMGUButton)
				ClientPacketDistributor.sendToServer(new BEGuiClick(tile.getBlockPos(), ((GuiMGUButton)button).id));
		};

		areaButton = addRenderableWidget(new GuiMGUButton(leftPos + 101, topPos + 113, GuiMGUButton.Size.LARGE, 0, areaLabel(), (button) -> {
			ClientPacketDistributor.sendToServer(new BEGuiClick(tile.getBlockPos(), 0));
			tile.showRenderBox = !tile.showRenderBox;
			button.setMessage(areaLabel());
		}));

		addRenderableWidget(new GuiMGUButton(leftPos + 101, topPos + 25, GuiMGUButton.Size.SMALL, 1, Component.literal("-"), message));
		addRenderableWidget(new GuiMGUButton(leftPos + 153, topPos + 25, GuiMGUButton.Size.SMALL, 2, Component.literal("+"), message));
		addRenderableWidget(new GuiMGUButton(leftPos + 101, topPos + 59, GuiMGUButton.Size.SMALL, 3, Component.literal("-"), message));
		addRenderableWidget(new GuiMGUButton(leftPos + 153, topPos + 59, GuiMGUButton.Size.SMALL, 4, Component.literal("+"), message));
		addRenderableWidget(new GuiMGUButton(leftPos + 101, topPos + 93, GuiMGUButton.Size.SMALL, 5, Component.literal("-"), message));
		addRenderableWidget(new GuiMGUButton(leftPos + 153, topPos + 93, GuiMGUButton.Size.SMALL, 6, Component.literal("+"), message));
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
	protected void extractLabels(@Nonnull GuiGraphicsExtractor gg, int mouseX, int mouseY) {
		gg.text(font, title, 8, imageHeight - 220, ARGB.opaque(4210752), false);

		gg.text(font, Component.translatable("block.mob_grinding_utils.absorption_hopper_d_u"), 102, 14, ARGB.opaque(4210752), false);
		gg.text(font, Component.translatable("block.mob_grinding_utils.absorption_hopper_n_s"), 102, 48, ARGB.opaque(4210752), false);
		gg.text(font, Component.translatable("block.mob_grinding_utils.absorption_hopper_w_e"), 102, 82, ARGB.opaque(4210752), false);

		if (tile.getProgress() > 0) {
			String attempting = "Attempting Spawn";
			gg.text(font, attempting, (int) (52 - font.width(attempting) / 2.0f), 98, ARGB.opaque(4210752), false);
		}

		gg.centeredText(font, String.valueOf(tile.getoffsetY()), 135, 29, ARGB.opaque(5285857));
		gg.centeredText(font, String.valueOf(tile.getoffsetZ()), 135, 63, ARGB.opaque(5285857));
		gg.centeredText(font, String.valueOf(tile.getoffsetX()), 135, 97, ARGB.opaque(5285857));
	}

	@Override
	public void extractBackground(@Nonnull GuiGraphicsExtractor gg, int mouseX, int mouseY, float partialTicks) {
		super.extractBackground(gg, mouseX, mouseY, partialTicks);
		int progress = tile.getProgressScaled(28);
		gg.blit(RenderPipelines.GUI_TEXTURED, TEX, leftPos + 44, topPos + 71 - progress, 178.0F, 28.0F - progress, 16, 28, 256, 256);
	}
}
