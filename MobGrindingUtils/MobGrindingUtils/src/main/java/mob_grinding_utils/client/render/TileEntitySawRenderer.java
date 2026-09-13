package mob_grinding_utils.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mob_grinding_utils.BlockEntities.BlockEntitySaw;
import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.blocks.BlockSaw;
import mob_grinding_utils.client.ModelLayers;
import mob_grinding_utils.models.ModelSawBase;
import mob_grinding_utils.models.ModelSawBlade;
import mob_grinding_utils.util.RL;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
public class TileEntitySawRenderer implements BlockEntityRenderer<BlockEntitySaw, TileEntitySawRenderer.SawRenderState> {
	private static final Identifier BASE_TEXTURE = RL.mgu("textures/tiles/saw_base.png");
	private static final Identifier BLADE_TEXTURE = RL.mgu("textures/tiles/saw_blade.png");
	private final ModelSawBase saw_base;
	private final ModelSawBlade saw_blade;

	public TileEntitySawRenderer(Context context) {
		saw_base = new ModelSawBase(context.bakeLayer(ModelLayers.SAW_BASE));
		saw_blade = new ModelSawBlade(context.bakeLayer(ModelLayers.SAW_BLADE));
	}

	@Override
	public SawRenderState createRenderState() {
		return new SawRenderState();
	}

	@Override
	public void extractRenderState(BlockEntitySaw tile, SawRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(tile, state, partialTicks, cameraPosition, breakProgress);
		state.valid = tile.hasLevel() && tile.getBlockState().is(ModBlocks.SAW.getBlock());
		if (!state.valid) {
			return;
		}
		state.facing = tile.getBlockState().getValue(BlockSaw.FACING);
		state.animationTicks = tile.animationTicks + (tile.animationTicks - tile.prevAnimationTicks) * partialTicks;
	}

	@Override
	public void submit(SawRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		if (!state.valid || state.facing == null) {
			return;
		}

		RenderType baseType = RenderTypes.entitySolid(BASE_TEXTURE);
		RenderType bladeType = RenderTypes.entitySolid(BLADE_TEXTURE);

		poseStack.pushPose();
		poseStack.translate(0.5D, 0.5D, 0.5D);
		poseStack.scale(-1, -1, 1);

		switch (state.facing) {
			case UP -> poseStack.mulPose(Axis.YP.rotationDegrees(0F));
			case DOWN -> poseStack.mulPose(Axis.XP.rotationDegrees(180F));
			case NORTH -> poseStack.mulPose(Axis.XP.rotationDegrees(90F));
			case SOUTH -> poseStack.mulPose(Axis.XP.rotationDegrees(-90F));
			case WEST -> poseStack.mulPose(Axis.ZP.rotationDegrees(90F));
			case EAST -> poseStack.mulPose(Axis.ZP.rotationDegrees(-90F));
		}
		poseStack.translate(0F, -1F, 0F);
		submitParts(submitNodeCollector, poseStack, baseType, state, saw_base.baseParts());

		poseStack.pushPose();
		poseStack.mulPose(Axis.YP.rotationDegrees(state.animationTicks));
		submitParts(submitNodeCollector, poseStack, baseType, state, saw_base.axleParts());

		poseStack.pushPose();
		poseStack.mulPose(Axis.YP.rotationDegrees(45F));
		submitParts(submitNodeCollector, poseStack, baseType, state, saw_base.maceParts());
		poseStack.popPose();

		poseStack.pushPose();
		poseStack.mulPose(Axis.YP.rotationDegrees(165F));
		submitParts(submitNodeCollector, poseStack, baseType, state, saw_base.maceParts());
		poseStack.popPose();

		poseStack.pushPose();
		poseStack.mulPose(Axis.YP.rotationDegrees(285F));
		submitParts(submitNodeCollector, poseStack, baseType, state, saw_base.maceParts());
		poseStack.popPose();

		poseStack.pushPose();
		poseStack.translate(0F, 0.2F, -0.16F);
		poseStack.mulPose(Axis.XP.rotationDegrees(8F));
		submitNodeCollector.submitModel(saw_blade, Unit.INSTANCE, poseStack, bladeType, state.lightCoords, OverlayTexture.NO_OVERLAY, 0, state.breakProgress);
		poseStack.popPose();

		poseStack.pushPose();
		poseStack.translate(0F, 0.00F, 0.16F);
		poseStack.mulPose(Axis.XP.rotationDegrees(-8F));
		submitNodeCollector.submitModel(saw_blade, Unit.INSTANCE, poseStack, bladeType, state.lightCoords, OverlayTexture.NO_OVERLAY, 0, state.breakProgress);
		poseStack.popPose();

		poseStack.pushPose();
		poseStack.translate(0F, -0.2F, -0.16F);
		poseStack.mulPose(Axis.XP.rotationDegrees(8F));
		submitNodeCollector.submitModel(saw_blade, Unit.INSTANCE, poseStack, bladeType, state.lightCoords, OverlayTexture.NO_OVERLAY, 0, state.breakProgress);
		poseStack.popPose();

		poseStack.popPose();
		poseStack.popPose();
	}

	private void submitParts(SubmitNodeCollector collector, PoseStack poseStack, RenderType renderType, SawRenderState state, ModelPart[] parts) {
		for (ModelPart part : parts) {
			collector.submitModelPart(part, poseStack, renderType, state.lightCoords, OverlayTexture.NO_OVERLAY, null, -1, state.breakProgress);
		}
	}
	public static class SawRenderState extends BlockEntityRenderState {
		public boolean valid;
		public @Nullable Direction facing;
		public float animationTicks;
	}
}
