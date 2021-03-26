package mob_grinding_utils.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import mob_grinding_utils.models.ModelXPSolidifier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
@OnlyIn(Dist.CLIENT)
public class TileXPSolidifierStackItemRenderer extends ItemStackTileEntityRenderer {
	private static final ResourceLocation TEXTURE = new ResourceLocation("mob_grinding_utils:textures/tiles/xp_solidifier.png");
	private final ModelXPSolidifier xp_solidifier_model = new ModelXPSolidifier();
	private FluidStack fluidStack;

	@Override
	public void func_239207_a_(ItemStack stack, TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int combinedLight, int combinedOverlayIn) {
		matrixStack.push();
		matrixStack.translate(0.5D, 1.5D, 0.5D);
		matrixStack.scale(-0.9999F, -0.9999F, 0.9999F);
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
	
		xp_solidifier_model.render(matrixStack, bufferIn.getBuffer(RenderType.getEntitySmoothCutout(TEXTURE)), combinedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1.0F);

		xp_solidifier_model.renderExport(matrixStack, bufferIn.getBuffer(RenderType.getEntitySmoothCutout(TEXTURE)), combinedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1.0F);

		xp_solidifier_model.renderRack(matrixStack, bufferIn.getBuffer(RenderType.getEntitySmoothCutout(TEXTURE)), combinedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1.0F);

		RenderSystem.disableBlend();
	    RenderSystem.defaultBlendFunc();
		matrixStack.pop();

		if (stack.hasTag() && !stack.getTag().contains("Empty")) {
			fluidStack = FluidStack.loadFluidStackFromNBT(stack.getTag());

		} else
			return;

		float fluidLevel = fluidStack.getAmount();
		if (fluidLevel < 1)
			return;
		float tankMax = 16000F;
		float height = (0.46875F / tankMax) * fluidLevel; // volumes hardcoded until config
		
		TextureAtlasSprite fluidStillSprite = Minecraft.getInstance().getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(fluidStack.getFluid().getAttributes().getStillTexture());
		IVertexBuilder buffer = bufferIn.getBuffer(RenderType.getTranslucent());
		int fluidColor = fluidStack.getFluid().getAttributes().getColor();
		matrixStack.push();
		matrixStack.translate(0D, 0D, 0D);
		float xMax, zMax, xMin, zMin, yMin = 0;
		xMax = 1.984375F;
		zMax = 1.984375F;
		xMin = 0.015625F;
		zMin = 0.015625F;
		yMin = 0.015625F;
		float alpha = 1F;
		float red = (fluidColor >> 16 & 0xFF) / 255.0F;
		float green = (fluidColor >> 8 & 0xFF) / 255.0F;
		float blue = (fluidColor & 0xFF) / 255.0F;
		renderCuboid(buffer, matrixStack, xMax, xMin, yMin, height, zMin, zMax, fluidStillSprite, red, green, blue, alpha, combinedLight);
		matrixStack.pop();
	}

	private void renderCuboid(IVertexBuilder buffer, MatrixStack matrixStack, float xMax, float xMin, float yMin, float height, float zMin, float zMax, TextureAtlasSprite textureAtlasSprite, float red, float green, float blue, float alpha, int combinedLight) {

		float uMin = textureAtlasSprite.getMinU();
		float uMax = textureAtlasSprite.getMaxU();
		float vMin = textureAtlasSprite.getMinV();
		float vMax = textureAtlasSprite.getMaxV();

		float vHeight = vMax - vMin;

		// top
		addVertexWithUV(buffer, matrixStack, xMax, height, zMax, uMax, vMin, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, xMax, height, zMin, uMin, vMin, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, xMin, height, zMin, uMin, vMax, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, xMin, height, zMax, uMax, vMax, red, green, blue, alpha, combinedLight);

		// north
		addVertexWithUV(buffer, matrixStack, xMax, yMin, zMin, uMax, vMin, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, xMin, yMin, zMin, uMin, vMin, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, xMin, height, zMin, uMin, vMin + (vHeight * height), red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, xMax, height, zMin, uMax, vMin + (vHeight * height), red, green, blue, alpha, combinedLight);

		// south
		addVertexWithUV(buffer, matrixStack, xMax, yMin, zMax, uMin, vMin, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, xMax, height, zMax, uMin, vMin + (vHeight * height), red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, xMin, height, zMax, uMax, vMin + (vHeight * height), red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, xMin, yMin, zMax, uMax, vMin, red, green, blue, alpha, combinedLight);

		// east
		addVertexWithUV(buffer, matrixStack, xMax, yMin, zMin, uMin, vMin, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, xMax, height, zMin, uMin, vMin + (vHeight * height), red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, xMax, height, zMax, uMax, vMin + (vHeight * height), red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, xMax, yMin, zMax, uMax, vMin, red, green, blue, alpha, combinedLight);

		// west
		addVertexWithUV(buffer, matrixStack, xMin, yMin, zMax, uMin, vMin, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, xMin, height, zMax, uMin, vMin + (vHeight * height), red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, xMin, height, zMin, uMax, vMin + (vHeight * height), red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, xMin, yMin, zMin, uMax, vMin, red, green, blue, alpha, combinedLight);

		// down
		addVertexWithUV(buffer, matrixStack, xMax, yMin, zMin, uMax, vMin, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, xMax, yMin, zMax, uMin, vMin, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, xMin, yMin, zMax, uMin, vMax, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, xMin, yMin, zMin, uMax, vMax, red, green, blue, alpha, combinedLight);
	}

	private void addVertexWithUV(IVertexBuilder buffer, MatrixStack matrixStack, float x, float y, float z, float u, float v, float red, float green, float blue, float alpha, int combinedLight) {
		buffer.pos(matrixStack.getLast().getMatrix(), x / 2f, y, z / 2f).color(red, green, blue, alpha).tex(u, v).lightmap(combinedLight, 240).normal(1, 0, 0).endVertex();
	}
}
