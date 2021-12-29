package mob_grinding_utils.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelXPSolidifier extends Model {
    public ModelPart tank;
    public ModelPart top;
    public ModelPart rack;
    public ModelPart rack_top;
    public ModelPart rack_front;
    public ModelPart rack_left;
    public ModelPart rack_right;

    public ModelXPSolidifier() {
		super(RenderType::entitySolid);
        this.texWidth = 64;
        this.texHeight = 64;
        this.rack_left = new ModelPart(this, 6, 2);
        this.rack_left.setPos(0.0F, 0.0F, 0.0F);
        this.rack_left.addBox(-6.0F, -5.0F, -12.0F, 1, 1, 10, 0.0F);
        this.top = new ModelPart(this, 0, 20);
        this.top.setPos(0.0F, 24.0F, 0.0F);
        this.top.addBox(-8.0F, -11.0F, -8.0F, 16, 3, 16, 0.0F);
        this.tank = new ModelPart(this, 0, 40);
        this.tank.setPos(0.0F, 24.0F, 0.0F);
        this.tank.addBox(-8.0F, -8.0F, -8.0F, 16, 8, 16, 0.0F);
        this.rack_top = new ModelPart(this, 17, 14);
        this.rack_top.setPos(0.0F, 0.0F, 0.0F);
        this.rack_top.addBox(-6.0F, -6.0F, -2.0F, 12, 2, 3, 0.0F);
        this.rack_front = new ModelPart(this, 19, 0);
        this.rack_front.setPos(0.0F, 0.0F, 0.0F);
        this.rack_front.addBox(-6.0F, -5.0F, -13.0F, 12, 1, 1, 0.0F);
        this.rack_right = new ModelPart(this, 36, 2);
        this.rack_right.setPos(0.0F, 0.0F, 0.0F);
        this.rack_right.addBox(5.0F, -5.0F, -12.0F, 1, 1, 10, 0.0F);
        this.rack = new ModelPart(this, 25, 4);
        this.rack.setPos(0.0F, 14.0F, 6.0F);
        this.rack.addBox(-2.0F, -4.0F, -1.0F, 4, 3, 2, 0.0F);
        this.rack.addChild(this.rack_left);
        this.rack.addChild(this.rack_top);
        this.rack.addChild(this.rack_front);
        this.rack.addChild(this.rack_right);
    }

    @Override
	public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.tank.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

	public void renderExport(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.top.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

	public void renderRack(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.rack.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    public void setRotateAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
