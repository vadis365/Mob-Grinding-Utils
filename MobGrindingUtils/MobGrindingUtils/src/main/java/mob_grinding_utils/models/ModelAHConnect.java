package mob_grinding_utils.models;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


@OnlyIn(Dist.CLIENT)
public class ModelAHConnect extends Model {
	ModelRenderer plate;
	ModelRenderer pipe;

	public ModelAHConnect() {
		super(RenderType::getEntitySolid);
		textureWidth = 32;
		textureHeight = 16;
		plate = new ModelRenderer(this, 0, 0);
		plate.addBox(-3.0F, -12.0F, -3.0F, 6, 1, 6);
		plate.setRotationPoint(0.0F, 7.0F, 0.0F);
		setRotation(plate, 0F, 0F, 0F);
		pipe = new ModelRenderer(this, 0, 7);
		pipe.addBox(-2.0F, -15.0F, -2.0F, 4, 3, 4);
		pipe.setRotationPoint(0.0F, 7.0F, 0.0F);
		setRotation(pipe, 0F, 0F, 0F);
	}

	@Override
	public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		ImmutableList.of(plate, pipe).forEach((modelRenderer) -> {
			modelRenderer.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		});
	}

	public void setRotation(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}