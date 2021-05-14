package com.tower.defense.network.client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import com.tower.defense.network.packet.client.PacketInChatMessage;
import com.tower.defense.network.packet.client.PacketInSearchMatch;

public class Client {

	private ClientConnection clientConnection;
	
	public Client() {
		try {
			Socket socket = new Socket("localhost",3456);
			clientConnection = new ClientConnection(socket, this);
			clientConnection.start();

			clientConnection.sendPacketToServer(new PacketInSearchMatch());
			
			listenForInput();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	
	private void listenForInput() {
		Scanner scanner = new Scanner(System.in);
		
		while(scanner.hasNext()) {
			try {
				String text = scanner.nextLine();
				clientConnection.sendPacketToServer(new PacketInChatMessage("Client" + hashCode(), text));
			} catch (IOException e) {
			}
		}
	}


}
