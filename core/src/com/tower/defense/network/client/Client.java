package com.tower.defense.network.client;

import com.badlogic.gdx.Screen;
import com.tower.defense.network.packet.Packet;

import java.io.IOException;
import java.net.Socket;

public class Client {

    private ClientConnection clientConnection;
    private Screen currentScreen;

    public Client() {
        try {
            Socket socket = new Socket("localhost", 3456);
            socket.setSoTimeout(30000);
            clientConnection = new ClientConnection(socket, this);
            Thread thread = new Thread(clientConnection, "ClientConnectionThread");
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendPacket(Packet packet) {
        try {
            clientConnection.sendPacketToServer(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Screen getCurrentScreen() {
        return currentScreen;
    }

    public void setCurrentScreen(Screen currentScreen) {
        this.currentScreen = currentScreen;
    }

    public ClientConnection getClientConnection() {
        return clientConnection;
    }
}
