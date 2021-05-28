package com.tower.defense.network.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.tower.defense.network.packet.Packet;
import com.tower.defense.network.packet.PacketType;
import com.tower.defense.network.packet.client.PacketInChatMessage;
import com.tower.defense.network.packet.server.PacketOutChatMessage;
import com.tower.defense.network.packet.server.PacketOutMatchFound;
import com.tower.defense.network.packet.server.PacketOutSearchMatch;
import com.tower.defense.network.packet.server.PacketOutStartMatch;
import com.tower.defense.screen.MainMenuScreen;



public class ServerConnection extends Thread{
	
	private final static Logger log = LogManager.getLogger(ServerConnection.class);
	
	private Socket socket;
	private Server server;
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	private boolean running = true;
	
	public ServerConnection(Socket socket, Server server) throws IOException{
		super("ServerConnectionThread");
		this.socket = socket;
		this.server = server;
		this.inputStream = new DataInputStream(socket.getInputStream());
		this.outputStream = new DataOutputStream(socket.getOutputStream());
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
			}
		}
		closeConnection();
	}
	
	public void handle(Packet packet) throws IOException {
		PacketType type = packet.getPacketType();
		
		log.info("Traffic: New {}",type.toString());
		
		switch (type) {
			case PACKETINSEARCHMATCH:
				
				ServerConnection serverSearchConnection = server.getGameManager().searchingForGame(this);
				if(serverSearchConnection == null) {
					PacketOutMatchFound packetOutMatchFound = new PacketOutMatchFound();
					sendPacketToClient(packetOutMatchFound);
					server.getGameManager().getPartnerConnection(this).sendPacketToClient(packetOutMatchFound);
				} else {
					sendPacketToClient(new PacketOutSearchMatch());
				}
				break;
			case PACKETINCHATMESSAGE:
				ServerConnection partnerConnection = server.getGameManager().getPartnerConnection(this);
				
				if(partnerConnection == null) {
					return;
				}
				
				PacketInChatMessage packetInChatMessage = (PacketInChatMessage) packet;
				PacketOutChatMessage packetOutChatMessage = new PacketOutChatMessage(packetInChatMessage.getText());
				
				partnerConnection.sendPacketToClient(packetOutChatMessage);
				
				break;
			case PACKETINSTARTMATCH:
				partnerConnection = server.getGameManager().getPartnerConnection(this);
				
				if(partnerConnection == null) {
					return;
				}
				
				PacketOutStartMatch packetOutStartMatch = new PacketOutStartMatch();
				//sendPacketToClient(packetOutStartMatch);
				partnerConnection.sendPacketToClient(packetOutStartMatch);
				
				break;
	
			default:
				break;
		}
		
		
	}
	
	private void sendPacketToClient(Packet packet) throws IOException {
		outputStream.writeUTF(packet.read().toString());
		outputStream.flush();
	}
	
	
	public Socket getSocket() {
		return socket;
	}
	
	

}
