package mob_grinding_utils.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.blocks.BlockSaw;
import mob_grinding_utils.client.ModelLayers;
import mob_grinding_utils.models.ModelSawBase;
import mob_grinding_utils.models.ModelSawBlade;
import mob_grinding_utils.tile.TileEntitySaw;
import mob_grinding_utils.util.RL;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class TileEntitySawRenderer implements BlockEntityRenderer<TileEntitySaw, MGUBlockEntityRenderState> {

	private static final Identifier BASE_TEXTURE = RL.mgu("tiles/saw_base");
	private static final Identifier BLADE_TEXTURE = RL.mgu("tiles/saw_blade");
	private final ModelSawBase saw_base;
	private final ModelSawBlade saw_blade;

	public TileEntitySawRenderer(Context context) {
		saw_base = new ModelSawBase(context.bakeLayer(ModelLayers.SAW_BASE)); 
		saw_blade = new ModelSawBlade(context.bakeLayer(ModelLayers.SAW_BLADE));
	}

	@Override public MGUBlockEntityRenderState createRenderState() { return new MGUBlockEntityRenderState(); }

	@Override public void extractRenderState(TileEntitySaw tile, MGUBlockEntityRenderState state, float partialTicks, Vec3 camera, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(tile, state, partialTicks, camera, breakProgress);
		BlockState blockState = tile.getBlockState();
		state.valid = blockState.getBlock() == ModBlocks.SAW.getBlock();
		state.facing = state.valid ? blockState.getValue(BlockSaw.FACING) : Direction.NORTH;
		state.animation = tile.animationTicks + (tile.animationTicks - tile.prevAnimationTicks) * partialTicks;
	}

	@Override public void submit(MGUBlockEntityRenderState state, PoseStack matrixStack, SubmitNodeCollector nodes, CameraRenderState camera) {
		if (!state.valid) return;
		Direction facing = state.facing;

		matrixStack.pushPose();
		matrixStack.translate(0.5D, 0.5D, 0.5D);
		matrixStack.scale(-1, -1, 1);

		switch (facing) {
			case UP -> matrixStack.mulPose(Axis.YP.rotationDegrees(0F));
			case DOWN -> matrixStack.mulPose(Axis.XP.rotationDegrees(180F));
			case NORTH -> matrixStack.mulPose(Axis.XP.rotationDegrees(90F));
			case SOUTH -> matrixStack.mulPose(Axis.XP.rotationDegrees(-90F));
			case WEST -> matrixStack.mulPose(Axis.ZP.rotationDegrees(90F));
			case EAST -> matrixStack.mulPose(Axis.ZP.rotationDegrees(-90F));
		}
		matrixStack.translate(0F, -1F, 0F);
		submitBase(matrixStack, nodes, state);

		matrixStack.pushPose();

		float ticks = state.animation;
		matrixStack.mulPose(Axis.YP.rotationDegrees(ticks));
		submitAxle(matrixStack, nodes, state);
		
		matrixStack.pushPose();
		matrixStack.mulPose(Axis.YP.rotationDegrees(45F));
		submitMace(matrixStack, nodes, state);
		matrixStack.popPose();
		
		matrixStack.pushPose();
		matrixStack.mulPose(Axis.YP.rotationDegrees(165F));
		submitMace(matrixStack, nodes, state);
		matrixStack.popPose();
		
		matrixStack.pushPose();
		matrixStack.mulPose(Axis.YP.rotationDegrees(285F));
		submitMace(matrixStack, nodes, state);
		matrixStack.popPose();
		
		matrixStack.pushPose();
		matrixStack.translate(0F, 0.2F, -0.16F);
		matrixStack.mulPose(Axis.XP.rotationDegrees(8F));
		submitBlade(matrixStack, nodes, state);
		matrixStack.popPose();

		matrixStack.pushPose();
		matrixStack.translate(0F, 0.00F, 0.16F);
		matrixStack.mulPose(Axis.XP.rotationDegrees(-8F));
		submitBlade(matrixStack, nodes, state);
		matrixStack.popPose();

		matrixStack.pushPose();
		matrixStack.translate(0F, -0.2F, -0.16F);
		matrixStack.mulPose(Axis.XP.rotationDegrees(8F));
		submitBlade(matrixStack, nodes, state);
		matrixStack.popPose();

		matrixStack.popPose();
		matrixStack.popPose();

	}

	private void submitBase(PoseStack stack, SubmitNodeCollector nodes, MGUBlockEntityRenderState state) { submit(nodes, stack, BASE_TEXTURE, state, saw_base.base, saw_base.plinth); }
	private void submitAxle(PoseStack stack, SubmitNodeCollector nodes, MGUBlockEntityRenderState state) { submit(nodes, stack, BASE_TEXTURE, state, saw_base.axle, saw_base.axle2, saw_base.axleTop); }
	private void submitMace(PoseStack stack, SubmitNodeCollector nodes, MGUBlockEntityRenderState state) { submit(nodes, stack, BASE_TEXTURE, state, saw_base.maceBase, saw_base.maceArm, saw_base.mace1, saw_base.mace2, saw_base.mace3, saw_base.mace4); }
	private static void submit(SubmitNodeCollector nodes, PoseStack stack, Identifier texture, MGUBlockEntityRenderState state, net.minecraft.client.model.geom.ModelPart... parts) {
		for (var part : parts) nodes.submitModelPart(part, stack, RenderTypes.entitySolid(texture), state.lightCoords, OverlayTexture.NO_OVERLAY, null, -1, state.breakProgress);
	}
	private void submitBlade(PoseStack stack, SubmitNodeCollector nodes, MGUBlockEntityRenderState state) {
		nodes.submitCustomGeometry(stack, RenderTypes.entitySolid(BLADE_TEXTURE), (pose, buffer) -> {
			PoseStack legacyStack = new PoseStack(); legacyStack.last().set(pose);
			saw_blade.renderToBuffer(legacyStack, buffer, state.lightCoords, OverlayTexture.NO_OVERLAY, -1);
		});
	}
}
