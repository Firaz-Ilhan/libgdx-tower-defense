package com.tower.defense.network.client;

import java.io.IOException;
import java.net.Socket;

import com.badlogic.gdx.Screen;
import com.tower.defense.network.packet.Packet;

public class Client {

	private ClientConnection clientConnection;
	private Screen currentScreen;
	
	public Client() {
		try {
			Socket socket = new Socket("localhost",3456);
			clientConnection = new ClientConnection(socket, this);
			clientConnection.start();
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
	


}
