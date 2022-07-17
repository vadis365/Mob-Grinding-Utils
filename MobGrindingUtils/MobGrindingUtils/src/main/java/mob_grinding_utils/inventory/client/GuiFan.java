package mob_grinding_utils.inventory.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.inventory.server.ContainerFan;
import mob_grinding_utils.network.MessageFan;
import mob_grinding_utils.tile.TileEntityFan;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
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
	public void render(@Nonnull PoseStack stack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(stack);
		super.render(stack, mouseX, mouseY, partialTicks);
		renderTooltip(stack, mouseX, mouseY);
	}

	@Override
	protected void renderLabels(@Nonnull PoseStack stack, int mouseX, int mouseY) {
		String title = Component.translatable("block.mob_grinding_utils.fan").getString();
		fontRenderer.draw(stack, title, imageWidth / 2.0f - fontRenderer.width(title) / 2.0f, imageHeight - 218, 4210752);
		fontRenderer.drawShadow(stack, !tile.showRenderBox ? "Show Area" : "Hide Area", imageWidth - 88 - fontRenderer.width(!tile.showRenderBox ? "Show Area" : "Hide Area") / 2.0f, imageHeight - 178, 14737632);
	}

	@Override
	protected void renderBg(@Nonnull PoseStack stack, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, GUI_FAN);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		int xOffSet = (width - imageWidth) / 2;
		int yOffSet = (height - imageHeight) / 2;
		this.blit(stack, xOffSet, yOffSet, 0, 0, imageWidth, imageHeight);
	}
}