package mob_grinding_utils.models;
//Paste this code into your mod.

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
@OnlyIn(Dist.CLIENT)
public class ModelTankBlock extends Model {
	ModelRenderer tank_box;

	public ModelTankBlock() {
		super(RenderType::getEntitySolid);
		textureWidth = 64;
		textureHeight = 32;

		tank_box = new ModelRenderer(this, 0, 0);
		tank_box.addBox(-8.0F, -16.0F, -8.0F, 16, 16, 16);
		tank_box.setRotationPoint(0.0F, 24.0F, 0.0F);
	}

	@Override
	public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		tank_box.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}
}