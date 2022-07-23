package me.ivan1f.villagerhelper.mixins.server;

import me.ivan1f.villagerhelper.network.ServerNetworkHandler;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.util.GlobalPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

// Server side VillagerEntityMixin
@Mixin(VillagerEntity.class)
public class VillagerEntityMixin {
    /*
     * -----------------------------------
     *    Sync Villager - offers change
     * -----------------------------------
     */

    @Inject(method = "fillRecipes", at = @At("TAIL"))
    private void syncVillagerWhenFillRecipes(CallbackInfo ci) {
        ServerNetworkHandler.updateEntity((VillagerEntity) (Object) this);
    }

    /*
     * -----------------------------------------
     *    Sync Villager - brain memory change
     * -----------------------------------------
     */

    private GlobalPos previousHomePos;
    private GlobalPos previousJobSitePos;

    @Inject(method = "tick", at = @At("RETURN"))
    private void syncVillagerWhenBrainMemoryChange(CallbackInfo ci) {
        VillagerEntity villager = (VillagerEntity) (Object) this;
        GlobalPos homePos = villager.getBrain().getOptionalMemory(MemoryModuleType.HOME).orElse(null);
        GlobalPos jobSitePos = villager.getBrain().getOptionalMemory(MemoryModuleType.JOB_SITE).orElse(null);

        if (!Objects.equals(this.previousHomePos, homePos) || !Objects.equals(this.previousJobSitePos, jobSitePos)) {
            ServerNetworkHandler.updateEntity(villager);
        }

        this.previousHomePos = homePos;
        this.previousJobSitePos = jobSitePos;
    }
}
