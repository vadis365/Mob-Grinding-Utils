package mob_grinding_utils.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mob_grinding_utils.BlockEntities.BlockEntityXPSolidifier;
import mob_grinding_utils.BlockEntities.BlockEntityXPSolidifier.OutputDirection;
import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.blocks.BlockXPSolidifier;
import mob_grinding_utils.client.ModelLayers;
import mob_grinding_utils.models.ModelXPSolidifier;
import mob_grinding_utils.util.RL;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.data.AtlasIds;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.fluid.FluidTintSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import org.jspecify.annotations.Nullable;
public class TileEntityXPSolidifierRenderer implements BlockEntityRenderer<BlockEntityXPSolidifier, TileEntityXPSolidifierRenderer.XPSolidifierRenderState> {
	private static final Identifier TEXTURE = RL.mgu("textures/tiles/xp_solidifier.png");
	private static final Identifier TEXTURE_NO_PUSH = RL.mgu("textures/tiles/xp_solidifier_no_push.png");
	private final ModelXPSolidifier xp_solidifier_model;
	private final ItemModelResolver itemModelResolver;

	public TileEntityXPSolidifierRenderer(Context context) {
		xp_solidifier_model = new ModelXPSolidifier(context.bakeLayer(ModelLayers.XPSOLIDIFIER));
		itemModelResolver = context.itemModelResolver();
	}

	@Override
	public XPSolidifierRenderState createRenderState() {
		return new XPSolidifierRenderState();
	}

	@Override
	public void extractRenderState(BlockEntityXPSolidifier tile, XPSolidifierRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(tile, state, partialTicks, cameraPosition, breakProgress);
		state.valid = tile.hasLevel() && tile.getBlockState().is(ModBlocks.XPSOLIDIFIER.getBlock());
		if (!state.valid) {
			return;
		}
		state.facing = tile.getBlockState().getValue(BlockXPSolidifier.FACING);
		state.outputDirection = tile.outputDirection;
		state.ticks = tile.prevAnimationTicks + (tile.animationTicks - tile.prevAnimationTicks) * partialTicks;
		state.progress = tile.getProgress();

		ItemStack mould = ItemUtil.getStack(tile.inputSlots, 0);
		state.mouldItem.clear();
		if (!mould.isEmpty()) {
			itemModelResolver.updateForTopItem(state.mouldItem, mould, ItemDisplayContext.GROUND, tile.getLevel(), null, 0);
		}

		ItemStack result = ItemUtil.getStack(tile.outputSlot, 0);
		state.resultItem.clear();
		ItemStack renderResult = !result.isEmpty() ? result : (!tile.getCachedOutPutRenderStack().isEmpty() && tile.getProgress() > 60 ? tile.getCachedOutPutRenderStack() : ItemStack.EMPTY);
		if (!renderResult.isEmpty()) {
			itemModelResolver.updateForTopItem(state.resultItem, renderResult, ItemDisplayContext.GROUND, tile.getLevel(), null, 1);
		}

		FluidStack fluid = FluidUtil.getStack(tile.tank, 0);
		state.hasFluid = !fluid.isEmpty() && tile.tank.getAmountAsInt(0) >= 1;
		if (state.hasFluid) {
			state.fluidHeight = (0.46875F / tile.tank.getCapacityAsInt(0, tile.tank.getResource(0))) * tile.tank.getAmountAsInt(0);
			FluidState fluidState = fluid.getFluid().defaultFluidState();
			FluidModel fluidModel = Minecraft.getInstance().getModelManager().getFluidStateModelSet().get(fluidState);
			state.fluidSprite = fluidModel.stillMaterial().sprite();
			FluidTintSource tint = fluidModel.fluidTintSource();
			state.fluidColor = tint != null ? tint.colorAsStack(fluid) : 0xFFFFFFFF;
			if (state.fluidSprite == null) {
				// AtlasManager.getAtlasOrThrow expects AtlasIds.* , not TextureAtlas.LOCATION_* texture paths
				state.fluidSprite = Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(AtlasIds.BLOCKS).getSprite(RL.mgu("block/fluid_xp"));
				state.fluidColor = 0xFFFFFFFF;
			}
			state.hasMould = !mould.isEmpty();
		} else {
			state.fluidSprite = null;
			state.hasMould = false;
		}
	}

