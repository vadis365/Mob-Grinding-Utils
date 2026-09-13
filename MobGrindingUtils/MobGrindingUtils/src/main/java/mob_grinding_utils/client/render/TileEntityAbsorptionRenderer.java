package mob_grinding_utils.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mob_grinding_utils.BlockEntities.BlockEntityAbsorptionHopper;
import mob_grinding_utils.BlockEntities.BlockEntityAbsorptionHopper.EnumStatus;
import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.client.ModelLayers;
import mob_grinding_utils.models.ModelAHConnect;
import mob_grinding_utils.util.RL;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
public class TileEntityAbsorptionRenderer implements BlockEntityRenderer<BlockEntityAbsorptionHopper, TileEntityAbsorptionRenderer.AbsorptionRenderState> {
	private static final Identifier ITEM_TEXTURE = RL.mgu("textures/tiles/absorption_hopper_connects_items.png");
	private static final Identifier FLUID_TEXTURE = RL.mgu("textures/tiles/absorption_hopper_connects_fluids.png");
	private final ModelAHConnect connectionModel;

	public TileEntityAbsorptionRenderer(Context context) {
		connectionModel = new ModelAHConnect(context.bakeLayer(ModelLayers.ABSORPTION_HOPPER));
	}

	@Override
	public AbsorptionRenderState createRenderState() {
		return new AbsorptionRenderState();
	}

	@Override
	public void extractRenderState(BlockEntityAbsorptionHopper tile, AbsorptionRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(tile, state, partialTicks, cameraPosition, breakProgress);
		state.valid = tile.hasLevel() && tile.getBlockState().is(ModBlocks.ABSORPTION_HOPPER.getBlock());
		if (!state.valid) {
			return;
		}
		System.arraycopy(tile.status, 0, state.status, 0, tile.status.length);
		state.showBox = tile.showRenderBox;
		if (state.showBox) {
			state.renderBox = tile.getAABBForRender();
		}
	}

	@Override
	public void submit(AbsorptionRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		if (!state.valid) {
			return;
		}

		poseStack.pushPose();
		poseStack.translate(0.5D, 0.5D, 0.5D);
		for (Direction facing : Direction.values()) {
			EnumStatus status = state.status[facing.ordinal()];
			if (status == EnumStatus.STATUS_OUTPUT_ITEM || status == EnumStatus.STATUS_OUTPUT_FLUID) {
				Identifier texture = status == EnumStatus.STATUS_OUTPUT_ITEM ? ITEM_TEXTURE : FLUID_TEXTURE;
				poseStack.pushPose();
				getRotTranslation(poseStack, facing);
				submitNodeCollector.submitModel(connectionModel, Unit.INSTANCE, poseStack, RenderTypes.entitySolid(texture), state.lightCoords, OverlayTexture.NO_OVERLAY, 0x7F7F7FFF, null, 0, state.breakProgress);
				poseStack.popPose();
			}
		}
		poseStack.popPose();

		if (state.showBox && state.renderBox != null) {
			RenderHelpers.drawDebugBox(state.renderBox, state.blockPos, 1F, 1F, 0F);
		}
	}

	private static void getRotTranslation(PoseStack matrixStack, Direction facing) {
		switch (facing) {
			case UP -> matrixStack.mulPose(Axis.XP.rotationDegrees(180F));
			case DOWN -> {}
			case NORTH -> matrixStack.mulPose(Axis.XP.rotationDegrees(90F));
			case SOUTH -> matrixStack.mulPose(Axis.XN.rotationDegrees(90F));
			case WEST -> matrixStack.mulPose(Axis.ZN.rotationDegrees(90F));
			case EAST -> matrixStack.mulPose(Axis.ZP.rotationDegrees(90F));
		}
	}

	@Override
	public AABB getRenderBoundingBox(BlockEntityAbsorptionHopper blockEntity) {
		return blockEntity.getAABBWithModifiers();
	}
	public static class AbsorptionRenderState extends BlockEntityRenderState {
		public boolean valid;
		public final EnumStatus[] status = new EnumStatus[Direction.values().length];
		public boolean showBox;
		public @Nullable AABB renderBox;
	}
}
