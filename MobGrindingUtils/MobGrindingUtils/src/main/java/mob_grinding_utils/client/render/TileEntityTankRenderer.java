package mob_grinding_utils.client.render;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.sun.prism.TextureMap;

import mob_grinding_utils.tile.TileEntityTank;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;

@OnlyIn(Dist.CLIENT)
public class TileEntityTankRenderer extends TileEntityRenderer<TileEntityTank> {

	public TileEntityTankRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}

	@Override
	public void render(TileEntityTank tile, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int combinedLight, int combinedOverlay) {
		float fluidLevel = tile.tank.getFluidAmount();
		if (fluidLevel < 1)
			return;
		FluidStack fluidStack = new FluidStack(tile.tank.getFluid(), 100);
		float height = (0.96875F / tile.tank.getCapacity()) * tile.tank.getFluidAmount();
		
		TextureAtlasSprite fluidStillSprite = Minecraft.getMinecraftGame().getTextureMapBlocks().getAtlasSprite(fluidStack.getFluid().getStill().toString());
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		int fluidColor = fluidStack.getFluid().getColor(fluidStack);
		
		GlStateManager.disableLighting();
		matrixStack.push();
		matrixStack.translate(tile.getPos().getX() + 0.5D, tile.getPos().getY() + 0.5D, tile.getPos().getZ() + 0.5D);
		Minecraft.getInstance().textureManager.bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE); // dunno if needed now

		setGLColorFromInt(fluidColor);
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		float xMax, zMax, xMin, zMin, yMin = 0;
		xMax = 1.984375F;
		zMax = 1.984375F;
		xMin = 0.015625F;
		zMin = 0.015625F;
		yMin = 0.015625F;

		renderCuboid(buffer, xMax, xMin, yMin, height, zMin, zMax, fluidStillSprite);
		tessellator.draw();
		matrixStack.pop();
		GlStateManager.enableLighting();
	}

	private void setGLColorFromInt(int color) {
		float red = (color >> 16 & 0xFF) / 255.0F;
		float green = (color >> 8 & 0xFF) / 255.0F;
		float blue = (color & 0xFF) / 255.0F;

		GlStateManager.color4f(red, green, blue, 1.0F);
	}

	private void renderCuboid(BufferBuilder buffer, float xMax, float xMin, float yMin, float height, float zMin, float zMax, TextureAtlasSprite textureAtlasSprite) {

		float uMin = textureAtlasSprite.getMinU();
		float uMax = textureAtlasSprite.getMaxU();
		float vMin = textureAtlasSprite.getMinV();
		float vMax = textureAtlasSprite.getMaxV();

		float vHeight = vMax - vMin;

		// top
		addVertexWithUV(buffer, xMax, height, zMax, uMax, vMin);
		addVertexWithUV(buffer, xMax, height, zMin, uMin, vMin);
		addVertexWithUV(buffer, xMin, height, zMin, uMin, vMax);
		addVertexWithUV(buffer, xMin, height, zMax, uMax, vMax);

		// north
		addVertexWithUV(buffer, xMax, yMin, zMin, uMax, vMin);
		addVertexWithUV(buffer, xMin, yMin, zMin, uMin, vMin);
		addVertexWithUV(buffer, xMin, height, zMin, uMin, vMin + (vHeight * height));
		addVertexWithUV(buffer, xMax, height, zMin, uMax, vMin + (vHeight * height));

		// south
		addVertexWithUV(buffer, xMax, yMin, zMax, uMin, vMin);
		addVertexWithUV(buffer, xMax, height, zMax, uMin, vMin + (vHeight * height));
		addVertexWithUV(buffer, xMin, height, zMax, uMax, vMin + (vHeight * height));
		addVertexWithUV(buffer, xMin, yMin, zMax, uMax, vMin);

		// east
		addVertexWithUV(buffer, xMax, yMin, zMin, uMin, vMin);
		addVertexWithUV(buffer, xMax, height, zMin, uMin, vMin + (vHeight * height));
		addVertexWithUV(buffer, xMax, height, zMax, uMax, vMin + (vHeight * height));
		addVertexWithUV(buffer, xMax, yMin, zMax, uMax, vMin);

		// west
		addVertexWithUV(buffer, xMin, yMin, zMax, uMin, vMin);
		addVertexWithUV(buffer, xMin, height, zMax, uMin, vMin + (vHeight * height));
		addVertexWithUV(buffer, xMin, height, zMin, uMax, vMin + (vHeight * height));
		addVertexWithUV(buffer, xMin, yMin, zMin, uMax, vMin);

		// down
		addVertexWithUV(buffer, xMax, yMin, zMin, uMax, vMin);
		addVertexWithUV(buffer, xMax, yMin, zMax, uMin, vMin);
		addVertexWithUV(buffer, xMin, yMin, zMax, uMin, vMax);
		addVertexWithUV(buffer, xMin, yMin, zMin, uMax, vMax);

	}

	private void addVertexWithUV(BufferBuilder buffer, float x, float y, float z, float u, float v) {
		buffer.pos(x / 2f, y, z / 2f).tex(u, v).endVertex();
	}

	private void addVertexWithColor(BufferBuilder buffer, float x, float y, float z, float red, float green, float blue, float alpha) {
		buffer.pos(x / 2f, y, z / 2f).color(red, green, blue, alpha).endVertex();
	}
}