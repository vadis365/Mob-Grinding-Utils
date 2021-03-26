package mob_grinding_utils.models;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelXPSolidifier extends Model {
    public ModelRenderer tank;
    public ModelRenderer top;
    public ModelRenderer rack;
    public ModelRenderer rack_top;
    public ModelRenderer rack_front;
    public ModelRenderer rack_left;
    public ModelRenderer rack_right;

    public ModelXPSolidifier() {
		super(RenderType::getEntitySolid);
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.rack_left = new ModelRenderer(this, 6, 2);
        this.rack_left.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.rack_left.addBox(-6.0F, -5.0F, -12.0F, 1, 1, 10, 0.0F);
        this.top = new ModelRenderer(this, 0, 20);
        this.top.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.top.addBox(-8.0F, -11.0F, -8.0F, 16, 3, 16, 0.0F);
        this.tank = new ModelRenderer(this, 0, 40);
        this.tank.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.tank.addBox(-8.0F, -8.0F, -8.0F, 16, 8, 16, 0.0F);
        this.rack_top = new ModelRenderer(this, 17, 14);
        this.rack_top.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.rack_top.addBox(-6.0F, -6.0F, -2.0F, 12, 2, 3, 0.0F);
        this.rack_front = new ModelRenderer(this, 19, 0);
        this.rack_front.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.rack_front.addBox(-6.0F, -5.0F, -13.0F, 12, 1, 1, 0.0F);
        this.rack_right = new ModelRenderer(this, 36, 2);
        this.rack_right.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.rack_right.addBox(5.0F, -5.0F, -12.0F, 1, 1, 10, 0.0F);
        this.rack = new ModelRenderer(this, 25, 4);
        this.rack.setRotationPoint(0.0F, 14.0F, 6.0F);
        this.rack.addBox(-2.0F, -4.0F, -1.0F, 4, 3, 2, 0.0F);
        this.rack.addChild(this.rack_left);
        this.rack.addChild(this.rack_top);
        this.rack.addChild(this.rack_front);
        this.rack.addChild(this.rack_right);
    }

    @Override
	public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.tank.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

	public void renderExport(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.top.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

	public void renderRack(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.rack.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
