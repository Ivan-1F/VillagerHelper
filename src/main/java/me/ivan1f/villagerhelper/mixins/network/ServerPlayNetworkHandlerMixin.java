package me.ivan1f.villagerhelper.mixins.network;

import me.ivan1f.villagerhelper.network.Network;
import me.ivan1f.villagerhelper.network.ServerNetworkHandler;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
    @Shadow public ServerPlayerEntity player;

    @Inject(method = "onCustomPayload", at = @At("HEAD"), cancellable = true)
    private void onCustomPayload$villagerhelper(CustomPayloadC2SPacket packet, CallbackInfo ci) {
        Identifier channel = ((CustomPayloadC2SPacketAccessor) packet).getChannel();
        if (Network.CHANNEL.equals(channel)) {
            ServerNetworkHandler.handleClientPacket(((CustomPayloadC2SPacketAccessor) packet).getData(), this.player);
            ci.cancel();
        }
    }
}
