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
import net.minecraft.client.renderer.RenderType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ChickenBodyModel extends Model {
	public final ModelPart body;

	public ChickenBodyModel(ModelPart root) {
		super(RenderType::entitySolid);
		this.body = root.getChild("body");
	}

	@SuppressWarnings("unused")
	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 9).addBox(-3F, -4F, -3F, 6F, 8F, 6F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 16F, 0.0F, ((float)Math.PI / 2F), 0F, 0F));
		return LayerDefinition.create(meshdefinition, 64, 32);
	}
	
	@Override
	public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, int color) {
		body.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, color);
	}
}
