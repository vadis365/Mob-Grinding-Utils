package mob_grinding_utils.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import mob_grinding_utils.client.ModelLayers;
import mob_grinding_utils.components.FluidContents;
import mob_grinding_utils.components.MGUComponents;
import mob_grinding_utils.models.ModelXPSolidifier;
import mob_grinding_utils.util.RL;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.fluids.FluidStack;
import org.joml.Vector3fc;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
public class TileXPSolidifierStackItemRenderer implements SpecialModelRenderer<TileTankStackItemRenderer.Data> {

	private static final Identifier TEXTURE = RL.mgu("textures/tiles/xp_solidifier_no_push.png");
	private final ModelXPSolidifier xp_solidifier_model;
	private FluidStack fluidStack;

	public TileXPSolidifierStackItemRenderer(SpecialModelRenderer.BakingContext context) {
		xp_solidifier_model = new ModelXPSolidifier(context.entityModelSet().bakeLayer(ModelLayers.XPSOLIDIFIER));
	}

	@Override
    public void submit(TileTankStackItemRenderer.Data argument, @Nonnull PoseStack poseStack, @Nonnull SubmitNodeCollector nodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
		poseStack.pushPose();
		poseStack.translate(0.5D, 1.5D, 0.5D);
		poseStack.scale(-0.9999F, -0.9999F, 0.9999F);

        nodeCollector.submitModelPart(xp_solidifier_model.tank, poseStack, RenderTypes.entityCutout(TEXTURE), lightCoords, OverlayTexture.NO_OVERLAY, null, 0xFFFFFFFF, null);
        nodeCollector.submitModelPart(xp_solidifier_model.tank, poseStack, RenderTypes.entityCutout(TEXTURE), lightCoords, OverlayTexture.NO_OVERLAY, null, 0xFFFFFFFF, null);
        nodeCollector.submitModelPart(xp_solidifier_model.rack, poseStack, RenderTypes.entityCutout(TEXTURE), lightCoords, OverlayTexture.NO_OVERLAY, null, 0xFFFFFFFF, null);

		poseStack.popPose();

		float fluidLevel = argument.fluidStack().getAmount();
		if (fluidLevel < 1)
			return;
		float tankMax = 16000F;
		float height = (0.46875F / tankMax) * fluidLevel; // volumes hardcoded until config

		poseStack.pushPose();
		poseStack.translate(0D, 0D, 0D);
		float xMax, zMax, xMin, zMin, yMin = 0;
		xMax = 1.984375F;
		zMax = 1.984375F;
		xMin = 0.015625F;
		zMin = 0.015625F;
		yMin = 0.015625F;
        TileEntityTankRenderer.renderFluidCube(poseStack, nodeCollector, argument.fluidStack().getFluid(), xMax, xMin, yMin, height, zMin, zMax, lightCoords);
		poseStack.popPose();
	}

    @Override
    public void getExtents(@Nonnull Consumer<Vector3fc> output) {
        xp_solidifier_model.tank.getExtentsForGui(new PoseStack(), output);
    }

    @Override
    public TileTankStackItemRenderer.Data extractArgument(ItemStack stack) {
        return new TileTankStackItemRenderer.Data(stack.getOrDefault(MGUComponents.FLUID, FluidContents.EMPTY).get(), stack.copy());
    }
}
