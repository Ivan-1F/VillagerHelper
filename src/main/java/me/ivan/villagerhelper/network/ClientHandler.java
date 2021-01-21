package me.ivan.villagerhelper.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;

public class ClientHandler {
    public static void handleEntityUpdate(CustomPayloadS2CPacket packet) {
        CompoundTag tag =  packet.getData().readCompoundTag();
        System.out.println(tag.contains("Brain"));

    }
}
