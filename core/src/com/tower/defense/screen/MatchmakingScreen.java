package com.tower.defense.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tower.defense.TowerDefense;
import com.tower.defense.helper.Constant;
import com.tower.defense.helper.NetworkINTF;
import com.tower.defense.network.client.Client;
import com.tower.defense.network.packet.Packet;
import com.tower.defense.network.packet.PacketType;
import com.tower.defense.network.packet.client.PacketChatMessage;
import com.tower.defense.network.packet.client.PacketStartMatch;
import com.tower.defense.network.packet.server.PacketSearchMatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.UnknownHostException;

import static com.tower.defense.helper.PacketQueue.packetQueue;

public class MatchmakingScreen implements Screen {

    private final static Logger log = LogManager.getLogger(MainMenuScreen.class);
    private final String IP_REGEX = "^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$";
    private final Stage stage;
    private final Skin skin;
    private final TowerDefense game;
    private final MatchmakingScreen instance;

    private Label connectionStatus;
    private Label startingStatus;
    private Table scrollTable;
    private ScrollPane scroller;
    private TextField inputArea;
    private Texture background;
    private Client client;

    private boolean isReady = false;
    private boolean matchFound = false;
    private boolean isWaitingForEnemy = false;

    public MatchmakingScreen(final TowerDefense game) {
        this.game = game;
        skin = game.assetManager.get(Constant.SKIN_PATH);
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
        background = new Texture(Gdx.files.internal("background.png"));
        TextureRegionDrawable region =
                new TextureRegionDrawable(new TextureRegion(background));
        final TextField serverIPField = new TextField("127.0.0.1", skin, "default");
        final Label serverIPLabel = new Label("Server's IP address", skin, "black");

        final Label ownLabel = new Label("Your IP address:", skin, "black");
        final Label ownIPLabel = new Label("placeholder", skin, "black");

        try {
            ownIPLabel.setText(NetworkINTF.getLocalIpAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
            ownIPLabel.setText("could not get local ip");
        }
        connectionStatus = new Label("not connected", skin, "black");
        startingStatus = new Label("", skin, "black");
        final TextButton connectButton = new TextButton("Connect...", skin, "small");
        final TextButton startGameButton = new TextButton("Start Game...", skin, "small");

        connectionTable.defaults().pad(10f);
        connectionTable.align(Align.top);
        connectionTable.add(serverIPLabel);
        connectionTable.add(serverIPField);
        connectionTable.row();
        connectionTable.add(ownLabel);
        connectionTable.add(ownIPLabel);
        connectionTable.row();
        connectionTable.add(connectButton);
        connectionTable.add(connectionStatus);
        connectionTable.row();
        connectionTable.add(startGameButton);
        connectionTable.add(startingStatus);
        connectionTable.background(region);

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

        stage.addActor(chatTable);

        /**
         * if the connectButton is clicked, it takes the text of TextField serverIPField
         * and checks its correctness. If its a correct IPv4 address, it tries
         * to initialize a Client() and creates a connection.
         * The client then sends a PacketInSearchMatch() packet to the server.
         * -> compare GameManager()
         */
        connectButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                final String ip = serverIPField.getText();
                log.debug("IP from Textfield is: {}", ip);
                if (ip.matches(IP_REGEX)) {
                    try {
                        if (!isWaitingForEnemy && !matchFound) {
                            connectionStatus.setText("Trying to connect");
                            client = new Client(ip, Constant.SERVER_PORT);
                            log.info("Creating Client with IP: {}", ip);
                            client.setCurrentScreen(instance);
                            game.setClient(client);
                            client.sendPacket(new PacketSearchMatch());
                            connectionStatus.setText("Connecting...");
                        }
                    } catch (Exception e1) {
                        connectionStatus.setText("could not connect to the server");
                        client = null; // set client to null after failed connection attempt
                    }

                    game.setClient(client);

                } else {
                    connectionStatus.setText("Please enter a correct server IPv4 address,if you're running the server,type your own");
                    log.info("IP from Textfield is: {}(else Block)", ip);
                }
            }
        });

        /**
         * If the startGameButton is clicked, it checks whether
         * the boolean isReady is true or false.
         * is it false it is set to true and a PacketInStartMatch() packet is
         * sent to the server.
         * If the player already got a PacketOutStartMatch() packet isReady must be true,
         * so the game starts and sends a PacketInStartMatch() packet to the other client
         * so they game starts too -> compare handle method
         */
        startGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                if (matchFound) {
                    if (!isReady) {
                        game.getClient().sendPacket(new PacketStartMatch());
                        isReady = true;
                        log.info("isReady is : {}", isReady);
                        startingStatus.setText("You're ready to play");
                    } else {
                        game.getClient().sendPacket(new PacketStartMatch());
                        game.getClient().setCurrentScreen(new GameScreen(game));
                        game.setScreen(game.getClient().getCurrentScreen());
                    }
                } else {
                    if (!isWaitingForEnemy)
                        game.setScreen(new GameScreen(game));
                }
            }
        });

        sendMessageButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (matchFound) {
                    final String msg = inputArea.getText();
                    addMessageToBox(true, msg);
                    game.getClient().sendPacket(new PacketChatMessage(this.toString(), msg));
                }
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.45f, 0.63f, 0.76f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        handlePacketQueue();
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
        background.dispose();
    }

    private void addMessageToBox(boolean self, String msg) {
        if (!msg.isEmpty()) {
            scrollTable.row();
            final Label msgLabel = new Label(msg, skin);

            if (self) {
                msgLabel.setAlignment(Align.right);
                msgLabel.setColor(Color.BLACK);
            } else {
                msgLabel.setAlignment(Align.left);
                msgLabel.setColor(Color.RED);
            }

            msgLabel.setWrap(true);
            scrollTable.add(msgLabel).expandX().fillX();
        }
        inputArea.setText("");
        scroller.scrollTo(0, 0, 0, 0); // scroll to bottom
    }

    /**
     * This Method decides what to do with each Type of packet
     * Most of the time it changes something in the GUI.
     */
    public void handlePacketQueue() {
        if (packetQueue.isEmpty()) {
            return;
        }
        while (!packetQueue.isEmpty()) {
            Packet packet = packetQueue.removeFirst();
            PacketType type = packet.getPacketType();

            log.info("Traffic: New {}", type.toString());

            switch (type) {
                case PACKETENDOFGAME:
                    if (game.getClient() != null) {
                        connectionStatus.setText("Partner lost connection");
                    }
                    break;
                case PACKETSEARCHMATCH:
                    isWaitingForEnemy = true;
                    connectionStatus.setText("Connected: Waiting for Enemy");
                    break;
                case PACKETMATCHFOUND:
                    matchFound = true;
                    connectionStatus.setText("Connected: Match found");
                    break;
                case PACKETCHATMESSAGE:
                    PacketChatMessage packetChatMessage = (PacketChatMessage) packet;
                    addMessageToBox(false, packetChatMessage.getText());
                    break;
                case PACKETSTARTMATCH:
                    //checking if player is ready to start game. If not it is set to true
                    //else the scene changes
                    if (isReady) {
                        Client client = game.getClient();
                        client.setCurrentScreen(new GameScreen(game));
                        game.setScreen(new GameScreen(game));
                    } else {
                        isReady = true;
                        startingStatus.setText("The other player is waiting to get started");
                    }
                    break;
                default:
                    break;

            }
        }
    }
}
