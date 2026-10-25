package mob_grinding_utils.models;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelAHConnect extends Model {
    public ModelPart plate;
    public ModelPart pipe;

    public ModelAHConnect(ModelPart root) {
        super(root, tex -> RenderTypes.entitySolid((net.minecraft.resources.Identifier) tex));
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

    public void submitConnector(PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLightIn, int packedOverlayIn, Identifier texture, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
                nodeCollector.submitModelPart(plate, poseStack, RenderTypes.entitySolid(texture), packedLightIn, packedOverlayIn, null, 0x7F7F7FFF, crumblingOverlay);
                nodeCollector.submitModelPart(pipe, poseStack, RenderTypes.entitySolid(texture), packedLightIn, packedOverlayIn, null, 0x7f7f7fff, crumblingOverlay);
    }
}