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

    /**
     * @param socket the connection needs to know which socket it is using
     * @param server and which server it belongs to
     * @throws IOException the input and output Streams are initialized, based on the socket
     */

    public ServerConnection(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;
        this.inputStream = new DataInputStream(socket.getInputStream());
        this.outputStream = new DataOutputStream(socket.getOutputStream());
    }

    /**
     * this method closes the Connection by closing Streams and Socket
     */
    public void closeConnection() {
        try {
            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * while the connection is running, it checks the inputStream for incoming packets.
     * While there is no Packet the thread sleeps.
     * Else the StreamContent is read into a Jason Object.
     * To get the Type (Class) of a Packet it uses the Enum PacketType depending on the ID
     * Then a new Instance of this Type of Packet is made. It contains the JasonObject
     * it is then transferred to the handle() Method
     */
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

    /**
     * This Method decides what to do with each Type of packet
     * Most of the time it sends an "out" Type Packet of the same context to the client
     * Every new "IN" Packet Type should be added to this switch case
     *
     * @param packet packet that was created in run()
     * @throws IOException
     */
    public void handle(Packet packet) throws IOException {
        PacketType type = packet.getPacketType();

        log.info("Traffic: New {}", type.toString());

        switch (type) {
            //calls GameManager to look for an opponent
            case PACKETINSEARCHMATCH:

                ServerConnection serverSearchConnection = server.getGameManager().searchingForGame(this);
                //if null is returned, player must have been matched, so they send a matchFound to the own and to the partners Client
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
                log.info("Server connection is: {}", partnerConnection);
                if (partnerConnection == null) {
                    return;
                }
                PacketInEndOfWave packetInEndOfWave = (PacketInEndOfWave) packet;
                PacketOutEndOfWave packetOutEndOfWave = new PacketOutEndOfWave(packetInEndOfWave.getReward());
                partnerConnection.sendPacketToClient(packetOutEndOfWave);
                log.info("packetOutEndOfWave sent");
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

    /**
     * @param packet "Out" Type Packet
     * @throws IOException The packet is written to the Sockets output Stream
     */
    private void sendPacketToClient(Packet packet) throws IOException {
        outputStream.writeUTF(packet.read().toString());
        outputStream.flush();
    }


    public Socket getSocket() {
        return socket;
    }


}
