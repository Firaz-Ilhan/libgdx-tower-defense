package com.tower.defense.network.server;

import com.tower.defense.network.packet.Packet;
import com.tower.defense.network.packet.PacketType;
import com.tower.defense.network.packet.client.PacketInChatMessage;
import com.tower.defense.network.packet.client.PacketInEndOfWave;
import com.tower.defense.network.packet.server.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;


public class ServerConnection extends Thread {

    private final static Logger log = LogManager.getLogger(ServerConnection.class);

    private final Socket socket;
    private final Server server;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;
    private boolean running = true;

    public ServerConnection(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;
        this.inputStream = new DataInputStream(socket.getInputStream());
        this.outputStream = new DataOutputStream(socket.getOutputStream());
    }


    public void closeConnection() {
        try {
            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (running) {
            try {
                while (inputStream.available() == 0) {
                    Thread.sleep(1);
                }

                JSONObject object = new JSONObject(inputStream.readUTF());

                Class<? extends Packet> packetClass = Objects.requireNonNull
                        (PacketType.getPacketTypeByID(object.getInt("id"))).getPacketClass();
                Packet packet = packetClass.getDeclaredConstructor().newInstance();
                packet.write(object);

                handle(packet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void handle(Packet packet) throws IOException {
        PacketType type = packet.getPacketType();

        log.info("Traffic: New {}", type.toString());

        switch (type) {
            case PACKETINSEARCHMATCH:

                ServerConnection serverSearchConnection = server.getGameManager().searchingForGame(this);
                if (serverSearchConnection == null) {
                    PacketOutMatchFound packetOutMatchFound = new PacketOutMatchFound();
                    sendPacketToClient(packetOutMatchFound);
                    server.getGameManager().getPartnerConnection(this).sendPacketToClient(packetOutMatchFound);
                } else {
                    sendPacketToClient(new PacketOutSearchMatch());
                }
                break;
            case PACKETINCHATMESSAGE:
                ServerConnection partnerConnection = server.getGameManager().getPartnerConnection(this);

                if (partnerConnection == null) {
                    return;
                }

                PacketInChatMessage packetInChatMessage = (PacketInChatMessage) packet;
                PacketOutChatMessage packetOutChatMessage = new PacketOutChatMessage(packetInChatMessage.getText());

                partnerConnection.sendPacketToClient(packetOutChatMessage);

                break;
            case PACKETINSTARTMATCH:
                partnerConnection = server.getGameManager().getPartnerConnection(this);

                if (partnerConnection == null) {
                    return;
                }

                PacketOutStartMatch packetOutStartMatch = new PacketOutStartMatch();
                partnerConnection.sendPacketToClient(packetOutStartMatch);

                break;
            case PACKETINENDOFWAVE:
                partnerConnection = server.getGameManager().getPartnerConnection(this);

                if (partnerConnection == null) {
                    return;
                }
                PacketInEndOfWave packetInEndOfWave = (PacketInEndOfWave) packet;
                PacketOutEndOfWave packetOutEndOfWave = new PacketOutEndOfWave(packetInEndOfWave.getReward());
                partnerConnection.sendPacketToClient(packetOutEndOfWave);
                break;
            case PACKETINSTARTWAVE:
                partnerConnection = server.getGameManager().getPartnerConnection(this);

                if (partnerConnection == null) {
                    return;
                }
                PacketOutStartWave packetOutStartWave = new PacketOutStartWave();
                partnerConnection.sendPacketToClient(packetOutStartWave);
                break;
            default:
                break;
        }


    }

    private void sendPacketToClient(Packet packet) throws IOException {
        outputStream.writeUTF(packet.read().toString());
        outputStream.flush();
    }


    public Socket getSocket() {
        return socket;
    }


}
