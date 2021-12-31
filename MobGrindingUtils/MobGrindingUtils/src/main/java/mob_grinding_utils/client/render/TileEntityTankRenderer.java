package mob_grinding_utils.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import mob_grinding_utils.client.ModelLayers;
import mob_grinding_utils.models.ModelTankBlock;
import mob_grinding_utils.tile.TileEntityJumboTank;
import mob_grinding_utils.tile.TileEntitySinkTank;
import mob_grinding_utils.tile.TileEntityTank;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;

@OnlyIn(Dist.CLIENT)
public class TileEntityTankRenderer implements BlockEntityRenderer<TileEntityTank> {
	private static final ResourceLocation TANK_TEXTURE = new ResourceLocation("mob_grinding_utils:textures/tiles/tank.png");
	private static final ResourceLocation TANK_SINK_TEXTURE = new ResourceLocation("mob_grinding_utils:textures/tiles/tank_sink.png");
	private static final ResourceLocation TANK_JUMBO_TEXTURE = new ResourceLocation("mob_grinding_utils:textures/tiles/tank_jumbo.png");
	private final ModelTankBlock tank_model;
	
	public TileEntityTankRenderer(Context context) {
		tank_model = new ModelTankBlock(context.bakeLayer(ModelLayers.TANK));
	}

	@Override
	public void render(TileEntityTank tile, float partialTicks, PoseStack matrixStack, MultiBufferSource bufferIn, int combinedLight, int combinedOverlay) {
		matrixStack.pushPose();
		matrixStack.translate(0.5D, 1.5D, 0.5D);
		matrixStack.scale(-0.9999F, -0.9999F, 0.9999F); //don't want to cull, but also don't want z-fighty nonsense
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		tank_model.renderToBuffer(matrixStack, bufferIn.getBuffer(RenderType.entitySmoothCutout(getTexture(tile))), combinedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1.0F);
		RenderSystem.disableBlend();
	    RenderSystem.defaultBlendFunc();
		matrixStack.popPose();

		if (tile.tank.getFluid().isEmpty())
			return;
		float fluidLevel = tile.tank.getFluidAmount();
		if (fluidLevel < 1)
			return;
		FluidStack fluidStack = new FluidStack(tile.tank.getFluid(), 100);
		float height = (0.96875F / tile.tank.getCapacity()) * tile.tank.getFluidAmount();
		
		TextureAtlasSprite fluidStillSprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidStack.getFluid().getAttributes().getStillTexture());
		VertexConsumer buffer = bufferIn.getBuffer(RenderType.translucent());
		int fluidColor = fluidStack.getFluid().getAttributes().getColor();
		matrixStack.pushPose();
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
		matrixStack.popPose();

	}

	private ResourceLocation getTexture(TileEntityTank tile) {
		return tile instanceof TileEntityJumboTank ? TANK_JUMBO_TEXTURE : tile instanceof TileEntitySinkTank ? TANK_SINK_TEXTURE : TANK_TEXTURE;
	}

	private void renderCuboid(VertexConsumer buffer, PoseStack matrixStack, float xMax, float xMin, float yMin, float height, float zMin, float zMax, TextureAtlasSprite textureAtlasSprite, float red, float green, float blue, float alpha, int combinedLight) {

		float uMin = textureAtlasSprite.getU0();
		float uMax = textureAtlasSprite.getU1();
		float vMin = textureAtlasSprite.getV0();
		float vMax = textureAtlasSprite.getV1();

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

	private void addVertexWithUV(VertexConsumer buffer, PoseStack matrixStack, float x, float y, float z, float u, float v, float red, float green, float blue, float alpha, int combinedLight) {
		buffer.vertex(matrixStack.last().pose(), x / 2f, y, z / 2f).color(red, green, blue, alpha).uv(u, v).uv2(combinedLight, 240).normal(1, 0, 0).endVertex();
	}

}