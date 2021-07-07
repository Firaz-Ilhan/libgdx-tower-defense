package com.tower.defense.network.packet;


import com.tower.defense.network.packet.client.*;
import com.tower.defense.network.packet.server.*;


public enum PacketType {

	//Clientpackets
	PACKETCHATMESSAGE(3, PacketChatMessage.class),
	PACKETSTARTMATCH(4, PacketStartMatch.class),
	PACKETENDOFWAVE(5, PacketEndOfWave.class),
	PACKETLIFEPOINTS(6, PacketLifepoints.class),
	PACKETADDTOWER(7, PacketAddTower.class),
	PACKETREMOVETOWER(8, PacketRemoveTower.class),
	PACKETENDOFGAME(9, PacketEndOfGame.class),
	PACKETINFLUENCE(10,PacketInfluence.class),

	//Serverpackets
	PACKETSEARCHMATCH(101, PacketSearchMatch.class),
	PACKETMATCHFOUND(102, PacketMatchFound.class);


	private int packetID;
	private Class<? extends Packet> packetClass;

	PacketType(int packetID, Class<? extends Packet> packetClass) {
		this.packetID = packetID;
		this.packetClass = packetClass;
	}

	/**
	 * Types are Iterated and id is checked
	 * @param id int
	 * @return packetType or null, if no type found
	 */
	public static PacketType getPacketTypeByID(int id) {
        for (PacketType pt : values()) {
            if (pt.getPacketID() == id) return pt;
        }
        return null;
    }
	/**
	 * Types are Iterated and class is checked
	 * @param packet Class
	 * @return packetType or null, if no type found
	 */
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
