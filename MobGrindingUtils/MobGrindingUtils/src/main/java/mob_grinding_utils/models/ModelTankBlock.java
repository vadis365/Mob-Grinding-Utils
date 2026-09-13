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
public class ModelTankBlock extends Model.Simple {
	public final ModelPart tank_box;

	public ModelTankBlock(ModelPart root) {
		// Translucent path honors glass alpha in textures/tiles/tank*.png (cutout was drawing opaque).
		super(root, RenderTypes::entityTranslucentCullItemTarget);
		this.tank_box = root.getChild("tank_box");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		partdefinition.addOrReplaceChild("tank_box", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -16.0F, -8.0F, 16F, 16F, 16F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 24F, 0.0F, 0F, 0F, 0F));
		return LayerDefinition.create(meshdefinition, 64, 32);
	}
}