	@Override
	public void submit(XPSolidifierRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		if (!state.valid || state.facing == null || state.outputDirection == null) {
			return;
		}

		Identifier exportTexture = state.outputDirection == OutputDirection.NONE ? TEXTURE_NO_PUSH : TEXTURE;

		poseStack.pushPose();
		poseStack.translate(0.5D, 1.5D, 0.5D);
		poseStack.scale(-0.9999F, -0.9999F, 0.9999F);
		switch (state.outputDirection) {
			case NONE, NORTH -> poseStack.mulPose(Axis.YP.rotationDegrees(90F));
			case SOUTH -> poseStack.mulPose(Axis.YN.rotationDegrees(90F));
			case WEST -> poseStack.mulPose(Axis.YP.rotationDegrees(0F));
			case EAST -> poseStack.mulPose(Axis.YN.rotationDegrees(180F));
		}
		submitNodeCollector.submitModelPart(xp_solidifier_model.top, poseStack, RenderTypes.entityCutout(exportTexture), state.lightCoords, OverlayTexture.NO_OVERLAY, null, -1, state.breakProgress);
		poseStack.popPose();

		poseStack.pushPose();
		poseStack.translate(0.5D, 1.5D, 0.5D);
		poseStack.scale(-0.9999F, -0.9999F, 0.9999F);
		switch (state.facing) {
			case NORTH -> poseStack.mulPose(Axis.YP.rotationDegrees(0F));
			case SOUTH -> poseStack.mulPose(Axis.YP.rotationDegrees(180F));
			case WEST -> poseStack.mulPose(Axis.YN.rotationDegrees(90F));
			case EAST -> poseStack.mulPose(Axis.YP.rotationDegrees(90F));
			default -> {}
		}

		poseStack.pushPose();
		float ticks = state.ticks;
		if (ticks > 0 && ticks <= 20F)
			poseStack.translate(0D, ticks * 0.009375F, 0D);
		else if (ticks > 20F && ticks <= 60)
			poseStack.translate(0D, 0.1875F, 0D);
		else if (ticks > 60F && ticks <= 80F)
			poseStack.translate(0D, (80F - ticks) * 0.009375F, 0D);

		submitNodeCollector.submitModelPart(xp_solidifier_model.rack, poseStack, RenderTypes.entityCutout(TEXTURE), state.lightCoords, OverlayTexture.NO_OVERLAY, null, -1, state.breakProgress);

		if (!state.mouldItem.isEmpty()) {
			poseStack.pushPose();
			poseStack.translate(0D, 0.60625D, -0.22D);
			poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
			poseStack.scale(1.25F, 1.25F, 1.25F);
			state.mouldItem.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
			poseStack.popPose();
		}
		poseStack.popPose();

		submitNodeCollector.submitModelPart(xp_solidifier_model.tank, poseStack, RenderTypes.entityCutout(TEXTURE), state.lightCoords, OverlayTexture.NO_OVERLAY, null, -1, state.breakProgress);

		if (!state.resultItem.isEmpty()) {
			poseStack.pushPose();
			poseStack.translate(0D, 0.79375D, -0.22D);
			poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
			poseStack.scale(1.25F, 1.25F, 1.25F);
			state.resultItem.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
			poseStack.popPose();
		}
		poseStack.popPose();

		if (!state.hasFluid || state.fluidSprite == null) {
			return;
		}

		float red = (state.fluidColor >> 16 & 0xFF) / 255.0F;
		float green = (state.fluidColor >> 8 & 0xFF) / 255.0F;
		float blue = (state.fluidColor & 0xFF) / 255.0F;
		TextureAtlasSprite sprite = state.fluidSprite;
		float height = state.fluidHeight;
		int light = state.lightCoords;

		submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.entityTranslucent(TextureAtlas.LOCATION_BLOCKS), (pose, buffer) ->
				RenderHelpers.submitFluidCuboid(pose, buffer, 1.984375F, 0.015625F, 0.015625F, height, 0.015625F, 1.984375F, sprite, red, green, blue, 1F, light));

		if (ticks > 20F && ticks < 60 && state.hasMould) {
			poseStack.pushPose();
			switch (state.facing) {
				case NORTH -> {}
				case SOUTH -> poseStack.translate(0D, 0D, 0.125D);
				case WEST -> poseStack.translate(-0.0625D, 0D, 0.0625D);
				case EAST -> poseStack.translate(0.0625D, 0D, 0.0625D);
				default -> {}
			}
			float mouldHeight = 0.6875F + ticks * 0.000625F;
			submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.entityTranslucent(TextureAtlas.LOCATION_BLOCKS), (pose, buffer) ->
					RenderHelpers.submitFluidCuboid(pose, buffer, 1.62F, 0.38F, 0.6875F, mouldHeight, 0.25F, 1.5F, sprite, red, green, blue, 1F, light));
			poseStack.popPose();
		}
	}
	public static class XPSolidifierRenderState extends BlockEntityRenderState {
		public boolean valid;
		public @Nullable Direction facing;
		public @Nullable OutputDirection outputDirection;
		public float ticks;
		public int progress;
		public boolean hasFluid;
		public boolean hasMould;
		public float fluidHeight;
		public int fluidColor;
		public @Nullable TextureAtlasSprite fluidSprite;
		public final ItemStackRenderState mouldItem = new ItemStackRenderState();
		public final ItemStackRenderState resultItem = new ItemStackRenderState();
	}
}
