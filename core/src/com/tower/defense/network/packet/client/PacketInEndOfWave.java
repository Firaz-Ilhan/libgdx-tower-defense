package com.tower.defense.network.packet.client;

import com.tower.defense.network.packet.Packet;
import com.tower.defense.network.packet.PacketType;
import org.json.JSONObject;

public class PacketInEndOfWave extends Packet {

    private int reward;

    public PacketInEndOfWave() {
    }

    public PacketInEndOfWave(int walletValue) {
        this.packetType = PacketType.getPacketTypeByClass(getClass());
        this.reward = walletValue;
    }

    @Override
    public JSONObject read() {
        JSONObject object = new JSONObject();
        object.put("id", packetType.getPacketID());
        object.put("reward", reward);
        return object;
    }

    @Override
    public void write(JSONObject object) {
        this.packetType = PacketType.getPacketTypeByID(object.getInt("id"));
        this.reward = object.getInt("reward");
    }

    public int getReward() {
        return reward;
    }

}