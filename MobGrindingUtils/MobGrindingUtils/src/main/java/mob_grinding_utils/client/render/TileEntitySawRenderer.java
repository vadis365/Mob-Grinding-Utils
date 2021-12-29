package mob_grinding_utils.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.blocks.BlockSaw;
import mob_grinding_utils.models.ModelSawBase;
import mob_grinding_utils.models.ModelSawBlade;
import mob_grinding_utils.tile.TileEntitySaw;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import com.mojang.math.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TileEntitySawRenderer extends BlockEntityRenderer<TileEntitySaw> {
	public TileEntitySawRenderer(BlockEntityRenderDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}

	private static final ResourceLocation BASE_TEXTURE = new ResourceLocation("mob_grinding_utils:textures/tiles/saw_base.png");
	private static final ResourceLocation BLADE_TEXTURE = new ResourceLocation("mob_grinding_utils:textures/tiles/saw_blade.png");
	private final ModelSawBase saw_base = new ModelSawBase();
	private final ModelSawBlade saw_blade = new ModelSawBlade();

	@Override
	public void render(TileEntitySaw tile, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
		if(tile == null || !tile.hasLevel())
			return;

		BlockState state = tile.getLevel().getBlockState(tile.getBlockPos());

		if(state == null || state.getBlock() != ModBlocks.SAW.getBlock())
			return;

		Direction facing = state.getValue(BlockSaw.FACING);

		VertexConsumer ivertexbuilder = buffer.getBuffer(RenderType.entitySolid(BASE_TEXTURE));

		matrixStack.pushPose();
		matrixStack.translate(0.5D, 0.5D, 0.5D);
		matrixStack.scale(-1, -1, 1);

		switch (facing) {
		case UP:
			matrixStack.mulPose(Vector3f.YP.rotationDegrees(0F));
			break;
		case DOWN:
			matrixStack.mulPose(Vector3f.XP.rotationDegrees(180F));
			break;
		case NORTH:
			matrixStack.mulPose(Vector3f.XP.rotationDegrees(90F));
			break;
		case SOUTH:
			matrixStack.mulPose(Vector3f.XP.rotationDegrees(-90F));
			break;
		case WEST:
			matrixStack.mulPose(Vector3f.ZP.rotationDegrees(90F));
			break;
		case EAST:
			matrixStack.mulPose(Vector3f.ZP.rotationDegrees(-90F));
			break;
		}
		matrixStack.translate(0F, -1F, 0F);
		saw_base.renderToBuffer(matrixStack, ivertexbuilder, combinedLight, OverlayTexture.NO_OVERLAY, 0.5F, 0.5F, 0.5F, 1.0F);

		matrixStack.pushPose();

		float ticks = tile.animationTicks + (tile.animationTicks - tile.prevAnimationTicks)  * partialTicks;
		matrixStack.mulPose(Vector3f.YP.rotationDegrees(ticks));
		saw_base.renderAxle(matrixStack, ivertexbuilder, combinedLight, OverlayTexture.NO_OVERLAY, 0.5F, 0.5F, 0.5F, 1.0F);
		
		matrixStack.pushPose();
		matrixStack.mulPose(Vector3f.YP.rotationDegrees(45F));
		saw_base.renderMace(matrixStack, ivertexbuilder, combinedLight, OverlayTexture.NO_OVERLAY, 0.5F, 0.5F, 0.5F, 1.0F);
		matrixStack.popPose();
		
		matrixStack.pushPose();
		matrixStack.mulPose(Vector3f.YP.rotationDegrees(165F));
		saw_base.renderMace(matrixStack, ivertexbuilder, combinedLight, OverlayTexture.NO_OVERLAY, 0.5F, 0.5F, 0.5F, 1.0F);
		matrixStack.popPose();
		
		matrixStack.pushPose();
		matrixStack.mulPose(Vector3f.YP.rotationDegrees(285F));
		saw_base.renderMace(matrixStack, ivertexbuilder, combinedLight, OverlayTexture.NO_OVERLAY, 0.5F, 0.5F, 0.5F, 1.0F);
		matrixStack.popPose();
		
		matrixStack.pushPose();
		matrixStack.translate(0F, 0.2F, -0.16F);
		matrixStack.mulPose(Vector3f.XP.rotationDegrees(8F));
		saw_blade.renderToBuffer(matrixStack, buffer.getBuffer(RenderType.entitySolid(BLADE_TEXTURE)), combinedLight, OverlayTexture.NO_OVERLAY, 0.5F, 0.5F, 0.5F, 1.0F);
		matrixStack.popPose();

		matrixStack.pushPose();
		matrixStack.translate(0F, 0.00F, 0.16F);
		matrixStack.mulPose(Vector3f.XP.rotationDegrees(-8F));
		saw_blade.renderToBuffer(matrixStack, buffer.getBuffer(RenderType.entitySolid(BLADE_TEXTURE)), combinedLight, OverlayTexture.NO_OVERLAY, 0.5F, 0.5F, 0.5F, 1.0F);
		matrixStack.popPose();

		matrixStack.pushPose();
		matrixStack.translate(0F, -0.2F, -0.16F);
		matrixStack.mulPose(Vector3f.XP.rotationDegrees(8F));
		saw_blade.renderToBuffer(matrixStack, buffer.getBuffer(RenderType.entitySolid(BLADE_TEXTURE)), combinedLight, OverlayTexture.NO_OVERLAY, 0.5F, 0.5F, 0.5F, 1.0F);
		matrixStack.popPose();

		matrixStack.popPose();
		matrixStack.popPose();

	}
}