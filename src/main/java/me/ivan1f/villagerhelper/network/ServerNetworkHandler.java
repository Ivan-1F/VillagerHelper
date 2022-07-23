package me.ivan1f.villagerhelper.network;

import com.google.common.collect.Lists;
import me.ivan1f.villagerhelper.VillagerHelperMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.world.dimension.DimensionType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ServerNetworkHandler {
    public static final List<ServerPlayerEntity> moddedPlayers = Lists.newArrayList();

    public static void handleClientPacket(PacketByteBuf data, ServerPlayerEntity player) {
        String playerName = player.getName().getString();
        int id = data.readVarInt();
        switch (id) {
            case Network.C2S.HI: {
                String clientModVersion = data.readString(Short.MAX_VALUE);
                VillagerHelperMod.LOGGER.info("Player {} connected with {} @ {}", playerName, VillagerHelperMod.MOD_NAME, clientModVersion);
                player.networkHandler.sendPacket(Network.S2C.packet(buf -> buf.
                        writeVarInt(Network.S2C.HI).
                        writeString(VillagerHelperMod.VERSION)
                ));
                player.getServerWorld()
                        .getEntities(EntityType.VILLAGER, villager -> true)
                        .forEach(villager -> ServerNetworkHandler.updateEntity(player, villager));
                moddedPlayers.add(player);
                break;
            }
            case Network.C2S.REQUEST: {
                int entityId = data.readVarInt();
                Entity entity = player.getServerWorld().getEntityById(entityId);
                if (entity != null) {
                    updateEntity(player, entity);
                }
            }
        }
    }

    public static void updateEntity(@NotNull Entity entity) {
        moddedPlayers.forEach(player -> ServerNetworkHandler.updateEntity(player, entity));
    }

    public static void updateEntity(@NotNull ServerPlayerEntity player, @NotNull Entity entity) {
        player.networkHandler.sendPacket(Network.S2C.packet(buf -> buf
                .writeVarInt(Network.S2C.ENTITY)
                .writeIdentifier(DimensionType.getId(entity.getEntityWorld().getDimension().getType()))
                .writeVarInt(entity.getEntityId())
                .writeCompoundTag(entity.toTag(new CompoundTag()))
        ));
    }
}
