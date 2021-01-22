package me.ivan.villagerhelper.mixin;

import io.netty.buffer.Unpooled;
import me.ivan.villagerhelper.network.Network;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Shadow @Final
    Map<DimensionType, ServerWorld> worlds;
    @Shadow
    PlayerManager playerManager;

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        ListTag listTag = new ListTag();

        worlds.forEach((dimensionType, world) -> {
            world.getEntities(EntityType.VILLAGER, entity -> true).forEach(entity -> {
                listTag.add(entity.toTag(new CompoundTag()));
            });
        });

        CompoundTag tag = new CompoundTag();
        tag.put("data", listTag);

        playerManager.getPlayerList().forEach(player -> {
            player.networkHandler.sendPacket(new CustomPayloadS2CPacket(
                    Network.CHANNEL,
                    new PacketByteBuf(Unpooled.buffer())
                            .writeCompoundTag(tag)
            ));
        });
    }
}
