package mob_grinding_utils.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.ShapeRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;

import javax.annotation.Nonnull;
import java.awt.*;

public record LineBoxRenderer(AABB box, Color color) implements SubmitNodeCollector.CustomGeometryRenderer {
    @Override
    public void render(@Nonnull PoseStack.Pose pose, @Nonnull VertexConsumer buffer) {
        PoseStack poseStack = new PoseStack();
        poseStack.last().set(pose);
        ShapeRenderer.renderShape(poseStack, buffer, Shapes.create(box), 0, 0, 0, color.getRGB(), 1.0f);
    }
}
