package mob_grinding_utils.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mob_grinding_utils.BlockEntities.BlockEntityXPSolidifier;
import mob_grinding_utils.BlockEntities.BlockEntityXPSolidifier.OutputDirection;
import mob_grinding_utils.blocks.BlockXPSolidifier;
import mob_grinding_utils.client.ModelLayers;
import mob_grinding_utils.models.ModelXPSolidifier;
import mob_grinding_utils.util.RL;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.transfer.fluid.FluidResource;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class TileEntityXPSolidifierRenderer implements BlockEntityRenderer<BlockEntityXPSolidifier, TileEntityXPSolidifierRenderer.XPSolidifierRenderState> {
    private static final Identifier TEXTURE = RL.mgu("textures/tiles/xp_solidifier.png");
    private static final Identifier TEXTURE_NO_PUSH = RL.mgu("textures/tiles/xp_solidifier_no_push.png");
    private final ModelXPSolidifier xp_solidifier_model;
    private final ItemModelResolver itemModelRenderer;

    public TileEntityXPSolidifierRenderer(Context context) {
        xp_solidifier_model = new ModelXPSolidifier(context.bakeLayer(ModelLayers.XPSOLIDIFIER));
        itemModelRenderer = context.itemModelResolver();
    }

    @Override
    public void submit(XPSolidifierRenderState state, @Nonnull PoseStack poseStack, @Nonnull SubmitNodeCollector nodeCollector, @Nonnull CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.translate(0.5D, 1.5D, 0.5D);
        poseStack.scale(-0.9999F, -0.9999F, 0.9999F);

        switch (state.outputDirection) {
        case NONE:
        case NORTH:
            poseStack.mulPose(Axis.YP.rotationDegrees(90F));
            break;
        case SOUTH:
            poseStack.mulPose(Axis.YN.rotationDegrees(90F));
            break;
        case WEST:
            poseStack.mulPose(Axis.YP.rotationDegrees(0F));
            break;
        case EAST:
            poseStack.mulPose(Axis.YN.rotationDegrees(180F));
            break;
        default:
            poseStack.mulPose(Axis.YP.rotationDegrees(90F));
            break;
        }
        nodeCollector.submitModelPart(xp_solidifier_model.top, poseStack, RenderTypes.entityCutout(state.outputDirection == OutputDirection.NONE ? TEXTURE_NO_PUSH : TEXTURE), state.lightCoords, OverlayTexture.NO_OVERLAY, null, 0xFFFFFFFF, state.breakProgress);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(0.5D, 1.5D, 0.5D);
        poseStack.scale(-0.9999F, -0.9999F, 0.9999F); //don't want to cull, but also don't want z-fighty nonsense

        switch (state.facing) {
        case NORTH:
            poseStack.mulPose(Axis.YP.rotationDegrees(0F));
            break;
        case SOUTH:
            poseStack.mulPose(Axis.YP.rotationDegrees(180F));
            break;
        case WEST:
            poseStack.mulPose(Axis.YN.rotationDegrees(90F));
            break;
        case EAST:
            poseStack.mulPose(Axis.YP.rotationDegrees(90F));
            break;
        default:
            break;
        }

        poseStack.pushPose();

        if(state.ticks > 0 && state.ticks <= 20F)
            poseStack.translate(0D, state.ticks * 0.009375F, 0D);
        if(state.ticks > 20F && state.ticks <= 60)
            poseStack.translate(0D, 0.1875F, 0D);
        if(state.ticks > 60F && state.ticks <= 80F)
            poseStack.translate(0D, (80F - state.ticks)  * 0.009375F, 0D);
        if(state.ticks > 80F || state.ticks <= 0)
            poseStack.translate(0D, 0D, 0D);
        nodeCollector.submitModelPart(xp_solidifier_model.rack, poseStack, RenderTypes.entityCutout(TEXTURE), state.lightCoords, OverlayTexture.NO_OVERLAY, null, 0xFFFFFFFF, state.breakProgress);

        poseStack.pushPose();
        poseStack.translate(0D, 0.60625D, -0.22D);
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        poseStack.scale(1.25F, 1.25F, 1.25F);
        state.mouldState.submit(poseStack, nodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();
        poseStack.popPose();
        nodeCollector.submitModelPart(xp_solidifier_model.tank, poseStack, RenderTypes.entityCutout(TEXTURE), state.lightCoords, OverlayTexture.NO_OVERLAY, null, 0xFFFFFFFF, state.breakProgress);
        poseStack.pushPose();
        poseStack.translate(0D, 0.79375D, -0.22D);
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        poseStack.scale(1.25F, 1.25F, 1.25F);
/*		ItemStack stackResult = state.stackResult;
        if (stackResult.isEmpty() && !tile.getCachedOutPutRenderStack().isEmpty() && tile.getProgress() > 60) { //may want to add some earlier blending fade here
            Minecraft.getInstance().getTextureManager().bindForSetup(InventoryMenu.BLOCK_ATLAS);
            Minecraft.getInstance().getItemRenderer().render(tile.getCachedOutPutRenderStack(), ItemDisplayContext.GROUND, false, poseStack, bufferIn, combinedLight, combinedOverlay, Minecraft.getInstance().getItemRenderer() .getModel(tile.getCachedOutPutRenderStack(), null, null, 0));
        } else if (!stackResult.isEmpty()) {
            Minecraft.getInstance().getTextureManager().bindForSetup(InventoryMenu.BLOCK_ATLAS);
            Minecraft.getInstance().getItemRenderer().render(stackResult, ItemDisplayContext.GROUND, false, poseStack, bufferIn, combinedLight, combinedOverlay, Minecraft.getInstance().getItemRenderer().getModel(stackResult, null, null, 0));
        }*/ //TODO i forget what this did...
        state.resultState.submit(poseStack, nodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();
        poseStack.popPose();

        if (state.fluidAmount < 1 || state.fluid == null)
            return;

        float height = (0.46875F / state.fluidCapacity) * state.fluidAmount;

        poseStack.pushPose();
        poseStack.translate(0D, 0D, 0D);
        float xMax, zMax, xMin, zMin, yMin = 0;
        xMax = 1.984375F;
        zMax = 1.984375F;
        xMin = 0.015625F;
        zMin = 0.015625F;
        yMin = 0.015625F;
        TileEntityTankRenderer.renderFluidCube(poseStack, nodeCollector, state.fluid, xMax, xMin, yMin, height, zMin, zMax, state.lightCoords);
        poseStack.popPose();

        if(state.ticks > 20F && state.ticks < 60 && state.hasMould) {
            poseStack.pushPose();
            switch (state.facing) {
            case NORTH:
                poseStack.translate(0D, 0D, 0D);
                break;
            case SOUTH:
                poseStack.translate(0D, 0D, 0.125D);
                break;
            case WEST:
                poseStack.translate(-0.0625D, 0D, 0.0625D);
                break;
            case EAST:
                poseStack.translate(0.0625D, 0D, 0.0625D);
                break;
            default:
                break;
            }
            xMax = 1.62F;
            zMax = 1.5F;
            xMin = 0.38F;
            zMin = 0.25F;
            yMin = 0.6875F;
            TileEntityTankRenderer.renderFluidCube(poseStack, nodeCollector, state.fluid, xMax, xMin, yMin, 0.6875F + state.ticks * 0.000625F, zMin, zMax, state.lightCoords);
            poseStack.popPose();
        }

    }

    @Override
    public XPSolidifierRenderState createRenderState() {
        return new XPSolidifierRenderState();
    }

    @Override
    public void extractRenderState(BlockEntityXPSolidifier blockEntity, XPSolidifierRenderState state, float partialTicks, @Nonnull Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.outputDirection = blockEntity.outputDirection;
        state.facing = blockEntity.getBlockState().getValue(BlockXPSolidifier.FACING);
        state.ticks = blockEntity.prevAnimationTicks + (blockEntity.animationTicks - blockEntity.prevAnimationTicks) * partialTicks;
        state.fluid = blockEntity.tank.getResource(0).getFluid();
        state.fluidAmount = blockEntity.tank.getAmountAsInt(0);
        state.fluidCapacity = blockEntity.tank.getCapacityAsInt(0, FluidResource.EMPTY);
        state.hasMould = !blockEntity.inputSlots.getResource(0).isEmpty() && blockEntity.inputSlots.getAmountAsInt(0) > 0;
        itemModelRenderer.updateForTopItem(state.mouldState, blockEntity.inputSlots.getResource(0).toStack(), ItemDisplayContext.GROUND, blockEntity.getLevel(), null, 0);
        itemModelRenderer.updateForTopItem(state.resultState, blockEntity.outputSlot.getResource(0).toStack(), ItemDisplayContext.GROUND, blockEntity.getLevel(), null, 0);
    }

    public static class XPSolidifierRenderState extends BlockEntityRenderState {
        public OutputDirection outputDirection = OutputDirection.NONE;
        public Direction facing = Direction.NORTH;
        public float ticks = 0F;
        public Fluid fluid;
        public int fluidAmount;
        public int fluidCapacity;
        public ItemStackRenderState mouldState = new ItemStackRenderState();
        public ItemStackRenderState resultState = new ItemStackRenderState();
        public boolean hasMould = false;
    }
}