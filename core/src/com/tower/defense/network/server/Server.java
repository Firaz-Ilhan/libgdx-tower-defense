package com.tower.defense.network.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedHashMap;


public class Server {
	
	private ServerSocket serverSocket;
	private LinkedHashMap<Integer,ServerConnection> connections = new LinkedHashMap<>();
	private boolean running = true;
	private GameManager gameManager;
	
	public Server() {
		try {
			this.serverSocket = new ServerSocket(3456);
			this.gameManager = new GameManager();
			while(running) {
				Socket socket = serverSocket.accept();
				ServerConnection serverConnection = new ServerConnection(socket,this);
				serverConnection.start();
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
