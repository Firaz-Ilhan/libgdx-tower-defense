package com.tower.defense.network.packet.server;

import com.tower.defense.network.packet.Packet;
import com.tower.defense.network.packet.PacketType;
import org.json.JSONObject;

public class PacketOutAddTower extends Packet {

    private float x;
    private float y;

    public PacketOutAddTower() {

    }

    public PacketOutAddTower(float x, float y) {
        this.packetType = PacketType.getPacketTypeByClass(getClass());
        this.x = x;
        this.y = y;
    }

    @Override
    public JSONObject read() {
        JSONObject object = new JSONObject();
        object.put("id", packetType.getPacketID());
        object.put("x", x);
        object.put("y", y);
        return object;
    }

    @Override
    public void write(JSONObject object) {
        this.packetType = PacketType.getPacketTypeByID(object.getInt("id"));
        this.x = object.getInt("x");
        this.y = object.getInt("y");
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}