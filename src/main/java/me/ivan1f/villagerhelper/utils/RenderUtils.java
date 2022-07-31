package me.ivan1f.villagerhelper.utils;

import me.ivan1f.villagerhelper.config.Configs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.text.Text;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

//#if MC >= 11500
import net.minecraft.client.util.math.Matrix4f;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.util.math.MatrixStack;
//#else
//$$ import com.mojang.blaze3d.platform.GlStateManager;
//#endif

public class RenderUtils {
    /**
     * Reference: {@link net.minecraft.client.render.entity.EntityRenderer}
     */
    public static void renderTextOnEntity(
            Entity entity, Text text,
            //#if MC >= 11500
            MatrixStack matrices, EntityRenderDispatcher renderManager, VertexConsumerProvider vertexConsumerProvider
            //#else
            //$$ double x, double y, double z
            //#endif
    ) {
        MinecraftClient client = MinecraftClient.getInstance();
        //#if MC >= 11500
        double distance = renderManager.getSquaredDistanceToCamera(entity);
        //#else
        //$$ double distance = client.getEntityRenderManager().getSquaredDistanceToCamera(entity.x, entity.y, entity.z);
        //#endif

        if (distance < Configs.RENDER_DISTANCE * Configs.RENDER_DISTANCE) {
            //#if MC >= 11500
            matrices.push();
            matrices.translate(0.0, entity.getHeight(), 0.0);
            matrices.multiply(renderManager.getRotation());
            matrices.scale(-0.025F, -0.025F, 0.025F);
            //#endif

            //#if MC < 11500
            //$$ GlStateManager.pushMatrix();
            //$$ GlStateManager.translated(x, y + entity.getHeight(), z);
            //$$ GlStateManager.normal3f(0.0F, 1.0F, 0.0F);
            //$$
            //$$ EntityRenderDispatcher entityRenderDispatcher = client.getEntityRenderManager();
            //$$ GlStateManager.rotatef(-entityRenderDispatcher.cameraYaw, 0.0F, 1.0F, 0.0F);
            //$$ GlStateManager.rotatef(entityRenderDispatcher.cameraPitch, 1.0F, 0.0F, 0.0F);
            //$$
            //$$ GlStateManager.scalef(-0.025F, -0.025F, 0.025F);
            //$$ GlStateManager.disableLighting();
            //$$ GlStateManager.depthMask(false);
            //$$
            //$$ GlStateManager.disableDepthTest();
            //$$
            //$$ GlStateManager.enableBlend();
            //$$ GlStateManager.blendFuncSeparate(
            //$$         GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
            //$$ );
            //#endif

            TextRenderer textRenderer = client.textRenderer;

            //#if MC >= 11600
            //$$ float renderX = (float) (-textRenderer.getWidth(text) / 2);
            //#else
            float renderX = (float) (-textRenderer.getStringWidth(text.asFormattedString()) / 2);
            //#endif

            //#if MC >= 11500
            Matrix4f matrix4f = matrices.peek().getModel();
            textRenderer.draw(
                    //#if MC >= 11600
                    //$$ text,
                    //#else
                    text.asFormattedString(),
                    //#endif
                    renderX, 0, 16777215, false, matrix4f, vertexConsumerProvider, true, 0, 15728880
            );
            //#else
            //$$ GlStateManager.enableTexture();
            //$$ GlStateManager.depthMask(true);
            //$$ textRenderer.draw(text.asFormattedString(), renderX, 0, 16777215);
            //#endif

            //#if MC >= 11500
            matrices.pop();
            //#else
            //$$ GlStateManager.enableLighting();
            //$$ GlStateManager.disableBlend();
            //$$ GlStateManager.enableDepthTest();
            //$$ GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            //$$ GlStateManager.popMatrix();
            //#endif
        }
    }


    /**
     * Reference: {@link net.minecraft.client.render.debug.DebugRenderer}
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
        //#if MC >= 11500
        WorldRenderer.drawBox(bufferBuilder, minX, minY, minZ, maxX, maxY, maxZ, red, green, blue, alpha);
        //#else
        //$$ WorldRenderer.buildBox(bufferBuilder, minX, minY, minZ, maxX, maxY, maxZ, red, green, blue, alpha);
        //#endif
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
        //#if MC >= 11500
        RenderSystem.lineWidth(3.0F);
        //#else
        //$$ GlStateManager.lineWidth(3.0F);
        //#endif
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(GL11.GL_LINE_STRIP, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(minX, minY, minZ).color(red, green, blue, alpha).next();
        bufferBuilder.vertex(maxX, maxY, maxZ).color(red, green, blue, alpha).next();
        tessellator.draw();
        //#if MC >= 11500
        RenderSystem.lineWidth(1.0F);
        //#else
        //$$ GlStateManager.lineWidth(1.0F);
        //#endif
    }
}
