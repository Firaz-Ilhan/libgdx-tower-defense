package network;

import com.tower.defense.helper.Constant;
import com.tower.defense.helper.PacketQueue;
import com.tower.defense.network.client.Client;
import com.tower.defense.network.packet.Packet;
import com.tower.defense.network.packet.client.PacketChatMessage;
import com.tower.defense.network.packet.client.PacketEndOfGame;
import com.tower.defense.network.packet.client.PacketInfluence;
import com.tower.defense.network.packet.server.PacketMatchFound;
import com.tower.defense.network.packet.server.PacketSearchMatch;
import com.tower.defense.server.Server;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.lang.Thread.sleep;


public class PacketTest {
    Client client1, client2;

    @BeforeEach
    public void initialize() throws InterruptedException {
        Thread serverThread = new Thread(Server::new);
        serverThread.setDaemon(true);
        serverThread.start();
        client1 = new Client("127.0.0.1", Constant.SERVER_PORT);
        client2 = new Client("127.0.0.1", Constant.SERVER_PORT);

        if (client1 != null) {
            client1.sendPacket(new PacketSearchMatch());
            client2.sendPacket(new PacketSearchMatch());
            //to assure that this packets come first
            sleep(10000);
        }

    }

    @Test
    public void testSearchMatchAndConnection() throws InterruptedException {
        sleep(10000);
        Packet packet = PacketQueue.packetQueue.removeFirst();
        Assertions.assertSame(packet.getClass(), PacketMatchFound.class);

    }

    @Test
    public void testPacketChatMessage() throws InterruptedException {
        client1.sendPacket(new PacketChatMessage("name", "hallo"));
        sleep(10000);
        Packet packet = PacketQueue.packetQueue.removeFirst();
        Assertions.assertSame(packet.getClass(), PacketChatMessage.class);
        Assertions.assertEquals("hallo", ((PacketChatMessage) packet).getText());
    }

    @Test
    public void testPacketInfluence() throws InterruptedException {
        client1.sendPacket(new PacketInfluence());
        sleep(10000);
        Packet packet = PacketQueue.packetQueue.removeFirst();
        Assertions.assertSame(packet.getClass(), PacketInfluence.class);
    }
}
