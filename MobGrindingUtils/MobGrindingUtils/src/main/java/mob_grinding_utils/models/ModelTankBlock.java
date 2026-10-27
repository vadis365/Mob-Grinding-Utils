package mob_grinding_utils.models;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelTankBlock extends Model {
    public ModelPart tank_box;

    public ModelTankBlock(ModelPart root) {
        super(root, tex -> RenderTypes.entitySolid((net.minecraft.resources.Identifier) tex));
        this.tank_box = root.getChild("tank_box");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("tank_box", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -16.0F, -8.0F, 16F, 16F, 16F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 24F, 0.0F, 0F, 0F, 0F));
        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    public void submitTank(PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLightIn, int packedOverlayIn, Identifier texture) {
        nodeCollector.submitModelPart(tank_box, poseStack, RenderTypes.entitySolid(texture), packedLightIn, packedOverlayIn, null);
    }
}