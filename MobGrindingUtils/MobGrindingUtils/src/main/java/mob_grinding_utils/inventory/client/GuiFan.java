package mob_grinding_utils.inventory.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.inventory.server.ContainerFan;
import mob_grinding_utils.network.MessageFan;
import mob_grinding_utils.tile.TileEntityFan;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiFan extends ContainerScreen<ContainerFan> {

	private static final ResourceLocation GUI_FAN = new ResourceLocation("mob_grinding_utils:textures/gui/fan_gui.png");
	protected final ContainerFan container;
	private final TileEntityFan tile;

	public GuiFan(ContainerFan container, PlayerInventory inventory, ITextComponent title) {
		super(container, inventory, title);
		this.container = container;
		this.tile = this.container.fan;
		ySize = 224;
	}

	@Override
	public void init() {
		super.init();
		int xOffSet = (width - xSize) / 2;
		int yOffSet = (height - ySize) / 2;
		addButton(new GuiMGUButton(xOffSet + 54, yOffSet + 42, GuiMGUButton.Size.LARGE, 0, StringTextComponent.EMPTY, (button) -> {
			MobGrindingUtils.NETWORK_WRAPPER.sendToServer(new MessageFan(0, tile.getPos()));
			tile.showRenderBox = !tile.showRenderBox;
		}));
	}

	@Override
	public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(stack);
		super.render(stack, mouseX, mouseY, partialTicks);
		renderHoveredTooltip(stack, mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack stack, int mouseX, int mouseY) {
		String title = new TranslationTextComponent("block.mob_grinding_utils.fan").getString();
		Minecraft.getInstance().fontRenderer.drawString(stack, title, xSize / 2 - Minecraft.getInstance().fontRenderer.getStringWidth(title) / 2, ySize - 218, 4210752);
		Minecraft.getInstance().fontRenderer.drawStringWithShadow(stack, !tile.showRenderBox ? "Show Area" : "Hide Area", xSize - 88 - Minecraft.getInstance().fontRenderer.getStringWidth(!tile.showRenderBox ? "Show Area" : "Hide Area") / 2, ySize - 178, 14737632);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		getMinecraft().getTextureManager().bindTexture(GUI_FAN);
		int xOffSet = (width - xSize) / 2;
		int yOffSet = (height - ySize) / 2;
		this.blit(stack, xOffSet, yOffSet, 0, 0, xSize, ySize);
	}
}