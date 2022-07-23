package me.ivan1f.villagerhelper.mixins.client;

import me.ivan1f.villagerhelper.network.ClientNetworkHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AbstractTraderEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Client side VillagerEntityMixin
@Environment(EnvType.CLIENT)
@Mixin(VillagerEntity.class)
public abstract class VillagerEntityMixin extends AbstractTraderEntity {
    private VillagerProfession oldVillagerProfession;

    public VillagerEntityMixin(EntityType<? extends AbstractTraderEntity> entityType, World world) {
        super(entityType, world);
    }

    /*
     * ---------------------------------------------------------
     *    Request Villager - profession change on client side
     * ---------------------------------------------------------
     */

    @Inject(method = "tick", at = @At(value = "RETURN"))
    private void requestVillagerWhenProfessionChange(CallbackInfo ci) {
        VillagerProfession currentVillagerProfession = ((VillagerEntity) (Object) this).getVillagerData().getProfession();
        if (oldVillagerProfession != currentVillagerProfession) {
            ClientNetworkHandler.requestEntityFromServer(this.getEntityId());
            oldVillagerProfession = currentVillagerProfession;
        }
    }
}
