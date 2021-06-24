package com.tower.defense.network.packet.server;

import org.json.JSONObject;

import com.tower.defense.network.packet.Packet;
import com.tower.defense.network.packet.PacketType;

public class PacketMatchFound extends Packet{

	public PacketMatchFound() {
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
