package com.tower.defense.network.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tower.defense.screen.MainMenuScreen;


public class Server {
	
	private final static Logger log = LogManager.getLogger(Server.class);
	
	private final LinkedHashMap<Integer,ServerConnection> connections = new LinkedHashMap<>();
	private boolean running = true;
	private GameManager gameManager;
	private final int port = 3456;
	
	public Server() {
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			log.info("Starting server on Port {}",port);
			this.gameManager = new GameManager();
			while(running) {
				Socket socket = serverSocket.accept();
				log.info("New incoming Connection Port: {}", socket.getPort());
				ServerConnection serverConnection = new ServerConnection(socket,this);
				Thread serverConnectionThread = new Thread(serverConnection, "ServerConnectionThread");
				serverConnectionThread.start();
				connections.put(socket.getPort(),serverConnection);
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
