package me.ivan.villagerhelper;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import me.ivan.villagerhelper.mixin.LivingEntityAccessor;
import me.ivan.villagerhelper.mixin.VillagerEntityMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.client.util.math.Rotation3;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.GlobalPos;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.village.TradeOffer;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class VillagerHelper {
    private static final VillagerHelper INSTANCE = new VillagerHelper();
    public static final MinecraftClient mc = MinecraftClient.getInstance();
    public static final float RENDER_DISTANCE = 256.0F;

    public static VillagerHelper getInstance() {
        return INSTANCE;
    }

    public void renderVillagerInfo(float tickDelta) {
        ServerWorld world = mc.getServer().getWorld(mc.player.dimension);
        Iterator iterator = world.getEntities(EntityType.VILLAGER, entity -> true).iterator();

        while (iterator.hasNext()) {
            Entity entity = (Entity) iterator.next();
            if (entity instanceof VillagerEntity && entity.distanceTo(mc.cameraEntity) <= RENDER_DISTANCE) {
                VillagerEntity villager = (VillagerEntity) entity;
                Vec3d villagerPos = villager.getPos();
                // parse villager offers
                Iterator offers = villager.getOffers().iterator();  // villager's offers
                while (offers.hasNext()) {
                    TradeOffer offer = (TradeOffer) offers.next();  // current villager offer
                    Item sell = offer.getSellItem().getItem();  // sell item
                    if (sell instanceof EnchantedBookItem) {    // enchantment book trade
                        EnchantmentHelper.getEnchantments(offer.getSellItem()).forEach((enchantment, integer) -> {  // all enchantment of the book
                            Item buy1 = offer.getOriginalFirstBuyItem().getItem();
                            Item buy2 = offer.getSecondBuyItem().getItem();
                            int price = -1;
                            if (buy1 == Items.EMERALD) {
                                price = offer.getOriginalFirstBuyItem().getCount();
                            } else if (buy2 == Items.EMERALD) {
                                price = offer.getSecondBuyItem().getCount();
                            }
                            drawString(enchantment.getName(integer).getString(), villagerPos.getX(), villagerPos.getY() + 2.5, villagerPos.getZ(), tickDelta, Formatting.AQUA.getColorValue(), -0.5F);
                            drawString(Integer.toString(price), villagerPos.getX(), villagerPos.getY() + 2.5, villagerPos.getZ(), tickDelta, Formatting.GREEN.getColorValue(), 0.5F);
                        });
                    }
                }
                // bed & job site line
                boolean hasBed = villager.getBrain().getOptionalMemory(MemoryModuleType.HOME).isPresent();
                boolean hasJobSite = villager.getBrain().getOptionalMemory(MemoryModuleType.JOB_SITE).isPresent();
                if (hasJobSite) {
                    BlockPos jobSitePos = villager.getBrain().getOptionalMemory(MemoryModuleType.JOB_SITE).get().getPos();
                    drawLine(villagerPos.getX(), villagerPos.getY() + 1, villagerPos.getZ(), jobSitePos.getX() + 0.5, jobSitePos.getY() + 0.5, jobSitePos.getZ() + 0.5, new Color(Formatting.BLUE.getColorValue()), 5.0F, mc.gameRenderer.getCamera(), tickDelta);
                }
                if (hasBed) {
                    BlockPos bedPos = villager.getBrain().getOptionalMemory(MemoryModuleType.HOME).get().getPos();
                    drawLine(villagerPos.getX(), villagerPos.getY() + 1, villagerPos.getZ(), bedPos.getX() + 0.5, bedPos.getY() + 0.5, bedPos.getZ() + 0.5, new Color(Formatting.RED.getColorValue()), 5.0F, mc.gameRenderer.getCamera(), tickDelta);
                }
            }
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
        if (camera.isReady() && client.getEntityRenderManager().gameOptions != null && client.player != null)
        {
            if (client.player.squaredDistanceTo(x, y, z) > RENDER_DISTANCE * RENDER_DISTANCE)
            {
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
}
