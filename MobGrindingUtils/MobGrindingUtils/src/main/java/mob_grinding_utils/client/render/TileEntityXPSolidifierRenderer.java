package mob_grinding_utils.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.blocks.BlockXPSolidifier;
import mob_grinding_utils.models.ModelXPSolidifier;
import mob_grinding_utils.tile.TileEntityXPSolidifier;
import mob_grinding_utils.tile.TileEntityXPSolidifier.OutputDirection;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;

@OnlyIn(Dist.CLIENT)
public class TileEntityXPSolidifierRenderer extends TileEntityRenderer<TileEntityXPSolidifier> {
	private static final ResourceLocation TEXTURE = new ResourceLocation("mob_grinding_utils:textures/tiles/xp_solidifier.png");
	private static final ResourceLocation TEXTURE_NO_PUSH = new ResourceLocation("mob_grinding_utils:textures/tiles/xp_solidifier_no_push.png");
	private final ModelXPSolidifier xp_solidifier_model = new ModelXPSolidifier();
	
	public TileEntityXPSolidifierRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}

	@Override
	public void render(TileEntityXPSolidifier tile, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int combinedLight, int combinedOverlay) {
		if(tile == null || !tile.hasWorld())
			return;

		BlockState state = tile.getWorld().getBlockState(tile.getPos());

		if(state == null || state.getBlock() != ModBlocks.XPSOLIDIFIER)
			return;

		Direction facing = state.get(BlockXPSolidifier.FACING);

		float ticks = tile.prevAnimationTicks + (tile.animationTicks - tile.prevAnimationTicks)  * partialTicks;
		
		matrixStack.push();
		matrixStack.translate(0.5D, 1.5D, 0.5D);
		matrixStack.scale(-0.9999F, -0.9999F, 0.9999F);

		switch (tile.outputDirection) {
		case NONE:
		case NORTH:
			matrixStack.rotate(Vector3f.YP.rotationDegrees(90F));
			break;
		case SOUTH:
			matrixStack.rotate(Vector3f.YN.rotationDegrees(90F));
			break;
		case WEST:
			matrixStack.rotate(Vector3f.YP.rotationDegrees(0F));
			break;
		case EAST:
			matrixStack.rotate(Vector3f.YN.rotationDegrees(180F));
			break;
		default:
			matrixStack.rotate(Vector3f.YP.rotationDegrees(90F));
			break;
		}

		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		xp_solidifier_model.renderExport(matrixStack, bufferIn.getBuffer(RenderType.getEntitySmoothCutout(tile.outputDirection == OutputDirection.NONE ? TEXTURE_NO_PUSH : TEXTURE)), combinedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1.0F);
		RenderSystem.disableBlend();
	    RenderSystem.defaultBlendFunc();
		matrixStack.pop();

		matrixStack.push();
		matrixStack.translate(0.5D, 1.5D, 0.5D);
		matrixStack.scale(-0.9999F, -0.9999F, 0.9999F); //don't want to cull, but also don't want z-fighty nonsense

		switch (facing) {
		case NORTH:
			matrixStack.rotate(Vector3f.YP.rotationDegrees(0F));
			break;
		case SOUTH:
			matrixStack.rotate(Vector3f.YP.rotationDegrees(180F));
			break;
		case WEST:
			matrixStack.rotate(Vector3f.YN.rotationDegrees(90F));
			break;
		case EAST:
			matrixStack.rotate(Vector3f.YP.rotationDegrees(90F));
			break;
		default:
			break;
		}
		
		matrixStack.push();
		RenderSystem.enableBlend();
		
		if(ticks > 0 && ticks <= 20F)
			matrixStack.translate(0D, ticks * 0.009375F, 0D);
		if(ticks > 20F && ticks <= 60)
			matrixStack.translate(0D, 0.1875F, 0D);
		if(ticks > 60F && ticks <= 80F)
			matrixStack.translate(0D, (80F - ticks)  * 0.009375F, 0D);
		if(ticks > 80F || ticks <= 0)
			matrixStack.translate(0D, 0D, 0D);
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		xp_solidifier_model.renderRack(matrixStack, bufferIn.getBuffer(RenderType.getEntitySmoothCutout(TEXTURE)), combinedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1.0F);
		
		matrixStack.push();
		matrixStack.translate(0D, 0.60625D, -0.22D);
		matrixStack.rotate(Vector3f.XP.rotationDegrees(90.0F));
		matrixStack.scale(1.25F, 1.25F, 1.25F);
		ItemStack stackMould = tile.inputSlots.getStackInSlot(0);
		if (!stackMould.isEmpty()) {
			Minecraft.getInstance().getTextureManager().bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
			Minecraft.getInstance().getItemRenderer().renderItem(stackMould, ItemCameraTransforms.TransformType.GROUND, false, matrixStack, bufferIn, combinedLight, combinedOverlay, Minecraft.getInstance().getItemRenderer().getItemModelWithOverrides(stackMould, null, null));
		}
		matrixStack.pop();
		
		RenderSystem.disableBlend();
	    RenderSystem.defaultBlendFunc();
	    matrixStack.pop();
	    
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		xp_solidifier_model.render(matrixStack, bufferIn.getBuffer(RenderType.getEntitySmoothCutout(TEXTURE)), combinedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1.0F);
		
		matrixStack.push();
		matrixStack.translate(0D, 0.79375D, -0.22D);
		matrixStack.rotate(Vector3f.XP.rotationDegrees(90.0F));
		matrixStack.scale(1.25F, 1.25F, 1.25F);
		ItemStack stackResult = tile.outputSlot.getStackInSlot(0);
		if (stackResult.isEmpty() && !tile.getCachedOutPutRenderStack().isEmpty() && tile.getProgress() > 60) { //may want to add some earlier blending fade here
			Minecraft.getInstance().getTextureManager().bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
			Minecraft.getInstance().getItemRenderer().renderItem(tile.getCachedOutPutRenderStack(), ItemCameraTransforms.TransformType.GROUND, false, matrixStack, bufferIn, combinedLight, combinedOverlay, Minecraft.getInstance().getItemRenderer() .getItemModelWithOverrides(tile.getCachedOutPutRenderStack(), null, null));
		} else if (!stackResult.isEmpty()) {
			Minecraft.getInstance().getTextureManager().bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
			Minecraft.getInstance().getItemRenderer().renderItem(stackResult, ItemCameraTransforms.TransformType.GROUND, false, matrixStack, bufferIn, combinedLight, combinedOverlay, Minecraft.getInstance().getItemRenderer().getItemModelWithOverrides(stackResult, null, null));
		}
		matrixStack.pop();

		RenderSystem.disableBlend();
	    RenderSystem.defaultBlendFunc();
		matrixStack.pop();

		if (tile.tank.getFluid().isEmpty())
			return;
		float fluidLevel = tile.tank.getFluidAmount();
		if (fluidLevel < 1)
			return;
		FluidStack fluidStack = new FluidStack(tile.tank.getFluid(), 100);
		float height = (0.46875F / tile.tank.getCapacity()) * tile.tank.getFluidAmount();

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

		if(ticks > 20F && ticks < 60 && !stackMould.isEmpty()) {
			matrixStack.push();
			switch (facing) {
			case NORTH:
				matrixStack.translate(0D, 0D, 0D);
				break;
			case SOUTH:
				matrixStack.translate(0D, 0D, 0.125D);
				break;
			case WEST:
				matrixStack.translate(-0.0625D, 0D, 0.0625D);
				break;
			case EAST:
				matrixStack.translate(0.0625D, 0D, 0.0625D);
				break;
			default:
				break;
			}
			xMax = 1.62F;
			zMax = 1.5F;
			xMin = 0.38F;
			zMin = 0.25F;
			yMin = 0.6875F;
			renderCuboid(buffer, matrixStack, xMax, xMin, yMin, 0.6875F + ticks * 0.000625F, zMin, zMax, fluidStillSprite, red, green, blue, alpha, combinedLight);
			matrixStack.pop();
		}

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