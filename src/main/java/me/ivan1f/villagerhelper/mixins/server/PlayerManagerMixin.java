package me.ivan1f.villagerhelper.mixins.server;

import me.ivan1f.villagerhelper.network.ServerNetworkHandler;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
    @Inject(method = "remove", at = @At("HEAD"))
    private void onPlayerLogout$villagerhelper(ServerPlayerEntity player, CallbackInfo ci) {
        ServerNetworkHandler.moddedPlayers.remove(player);
    }
}
