package me.ivan1f.villagerhelper.network;

import me.ivan1f.villagerhelper.VillagerHelperMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

@Environment(EnvType.CLIENT)
public class ClientNetworkHandler {
    private static boolean serverModded = false;

    public static boolean isServerModded() {
        return serverModded;
    }

    public static void handleServerPacket(PacketByteBuf data, ClientPlayerEntity player) {
        int id = data.readVarInt();
        switch (id) {
            case Network.S2C.HI: {
                String serverModVersion = data.readString();
                VillagerHelperMod.LOGGER.info("Server is installed with mod {} @ {}", VillagerHelperMod.MOD_NAME, serverModVersion);
                serverModded = true;
                break;
            }
            case Network.S2C.ENTITY: {
                handleUpdateEntity(data);
                break;
            }
        }
    }

    public static void sendHiToTheServer(ClientPlayNetworkHandler clientPlayNetworkHandler) {
        serverModded = false;

        clientPlayNetworkHandler.sendPacket(Network.C2S.packet(buf -> buf
                .writeVarInt(Network.C2S.HI)
                .writeString(VillagerHelperMod.VERSION)
        ));
    }

    public static void requestEntityFromServer(int entityId) {
        ClientPlayNetworkHandler networkHandler = MinecraftClient.getInstance().getNetworkHandler();
        if (networkHandler == null) return;

        networkHandler.sendPacket(Network.C2S.packet(buf -> buf
                .writeVarInt(Network.C2S.REQUEST)
                .writeVarInt(entityId)
        ));
    }

    private static void handleUpdateEntity(PacketByteBuf buf) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) {
            return;
        }
        World world = player.world;


        Identifier dimId =
                //#if MC >= 11600
                //$$ world.getRegistryKey().getValue();
                //#else
                DimensionType.getId(world.getDimension().getType());
                //#endif
        if (!buf.readIdentifier().equals(dimId)) return;

        int entityId = buf.readVarInt();
        CompoundTag tag = buf.readCompoundTag();
        Entity entity = world.getEntityById(entityId);

        if (entity != null) {
            assert tag != null;
            if (entity instanceof VillagerEntity) {
                VillagerEntity villager = (VillagerEntity) entity;
                villager.readCustomDataFromTag(tag);
            }
        }
    }
}
