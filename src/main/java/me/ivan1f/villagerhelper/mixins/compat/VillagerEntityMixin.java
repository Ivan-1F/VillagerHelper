package me.ivan1f.villagerhelper.mixins.compat;

import net.minecraft.entity.passive.VillagerEntity;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import net.fabricmc.api.EnvType;

//#if MC < 11500
//$$ import net.minecraft.entity.EntityType;
//$$ import net.minecraft.entity.passive.AbstractTraderEntity;
//$$ import net.minecraft.nbt.CompoundTag;
//$$ import net.minecraft.server.world.ServerWorld;
//$$ import net.minecraft.world.World;
//$$ import org.spongepowered.asm.mixin.Mutable;
//$$ import org.spongepowered.asm.mixin.Shadow;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$ import org.spongepowered.asm.mixin.injection.Inject;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//#endif

@Environment(EnvType.CLIENT)
@Mixin(VillagerEntity.class)
public abstract class VillagerEntityMixin
        //#if MC < 11500
        //$$ extends AbstractTraderEntity
        //#endif
{
    //#if MC < 11500
    //$$ @Shadow
    //$$ public abstract void reinitializeBrain(ServerWorld world);
    //$$
    //$$ @Mutable
    //$$ @Shadow
    //$$ private int restocksToday;
    //$$
    //$$ public VillagerEntityMixin(EntityType<? extends AbstractTraderEntity> entityType, World world) {
    //$$     super(entityType, world);
    //$$ }
    //$$
    //$$ @Inject(
    //$$         method = "readCustomDataFromTag",
    //$$         at = @At(
    //$$                 value = "INVOKE",
    //$$                 target = "Lnet/minecraft/entity/passive/VillagerEntity;reinitializeBrain(Lnet/minecraft/server/world/ServerWorld;)V",
    //$$                 shift = At.Shift.BEFORE
    //$$         ),
    //$$         cancellable = true
    //$$ )
    //$$ private void stopCastingWorld(CompoundTag tag, CallbackInfo ci) {
    //$$     if (this.world instanceof ServerWorld) {
    //$$         this.reinitializeBrain((ServerWorld) this.world);
    //$$     }
    //$$     this.restocksToday = tag.getInt("RestocksToday");
    //$$     ci.cancel();
    //$$ }
    //#endif
}
