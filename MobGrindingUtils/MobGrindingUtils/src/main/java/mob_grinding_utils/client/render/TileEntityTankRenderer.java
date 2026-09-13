package mob_grinding_utils.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import mob_grinding_utils.BlockEntities.BlockEntityJumboTank;
import mob_grinding_utils.BlockEntities.BlockEntitySinkTank;
import mob_grinding_utils.BlockEntities.BlockEntityTank;
import mob_grinding_utils.client.ModelLayers;
import mob_grinding_utils.models.ModelTankBlock;
import mob_grinding_utils.util.RL;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.data.AtlasIds;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.fluid.FluidTintSource;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import org.jspecify.annotations.Nullable;
public class TileEntityTankRenderer implements BlockEntityRenderer<BlockEntityTank, TileEntityTankRenderer.TankRenderState> {
	private static final Identifier TANK_TEXTURE = RL.mgu("textures/tiles/tank.png");
	private static final Identifier TANK_SINK_TEXTURE = RL.mgu("textures/tiles/tank_sink.png");
	private static final Identifier TANK_JUMBO_TEXTURE = RL.mgu("textures/tiles/tank_jumbo.png");
	private static final Identifier FALLBACK_FLUID_SPRITE = RL.mgu("block/fluid_xp");
	private final ModelTankBlock tank_model;

	public TileEntityTankRenderer(Context context) {
		tank_model = new ModelTankBlock(context.bakeLayer(ModelLayers.TANK));
	}

	@Override
	public TankRenderState createRenderState() {
		return new TankRenderState();
	}

	@Override
	public void extractRenderState(BlockEntityTank tile, TankRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(tile, state, partialTicks, cameraPosition, breakProgress);
		state.texture = tile instanceof BlockEntityJumboTank ? TANK_JUMBO_TEXTURE : tile instanceof BlockEntitySinkTank ? TANK_SINK_TEXTURE : TANK_TEXTURE;
		FluidStack fluid = tile.tank.getResource(0).toStack(tile.tank.getAmountAsInt(0));
		state.hasFluid = !fluid.isEmpty() && tile.tank.getAmountAsInt(0) >= 1;
		if (state.hasFluid) {
			state.fluidHeight = (0.96875F / tile.tank.getCapacityAsInt(0, FluidResource.EMPTY)) * tile.tank.getAmountAsInt(0);
			resolveFluidVisuals(fluid, state);
		} else {
			state.fluidSprite = null;
		}
	}

	private static void resolveFluidVisuals(FluidStack fluidStack, TankRenderState state) {
		Fluid fluid = fluidStack.getFluid();
		FluidState fluidState = fluid.defaultFluidState();
		FluidModel fluidModel = Minecraft.getInstance().getModelManager().getFluidStateModelSet().get(fluidState);
		state.fluidSprite = fluidModel.stillMaterial().sprite();
		FluidTintSource tint = fluidModel.fluidTintSource();
		state.fluidColor = tint != null ? tint.colorAsStack(fluidStack) : 0xFFFFFFFF;
		if (state.fluidSprite == null) {
			state.fluidSprite = Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(AtlasIds.BLOCKS).getSprite(FALLBACK_FLUID_SPRITE);
			state.fluidColor = 0xFFFFFFFF;
		}
	}

	@Override
	public void submit(TankRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		poseStack.pushPose();
		poseStack.translate(0.5D, 1.5D, 0.5D);
		poseStack.scale(-0.9999F, -0.9999F, 0.9999F);
		submitNodeCollector.submitModel(
				tank_model,
				Unit.INSTANCE,
				poseStack,
				RenderTypes.entityTranslucentCullItemTarget(state.texture),
				state.lightCoords,
				OverlayTexture.NO_OVERLAY,
				0,
				state.breakProgress);
		poseStack.popPose();

		if (!state.hasFluid || state.fluidSprite == null) {
			return;
		}

		float red = (state.fluidColor >> 16 & 0xFF) / 255.0F;
		float green = (state.fluidColor >> 8 & 0xFF) / 255.0F;
		float blue = (state.fluidColor & 0xFF) / 255.0F;
		TextureAtlasSprite sprite = state.fluidSprite;
		float height = state.fluidHeight;
		int light = state.lightCoords;

		submitNodeCollector.submitCustomGeometry(poseStack, Sheets.translucentBlockSheet(), (pose, buffer) ->
				RenderHelpers.submitFluidCuboid(pose, buffer, 1.984375F, 0.015625F, 0.015625F, height, 0.015625F, 1.984375F, sprite, red, green, blue, 1F, light));
	}
	public static class TankRenderState extends BlockEntityRenderState {
		public Identifier texture = TANK_TEXTURE;
		public boolean hasFluid;
		public float fluidHeight;
		public int fluidColor;
		public @Nullable TextureAtlasSprite fluidSprite;
	}
}
