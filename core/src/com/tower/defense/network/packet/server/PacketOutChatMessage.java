package com.tower.defense.network.packet.server;

import org.json.JSONObject;

import com.tower.defense.network.packet.Packet;
import com.tower.defense.network.packet.PacketType;


public class PacketOutChatMessage extends Packet {
	
	private String text;
	
	public PacketOutChatMessage() {
	}
	
	public PacketOutChatMessage(String text) {
		this.packetType = PacketType.getPacketTypeByClass(getClass());
		this.text = text;
	}
	
	@Override
	public JSONObject read() {
		JSONObject object = new JSONObject();
		object.put("id", packetType.getPacketID());
		object.put("text", text);
		return object;
	}
	
	@Override
	public void write(JSONObject object) {
		this.packetType = PacketType.getPacketTypeByID(object.getInt("id"));
		this.text = object.getString("text");
	}
	
	
	public String getText() {
		return text;
	}
	
	

}
