package mob_grinding_utils.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.ShapeRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;

final class MGURenderUtil {
    private MGURenderUtil() {}

    static void submitLineBox(PoseStack stack, SubmitNodeCollector nodes, AABB box, float red, float green, float blue) {
		nodes.submitCustomGeometry(stack, RenderTypes.lines(), (pose, buffer) -> {
            PoseStack legacyStack = new PoseStack();
            legacyStack.last().set(pose);
			ShapeRenderer.renderShape(legacyStack, buffer, Shapes.create(box), 0, 0, 0, ((int) (255 * red) << 24) | ((int) (255 * blue) << 16) | ((int) (255 * green) << 8) | 255, 1.0F);
        });
    }

    static void submitFluidCuboid(
        PoseStack stack, SubmitNodeCollector nodes, TextureAtlasSprite sprite, int color, int light, float xMax, float xMin, float yMin, float height, float zMin, float zMax
    ) {
		nodes.submitCustomGeometry(stack, RenderTypes.translucentMovingBlock(), (pose, buffer) -> renderCuboid(buffer, pose, xMax, xMin, yMin, height, zMin, zMax, sprite, color, light));
    }

    private static void renderCuboid(VertexConsumer buffer, PoseStack.Pose pose, float xMax, float xMin, float yMin, float height, float zMin, float zMax, TextureAtlasSprite sprite, int color, int light) {
        float u0 = sprite.getU0(), u1 = sprite.getU1(), v0 = sprite.getV0(), v1 = sprite.getV1(), dv = v1 - v0;
        vertex(buffer, pose, xMax, height, zMax, u1, v0, color, light); vertex(buffer, pose, xMax, height, zMin, u0, v0, color, light); vertex(buffer, pose, xMin, height, zMin, u0, v1, color, light); vertex(buffer, pose, xMin, height, zMax, u1, v1, color, light);
        vertex(buffer, pose, xMax, yMin, zMin, u1, v0, color, light); vertex(buffer, pose, xMin, yMin, zMin, u0, v0, color, light); vertex(buffer, pose, xMin, height, zMin, u0, v0 + dv * height, color, light); vertex(buffer, pose, xMax, height, zMin, u1, v0 + dv * height, color, light);
        vertex(buffer, pose, xMax, yMin, zMax, u0, v0, color, light); vertex(buffer, pose, xMax, height, zMax, u0, v0 + dv * height, color, light); vertex(buffer, pose, xMin, height, zMax, u1, v0 + dv * height, color, light); vertex(buffer, pose, xMin, yMin, zMax, u1, v0, color, light);
        vertex(buffer, pose, xMax, yMin, zMin, u0, v0, color, light); vertex(buffer, pose, xMax, height, zMin, u0, v0 + dv * height, color, light); vertex(buffer, pose, xMax, height, zMax, u1, v0 + dv * height, color, light); vertex(buffer, pose, xMax, yMin, zMax, u1, v0, color, light);
        vertex(buffer, pose, xMin, yMin, zMax, u0, v0, color, light); vertex(buffer, pose, xMin, height, zMax, u0, v0 + dv * height, color, light); vertex(buffer, pose, xMin, height, zMin, u1, v0 + dv * height, color, light); vertex(buffer, pose, xMin, yMin, zMin, u1, v0, color, light);
        vertex(buffer, pose, xMax, yMin, zMin, u1, v0, color, light); vertex(buffer, pose, xMax, yMin, zMax, u0, v0, color, light); vertex(buffer, pose, xMin, yMin, zMax, u0, v1, color, light); vertex(buffer, pose, xMin, yMin, zMin, u1, v1, color, light);
    }

    private static void vertex(VertexConsumer buffer, PoseStack.Pose pose, float x, float y, float z, float u, float v, int color, int light) {
        buffer.addVertex(pose, x / 2.0F, y, z / 2.0F).setColor(color).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(1, 0, 0);
    }
}
