package mob_grinding_utils.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.util.ARGB;
import net.minecraft.util.Unit;
import net.minecraft.world.phys.AABB;

final class RenderHelpers {
	private RenderHelpers() {}

	static void submitFluidCuboid(
			PoseStack.Pose pose,
			VertexConsumer buffer,
			float xMax,
			float xMin,
			float yMin,
			float height,
			float zMin,
			float zMax,
			TextureAtlasSprite sprite,
			float red,
			float green,
			float blue,
			float alpha,
			int light
	) {
		float uMin = sprite.getU0();
		float uMax = sprite.getU1();
		float vMin = sprite.getV0();
		float vMax = sprite.getV1();
		float vHeight = vMax - vMin;

		addVertex(buffer, pose, xMax, height, zMax, uMax, vMin, red, green, blue, alpha, light);
		addVertex(buffer, pose, xMax, height, zMin, uMin, vMin, red, green, blue, alpha, light);
		addVertex(buffer, pose, xMin, height, zMin, uMin, vMax, red, green, blue, alpha, light);
		addVertex(buffer, pose, xMin, height, zMax, uMax, vMax, red, green, blue, alpha, light);

		addVertex(buffer, pose, xMax, yMin, zMin, uMax, vMin, red, green, blue, alpha, light);
		addVertex(buffer, pose, xMin, yMin, zMin, uMin, vMin, red, green, blue, alpha, light);
		addVertex(buffer, pose, xMin, height, zMin, uMin, vMin + (vHeight * height), red, green, blue, alpha, light);
		addVertex(buffer, pose, xMax, height, zMin, uMax, vMin + (vHeight * height), red, green, blue, alpha, light);

		addVertex(buffer, pose, xMax, yMin, zMax, uMin, vMin, red, green, blue, alpha, light);
		addVertex(buffer, pose, xMax, height, zMax, uMin, vMin + (vHeight * height), red, green, blue, alpha, light);
		addVertex(buffer, pose, xMin, height, zMax, uMax, vMin + (vHeight * height), red, green, blue, alpha, light);
		addVertex(buffer, pose, xMin, yMin, zMax, uMax, vMin, red, green, blue, alpha, light);

		addVertex(buffer, pose, xMax, yMin, zMin, uMin, vMin, red, green, blue, alpha, light);
		addVertex(buffer, pose, xMax, height, zMin, uMin, vMin + (vHeight * height), red, green, blue, alpha, light);
		addVertex(buffer, pose, xMax, height, zMax, uMax, vMin + (vHeight * height), red, green, blue, alpha, light);
		addVertex(buffer, pose, xMax, yMin, zMax, uMax, vMin, red, green, blue, alpha, light);

		addVertex(buffer, pose, xMin, yMin, zMax, uMin, vMin, red, green, blue, alpha, light);
		addVertex(buffer, pose, xMin, height, zMax, uMin, vMin + (vHeight * height), red, green, blue, alpha, light);
		addVertex(buffer, pose, xMin, height, zMin, uMax, vMin + (vHeight * height), red, green, blue, alpha, light);
		addVertex(buffer, pose, xMin, yMin, zMin, uMax, vMin, red, green, blue, alpha, light);

		addVertex(buffer, pose, xMax, yMin, zMin, uMax, vMin, red, green, blue, alpha, light);
		addVertex(buffer, pose, xMax, yMin, zMax, uMin, vMin, red, green, blue, alpha, light);
		addVertex(buffer, pose, xMin, yMin, zMax, uMin, vMax, red, green, blue, alpha, light);
		addVertex(buffer, pose, xMin, yMin, zMin, uMax, vMax, red, green, blue, alpha, light);
	}

	private static void addVertex(VertexConsumer buffer, PoseStack.Pose pose, float x, float y, float z, float u, float v, float red, float green, float blue, float alpha, int light) {
		buffer.addVertex(pose, x / 2f, y, z / 2f)
				.setColor(red, green, blue, alpha)
				.setUv(u, v)
				.setOverlay(OverlayTexture.NO_OVERLAY)
				.setLight(light)
				.setNormal(pose, 0, 1, 0);
	}

	static void drawDebugBox(AABB relativeBox, BlockPos blockPos, float r, float g, float b) {
		AABB worldBox = relativeBox.move(blockPos.getX(), blockPos.getY(), blockPos.getZ());
		Gizmos.cuboid(worldBox, GizmoStyle.stroke(ARGB.colorFromFloat(1.0F, r, g, b)), true);
	}

	static final Unit UNIT = Unit.INSTANCE;
}
