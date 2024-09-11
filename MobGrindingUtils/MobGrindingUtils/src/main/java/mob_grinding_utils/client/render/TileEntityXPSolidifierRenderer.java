package mob_grinding_utils.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.blocks.BlockXPSolidifier;
import mob_grinding_utils.client.ModelLayers;
import mob_grinding_utils.models.ModelXPSolidifier;
import mob_grinding_utils.tile.TileEntityXPSolidifier;
import mob_grinding_utils.tile.TileEntityXPSolidifier.OutputDirection;
import mob_grinding_utils.util.RL;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;

@OnlyIn(Dist.CLIENT)
public class TileEntityXPSolidifierRenderer implements BlockEntityRenderer<TileEntityXPSolidifier> {
	private static final ResourceLocation TEXTURE = RL.mgu("textures/tiles/xp_solidifier.png");
	private static final ResourceLocation TEXTURE_NO_PUSH = RL.mgu("textures/tiles/xp_solidifier_no_push.png");
	private final ModelXPSolidifier xp_solidifier_model;

	public TileEntityXPSolidifierRenderer(Context context) {
		xp_solidifier_model = new ModelXPSolidifier(context.bakeLayer(ModelLayers.XPSOLIDIFIER));
	}

	@Override
	public void render(TileEntityXPSolidifier tile, float partialTicks, PoseStack matrixStack, MultiBufferSource bufferIn, int combinedLight, int combinedOverlay) {
		if(tile == null || !tile.hasLevel())
			return;

		BlockState state = tile.getLevel().getBlockState(tile.getBlockPos());

		if(state == null || state.getBlock() != ModBlocks.XPSOLIDIFIER.getBlock())
			return;

		Direction facing = state.getValue(BlockXPSolidifier.FACING);

		float ticks = tile.prevAnimationTicks + (tile.animationTicks - tile.prevAnimationTicks)  * partialTicks;
		
		matrixStack.pushPose();
		matrixStack.translate(0.5D, 1.5D, 0.5D);
		matrixStack.scale(-0.9999F, -0.9999F, 0.9999F);

		switch (tile.outputDirection) {
		case NONE:
		case NORTH:
			matrixStack.mulPose(Axis.YP.rotationDegrees(90F));
			break;
		case SOUTH:
			matrixStack.mulPose(Axis.YN.rotationDegrees(90F));
			break;
		case WEST:
			matrixStack.mulPose(Axis.YP.rotationDegrees(0F));
			break;
		case EAST:
			matrixStack.mulPose(Axis.YN.rotationDegrees(180F));
			break;
		default:
			matrixStack.mulPose(Axis.YP.rotationDegrees(90F));
			break;
		}

		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		xp_solidifier_model.renderExport(matrixStack, bufferIn.getBuffer(RenderType.entitySmoothCutout(tile.outputDirection == OutputDirection.NONE ? TEXTURE_NO_PUSH : TEXTURE)), combinedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
		RenderSystem.disableBlend();
	    RenderSystem.defaultBlendFunc();
		matrixStack.popPose();

		matrixStack.pushPose();
		matrixStack.translate(0.5D, 1.5D, 0.5D);
		matrixStack.scale(-0.9999F, -0.9999F, 0.9999F); //don't want to cull, but also don't want z-fighty nonsense

		switch (facing) {
		case NORTH:
			matrixStack.mulPose(Axis.YP.rotationDegrees(0F));
			break;
		case SOUTH:
			matrixStack.mulPose(Axis.YP.rotationDegrees(180F));
			break;
		case WEST:
			matrixStack.mulPose(Axis.YN.rotationDegrees(90F));
			break;
		case EAST:
			matrixStack.mulPose(Axis.YP.rotationDegrees(90F));
			break;
		default:
			break;
		}
		
		matrixStack.pushPose();
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
		xp_solidifier_model.renderRack(matrixStack, bufferIn.getBuffer(RenderType.entitySmoothCutout(TEXTURE)), combinedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
		
		matrixStack.pushPose();
		matrixStack.translate(0D, 0.60625D, -0.22D);
		matrixStack.mulPose(Axis.XP.rotationDegrees(90.0F));
		matrixStack.scale(1.25F, 1.25F, 1.25F);
		ItemStack stackMould = tile.inputSlots.getStackInSlot(0);
		if (!stackMould.isEmpty()) {
			Minecraft.getInstance().getTextureManager().bindForSetup(InventoryMenu.BLOCK_ATLAS);
			Minecraft.getInstance().getItemRenderer().render(stackMould, ItemDisplayContext.GROUND, false, matrixStack, bufferIn, combinedLight, combinedOverlay, Minecraft.getInstance().getItemRenderer().getModel(stackMould, null, null, 0));
		}
		matrixStack.popPose();
		
		RenderSystem.disableBlend();
	    RenderSystem.defaultBlendFunc();
	    matrixStack.popPose();
	    
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		xp_solidifier_model.renderToBuffer(matrixStack, bufferIn.getBuffer(RenderType.entitySmoothCutout(TEXTURE)), combinedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
		
		matrixStack.pushPose();
		matrixStack.translate(0D, 0.79375D, -0.22D);
		matrixStack.mulPose(Axis.XP.rotationDegrees(90.0F));
		matrixStack.scale(1.25F, 1.25F, 1.25F);
		ItemStack stackResult = tile.outputSlot.getStackInSlot(0);
		if (stackResult.isEmpty() && !tile.getCachedOutPutRenderStack().isEmpty() && tile.getProgress() > 60) { //may want to add some earlier blending fade here
			Minecraft.getInstance().getTextureManager().bindForSetup(InventoryMenu.BLOCK_ATLAS);
			Minecraft.getInstance().getItemRenderer().render(tile.getCachedOutPutRenderStack(), ItemDisplayContext.GROUND, false, matrixStack, bufferIn, combinedLight, combinedOverlay, Minecraft.getInstance().getItemRenderer() .getModel(tile.getCachedOutPutRenderStack(), null, null, 0));
		} else if (!stackResult.isEmpty()) {
			Minecraft.getInstance().getTextureManager().bindForSetup(InventoryMenu.BLOCK_ATLAS);
			Minecraft.getInstance().getItemRenderer().render(stackResult, ItemDisplayContext.GROUND, false, matrixStack, bufferIn, combinedLight, combinedOverlay, Minecraft.getInstance().getItemRenderer().getModel(stackResult, null, null, 0));
		}
		matrixStack.popPose();

		RenderSystem.disableBlend();
	    RenderSystem.defaultBlendFunc();
		matrixStack.popPose();

		if (tile.tank.getFluid().isEmpty())
			return;
		float fluidLevel = tile.tank.getFluidAmount();
		if (fluidLevel < 1)
			return;
		FluidStack fluidStack = new FluidStack(tile.tank.getFluid().getFluidHolder(), 100);
		float height = (0.46875F / tile.tank.getCapacity()) * tile.tank.getFluidAmount();

		var fluidExtensions = IClientFluidTypeExtensions.of(fluidStack.getFluid());

		TextureAtlasSprite fluidStillSprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidExtensions.getStillTexture());
		VertexConsumer buffer = bufferIn.getBuffer(RenderType.translucent());
		int fluidColor = fluidExtensions.getTintColor();
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

		if(ticks > 20F && ticks < 60 && !stackMould.isEmpty()) {
			matrixStack.pushPose();
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
			matrixStack.popPose();
		}

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
		buffer.addVertex(matrixStack.last().pose(), x / 2f, y, z / 2f).setColor(red, green, blue, alpha).setUv(u, v).setUv2(combinedLight, 240).setNormal(1, 0, 0);
	}

}