package mob_grinding_utils.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import mob_grinding_utils.BlockEntities.BlockEntityFan;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class TileEntityFanRenderer implements BlockEntityRenderer<BlockEntityFan, TileEntityFanRenderer.FanRenderState> {

    public TileEntityFanRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void submit(FanRenderState state, @Nonnull PoseStack poseStack, @Nonnull SubmitNodeCollector submitNodeCollector, @Nonnull CameraRenderState camera) {
        if (!state.showRenderBox)
            return;

        poseStack.pushPose();
        poseStack.translate(-0.0005D, -0.0005D, -0.0005D);
        poseStack.scale(0.999F, 0.999F, 0.999F);

        submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.lines(), (poses, nodes) -> LineBoxRenderer.render(poses, nodes, state.AABBForRender, 0x0000FFFF));
        poseStack.popPose();

    }


    @Nonnull
    @Override
    @OnlyIn(Dist.CLIENT)
    public AABB getRenderBoundingBox(BlockEntityFan blockEntity) {
        return blockEntity.getRenderBoundingBox();
    }

    @Override
    public FanRenderState createRenderState() {
        return new FanRenderState();
    }

    @Override
    public void extractRenderState(BlockEntityFan blockEntity, FanRenderState state, float partialTicks, @Nonnull Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.showRenderBox = blockEntity.showRenderBox;
        state.AABBForRender = blockEntity.getAABBForRender();
    }

    public static class FanRenderState extends BlockEntityRenderState {
        public boolean showRenderBox;
        public AABB AABBForRender;
    }
}