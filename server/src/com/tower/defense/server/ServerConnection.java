package com.tower.defense.server;

import com.tower.defense.network.packet.Packet;
import com.tower.defense.network.packet.PacketType;
import com.tower.defense.network.packet.server.PacketMatchFound;
import com.tower.defense.network.packet.server.PacketSearchMatch;
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
        ServerConnection partnerConnection;
        log.info("Traffic: New {}", type.toString());

        switch (type) {
            //calls model.GameManager to look for an opponent
            case PACKETSEARCHMATCH:

                ServerConnection serverSearchConnection = server.getGameManager().searchingForGame(this);
                //if null is returned, player must have been matched, so they send a matchFound to the own and to the partners Client
                if (serverSearchConnection == null) {
                    PacketMatchFound packetMatchFound = new PacketMatchFound();
                    sendPacketToClient(packetMatchFound);
                    server.getGameManager().getPartnerConnection(this).sendPacketToClient(packetMatchFound);
                } else {
                    sendPacketToClient(new PacketSearchMatch());
                }
                break;

            default:
                partnerConnection = server.getGameManager().getPartnerConnection(this);

                if (partnerConnection == null) {
                    return;
                }
                partnerConnection.sendPacketToClient(packet);
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
