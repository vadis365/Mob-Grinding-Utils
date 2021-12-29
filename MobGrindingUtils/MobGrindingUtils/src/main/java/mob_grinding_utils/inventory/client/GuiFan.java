package mob_grinding_utils.inventory.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.inventory.server.ContainerFan;
import mob_grinding_utils.network.MessageFan;
import mob_grinding_utils.tile.TileEntityFan;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiFan extends AbstractContainerScreen<ContainerFan> {

	private static final ResourceLocation GUI_FAN = new ResourceLocation("mob_grinding_utils:textures/gui/fan_gui.png");
	protected final ContainerFan container;
	private final TileEntityFan tile;

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
		addButton(new GuiMGUButton(xOffSet + 54, yOffSet + 42, GuiMGUButton.Size.LARGE, 0, TextComponent.EMPTY, (button) -> {
			MobGrindingUtils.NETWORK_WRAPPER.sendToServer(new MessageFan(0, tile.getBlockPos()));
			tile.showRenderBox = !tile.showRenderBox;
		}));
	}

	@Override
	public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(stack);
		super.render(stack, mouseX, mouseY, partialTicks);
		renderTooltip(stack, mouseX, mouseY);
	}

	@Override
	protected void renderLabels(PoseStack stack, int mouseX, int mouseY) {
		String title = new TranslatableComponent("block.mob_grinding_utils.fan").getString();
		Minecraft.getInstance().font.draw(stack, title, imageWidth / 2 - Minecraft.getInstance().font.width(title) / 2, imageHeight - 218, 4210752);
		Minecraft.getInstance().font.drawShadow(stack, !tile.showRenderBox ? "Show Area" : "Hide Area", imageWidth - 88 - Minecraft.getInstance().font.width(!tile.showRenderBox ? "Show Area" : "Hide Area") / 2, imageHeight - 178, 14737632);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		getMinecraft().getTextureManager().bind(GUI_FAN);
		int xOffSet = (width - imageWidth) / 2;
		int yOffSet = (height - imageHeight) / 2;
		this.blit(stack, xOffSet, yOffSet, 0, 0, imageWidth, imageHeight);
	}
}