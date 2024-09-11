package mob_grinding_utils.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.blocks.BlockSaw;
import mob_grinding_utils.client.ModelLayers;
import mob_grinding_utils.models.ModelSawBase;
import mob_grinding_utils.models.ModelSawBlade;
import mob_grinding_utils.tile.TileEntitySaw;
import mob_grinding_utils.util.RL;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class TileEntitySawRenderer implements BlockEntityRenderer<TileEntitySaw> {

	private static final ResourceLocation BASE_TEXTURE = RL.mgu("textures/tiles/saw_base.png");
	private static final ResourceLocation BLADE_TEXTURE = RL.mgu("textures/tiles/saw_blade.png");
	private final ModelSawBase saw_base;
	private final ModelSawBlade saw_blade;

	public TileEntitySawRenderer(Context context) {
		saw_base = new ModelSawBase(context.bakeLayer(ModelLayers.SAW_BASE)); 
		saw_blade = new ModelSawBlade(context.bakeLayer(ModelLayers.SAW_BLADE));
	}

	@Override
	public void render(@Nonnull TileEntitySaw tile, float partialTicks, @Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
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
			case UP -> matrixStack.mulPose(Axis.YP.rotationDegrees(0F));
			case DOWN -> matrixStack.mulPose(Axis.XP.rotationDegrees(180F));
			case NORTH -> matrixStack.mulPose(Axis.XP.rotationDegrees(90F));
			case SOUTH -> matrixStack.mulPose(Axis.XP.rotationDegrees(-90F));
			case WEST -> matrixStack.mulPose(Axis.ZP.rotationDegrees(90F));
			case EAST -> matrixStack.mulPose(Axis.ZP.rotationDegrees(-90F));
		}
		matrixStack.translate(0F, -1F, 0F);
		saw_base.renderToBuffer(matrixStack, ivertexbuilder, combinedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1.0F);

		matrixStack.pushPose();

		float ticks = tile.animationTicks + (tile.animationTicks - tile.prevAnimationTicks)  * partialTicks;
		matrixStack.mulPose(Axis.YP.rotationDegrees(ticks));
		saw_base.renderAxle(matrixStack, ivertexbuilder, combinedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1.0F);
		
		matrixStack.pushPose();
		matrixStack.mulPose(Axis.YP.rotationDegrees(45F));
		saw_base.renderMace(matrixStack, ivertexbuilder, combinedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1.0F);
		matrixStack.popPose();
		
		matrixStack.pushPose();
		matrixStack.mulPose(Axis.YP.rotationDegrees(165F));
		saw_base.renderMace(matrixStack, ivertexbuilder, combinedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1.0F);
		matrixStack.popPose();
		
		matrixStack.pushPose();
		matrixStack.mulPose(Axis.YP.rotationDegrees(285F));
		saw_base.renderMace(matrixStack, ivertexbuilder, combinedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1.0F);
		matrixStack.popPose();
		
		matrixStack.pushPose();
		matrixStack.translate(0F, 0.2F, -0.16F);
		matrixStack.mulPose(Axis.XP.rotationDegrees(8F));
		saw_blade.renderToBuffer(matrixStack, buffer.getBuffer(RenderType.entitySolid(BLADE_TEXTURE)), combinedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1.0F);
		matrixStack.popPose();

		matrixStack.pushPose();
		matrixStack.translate(0F, 0.00F, 0.16F);
		matrixStack.mulPose(Axis.XP.rotationDegrees(-8F));
		saw_blade.renderToBuffer(matrixStack, buffer.getBuffer(RenderType.entitySolid(BLADE_TEXTURE)), combinedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1.0F);
		matrixStack.popPose();

		matrixStack.pushPose();
		matrixStack.translate(0F, -0.2F, -0.16F);
		matrixStack.mulPose(Axis.XP.rotationDegrees(8F));
		saw_blade.renderToBuffer(matrixStack, buffer.getBuffer(RenderType.entitySolid(BLADE_TEXTURE)), combinedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1.0F);
		matrixStack.popPose();

		matrixStack.popPose();
		matrixStack.popPose();

	}
}