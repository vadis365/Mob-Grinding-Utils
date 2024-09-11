package mob_grinding_utils.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.client.ModelLayers;
import mob_grinding_utils.models.ModelAHConnect;
import mob_grinding_utils.tile.TileEntityAbsorptionHopper;
import mob_grinding_utils.tile.TileEntityAbsorptionHopper.EnumStatus;
import mob_grinding_utils.util.RL;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class TileEntityAbsorptionRenderer implements BlockEntityRenderer<TileEntityAbsorptionHopper> {
	private static final ResourceLocation ITEM_TEXTURE = RL.mgu("textures/tiles/absorption_hopper_connects_items.png");
	private static final ResourceLocation FLUID_TEXTURE = RL.mgu("textures/tiles/absorption_hopper_connects_fluids.png");
	private final ModelAHConnect connectionModel;

	public TileEntityAbsorptionRenderer(Context context) {
		connectionModel = new ModelAHConnect(context.bakeLayer(ModelLayers.ABSORPTION_HOPPER));
	}

	@Override
	public void render(TileEntityAbsorptionHopper tile, float partialTicks, @Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
		if (tile == null || !tile.hasLevel())
			return;

		BlockState state = tile.getLevel().getBlockState(tile.getBlockPos());

		if(state == null || state.getBlock() != ModBlocks.ABSORPTION_HOPPER.getBlock())
			return;

		matrixStack.pushPose();
		matrixStack.translate(0.5D, 0.5D, 0.5D);
		for (Direction facing : Direction.values()) {
			if (tile.status[facing.ordinal()] == EnumStatus.STATUS_OUTPUT_ITEM) {
				matrixStack.pushPose();
				getRotTranslation(matrixStack, facing);
				connectionModel.renderToBuffer(matrixStack, buffer.getBuffer(RenderType.entitySolid(ITEM_TEXTURE)), combinedLight, OverlayTexture.NO_OVERLAY, 0.5F, 0.5F, 0.5F, 1.0F);
				matrixStack.popPose();
			}
			if (tile.status[facing.ordinal()] == EnumStatus.STATUS_OUTPUT_FLUID) {
				matrixStack.pushPose();
				getRotTranslation(matrixStack, facing);
				connectionModel.renderToBuffer(matrixStack, buffer.getBuffer(RenderType.entitySolid(FLUID_TEXTURE)), combinedLight, OverlayTexture.NO_OVERLAY, 0.5F, 0.5F, 0.5F, 1.0F);
				matrixStack.popPose();
			}
		}
		matrixStack.popPose();

		if (!tile.showRenderBox)
			return;
		matrixStack.pushPose();
		matrixStack.translate(-0.0005D, -0.0005D, -0.0005D);
		matrixStack.scale(0.999F, 0.999F, 0.999F);

		// TODO could be this now?
		//DebugRenderer.renderBox(tile.getAABBForRender(), 0F, 0F, 1F, 0.75F);

		LevelRenderer.renderLineBox(matrixStack, buffer.getBuffer(RenderType.lines()), tile.getAABBForRender(), 1F, 1F, 0F, 1F);
		matrixStack.popPose();
	}

	public void getRotTranslation(PoseStack matrixStack, Direction facing) {
		switch (facing) {
		case UP:
			matrixStack.mulPose(Axis.XP.rotationDegrees(180F));
			break;
		case DOWN:
			break;
		case NORTH:
			matrixStack.mulPose(Axis.XP.rotationDegrees(90F));
			break;
		case SOUTH:
			matrixStack.mulPose(Axis.XN.rotationDegrees(90F));
			break;
		case WEST:
			matrixStack.mulPose(Axis.ZN.rotationDegrees(90F));
			break;
		case EAST:
			matrixStack.mulPose(Axis.ZP.rotationDegrees(90F));
			break;
		}
	}

	@Override
	public AABB getRenderBoundingBox(TileEntityAbsorptionHopper blockEntity) {
		return blockEntity.getAABBWithModifiers();
	}
}