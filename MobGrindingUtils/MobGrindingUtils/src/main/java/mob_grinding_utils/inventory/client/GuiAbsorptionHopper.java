package mob_grinding_utils.inventory.client;

import org.lwjgl.opengl.GL11;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.inventory.server.ContainerAbsorptionHopper;
import mob_grinding_utils.network.AbsorptionHopperMessage;
import mob_grinding_utils.tile.TileEntityAbsorptionHopper;
import mob_grinding_utils.tile.TileEntityAbsorptionHopper.EnumStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiAbsorptionHopper extends GuiContainer {

	private static final ResourceLocation GUI_ABSORPTION_HOPPER = new ResourceLocation("mob_grinding_utils:textures/gui/absorption_hopper_gui.png");
	private final TileEntityAbsorptionHopper tile;

	public GuiAbsorptionHopper(EntityPlayer player, TileEntityAbsorptionHopper tile) {
		super(new ContainerAbsorptionHopper(player, tile));
		this.tile = tile;
		ySize = 224;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void initGui() {
		super.initGui();
		buttonList.clear();
		int xOffSet = (width - xSize) / 2;
		int yOffSet = (height - ySize) / 2;
		buttonList.add(new GuiLargeButton(0, xOffSet + 7, yOffSet + 17, 192, 0, "Down"));
		buttonList.add(new GuiLargeButton(1, xOffSet + 7, yOffSet + 34, 192, 0, "Up"));
		buttonList.add(new GuiLargeButton(2, xOffSet + 7, yOffSet + 51, 192, 0, "North"));
		buttonList.add(new GuiLargeButton(3, xOffSet + 82, yOffSet + 17, 192, 0, "South"));
		buttonList.add(new GuiLargeButton(4, xOffSet + 82, yOffSet + 34, 192, 0, "West"));
		buttonList.add(new GuiLargeButton(5, xOffSet + 82, yOffSet + 51, 192, 0, "East"));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		fontRendererObj.drawString(I18n.format("Absorption Hopper"), xSize / 2 - fontRendererObj.getStringWidth(I18n.format("Absorption Hopper")) / 2, ySize - 218, 4210752);	
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTickTime, int x, int y) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
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

		fontRendererObj.drawString(I18n.format(DOWN.getName()), xOffSet + 58 - fontRendererObj.getStringWidth(I18n.format(DOWN.getName())) / 2, yOffSet + 21, getModeColour(DOWN.ordinal()));
		fontRendererObj.drawString(I18n.format(UP.getName()), xOffSet + 58 - fontRendererObj.getStringWidth(I18n.format(UP.getName())) / 2, yOffSet + 38, getModeColour(UP.ordinal()));
		fontRendererObj.drawString(I18n.format(NORTH.getName()), xOffSet + 58 - fontRendererObj.getStringWidth(I18n.format(NORTH.getName())) / 2, yOffSet + 55, getModeColour(NORTH.ordinal()));
		fontRendererObj.drawString(I18n.format(SOUTH.getName()), xOffSet + 133 - fontRendererObj.getStringWidth(I18n.format(SOUTH.getName())) / 2, yOffSet + 21, getModeColour(SOUTH.ordinal()));
		fontRendererObj.drawString(I18n.format(WEST.getName()), xOffSet + 133 - fontRendererObj.getStringWidth(I18n.format(WEST.getName())) / 2, yOffSet + 38, getModeColour(WEST.ordinal()));
		fontRendererObj.drawString(I18n.format(EAST.getName()), xOffSet + 133 - fontRendererObj.getStringWidth(I18n.format(EAST.getName())) / 2, yOffSet + 55, getModeColour(EAST.ordinal()));

		int fluid = tile.getScaledFluid(119);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		if (fluid >= 1) {
			TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(tile.tank.getFluid().getFluid().getStill().toString());
			Tessellator tessellator = Tessellator.getInstance();
			VertexBuffer buffer = tessellator.getBuffer();
			mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			buffer.pos(xOffSet + 156, yOffSet + 128, zLevel).tex(sprite.getMinU(), sprite.getMinV()).endVertex();
			buffer.pos(xOffSet + 168, yOffSet + 128, zLevel).tex(sprite.getMaxU(), sprite.getMinV()).endVertex();
			buffer.pos(xOffSet + 168, yOffSet + 128 - fluid, zLevel).tex(sprite.getMaxU(), sprite.getMaxV()).endVertex();
			buffer.pos(xOffSet + 156, yOffSet + 128 - fluid, zLevel).tex(sprite.getMinU(), sprite.getMaxV()).endVertex();
			tessellator.draw();
		}

		mc.getTextureManager().bindTexture(GUI_ABSORPTION_HOPPER);
		drawTexturedModalRect(xOffSet + 155, yOffSet + 8 , 178, 0, 12, 121);
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
			MobGrindingUtils.NETWORK_WRAPPER.sendToServer(new AbsorptionHopperMessage(mc.thePlayer, guibutton.id, tile.getPos()));
	}

}