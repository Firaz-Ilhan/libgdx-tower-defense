package com.tower.defense.network.packet.server;

import com.tower.defense.network.packet.Packet;
import com.tower.defense.network.packet.PacketType;
import org.json.JSONObject;

public class PacketOutLifepoints extends Packet {
    private int LP;
    public PacketOutLifepoints() {
    }
    public PacketOutLifepoints(int LP) {
        this.packetType = PacketType.getPacketTypeByClass(getClass());
        this.LP = LP;
    }

    @Override
    public JSONObject read() {
        JSONObject object = new JSONObject();
        object.put("id", packetType.getPacketID());
        object.put("LP", LP);
        return object;
    }

    @Override
    public void write(JSONObject object) {
        this.packetType = PacketType.getPacketTypeByID(object.getInt("id"));
        this.LP = object.getInt("LP");
    }


    public int getLP() {
        return LP;
    }
}