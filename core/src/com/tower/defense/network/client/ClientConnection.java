package com.tower.defense.network.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.json.JSONObject;

import com.tower.defense.network.packet.Packet;
import com.tower.defense.network.packet.PacketType;
import com.tower.defense.network.packet.server.PacketOutChatMessage;


public class ClientConnection extends Thread{
	
	private Socket socket;
	private Client client;
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	private boolean running = true;
	
	
	public ClientConnection(Socket socket, Client client) throws IOException {
		super("ClientConnectionThread");
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
		while(running) {
			try {
				while(inputStream.available() == 0) {
					Thread.sleep(1);
				}
				
				JSONObject object = new JSONObject(inputStream.readUTF());
				
				Class<? extends Packet> packetClass = PacketType.getPacketTypeByID(object.getInt("id")).getPacketClass();
				Packet packet = packetClass.newInstance();
				packet.write(object);
				
				handle(packet);
			} catch (Exception e) {
				e.printStackTrace();
				running = false;
				closeConnection();
			}
		}
	}

	private void handle(Packet packet) {
		PacketType type = packet.getPacketType();
		
		
		switch (type) {
			case PACKETOUTSEARCHMATCH:
				System.out.println("Warten auf Match");
				break;
			case PACKETOUTCHATMESSAGE:
				PacketOutChatMessage packetOutChatMessage = (PacketOutChatMessage) packet;
				System.err.println(packetOutChatMessage.getName() + ": " + packetOutChatMessage.getText());
			default:
				break;	
				
		}
		
	}

}
