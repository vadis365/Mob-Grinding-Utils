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
public class ModelSawBase extends Model {
    ModelPart axle;
    ModelPart axle2;
    ModelPart axleTop;
    ModelPart plinth;
    ModelPart base;
    ModelPart maceBase;
    ModelPart maceArm;
    ModelPart mace1;
    ModelPart mace2;
    ModelPart mace3;
    ModelPart mace4;

	public ModelSawBase() {
		super(RenderType::entitySolid);
	    texWidth = 64;
	    texHeight = 32;
	    
	      axle = new ModelPart(this, 0, 0);
	      axle.addBox(-1F, -5F, -1F, 2, 10, 2);
	      axle.setPos(0F, 16F, 0F);
	      setRotation(axle, 0F, 0F, 0F);
	      axle2 = new ModelPart(this, 0, 0);
	      axle2.addBox(-1F, -5F, -1F, 2, 10, 2);
	      axle2.setPos(0F, 16F, 0F);
	      setRotation(axle2, 0F, 0.7853982F, 0F);
	      axleTop = new ModelPart(this, 0, 21);
	      axleTop.addBox(-1.5F, -8F, -1.5F, 3, 3, 3);
	      axleTop.setPos(0F, 16F, 0F);
	      setRotation(axleTop, 0F, 0.7853982F, 0F);
	      plinth = new ModelPart(this, 9, 0);
	      plinth.addBox(-5.5F, 5F, -5.5F, 11, 2, 11);
	      plinth.setPos(0F, 16F, 0F);
	      setRotation(plinth, 0F, 0.7853982F, 0F);
	      base = new ModelPart(this, 0, 15);
	      base.addBox(-8F, 7F, -8F, 16, 1, 16);
	      base.setPos(0F, 16F, 0F);
	      setRotation(base, 0F, 0F, 0F);
	      maceBase = new ModelPart(this, 0, 16);
	      maceBase.addBox(-3.5F, -7.5F, -1F, 2, 2, 2);
	      maceBase.setPos(0F, 16F, 0F);
	      setRotation(maceBase, 0F, -0.7853982F, 0F);
	      maceArm = new ModelPart(this, 9, 16);
	      maceArm.addBox(-5.5F, -7F, -0.5F, 2, 1, 1);
	      maceArm.setPos(0F, 16F, 0F);
	      setRotation(maceArm, 0F, -0.7853982F, 0F);
	      mace1 = new ModelPart(this, 0, 21);
	      mace1.addBox(-1.5F, -8F, -8.5F, 3, 3, 3);
	      mace1.setPos(0F, 16F, 0F);
	      setRotation(mace1, 0F, 0.7853982F, 0F);
	      mace2 = new ModelPart(this, 0, 21);
	      mace2.addBox(-9.5F, -2.5F, -6.5F, 3, 3, 3);
	      mace2.setPos(0F, 16F, 0F);
	      setRotation(mace2, 0F, 0F, 0.7853982F);
	      mace3 = new ModelPart(this, 0, 21);
	      mace3.addBox(-6.5F, -9.5F, -0.5F, 3, 3, 3);
	      mace3.setPos(0F, 16F, 0F);
	      setRotation(mace3, 0.7853982F, 0F, 0F);
	      mace4 = new ModelPart(this, 0, 21);
	      mace4.addBox(-6.5F, -8F, -6.5F, 3, 3, 3);
	      mace4.setPos(0F, 16F, 0F);
	      setRotation(mace4, 0F, 0F, 0F);
	}

	public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		ImmutableList.of(base, plinth)
		.forEach((p_228279_8_) -> {
			p_228279_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		});
	}

	public void renderAxle(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		ImmutableList.of(axle, axle2, axleTop)
		.forEach((p_228279_8_) -> {
			p_228279_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		});
	}
	
	public void renderMace(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		ImmutableList.of(maceBase, maceArm, mace1, mace2, mace3, mace4)
		.forEach((p_228279_8_) -> {
			p_228279_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		});
	}

	private void setRotation(ModelPart model, float x, float y, float z) {
		model.xRot = x;
		model.yRot = y;
		model.zRot = z;
	}

}
