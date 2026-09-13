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
public class ModelAHConnect extends Model.Simple {
	public final ModelPart plate;
	public final ModelPart pipe;

	public ModelAHConnect(ModelPart root) {
		super(root, RenderTypes::entitySolid);
		this.plate = root.getChild("plate");
		this.pipe = root.getChild("pipe");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		partdefinition.addOrReplaceChild("plate", CubeListBuilder.create().texOffs(0, 0).addBox(-3F, -12F, -3F, 6F, 1F, 6F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 7F, 0F, 0F, 0F, 0F));
		partdefinition.addOrReplaceChild("pipe", CubeListBuilder.create().texOffs(0, 7).addBox(-2F, -15F, -2F, 4F, 3F, 4F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 7F, 0F, 0F, 0F, 0F));
		return LayerDefinition.create(meshdefinition, 32, 16);
	}
}
