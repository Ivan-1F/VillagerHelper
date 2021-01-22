package me.ivan.villagerhelper.mixin;

import me.ivan.villagerhelper.network.ClientHandler;
import me.ivan.villagerhelper.network.Network;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Inject(
            method = "onCustomPayload",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onCustomPayload(CustomPayloadS2CPacket packet, CallbackInfo ci) {
        synchronized (Network.sync) {
            if (((CustomPayloadS2CPacketAccessor) packet).getChannel().toString().equals(Network.CHANNEL.toString())) {
                ClientHandler.handleEntityUpdate(packet);
                ci.cancel();
            }
        }
    }
}
