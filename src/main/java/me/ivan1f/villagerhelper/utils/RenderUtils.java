package me.ivan1f.villagerhelper.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class RenderUtils {

    /**
     * Reference: {@link net.minecraft.client.render.entity.EntityRenderer}
     */
    public static void renderTextOnEntity(Entity entity, String string, MatrixStack matrices, EntityRenderDispatcher renderManager, VertexConsumerProvider vertexConsumerProvider) {
        double d = renderManager.getSquaredDistanceToCamera(entity);
        if (!(d > 4096.0)) {
            matrices.push();
            matrices.translate(0.0, entity.getHeight() + 0.5, 0.0);
            matrices.multiply(renderManager.getRotation());
            matrices.scale(-0.025F, -0.025F, 0.025F);
            Matrix4f matrix4f = matrices.peek().getModel();
            float g = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25F);
            int k = (int) (g * 255.0F) << 24;
            TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
            float h = (float) (-textRenderer.getStringWidth(string) / 2);
            textRenderer.draw(string, h, 0, 553648127, false, matrix4f, vertexConsumerProvider, true, k, 0xf00000);
            textRenderer.draw(string, h, 0, -1, false, matrix4f, vertexConsumerProvider, true, 0, 0xf00000);

            matrices.pop();
        }
    }


    /**
     * Reference: {@link net.minecraft.client.render.WorldRenderer}
     */
    public static void drawBox(BlockPos pos, float red, float green, float blue, float alpha) {
        Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
        if (camera.isReady()) {
            Vec3d vec3d = camera.getPos().negate();
            Box box = (new Box(pos)).offset(vec3d);
            drawBox(box, red, green, blue, alpha);
        }
    }

    public static void drawBox(Box box, float red, float green, float blue, float alpha) {
        drawBox(box.x1, box.y1, box.z1, box.x2, box.y2, box.z2, red, green, blue, alpha);
    }

    public static void drawBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float red, float green, float blue, float alpha) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(GL11.GL_TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);
        WorldRenderer.drawBox(bufferBuilder, minX, minY, minZ, maxX, maxY, maxZ, red, green, blue, alpha);
        tessellator.draw();
    }

    public static void drawLine(Vec3d start, Vec3d end, float red, float green, float blue, float alpha) {
        Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
        if (camera.isReady()) {
            Vec3d vec3d = camera.getPos().negate();

            drawLine(
                    start.add(vec3d).getX(),
                    start.add(vec3d).getY(),
                    start.add(vec3d).getZ(),
                    end.add(vec3d).getX(),
                    end.add(vec3d).getY(),
                    end.add(vec3d).getZ(),
                    red, green, blue, alpha
            );
        }
    }

    public static void drawLine(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float red, float green, float blue, float alpha) {
        RenderSystem.lineWidth(3.0F);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(GL11.GL_LINE_STRIP, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(minX, minY, minZ).color(red, green, blue, alpha).next();
        bufferBuilder.vertex(maxX, maxY, maxZ).color(red, green, blue, alpha).next();
        tessellator.draw();
        RenderSystem.lineWidth(1.0F);
    }
}
