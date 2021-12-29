package mob_grinding_utils.models;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


@OnlyIn(Dist.CLIENT)
public class ModelAHConnect extends Model {
	ModelPart plate;
	ModelPart pipe;

	public ModelAHConnect() {
		super(RenderType::entitySolid);
		texWidth = 32;
		texHeight = 16;
		plate = new ModelPart(this, 0, 0);
		plate.addBox(-3.0F, -12.0F, -3.0F, 6, 1, 6);
		plate.setPos(0.0F, 7.0F, 0.0F);
		setRotation(plate, 0F, 0F, 0F);
		pipe = new ModelPart(this, 0, 7);
		pipe.addBox(-2.0F, -15.0F, -2.0F, 4, 3, 4);
		pipe.setPos(0.0F, 7.0F, 0.0F);
		setRotation(pipe, 0F, 0F, 0F);
	}

	@Override
	public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		ImmutableList.of(plate, pipe).forEach((modelRenderer) -> {
			modelRenderer.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		});
	}

	public void setRotation(ModelPart modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}