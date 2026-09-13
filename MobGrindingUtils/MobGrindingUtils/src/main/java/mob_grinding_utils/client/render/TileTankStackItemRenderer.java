package mob_grinding_utils.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.function.Consumer;
import mob_grinding_utils.client.ModelLayers;
import mob_grinding_utils.models.ModelTankBlock;
import mob_grinding_utils.util.RL;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;
import org.joml.Vector3fc;
public class TileTankStackItemRenderer implements NoDataSpecialModelRenderer {
	private static final Identifier TANK_TEXTURE = RL.mgu("textures/tiles/tank.png");
	private static final Identifier TANK_SINK_TEXTURE = RL.mgu("textures/tiles/tank_sink.png");
	private static final Identifier TANK_JUMBO_TEXTURE = RL.mgu("textures/tiles/tank_jumbo.png");

	private final ModelTankBlock tank_model;
	private final Identifier texture;

	public TileTankStackItemRenderer(ModelTankBlock tankModel, Identifier texture) {
		this.tank_model = tankModel;
		this.texture = texture;
	}

	@Override
	public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
		poseStack.pushPose();
		poseStack.translate(0.5D, 1.5D, 0.5D);
		poseStack.scale(-0.9999F, -0.9999F, 0.9999F);
		submitNodeCollector.submitModel(tank_model, Unit.INSTANCE, poseStack, RenderTypes.entityTranslucentCullItemTarget(texture), lightCoords, overlayCoords, outlineColor, null);
		poseStack.popPose();
	}

	@Override
	public void getExtents(Consumer<Vector3fc> output) {
		PoseStack poseStack = new PoseStack();
		tank_model.root().getExtentsForGui(poseStack, output);
	}
	public record Unbaked(String variant) implements NoDataSpecialModelRenderer.Unbaked {
		public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(
				i -> i.group(Codec.STRING.optionalFieldOf("variant", "tank").forGetter(Unbaked::variant)).apply(i, Unbaked::new)
		);

		@Override
		public MapCodec<? extends NoDataSpecialModelRenderer.Unbaked> type() {
			return MAP_CODEC;
		}

		@Override
		public SpecialModelRenderer<Void> bake(SpecialModelRenderer.BakingContext context) {
			Identifier texture = switch (variant) {
				case "jumbo" -> TANK_JUMBO_TEXTURE;
				case "sink" -> TANK_SINK_TEXTURE;
				default -> TANK_TEXTURE;
			};
			return new TileTankStackItemRenderer(new ModelTankBlock(context.entityModelSet().bakeLayer(ModelLayers.TANK)), texture);
		}
	}
}
