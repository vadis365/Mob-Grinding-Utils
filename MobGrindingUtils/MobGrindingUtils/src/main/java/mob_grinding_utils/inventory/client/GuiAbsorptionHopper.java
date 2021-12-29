package mob_grinding_utils.inventory.client;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.inventory.server.ContainerAbsorptionHopper;
import mob_grinding_utils.network.MessageAbsorptionHopper;
import mob_grinding_utils.tile.TileEntityAbsorptionHopper;
import mob_grinding_utils.tile.TileEntityAbsorptionHopper.EnumStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.Button;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public class GuiAbsorptionHopper extends AbstractContainerScreen<ContainerAbsorptionHopper> {

	private static final ResourceLocation GUI_ABSORPTION_HOPPER = new ResourceLocation("mob_grinding_utils:textures/gui/absorption_hopper_gui.png");
	protected final ContainerAbsorptionHopper container;
	private final TileEntityAbsorptionHopper tile;
	Font fontRenderer = Minecraft.getInstance().font;

	public GuiAbsorptionHopper(ContainerAbsorptionHopper container, Inventory playerInventory, Component name) {
		super(container, playerInventory, name);
		this.container = container;
		this.tile = this.container.hopper;
		imageHeight = 226;
		imageWidth = 248;
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
				MobGrindingUtils.NETWORK_WRAPPER.sendToServer(new MessageAbsorptionHopper(inventory.player, ((GuiMGUButton)button).id, tile.getBlockPos()));
			}
		};

		addButton(new GuiMGUButton(xOffSet + 7, yOffSet + 17, GuiMGUButton.Size.MEDIUM, 0, new TextComponent("Down"), message));
		addButton(new GuiMGUButton(xOffSet + 7, yOffSet + 34, GuiMGUButton.Size.MEDIUM, 1, new TextComponent("Up"), message));
		addButton(new GuiMGUButton(xOffSet + 7, yOffSet + 51, GuiMGUButton.Size.MEDIUM, 2, new TextComponent("North"), message));
		addButton(new GuiMGUButton(xOffSet + 82, yOffSet + 17, GuiMGUButton.Size.MEDIUM, 3, new TextComponent("South"), message));
		addButton(new GuiMGUButton(xOffSet + 82, yOffSet + 34, GuiMGUButton.Size.MEDIUM, 4, new TextComponent("West"), message));
		addButton(new GuiMGUButton(xOffSet + 82, yOffSet + 51, GuiMGUButton.Size.MEDIUM, 5, new TextComponent("East"), message));

		addButton(new GuiMGUButton(xOffSet + 173, yOffSet + 113, GuiMGUButton.Size.LARGE, 6, TextComponent.EMPTY, (button) -> {
			MobGrindingUtils.NETWORK_WRAPPER.sendToServer(new MessageAbsorptionHopper(inventory.player, 6, tile.getBlockPos()));
			tile.showRenderBox = !tile.showRenderBox;
		}));

		addButton(new GuiMGUButton(xOffSet + 173, yOffSet + 25, GuiMGUButton.Size.SMALL, 7, new TextComponent("-"), message));
		addButton(new GuiMGUButton(xOffSet + 225, yOffSet + 25, GuiMGUButton.Size.SMALL, 8, new TextComponent("+"), message));
		addButton(new GuiMGUButton(xOffSet + 173, yOffSet + 59, GuiMGUButton.Size.SMALL, 9, new TextComponent("-"), message));
		addButton(new GuiMGUButton(xOffSet + 225, yOffSet + 59, GuiMGUButton.Size.SMALL, 10, new TextComponent("+"), message));
		addButton(new GuiMGUButton(xOffSet + 173, yOffSet + 93, GuiMGUButton.Size.SMALL, 11, new TextComponent("-"), message));
		addButton(new GuiMGUButton(xOffSet + 225, yOffSet + 93, GuiMGUButton.Size.SMALL, 12, new TextComponent("+"), message));
	}

	@Override
	public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(stack);
		super.render(stack, mouseX, mouseY, partialTicks);
		renderTooltip(stack, mouseX, mouseY);
	}

	@Override
	protected void renderLabels(PoseStack stack, int mouseX, int mouseY) {
		fontRenderer.draw(stack, new TranslatableComponent("block.mob_grinding_utils.absorption_hopper").getString(), 8, imageHeight - 220, 4210752);
		
		fontRenderer.draw(stack, new TranslatableComponent("block.mob_grinding_utils.absorption_hopper_d_u").getString(), 174, imageHeight - 212, 4210752);
		
		fontRenderer.draw(stack, new TranslatableComponent("block.mob_grinding_utils.absorption_hopper_n_s").getString(), 174, imageHeight - 178, 4210752);
		fontRenderer.draw(stack, new TranslatableComponent("block.mob_grinding_utils.absorption_hopper_w_e").getString(), 174, imageHeight - 144, 4210752);
	
		fontRenderer.drawShadow(stack, !tile.showRenderBox ? "Show Area" : "Hide Area", imageWidth - 41 - fontRenderer.width(!tile.showRenderBox ? "Show Area" : "Hide Area") / 2, imageHeight - 109, 14737632);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		getMinecraft().getTextureManager().bind(GUI_ABSORPTION_HOPPER);
		int xOffSet = (width - imageWidth) / 2;
		int yOffSet = (height - imageHeight) / 2;
		int zLevel = 0; /// this may need increasing depending on layers
		this.blit(stack, xOffSet, yOffSet, 0, 0, imageWidth, imageHeight);

		EnumStatus DOWN = tile.getSideStatus(Direction.DOWN);
		EnumStatus UP = tile.getSideStatus(Direction.UP);
		EnumStatus NORTH = tile.getSideStatus(Direction.NORTH);
		EnumStatus SOUTH = tile.getSideStatus(Direction.SOUTH);
		EnumStatus WEST = tile.getSideStatus(Direction.WEST);
		EnumStatus EAST = tile.getSideStatus(Direction.EAST);
		String OFFSETX = String.valueOf(tile.getoffsetX());
		String OFFSETY = String.valueOf(tile.getoffsetY());
		String OFFSETZ = String.valueOf(tile.getoffsetZ());

		fontRenderer.draw(stack, I18n.get(DOWN.getSerializedName()), xOffSet + 58 - fontRenderer.width(I18n.get(DOWN.getSerializedName())) / 2, yOffSet + 21, getModeColour(DOWN.ordinal()));
		fontRenderer.draw(stack, I18n.get(UP.getSerializedName()), xOffSet + 58 - fontRenderer.width(I18n.get(UP.getSerializedName())) / 2, yOffSet + 38, getModeColour(UP.ordinal()));
		fontRenderer.draw(stack, I18n.get(NORTH.getSerializedName()), xOffSet + 58 - fontRenderer.width(I18n.get(NORTH.getSerializedName())) / 2, yOffSet + 55, getModeColour(NORTH.ordinal()));
		fontRenderer.draw(stack, I18n.get(SOUTH.getSerializedName()), xOffSet + 133 - fontRenderer.width(I18n.get(SOUTH.getSerializedName())) / 2, yOffSet + 21, getModeColour(SOUTH.ordinal()));
		fontRenderer.draw(stack, I18n.get(WEST.getSerializedName()), xOffSet + 133 - fontRenderer.width(I18n.get(WEST.getSerializedName())) / 2, yOffSet + 38, getModeColour(WEST.ordinal()));
		fontRenderer.draw(stack, I18n.get(EAST.getSerializedName()), xOffSet + 133 - fontRenderer.width(I18n.get(EAST.getSerializedName())) / 2, yOffSet + 55, getModeColour(EAST.ordinal()));
		fontRenderer.draw(stack, I18n.get(OFFSETY), xOffSet + 207 - fontRenderer.width(I18n.get(OFFSETY)) / 2, yOffSet + 29, 5285857);//NS
		fontRenderer.draw(stack, I18n.get(OFFSETZ), xOffSet + 207 - fontRenderer.width(I18n.get(OFFSETZ)) / 2, yOffSet + 63, 5285857);//WE
		fontRenderer.draw(stack, I18n.get(OFFSETX), xOffSet + 207 - fontRenderer.width(I18n.get(OFFSETX)) / 2, yOffSet + 97, 5285857);//DU

		int fluid = tile.getScaledFluid(120);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		if (fluid >= 1) {
			TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(tile.tank.getFluid().getFluid().getAttributes().getStillTexture());
			Tesselator tessellator = Tesselator.getInstance();
			BufferBuilder buffer = tessellator.getBuilder();
			Minecraft.getInstance().textureManager.bind(InventoryMenu.BLOCK_ATLAS); // dunno if needed now
			buffer.begin(GL11.GL_QUADS, DefaultVertexFormat.POSITION_TEX);
			buffer.vertex(xOffSet + 156, yOffSet + 128, zLevel).uv(sprite.getU0(), sprite.getV0()).endVertex();
			buffer.vertex(xOffSet + 168, yOffSet + 128, zLevel).uv(sprite.getU1(), sprite.getV0()).endVertex();
			buffer.vertex(xOffSet + 168, yOffSet + 128 - fluid, zLevel).uv(sprite.getU1(), sprite.getV1()).endVertex();
			buffer.vertex(xOffSet + 156, yOffSet + 128 - fluid, zLevel).uv(sprite.getU0(), sprite.getV1()).endVertex();
			tessellator.end();
		}

		getMinecraft().getTextureManager().bind(GUI_ABSORPTION_HOPPER);
		this.blit(stack, xOffSet + 153, yOffSet + 8 , 248, 0, 6, 120);
	}

	public int getModeColour(int index) {
		switch (index) {
		case 0:
			return 16711680;
		case 1:
			return 5285857;
		case 2:
			return 16776960;
		default:
			return 16776960;
		}
	}
}