package me.ivan.villagerhelper;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import me.ivan.villagerhelper.utils.CompoundTagParser;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.client.util.math.Rotation3;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Formatting;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.dimension.DimensionType;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class VillagerHelper {
    public static final String MOD_ID = "villagerhelper";

    private static final VillagerHelper INSTANCE = new VillagerHelper();
    public static final MinecraftClient mc = MinecraftClient.getInstance();
    public static final float RENDER_DISTANCE = 256.0F;
    public static boolean enable = false;

    public static Queue<CompoundTag> tagQueue = new LinkedList<>();

    // Data
    private static Pair<String, Integer> enchantmentBookTrade;
    private static Vec3d villagerPos;
    private static BlockPos home;
    private static BlockPos jobSite;

    private static ListTag listTag;

    public static VillagerHelper getInstance() {
        return INSTANCE;
    }

    public void renderVillagerInfo(float tickDelta) {
        if (!enable) return;

        if (!tagQueue.isEmpty()) listTag = tagQueue.poll().getList("data", 10);

        Iterator tagIterator = listTag.iterator();
        int tagPos = 0;
        while (tagIterator.hasNext()) {
            CompoundTag tag = listTag.getCompound(tagPos);
            DimensionType dimension = DimensionType.byRawId(tag.getInt("Dimension"));
            if (mc.player.dimension != dimension) continue;

            // prepare renderer
            GlStateManager.disableTexture();
            GlStateManager.enableBlend();

            // Get villager data
            enchantmentBookTrade = CompoundTagParser.getFirstEnchantmentBookTrade(tag);
            villagerPos = CompoundTagParser.getPos(tag);
            home = CompoundTagParser.getHome(tag);
            jobSite = CompoundTagParser.getJobSite(tag);

            // render data
            if (enchantmentBookTrade != null) {
                drawString(enchantmentBookTrade.getLeft(), villagerPos.getX(), villagerPos.getY() + 2.5, villagerPos.getZ(), tickDelta, Formatting.AQUA.getColorValue(), -0.5F);
                drawString(enchantmentBookTrade.getRight().toString(), villagerPos.getX(), villagerPos.getY() + 2.5, villagerPos.getZ(), tickDelta, Formatting.GREEN.getColorValue(), 0.5F);
            }

            if (home != null) {
                drawLine(villagerPos.getX(), villagerPos.getY() + 1, villagerPos.getZ(), home.getX() + 0.5, home.getY() + 0.5, home.getZ() + 0.5, new Color(Formatting.RED.getColorValue()), 5.0F, mc.gameRenderer.getCamera(), tickDelta);
                drawBoxOutlined(home.getX(), home.getY(), home.getZ(), home.getX() + 1, home.getY() + 0.6, home.getZ() + 1, new Color(Formatting.RED.getColorValue()), 5.0F, mc.gameRenderer.getCamera());
            }
            if (jobSite != null) {
                drawLine(villagerPos.getX(), villagerPos.getY() + 1, villagerPos.getZ(), jobSite.getX() + 0.5, jobSite.getY() + 0.5, jobSite.getZ() + 0.5, new Color(Formatting.BLUE.getColorValue()), 5.0F, mc.gameRenderer.getCamera(), tickDelta);
                drawBoxOutlined(jobSite.getX(), jobSite.getY(), jobSite.getZ(), jobSite.getX() + 1, jobSite.getY() + 1, jobSite.getZ() + 1, new Color(Formatting.BLUE.getColorValue()), 5.0F, mc.gameRenderer.getCamera());
            }

            tagIterator.next();
            tagPos ++;
        }
    }

    private static void drawLine(BlockPos start, BlockPos end, Color color, float lineWidth, Camera camera, float tickDelta) {
        drawLine(start.getX(), start.getY(), start.getZ(), end.getX(), end.getY(), end.getZ(), color, lineWidth, camera, tickDelta);
    }

    private static void drawLine(double startX, double startY, double startZ, double endX, double endY, double endZ, Color color, float lineWidth, Camera camera, float tickDelta) {
        double cameraX = camera.getPos().getX();
        double cameraY = camera.getPos().getY();
        double cameraZ = camera.getPos().getZ();

        GlStateManager.disableTexture();
        GlStateManager.enableBlend();
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(GL11.GL_LINE_STRIP, VertexFormats.POSITION_COLOR);
        GlStateManager.lineWidth(lineWidth);

        bufferBuilder.vertex(startX - cameraX, startY - cameraY, startZ - cameraZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).next();
        bufferBuilder.vertex(endX - cameraX, endY - cameraY, endZ - cameraZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).next();

        Tessellator.getInstance().draw();
        GlStateManager.lineWidth(lineWidth);
    }

    public static void drawString(String text, BlockPos pos, float tickDelta, int color, float line) {
        drawString(text, pos.getX(), pos.getY(), pos.getZ(), tickDelta, color, line);
    }
    public static void drawString(String text, Vec3d pos, float tickDelta, int color, float line) {
        drawString(text, pos.getX(), pos.getY(), pos.getZ(), tickDelta, color, line);
    }
    public static void drawString(String text, double x, double y, double z, float tickDelta, int color, float line) {
        MinecraftClient client = MinecraftClient.getInstance();
        Camera camera = client.gameRenderer.getCamera();
        if (camera.isReady() && client.getEntityRenderManager().gameOptions != null && client.player != null) {
            if (client.player.squaredDistanceTo(x, y, z) > RENDER_DISTANCE * RENDER_DISTANCE) {
                return;
            }
            double camX = camera.getPos().x;
            double camY = camera.getPos().y;
            double camZ = camera.getPos().z;
            RenderSystem.pushMatrix();
            RenderSystem.translatef((float)(x - camX), (float)(y - camY), (float)(z - camZ));
            RenderSystem.normal3f(0.0F, 1.0F, 0.0F);
            RenderSystem.multMatrix(new Matrix4f(camera.getRotation()));
            RenderSystem.scalef(0.025F, -0.025F, 0.025F);
            RenderSystem.enableTexture();
            RenderSystem.disableDepthTest();  // visibleThroughObjects
            RenderSystem.depthMask(true);
            RenderSystem.scalef(-1.0F, 1.0F, 1.0F);
            RenderSystem.enableAlphaTest();

            VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
            float renderX = -client.textRenderer.getStringWidth(text) * 0.5F;
            float renderY = client.textRenderer.getStringBoundedHeight(text, Integer.MAX_VALUE) * (-0.5F + 1.25F * line);
            Matrix4f matrix4f = Rotation3.identity().getMatrix();
            client.textRenderer.draw(text, renderX, renderY, color, false, matrix4f, immediate, true, 0, 0xF000F0);
            immediate.draw();

            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.enableDepthTest();
            RenderSystem.popMatrix();
        }
    }

    public static void drawBoxOutlined(double startX, double startY, double startZ, double endX, double endY, double endZ, Color color, float lineWidth, Camera camera) {
        double cameraX = camera.getPos().getX();
        double cameraY = camera.getPos().getY();
        double cameraZ = camera.getPos().getZ();

        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(GL11.GL_LINE_STRIP, VertexFormats.POSITION_COLOR);
        GlStateManager.lineWidth(lineWidth);

        bufferBuilder.vertex(startX - cameraX, startY - cameraY, startZ - cameraZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).next();
        bufferBuilder.vertex(startX - cameraX, startY - cameraY, startZ - cameraZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).next();
        bufferBuilder.vertex(endX - cameraX, startY - cameraY, startZ - cameraZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).next();
        bufferBuilder.vertex(endX - cameraX, startY - cameraY, endZ - cameraZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).next();
        bufferBuilder.vertex(startX - cameraX, startY - cameraY, endZ - cameraZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).next();
        bufferBuilder.vertex(startX - cameraX, startY - cameraY, startZ - cameraZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).next();
        bufferBuilder.vertex(startX - cameraX, endY - cameraY, startZ - cameraZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).next();
        bufferBuilder.vertex(endX - cameraX, endY - cameraY, startZ - cameraZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).next();
        bufferBuilder.vertex(endX - cameraX, endY - cameraY, endZ - cameraZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).next();
        bufferBuilder.vertex(startX - cameraX, endY - cameraY, endZ - cameraZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).next();
        bufferBuilder.vertex(startX - cameraX, endY - cameraY, startZ - cameraZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).next();
        bufferBuilder.vertex(startX - cameraX, endY - cameraY, endZ - cameraZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).next();
        bufferBuilder.vertex(startX - cameraX, startY - cameraY, endZ - cameraZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).next();
        bufferBuilder.vertex(endX - cameraX, startY - cameraY, endZ - cameraZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).next();
        bufferBuilder.vertex(endX - cameraX, endY - cameraY, endZ - cameraZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).next();
        bufferBuilder.vertex(endX - cameraX, endY - cameraY, startZ - cameraZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).next();
        bufferBuilder.vertex(endX - cameraX, startY - cameraY, startZ - cameraZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).next();
        bufferBuilder.vertex(endX - cameraX, startY - cameraY, startZ - cameraZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).next();

        Tessellator.getInstance().draw();
        GlStateManager.lineWidth(1.0f);
    }

}
