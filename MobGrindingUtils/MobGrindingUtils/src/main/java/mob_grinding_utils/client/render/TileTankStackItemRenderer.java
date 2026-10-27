package mob_grinding_utils.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.client.ModelLayers;
import mob_grinding_utils.components.FluidContents;
import mob_grinding_utils.components.MGUComponents;
import mob_grinding_utils.models.ModelTankBlock;
import mob_grinding_utils.util.RL;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.fluids.FluidStack;
import org.joml.Vector3fc;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
public class TileTankStackItemRenderer implements SpecialModelRenderer<TileTankStackItemRenderer.Data> {

    private static final Identifier TANK_TEXTURE = RL.mgu("textures/tiles/tank.png");
    private static final Identifier TANK_SINK_TEXTURE = RL.mgu("textures/tiles/tank_sink.png");
    private static final Identifier TANK_JUMBO_TEXTURE = RL.mgu("textures/tiles/tank_jumbo.png");
    private final ModelTankBlock tank_model;
    private FluidStack fluidStack;

    public TileTankStackItemRenderer(SpecialModelRenderer.BakingContext context) {
        EntityModelSet EntityModelSetThatIsntNULL = Minecraft.getInstance().getEntityModels();
        tank_model = new ModelTankBlock(EntityModelSetThatIsntNULL.bakeLayer(ModelLayers.TANK));
    }

    @Override
    public void submit(@Nullable Data data, @Nonnull PoseStack poseStack, @Nonnull SubmitNodeCollector nodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
        poseStack.pushPose();
        poseStack.translate(0.5D, 1.5D, 0.5D);
        poseStack.scale(-0.9999F, -0.9999F, 0.9999F);
        nodeCollector.submitModelPart(tank_model.tank_box, poseStack, RenderTypes.entityCutout(getTexture(data.item.getItem())), lightCoords, OverlayTexture.NO_OVERLAY, null, 0xFFFFFFFF, null);
        poseStack.popPose();

        fluidStack = data.fluidStack();

        float fluidLevel = fluidStack.getAmount();
        if (fluidLevel < 1)
            return;
        float tankMax = 32000F;
        if(data.item.getItem() == ModBlocks.JUMBO_TANK.getItem())
            tankMax = 1024000F;
        float height = (0.96875F / tankMax) * fluidLevel; // volumes hardcoded until config

        poseStack.pushPose();
        poseStack.translate(0D, 0D, 0D);
        float xMax, zMax, xMin, zMin, yMin = 0;
        xMax = 1.984375F;
        zMax = 1.984375F;
        xMin = 0.015625F;
        zMin = 0.015625F;
        yMin = 0.015625F;
        TileEntityTankRenderer.renderFluidCube(poseStack, nodeCollector, fluidStack.getFluid(), xMax, xMin, yMin, height, zMin, zMax, lightCoords);
        poseStack.popPose();
    }

    private Identifier getTexture(Item item) {
        return item == ModBlocks.JUMBO_TANK.getItem() ? TANK_JUMBO_TEXTURE : item == ModBlocks.TANK_SINK.getItem() ? TANK_SINK_TEXTURE : TANK_TEXTURE;
    }

    @Override
    public void getExtents(Consumer<Vector3fc> output) {

    }

    @Override
    public @Nullable Data extractArgument(ItemStack stack) {
        return new Data(stack.getOrDefault(MGUComponents.FLUID, FluidContents.EMPTY).get(), stack.copy());
    }

    public record Data(FluidStack fluidStack, ItemStack item) {}
}
