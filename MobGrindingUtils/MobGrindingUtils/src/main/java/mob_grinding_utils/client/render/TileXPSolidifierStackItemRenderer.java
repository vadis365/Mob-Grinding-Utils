package mob_grinding_utils.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import java.util.function.Consumer;
import mob_grinding_utils.client.ModelLayers;
import mob_grinding_utils.components.FluidContents;
import mob_grinding_utils.components.MGUComponents;
import mob_grinding_utils.models.ModelXPSolidifier;
import mob_grinding_utils.util.RL;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

/** Component-aware item renderer replacing the removed BEWR path. */
public final class TileXPSolidifierStackItemRenderer implements SpecialModelRenderer<FluidStack> {
    private static final Identifier TEXTURE = RL.mgu("tiles/xp_solidifier_no_push");
    private final ModelXPSolidifier model;
    private TileXPSolidifierStackItemRenderer(BakingContext context) { model = new ModelXPSolidifier(context.entityModelSet().bakeLayer(ModelLayers.XPSOLIDIFIER)); }
    @Override public @Nullable FluidStack extractArgument(ItemStack stack) { return stack.getOrDefault(MGUComponents.FLUID, FluidContents.EMPTY).get().copy(); }
    @Override public void submit(@Nullable FluidStack fluid, PoseStack p, SubmitNodeCollector n, int light, int overlay, boolean foil, int outline) {
        p.pushPose(); p.translate(.5D,1.5D,.5D); p.scale(-.9999F,-.9999F,.9999F);
        n.submitModelPart(model.tank,p,RenderTypes.entityCutout(TEXTURE),light,overlay,null,false,foil,-1,null,outline); n.submitModelPart(model.top,p,RenderTypes.entityCutout(TEXTURE),light,overlay,null,false,foil,-1,null,outline); n.submitModelPart(model.rack,p,RenderTypes.entityCutout(TEXTURE),light,overlay,null,false,foil,-1,null,outline); p.popPose();
        if (fluid == null || fluid.isEmpty()) return; float height = .46875F / 16_000F * fluid.getAmount(); if (height <= 0) return;
        TextureAtlasSprite sprite = Minecraft.getInstance().getModelManager().getFluidStateModelSet().get(fluid.getFluid().defaultFluidState()).stillMaterial().sprite(); MGURenderUtil.submitFluidCuboid(p,n,sprite,-1,light,1.984375F,.015625F,.015625F,height,.015625F,1.984375F);
    }
    @Override public void getExtents(Consumer<Vector3fc> output) { model.tank.getExtentsForGui(new PoseStack(), output); }
    public record Unbaked() implements SpecialModelRenderer.Unbaked<FluidStack> {
        public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(new Unbaked());
        @Override public MapCodec<Unbaked> type() { return MAP_CODEC; }
        @Override public TileXPSolidifierStackItemRenderer bake(BakingContext context) { return new TileXPSolidifierStackItemRenderer(context); }
    }
}
