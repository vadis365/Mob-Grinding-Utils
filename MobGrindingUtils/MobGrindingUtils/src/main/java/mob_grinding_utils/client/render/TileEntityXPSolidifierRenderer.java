package mob_grinding_utils.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.blocks.BlockXPSolidifier;
import mob_grinding_utils.client.ModelLayers;
import mob_grinding_utils.models.ModelXPSolidifier;
import mob_grinding_utils.tile.TileEntityXPSolidifier;
import mob_grinding_utils.tile.TileEntityXPSolidifier.OutputDirection;
import mob_grinding_utils.util.RL;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class TileEntityXPSolidifierRenderer implements BlockEntityRenderer<TileEntityXPSolidifier, MGUBlockEntityRenderState> {
    private static final Identifier TEXTURE = RL.mgu("textures/tiles/xp_solidifier.png"), TEXTURE_NO_PUSH = RL.mgu("textures/tiles/xp_solidifier_no_push.png");
    private final ModelXPSolidifier model;
    private final ItemModelResolver itemModels;
    public TileEntityXPSolidifierRenderer(Context context) { model = new ModelXPSolidifier(context.bakeLayer(ModelLayers.XPSOLIDIFIER)); itemModels = context.itemModelResolver(); }
    @Override public MGUBlockEntityRenderState createRenderState() { return new MGUBlockEntityRenderState(); }
    @Override public void extractRenderState(TileEntityXPSolidifier tile, MGUBlockEntityRenderState state, float partialTicks, Vec3 camera, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(tile, state, partialTicks, camera, breakProgress);
        BlockState blockState = tile.getBlockState(); state.valid = blockState.getBlock() == ModBlocks.XPSOLIDIFIER.getBlock();
        if (!state.valid) return;
        state.facing = (Direction) blockState.getValue(BlockXPSolidifier.FACING); state.outputDirection = tile.outputDirection;
        state.animation = tile.prevAnimationTicks + (tile.animationTicks - tile.prevAnimationTicks) * partialTicks;
        state.fluid = tile.tank.stack().copy(); state.fluidCapacity = tile.tank.capacity();
        state.input = tile.getInputStack(0).copy(); state.output = tile.getOutputStack().copy();
        if (state.output.isEmpty() && !tile.getCachedOutPutRenderStack().isEmpty() && tile.getProgress() > 60) state.output = tile.getCachedOutPutRenderStack().copy();
        int seed = (int)tile.getBlockPos().asLong();
        itemModels.updateForTopItem(state.inputItemState, state.input, ItemDisplayContext.GROUND, tile.getLevel(), null, seed);
        itemModels.updateForTopItem(state.outputItemState, state.output, ItemDisplayContext.GROUND, tile.getLevel(), null, seed + 1);
    }
    @Override public void submit(MGUBlockEntityRenderState state, PoseStack stack, SubmitNodeCollector nodes, CameraRenderState camera) {
        if (!state.valid) return;
        submitExport(state, stack, nodes); submitBodyAndRack(state, stack, nodes); submitFluid(state, stack, nodes);
    }
    private void submitExport(MGUBlockEntityRenderState s, PoseStack p, SubmitNodeCollector n) {
        p.pushPose(); p.translate(0.5D, 1.5D, 0.5D); p.scale(-.9999F, -.9999F, .9999F);
        switch (s.outputDirection) { case SOUTH -> p.mulPose(Axis.YN.rotationDegrees(90)); case WEST -> {} case EAST -> p.mulPose(Axis.YN.rotationDegrees(180)); default -> p.mulPose(Axis.YP.rotationDegrees(90)); }
        n.submitModelPart(model.top, p, RenderTypes.entityCutout(s.outputDirection == OutputDirection.NONE ? TEXTURE_NO_PUSH : TEXTURE), s.lightCoords, OverlayTexture.NO_OVERLAY, null, -1, s.breakProgress); p.popPose();
    }
    private void submitBodyAndRack(MGUBlockEntityRenderState s, PoseStack p, SubmitNodeCollector n) {
        p.pushPose(); p.translate(.5D, 1.5D, .5D); p.scale(-.9999F, -.9999F, .9999F);
        switch (s.facing) { case SOUTH -> p.mulPose(Axis.YP.rotationDegrees(180)); case WEST -> p.mulPose(Axis.YN.rotationDegrees(90)); case EAST -> p.mulPose(Axis.YP.rotationDegrees(90)); default -> {} }
        p.pushPose(); float t = s.animation;
        if (t > 0 && t <= 20) p.translate(0, t * .009375F, 0); else if (t <= 60) p.translate(0, .1875F, 0); else if (t <= 80) p.translate(0, (80 - t) * .009375F, 0);
        n.submitModelPart(model.rack, p, RenderTypes.entityCutout(TEXTURE), s.lightCoords, OverlayTexture.NO_OVERLAY, null, -1, s.breakProgress);
        p.pushPose(); p.translate(0, .60625D, -.22D); p.mulPose(Axis.XP.rotationDegrees(90)); p.scale(1.25F, 1.25F, 1.25F); s.inputItemState.submit(p, n, s.lightCoords, OverlayTexture.NO_OVERLAY, 0); p.popPose(); p.popPose();
        n.submitModelPart(model.tank, p, RenderTypes.entityCutout(TEXTURE), s.lightCoords, OverlayTexture.NO_OVERLAY, null, -1, s.breakProgress);
        p.pushPose(); p.translate(0, .79375D, -.22D); p.mulPose(Axis.XP.rotationDegrees(90)); p.scale(1.25F, 1.25F, 1.25F); s.outputItemState.submit(p, n, s.lightCoords, OverlayTexture.NO_OVERLAY, 0); p.popPose(); p.popPose();
    }
    private static void submitFluid(MGUBlockEntityRenderState s, PoseStack p, SubmitNodeCollector n) {
        if (s.fluid.isEmpty()) return;
        float height = .46875F / s.fluidCapacity * s.fluid.getAmount(); if (height <= 0) return;
		TextureAtlasSprite sprite = Minecraft.getInstance().getModelManager().getFluidStateModelSet().get(s.fluid.getFluid().defaultFluidState()).stillMaterial().sprite();
		MGURenderUtil.submitFluidCuboid(p, n, sprite, -1, s.lightCoords, 1.984375F, .015625F, .015625F, height, .015625F, 1.984375F);
        if (s.animation > 20 && s.animation < 60 && !s.input.isEmpty()) {
            p.pushPose(); switch (s.facing) { case SOUTH -> p.translate(0, 0, .125D); case WEST -> p.translate(-.0625D, 0, .0625D); case EAST -> p.translate(.0625D, 0, .0625D); default -> {} }
			MGURenderUtil.submitFluidCuboid(p, n, sprite, -1, s.lightCoords, 1.62F, .38F, .6875F, .6875F + s.animation * .000625F, .25F, 1.5F); p.popPose();
        }
    }
}
