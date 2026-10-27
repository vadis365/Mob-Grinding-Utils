package mob_grinding_utils.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mob_grinding_utils.BlockEntities.BlockEntityJumboTank;
import mob_grinding_utils.BlockEntities.BlockEntitySinkTank;
import mob_grinding_utils.BlockEntities.BlockEntityTank;
import mob_grinding_utils.client.ModelLayers;
import mob_grinding_utils.models.ModelTankBlock;
import mob_grinding_utils.util.RL;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.transfer.fluid.FluidResource;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class TileEntityTankRenderer implements BlockEntityRenderer<BlockEntityTank, TileEntityTankRenderer.TankRenderState> {
    private static final Identifier TANK_TEXTURE = RL.mgu("textures/tiles/tank.png");
    private static final Identifier TANK_SINK_TEXTURE = RL.mgu("textures/tiles/tank_sink.png");
    private static final Identifier TANK_JUMBO_TEXTURE = RL.mgu("textures/tiles/tank_jumbo.png");
    private final ModelTankBlock tank_model;

    public TileEntityTankRenderer(Context context) {
        tank_model = new ModelTankBlock(context.bakeLayer(ModelLayers.TANK));
    }

    @Override
    public void submit(TileEntityTankRenderer.TankRenderState state, PoseStack poseStack, @Nonnull SubmitNodeCollector nodeCollector, @Nonnull CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.translate(0.5D, 1.5D, 0.5D);
        poseStack.scale(-0.9999F, -0.9999F, 0.9999F); //don't want to cull, but also don't want z-fighty nonsense
        nodeCollector.submitModelPart(tank_model.tank_box, poseStack, RenderTypes.entityCutout(state.texture), state.lightCoords, OverlayTexture.NO_OVERLAY, null, 0xFFFFFFFF, state.breakProgress);
        poseStack.popPose();

        if (!state.shouldRenderFluid)
            return;

        Fluid fluid = state.fluidResource.getFluid();
        float height = (0.96875F / state.fluidCapacity) * state.fluidAmount;

        FluidModel fluidModel = Minecraft.getInstance().getModelManager().getFluidStateModelSet().get(fluid.defaultFluidState());
        int fluidColor = fluidModel.fluidTintSource().color(fluid.defaultFluidState());
        TextureAtlasSprite fluidStillSprite = fluidModel.stillMaterial().sprite();

        poseStack.pushPose();
        poseStack.translate(0D, 0D, 0D);
        float xMax, zMax, xMin, zMin, yMin = 0;
        xMax = 1.984375F;
        zMax = 1.984375F;
        xMin = 0.015625F;
        zMin = 0.015625F;
        yMin = 0.015625F;
        float alpha = 1F;
        float red = (fluidColor >> 16 & 0xFF) / 255.0F;
        float green = (fluidColor >> 8 & 0xFF) / 255.0F;
        float blue = (fluidColor & 0xFF) / 255.0F;
        renderFluidCube(poseStack, nodeCollector, fluidStillSprite, xMax, xMin, yMin, height, zMin, zMax, fluidColor, state.lightCoords);
        poseStack.popPose();

    }

    private Identifier getTexture(BlockEntityTank tile) {
        return tile instanceof BlockEntityJumboTank ? TANK_JUMBO_TEXTURE : tile instanceof BlockEntitySinkTank ? TANK_SINK_TEXTURE : TANK_TEXTURE;
    }

    public static void renderFluidCube(PoseStack poseStack, SubmitNodeCollector nodeCollector, TextureAtlasSprite textureAtlasSprite, float xMax, float xMin, float yMin, float height, float zMin, float zMax, int color, int combinedLight) {
        nodeCollector.submitCustomGeometry(poseStack, RenderTypes.translucentMovingBlock(), (pose, vertexConsumer) -> renderCuboid(vertexConsumer, pose, xMax, xMin, yMin, height, zMin, zMax, textureAtlasSprite, color, combinedLight));
    }

    public static void renderCuboid(VertexConsumer buffer, PoseStack.Pose pose, float xMax, float xMin, float yMin, float height, float zMin, float zMax, TextureAtlasSprite textureAtlasSprite, int color, int combinedLight) {

        float uMin = textureAtlasSprite.getU0();
        float uMax = textureAtlasSprite.getU1();
        float vMin = textureAtlasSprite.getV0();
        float vMax = textureAtlasSprite.getV1();

        float vHeight = vMax - vMin;

        // top
        addVertexWithUV(buffer, pose, xMax, height, zMax, uMax, vMin, color, combinedLight);
        addVertexWithUV(buffer, pose, xMax, height, zMin, uMin, vMin, color, combinedLight);
        addVertexWithUV(buffer, pose, xMin, height, zMin, uMin, vMax, color, combinedLight);
        addVertexWithUV(buffer, pose, xMin, height, zMax, uMax, vMax, color, combinedLight);

        // north
        addVertexWithUV(buffer, pose, xMax, yMin, zMin, uMax, vMin, color, combinedLight);
        addVertexWithUV(buffer, pose, xMin, yMin, zMin, uMin, vMin, color, combinedLight);
        addVertexWithUV(buffer, pose, xMin, height, zMin, uMin, vMin + (vHeight * height), color, combinedLight);
        addVertexWithUV(buffer, pose, xMax, height, zMin, uMax, vMin + (vHeight * height), color, combinedLight);

        // south
        addVertexWithUV(buffer, pose, xMax, yMin, zMax, uMin, vMin, color, combinedLight);
        addVertexWithUV(buffer, pose, xMax, height, zMax, uMin, vMin + (vHeight * height), color, combinedLight);
        addVertexWithUV(buffer, pose, xMin, height, zMax, uMax, vMin + (vHeight * height), color, combinedLight);
        addVertexWithUV(buffer, pose, xMin, yMin, zMax, uMax, vMin, color, combinedLight);

        // east
        addVertexWithUV(buffer, pose, xMax, yMin, zMin, uMin, vMin, color, combinedLight);
        addVertexWithUV(buffer, pose, xMax, height, zMin, uMin, vMin + (vHeight * height), color, combinedLight);
        addVertexWithUV(buffer, pose, xMax, height, zMax, uMax, vMin + (vHeight * height), color, combinedLight);
        addVertexWithUV(buffer, pose, xMax, yMin, zMax, uMax, vMin, color, combinedLight);

        // west
        addVertexWithUV(buffer, pose, xMin, yMin, zMax, uMin, vMin, color, combinedLight);
        addVertexWithUV(buffer, pose, xMin, height, zMax, uMin, vMin + (vHeight * height), color, combinedLight);
        addVertexWithUV(buffer, pose, xMin, height, zMin, uMax, vMin + (vHeight * height), color, combinedLight);
        addVertexWithUV(buffer, pose, xMin, yMin, zMin, uMax, vMin, color, combinedLight);

        // down
        addVertexWithUV(buffer, pose, xMax, yMin, zMin, uMax, vMin, color, combinedLight);
        addVertexWithUV(buffer, pose, xMax, yMin, zMax, uMin, vMin, color, combinedLight);
        addVertexWithUV(buffer, pose, xMin, yMin, zMax, uMin, vMax, color, combinedLight);
        addVertexWithUV(buffer, pose, xMin, yMin, zMin, uMax, vMax, color, combinedLight);
    }

    private static void addVertexWithUV(VertexConsumer buffer, PoseStack.Pose pose, float x, float y, float z, float u, float v, int color, int combinedLight) {
        buffer.addVertex(pose, x / 2f, y, z / 2f).setColor(color).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(combinedLight, 240).setNormal(1, 0, 0);
    }

    @Override
    public TankRenderState createRenderState() {
        return new TankRenderState();
    }

    @Override
    public void extractRenderState(BlockEntityTank blockEntity, TankRenderState state, float partialTicks, @Nonnull Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.shouldRenderFluid = !blockEntity.tank.getResource(0).isEmpty() && blockEntity.tank.getAmountAsInt(0) > 0;
        state.fluidResource = blockEntity.tank.getResource(0);
        state.fluidAmount = blockEntity.tank.getAmountAsInt(0);
        state.fluidCapacity = blockEntity.tank.getCapacityAsInt(0, FluidResource.EMPTY);
        state.texture = getTexture(blockEntity);
    }

    public static class TankRenderState extends BlockEntityRenderState {
        public boolean shouldRenderFluid;
        public FluidResource fluidResource;
        public int fluidAmount;
        public int fluidCapacity;
        public Identifier texture;
    }
}