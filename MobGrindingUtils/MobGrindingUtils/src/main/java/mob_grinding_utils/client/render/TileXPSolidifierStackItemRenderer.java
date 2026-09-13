package mob_grinding_utils.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import java.util.function.Consumer;
import mob_grinding_utils.client.ModelLayers;
import mob_grinding_utils.models.ModelXPSolidifier;
import mob_grinding_utils.util.RL;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import org.joml.Vector3fc;
public class TileXPSolidifierStackItemRenderer implements NoDataSpecialModelRenderer {
	private static final Identifier TEXTURE = RL.mgu("textures/tiles/xp_solidifier_no_push.png");
	private final ModelXPSolidifier xp_solidifier_model;

	public TileXPSolidifierStackItemRenderer(ModelXPSolidifier model) {
		this.xp_solidifier_model = model;
	}

	@Override
	public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
		poseStack.pushPose();
		poseStack.translate(0.5D, 1.5D, 0.5D);
		poseStack.scale(-0.9999F, -0.9999F, 0.9999F);
		var renderType = RenderTypes.entityCutout(TEXTURE);
		submitNodeCollector.submitModelPart(xp_solidifier_model.tank, poseStack, renderType, lightCoords, overlayCoords, null, false, false, -1, null, outlineColor);
		submitNodeCollector.submitModelPart(xp_solidifier_model.top, poseStack, renderType, lightCoords, overlayCoords, null, false, false, -1, null, outlineColor);
		submitNodeCollector.submitModelPart(xp_solidifier_model.rack, poseStack, renderType, lightCoords, overlayCoords, null, false, false, -1, null, outlineColor);
		poseStack.popPose();
	}

	@Override
	public void getExtents(Consumer<Vector3fc> output) {
		PoseStack poseStack = new PoseStack();
		xp_solidifier_model.root().getExtentsForGui(poseStack, output);
	}
	public record Unbaked() implements NoDataSpecialModelRenderer.Unbaked {
		public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(new Unbaked());

		@Override
		public MapCodec<? extends NoDataSpecialModelRenderer.Unbaked> type() {
			return MAP_CODEC;
		}

		@Override
		public SpecialModelRenderer<Void> bake(SpecialModelRenderer.BakingContext context) {
			return new TileXPSolidifierStackItemRenderer(new ModelXPSolidifier(context.entityModelSet().bakeLayer(ModelLayers.XPSOLIDIFIER)));
		}
	}
}
