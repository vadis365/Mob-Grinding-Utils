package mob_grinding_utils.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mob_grinding_utils.client.ModelLayers;
import mob_grinding_utils.models.ModelSawBase;
import mob_grinding_utils.models.ModelSawBlade;
import mob_grinding_utils.util.RL;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Vector3fc;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
public class TileSawStackItemRenderer implements NoDataSpecialModelRenderer {

    private static final Identifier BASE_TEXTURE = RL.mgu("textures/tiles/saw_base.png");
    private static final Identifier BLADE_TEXTURE = RL.mgu("textures/tiles/saw_blade.png");
    private final ModelSawBase saw_base;
    private final ModelSawBlade saw_blade;

    public TileSawStackItemRenderer(SpecialModelRenderer.BakingContext context) {
        saw_base = new ModelSawBase(context.entityModelSet().bakeLayer(ModelLayers.SAW_BASE));
        saw_blade = new ModelSawBlade(context.entityModelSet().bakeLayer(ModelLayers.SAW_BLADE));
    }

    @Override
    public void submit(@Nonnull PoseStack poseStack, @Nonnull SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
        poseStack.pushPose();
        poseStack.translate(0.5D, 1.5D, 0.5D);
        poseStack.scale(-1, -1, 1);
        saw_base.submitBase(poseStack, submitNodeCollector, lightCoords, overlayCoords, BASE_TEXTURE);
        saw_base.submitAxle(poseStack, submitNodeCollector, lightCoords, overlayCoords, BASE_TEXTURE);

        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(45F));
        saw_base.submitMace(poseStack, submitNodeCollector, lightCoords, overlayCoords, BASE_TEXTURE);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(165F));
        saw_base.submitMace(poseStack, submitNodeCollector, lightCoords, overlayCoords, BASE_TEXTURE);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(285F));
        saw_base.submitMace(poseStack, submitNodeCollector, lightCoords, overlayCoords, BASE_TEXTURE);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(0F, 0.2F, -0.16F);
        poseStack.mulPose(Axis.XP.rotationDegrees(8F));
        saw_blade.submitBlade(poseStack, submitNodeCollector, lightCoords, overlayCoords, BLADE_TEXTURE);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(0F, 0.00F, 0.16F);
        poseStack.mulPose(Axis.XP.rotationDegrees(-8F));
        saw_blade.submitBlade(poseStack, submitNodeCollector, lightCoords, overlayCoords, BLADE_TEXTURE);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(0F, -0.2F, -0.16F);
        poseStack.mulPose(Axis.XP.rotationDegrees(8F));
        saw_blade.submitBlade(poseStack, submitNodeCollector, lightCoords, overlayCoords, BLADE_TEXTURE);
        poseStack.popPose();

        poseStack.popPose();

    }

    @Override
    public void getExtents(@Nonnull Consumer<Vector3fc> output) {
        saw_base.base.getExtentsForGui(new PoseStack(), output);
    }
}
