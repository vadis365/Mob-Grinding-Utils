package mob_grinding_utils.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.blocks.BlockSaw;
import mob_grinding_utils.models.ModelSawBase;
import mob_grinding_utils.models.ModelSawBlade;
import mob_grinding_utils.tile.TileEntitySaw;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TileEntitySawRenderer extends TileEntityRenderer<TileEntitySaw> {
	public TileEntitySawRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}

	private static final ResourceLocation BASE_TEXTURE = new ResourceLocation("mob_grinding_utils:textures/tiles/saw_base.png");
	private static final ResourceLocation BLADE_TEXTURE = new ResourceLocation("mob_grinding_utils:textures/tiles/saw_blade.png");
	private final ModelSawBase saw_base = new ModelSawBase();
	private final ModelSawBlade saw_blade = new ModelSawBlade();

	@Override
	public void render(TileEntitySaw tile, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
		if(tile == null || !tile.hasWorld())
			return;

		BlockState state = tile.getWorld().getBlockState(tile.getPos());

		if(state == null || state.getBlock() != ModBlocks.SAW.getBlock())
			return;

		Direction facing = state.get(BlockSaw.FACING);

		IVertexBuilder ivertexbuilder = buffer.getBuffer(RenderType.getEntitySolid(BASE_TEXTURE));

		matrixStack.push();
		matrixStack.translate(0.5D, 0.5D, 0.5D);
		matrixStack.scale(-1, -1, 1);

		switch (facing) {
		case UP:
			matrixStack.rotate(Vector3f.YP.rotationDegrees(0F));
			break;
		case DOWN:
			matrixStack.rotate(Vector3f.XP.rotationDegrees(180F));
			break;
		case NORTH:
			matrixStack.rotate(Vector3f.XP.rotationDegrees(90F));
			break;
		case SOUTH:
			matrixStack.rotate(Vector3f.XP.rotationDegrees(-90F));
			break;
		case WEST:
			matrixStack.rotate(Vector3f.ZP.rotationDegrees(90F));
			break;
		case EAST:
			matrixStack.rotate(Vector3f.ZP.rotationDegrees(-90F));
			break;
		}
		matrixStack.translate(0F, -1F, 0F);
		saw_base.render(matrixStack, ivertexbuilder, combinedLight, OverlayTexture.NO_OVERLAY, 0.5F, 0.5F, 0.5F, 1.0F);

		matrixStack.push();

		float ticks = tile.animationTicks + (tile.animationTicks - tile.prevAnimationTicks)  * partialTicks;
		matrixStack.rotate(Vector3f.YP.rotationDegrees(ticks));
		saw_base.renderAxle(matrixStack, ivertexbuilder, combinedLight, OverlayTexture.NO_OVERLAY, 0.5F, 0.5F, 0.5F, 1.0F);
		
		matrixStack.push();
		matrixStack.rotate(Vector3f.YP.rotationDegrees(45F));
		saw_base.renderMace(matrixStack, ivertexbuilder, combinedLight, OverlayTexture.NO_OVERLAY, 0.5F, 0.5F, 0.5F, 1.0F);
		matrixStack.pop();
		
		matrixStack.push();
		matrixStack.rotate(Vector3f.YP.rotationDegrees(165F));
		saw_base.renderMace(matrixStack, ivertexbuilder, combinedLight, OverlayTexture.NO_OVERLAY, 0.5F, 0.5F, 0.5F, 1.0F);
		matrixStack.pop();
		
		matrixStack.push();
		matrixStack.rotate(Vector3f.YP.rotationDegrees(285F));
		saw_base.renderMace(matrixStack, ivertexbuilder, combinedLight, OverlayTexture.NO_OVERLAY, 0.5F, 0.5F, 0.5F, 1.0F);
		matrixStack.pop();
		
		matrixStack.push();
		matrixStack.translate(0F, 0.2F, -0.16F);
		matrixStack.rotate(Vector3f.XP.rotationDegrees(8F));
		saw_blade.render(matrixStack, buffer.getBuffer(RenderType.getEntitySolid(BLADE_TEXTURE)), combinedLight, OverlayTexture.NO_OVERLAY, 0.5F, 0.5F, 0.5F, 1.0F);
		matrixStack.pop();

		matrixStack.push();
		matrixStack.translate(0F, 0.00F, 0.16F);
		matrixStack.rotate(Vector3f.XP.rotationDegrees(-8F));
		saw_blade.render(matrixStack, buffer.getBuffer(RenderType.getEntitySolid(BLADE_TEXTURE)), combinedLight, OverlayTexture.NO_OVERLAY, 0.5F, 0.5F, 0.5F, 1.0F);
		matrixStack.pop();

		matrixStack.push();
		matrixStack.translate(0F, -0.2F, -0.16F);
		matrixStack.rotate(Vector3f.XP.rotationDegrees(8F));
		saw_blade.render(matrixStack, buffer.getBuffer(RenderType.getEntitySolid(BLADE_TEXTURE)), combinedLight, OverlayTexture.NO_OVERLAY, 0.5F, 0.5F, 0.5F, 1.0F);
		matrixStack.pop();

		matrixStack.pop();
		matrixStack.pop();

	}
}