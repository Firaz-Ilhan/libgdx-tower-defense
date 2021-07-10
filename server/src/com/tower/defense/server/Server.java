package com.tower.defense.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedHashMap;

import com.tower.defense.helper.Constant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Server {

    private final static Logger log = LogManager.getLogger(Server.class);

    private final LinkedHashMap<Integer, ServerConnection> connections = new LinkedHashMap<>();
    private volatile boolean running = true;
    private GameManager gameManager;

    /**
     * while the server is running it always checks for new sockets.
     * Those sockets are used to create a model.ServerConnection
     * which then runs as a separate Thread.
     * All sockets and its ServerConnections are stored in a LinkedHashMap
     */
    public Server() {
        try (ServerSocket serverSocket = new ServerSocket(Constant.SERVER_PORT)) {
            log.info("Starting server on Port {}", Constant.SERVER_PORT);
            this.gameManager = new GameManager();
            while (running) {
                Socket socket = serverSocket.accept();
                log.info("New incoming Connection Port: {}", socket.getPort());
                ServerConnection serverConnection = new ServerConnection(socket, this);
                Thread serverConnectionThread = new Thread(serverConnection, "ServerConnectionThread");
                serverConnectionThread.setDaemon(true);
                serverConnectionThread.start();
                connections.put(socket.getPort(), serverConnection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ServerConnection getServerConnectionByLocalPort(int port) {
        return connections.get(port);
    }

    public GameManager getGameManager() {
        return gameManager;
    }
}
