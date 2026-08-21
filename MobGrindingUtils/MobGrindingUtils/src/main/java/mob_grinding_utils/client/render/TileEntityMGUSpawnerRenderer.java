package mob_grinding_utils.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mob_grinding_utils.tile.TileEntityMGUSpawner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class TileEntityMGUSpawnerRenderer implements BlockEntityRenderer<TileEntityMGUSpawner, MGUBlockEntityRenderState> {
    public TileEntityMGUSpawnerRenderer(Context context) {}
    @Override public MGUBlockEntityRenderState createRenderState() { return new MGUBlockEntityRenderState(); }
    @Override public void extractRenderState(TileEntityMGUSpawner tile, MGUBlockEntityRenderState state, float partialTicks, Vec3 camera, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(tile, state, partialTicks, camera, breakProgress);
        state.showRenderBox = tile.showRenderBox; state.renderBox = tile.getAABBForRender();
        state.animation = tile.animationTicks + (tile.animationTicks - tile.prevAnimationTicks) * partialTicks;
        if (tile.isOn && tile.hasSpawnEggItem() && tile.getEntityToRender() != null) state.spawnedEntity = Minecraft.getInstance().getEntityRenderDispatcher().extractEntity(tile.getEntityToRender(), partialTicks);
    }
    @Override public void submit(MGUBlockEntityRenderState state, PoseStack stack, SubmitNodeCollector nodes, CameraRenderState camera) {
        if (state.spawnedEntity != null) { stack.pushPose(); stack.translate(.5D, .75D, .5D); stack.mulPose(Axis.YP.rotationDegrees(state.animation)); stack.scale(.125F, .125F, .125F); Minecraft.getInstance().getEntityRenderDispatcher().submit(state.spawnedEntity, camera, 0, 0, 0, stack, nodes); stack.popPose(); }
        if (state.showRenderBox && state.renderBox != null) { stack.pushPose(); stack.translate(-.0005D, -.0005D, -.0005D); stack.scale(.999F, .999F, .999F); MGURenderUtil.submitLineBox(stack, nodes, state.renderBox, 1, 0, 0); stack.popPose(); }
    }
    @Override public AABB getRenderBoundingBox(TileEntityMGUSpawner blockEntity) { return blockEntity.getAABBWithModifiers(); }
}
