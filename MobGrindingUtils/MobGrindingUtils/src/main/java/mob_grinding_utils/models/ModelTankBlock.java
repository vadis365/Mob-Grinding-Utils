package mob_grinding_utils.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.neoforged.api.distmarker.Dist;

public class ModelTankBlock extends Model {
    public ModelPart tank_box;

	public ModelTankBlock(ModelPart root) {
		super(root, texture -> RenderTypes.entitySolid((net.minecraft.resources.Identifier) texture));
		this.tank_box = root.getChild("tank_box");
	}
	
	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		partdefinition.addOrReplaceChild("tank_box", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -16.0F, -8.0F, 16F, 16F, 16F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 24F, 0.0F, 0F, 0F, 0F));
		return LayerDefinition.create(meshdefinition, 64, 32);
	}

}
