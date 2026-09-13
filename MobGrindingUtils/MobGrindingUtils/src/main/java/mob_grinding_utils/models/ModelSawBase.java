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
public class ModelSawBase extends Model.Simple {
	public final ModelPart axle;
	public final ModelPart axle2;
	public final ModelPart axleTop;
	public final ModelPart plinth;
	public final ModelPart base;
	public final ModelPart maceBase;
	public final ModelPart maceArm;
	public final ModelPart mace1;
	public final ModelPart mace2;
	public final ModelPart mace3;
	public final ModelPart mace4;

	public ModelSawBase(ModelPart root) {
		super(root, RenderTypes::entitySolid);
		this.axle = root.getChild("axle");
		this.axle2 = root.getChild("axle2");
		this.axleTop = root.getChild("axleTop");
		this.plinth = root.getChild("plinth");
		this.base = root.getChild("base");
		this.maceBase = root.getChild("maceBase");
		this.maceArm = root.getChild("maceArm");
		this.mace1 = root.getChild("mace1");
		this.mace2 = root.getChild("mace2");
		this.mace3 = root.getChild("mace3");
		this.mace4 = root.getChild("mace4");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		partdefinition.addOrReplaceChild("axle", CubeListBuilder.create().texOffs(0, 0).addBox(-1F, -5F, -1F, 2F, 10F, 2F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 16F, 0F, 0F, 0F, 0F));
		partdefinition.addOrReplaceChild("axle2", CubeListBuilder.create().texOffs(0, 0).addBox(-1F, -5F, -1F, 2F, 10F, 2F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 16F, 0F, 0F, 0.7853982F, 0F));
		partdefinition.addOrReplaceChild("axleTop", CubeListBuilder.create().texOffs(0, 21).addBox(-1.5F, -8F, -1.5F, 3F, 3F, 3F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 16F, 0F, 0F, 0.7853982F, 0F));
		partdefinition.addOrReplaceChild("plinth", CubeListBuilder.create().texOffs(9, 0).addBox(-5.5F, -5F, -5.5F, 11F, 2F, 11F, new CubeDeformation(0F)),
				PartPose.offsetAndRotation(0F, 26F, 0F, 0F, 0.7853982F, 0F));
		partdefinition.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 15).addBox(-8F, -7F, -8F, 16F, 1F, 16F, new CubeDeformation(0F)),
				PartPose.offsetAndRotation(0F, 30F, 0F, 0F, 0F, 0F));
		partdefinition.addOrReplaceChild("maceBase", CubeListBuilder.create().texOffs(0, 16).addBox(-3.5F, -7.5F, -1F, 2F, 2F, 2F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 16F, 0F, 0F, -0.7853982F, 0F));
		partdefinition.addOrReplaceChild("maceArm", CubeListBuilder.create().texOffs(9, 16).addBox(-5.5F, -7F, -0.5F, 2F, 1F, 1F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 16F, 0F, 0F, -0.7853982F, 0F));
		partdefinition.addOrReplaceChild("mace1", CubeListBuilder.create().texOffs(0, 21).addBox(-1.5F, -8F, -8.5F, 3F, 3F, 3F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 16F, 0F, 0F, 0.7853982F, 0F));
		partdefinition.addOrReplaceChild("mace2", CubeListBuilder.create().texOffs(0, 21).addBox(-9.5F, -2.5F, -6.5F, 3F, 3F, 3F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 16F, 0F, 0F, 0F, 0.7853982F));
		partdefinition.addOrReplaceChild("mace3", CubeListBuilder.create().texOffs(0, 21).addBox(-6.5F, -9.5F, -0.5F, 3F, 3F, 3F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 16F, 0F, 0.7853982F, 0F, 0F));
		partdefinition.addOrReplaceChild("mace4", CubeListBuilder.create().texOffs(0, 21).addBox(-6.5F, -8F, -6.5F, 3F, 3F, 3F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 16F, 0F, 0F, 0F, 0F));
		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	public ModelPart[] baseParts() {
		return new ModelPart[]{base, plinth};
	}

	public ModelPart[] axleParts() {
		return new ModelPart[]{axle, axle2, axleTop};
	}

	public ModelPart[] maceParts() {
		return new ModelPart[]{maceBase, maceArm, mace1, mace2, mace3, mace4};
	}
}
