package mob_grinding_utils.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.serialization.MapCodec;
import java.util.function.Consumer;
import mob_grinding_utils.client.ModelLayers;
import mob_grinding_utils.models.ModelSawBase;
import mob_grinding_utils.models.ModelSawBlade;
import mob_grinding_utils.util.RL;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;
import org.joml.Vector3fc;
public class TileSawStackItemRenderer implements NoDataSpecialModelRenderer {
	private static final Identifier BASE_TEXTURE = RL.mgu("textures/tiles/saw_base.png");
	private static final Identifier BLADE_TEXTURE = RL.mgu("textures/tiles/saw_blade.png");
	private final ModelSawBase saw_base;
	private final ModelSawBlade saw_blade;

	public TileSawStackItemRenderer(ModelSawBase sawBase, ModelSawBlade sawBlade) {
		this.saw_base = sawBase;
		this.saw_blade = sawBlade;
	}

	@Override
	public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
		RenderType baseType = RenderTypes.entitySolid(BASE_TEXTURE);
		RenderType bladeType = RenderTypes.entitySolid(BLADE_TEXTURE);

		poseStack.pushPose();
		poseStack.translate(0.5D, 1.5D, 0.5D);
		poseStack.scale(-1, -1, 1);
		submitParts(submitNodeCollector, poseStack, baseType, lightCoords, overlayCoords, outlineColor, saw_base.baseParts());
		submitParts(submitNodeCollector, poseStack, baseType, lightCoords, overlayCoords, outlineColor, saw_base.axleParts());

		poseStack.pushPose();
		poseStack.mulPose(Axis.YP.rotationDegrees(45F));
		submitParts(submitNodeCollector, poseStack, baseType, lightCoords, overlayCoords, outlineColor, saw_base.maceParts());
		poseStack.popPose();

		poseStack.pushPose();
		poseStack.mulPose(Axis.YP.rotationDegrees(165F));
		submitParts(submitNodeCollector, poseStack, baseType, lightCoords, overlayCoords, outlineColor, saw_base.maceParts());
		poseStack.popPose();

		poseStack.pushPose();
		poseStack.mulPose(Axis.YP.rotationDegrees(285F));
		submitParts(submitNodeCollector, poseStack, baseType, lightCoords, overlayCoords, outlineColor, saw_base.maceParts());
		poseStack.popPose();

		poseStack.pushPose();
		poseStack.translate(0F, 0.2F, -0.16F);
		poseStack.mulPose(Axis.XP.rotationDegrees(8F));
		submitNodeCollector.submitModel(saw_blade, Unit.INSTANCE, poseStack, bladeType, lightCoords, overlayCoords, outlineColor, null);
		poseStack.popPose();

		poseStack.pushPose();
		poseStack.translate(0F, 0.00F, 0.16F);
		poseStack.mulPose(Axis.XP.rotationDegrees(-8F));
		submitNodeCollector.submitModel(saw_blade, Unit.INSTANCE, poseStack, bladeType, lightCoords, overlayCoords, outlineColor, null);
		poseStack.popPose();

		poseStack.pushPose();
		poseStack.translate(0F, -0.2F, -0.16F);
		poseStack.mulPose(Axis.XP.rotationDegrees(8F));
		submitNodeCollector.submitModel(saw_blade, Unit.INSTANCE, poseStack, bladeType, lightCoords, overlayCoords, outlineColor, null);
		poseStack.popPose();

		poseStack.popPose();
	}

	private static void submitParts(SubmitNodeCollector collector, PoseStack poseStack, RenderType renderType, int light, int overlay, int outline, ModelPart[] parts) {
		for (ModelPart part : parts) {
			collector.submitModelPart(part, poseStack, renderType, light, overlay, null, false, false, -1, null, outline);
		}
	}

	@Override
	public void getExtents(Consumer<Vector3fc> output) {
		PoseStack poseStack = new PoseStack();
		saw_base.root().getExtentsForGui(poseStack, output);
	}
	public record Unbaked() implements NoDataSpecialModelRenderer.Unbaked {
		public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(new Unbaked());

		@Override
		public MapCodec<? extends NoDataSpecialModelRenderer.Unbaked> type() {
			return MAP_CODEC;
		}

		@Override
		public SpecialModelRenderer<Void> bake(SpecialModelRenderer.BakingContext context) {
			return new TileSawStackItemRenderer(
					new ModelSawBase(context.entityModelSet().bakeLayer(ModelLayers.SAW_BASE)),
					new ModelSawBlade(context.entityModelSet().bakeLayer(ModelLayers.SAW_BLADE))
			);
		}
	}
}
