package mob_grinding_utils.inventory.client;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.inventory.server.ContainerAbsorptionHopper;
import mob_grinding_utils.network.MessageAbsorptionHopper;
import mob_grinding_utils.tile.TileEntityAbsorptionHopper;
import mob_grinding_utils.tile.TileEntityAbsorptionHopper.EnumStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class GuiAbsorptionHopper extends ContainerScreen<ContainerAbsorptionHopper> {

	private static final ResourceLocation GUI_ABSORPTION_HOPPER = new ResourceLocation("mob_grinding_utils:textures/gui/absorption_hopper_gui.png");
	protected final ContainerAbsorptionHopper container;
	private final TileEntityAbsorptionHopper tile;
	FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;

	public GuiAbsorptionHopper(ContainerAbsorptionHopper container, PlayerInventory playerInventory, ITextComponent name) {
		super(container, playerInventory, name);
		this.container = container;
		this.tile = this.container.hopper;
		ySize = 226;
		xSize = 248;
	}

	@Override
	public void init() {
		super.init();
		buttons.clear();
		int xOffSet = (width - xSize) / 2;
		int yOffSet = (height - ySize) / 2;
		addButton(new GuiMediumButton(xOffSet + 7, yOffSet + 17, 0, 228, new StringTextComponent("Down"), (button) -> {
			MobGrindingUtils.NETWORK_WRAPPER.sendToServer(new MessageAbsorptionHopper(playerInventory.player, 0, tile.getPos()));
		}));

		addButton(new GuiMediumButton(xOffSet + 7, yOffSet + 34, 0, 228, new StringTextComponent("Up"), (button) -> {
			MobGrindingUtils.NETWORK_WRAPPER.sendToServer(new MessageAbsorptionHopper(playerInventory.player, 1, tile.getPos()));
		}));

		addButton(new GuiMediumButton(xOffSet + 7, yOffSet + 51, 0, 228, new StringTextComponent("North"), (button) -> {
			MobGrindingUtils.NETWORK_WRAPPER.sendToServer(new MessageAbsorptionHopper(playerInventory.player, 2, tile.getPos()));
		}));

		addButton(new GuiMediumButton(xOffSet + 82, yOffSet + 17, 0, 228, new StringTextComponent("South"), (button) -> {
			MobGrindingUtils.NETWORK_WRAPPER.sendToServer(new MessageAbsorptionHopper(playerInventory.player, 3, tile.getPos()));
		}));

		addButton(new GuiMediumButton(xOffSet + 82, yOffSet + 34, 0, 228, new StringTextComponent("West"), (button) -> {
			MobGrindingUtils.NETWORK_WRAPPER.sendToServer(new MessageAbsorptionHopper(playerInventory.player, 4, tile.getPos()));
		}));

		addButton(new GuiMediumButton(xOffSet + 82, yOffSet + 51, 0, 228, new StringTextComponent("East"), (button) -> {
			MobGrindingUtils.NETWORK_WRAPPER.sendToServer(new MessageAbsorptionHopper(playerInventory.player, 5, tile.getPos()));
		}));

		addButton(new GuiBigButton(xOffSet + 173, yOffSet + 113, 33, 228, StringTextComponent.EMPTY, (button) -> {
			MobGrindingUtils.NETWORK_WRAPPER.sendToServer(new MessageAbsorptionHopper(playerInventory.player, 6, tile.getPos()));
			tile.showRenderBox = !tile.showRenderBox;
		}));

		addButton(new GuiSmallButton(xOffSet + 173, yOffSet + 25, 103, 228, new StringTextComponent("-"), (button) -> {
			MobGrindingUtils.NETWORK_WRAPPER.sendToServer(new MessageAbsorptionHopper(playerInventory.player, 7, tile.getPos()));
		}));

		addButton(new GuiSmallButton(xOffSet + 225, yOffSet + 25, 103, 228, new StringTextComponent("+"), (button) -> {
			MobGrindingUtils.NETWORK_WRAPPER.sendToServer(new MessageAbsorptionHopper(playerInventory.player, 8, tile.getPos()));
		}));

		addButton(new GuiSmallButton(xOffSet + 173, yOffSet + 59, 103, 228, new StringTextComponent("-"), (button) -> {
			MobGrindingUtils.NETWORK_WRAPPER.sendToServer(new MessageAbsorptionHopper(playerInventory.player, 9, tile.getPos()));
		}));

		addButton(new GuiSmallButton(xOffSet + 225, yOffSet + 59, 103, 228, new StringTextComponent("+"), (button) -> {
			MobGrindingUtils.NETWORK_WRAPPER.sendToServer(new MessageAbsorptionHopper(playerInventory.player, 10, tile.getPos()));
		}));

		addButton(new GuiSmallButton(xOffSet + 173, yOffSet + 93, 103, 228, new StringTextComponent("-"), (button) -> {
			MobGrindingUtils.NETWORK_WRAPPER.sendToServer(new MessageAbsorptionHopper(playerInventory.player, 11, tile.getPos()));
		}));

		addButton(new GuiSmallButton(xOffSet + 225, yOffSet + 93, 103, 228, new StringTextComponent("+"), (button) -> {
			MobGrindingUtils.NETWORK_WRAPPER.sendToServer(new MessageAbsorptionHopper(playerInventory.player, 12, tile.getPos()));
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
		fontRenderer.drawString(stack, new TranslationTextComponent("block.mob_grinding_utils.absorption_hopper").getString(), 8, ySize - 220, 4210752);
		
		fontRenderer.drawString(stack, new TranslationTextComponent("block.mob_grinding_utils.absorption_hopper_d_u").getString(), 174, ySize - 212, 4210752);
		
		fontRenderer.drawString(stack, new TranslationTextComponent("block.mob_grinding_utils.absorption_hopper_n_s").getString(), 174, ySize - 178, 4210752);
		fontRenderer.drawString(stack, new TranslationTextComponent("block.mob_grinding_utils.absorption_hopper_w_e").getString(), 174, ySize - 144, 4210752);
	
		fontRenderer.drawStringWithShadow(stack, !tile.showRenderBox ? "Show Area" : "Hide Area", xSize - 41 - fontRenderer.getStringWidth(!tile.showRenderBox ? "Show Area" : "Hide Area") / 2, ySize - 109, 14737632);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		getMinecraft().getTextureManager().bindTexture(GUI_ABSORPTION_HOPPER);
		int xOffSet = (width - xSize) / 2;
		int yOffSet = (height - ySize) / 2;
		int zLevel = 0; /// this may need increasing depending on layers
		this.blit(stack, xOffSet, yOffSet, 0, 0, xSize, ySize);

		EnumStatus DOWN = tile.getSideStatus(Direction.DOWN);
		EnumStatus UP = tile.getSideStatus(Direction.UP);
		EnumStatus NORTH = tile.getSideStatus(Direction.NORTH);
		EnumStatus SOUTH = tile.getSideStatus(Direction.SOUTH);
		EnumStatus WEST = tile.getSideStatus(Direction.WEST);
		EnumStatus EAST = tile.getSideStatus(Direction.EAST);
		String OFFSETX = String.valueOf(tile.getoffsetX());
		String OFFSETY = String.valueOf(tile.getoffsetY());
		String OFFSETZ = String.valueOf(tile.getoffsetZ());

		fontRenderer.drawString(stack, I18n.format(DOWN.getString()), xOffSet + 58 - fontRenderer.getStringWidth(I18n.format(DOWN.getString())) / 2, yOffSet + 21, getModeColour(DOWN.ordinal()));
		fontRenderer.drawString(stack, I18n.format(UP.getString()), xOffSet + 58 - fontRenderer.getStringWidth(I18n.format(UP.getString())) / 2, yOffSet + 38, getModeColour(UP.ordinal()));
		fontRenderer.drawString(stack, I18n.format(NORTH.getString()), xOffSet + 58 - fontRenderer.getStringWidth(I18n.format(NORTH.getString())) / 2, yOffSet + 55, getModeColour(NORTH.ordinal()));
		fontRenderer.drawString(stack, I18n.format(SOUTH.getString()), xOffSet + 133 - fontRenderer.getStringWidth(I18n.format(SOUTH.getString())) / 2, yOffSet + 21, getModeColour(SOUTH.ordinal()));
		fontRenderer.drawString(stack, I18n.format(WEST.getString()), xOffSet + 133 - fontRenderer.getStringWidth(I18n.format(WEST.getString())) / 2, yOffSet + 38, getModeColour(WEST.ordinal()));
		fontRenderer.drawString(stack, I18n.format(EAST.getString()), xOffSet + 133 - fontRenderer.getStringWidth(I18n.format(EAST.getString())) / 2, yOffSet + 55, getModeColour(EAST.ordinal()));
		fontRenderer.drawString(stack, I18n.format(OFFSETY), xOffSet + 207 - fontRenderer.getStringWidth(I18n.format(OFFSETY)) / 2, yOffSet + 29, 5285857);//NS
		fontRenderer.drawString(stack, I18n.format(OFFSETZ), xOffSet + 207 - fontRenderer.getStringWidth(I18n.format(OFFSETZ)) / 2, yOffSet + 63, 5285857);//WE
		fontRenderer.drawString(stack, I18n.format(OFFSETX), xOffSet + 207 - fontRenderer.getStringWidth(I18n.format(OFFSETX)) / 2, yOffSet + 97, 5285857);//DU

		int fluid = tile.getScaledFluid(120);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		if (fluid >= 1) {
			TextureAtlasSprite sprite = Minecraft.getInstance().getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(tile.tank.getFluid().getFluid().getAttributes().getStillTexture());
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder buffer = tessellator.getBuffer();
			Minecraft.getInstance().textureManager.bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE); // dunno if needed now
			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			buffer.pos(xOffSet + 156, yOffSet + 128, zLevel).tex(sprite.getMinU(), sprite.getMinV()).endVertex();
			buffer.pos(xOffSet + 168, yOffSet + 128, zLevel).tex(sprite.getMaxU(), sprite.getMinV()).endVertex();
			buffer.pos(xOffSet + 168, yOffSet + 128 - fluid, zLevel).tex(sprite.getMaxU(), sprite.getMaxV()).endVertex();
			buffer.pos(xOffSet + 156, yOffSet + 128 - fluid, zLevel).tex(sprite.getMinU(), sprite.getMaxV()).endVertex();
			tessellator.draw();
		}

		getMinecraft().getTextureManager().bindTexture(GUI_ABSORPTION_HOPPER);
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
/*
	@Override
	protected void actionPerformed(Button guibutton) {
		if (guibutton instanceof Button)
			MobGrindingUtils.NETWORK_WRAPPER.sendToServer(new MessageAbsorptionHopper(Minecraft.getInstance().player, guibutton.id, tile.getPos()));
	}
*/
}