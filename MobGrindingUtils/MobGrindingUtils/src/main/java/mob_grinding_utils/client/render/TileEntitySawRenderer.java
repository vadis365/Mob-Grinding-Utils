package mob_grinding_utils.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mob_grinding_utils.BlockEntities.BlockEntitySaw;
import mob_grinding_utils.blocks.BlockSaw;
import mob_grinding_utils.client.ModelLayers;
import mob_grinding_utils.models.ModelSawBase;
import mob_grinding_utils.models.ModelSawBlade;
import mob_grinding_utils.util.RL;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
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
    public void submit(SawRenderState state, PoseStack poseStack, @Nonnull SubmitNodeCollector nodeCollector, @Nonnull CameraRenderState camera) {
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
        saw_base.submitBase(poseStack, nodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, BASE_TEXTURE);

        poseStack.pushPose();

        float ticks = state.ticks;
        poseStack.mulPose(Axis.YP.rotationDegrees(ticks));
        saw_base.submitAxle(poseStack, nodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, BASE_TEXTURE);

        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(45F));
        saw_base.submitMace(poseStack, nodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, BASE_TEXTURE);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(165F));
        saw_base.submitMace(poseStack, nodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, BASE_TEXTURE);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(285F));
        saw_base.submitMace(poseStack, nodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, BASE_TEXTURE);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(0F, 0.2F, -0.16F);
        poseStack.mulPose(Axis.XP.rotationDegrees(8F));
        saw_blade.submitBlade(poseStack, nodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, BLADE_TEXTURE);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(0F, 0.00F, 0.16F);
        poseStack.mulPose(Axis.XP.rotationDegrees(-8F));
        saw_blade.submitBlade(poseStack, nodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, BLADE_TEXTURE);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(0F, -0.2F, -0.16F);
        poseStack.mulPose(Axis.XP.rotationDegrees(8F));
        saw_blade.submitBlade(poseStack, nodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, BLADE_TEXTURE);
        poseStack.popPose();

        poseStack.popPose();
        poseStack.popPose();

    }

    @Override
    public SawRenderState createRenderState() {
        return new SawRenderState();
    }

    public static class SawRenderState extends BlockEntityRenderState {
        public float ticks;
        public Direction facing = Direction.NORTH;
    }

    @Override
    public void extractRenderState(BlockEntitySaw blockEntity, SawRenderState state, float partialTicks, @Nonnull Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.facing = blockEntity.getBlockState().getValue(BlockSaw.FACING);
        state.ticks = blockEntity.animationTicks + (blockEntity.animationTicks - blockEntity.prevAnimationTicks) * partialTicks;
    }
}