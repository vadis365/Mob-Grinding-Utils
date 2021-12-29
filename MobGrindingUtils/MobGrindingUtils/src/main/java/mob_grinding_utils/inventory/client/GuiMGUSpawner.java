package mob_grinding_utils.inventory.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.inventory.server.ContainerMGUSpawner;
import mob_grinding_utils.network.MessageEntitySpawner;
import mob_grinding_utils.tile.TileEntityMGUSpawner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public class GuiMGUSpawner extends AbstractContainerScreen<ContainerMGUSpawner> {

	private static final ResourceLocation GUI_TEXTURE = new ResourceLocation("mob_grinding_utils:textures/gui/entity_spawner_gui.png");
	protected final ContainerMGUSpawner container;
	private final TileEntityMGUSpawner tile;
	Font fontRenderer = Minecraft.getInstance().font;

	public GuiMGUSpawner(ContainerMGUSpawner container, Inventory playerInventory, Component name) {
		super(container, playerInventory, name);
		this.container = container;
		this.tile = this.container.tile;
		imageHeight = 226;
		imageWidth = 176;
	}

	@Override
	public void init() {
		super.init();
		buttons.clear();
		int xOffSet = (width - imageWidth) / 2;
		int yOffSet = (height - imageHeight) / 2;

		Button.OnPress message = new Button.OnPress() {
			@Override
			public void onPress(Button button) {
				if (button instanceof GuiMGUButton)
				MobGrindingUtils.NETWORK_WRAPPER.sendToServer(new MessageEntitySpawner(inventory.player, ((GuiMGUButton)button).id, tile.getBlockPos()));
			}
		};

		addButton(new GuiMGUButton(xOffSet + 101, yOffSet + 113, GuiMGUButton.Size.LARGE, 0, TextComponent.EMPTY, (button) -> {
			MobGrindingUtils.NETWORK_WRAPPER.sendToServer(new MessageEntitySpawner(inventory.player, 0, tile.getBlockPos()));
			tile.showRenderBox = !tile.showRenderBox;
		}));

		addButton(new GuiMGUButton(xOffSet + 101, yOffSet + 25, GuiMGUButton.Size.SMALL, 1, new TextComponent("-"), message));
		addButton(new GuiMGUButton(xOffSet + 153, yOffSet + 25, GuiMGUButton.Size.SMALL, 2, new TextComponent("+"), message));
		addButton(new GuiMGUButton(xOffSet + 101, yOffSet + 59, GuiMGUButton.Size.SMALL, 3, new TextComponent("-"), message));
		addButton(new GuiMGUButton(xOffSet + 153, yOffSet + 59, GuiMGUButton.Size.SMALL, 4, new TextComponent("+"), message));
		addButton(new GuiMGUButton(xOffSet + 101, yOffSet + 93, GuiMGUButton.Size.SMALL, 5, new TextComponent("-"), message));
		addButton(new GuiMGUButton(xOffSet + 153, yOffSet + 93, GuiMGUButton.Size.SMALL, 6, new TextComponent("+"), message));
	}

	@Override
	public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(stack);
		super.render(stack, mouseX, mouseY, partialTicks);
		renderTooltip(stack, mouseX, mouseY);
	}

	@Override
	protected void renderLabels(PoseStack stack, int mouseX, int mouseY) {
		fontRenderer.draw(stack, new TranslatableComponent("block.mob_grinding_utils.entity_spawner").getString(), 8, imageHeight - 220, 4210752);

		fontRenderer.draw(stack, new TranslatableComponent("block.mob_grinding_utils.absorption_hopper_d_u").getString(), 102, imageHeight - 212, 4210752);

		fontRenderer.draw(stack, new TranslatableComponent("block.mob_grinding_utils.absorption_hopper_n_s").getString(), 102, imageHeight - 178, 4210752);
		fontRenderer.draw(stack, new TranslatableComponent("block.mob_grinding_utils.absorption_hopper_w_e").getString(), 102, imageHeight - 144, 4210752);
	
		fontRenderer.drawShadow(stack, !tile.showRenderBox ? "Show Area" : "Hide Area", imageWidth - 41 - fontRenderer.width(!tile.showRenderBox ? "Show Area" : "Hide Area") / 2, imageHeight - 109, 14737632);

		if(tile.getProgress() > 0)
			fontRenderer.draw(stack, "Attempting Spawn", imageWidth- 140 - fontRenderer.width("Attempting") / 2, imageHeight - 128, 4210752);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		getMinecraft().getTextureManager().bind(GUI_TEXTURE);
		int xOffSet = (width - imageWidth) / 2;
		int yOffSet = (height - imageHeight) / 2;
		int zLevel = 0; /// this may need increasing depending on layers
		this.blit(stack, xOffSet, yOffSet, 0, 0, imageWidth, imageHeight);
		this.blit(stack, xOffSet + 44, yOffSet + 71 - tile.getProgressScaled(28), 178, 28 - tile.getProgressScaled(28), 16, 28);

		String OFFSETX = String.valueOf(tile.getoffsetX());
		String OFFSETY = String.valueOf(tile.getoffsetY());
		String OFFSETZ = String.valueOf(tile.getoffsetZ());

		fontRenderer.draw(stack, I18n.get(OFFSETY), xOffSet + 135 - fontRenderer.width(I18n.get(OFFSETY)) / 2, yOffSet + 29, 5285857);//NS
		fontRenderer.draw(stack, I18n.get(OFFSETZ), xOffSet + 135 - fontRenderer.width(I18n.get(OFFSETZ)) / 2, yOffSet + 63, 5285857);//WE
		fontRenderer.draw(stack, I18n.get(OFFSETX), xOffSet + 135 - fontRenderer.width(I18n.get(OFFSETX)) / 2, yOffSet + 97, 5285857);//DU
	}

}