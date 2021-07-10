package com.tower.defense.server;

import java.util.ArrayList;

/**
 * this class holds a list of serverConnection Arrays (called games) and matches single serverConnections
 */
public class GameManager {

    private ServerConnection searchingConnection;
    private ArrayList<ServerConnection[]> games;

    public GameManager() {
        this.searchingConnection = null;
        this.games = new ArrayList<>();
    }

    /**
     * tells the serverConnection if there is already someone searching for a game.
     * If not the connection becomes the searchingConnection
     */


    public ServerConnection searchingForGame(ServerConnection serverConnection) {
        if (searchingConnection == null) {
            searchingConnection = serverConnection;
        } else {
            addGame(searchingConnection, serverConnection);
            searchingConnection = null;
        }

        return searchingConnection;

    }

    public void addGame(ServerConnection player1, ServerConnection player2) {
        games.add(new ServerConnection[]{player1, player2});
    }

    public ServerConnection[] getGame(ServerConnection serverConnection) {
        for (ServerConnection[] game : games) {
            if (game[0] == serverConnection || game[1] == serverConnection) {
                return game;
            }
        }

        return null;
    }

    public ServerConnection getPartnerConnection(ServerConnection serverConnection) {
        ServerConnection[] game = getGame(serverConnection);

        if (game != null) {
            return game[1].equals(serverConnection) ? game[0] : game[1];
        }

        return null;
    }

}
