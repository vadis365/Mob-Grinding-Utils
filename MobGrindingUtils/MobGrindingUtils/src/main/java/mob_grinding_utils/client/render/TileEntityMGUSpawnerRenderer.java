package mob_grinding_utils.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mob_grinding_utils.BlockEntities.BlockEntityMGUSpawner;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
public class TileEntityMGUSpawnerRenderer implements BlockEntityRenderer<BlockEntityMGUSpawner, TileEntityMGUSpawnerRenderer.MGUSpawnerRenderState> {
	private final EntityRenderDispatcher entityRenderer;

	public TileEntityMGUSpawnerRenderer(Context context) {
		this.entityRenderer = context.entityRenderer();
	}

	@Override
	public MGUSpawnerRenderState createRenderState() {
		return new MGUSpawnerRenderState();
	}

	@Override
	public void extractRenderState(BlockEntityMGUSpawner tile, MGUSpawnerRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(tile, state, partialTicks, cameraPosition, breakProgress);
		state.valid = tile.hasLevel();
		state.showBox = state.valid && tile.showRenderBox;
		if (state.showBox) {
			state.renderBox = tile.getAABBForRender();
		}
		state.displayEntity = null;
		if (state.valid && tile.isOn && tile.hasSpawnEggItem() && tile.getEntityToRender() != null) {
			Entity entity = tile.getEntityToRender();
			state.displayEntity = this.entityRenderer.extractEntity(entity, partialTicks);
			state.displayEntity.lightCoords = state.lightCoords;
			state.spin = tile.animationTicks + (tile.animationTicks - tile.prevAnimationTicks) * partialTicks;
			state.scale = 0.125F;
		}
	}

	@Override
	public void submit(MGUSpawnerRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		if (state.displayEntity != null) {
			poseStack.pushPose();
			poseStack.translate(0.5D, 0.75D, 0.5D);
			poseStack.mulPose(Axis.YP.rotationDegrees(state.spin));
			poseStack.scale(state.scale, state.scale, state.scale);
			this.entityRenderer.submit(state.displayEntity, camera, 0.0, 0.0, 0.0, poseStack, submitNodeCollector);
			poseStack.popPose();
		}

		if (state.showBox && state.renderBox != null) {
			RenderHelpers.drawDebugBox(state.renderBox, state.blockPos, 1F, 0F, 0F);
		}
	}

	@Override
	public AABB getRenderBoundingBox(BlockEntityMGUSpawner blockEntity) {
		return blockEntity.getAABBWithModifiers();
	}
	public static class MGUSpawnerRenderState extends BlockEntityRenderState {
		public boolean valid;
		public boolean showBox;
		public @Nullable AABB renderBox;
		public @Nullable EntityRenderState displayEntity;
		public float spin;
		public float scale;
	}
}
