package mob_grinding_utils.client.render;

import mob_grinding_utils.tile.TileEntityAbsorptionHopper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TileEntityAbsorptionRenderer extends TileEntityRenderer<TileEntityAbsorptionHopper> {

	@Override
	public void render(TileEntityAbsorptionHopper tile, double x, double y, double z, float partialTicks, int destroyStage) {
		if (tile == null || !tile.hasWorld())
			return;

		if (!tile.showRenderBox)
			return;
		GlStateManager.pushMatrix();
		GlStateManager.translated(x-0.0005D, y-0.0005D, z-0.0005D);
		GlStateManager.scaled(0.999D, 0.999D, 0.999D);
		GlStateManager.depthMask(false);
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.disableLighting();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
		GlStateManager.disableCull();
		int i = 61680;
		int j = i % 65536;
		int k = i / 65536;
		OpenGlHelper.glMultiTexCoord2f(OpenGlHelper.GL_TEXTURE1, (float)j, (float)k);
		RenderGlobal.renderFilledBox(tile.getAABBForRender(), 0F, 1F, 0F, 0.75F);
		RenderGlobal.drawSelectionBoundingBox(tile.getAABBForRender(), 1F, 1F, 0F, 1F);
		GlStateManager.enableCull();
		GlStateManager.enableLighting();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
		GlStateManager.depthMask(true);
		GlStateManager.popMatrix();
	}
}