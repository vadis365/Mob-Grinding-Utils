package mob_grinding_utils.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelXPSolidifier extends Model {
    public ModelPart tank;
    public ModelPart top;
    public ModelPart rack;
    public ModelPart rack_top;
    public ModelPart rack_front;
    public ModelPart rack_left;
    public ModelPart rack_right;

    public ModelXPSolidifier(ModelPart root) {
		super(RenderType::entitySolid);
		this.tank = root.getChild("tank");
		this.top = root.getChild("top");
		this.rack = root.getChild("rack");
		this.rack_top = rack.getChild("rack_top");
		this.rack_front = rack.getChild("rack_front");
		this.rack_left = rack.getChild("rack_left");
		this.rack_right = rack.getChild("rack_right");
    }

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		partdefinition.addOrReplaceChild("top", CubeListBuilder.create().texOffs(0, 20).addBox(-8F, -11F, -8F, 16F, 3F, 16F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 24F, 0F, 0F, 0F, 0F));
		partdefinition.addOrReplaceChild("tank", CubeListBuilder.create().texOffs(0, 40).addBox(-8F, -8F, -8F, 16F, 8F, 16F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 24F, 0F, 0F, 0F, 0F));
		PartDefinition rack = partdefinition.addOrReplaceChild("rack", CubeListBuilder.create().texOffs(25, 4).addBox(-2F, -4F, -1F, 4F, 3F, 2F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 14F, 6F, 0F, 0F, 0F));
		PartDefinition rack_top = rack.addOrReplaceChild("rack_top", CubeListBuilder.create().texOffs(17, 14).addBox(-6F, -6F, -2F, 12F, 2F, 3F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 0F, 0F, 0F, 0F, 0F));
		PartDefinition rack_front = rack.addOrReplaceChild("rack_front", CubeListBuilder.create().texOffs(19, 0).addBox(-6F, -5F, -13F, 12F, 1F, 1F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 0F, 0F, 0F, 0F, 0F));
		PartDefinition rack_left = rack.addOrReplaceChild("rack_left", CubeListBuilder.create().texOffs(6, 2).addBox(-6F, -5F, -12F, 1F, 1F, 10F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 0F, 0F, 0F, 0F, 0F));
		PartDefinition rack_right = rack.addOrReplaceChild("rack_right", CubeListBuilder.create().texOffs(36, 2).addBox(5F, -5F, -12F, 1F, 1F, 10F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 0F, 0F, 0F, 0F, 0F));
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

    @Override
	public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, int color) {
        this.tank.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, color);
    }

	public void renderExport(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, int color) {
        this.top.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, color);
    }

	public void renderRack(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, int color) {
        this.rack.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, color);
    }
}
