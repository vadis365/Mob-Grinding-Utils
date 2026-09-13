package mob_grinding_utils.models;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.rendertype.RenderTypes;
public class ModelXPSolidifier extends Model.Simple {
	public final ModelPart tank;
	public final ModelPart top;
	public final ModelPart rack;

	public ModelXPSolidifier(ModelPart root) {
		super(root, RenderTypes::entityCutout);
		this.tank = root.getChild("tank");
		this.top = root.getChild("top");
		this.rack = root.getChild("rack");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		partdefinition.addOrReplaceChild("top", CubeListBuilder.create().texOffs(0, 20).addBox(-8F, -11F, -8F, 16F, 3F, 16F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 24F, 0F, 0F, 0F, 0F));
		partdefinition.addOrReplaceChild("tank", CubeListBuilder.create().texOffs(0, 40).addBox(-8F, -8F, -8F, 16F, 8F, 16F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 24F, 0F, 0F, 0F, 0F));
		PartDefinition rack = partdefinition.addOrReplaceChild("rack", CubeListBuilder.create().texOffs(25, 4).addBox(-2F, -4F, -1F, 4F, 3F, 2F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 14F, 6F, 0F, 0F, 0F));
		rack.addOrReplaceChild("rack_top", CubeListBuilder.create().texOffs(17, 14).addBox(-6F, -6F, -2F, 12F, 2F, 3F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 0F, 0F, 0F, 0F, 0F));
		rack.addOrReplaceChild("rack_front", CubeListBuilder.create().texOffs(19, 0).addBox(-6F, -5F, -13F, 12F, 1F, 1F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 0F, 0F, 0F, 0F, 0F));
		rack.addOrReplaceChild("rack_left", CubeListBuilder.create().texOffs(6, 2).addBox(-6F, -5F, -12F, 1F, 1F, 10F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 0F, 0F, 0F, 0F, 0F));
		rack.addOrReplaceChild("rack_right", CubeListBuilder.create().texOffs(36, 2).addBox(5F, -5F, -12F, 1F, 1F, 10F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 0F, 0F, 0F, 0F, 0F));
		return LayerDefinition.create(meshdefinition, 64, 64);
	}
}
