package mob_grinding_utils.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import java.util.function.Consumer;
import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.client.ModelLayers;
import mob_grinding_utils.components.FluidContents;
import mob_grinding_utils.components.MGUComponents;
import mob_grinding_utils.models.ModelTankBlock;
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

/** Component-aware special item renderer for all tank variants. */
public final class TileTankStackItemRenderer implements SpecialModelRenderer<TileTankStackItemRenderer.Data> {
    private static final Identifier TANK = RL.mgu("tiles/tank"), SINK = RL.mgu("tiles/tank_sink"), JUMBO = RL.mgu("tiles/tank_jumbo");
    private final ModelTankBlock model;
    private TileTankStackItemRenderer(BakingContext context) { model = new ModelTankBlock(context.entityModelSet().bakeLayer(ModelLayers.TANK)); }
    @Override public @Nullable Data extractArgument(ItemStack stack) {
        FluidStack fluid = stack.getOrDefault(MGUComponents.FLUID, FluidContents.EMPTY).get();
        return new Data(fluid.copy(), stack.is(ModBlocks.JUMBO_TANK.getItem()), stack.is(ModBlocks.TANK_SINK.getItem()));
    }
    @Override public void submit(@Nullable Data data, PoseStack p, SubmitNodeCollector n, int light, int overlay, boolean foil, int outline) {
        if (data == null) return; Identifier texture = data.jumbo ? JUMBO : data.sink ? SINK : TANK;
        p.pushPose(); p.translate(.5D,1.5D,.5D); p.scale(-.9999F,-.9999F,.9999F); n.submitModelPart(model.tank_box,p,RenderTypes.entityCutout(texture),light,overlay,null,false,foil,-1,null,outline); p.popPose();
        if (data.fluid.isEmpty()) return; float capacity = data.jumbo ? 1_024_000F : 32_000F, height = .96875F / capacity * data.fluid.getAmount(); if (height <= 0) return;
		TextureAtlasSprite sprite = Minecraft.getInstance().getModelManager().getFluidStateModelSet().get(data.fluid.getFluid().defaultFluidState()).stillMaterial().sprite();
		MGURenderUtil.submitFluidCuboid(p,n,sprite,-1,light,1.984375F,.015625F,.015625F,height,.015625F,1.984375F);
    }
    @Override public void getExtents(Consumer<Vector3fc> output) { model.tank_box.getExtentsForGui(new PoseStack(), output); }
    public record Data(FluidStack fluid, boolean jumbo, boolean sink) {}
    public record Unbaked() implements SpecialModelRenderer.Unbaked<Data> {
        public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(new Unbaked());
        @Override public MapCodec<Unbaked> type() { return MAP_CODEC; }
        @Override public TileTankStackItemRenderer bake(BakingContext context) { return new TileTankStackItemRenderer(context); }
    }
}
