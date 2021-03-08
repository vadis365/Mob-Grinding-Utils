package mob_grinding_utils.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;

import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.models.ModelAHConnect;
import mob_grinding_utils.tile.TileEntityAbsorptionHopper;
import mob_grinding_utils.tile.TileEntityAbsorptionHopper.EnumStatus;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TileEntityAbsorptionRenderer extends TileEntityRenderer<TileEntityAbsorptionHopper> {
	private static final ResourceLocation ITEM_TEXTURE = new ResourceLocation("mob_grinding_utils:textures/tiles/absorption_hopper_connects_items.png");
	private static final ResourceLocation FLUID_TEXTURE = new ResourceLocation("mob_grinding_utils:textures/tiles/absorption_hopper_connects_fluids.png");
	private final ModelAHConnect connectionModel = new ModelAHConnect();

	public TileEntityAbsorptionRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}

	@Override
	public void render(TileEntityAbsorptionHopper tile, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
		if (tile == null || !tile.hasWorld())
			return;

		BlockState state = tile.getWorld().getBlockState(tile.getPos());

		if(state == null || state.getBlock() != ModBlocks.ABSORPTION_HOPPER)
			return;

		matrixStack.push();
		matrixStack.translate(0.5D, 0.5D, 0.5D);
		for (Direction facing : Direction.values()) {
			if (tile.status[facing.ordinal()] == EnumStatus.STATUS_OUTPUT_ITEM) {
				matrixStack.push();
				getRotTranslation(matrixStack, facing);
				connectionModel.render(matrixStack, buffer.getBuffer(RenderType.getEntitySolid(ITEM_TEXTURE)), combinedLight, OverlayTexture.NO_OVERLAY, 0.5F, 0.5F, 0.5F, 1.0F);
				matrixStack.pop();
			}
			if (tile.status[facing.ordinal()] == EnumStatus.STATUS_OUTPUT_FLUID) {
				matrixStack.push();
				getRotTranslation(matrixStack, facing);
				connectionModel.render(matrixStack, buffer.getBuffer(RenderType.getEntitySolid(FLUID_TEXTURE)), combinedLight, OverlayTexture.NO_OVERLAY, 0.5F, 0.5F, 0.5F, 1.0F);
				matrixStack.pop();
			}
		}
		matrixStack.pop();

		if (!tile.showRenderBox)
			return;
		matrixStack.push();
		matrixStack.translate(-0.0005D, -0.0005D, -0.0005D);
		matrixStack.scale(0.999F, 0.999F, 0.999F);

		// TODO could be this now?
		//DebugRenderer.renderBox(tile.getAABBForRender(), 0F, 0F, 1F, 0.75F);

		WorldRenderer.drawBoundingBox(matrixStack, buffer.getBuffer(RenderType.getLines()), tile.getAABBForRender(), 1F, 1F, 0F, 1F);
		matrixStack.pop();
	}

	public void getRotTranslation(MatrixStack matrixStack, Direction facing) {
		switch (facing) {
		case UP:
			matrixStack.rotate(Vector3f.XP.rotationDegrees(180F));
			break;
		case DOWN:
			break;
		case NORTH:
			matrixStack.rotate(Vector3f.XP.rotationDegrees(90F));
			break;
		case SOUTH:
			matrixStack.rotate(Vector3f.XN.rotationDegrees(90F));
			break;
		case WEST:
			matrixStack.rotate(Vector3f.ZN.rotationDegrees(90F));
			break;
		case EAST:
			matrixStack.rotate(Vector3f.ZP.rotationDegrees(90F));
			break;
		}
	}
}