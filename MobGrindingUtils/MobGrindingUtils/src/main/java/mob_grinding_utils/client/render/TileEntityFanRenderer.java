package mob_grinding_utils.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import mob_grinding_utils.tile.TileEntityFan;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class TileEntityFanRenderer implements BlockEntityRenderer<TileEntityFan, MGUBlockEntityRenderState> {

	public TileEntityFanRenderer(BlockEntityRendererProvider.Context context) {
	}

	@Override public MGUBlockEntityRenderState createRenderState() { return new MGUBlockEntityRenderState(); }

	@Override public void extractRenderState(TileEntityFan tile, MGUBlockEntityRenderState state, float partialTicks, Vec3 camera, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(tile, state, partialTicks, camera, breakProgress);
		state.showRenderBox = tile.showRenderBox;
		state.renderBox = tile.getAABBForRender();
	}

	@Override public void submit(MGUBlockEntityRenderState state, PoseStack stack, SubmitNodeCollector nodes, CameraRenderState camera) {
		if (!state.showRenderBox || state.renderBox == null) return;
		stack.pushPose();
		stack.translate(-0.0005D, -0.0005D, -0.0005D);
		stack.scale(0.999F, 0.999F, 0.999F);
		MGURenderUtil.submitLineBox(stack, nodes, state.renderBox, 0, 0, 1);
		stack.popPose();
	}
	

	@Override
	public AABB getRenderBoundingBox(TileEntityFan blockEntity) {
		return blockEntity.getRenderBoundingBox();
	}
}
