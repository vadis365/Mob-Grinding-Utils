package mob_grinding_utils.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mob_grinding_utils.BlockEntities.BlockEntityMGUSpawner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class TileEntityMGUSpawnerRenderer implements BlockEntityRenderer<BlockEntityMGUSpawner, TileEntityMGUSpawnerRenderer.MGUSpawnerRenderState> {

    public TileEntityMGUSpawnerRenderer(Context context) {
    }

    @Override
    public void submit(MGUSpawnerRenderState state, @Nonnull PoseStack poseStack, @Nonnull SubmitNodeCollector submitNodeCollector, @Nonnull CameraRenderState camera) {

        if (state.shouldRender) {
            float ticks = state.ticks;
            EntityRenderState entity = state.entityToRender;

            poseStack.pushPose();
            poseStack.translate(0.5D, 0.75D, 0.5D);

            poseStack.mulPose(Axis.YP.rotationDegrees(ticks));
            poseStack.scale(0.125F, 0.125F, 0.125F);
            Minecraft.getInstance().getEntityRenderDispatcher().submit(entity, camera, 0d, 0d, 0d, poseStack, submitNodeCollector);
            poseStack.popPose();
        }

        if (!state.showRenderBox)
            return;

        poseStack.pushPose();
        poseStack.translate(-0.0005D, -0.0005D, -0.0005D);
        poseStack.scale(0.999F, 0.999F, 0.999F);

        submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.lines(), (pose, nodes) -> LineBoxRenderer.render(pose, nodes, state.AABBForRender, 0xFF0000FF));
        poseStack.popPose();
    }

    @Nonnull
    @Override
    public AABB getRenderBoundingBox(BlockEntityMGUSpawner blockEntity) {
        return blockEntity.getAABBWithModifiers();
    }

    @Override
    public MGUSpawnerRenderState createRenderState() {
        return null;
    }

    @Override
    public void extractRenderState(BlockEntityMGUSpawner blockEntity, MGUSpawnerRenderState state, float partialTicks, @Nonnull Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.showRenderBox = blockEntity.showRenderBox;
        state.AABBForRender = blockEntity.getAABBForRender();
        state.ticks = blockEntity.animationTicks + (blockEntity.animationTicks - blockEntity.prevAnimationTicks)  * partialTicks;
        state.partialTicks = partialTicks;
        state.hasSpawnEggItem = blockEntity.hasSpawnEggItem();
        if (blockEntity.getEntityToRender() != null)
            state.entityToRender = Minecraft.getInstance().getEntityRenderDispatcher().extractEntity(blockEntity.getEntityToRender(), partialTicks);
        state.shouldRender = blockEntity.isOn && state.hasSpawnEggItem && blockEntity.getEntityToRender() != null;
    }

    public static class MGUSpawnerRenderState extends BlockEntityRenderState {
        public boolean showRenderBox;
        public AABB AABBForRender;
        public boolean shouldRender;
        public boolean hasSpawnEggItem;
        public float ticks;
        public EntityRenderState entityToRender;
        public float partialTicks;
    }
}