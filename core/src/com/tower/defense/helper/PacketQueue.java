package com.tower.defense.helper;

import com.badlogic.gdx.utils.Queue;
import com.tower.defense.network.packet.Packet;

public class PacketQueue {
    public final static Queue<Packet> packetQueue = new Queue<>();
}
