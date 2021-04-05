package mob_grinding_utils.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import mob_grinding_utils.tile.TileEntityMGUSpawner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TileEntityMGUSpawnerRenderer extends TileEntityRenderer<TileEntityMGUSpawner> {

	public TileEntityMGUSpawnerRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}

	@Override
	public void render(TileEntityMGUSpawner tile, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer,
			int combinedLight, int combinedOverlay) {
		if (tile == null || !tile.hasWorld())
			return;

		if (tile.isOn && tile.hasSpawnEggItem() && tile.getEntityToRender() != null) {
			float ticks = tile.animationTicks + (tile.animationTicks - tile.prevAnimationTicks)  * partialTicks;
			Entity entity = tile.getEntityToRender();
			matrixStack.push();
			matrixStack.translate(0.5D, 0.75D, 0.5D);
			RenderSystem.enableBlend();
			RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, 0.65F);
			matrixStack.rotate(Vector3f.YP.rotationDegrees(ticks));
			matrixStack.scale(0.125F, 0.125F, 0.125F);
			Minecraft.getInstance().getRenderManager().renderEntityStatic(entity, 0D, 0D, 0D, 0F, 0F, matrixStack, buffer, combinedLight);
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			matrixStack.pop();
		}

		if (!tile.showRenderBox)
			return;

		matrixStack.push();
		matrixStack.translate(-0.0005D, -0.0005D, -0.0005D);
		matrixStack.scale(0.999F, 0.999F, 0.999F);

		WorldRenderer.drawBoundingBox(matrixStack, buffer.getBuffer(RenderType.getLines()), tile.getAABBForRender(), 1F, 0F, 0F, 1F);
		matrixStack.pop();
	}

}