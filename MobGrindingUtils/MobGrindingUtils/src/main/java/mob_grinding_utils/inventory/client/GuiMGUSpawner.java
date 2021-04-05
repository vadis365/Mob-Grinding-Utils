package mob_grinding_utils.inventory.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.inventory.server.ContainerMGUSpawner;
import mob_grinding_utils.network.MessageEntitySpawner;
import mob_grinding_utils.tile.TileEntityMGUSpawner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class GuiMGUSpawner extends ContainerScreen<ContainerMGUSpawner> {

	private static final ResourceLocation GUI_TEXTURE = new ResourceLocation("mob_grinding_utils:textures/gui/entity_spawner_gui.png");
	protected final ContainerMGUSpawner container;
	private final TileEntityMGUSpawner tile;
	FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;

	public GuiMGUSpawner(ContainerMGUSpawner container, PlayerInventory playerInventory, ITextComponent name) {
		super(container, playerInventory, name);
		this.container = container;
		this.tile = this.container.tile;
		ySize = 226;
		xSize = 176;
	}

	@Override
	public void init() {
		super.init();
		buttons.clear();
		int xOffSet = (width - xSize) / 2;
		int yOffSet = (height - ySize) / 2;

		Button.IPressable message = new Button.IPressable() {
			@Override
			public void onPress(Button button) {
				if (button instanceof GuiMGUButton)
				MobGrindingUtils.NETWORK_WRAPPER.sendToServer(new MessageEntitySpawner(playerInventory.player, ((GuiMGUButton)button).id, tile.getPos()));
			}
		};

		addButton(new GuiMGUButton(xOffSet + 101, yOffSet + 113, GuiMGUButton.Size.LARGE, 0, StringTextComponent.EMPTY, (button) -> {
			MobGrindingUtils.NETWORK_WRAPPER.sendToServer(new MessageEntitySpawner(playerInventory.player, 0, tile.getPos()));
			tile.showRenderBox = !tile.showRenderBox;
		}));

		addButton(new GuiMGUButton(xOffSet + 101, yOffSet + 25, GuiMGUButton.Size.SMALL, 1, new StringTextComponent("-"), message));
		addButton(new GuiMGUButton(xOffSet + 153, yOffSet + 25, GuiMGUButton.Size.SMALL, 2, new StringTextComponent("+"), message));
		addButton(new GuiMGUButton(xOffSet + 101, yOffSet + 59, GuiMGUButton.Size.SMALL, 3, new StringTextComponent("-"), message));
		addButton(new GuiMGUButton(xOffSet + 153, yOffSet + 59, GuiMGUButton.Size.SMALL, 4, new StringTextComponent("+"), message));
		addButton(new GuiMGUButton(xOffSet + 101, yOffSet + 93, GuiMGUButton.Size.SMALL, 5, new StringTextComponent("-"), message));
		addButton(new GuiMGUButton(xOffSet + 153, yOffSet + 93, GuiMGUButton.Size.SMALL, 6, new StringTextComponent("+"), message));
	}

	@Override
	public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(stack);
		super.render(stack, mouseX, mouseY, partialTicks);
		renderHoveredTooltip(stack, mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack stack, int mouseX, int mouseY) {
		fontRenderer.drawString(stack, new TranslationTextComponent("block.mob_grinding_utils.entity_spawner").getString(), 8, ySize - 220, 4210752);

		fontRenderer.drawString(stack, new TranslationTextComponent("block.mob_grinding_utils.absorption_hopper_d_u").getString(), 102, ySize - 212, 4210752);

		fontRenderer.drawString(stack, new TranslationTextComponent("block.mob_grinding_utils.absorption_hopper_n_s").getString(), 102, ySize - 178, 4210752);
		fontRenderer.drawString(stack, new TranslationTextComponent("block.mob_grinding_utils.absorption_hopper_w_e").getString(), 102, ySize - 144, 4210752);
	
		fontRenderer.drawStringWithShadow(stack, !tile.showRenderBox ? "Show Area" : "Hide Area", xSize - 41 - fontRenderer.getStringWidth(!tile.showRenderBox ? "Show Area" : "Hide Area") / 2, ySize - 109, 14737632);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		getMinecraft().getTextureManager().bindTexture(GUI_TEXTURE);
		int xOffSet = (width - xSize) / 2;
		int yOffSet = (height - ySize) / 2;
		int zLevel = 0; /// this may need increasing depending on layers
		this.blit(stack, xOffSet, yOffSet, 0, 0, xSize, ySize);
		this.blit(stack, xOffSet + 44, yOffSet + 71 - tile.getProgressScaled(28), 178, 28 - tile.getProgressScaled(28), 16, 28);

		String OFFSETX = String.valueOf(tile.getoffsetX());
		String OFFSETY = String.valueOf(tile.getoffsetY());
		String OFFSETZ = String.valueOf(tile.getoffsetZ());

		fontRenderer.drawString(stack, I18n.format(OFFSETY), xOffSet + 135 - fontRenderer.getStringWidth(I18n.format(OFFSETY)) / 2, yOffSet + 29, 5285857);//NS
		fontRenderer.drawString(stack, I18n.format(OFFSETZ), xOffSet + 135 - fontRenderer.getStringWidth(I18n.format(OFFSETZ)) / 2, yOffSet + 63, 5285857);//WE
		fontRenderer.drawString(stack, I18n.format(OFFSETX), xOffSet + 135 - fontRenderer.getStringWidth(I18n.format(OFFSETX)) / 2, yOffSet + 97, 5285857);//DU
	}

}