package com.tower.defense.network.packet.client;

import com.tower.defense.network.packet.Packet;
import com.tower.defense.network.packet.PacketType;
import org.json.JSONObject;

public class PacketInEndOfGame extends Packet {

    public PacketInEndOfGame() {
        this.packetType = PacketType.getPacketTypeByClass(getClass());
    }

    @Override
    public JSONObject read() {
        JSONObject object = new JSONObject();
        object.put("id", packetType.getPacketID());
        return object;
    }

    @Override
    public void write(JSONObject object) {
        this.packetType = PacketType.getPacketTypeByID(object.getInt("id"));
    }

}
