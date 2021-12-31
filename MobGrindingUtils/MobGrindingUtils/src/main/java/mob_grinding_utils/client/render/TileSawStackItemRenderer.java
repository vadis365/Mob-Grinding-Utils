package mob_grinding_utils.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;

import mob_grinding_utils.client.ModelLayers;
import mob_grinding_utils.models.ModelSawBase;
import mob_grinding_utils.models.ModelSawBlade;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
@OnlyIn(Dist.CLIENT)
public class TileSawStackItemRenderer extends BlockEntityWithoutLevelRenderer {

	private static final ResourceLocation BASE_TEXTURE = new ResourceLocation("mob_grinding_utils:textures/tiles/saw_base.png");
	private static final ResourceLocation BLADE_TEXTURE = new ResourceLocation("mob_grinding_utils:textures/tiles/saw_blade.png");
	private final ModelSawBase saw_base;
	private final ModelSawBlade saw_blade;

	public TileSawStackItemRenderer(BlockEntityRenderDispatcher renderer, EntityModelSet modelSet) {
		super(renderer, modelSet);
	    EntityModelSet EntityModelSetThatIsntNULL = Minecraft.getInstance().getEntityModels();
		saw_base = new ModelSawBase(EntityModelSetThatIsntNULL.bakeLayer(ModelLayers.SAW_BASE)); 
		saw_blade = new ModelSawBlade(EntityModelSetThatIsntNULL.bakeLayer(ModelLayers.SAW_BLADE));
	}

	@Override
	public void renderByItem(ItemStack itemStackIn, TransformType transformType, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlayIn) {
		VertexConsumer ivertexbuilder = buffer.getBuffer(RenderType.entitySolid(BASE_TEXTURE));
		matrixStack.pushPose();
		matrixStack.translate(0.5D, 1.5D, 0.5D);
		matrixStack.scale(-1, -1, 1);
		saw_base.renderToBuffer(matrixStack, ivertexbuilder, combinedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1.0F);
		saw_base.renderAxle(matrixStack, ivertexbuilder, combinedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1.0F);
		
		matrixStack.pushPose();
		matrixStack.mulPose(Vector3f.YP.rotationDegrees(45F));
		saw_base.renderMace(matrixStack, ivertexbuilder, combinedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1.0F);
		matrixStack.popPose();
		
		matrixStack.pushPose();
		matrixStack.mulPose(Vector3f.YP.rotationDegrees(165F));
		saw_base.renderMace(matrixStack, ivertexbuilder, combinedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1.0F);
		matrixStack.popPose();
		
		matrixStack.pushPose();
		matrixStack.mulPose(Vector3f.YP.rotationDegrees(285F));
		saw_base.renderMace(matrixStack, ivertexbuilder, combinedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1.0F);
		matrixStack.popPose();

		matrixStack.pushPose();
		matrixStack.translate(0F, 0.2F, -0.16F);
		matrixStack.mulPose(Vector3f.XP.rotationDegrees(8F));
		saw_blade.renderToBuffer(matrixStack, buffer.getBuffer(RenderType.entitySolid(BLADE_TEXTURE)), combinedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1.0F);
		matrixStack.popPose();

		matrixStack.pushPose();
		matrixStack.translate(0F, 0.00F, 0.16F);
		matrixStack.mulPose(Vector3f.XP.rotationDegrees(-8F));
		saw_blade.renderToBuffer(matrixStack, buffer.getBuffer(RenderType.entitySolid(BLADE_TEXTURE)), combinedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1.0F);
		matrixStack.popPose();

		matrixStack.pushPose();
		matrixStack.translate(0F, -0.2F, -0.16F);
		matrixStack.mulPose(Vector3f.XP.rotationDegrees(8F));
		saw_blade.renderToBuffer(matrixStack, buffer.getBuffer(RenderType.entitySolid(BLADE_TEXTURE)), combinedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1.0F);
		matrixStack.popPose();

		matrixStack.popPose();

	}
}
