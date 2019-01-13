package mob_grinding_utils.models;

import net.minecraft.client.renderer.entity.model.ModelBase;
import net.minecraft.client.renderer.entity.model.ModelRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelSawBase extends ModelBase {
    ModelRenderer axle;
    ModelRenderer axle2;
    ModelRenderer axleTop;
    ModelRenderer plinth;
    ModelRenderer base;
    ModelRenderer maceBase;
    ModelRenderer maceArm;
    ModelRenderer mace1;
    ModelRenderer mace2;
    ModelRenderer mace3;
    ModelRenderer mace4;

	public ModelSawBase() {
	    textureWidth = 64;
	    textureHeight = 32;
	    
	      axle = new ModelRenderer(this, 0, 0);
	      axle.addBox(-1F, -5F, -1F, 2, 10, 2);
	      axle.setRotationPoint(0F, 16F, 0F);
	      setRotation(axle, 0F, 0F, 0F);
	      axle2 = new ModelRenderer(this, 0, 0);
	      axle2.addBox(-1F, -5F, -1F, 2, 10, 2);
	      axle2.setRotationPoint(0F, 16F, 0F);
	      setRotation(axle2, 0F, 0.7853982F, 0F);
	      axleTop = new ModelRenderer(this, 0, 21);
	      axleTop.addBox(-1.5F, -8F, -1.5F, 3, 3, 3);
	      axleTop.setRotationPoint(0F, 16F, 0F);
	      setRotation(axleTop, 0F, 0.7853982F, 0F);
	      plinth = new ModelRenderer(this, 9, 0);
	      plinth.addBox(-5.5F, 5F, -5.5F, 11, 2, 11);
	      plinth.setRotationPoint(0F, 16F, 0F);
	      setRotation(plinth, 0F, 0.7853982F, 0F);
	      base = new ModelRenderer(this, 0, 15);
	      base.addBox(-8F, 7F, -8F, 16, 1, 16);
	      base.setRotationPoint(0F, 16F, 0F);
	      setRotation(base, 0F, 0F, 0F);
	      maceBase = new ModelRenderer(this, 0, 16);
	      maceBase.addBox(-3.5F, -7.5F, -1F, 2, 2, 2);
	      maceBase.setRotationPoint(0F, 16F, 0F);
	      setRotation(maceBase, 0F, -0.7853982F, 0F);
	      maceArm = new ModelRenderer(this, 9, 16);
	      maceArm.addBox(-5.5F, -7F, -0.5F, 2, 1, 1);
	      maceArm.setRotationPoint(0F, 16F, 0F);
	      setRotation(maceArm, 0F, -0.7853982F, 0F);
	      mace1 = new ModelRenderer(this, 0, 21);
	      mace1.addBox(-1.5F, -8F, -8.5F, 3, 3, 3);
	      mace1.setRotationPoint(0F, 16F, 0F);
	      setRotation(mace1, 0F, 0.7853982F, 0F);
	      mace2 = new ModelRenderer(this, 0, 21);
	      mace2.addBox(-9.5F, -2.5F, -6.5F, 3, 3, 3);
	      mace2.setRotationPoint(0F, 16F, 0F);
	      setRotation(mace2, 0F, 0F, 0.7853982F);
	      mace3 = new ModelRenderer(this, 0, 21);
	      mace3.addBox(-6.5F, -9.5F, -0.5F, 3, 3, 3);
	      mace3.setRotationPoint(0F, 16F, 0F);
	      setRotation(mace3, 0.7853982F, 0F, 0F);
	      mace4 = new ModelRenderer(this, 0, 21);
	      mace4.addBox(-6.5F, -8F, -6.5F, 3, 3, 3);
	      mace4.setRotationPoint(0F, 16F, 0F);
	      setRotation(mace4, 0F, 0F, 0F);
	}

	public void render() {
		base.render(0.0625F);
		plinth.render(0.0625F);
	}

	public void renderAxle() {
		axle.render(0.0625F);
		axle2.render(0.0625F);
		axleTop.render(0.0625F);
	}
	
	public void renderMace() {
		maceBase.render(0.0625F);
		maceArm.render(0.0625F);
		mace1.render(0.0625F);
		mace2.render(0.0625F);
		mace3.render(0.0625F);
		mace4.render(0.0625F);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

}
