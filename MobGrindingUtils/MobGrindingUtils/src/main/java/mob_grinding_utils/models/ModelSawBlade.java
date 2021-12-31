package mob_grinding_utils.models;

import com.google.common.collect.ImmutableList;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelSawBlade extends Model {
    public ModelPart main;
    public ModelPart back;
    public ModelPart front;
    public ModelPart left;
    public ModelPart right;
    public ModelPart tooth1Main;
    public ModelPart tooth2Main;
    public ModelPart tooth3Main;
    public ModelPart tooth4Main;
    public ModelPart tooth5Main;
    public ModelPart tooth6Main;
    public ModelPart tooth7Main;
    public ModelPart tooth8Main;
    public ModelPart tooth9Main;
    public ModelPart tooth10Main;
    public ModelPart tooth11Main;
    public ModelPart tooth12Main;
    public ModelPart tooth13Main;
    public ModelPart tooth14Main;
    public ModelPart tooth15Main;
    public ModelPart tooth16Main;
    public ModelPart tooth1End;
    public ModelPart tooth2End;
    public ModelPart tooth3End;
    public ModelPart tooth4End;
    public ModelPart tooth5End;
    public ModelPart tooth6End;
    public ModelPart tooth7End;
    public ModelPart tooth8End;
    public ModelPart tooth9End;
    public ModelPart tooth10End;
    public ModelPart tooth11End;
    public ModelPart tooth12End;
    public ModelPart tooth13End;
    public ModelPart tooth14End;
    public ModelPart tooth15End;
    public ModelPart tooth16End;

	public ModelSawBlade(ModelPart root) {
		super(RenderType::entitySolid);
		this.main = root.getChild("main");
		this.back = root.getChild("back");
		this.front = root.getChild("front");
		this.left = root.getChild("left");
		this.right = root.getChild("right");
		this.tooth1Main = root.getChild("tooth1Main");
		this.tooth2Main = root.getChild("tooth2Main");
		this.tooth3Main = root.getChild("tooth3Main");
		this.tooth4Main = root.getChild("tooth4Main");
		this.tooth5Main = root.getChild("tooth5Main");
		this.tooth6Main = root.getChild("tooth6Main");
		this.tooth7Main = root.getChild("tooth7Main");
		this.tooth8Main = root.getChild("tooth8Main");
		this.tooth9Main = root.getChild("tooth9Main");
		this.tooth10Main = root.getChild("tooth10Main");
		this.tooth11Main = root.getChild("tooth11Main");
		this.tooth12Main = root.getChild("tooth12Main");
		this.tooth13Main = root.getChild("tooth13Main");
		this.tooth14Main = root.getChild("tooth14Main");
		this.tooth15Main = root.getChild("tooth15Main");
		this.tooth16Main = root.getChild("tooth16Main");
		this.tooth1End = root.getChild("tooth1End");
		this.tooth2End = root.getChild("tooth2End");
		this.tooth3End = root.getChild("tooth3End");
		this.tooth4End = root.getChild("tooth4End");
		this.tooth5End = root.getChild("tooth5End");
		this.tooth6End = root.getChild("tooth6End");
		this.tooth7End = root.getChild("tooth7End");
		this.tooth8End = root.getChild("tooth8End");
		this.tooth9End = root.getChild("tooth9End");
		this.tooth10End = root.getChild("tooth10End");
		this.tooth11End = root.getChild("tooth11End");
		this.tooth12End = root.getChild("tooth12End");
		this.tooth13End = root.getChild("tooth13End");
		this.tooth14End = root.getChild("tooth14End");
		this.tooth15End = root.getChild("tooth15End");
		this.tooth16End = root.getChild("tooth16End");
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
		partdefinition.addOrReplaceChild("tooth4Main", CubeListBuilder.create().texOffs(0, 3).addBox(-7F, -0.5F, -1F, 1F, 1F, 2F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 16F, 0F,  0F, -0.3926991F, 0F));
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

	
	public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		ImmutableList.of(main, back, front, left, right, tooth1Main, tooth2Main, tooth3Main, tooth4Main, tooth5Main,
				tooth6Main, tooth7Main, tooth8Main, tooth9Main, tooth10Main, tooth11Main, tooth12Main, tooth13Main,
				tooth14Main, tooth15Main, tooth16Main, tooth1End, tooth2End, tooth3End, tooth4End, tooth5End, tooth6End,
				tooth7End, tooth8End, tooth9End, tooth10End, tooth11End, tooth12End, tooth13End, tooth14End, tooth15End,
				tooth16End).forEach((p_228279_8_) -> { p_228279_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
				});
	}

	private void setRotation(ModelPart model, float x, float y, float z) {
		model.xRot = x;
		model.yRot = y;
		model.zRot = z;
	}

}
