package com.tower.defense.network.packet.client;

import com.tower.defense.network.packet.Packet;
import com.tower.defense.network.packet.PacketType;
import org.json.JSONObject;


public class PacketStartMatch extends Packet {


    public PacketStartMatch() {
        this.packetType = PacketType.getPacketTypeByClass(getClass());
    }

    @Override
    public JSONObject read() {
        return super.read();
    }

    @Override
    public void write(JSONObject object) {
        super.write(object);
    }


}
