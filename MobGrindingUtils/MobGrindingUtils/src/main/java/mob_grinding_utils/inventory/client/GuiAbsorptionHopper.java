package mob_grinding_utils.inventory.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import mob_grinding_utils.inventory.server.ContainerAbsorptionHopper;
import mob_grinding_utils.network.BEGuiClick;
import mob_grinding_utils.tile.TileEntityAbsorptionHopper;
import mob_grinding_utils.tile.TileEntityAbsorptionHopper.EnumStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.network.PacketDistributor;

public class GuiAbsorptionHopper extends MGUScreen<ContainerAbsorptionHopper> {

	protected final ContainerAbsorptionHopper container;
	private final TileEntityAbsorptionHopper tile;
	private final Player player;

	private Fluid oldFluid;
	private TextureAtlasSprite sprite;

	public GuiAbsorptionHopper(ContainerAbsorptionHopper container, Inventory playerInventory, Component title) {
		super(container, playerInventory, title, new ResourceLocation("mob_grinding_utils:textures/gui/absorption_hopper_gui.png"));
		this.container = container;
		this.tile = this.container.hopper;
		this.player = playerInventory.player;
		imageHeight = 226;
		imageWidth = 248;
	}

	@Override
	public void init() {
		super.init();
		clearWidgets();

		Button.OnPress message = button -> {
			if (button instanceof GuiMGUButton)
				PacketDistributor.SERVER.noArg().send(new BEGuiClick(tile.getBlockPos(), ((GuiMGUButton)button).id));
		};

		addRenderableWidget(new GuiMGUButton(leftPos + 7, topPos + 17, GuiMGUButton.Size.MEDIUM, 0, Component.literal("Down"), message));
		addRenderableWidget(new GuiMGUButton(leftPos + 7, topPos + 34, GuiMGUButton.Size.MEDIUM, 1, Component.literal("Up"), message));
		addRenderableWidget(new GuiMGUButton(leftPos + 7, topPos + 51, GuiMGUButton.Size.MEDIUM, 2, Component.literal("North"), message));
		addRenderableWidget(new GuiMGUButton(leftPos + 82, topPos + 17, GuiMGUButton.Size.MEDIUM, 3, Component.literal("South"), message));
		addRenderableWidget(new GuiMGUButton(leftPos + 82, topPos + 34, GuiMGUButton.Size.MEDIUM, 4, Component.literal("West"), message));
		addRenderableWidget(new GuiMGUButton(leftPos + 82, topPos + 51, GuiMGUButton.Size.MEDIUM, 5, Component.literal("East"), message));

		addRenderableWidget(new GuiMGUButton(leftPos + 173, topPos + 113, GuiMGUButton.Size.LARGE, 6, Component.empty(), (button) -> {
			PacketDistributor.SERVER.noArg().send(new BEGuiClick(tile.getBlockPos(), 6));
			tile.showRenderBox = !tile.showRenderBox;
		}));

		addRenderableWidget(new GuiMGUButton(leftPos + 173, topPos + 25, GuiMGUButton.Size.SMALL, 7, Component.literal("-"), message));
		addRenderableWidget(new GuiMGUButton(leftPos + 225, topPos + 25, GuiMGUButton.Size.SMALL, 8, Component.literal("+"), message));
		addRenderableWidget(new GuiMGUButton(leftPos + 173, topPos + 59, GuiMGUButton.Size.SMALL, 9, Component.literal("-"), message));
		addRenderableWidget(new GuiMGUButton(leftPos + 225, topPos + 59, GuiMGUButton.Size.SMALL, 10, Component.literal("+"), message));
		addRenderableWidget(new GuiMGUButton(leftPos + 173, topPos + 93, GuiMGUButton.Size.SMALL, 11, Component.literal("-"), message));
		addRenderableWidget(new GuiMGUButton(leftPos + 225, topPos + 93, GuiMGUButton.Size.SMALL, 12, Component.literal("+"), message));
	}

