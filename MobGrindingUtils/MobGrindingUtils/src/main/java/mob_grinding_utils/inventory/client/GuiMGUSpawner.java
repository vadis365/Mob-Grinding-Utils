package mob_grinding_utils.inventory.client;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.inventory.server.ContainerMGUSpawner;
import mob_grinding_utils.network.MessageEntitySpawner;
import mob_grinding_utils.tile.TileEntityMGUSpawner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nonnull;

public class GuiMGUSpawner extends AbstractContainerScreen<ContainerMGUSpawner> {

	private static final ResourceLocation GUI_TEXTURE = new ResourceLocation("mob_grinding_utils:textures/gui/entity_spawner_gui.png");
	protected final ContainerMGUSpawner container;
	private final TileEntityMGUSpawner tile;
	private final Player player;
	Font fontRenderer = Minecraft.getInstance().font;

	public GuiMGUSpawner(ContainerMGUSpawner container, Inventory playerInventory, Component name) {
		super(container, playerInventory, name);
		this.container = container;
		this.tile = this.container.tile;
		this.player = playerInventory.player;
		imageHeight = 226;
		imageWidth = 176;
	}

	@Override
	public void init() {
		super.init();
		clearWidgets();
		int xOffSet = (width - imageWidth) / 2;
		int yOffSet = (height - imageHeight) / 2;

		Button.OnPress message = button -> {
			if (button instanceof GuiMGUButton)
				MobGrindingUtils.NETWORK_WRAPPER.sendToServer(new MessageEntitySpawner(player, ((GuiMGUButton)button).id, tile.getBlockPos()));
		};

		addRenderableWidget(new GuiMGUButton(xOffSet + 101, yOffSet + 113, GuiMGUButton.Size.LARGE, 0, Component.empty(), (button) -> {
			MobGrindingUtils.NETWORK_WRAPPER.sendToServer(new MessageEntitySpawner(player, 0, tile.getBlockPos()));
			tile.showRenderBox = !tile.showRenderBox;
		}));

		addRenderableWidget(new GuiMGUButton(xOffSet + 101, yOffSet + 25, GuiMGUButton.Size.SMALL, 1, Component.literal("-"), message));
		addRenderableWidget(new GuiMGUButton(xOffSet + 153, yOffSet + 25, GuiMGUButton.Size.SMALL, 2, Component.literal("+"), message));
		addRenderableWidget(new GuiMGUButton(xOffSet + 101, yOffSet + 59, GuiMGUButton.Size.SMALL, 3, Component.literal("-"), message));
		addRenderableWidget(new GuiMGUButton(xOffSet + 153, yOffSet + 59, GuiMGUButton.Size.SMALL, 4, Component.literal("+"), message));
		addRenderableWidget(new GuiMGUButton(xOffSet + 101, yOffSet + 93, GuiMGUButton.Size.SMALL, 5, Component.literal("-"), message));
		addRenderableWidget(new GuiMGUButton(xOffSet + 153, yOffSet + 93, GuiMGUButton.Size.SMALL, 6, Component.literal("+"), message));
	}

	@Override
	public void render(@Nonnull GuiGraphics gg, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(gg);
		super.render(gg, mouseX, mouseY, partialTicks);
		renderTooltip(gg, mouseX, mouseY);
	}

	@Override
	protected void renderLabels(@Nonnull GuiGraphics gg, int mouseX, int mouseY) {
		gg.drawString(font, Component.translatable("block.mob_grinding_utils.entity_spawner").getString(), 8, imageHeight - 220, 4210752, false);

		gg.drawString(font, Component.translatable("block.mob_grinding_utils.absorption_hopper_d_u").getString(), 102, imageHeight - 212, 4210752, false);

		gg.drawString(font, Component.translatable("block.mob_grinding_utils.absorption_hopper_n_s").getString(), 102, imageHeight - 178, 4210752, false);
		gg.drawString(font, Component.translatable("block.mob_grinding_utils.absorption_hopper_w_e").getString(), 102, imageHeight - 144, 4210752, false);

		gg.drawString(font, !tile.showRenderBox ? "Show Area" : "Hide Area", imageWidth - 41 - fontRenderer.width(!tile.showRenderBox ? "Show Area" : "Hide Area") / 2.0f, imageHeight - 109, 14737632, true);

		if(tile.getProgress() > 0)
			gg.drawString(font, "Attempting Spawn", imageWidth- 140 - fontRenderer.width("Attempting") / 2.0f, imageHeight - 128, 4210752, false);
	}

	@Override
	protected void renderBg(@Nonnull GuiGraphics gg, float partialTicks, int mouseX, int mouseY) {
		int xOffSet = (width - imageWidth) / 2;
		int yOffSet = (height - imageHeight) / 2;
		gg.blit(GUI_TEXTURE, xOffSet, yOffSet, 0, 0, imageWidth, imageHeight);
		gg.blit(GUI_TEXTURE, xOffSet + 44, yOffSet + 71 - tile.getProgressScaled(28), 178, 28 - tile.getProgressScaled(28), 16, 28);

		String OFFSETX = String.valueOf(tile.getoffsetX());
		String OFFSETY = String.valueOf(tile.getoffsetY());
		String OFFSETZ = String.valueOf(tile.getoffsetZ());

		gg.drawString(font, I18n.get(OFFSETY), xOffSet + 135 - fontRenderer.width(I18n.get(OFFSETY)) / 2.0f, yOffSet + 29, 5285857, false);//NS
		gg.drawString(font, I18n.get(OFFSETZ), xOffSet + 135 - fontRenderer.width(I18n.get(OFFSETZ)) / 2.0f, yOffSet + 63, 5285857, false);//WE
		gg.drawString(font, I18n.get(OFFSETX), xOffSet + 135 - fontRenderer.width(I18n.get(OFFSETX)) / 2.0f, yOffSet + 97, 5285857, false);//DU
	}

}