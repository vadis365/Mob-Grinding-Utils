package mob_grinding_utils.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mob_grinding_utils.tile.TileEntityMGUSpawner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class TileEntityMGUSpawnerRenderer implements BlockEntityRenderer<TileEntityMGUSpawner> {

	public TileEntityMGUSpawnerRenderer(Context context) {
	}

	@Override
	public void render(@Nonnull TileEntityMGUSpawner tile, float partialTicks, @Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
		if (tile == null || !tile.hasLevel())
			return;

		if (tile.isOn && tile.hasSpawnEggItem() && tile.getEntityToRender() != null) {
			float ticks = tile.animationTicks + (tile.animationTicks - tile.prevAnimationTicks)  * partialTicks;
			Entity entity = tile.getEntityToRender();
			matrixStack.pushPose();
			matrixStack.translate(0.5D, 0.75D, 0.5D);
			RenderSystem.enableBlend();
			
			RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			//RenderSystem.color4f(1.0F, 1.0F, 1.0F, 0.65F);
			matrixStack.mulPose(Axis.YP.rotationDegrees(ticks));
			matrixStack.scale(0.125F, 0.125F, 0.125F);
			Minecraft.getInstance().getEntityRenderDispatcher().render(entity, 0D, 0D, 0D, 0F, 0F, matrixStack, buffer, combinedLight);
			//RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			matrixStack.popPose();
		}

		if (!tile.showRenderBox)
			return;

		matrixStack.pushPose();
		matrixStack.translate(-0.0005D, -0.0005D, -0.0005D);
		matrixStack.scale(0.999F, 0.999F, 0.999F);

		LevelRenderer.renderLineBox(matrixStack, buffer.getBuffer(RenderType.lines()), tile.getAABBForRender(), 1F, 0F, 0F, 1F);
		matrixStack.popPose();
	}

	@Override
	public AABB getRenderBoundingBox(TileEntityMGUSpawner blockEntity) {
		return blockEntity.getAABBWithModifiers();
	}
}