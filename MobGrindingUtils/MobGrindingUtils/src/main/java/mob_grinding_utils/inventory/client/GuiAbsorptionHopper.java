package mob_grinding_utils.inventory.client;

import org.lwjgl.opengl.GL11;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.inventory.server.ContainerAbsorptionHopper;
import mob_grinding_utils.network.MessageAbsorptionHopper;
import mob_grinding_utils.tile.TileEntityAbsorptionHopper;
import mob_grinding_utils.tile.TileEntityAbsorptionHopper.EnumStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiAbsorptionHopper extends GuiContainer {

	private static final ResourceLocation GUI_ABSORPTION_HOPPER = new ResourceLocation("mob_grinding_utils:textures/gui/absorption_hopper_gui.png");
	private final TileEntityAbsorptionHopper tile;

	public GuiAbsorptionHopper(EntityPlayer player, TileEntityAbsorptionHopper tile) {
		super(new ContainerAbsorptionHopper(player, tile));
		this.tile = tile;
		ySize = 226;
		xSize = 248;
	}

	@Override
	public void initGui() {
		super.initGui();
		buttonList.clear();
		int xOffSet = (width - xSize) / 2;
		int yOffSet = (height - ySize) / 2;
		buttonList.add(new GuiMediumButton(0, xOffSet + 7, yOffSet + 17, 0, 228, "Down"));
		buttonList.add(new GuiMediumButton(1, xOffSet + 7, yOffSet + 34, 0, 228, "Up"));
		buttonList.add(new GuiMediumButton(2, xOffSet + 7, yOffSet + 51, 0, 228, "North"));
		buttonList.add(new GuiMediumButton(3, xOffSet + 82, yOffSet + 17, 0, 228, "South"));
		buttonList.add(new GuiMediumButton(4, xOffSet + 82, yOffSet + 34, 0, 228, "West"));
		buttonList.add(new GuiMediumButton(5, xOffSet + 82, yOffSet + 51, 0, 228, "East"));
		buttonList.add(new GuiBigButton(6, xOffSet + 173, yOffSet + 113, 33, 228, ""));
		
		buttonList.add(new GuiSmallButton(7, xOffSet + 173, yOffSet + 25, 103, 228, "-"));//D
		buttonList.add(new GuiSmallButton(8, xOffSet + 225, yOffSet + 25, 103, 228, "+"));//U
		
		buttonList.add(new GuiSmallButton(9, xOffSet + 173, yOffSet + 59, 103, 228, "-"));//N
		buttonList.add(new GuiSmallButton(10, xOffSet + 225, yOffSet + 59, 103, 228, "+"));//S
		
		buttonList.add(new GuiSmallButton(11, xOffSet + 173, yOffSet + 93, 103, 228, "-"));//W
		buttonList.add(new GuiSmallButton(12, xOffSet + 225, yOffSet + 93, 103, 228, "+"));//E
	}

	@Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		int xOffSet = (width - xSize) / 2;
		int yOffSet = (height - ySize) / 2;
		fontRenderer.drawString(I18n.format(new TextComponentTranslation("tile.mob_grinding_utils.absorption_hopper.name").getFormattedText()), 8, ySize - 220, 4210752);
		
		fontRenderer.drawString(I18n.format(new TextComponentTranslation("tile.mob_grinding_utils.absorption_hopper_d_u.name").getFormattedText()), 174, ySize - 212, 4210752);
		
		fontRenderer.drawString(I18n.format(new TextComponentTranslation("tile.mob_grinding_utils.absorption_hopper_n_s.name").getFormattedText()), 174, ySize - 178, 4210752);
		fontRenderer.drawString(I18n.format(new TextComponentTranslation("tile.mob_grinding_utils.absorption_hopper_w_e.name").getFormattedText()), 174, ySize - 144, 4210752);
	
		fontRenderer.drawStringWithShadow(I18n.format(!tile.showRenderBox ? "Show Area" : "Hide Area"), xSize - 41 - fontRenderer.getStringWidth(I18n.format(!tile.showRenderBox ? "Show Area" : "Hide Area")) / 2, ySize - 109, 14737632);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTickTime, int x, int y) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(GUI_ABSORPTION_HOPPER);
		int xOffSet = (width - xSize) / 2;
		int yOffSet = (height - ySize) / 2;
		drawTexturedModalRect(xOffSet, yOffSet, 0, 0, xSize, ySize);

		EnumStatus DOWN = tile.getSideStatus(EnumFacing.DOWN);
		EnumStatus UP = tile.getSideStatus(EnumFacing.UP);
		EnumStatus NORTH = tile.getSideStatus(EnumFacing.NORTH);
		EnumStatus SOUTH = tile.getSideStatus(EnumFacing.SOUTH);
		EnumStatus WEST = tile.getSideStatus(EnumFacing.WEST);
		EnumStatus EAST = tile.getSideStatus(EnumFacing.EAST);
		String OFFSETX = String.valueOf(tile.getoffsetX());
		String OFFSETY = String.valueOf(tile.getoffsetY());
		String OFFSETZ = String.valueOf(tile.getoffsetZ());

		fontRenderer.drawString(I18n.format(DOWN.getName()), xOffSet + 58 - fontRenderer.getStringWidth(I18n.format(DOWN.getName())) / 2, yOffSet + 21, getModeColour(DOWN.ordinal()));
		fontRenderer.drawString(I18n.format(UP.getName()), xOffSet + 58 - fontRenderer.getStringWidth(I18n.format(UP.getName())) / 2, yOffSet + 38, getModeColour(UP.ordinal()));
		fontRenderer.drawString(I18n.format(NORTH.getName()), xOffSet + 58 - fontRenderer.getStringWidth(I18n.format(NORTH.getName())) / 2, yOffSet + 55, getModeColour(NORTH.ordinal()));
		fontRenderer.drawString(I18n.format(SOUTH.getName()), xOffSet + 133 - fontRenderer.getStringWidth(I18n.format(SOUTH.getName())) / 2, yOffSet + 21, getModeColour(SOUTH.ordinal()));
		fontRenderer.drawString(I18n.format(WEST.getName()), xOffSet + 133 - fontRenderer.getStringWidth(I18n.format(WEST.getName())) / 2, yOffSet + 38, getModeColour(WEST.ordinal()));
		fontRenderer.drawString(I18n.format(EAST.getName()), xOffSet + 133 - fontRenderer.getStringWidth(I18n.format(EAST.getName())) / 2, yOffSet + 55, getModeColour(EAST.ordinal()));
		fontRenderer.drawString(I18n.format(OFFSETY), xOffSet + 207 - fontRenderer.getStringWidth(I18n.format(OFFSETY)) / 2, yOffSet + 29, 5285857);//NS
		fontRenderer.drawString(I18n.format(OFFSETZ), xOffSet + 207 - fontRenderer.getStringWidth(I18n.format(OFFSETZ)) / 2, yOffSet + 63, 5285857);//WE
		fontRenderer.drawString(I18n.format(OFFSETX), xOffSet + 207 - fontRenderer.getStringWidth(I18n.format(OFFSETX)) / 2, yOffSet + 97, 5285857);//DU

		int fluid = tile.getScaledFluid(120);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		if (fluid >= 1) {
			TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(tile.tank.getFluid().getFluid().getStill().toString());
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder buffer = tessellator.getBuffer();
			mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			buffer.pos(xOffSet + 156, yOffSet + 128, zLevel).tex(sprite.getMinU(), sprite.getMinV()).endVertex();
			buffer.pos(xOffSet + 168, yOffSet + 128, zLevel).tex(sprite.getMaxU(), sprite.getMinV()).endVertex();
			buffer.pos(xOffSet + 168, yOffSet + 128 - fluid, zLevel).tex(sprite.getMaxU(), sprite.getMaxV()).endVertex();
			buffer.pos(xOffSet + 156, yOffSet + 128 - fluid, zLevel).tex(sprite.getMinU(), sprite.getMaxV()).endVertex();
			tessellator.draw();
		}

		mc.getTextureManager().bindTexture(GUI_ABSORPTION_HOPPER);
		drawTexturedModalRect(xOffSet + 153, yOffSet + 8 , 248, 0, 6, 120);
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

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (guibutton instanceof GuiButton)
			MobGrindingUtils.NETWORK_WRAPPER.sendToServer(new MessageAbsorptionHopper(mc.player, guibutton.id, tile.getPos()));
	}

}