package me.ivan.villagerhelper.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import me.ivan.villagerhelper.VillagerHelper;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.passive.AbstractTraderEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.util.GlobalPos;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.Arrays;

@Mixin(VillagerEntity.class)
public abstract class VillagerEntityMixin {
    public boolean hasBed;
    @Inject(method = "tick", at = @At("HEAD"))
    private void ontick(CallbackInfo ci) {
        Vec3d pos = new Vec3d(((EntityAccessor) this).getX(), ((EntityAccessor) this).getY(), ((EntityAccessor) this).getZ());
        DimensionType dim = ((EntityAccessor) this).getDimension();
        MinecraftClient mc = VillagerHelper.mc;
        if (pos.squaredDistanceTo(mc.player.getPos()) <= VillagerHelper.RENDER_DISTANCE && dim.equals(mc.player.dimension)) {
            hasBed = ((LivingEntityAccessor) this).getBrain().getOptionalMemory(MemoryModuleType.HOME).isPresent();
            if (hasBed) {
                BlockPos bedPos = ((GlobalPos) ((LivingEntityAccessor) this).getBrain().getOptionalMemory(MemoryModuleType.HOME).get()).getPos();
                drawLine(pos.x, pos.y, pos.z, bedPos.getX(), bedPos.getY(), bedPos.getZ(), new Color(255, 0, 0), 1.0F, mc.gameRenderer.getCamera());
                System.out.println(bedPos.toShortString());
            }
        }
    }

    private static void drawLine(double startX, double startY, double startZ, double endX, double endY, double endZ, Color color, float lineWidth, Camera camera) {
        double cameraX = camera.getPos().getX();
        double cameraY = camera.getPos().getY();
        double cameraZ = camera.getPos().getZ();

        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(GL11.GL_LINE_STRIP, VertexFormats.POSITION_COLOR);
        GlStateManager.lineWidth(lineWidth);

        bufferBuilder.vertex(startX - cameraX, startY - cameraY, startZ - cameraZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).next();
        bufferBuilder.vertex(endX - cameraX, endY - cameraY, endZ - cameraZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).next();

        Tessellator.getInstance().draw();
        GlStateManager.lineWidth(lineWidth);
    }

}
