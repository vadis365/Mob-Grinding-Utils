package mob_grinding_utils.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mob_grinding_utils.BlockEntities.BlockEntityAbsorptionHopper;
import mob_grinding_utils.BlockEntities.BlockEntityAbsorptionHopper.EnumStatus;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.awt.*;

@OnlyIn(Dist.CLIENT)
public class TileEntityAbsorptionRenderer implements BlockEntityRenderer<BlockEntityAbsorptionHopper, TileEntityAbsorptionRenderer.AHRenderState> {
    private static final Identifier ITEM_TEXTURE = RL.mgu("textures/tiles/absorption_hopper_connects_items.png");
    private static final Identifier FLUID_TEXTURE = RL.mgu("textures/tiles/absorption_hopper_connects_fluids.png");
    private final ModelAHConnect connectionModel;

    public TileEntityAbsorptionRenderer(Context context) {
        connectionModel = new ModelAHConnect(context.bakeLayer(ModelLayers.ABSORPTION_HOPPER));
    }

    @Override
    public void submit(AHRenderState renderState, @Nonnull PoseStack poseStack, @Nonnull SubmitNodeCollector nodeCollector, @Nonnull CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.translate(0.5D, 0.5D, 0.5D);
        for (Direction facing : Direction.values()) {
            if (renderState.status[facing.ordinal()] == EnumStatus.STATUS_OUTPUT_ITEM) {
                poseStack.pushPose();
                getRotTranslation(poseStack, facing);
                connectionModel.submitConnector(poseStack, nodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, ITEM_TEXTURE, renderState.breakProgress);
                poseStack.popPose();
            }
            if (renderState.status[facing.ordinal()] == EnumStatus.STATUS_OUTPUT_FLUID) {
                poseStack.pushPose();
                getRotTranslation(poseStack, facing);
                connectionModel.submitConnector(poseStack, nodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, FLUID_TEXTURE, renderState.breakProgress);
                poseStack.popPose();
            }
        }
        poseStack.popPose();

        if (!renderState.showRenderBox)
            return;
        poseStack.pushPose();
        poseStack.translate(-0.0005D, -0.0005D, -0.0005D);
        poseStack.scale(0.999F, 0.999F, 0.999F);

        nodeCollector.submitCustomGeometry(poseStack, RenderTypes.lines(), new LineBoxRenderer(renderState.getAABBForRender, new Color(1F, 1F, 0F, 1F)));
        poseStack.popPose();
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
    public AABB getRenderBoundingBox(BlockEntityAbsorptionHopper blockEntity) {
        return blockEntity.getAABBWithModifiers();
    }

    public static class AHRenderState extends BlockEntityRenderState {
        public EnumStatus[] status = new EnumStatus[] { EnumStatus.STATUS_NONE, EnumStatus.STATUS_NONE, EnumStatus.STATUS_NONE, EnumStatus.STATUS_NONE, EnumStatus.STATUS_NONE, EnumStatus.STATUS_NONE };
        public boolean showRenderBox = false;
        public AABB getAABBForRender = new AABB(0, 0, 0, 1, 1, 1);
    }

    @Override
    public void extractRenderState(BlockEntityAbsorptionHopper blockEntity, AHRenderState state, float partialTicks, @Nonnull Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        for (int i = 0; i < 6; i++) {
            state.status[i] = blockEntity.status[i];
        }
        state.showRenderBox = blockEntity.showRenderBox;
        state.getAABBForRender = blockEntity.getAABBForRender();
    }

    @Override
    public AHRenderState createRenderState() {
        return new AHRenderState();
    }
}