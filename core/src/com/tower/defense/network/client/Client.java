package com.tower.defense.network.client;

import com.badlogic.gdx.Screen;
import com.tower.defense.network.packet.Packet;

import java.io.IOException;
import java.net.Socket;

public class Client {

    private ClientConnection clientConnection;
    private Screen currentScreen;

    /**
     * When creating a client a socket based on Server IP and Port is created
     * As well as a belonging clientConnection
     * clientConnections also run as a separate Thread
     *
     * @param serverIp   String
     * @param serverPort Integer
     */
    public Client(String serverIp, int serverPort) {
        try {
            Socket socket = new Socket(serverIp, serverPort);
            socket.setSoTimeout(30000);
            clientConnection = new ClientConnection(socket, this);
            Thread clientConnectionThread = new Thread(clientConnection, "ClientConnectionThread");
            clientConnectionThread.setDaemon(true);
            clientConnectionThread.start();
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
