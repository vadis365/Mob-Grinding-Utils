package mob_grinding_utils.models;
//Paste this code into your mod.

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
@OnlyIn(Dist.CLIENT)
public class ModelTankBlock extends Model {
	ModelPart tank_box;

	public ModelTankBlock() {
		super(RenderType::entitySolid);
		texWidth = 64;
		texHeight = 32;

		tank_box = new ModelPart(this, 0, 0);
		tank_box.addBox(-8.0F, -16.0F, -8.0F, 16, 16, 16);
		tank_box.setPos(0.0F, 24.0F, 0.0F);
	}

	@Override
	public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		tank_box.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}
}