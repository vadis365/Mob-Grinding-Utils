package mob_grinding_utils.client.render;

import mob_grinding_utils.tile.TileEntityAbsorptionHopper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntityAbsorptionRenderer extends TileEntitySpecialRenderer<TileEntityAbsorptionHopper> {

	@Override
	public void render(TileEntityAbsorptionHopper tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if (tile == null || !tile.hasWorld())
			return;

		if (!tile.showRenderBox)
			return;
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.scale(0.999D, 0.999D, 0.999D);
		GlStateManager.depthMask(false);
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.disableLighting();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.disableCull();
		RenderGlobal.renderFilledBox(tile.getAABBForRender(), 0F, 0F, 1F, 0.125F);
		RenderGlobal.drawSelectionBoundingBox(tile.getAABBForRender(), 1F, 1F, 1F, 0.25F);
		GlStateManager.enableCull();
		GlStateManager.enableLighting();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
		GlStateManager.depthMask(true);
		GlStateManager.popMatrix();
	}
}