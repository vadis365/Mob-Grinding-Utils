package mob_grinding_utils.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.client.ModelLayers;
import mob_grinding_utils.models.ModelAHConnect;
import mob_grinding_utils.tile.TileEntityAbsorptionHopper;
import mob_grinding_utils.tile.TileEntityAbsorptionHopper.EnumStatus;
import mob_grinding_utils.util.RL;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class TileEntityAbsorptionRenderer implements BlockEntityRenderer<TileEntityAbsorptionHopper, MGUBlockEntityRenderState> {
	private static final Identifier ITEM_TEXTURE = RL.mgu("tiles/absorption_hopper_connects_items");
	private static final Identifier FLUID_TEXTURE = RL.mgu("tiles/absorption_hopper_connects_fluids");
	private final ModelAHConnect connectionModel;

	public TileEntityAbsorptionRenderer(Context context) {
		connectionModel = new ModelAHConnect(context.bakeLayer(ModelLayers.ABSORPTION_HOPPER));
	}

	@Override public MGUBlockEntityRenderState createRenderState() { return new MGUBlockEntityRenderState(); }

	@Override public void extractRenderState(TileEntityAbsorptionHopper tile, MGUBlockEntityRenderState renderState, float partialTicks, Vec3 camera, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(tile, renderState, partialTicks, camera, breakProgress);
		BlockState state = tile.getBlockState();
		renderState.valid = state.getBlock() == ModBlocks.ABSORPTION_HOPPER.getBlock();
		renderState.absorptionStatus = tile.status.clone();
		renderState.showRenderBox = tile.showRenderBox;
		renderState.renderBox = tile.getAABBForRender();
	}

	@Override public void submit(MGUBlockEntityRenderState state, PoseStack matrixStack, SubmitNodeCollector nodes, CameraRenderState camera) {
		if (!state.valid) return;
		matrixStack.pushPose(); matrixStack.translate(0.5D, 0.5D, 0.5D);
		for (Direction facing : Direction.values()) {
			if (state.absorptionStatus[facing.ordinal()] == EnumStatus.STATUS_OUTPUT_ITEM) {
				matrixStack.pushPose();
				getRotTranslation(matrixStack, facing);
				submitConnection(matrixStack, nodes, ITEM_TEXTURE, state);
				matrixStack.popPose();
			}
			if (state.absorptionStatus[facing.ordinal()] == EnumStatus.STATUS_OUTPUT_FLUID) {
				matrixStack.pushPose();
				getRotTranslation(matrixStack, facing);
				submitConnection(matrixStack, nodes, FLUID_TEXTURE, state);
				matrixStack.popPose();
			}
		}
		matrixStack.popPose();

		if (!state.showRenderBox || state.renderBox == null)
			return;
		matrixStack.pushPose();
		matrixStack.translate(-0.0005D, -0.0005D, -0.0005D);
		matrixStack.scale(0.999F, 0.999F, 0.999F);

		MGURenderUtil.submitLineBox(matrixStack, nodes, state.renderBox, 1, 1, 0);
		matrixStack.popPose();
	}

	private void submitConnection(PoseStack stack, SubmitNodeCollector nodes, Identifier texture, MGUBlockEntityRenderState state) {
		nodes.submitModelPart(connectionModel.plate, stack, RenderTypes.entitySolid(texture), state.lightCoords, OverlayTexture.NO_OVERLAY, null, 0x7F7F7FFF, state.breakProgress);
		nodes.submitModelPart(connectionModel.pipe, stack, RenderTypes.entitySolid(texture), state.lightCoords, OverlayTexture.NO_OVERLAY, null, 0x7F7F7FFF, state.breakProgress);
	}

	public void getRotTranslation(PoseStack matrixStack, Direction facing) {
		switch (facing) {
		case UP:
			matrixStack.mulPose(Axis.XP.rotationDegrees(180F));
			break;
		case DOWN:
			break;
		case NORTH:
			matrixStack.mulPose(Axis.XP.rotationDegrees(90F));
			break;
		case SOUTH:
			matrixStack.mulPose(Axis.XN.rotationDegrees(90F));
			break;
		case WEST:
			matrixStack.mulPose(Axis.ZN.rotationDegrees(90F));
			break;
		case EAST:
			matrixStack.mulPose(Axis.ZP.rotationDegrees(90F));
			break;
		}
	}

	@Override
	public AABB getRenderBoundingBox(TileEntityAbsorptionHopper blockEntity) {
		return blockEntity.getAABBWithModifiers();
	}
}
