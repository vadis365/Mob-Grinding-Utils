package mob_grinding_utils.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import mob_grinding_utils.models.ModelSawBase;
import mob_grinding_utils.models.ModelSawBlade;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
@OnlyIn(Dist.CLIENT)
public class TileSawStackItemRenderer extends ItemStackTileEntityRenderer {
	private static final ResourceLocation BASE_TEXTURE = new ResourceLocation("mob_grinding_utils:textures/tiles/saw_base.png");
	private static final ResourceLocation BLADE_TEXTURE = new ResourceLocation("mob_grinding_utils:textures/tiles/saw_blade.png");
	private final ModelSawBase saw_base = new ModelSawBase();
	private final ModelSawBlade saw_blade = new ModelSawBlade();
	@Override
	public void func_239207_a_(ItemStack itemStackIn, TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlayIn) {
		IVertexBuilder ivertexbuilder = buffer.getBuffer(RenderType.getEntitySolid(BASE_TEXTURE));
		matrixStack.push();
		matrixStack.translate(0.5D, 1.5D, 0.5D);
		matrixStack.scale(-1, -1, 1);
		saw_base.render(matrixStack, ivertexbuilder, combinedLight, OverlayTexture.NO_OVERLAY, 0.5F, 0.5F, 0.5F, 1.0F);
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

	}
}
