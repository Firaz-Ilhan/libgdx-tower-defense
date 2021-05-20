package com.tower.defense.network.packet.client;

import org.json.JSONObject;

import com.tower.defense.network.packet.Packet;
import com.tower.defense.network.packet.PacketType;


public class PacketInChatMessage extends Packet {
	
	private String name;
	private String text;
	
	public PacketInChatMessage() {
	}
	
	public PacketInChatMessage(String name, String text) {
		this.packetType = PacketType.getPacketTypeByClass(getClass());
		this.name = name;
		this.text = text;
	}
	
	@Override
	public JSONObject read() {
		JSONObject object = new JSONObject();
		object.put("id", packetType.getPacketID());
		object.put("name", name);
		object.put("text", text);
		return object;
	}
	
	@Override
	public void write(JSONObject object) {
		this.packetType = PacketType.getPacketTypeByID(object.getInt("id"));
		this.name = object.getString("name");
		this.text = object.getString("text");
	}
	
	public String getName() {
		return name;
	}
	
	public String getText() {
		return text;
	}
	
	

}
