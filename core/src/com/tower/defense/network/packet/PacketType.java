package com.tower.defense.network.packet;


import com.tower.defense.network.packet.client.*;
import com.tower.defense.network.packet.server.*;


public enum PacketType {

	//Clientpackets
	PACKETINSEARCHMATCH(1, PacketInSearchMatch.class),
	PACKETINCHATMESSAGE(3, PacketInChatMessage.class),
	
	//Serverpackets
	PACKETOUTSEARCHMATCH(101, PacketOutSearchMatch.class),
	PACKETOUTMATCHFOUND(102, PacketOutMatchFound.class),
	PACKETOUTCHATMESSAGE(103, PacketOutChatMessage.class);
	
	
	
	private int packetID;
	private Class<? extends Packet> packetClass;
	
	private PacketType(int packetID, Class<? extends Packet> packetClass) {
		this.packetID = packetID;
		this.packetClass = packetClass;
	}
	
	
	public static PacketType getPacketTypeByID(int id) {
        for (PacketType pt : values()) {
            if (pt.getPacketID() == id) return pt;
        }
        return null;
    }

    public static PacketType getPacketTypeByClass(Class<? extends Packet> packet) {
        for (PacketType pt : values()) {
            if (pt.getPacketClass().equals(packet)) return pt;
        }
        return null;
    }
    

    public Class<? extends Packet> getPacketClass() {
        return packetClass;
    }

    public int getPacketID() {
        return packetID;
    }
    


}
