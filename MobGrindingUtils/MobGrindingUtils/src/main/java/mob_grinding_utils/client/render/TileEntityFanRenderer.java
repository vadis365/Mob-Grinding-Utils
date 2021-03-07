package mob_grinding_utils.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import mob_grinding_utils.tile.TileEntityFan;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TileEntityFanRenderer extends TileEntityRenderer<TileEntityFan> {

	public TileEntityFanRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void render(TileEntityFan tile, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
		if (tile == null || !tile.hasWorld())
			return;

		if (!tile.showRenderBox)
			return;
		IVertexBuilder ivertexbuilder = buffer.getBuffer(RenderType.getLines());
		matrixStack.push();
		matrixStack.translate(-0.0005D, -0.0005D, -0.0005D);
		matrixStack.scale(0.999F, 0.999F, 0.999F);
/*		GlStateManager.depthMask(false);
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.disableLighting();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
		GlStateManager.disableCull();
		int i = 61680;
		int j = i % 65536;
		int k = i / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j, (float) k);
		WorldRenderer.renderFilledBox(tile.getAABBForRender(), 0F, 0F, 1F, 0.75F);

*/
		WorldRenderer.drawBoundingBox(matrixStack, ivertexbuilder, tile.getAABBForRender(), 0F, 0F, 1F, 1F);
/*
		GlStateManager.enableCull();
		GlStateManager.enableLighting();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
		GlStateManager.depthMask(true);
*/
		matrixStack.pop();
		
	}
	
	
}