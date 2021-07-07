package com.tower.defense.helper;

import com.badlogic.gdx.utils.Queue;
import com.tower.defense.network.packet.Packet;

public class PacketQueue {
    //all packets are stored in this queue
    //it is static, so both threats can reach it
    public final static Queue<Packet> packetQueue = new Queue<>();
}
