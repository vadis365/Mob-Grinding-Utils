package mob_grinding_utils.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import mob_grinding_utils.BlockEntities.BlockEntityFan;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
public class TileEntityFanRenderer implements BlockEntityRenderer<BlockEntityFan, TileEntityFanRenderer.FanRenderState> {

	public TileEntityFanRenderer(BlockEntityRendererProvider.Context context) {
	}

	@Override
	public FanRenderState createRenderState() {
		return new FanRenderState();
	}

	@Override
	public void extractRenderState(BlockEntityFan tile, FanRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(tile, state, partialTicks, cameraPosition, breakProgress);
		state.showBox = tile.hasLevel() && tile.showRenderBox;
		if (state.showBox) {
			state.renderBox = tile.getAABBForRender();
		}
	}

	@Override
	public void submit(FanRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		if (state.showBox && state.renderBox != null) {
			RenderHelpers.drawDebugBox(state.renderBox, state.blockPos, 0F, 0F, 1F);
		}
	}

	@Override
	public AABB getRenderBoundingBox(BlockEntityFan blockEntity) {
		return blockEntity.getRenderBoundingBox();
	}
	public static class FanRenderState extends BlockEntityRenderState {
		public boolean showBox;
		public @Nullable AABB renderBox;
	}
}
