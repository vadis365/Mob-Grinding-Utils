package mob_grinding_utils.models;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ChickenBodyModel extends Model {
    public final ModelPart body;

    public ChickenBodyModel(ModelPart root) {
        super(root, tex -> RenderTypes.entitySolid((Identifier) tex));
        this.body = root.getChild("body");
    }

    @SuppressWarnings("unused")
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 9).addBox(-3F, -4F, -3F, 6F, 8F, 6F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 16F, 0.0F, ((float)Math.PI / 2F), 0F, 0F));
        return LayerDefinition.create(meshdefinition, 64, 32);
    }
}
