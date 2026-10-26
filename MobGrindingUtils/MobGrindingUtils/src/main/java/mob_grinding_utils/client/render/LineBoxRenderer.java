package mob_grinding_utils.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.ShapeRenderer;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;

import javax.annotation.Nonnull;
import java.awt.*;

public class LineBoxRenderer {
    public static void render(@Nonnull PoseStack.Pose pose, @Nonnull VertexConsumer buffer, AABB box, int color) {
        PoseStack poseStack = new PoseStack();
        poseStack.last().set(pose);
        ShapeRenderer.renderShape(poseStack, buffer, Shapes.create(box), 0, 0, 0, color, 1.0f);
    }
}
