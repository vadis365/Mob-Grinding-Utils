package mob_grinding_utils.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.serialization.MapCodec;
import java.util.function.Consumer;
import mob_grinding_utils.client.ModelLayers;
import mob_grinding_utils.models.ModelSawBase;
import mob_grinding_utils.models.ModelSawBlade;
import mob_grinding_utils.util.RL;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import org.joml.Vector3fc;

/** Modern data-driven replacement for BlockEntityWithoutLevelRenderer. */
public final class TileSawStackItemRenderer implements NoDataSpecialModelRenderer {
    private static final Identifier BASE = RL.mgu("textures/tiles/saw_base.png"), BLADE = RL.mgu("textures/tiles/saw_blade.png");
    private final ModelSawBase base; private final ModelSawBlade blade;
    private TileSawStackItemRenderer(SpecialModelRenderer.BakingContext context) { base = new ModelSawBase(context.entityModelSet().bakeLayer(ModelLayers.SAW_BASE)); blade = new ModelSawBlade(context.entityModelSet().bakeLayer(ModelLayers.SAW_BLADE)); }
    @Override public void submit(PoseStack p, SubmitNodeCollector n, int light, int overlay, boolean foil, int outline) {
        p.pushPose(); p.translate(.5D, 1.5D, .5D); p.scale(-1, -1, 1);
        submit(n, p, BASE, light, overlay, outline, base.base, base.plinth, base.axle, base.axle2, base.axleTop);
        p.pushPose(); p.mulPose(Axis.YP.rotationDegrees(45)); submitMace(n,p,light,overlay,outline); p.popPose(); p.pushPose(); p.mulPose(Axis.YP.rotationDegrees(165)); submitMace(n,p,light,overlay,outline); p.popPose(); p.pushPose(); p.mulPose(Axis.YP.rotationDegrees(285)); submitMace(n,p,light,overlay,outline); p.popPose();
        submitBlade(p,n,light,overlay,outline,.2F,-.16F,8); submitBlade(p,n,light,overlay,outline,0,.16F,-8); submitBlade(p,n,light,overlay,outline,-.2F,-.16F,8); p.popPose();
    }
    private void submitMace(SubmitNodeCollector n, PoseStack p, int light, int overlay, int outline) { submit(n,p,BASE,light,overlay,outline,base.maceBase,base.maceArm,base.mace1,base.mace2,base.mace3,base.mace4); }
    private void submitBlade(PoseStack p, SubmitNodeCollector n, int light, int overlay, int outline, float y, float z, float angle) { p.pushPose(); p.translate(0,y,z); p.mulPose(Axis.XP.rotationDegrees(angle)); n.submitCustomGeometry(p, RenderTypes.entitySolid(BLADE), (pose, buffer) -> { PoseStack legacy = new PoseStack(); legacy.last().set(pose); blade.renderToBuffer(legacy, buffer, light, overlay, -1); }); p.popPose(); }
    private static void submit(SubmitNodeCollector n, PoseStack p, Identifier texture, int light, int overlay, int outline, net.minecraft.client.model.geom.ModelPart... parts) { for (var part : parts) n.submitModelPart(part,p,RenderTypes.entitySolid(texture),light,overlay,null,false,false,-1,null,outline); }
    @Override public void getExtents(Consumer<Vector3fc> output) { PoseStack p = new PoseStack(); base.base.getExtentsForGui(p, output); }
    public record Unbaked() implements NoDataSpecialModelRenderer.Unbaked {
        public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(new Unbaked());
        @Override public MapCodec<Unbaked> type() { return MAP_CODEC; }
        @Override public TileSawStackItemRenderer bake(SpecialModelRenderer.BakingContext context) { return new TileSawStackItemRenderer(context); }
    }
}
