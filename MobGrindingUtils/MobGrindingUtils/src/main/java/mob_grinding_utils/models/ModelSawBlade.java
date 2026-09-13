package mob_grinding_utils.models;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.rendertype.RenderTypes;
public class ModelSawBlade extends Model.Simple {
	public ModelSawBlade(ModelPart root) {
		super(root, RenderTypes::entitySolid);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		partdefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 9).addBox(-5F, -0.5F, -5F, 10F, 1F, 10F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 16F, 0F, 0F, 0F, 0F));
		partdefinition.addOrReplaceChild("back", CubeListBuilder.create().texOffs(0, 0).addBox(-6F, -0.5F, -3.5F, 1F, 1F, 7F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 16F, 0F, 0F, 1.570796F, 0F));
		partdefinition.addOrReplaceChild("front", CubeListBuilder.create().texOffs(0, 0).addBox(-6F, -0.5F, -3.5F, 1F, 1F, 7F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 16F, 0F, 0F, -1.570796F, 0F));
		partdefinition.addOrReplaceChild("left", CubeListBuilder.create().texOffs(0, 0).addBox(5F, -0.5F, -3.5F, 1F, 1F, 7F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 16F, 0F, 0F, 0F, 0F));
		partdefinition.addOrReplaceChild("right", CubeListBuilder.create().texOffs(0, 0).addBox(-6F, -0.5F, -3.5F, 1F, 1F, 7F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 16F, 0F, 0F, 0F, 0F));
		partdefinition.addOrReplaceChild("tooth1Main", CubeListBuilder.create().texOffs(0, 3).addBox(-7F, -0.5F, -1F, 1F, 1F, 2F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 16F, 0F, 0F, -1.570796F, 0F));
		partdefinition.addOrReplaceChild("tooth2Main", CubeListBuilder.create().texOffs(0, 3).addBox(-7F, -0.5F, -1F, 1F, 1F, 2F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 16F, 0F, 0F, -1.178097F, 0F));
		partdefinition.addOrReplaceChild("tooth3Main", CubeListBuilder.create().texOffs(0, 3).addBox(-7F, -0.5F, -1F, 1F, 1F, 2F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 16F, 0F, 0F, -0.7853982F, 0F));
		partdefinition.addOrReplaceChild("tooth4Main", CubeListBuilder.create().texOffs(0, 3).addBox(-7F, -0.5F, -1F, 1F, 1F, 2F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 16F, 0F, 0F, -0.3926991F, 0F));
		partdefinition.addOrReplaceChild("tooth5Main", CubeListBuilder.create().texOffs(0, 3).addBox(-7F, -0.5F, -1F, 1F, 1F, 2F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 16F, 0F, 0F, 0F, 0F));
		partdefinition.addOrReplaceChild("tooth6Main", CubeListBuilder.create().texOffs(0, 3).addBox(-7F, -0.5F, -1F, 1F, 1F, 2F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 16F, 0F, 0F, 0.3926991F, 0F));
		partdefinition.addOrReplaceChild("tooth7Main", CubeListBuilder.create().texOffs(0, 3).addBox(-7F, -0.5F, -1F, 1F, 1F, 2F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 16F, 0F, 0F, 0.7853982F, 0F));
		partdefinition.addOrReplaceChild("tooth8Main", CubeListBuilder.create().texOffs(0, 3).addBox(-7F, -0.5F, -1F, 1F, 1F, 2F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 16F, 0F, 0F, 1.178097F, 0F));
		partdefinition.addOrReplaceChild("tooth9Main", CubeListBuilder.create().texOffs(0, 3).addBox(-7F, -0.5F, -1F, 1F, 1F, 2F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 16F, 0F, 0F, 1.570796F, 0F));
		partdefinition.addOrReplaceChild("tooth10Main", CubeListBuilder.create().texOffs(0, 3).addBox(-7F, -0.5F, -1F, 1F, 1F, 2F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 16F, 0F, 0F, 1.963495F, 0F));
		partdefinition.addOrReplaceChild("tooth11Main", CubeListBuilder.create().texOffs(0, 3).addBox(-7F, -0.5F, -1F, 1F, 1F, 2F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 16F, 0F, 0F, 2.356194F, 0F));
		partdefinition.addOrReplaceChild("tooth12Main", CubeListBuilder.create().texOffs(0, 3).addBox(-7F, -0.5F, -1F, 1F, 1F, 2F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 16F, 0F, 0F, 2.748893F, 0F));
		partdefinition.addOrReplaceChild("tooth13Main", CubeListBuilder.create().texOffs(0, 3).addBox(-7F, -0.5F, -1F, 1F, 1F, 2F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 16F, 0F, 0F, 3.141593F, 0F));
		partdefinition.addOrReplaceChild("tooth14Main", CubeListBuilder.create().texOffs(0, 3).addBox(-7F, -0.5F, -1F, 1F, 1F, 2F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 16F, 0F, 0F, -2.748893F, 0F));
		partdefinition.addOrReplaceChild("tooth15Main", CubeListBuilder.create().texOffs(0, 3).addBox(-7F, -0.5F, -1F, 1F, 1F, 2F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 16F, 0F, 0F, -2.356194F, 0F));
		partdefinition.addOrReplaceChild("tooth16Main", CubeListBuilder.create().texOffs(0, 3).addBox(-7F, -0.5F, -1F, 1F, 1F, 2F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 16F, 0F, 0F, -1.963495F, 0F));
		partdefinition.addOrReplaceChild("tooth1End", CubeListBuilder.create().texOffs(0, 0).addBox(-8F, -0.5F, -1F, 1F, 1F, 1F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 16F, 0F, 0F, -1.570796F, 0F));
		partdefinition.addOrReplaceChild("tooth2End", CubeListBuilder.create().texOffs(0, 0).addBox(-8F, -0.5F, -1F, 1F, 1F, 1F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 16F, 0F, 0F, -1.178097F, 0F));
		partdefinition.addOrReplaceChild("tooth3End", CubeListBuilder.create().texOffs(0, 0).addBox(-8F, -0.5F, -1F, 1F, 1F, 1F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 16F, 0F, 0F, -0.7853982F, 0F));
		partdefinition.addOrReplaceChild("tooth4End", CubeListBuilder.create().texOffs(0, 0).addBox(-8F, -0.5F, -1F, 1F, 1F, 1F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 16F, 0F, 0F, -0.3926991F, 0F));
		partdefinition.addOrReplaceChild("tooth5End", CubeListBuilder.create().texOffs(0, 0).addBox(-8F, -0.5F, -1F, 1F, 1F, 1F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 16F, 0F, 0F, 0F, 0F));
		partdefinition.addOrReplaceChild("tooth6End", CubeListBuilder.create().texOffs(0, 0).addBox(-8F, -0.5F, -1F, 1F, 1F, 1F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 16F, 0F, 0F, 0.3926991F, 0F));
		partdefinition.addOrReplaceChild("tooth7End", CubeListBuilder.create().texOffs(0, 0).addBox(-8F, -0.5F, -1F, 1F, 1F, 1F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 16F, 0F, 0F, 0.7853982F, 0F));
		partdefinition.addOrReplaceChild("tooth8End", CubeListBuilder.create().texOffs(0, 0).addBox(-8F, -0.5F, -1F, 1F, 1F, 1F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 16F, 0F, 0F, 1.178097F, 0F));
		partdefinition.addOrReplaceChild("tooth9End", CubeListBuilder.create().texOffs(0, 0).addBox(-8F, -0.5F, -1F, 1F, 1F, 1F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 16F, 0F, 0F, 1.570796F, 0F));
		partdefinition.addOrReplaceChild("tooth10End", CubeListBuilder.create().texOffs(0, 0).addBox(-8F, -0.5F, -1F, 1F, 1F, 1F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 16F, 0F, 0F, 1.963495F, 0F));
		partdefinition.addOrReplaceChild("tooth11End", CubeListBuilder.create().texOffs(0, 0).addBox(-8F, -0.5F, -1F, 1F, 1F, 1F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 16F, 0F, 0F, 2.356194F, 0F));
		partdefinition.addOrReplaceChild("tooth12End", CubeListBuilder.create().texOffs(0, 0).addBox(-8F, -0.5F, -1F, 1F, 1F, 1F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 16F, 0F, 0F, 2.748893F, 0F));
		partdefinition.addOrReplaceChild("tooth13End", CubeListBuilder.create().texOffs(0, 0).addBox(-8F, -0.5F, -1F, 1F, 1F, 1F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 16F, 0F, 0F, 3.141593F, 0F));
		partdefinition.addOrReplaceChild("tooth14End", CubeListBuilder.create().texOffs(0, 0).addBox(-8F, -0.5F, -1F, 1F, 1F, 1F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 16F, 0F, 0F, -2.748893F, 0F));
		partdefinition.addOrReplaceChild("tooth15End", CubeListBuilder.create().texOffs(0, 0).addBox(-8F, -0.5F, -1F, 1F, 1F, 1F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 16F, 0F, 0F, -2.356194F, 0F));
		partdefinition.addOrReplaceChild("tooth16End", CubeListBuilder.create().texOffs(0, 0).addBox(-8F, -0.5F, -1F, 1F, 1F, 1F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 16F, 0F, 0F, -1.963495F, 0F));
		return LayerDefinition.create(meshdefinition, 64, 32);
	}
}
