package me.ivan1f.villagerhelper.renderers;

import me.ivan1f.villagerhelper.config.Configs;
import me.ivan1f.villagerhelper.utils.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.util.math.Vec3d;

public class PoiRenderer implements IRenderer {
    public static PoiRenderer INSTANCE = new PoiRenderer();

    public static PoiRenderer getInstance() {
        return INSTANCE;
    }

    @Override
    public void onWorldRenderLast(float tickDelta) {
        if (!Configs.ENABLE) return;
        MinecraftClient client = MinecraftClient.getInstance();
        ClientWorld world = client.world;
        if (world == null) return;
        for (Entity entity : world.getEntities()) {
            if (entity.squaredDistanceTo(client.player) > Configs.RENDER_DISTANCE * Configs.RENDER_DISTANCE) continue;
            if (entity instanceof VillagerEntity) {
                VillagerEntity villager = (VillagerEntity) entity;
                villager.getBrain().getOptionalMemory(MemoryModuleType.HOME).ifPresent(homePos -> {
                    RenderUtils.drawBox(homePos.getPos(), 0, 0, 1.0F, 0.3F);
                    RenderUtils.drawLine(
                            new Vec3d(homePos.getPos()).add(0.5, 0.5, 0.5),
                            villager.getPos().add(0, villager.getHeight() / 2, 0),
                            0, 0, 1.0F, 1.0F
                    );
                });
                villager.getBrain().getOptionalMemory(MemoryModuleType.JOB_SITE).ifPresent(jobSitePos -> {
                    RenderUtils.drawBox(jobSitePos.getPos(), 1.0F, 0, 0, 0.3F);
                    RenderUtils.drawLine(
                            new Vec3d(jobSitePos.getPos()).add(0.5, 0.5, 0.5),
                            villager.getPos().add(0, villager.getHeight() / 2, 0),
                            1.0F, 0, 0, 1.0F
                    );
                });
            }
        }
    }
}
