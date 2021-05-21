package com.tower.defense.screen;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tower.defense.TowerDefense;
import com.tower.defense.network.client.Client;
import com.tower.defense.network.packet.Packet;
import com.tower.defense.network.packet.PacketType;
import com.tower.defense.network.packet.client.PacketInChatMessage;
import com.tower.defense.network.packet.client.PacketInSearchMatch;
import com.tower.defense.network.packet.server.PacketOutChatMessage;

public class MatchmakingScreen implements Screen {

    private final static Logger log = LogManager.getLogger(MainMenuScreen.class);

    private final Stage stage;
    private final Skin skin;
    private final TowerDefense game;
    private final MatchmakingScreen instance;
    
    private Label connectionStatus;
    private Table scrollTable;
    private ScrollPane scroller;
    private TextField inputArea;

    public MatchmakingScreen(final TowerDefense game) {
        this.game = game;
        skin = game.assetManager.get("skins/glassyui/glassy-ui.json");
        stage = new Stage(new ScreenViewport());
        this.instance = this;
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        final Table connectionTable = new Table();
        connectionTable.setFillParent(true);
        connectionTable.setDebug(false);
        stage.addActor(connectionTable);

        final TextField opponentIPField = new TextField("", skin, "default");
        final Label opponentIPLabel = new Label("Opponent's IP address", skin, "default");

        final Label ownLabel = new Label("Your IP address:", skin, "default");
        final Label ownIPLabel = new Label("placeholder", skin, "default");
        // ownIPLabel.setText("192.168...");
        connectionStatus = new Label("not connected", skin, "default");

        final TextButton connectButton = new TextButton("Connect...", skin, "small");
        final TextButton startGameButton = new TextButton("Start Game...", skin, "small");

        connectionTable.defaults().pad(10f);
        connectionTable.align(Align.top);
        connectionTable.add(opponentIPLabel);
        connectionTable.add(opponentIPField);
        connectionTable.row();
        connectionTable.add(ownLabel);
        connectionTable.add(ownIPLabel);
        connectionTable.row();
        connectionTable.add(connectButton);
        connectionTable.add(connectionStatus);
        connectionTable.row();
        connectionTable.add(startGameButton);

        scrollTable = new Table(skin);
        scrollTable.setDebug(false);
        scrollTable.setFillParent(false);
        scrollTable.align(Align.bottom);

        scroller = new ScrollPane(scrollTable, skin);
        scroller.validate();
        scroller.setScrollingDisabled(true, false);
        scroller.setDebug(false);
        scroller.setFadeScrollBars(false);

        final Table chatTable = new Table(skin);
        chatTable.setFillParent(true);
        chatTable.setDebug(false);
        chatTable.align(Align.bottom);
        chatTable.add(scroller).width(500f).height(300f).colspan(2);

        chatTable.row();

        inputArea = new TextField("", skin, "default");
        final TextButton sendMessageButton = new TextButton("Send", skin, "small");

        chatTable.add(inputArea);
        chatTable.add(sendMessageButton);

        this.stage.addActor(chatTable);

        connectButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            	Client client = new Client();
            	client.setCurrentScreen(instance);
            	game.setClient(client);
            	client.sendPacket(new PacketInSearchMatch());
            	connectionStatus.setText("Connecting...");
            }
        });

        startGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // start the game only when the players are connected
                game.setScreen(new GameScreen(game));
                log.info("set screen to {}", game.getScreen().getClass());
            }
        });

        sendMessageButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            	if(connectionStatus.getText().toString().equals("Connected: Match found")) {
            		final String msg = inputArea.getText();
                	addMessageToBox(true, msg);
                    game.getClient().sendPacket(new PacketInChatMessage(this.toString(), msg));
            	}
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        game.dispose();
    }
    
    private void addMessageToBox(boolean self, String msg) {
        if (!msg.isEmpty()) {
            scrollTable.row();
            final Label msglabel = new Label(msg, skin);
            
            if(self) {
            	msglabel.setAlignment(Align.right);
            	msglabel.setColor(Color.GREEN);
            } else {
            	msglabel.setAlignment(Align.left);
            	msglabel.setColor(Color.GRAY);
            }
            
            msglabel.setWrap(true);
            scrollTable.add(msglabel).expandX().fillX();
        }
        inputArea.setText("");
        scroller.scrollTo(0, 0, 0, 0); // scroll to bottom
    }
    
    public void handle(Packet packet) {
    	PacketType type = packet.getPacketType();
		
		
		switch (type) {
			case PACKETOUTSEARCHMATCH:
				connectionStatus.setText("Connected: Waiting for Enemy");
				break;
			case PACKETOUTMATCHFOUND:
				connectionStatus.setText("Connected: Match found");
				break;
			case PACKETOUTCHATMESSAGE:
				PacketOutChatMessage packetOutChatMessage = (PacketOutChatMessage) packet;
				addMessageToBox(false, packetOutChatMessage.getText());
				break;
			default:
				break;	
				
		}
    }
    
}
