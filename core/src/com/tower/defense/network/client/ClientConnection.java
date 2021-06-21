package com.tower.defense.network.client;

import com.badlogic.gdx.Screen;
import com.tower.defense.network.packet.Packet;
import com.tower.defense.network.packet.PacketType;
import com.tower.defense.network.packet.client.PacketInEndOfGame;
import com.tower.defense.screen.GameScreen;
import com.tower.defense.screen.MainMenuScreen;
import com.tower.defense.screen.MatchmakingScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;


public class ClientConnection implements Runnable {
    private final static Logger log = LogManager.getLogger(ClientConnection.class);
    private final Socket socket;
    private final Client client;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;
    private boolean running = true;

    /**
     * @param socket the connection needs to know which socket it is using
     * @param client and which client it belongs to
     * @throws IOException the input and output Streams are initialized, based on the socket
     */
    public ClientConnection(Socket socket, Client client) throws IOException {
        this.client = client;
        this.socket = socket;
        this.inputStream = new DataInputStream(socket.getInputStream());
        this.outputStream = new DataOutputStream(socket.getOutputStream());
    }

    /**
     * writes Packet to sockets outputStream
     *
     * @param packet Packet
     * @throws IOException
     */
    public void sendPacketToServer(Packet packet) throws IOException {
        outputStream.writeUTF(packet.read().toString());
        outputStream.flush();
        log.debug("clientConnection sent packet");
    }

    /**
     * this method closes the Connection by closing Streams and Socket
     */
    public void closeConnection() {
        try {
            sendPacketToServer(new PacketInEndOfGame());
            inputStream.close();
            outputStream.close();
            socket.close();
            log.info("socket closed");
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
        try {
            while (running) {
                while (inputStream.available() == 0) {
                    Thread.sleep(1);
                }

                JSONObject object = new JSONObject(inputStream.readUTF());

                Class<? extends Packet> packetClass = Objects.requireNonNull
                        (PacketType.getPacketTypeByID(object.getInt("id"))).getPacketClass();
                Packet packet = packetClass.getDeclaredConstructor().newInstance();
                packet.write(object);

                handle(packet);
            }
        } catch (Exception e) {
            e.printStackTrace();
            closeConnection();
        }
    }

    /**
     * screens have there own handle() methods,
     * so this method calls the handle method of those screens
     *
     * @param packet packet that was created in run()
     * @throws IOException
     */
    private void handle(Packet packet) {
        Screen screen = client.getCurrentScreen();
        if (screen instanceof MatchmakingScreen) {
            MatchmakingScreen matchmakingScreen = (MatchmakingScreen) screen;
            matchmakingScreen.handle(packet);
        }
        if (screen instanceof GameScreen) {
            GameScreen gameScreen = (GameScreen) screen;
            gameScreen.handle(packet);
        }
    }

}
