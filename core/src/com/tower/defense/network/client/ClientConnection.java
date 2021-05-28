package com.tower.defense.network.client;

import com.badlogic.gdx.Screen;
import com.tower.defense.network.packet.Packet;
import com.tower.defense.network.packet.PacketType;
import com.tower.defense.screen.MatchmakingScreen;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;


public class ClientConnection implements Runnable {

    private final Socket socket;
    private final Client client;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;
    private boolean running = true;


    public ClientConnection(Socket socket, Client client) throws IOException {
        this.client = client;
        this.socket = socket;
        this.inputStream = new DataInputStream(socket.getInputStream());
        this.outputStream = new DataOutputStream(socket.getOutputStream());
    }

    public void sendPacketToServer(Packet packet) throws IOException {
        outputStream.writeUTF(packet.read().toString());
        outputStream.flush();
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
        } finally {
            closeConnection();
        }
    }

    private void handle(Packet packet) {
        Screen screen = client.getCurrentScreen();
        if (screen instanceof MatchmakingScreen) {
            MatchmakingScreen matchmakingScreen = (MatchmakingScreen) screen;
            matchmakingScreen.handle(packet);
        }

    }

}
