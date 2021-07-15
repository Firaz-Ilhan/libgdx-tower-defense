package com.tower.defense.network.packet;

import org.json.JSONObject;


public abstract class Packet {

    protected PacketType packetType;

    public JSONObject read() {
        JSONObject object = new JSONObject();
        object.put("id", packetType.getPacketID());
        return object;
    }

    public void write(JSONObject object) {
        this.packetType = PacketType.getPacketTypeByID(object.getInt("id"));
    }

    public PacketType getPacketType() {
        return packetType;
    }

}
