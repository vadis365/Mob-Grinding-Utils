package mob_grinding_utils.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import mob_grinding_utils.client.ModelLayers;
import mob_grinding_utils.models.ModelTankBlock;
import mob_grinding_utils.tile.TileEntityJumboTank;
import mob_grinding_utils.tile.TileEntitySinkTank;
import mob_grinding_utils.tile.TileEntityTank;
import mob_grinding_utils.util.RL;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class TileEntityTankRenderer implements BlockEntityRenderer<TileEntityTank, MGUBlockEntityRenderState> {
    private static final Identifier TANK_TEXTURE = RL.mgu("tiles/tank"), TANK_SINK_TEXTURE = RL.mgu("tiles/tank_sink"), TANK_JUMBO_TEXTURE = RL.mgu("tiles/tank_jumbo");
    private final ModelTankBlock model;
    public TileEntityTankRenderer(Context context) { model = new ModelTankBlock(context.bakeLayer(ModelLayers.TANK)); }
    @Override public MGUBlockEntityRenderState createRenderState() { return new MGUBlockEntityRenderState(); }
    @Override public void extractRenderState(TileEntityTank tile, MGUBlockEntityRenderState state, float partialTicks, Vec3 camera, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(tile, state, partialTicks, camera, breakProgress);
        state.fluid = tile.tank.stack().copy(); state.fluidCapacity = tile.tank.capacity(); state.valid = tile instanceof TileEntityJumboTank;
        if (tile instanceof TileEntitySinkTank) state.facing = net.minecraft.core.Direction.UP;
    }
    @Override public void submit(MGUBlockEntityRenderState state, PoseStack stack, SubmitNodeCollector nodes, CameraRenderState camera) {
        Identifier texture = state.valid ? TANK_JUMBO_TEXTURE : state.facing == net.minecraft.core.Direction.UP ? TANK_SINK_TEXTURE : TANK_TEXTURE;
        stack.pushPose(); stack.translate(0.5D, 1.5D, 0.5D); stack.scale(-0.9999F, -0.9999F, 0.9999F);
        nodes.submitModelPart(model.tank_box, stack, RenderTypes.entityCutout(texture), state.lightCoords, OverlayTexture.NO_OVERLAY, null, -1, state.breakProgress);
        stack.popPose();
        if (state.fluid.isEmpty()) return;
        float height = 0.96875F / state.fluidCapacity * state.fluid.getAmount();
        if (height <= 0) return;
		TextureAtlasSprite sprite = Minecraft.getInstance().getModelManager().getFluidStateModelSet().get(state.fluid.getFluid().defaultFluidState()).stillMaterial().sprite();
		MGURenderUtil.submitFluidCuboid(stack, nodes, sprite, -1, state.lightCoords, 1.984375F, 0.015625F, 0.015625F, height, 0.015625F, 1.984375F);
    }
}