	@Override
	protected void renderLabels(GuiGraphics gg, int mouseX, int mouseY) {
		gg.drawString(font, getTitle(), 8, 6, 4210752, false);

		gg.drawString(font, Component.translatable("block.mob_grinding_utils.absorption_hopper_d_u").getString(), 174, 14, 4210752, false);
		gg.drawString(font, Component.translatable("block.mob_grinding_utils.absorption_hopper_n_s").getString(), 174, 48, 4210752, false);
		gg.drawString(font, Component.translatable("block.mob_grinding_utils.absorption_hopper_w_e").getString(), 174, 82, 4210752, false);

		gg.drawString(font, !tile.showRenderBox ? "Show Area" : "Hide Area", 207 - font.width(!tile.showRenderBox ? "Show Area" : "Hide Area") / 2.0f, 117, 14737632, true);

		EnumStatus DOWN = tile.getSideStatus(Direction.DOWN);
		EnumStatus UP = tile.getSideStatus(Direction.UP);
		EnumStatus NORTH = tile.getSideStatus(Direction.NORTH);
		EnumStatus SOUTH = tile.getSideStatus(Direction.SOUTH);
		EnumStatus WEST = tile.getSideStatus(Direction.WEST);
		EnumStatus EAST = tile.getSideStatus(Direction.EAST);

		gg.drawCenteredString(font, DOWN.getSerializedName(), 58, 21, getModeColour(DOWN.ordinal()));
		gg.drawCenteredString(font, UP.getSerializedName(), 58, 38, getModeColour(UP.ordinal()));
		gg.drawCenteredString(font, NORTH.getSerializedName(), 58, 55, getModeColour(NORTH.ordinal()));
		gg.drawCenteredString(font, SOUTH.getSerializedName(), 133, 21, getModeColour(SOUTH.ordinal()));
		gg.drawCenteredString(font, WEST.getSerializedName(), 133, 38, getModeColour(WEST.ordinal()));
		gg.drawCenteredString(font, EAST.getSerializedName(), 133, 55, getModeColour(EAST.ordinal()));
		gg.drawCenteredString(font, String.valueOf(tile.getoffsetY()), 207, 29, 5285857);//NS
		gg.drawCenteredString(font, String.valueOf(tile.getoffsetZ()), 207, 63, 5285857);//WE
		gg.drawCenteredString(font, String.valueOf(tile.getoffsetX()), 207, 97, 5285857);//DU
	}

	@Override
	protected void renderBg(GuiGraphics gg, float partialTicks, int mouseX, int mouseY) {
		int zLevel = 0; /// this may need increasing depending on layers
		gg.blit(TEX, leftPos, topPos, 0, 0, imageWidth, imageHeight);

		int scaledFluid = tile.getScaledFluid(121);
		if (scaledFluid >= 1) {
			FluidStack stack = tile.tank.getFluid();
			IClientFluidTypeExtensions fluidTypeExtension = IClientFluidTypeExtensions.of(stack.getFluid());
			int color = fluidTypeExtension.getTintColor(stack);
			float red = (float)(FastColor.ARGB32.red(color) / 255.0);
			float green = (float)(FastColor.ARGB32.green(color) / 255.0);
			float blue = (float)(FastColor.ARGB32.blue(color) / 255.0);
			float alpha = (float)(FastColor.ARGB32.alpha(color) / 255.0);
			ResourceLocation stillTexture = fluidTypeExtension.getStillTexture();

			if (this.sprite == null || this.oldFluid != stack.getFluid()) {
				this.oldFluid = stack.getFluid();

				AbstractTexture texture = Minecraft.getInstance().getTextureManager().getTexture(InventoryMenu.BLOCK_ATLAS);
				if (texture instanceof TextureAtlas) {
					TextureAtlasSprite sprite = ((TextureAtlas) texture).getSprite(stillTexture);
					if (sprite != null) {
						this.sprite = sprite;
					}
				}
			}

			if (this.sprite != null) { // Pulled byscos fix in from AA
				float minU = sprite.getU0();
				float maxU = sprite.getU1();
				float minV = sprite.getV0();
				float maxV = sprite.getV1();
				float deltaV = maxV - minV;

				double tankLevel = scaledFluid;

				RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
				RenderSystem.setShaderColor(red, green, blue, alpha);
				RenderSystem.enableBlend();
				int count = 1 + ((int) Math.ceil(tankLevel)) / 16;
				for (int i = 0; i < count; i++) {
					double subHeight = Math.min(16.0, tankLevel - (16.0 * i));
					double offsetY = 120 - 16.0 * i - subHeight;
					drawQuad(leftPos + 156, topPos + 8 + offsetY, 12, subHeight, minU, (float) (maxV - deltaV * (subHeight / 16.0)), maxU, maxV);
				}
				RenderSystem.disableBlend();
				RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			}
		}
		gg.blit(TEX, leftPos + 153, topPos + 8 , 248, 0, 6, 120);
	}

	private void drawQuad(double x, double y, double width, double height, float minU, float minV, float maxU, float maxV) {
		Tesselator tesselator = Tesselator.getInstance();
		BufferBuilder buffer = tesselator.getBuilder();
		buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		buffer.vertex(x, y + height, 0).uv(minU, maxV).endVertex();
		buffer.vertex(x + width, y + height, 0).uv(maxU, maxV).endVertex();
		buffer.vertex(x + width, y, 0).uv(maxU, minV).endVertex();
		buffer.vertex(x, y, 0).uv(minU, minV).endVertex();
		tesselator.end();
	}

	public int getModeColour(int index) {
		return switch (index) {
			case 0 -> 16711680;
			case 1 -> 5285857;
			case 2 -> 16776960;
			default -> 16776960;
		};
	}
}