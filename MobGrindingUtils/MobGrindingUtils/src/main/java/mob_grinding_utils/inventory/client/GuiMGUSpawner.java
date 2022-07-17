package mob_grinding_utils.inventory.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.inventory.server.ContainerMGUSpawner;
import mob_grinding_utils.network.MessageEntitySpawner;
import mob_grinding_utils.tile.TileEntityMGUSpawner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
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
	public void render(@Nonnull PoseStack stack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(stack);
		super.render(stack, mouseX, mouseY, partialTicks);
		renderTooltip(stack, mouseX, mouseY);
	}

	@Override
	protected void renderLabels(@Nonnull PoseStack stack, int mouseX, int mouseY) {
		fontRenderer.draw(stack, Component.translatable("block.mob_grinding_utils.entity_spawner").getString(), 8, imageHeight - 220, 4210752);

		fontRenderer.draw(stack, Component.translatable("block.mob_grinding_utils.absorption_hopper_d_u").getString(), 102, imageHeight - 212, 4210752);

		fontRenderer.draw(stack, Component.translatable("block.mob_grinding_utils.absorption_hopper_n_s").getString(), 102, imageHeight - 178, 4210752);
		fontRenderer.draw(stack, Component.translatable("block.mob_grinding_utils.absorption_hopper_w_e").getString(), 102, imageHeight - 144, 4210752);
	
		fontRenderer.drawShadow(stack, !tile.showRenderBox ? "Show Area" : "Hide Area", imageWidth - 41 - fontRenderer.width(!tile.showRenderBox ? "Show Area" : "Hide Area") / 2.0f, imageHeight - 109, 14737632);

		if(tile.getProgress() > 0)
			fontRenderer.draw(stack, "Attempting Spawn", imageWidth- 140 - fontRenderer.width("Attempting") / 2.0f, imageHeight - 128, 4210752);
	}

	@Override
	protected void renderBg(@Nonnull PoseStack stack, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, GUI_TEXTURE);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		int xOffSet = (width - imageWidth) / 2;
		int yOffSet = (height - imageHeight) / 2;
		this.blit(stack, xOffSet, yOffSet, 0, 0, imageWidth, imageHeight);
		this.blit(stack, xOffSet + 44, yOffSet + 71 - tile.getProgressScaled(28), 178, 28 - tile.getProgressScaled(28), 16, 28);

		String OFFSETX = String.valueOf(tile.getoffsetX());
		String OFFSETY = String.valueOf(tile.getoffsetY());
		String OFFSETZ = String.valueOf(tile.getoffsetZ());

		fontRenderer.draw(stack, I18n.get(OFFSETY), xOffSet + 135 - fontRenderer.width(I18n.get(OFFSETY)) / 2.0f, yOffSet + 29, 5285857);//NS
		fontRenderer.draw(stack, I18n.get(OFFSETZ), xOffSet + 135 - fontRenderer.width(I18n.get(OFFSETZ)) / 2.0f, yOffSet + 63, 5285857);//WE
		fontRenderer.draw(stack, I18n.get(OFFSETX), xOffSet + 135 - fontRenderer.width(I18n.get(OFFSETX)) / 2.0f, yOffSet + 97, 5285857);//DU
	}

}